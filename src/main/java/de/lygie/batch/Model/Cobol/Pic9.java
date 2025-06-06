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

        // Entferne Dezimalzeichen (z. B. . oder ,) und alle Nicht-Ziffern
        svalue = svalue.replaceAll(java.util.regex.Pattern.quote(decimal), "");
        String digits = svalue.replaceAll("[^0-9]", "");

        // Wenn zu lang: schneide von links, um auf `length` Ziffern zu kommen
        if (digits.length() > length) {
            digits = digits.substring(digits.length() - length);
        }

        // Split in value und nachkomma, basierend auf fester Zahl `anzahlnachkommastellen`
        int splitIndex = digits.length() - anzahlnachkommastellen;
        if (splitIndex < 0) {
            value = BigInteger.ZERO;
            wertnachkommastellen = Integer.parseInt(digits);
        } else {
            String intPart = digits.substring(0, splitIndex);
            String decimalPart = digits.substring(splitIndex);

            value = new BigInteger(intPart.isEmpty() ? "0" : intPart);
            wertnachkommastellen = decimalPart.isEmpty() ? 0 : Integer.parseInt(decimalPart);
        }
    }

    @Override
    public String toString() {
        String intPart = value != null ? value.toString() : "0";
        String decimalPart = "";

        if (anzahlnachkommastellen > 0) {
            decimalPart = String.format("%0" + anzahlnachkommastellen + "d", wertnachkommastellen);
        }

        String full = (anzahlnachkommastellen > 0)
                ? intPart + decimal + decimalPart
                : intPart;

        // Ziel: `length` Ziffern (ohne Komma)
        int currentDigitCount = intPart.length() + decimalPart.length();
        int paddingNeeded = length - currentDigitCount;

        if (paddingNeeded > 0) {
            char[] zeros = new char[paddingNeeded];
            java.util.Arrays.fill(zeros, '0');
            full = new String(zeros) + full;
        }

        return full;
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
