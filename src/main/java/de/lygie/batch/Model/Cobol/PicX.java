package de.lygie.batch.Model.Cobol;

public class PicX extends AbstractCobolPicture implements ICobolPicture {

    private String[] validValues;

    private String value;

    public PicX(int length, String[] validValues) {
        super(length);
        this.validValues = validValues;
    }

    public PicX(int length, String value) {
        super(length);
        setValue(value);
    }


    public PicX(int length){
        super(length);
    }

    public String setValue(String value){
        if(value.length() > length){
            value = value.substring(0, length);
        }
        if(value.length() < length){
            value = value.substring(0, value.length());
            value = padRight(value, length);
        }
        this.value = value;

        return value;
    }

    @Override
    public String toString() {

        return padRight(value, length);
    }

    public String getValue(){
        if(value == null){
            return padRight("", length);
        }
        return value;
    }

    public void setLength(int length) {
        this.length = length;
    }
    


}
