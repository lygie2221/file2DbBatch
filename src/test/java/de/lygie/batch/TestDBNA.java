package de.lygie.batch;

import de.lygie.batch.Model.Cobol.PicX;
import de.lygie.batch.Model.DBNA;
import org.junit.jupiter.api.Test;

import java.nio.charset.Charset;
import java.util.Random;

public class TestDBNA {

    @Test
    public void testGeneration() {

        DBNA dbna = new DBNA();
        PicX kennung = new PicX(4);
        kennung.setValue(randomString(kennung.getLength()));
        dbna.setKennung(kennung);

        PicX fnma = new PicX(30);
        fnma.setValue(randomString(fnma.getLength()));
        dbna.setFmna(fnma);

        PicX vona = new PicX(30);
        vona.setValue(randomString(vona.getLength()));
        dbna.setVona(vona);

        PicX vosa = new PicX(30);
        vosa.setValue(randomString(vosa.getLength()));
        dbna.setVosa(vosa);

        PicX nazu = new PicX(20);
        nazu.setValue(randomString(nazu.getLength()));
        dbna.setNazu(nazu);

        PicX titel = new PicX(20);
        titel.setValue(randomString(titel.getLength()));
        dbna.setTitel(titel);

        PicX kennzAb = new PicX(1);
        kennzAb.setValue(randomString(kennzAb.getLength()));
        dbna.setKennzAb(kennzAb);

        System.out.println(dbna.toString());

        fnma.setValue("Lygie");
        dbna.setFmna(fnma);
        System.out.println(dbna.toString());

        String testme = randomString(135);
        dbna.fromString(testme);

        assert(dbna.toString().equals(testme));

        assert(true);

    }

    private String randomString(int length){
        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        Random random = new Random();
        StringBuilder buffer = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int randomLimitedInt = leftLimit + (int)
                    (random.nextFloat() * (rightLimit - leftLimit + 1));
            buffer.append((char) randomLimitedInt);
        }
        return buffer.toString();
    }
}
