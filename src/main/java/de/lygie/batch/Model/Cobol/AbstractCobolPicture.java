package de.lygie.batch.Model.Cobol;

import org.apache.commons.lang3.StringUtils;

public class AbstractCobolPicture {

    protected int length;

    public AbstractCobolPicture(int length){
        this.length = length;
    }

    public int getLength() {
        return length;
    }

    public static String padRight(String s, int n) {
        return String.format("%" + n + "s", s);
    }

    public static String padLeft(String s, int n) {
        return StringUtils.leftPad(s, n, '0');
    }

}
