package de.lygie.batch.helper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.Normalizer;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Prüft und bereinigt Zeichenketten gegen eine Positivliste erlaubter
 * String-Latin-Zeichen und -Sequenzen.
 *
 * <p>Die erlaubten Zeichen werden aus einer UTF-8-Ressource geladen. Jede
 * nicht-leere Zeile der Datei beschreibt genau ein erlaubtes Zeichen oder eine
 * erlaubte Zeichenfolge. Kommentarzeilen beginnen mit {@code #}.</p>
 *
 * <p>Die Eingabe wird vor der Prüfung nach NFC normalisiert.</p>
 */
public final class StringLatinSanitizer {

    /**
     * Standard-Ressource mit erlaubten String-Latin-Zeichen.
     */
    private static final String DEFAULT_RESOURCE = "/string-latin-allowed.txt";

    /**
     * Explizite Notation für ein Leerzeichen in der Ressourcendatei.
     */
    private static final String SPACE_TOKEN = "\\s";

    /**
     * Unveränderliche Menge der erlaubten Zeichen und Sequenzen.
     */
    private final Set<String> allowed;

    /**
     * Maximale Länge eines erlaubten Eintrags in Java-{@code char}-Einheiten.
     */
    private final int maxAllowedLengthInChars;

    /**
     * Erstellt einen Sanitizer mit der Standard-Ressource
     * {@value #DEFAULT_RESOURCE}.
     */
    public StringLatinSanitizer() {
        this(DEFAULT_RESOURCE);
    }

    /**
     * Erstellt einen Sanitizer mit der angegebenen Ressource.
     *
     * @param resourceName Name der Ressource im Klassenpfad
     */
    public StringLatinSanitizer(final String resourceName) {
        this.allowed = loadAllowedCharacters(resourceName);
        this.maxAllowedLengthInChars = calculateMaxLength(this.allowed);
    }

    /**
     * Ersetzt alle nicht erlaubten String-Latin-Zeichen.
     *
     * <p>Ist {@code replacement} {@code null}, wird nichts ersetzt. Das Ergebnis
     * enthält dann den normalisierten Originaltext, aber weiterhin die
     * Information, ob nicht erlaubte Zeichen gefunden wurden.</p>
     *
     * @param input zu prüfende Zeichenkette
     * @param replacement Ersatzzeichenkette oder {@code null}
     * @return Ergebnis der Prüfung und Bereinigung
     */
    public Result replaceNonStringLatin(
            final String input,
            final String replacement) {

        if (input == null) {
            return new Result(false, null);
        }

        final String normalized = Normalizer.normalize(
                input,
                Normalizer.Form.NFC);

        final StringBuilder out = new StringBuilder(normalized.length());
        boolean found = false;

        int index = 0;

        while (index < normalized.length()) {
            final Match match = findLongestAllowedMatch(normalized, index);

            if (match != null) {
                out.append(match.getValue());
                index += match.getValue().length();
            } else {
                found = true;

                final int codePoint = normalized.codePointAt(index);

                if (replacement != null) {
                    out.append(replacement);
                } else {
                    out.appendCodePoint(codePoint);
                }

                index += Character.charCount(codePoint);
            }
        }

        return new Result(found, out.toString());
    }

    /**
     * Prüft, ob die Zeichenkette mindestens ein nicht erlaubtes Zeichen
     * enthält.
     *
     * @param input zu prüfende Zeichenkette
     * @return {@code true}, wenn mindestens ein nicht erlaubtes Zeichen vorkommt
     */
    public boolean containsNonStringLatin(final String input) {
        return replaceNonStringLatin(input, null).containsNonStringLatin();
    }

    /**
     * Sucht ab der angegebenen Position die längste erlaubte Zeichenfolge.
     *
     * @param text normalisierte Eingabe
     * @param startIndex Startposition
     * @return Treffer oder {@code null}
     */
    private Match findLongestAllowedMatch(
            final String text,
            final int startIndex) {

        final int maxEnd = Math.min(
                text.length(),
                startIndex + maxAllowedLengthInChars);

        for (int end = maxEnd; end > startIndex; end--) {
            final String candidate = text.substring(startIndex, end);

            if (allowed.contains(candidate)) {
                return new Match(candidate);
            }
        }

        return null;
    }

    /**
     * Lädt die erlaubten Zeichen aus einer UTF-8-Ressource.
     *
     * <p>Leere Zeilen und Kommentarzeilen werden ignoriert. Die Zeichenfolge
     * {@code \s} steht für ein einzelnes Leerzeichen.</p>
     *
     * @param resourceName Name der Ressource im Klassenpfad
     * @return unveränderliche Menge erlaubter Zeichen
     */
    private static Set<String> loadAllowedCharacters(
            final String resourceName) {

        final InputStream in =
                StringLatinSanitizer.class.getResourceAsStream(resourceName);

        if (in == null) {
            throw new IllegalStateException(
                    "Resource nicht gefunden: " + resourceName);
        }

        final Set<String> result = new HashSet<String>();

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(in, "UTF-8"))) {

            String line;

            while ((line = reader.readLine()) != null) {
                final String value = parseResourceLine(line);

                if (value == null) {
                    continue;
                }

                result.add(Normalizer.normalize(value, Normalizer.Form.NFC));
            }

        } catch (IOException e) {
            throw new IllegalStateException(
                    "Resource konnte nicht gelesen werden: " + resourceName,
                    e);
        }

        return Collections.unmodifiableSet(result);
    }

    /**
     * Interpretiert eine Zeile der Ressourcendatei.
     *
     * @param line Rohzeile aus der Ressource
     * @return Zeichenfolge oder {@code null}, wenn die Zeile zu ignorieren ist
     */
    private static String parseResourceLine(final String line) {
        if (line == null) {
            return null;
        }

        if (line.trim().isEmpty()) {
            return null;
        }

        if (line.trim().startsWith("#")) {
            return null;
        }

        if (SPACE_TOKEN.equals(line)) {
            return " ";
        }

        return line;
    }

    /**
     * Berechnet die maximale Länge eines Eintrags.
     *
     * @param values erlaubte Zeichen und Sequenzen
     * @return maximale Länge in Java-{@code char}-Einheiten
     */
    private static int calculateMaxLength(final Set<String> values) {
        int max = 0;

        for (String value : values) {
            max = Math.max(max, value.length());
        }

        return max;
    }

    /**
     * Interner Treffer einer erlaubten Zeichenfolge.
     */
    private static final class Match {

        /**
         * Gefundene Zeichenfolge.
         */
        private final String value;

        /**
         * Erstellt einen Treffer.
         *
         * @param value gefundene Zeichenfolge
         */
        private Match(final String value) {
            this.value = value;
        }

        /**
         * Gibt die gefundene Zeichenfolge zurück.
         *
         * @return gefundene Zeichenfolge
         */
        private String getValue() {
            return value;
        }
    }

    /**
     * Ergebnis einer String-Latin-Prüfung.
     */
    public static final class Result {

        /**
         * Gibt an, ob mindestens ein nicht erlaubtes Zeichen gefunden wurde.
         */
        private final boolean containsNonStringLatin;

        /**
         * Bereinigter oder unveränderter Text.
         */
        private final String text;

        /**
         * Erstellt ein Ergebnis.
         *
         * @param containsNonStringLatin {@code true}, wenn nicht erlaubte
         *                               Zeichen gefunden wurden
         * @param text Ergebnistext
         */
        private Result(
                final boolean containsNonStringLatin,
                final String text) {
            this.containsNonStringLatin = containsNonStringLatin;
            this.text = text;
        }

        /**
         * Gibt zurück, ob nicht erlaubte Zeichen gefunden wurden.
         *
         * @return {@code true}, wenn nicht erlaubte Zeichen gefunden wurden
         */
        public boolean containsNonStringLatin() {
            return containsNonStringLatin;
        }

        /**
         * Gibt den Ergebnistext zurück.
         *
         * @return Ergebnistext
         */
        public String getText() {
            return text;
        }
    }
}
