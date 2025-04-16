package de.lygie.batch.Model.Cobol;

public class Pic9 extends AbstractCobolPicture {

    private int anahlnachkommastellen = 0;
    private int wertnachkommastellen;
    private int value; //ganzzahlbereich
    private String decimal = ",";

    public Pic9(int length) {
        super(length);
    }
}
