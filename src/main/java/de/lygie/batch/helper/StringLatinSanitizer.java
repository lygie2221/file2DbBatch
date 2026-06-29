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
 * Sanitizer fuer Zeichenketten nach DIN 91379 / String.Latin.
 *
 * <p>Die erlaubten Zeichen und Sequenzen werden aus der Originaldatei
 * {@code latin_list_search_form_1.3.txt} geladen. Fuer die Pruefung werden
 * ausschliesslich die Codepoints aus der Datei verwendet.</p>
 */
public final class StringLatinSanitizer {

    private static final String DEFAULT_RESOURCE =
            "/din91379/latin_list_search_form_1.3.txt";

    private final Set<String> allowedValues;

    private final int maxAllowedLength;

    /**
     * Erstellt einen Sanitizer mit der Standard-Resource.
     */
    public StringLatinSanitizer() {
        this(DEFAULT_RESOURCE);
    }

    /**
     * Erstellt einen Sanitizer mit einer frei waehlbaren Resource.
     *
     * @param resourceName Resource im Klassenpfad
     */
    public StringLatinSanitizer(final String resourceName) {
        this.allowedValues = loadAllowedValues(resourceName);
        this.maxAllowedLength = calculateMaxLength(allowedValues);
    }

    /**
     * Prueft und ersetzt nicht erlaubte Zeichen.
     *
     * @param input Eingabetext
     * @param replacement Ersatztext, oder {@code null}, wenn nichts ersetzt
     *                    werden soll
     * @return Ergebnis der Pruefung
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

        final StringBuilder output = new StringBuilder(normalized.length());
        boolean found = false;

        int index = 0;

        while (index < normalized.length()) {
            final String match = findLongestMatch(normalized, index);

            if (match != null) {
                output.append(match);
                index += match.length();
            } else {
                found = true;

                final int codePoint = normalized.codePointAt(index);

                if (replacement == null) {
                    output.appendCodePoint(codePoint);
                } else {
                    output.append(replacement);
                }

                index += Character.charCount(codePoint);
            }
        }

        return new Result(found, output.toString());
    }

    /**
     * Prueft, ob ein nicht erlaubtes Zeichen enthalten ist.
     *
     * @param input Eingabetext
     * @return {@code true}, wenn mindestens ein nicht erlaubtes Zeichen
     *         enthalten ist
     */
    public boolean containsNonStringLatin(final String input) {
        return replaceNonStringLatin(input, null).containsNonStringLatin();
    }

    private String findLongestMatch(final String text, final int startIndex) {
        final int maxEnd = Math.min(
                text.length(),
                startIndex + maxAllowedLength);

        for (int end = maxEnd; end > startIndex; end--) {
            final String candidate = text.substring(startIndex, end);

            if (allowedValues.contains(candidate)) {
                return candidate;
            }
        }

        return null;
    }

    private static Set<String> loadAllowedValues(final String resourceName) {
        final InputStream inputStream =
                StringLatinSanitizer.class.getResourceAsStream(resourceName);

        if (inputStream == null) {
            throw new IllegalStateException(
                    "Resource nicht gefunden: " + resourceName);
        }

        final Set<String> result = new HashSet<String>();

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(inputStream, "UTF-8"))) {

            String line;

            while ((line = reader.readLine()) != null) {
                parseLine(line, result);
            }

        } catch (IOException e) {
            throw new IllegalStateException(
                    "Resource konnte nicht gelesen werden: " + resourceName,
                    e);
        }

        return Collections.unmodifiableSet(result);
    }

    private static void parseLine(
            final String line,
            final Set<String> target) {

        if (line == null || line.trim().isEmpty()) {
            return;
        }

        final String[] parts = line.split(";", -1);

        int index = 0;

        while (index + 4 < parts.length) {
            final String type = parts[index + 1].trim();
            final String codePoints = parts[index + 2].trim();

            if ("char".equals(type) || "seq".equals(type)) {
                target.add(toStringFromCodePoints(codePoints));
            }

            index += 5;

            if (index < parts.length
                    && "deprecated".equals(parts[index].trim())) {
                index++;
            }
        }
    }

    private static String toStringFromCodePoints(final String value) {
        final StringBuilder result = new StringBuilder();

        final String[] codePoints = value.trim().split("\\s+");

        for (String codePoint : codePoints) {
            if (!codePoint.isEmpty()) {
                final int number = Integer.parseInt(codePoint, 16);
                result.appendCodePoint(number);
            }
        }

        return Normalizer.normalize(result.toString(), Normalizer.Form.NFC);
    }

    private static int calculateMaxLength(final Set<String> values) {
        int max = 0;

        for (String value : values) {
            max = Math.max(max, value.length());
        }

        return max;
    }

    /**
     * Ergebnis einer String-Latin-Pruefung.
     */
    public static final class Result {

        private final boolean containsNonStringLatin;

        private final String text;

        private Result(
                final boolean containsNonStringLatin,
                final String text) {
            this.containsNonStringLatin = containsNonStringLatin;
            this.text = text;
        }

        /**
         * Gibt zurueck, ob nicht erlaubte Zeichen gefunden wurden.
         *
         * @return {@code true}, wenn nicht erlaubte Zeichen gefunden wurden
         */
        public boolean containsNonStringLatin() {
            return containsNonStringLatin;
        }

        /**
         * Gibt den bereinigten Text zurueck.
         *
         * @return Ergebnistext
         */
        public String getText() {
            return text;
        }
    }
}