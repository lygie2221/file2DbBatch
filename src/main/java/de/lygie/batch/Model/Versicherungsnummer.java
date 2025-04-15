package de.lygie.batch.Model;

import java.util.Random;

public class Versicherungsnummer {
    private String vsnr;

    private char[] bereich = {'0','0'};
    private char[] tt = {'0','0'};
    private char[] mm = {'0','0'};
    private char[] jj = {'0','0'};
    private char name = 'A';
    private char[] sn = {'0','0'};
    private char pruefziffer;

    private final int min= 1;
    private final int max= 99;



    public String generateRandomVersicherungsnummer(){

        int ramdom = min + (int)(Math.random() * max);
        bereich = String.format("%02d", ramdom).toCharArray();

        ramdom = min + (int)(Math.random() * 30);
        tt = String.format("%02d", ramdom).toCharArray();

        ramdom = min + (int)(Math.random() * 12);
        mm = String.format("%02d", ramdom).toCharArray();

        ramdom = min + (int)(Math.random() * max);
        jj = String.format("%02d", ramdom).toCharArray();

        Random r = new Random();
        name = (char)(r.nextInt(26) + 'a');

        ramdom = min + (int)(Math.random() * max);
        sn = String.format("%02d", ramdom).toCharArray();

        pruefziffer = (char)(r.nextInt(9) + '0');



        vsnr = String.valueOf(bereich)
                + String.valueOf(tt)
                + String.valueOf(mm)
                + String.valueOf(jj)
                + String.valueOf(name)
                + String.valueOf(sn)
                + String.valueOf(pruefziffer);

        return vsnr;
    }

    public String getVsnr() {
        return vsnr;
    }

    public String getSn() {
        return String.valueOf(sn);
    }

}
