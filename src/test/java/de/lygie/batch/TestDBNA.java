package de.lygie.batch;

import de.lygie.batch.Model.Cobol.PicX;
import de.lygie.batch.Model.DBNA;
import de.lygie.batch.helper.StaticHelper;
import org.junit.jupiter.api.Test;

import java.nio.charset.Charset;
import java.util.Random;

public class TestDBNA {

    @Test
    public void testGeneration() {

        DBNA dbna = new DBNA();
        PicX kennung = new PicX(4);
        kennung.setValue(StaticHelper.randomString(kennung.getLength()));
        dbna.setKennung(kennung);

        PicX fnma = new PicX(30);
        fnma.setValue(StaticHelper.randomString(fnma.getLength()));
        dbna.setFmna(fnma);

        PicX vona = new PicX(30);
        vona.setValue(StaticHelper.randomString(vona.getLength()));
        dbna.setVona(vona);

        PicX vosa = new PicX(30);
        vosa.setValue(StaticHelper.randomString(vosa.getLength()));
        dbna.setVosa(vosa);

        PicX nazu = new PicX(20);
        nazu.setValue(StaticHelper.randomString(nazu.getLength()));
        dbna.setNazu(nazu);

        PicX titel = new PicX(20);
        titel.setValue(StaticHelper.randomString(titel.getLength()));
        dbna.setTitel(titel);

        PicX kennzAb = new PicX(1);
        kennzAb.setValue(StaticHelper.randomString(kennzAb.getLength()));
        dbna.setKennzAb(kennzAb);

        System.out.println(dbna.toString());

        fnma.setValue("Lygie");
        dbna.setFmna(fnma);
        System.out.println(dbna.toString());

        String testme = StaticHelper.randomString(135);
        dbna.fromString(testme);

        System.out.println(dbna.getDDL("DBNA","id"));
        System.out.println(dbna.getInsertQuery("DBNA"));


        assert(dbna.toString().equals(testme));

        assert(true);

    }


}
