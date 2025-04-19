package de.lygie.batch;

import de.lygie.batch.Model.DBNA;
import de.lygie.batch.Model.DEUEV;
import de.lygie.batch.Model.DSME;
import de.lygie.batch.helper.StaticHelper;
import org.junit.jupiter.api.Test;

public class TestDeuev {

    String testDbna = StaticHelper.randomString(135);
    DBNA dbna = new DBNA();
    DSME dsme = new DSME();
    DEUEV deuev = new DEUEV();

    @Test
    public void testDeuev() {
        dbna.fromString(testDbna);
        String testDsme = "bugimtxpiuofqigzprggkymvhvnswojmnduenxb910000000000013872106256dbsqkvbrshkcwnsdxhpjaodjycecyezidzyzshguboruenchtyxpybhlpsutbjekimefuecinbgfpzkqogjbefrlwoxyzyhnwuf40966yajxgyyahzzeoiemgpxhkbzzfsbkfwgyadeqjxhsduwfzxsqxxiqoqvaonvnwsvfecajjsfcshopsxtqrkxblpcfnhgvjsrmxpknyeydllqbilpvjeulqfxlupteuritekwziikpjoqmbavnrtpebubwbpdwuheohsdmyjapezoylulzwjyoildgkywzvtcvhnnnfazwzaaqnic";
        dsme.fromString(testDsme);

        deuev.setDsme(dsme);
        deuev.setDbna(dbna);

        System.out.println(deuev.toString());

        String testDeuev = testDsme + testDbna;

        deuev.fromString(testDeuev);
        System.out.println(deuev.toString());

        assert(deuev.toString().equals(testDeuev));



    }

}
