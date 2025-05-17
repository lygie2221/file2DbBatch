package de.lygie.batch.Model.Cobol;

import java.math.BigInteger;

/**
 * Represents a PIC9 field in a COBOL program.
 *
 * @author Your Name
 */
public class Pic9 extends AbstractCobolPicture {


    private int anzahlnachkommastellen = 0;
    private int wertnachkommastellen = 0;
    private BigInteger value; //ganzzahlbereich
    private String decimal = ",";

    public Pic9(int length, int anzahlnachkommastellen){
        super(length);
        this.anzahlnachkommastellen = anzahlnachkommastellen;
    }


    public Pic9(int length) {
        super(length);
    }



    public BigInteger setValue(int value){

        this.value = new BigInteger(String.valueOf(value));

        return this.value;
    }

    public BigInteger getValue(){
        return value;
    }

    public void setValue(String svalue) {
        if (svalue == null || svalue.isEmpty()) {
            throw new IllegalArgumentException("Input string cannot be null or empty");
        }

        // Entferne das Dezimaltrennzeichen (nur das erste Vorkommen) und nicht-Ziffern
        String cleaned = svalue.replaceFirst(java.util.regex.Pattern.quote(decimal), "");
        cleaned = cleaned.replaceAll("[^0-9]", "");

        // Wenn der String zu lang ist, kürze von links
        if (cleaned.length() > length) {
            cleaned = cleaned.substring(cleaned.length() - length);
        }

        // Splitten in Ganzzahlteil und Nachkommastellen
        int splitIndex = cleaned.length() - anzahlnachkommastellen;

        if (splitIndex < 0) {
            // zu wenige Stellen, alles Nachkommastellen
            value = BigInteger.ZERO;
            wertnachkommastellen = Integer.parseInt(cleaned);
        } else {
            String intPart = cleaned.substring(0, splitIndex);
            String decimalPart = cleaned.substring(splitIndex);

            value = new BigInteger(intPart.isEmpty() ? "0" : intPart);
            wertnachkommastellen = decimalPart.isEmpty() ? 0 : Integer.parseInt(decimalPart);
        }
    }

    @Override
    public String toString() {
        String intPart = value != null ? value.toString() : "0";
        String decimalPart = "";

        if (anzahlnachkommastellen > 0) {
            // Stelle sicher, dass Nachkomma genau x-stellig ist
            decimalPart = String.format("%0" + anzahlnachkommastellen + "d", wertnachkommastellen);
        }

        String fullString = (anzahlnachkommastellen > 0)
                ? intPart + decimal + decimalPart
                : intPart;

        // Links mit Nullen auffüllen, um Gesamtlänge zu erreichen
        int padLength = length - fullString.length();
        if (padLength > 0) {
            char[] zeros = new char[padLength];
            java.util.Arrays.fill(zeros, '0');
            fullString = new String(zeros) + fullString;
        } else if (padLength < 0) {
            // Sollte bei korrekter Benutzung nicht vorkommen
            fullString = fullString.substring(-padLength); // Kürzen von links
        }

        return fullString;
    }

    // Getter für Tests
    public int getAnzahlnachkommastellen() {
        return anzahlnachkommastellen;
    }

    public int getWertnachkommastellen() {
        return wertnachkommastellen;
    }

    public void setDecimal(String decimal) {
        this.decimal = decimal;
    }

}
