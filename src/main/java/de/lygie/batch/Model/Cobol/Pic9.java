package de.lygie.batch.Model.Cobol;

import java.math.BigInteger;

public class Pic9 extends AbstractCobolPicture {

    private int anahlnachkommastellen = 0;
    private int wertnachkommastellen;
    private BigInteger value; //ganzzahlbereich
    private String decimal = ",";



    public Pic9(int length) {
        super(length);
    }

    public BigInteger setValue(String value){

        try {
            this.value = new BigInteger(value);
        }catch (NumberFormatException e){
            this.value = new BigInteger("0");
        }

        return this.value;
    }

    public BigInteger setValue(int value){

        this.value = new BigInteger(String.valueOf(value));

        return this.value;
    }

    public BigInteger getValue(){
        return value;
    }


    @Override
    public String toString() {
        return padLeft(String.valueOf(value),length);
    }

}
