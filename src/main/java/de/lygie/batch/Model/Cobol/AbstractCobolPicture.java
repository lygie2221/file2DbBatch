package de.lygie.batch.Model.Cobol;

import org.apache.commons.lang3.StringUtils;

/**
 * Da COBOL-Pictures eine zur Compilezeit festgelegte Länge haben,
 * die die Behandlung nicht verwendeter
 */
public class AbstractCobolPicture {

    protected int length;

    public AbstractCobolPicture(int length){
        this.length = length;
    }

    public int getLength() {
        return length;
    }

    /**
     * @param input Zeichenkette
     * @param length anzahl der anzuhängenden Zeichen
     * @return
     *
     * die Funktion PAD-Right ist vermutlich nur bei PICX sinnvoll einzusetzen.
     * PIC9, also dezimalzahlen verändern bei Padding nach rechts ihren Wert und sind
     * in COBOL auch linksbündig ausgerichtet.
     */
    public static String padRight(String input, int length) {
        if (input == null) {
            input = "";
        }
        if (input.length() >= length) {
            return input;
        }
        StringBuilder sb = new StringBuilder(input);
        while (sb.length() < length) {
            sb.append(' ');
        }
        return sb.toString();
    }

    /**
     * in Cobol werden numerische Werte oft mit low-Value vorbelegt.
     * Mathematisch erscheint mir das Vorgehen auch mit Blick auf Plattformunabhängigkeit gefährlich.
     * Mathematisch korrekt und damit unabhängig vom Ökosystem ist das Auffüllen der Werte mit führenden nullen
     * @param s
     * @param n
     * @return
     */
    public static String padLeft(String s, int n) {
        return StringUtils.leftPad(s, n, '0');
    }

    public static String padLeft(String s, int n, String padChar) {
        return StringUtils.leftPad(s, n, padChar);
    }

}
