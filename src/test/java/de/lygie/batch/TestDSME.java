package de.lygie.batch;

import de.lygie.batch.Model.Cobol.AbstractCobolPicture;
import de.lygie.batch.Model.Cobol.Pic9;
import de.lygie.batch.Model.Cobol.PicX;
import de.lygie.batch.Model.DBNA;
import de.lygie.batch.Model.DSME;
import de.lygie.batch.Model.Versicherungsnummer;
import de.lygie.batch.helper.StaticHelper;
import org.junit.jupiter.api.Test;

import java.util.Random;

public class TestDSME {

    @Test
    public void testGeneration() {

        DSME dsme = new DSME();

        PicX kennung = new PicX(4);
        kennung.setValue(StaticHelper.randomString(kennung.getLength()));
        dsme.setKennung(kennung);

        PicX verfahren = new PicX(5);
        verfahren.setValue(StaticHelper.randomString(verfahren.getLength()));
        dsme.setVerfahren(verfahren);

        PicX absenderNummer = new PicX(15);
        absenderNummer.setValue(StaticHelper.randomString(absenderNummer.getLength()));
        dsme.setAbsenderNummer(absenderNummer);

        PicX empfaegernummer = new PicX(15);
        empfaegernummer.setValue(StaticHelper.randomString(empfaegernummer.getLength()));
        dsme.setEmpfaegernummer(empfaegernummer);

        PicX reserve = new PicX(2);
        reserve.setValue(StaticHelper.randomString(reserve.getLength()));
        dsme.setReserve(reserve);

        PicX bbnrvu = new PicX(15);
        bbnrvu.setValue(StaticHelper.randomString(bbnrvu.getLength()));
        dsme.setBbnrvu(bbnrvu);

        PicX azvu = new PicX(20);
        azvu.setValue(StaticHelper.randomString(azvu.getLength()));
        dsme.setAzvu(azvu);

        PicX bbnrkk = new PicX(15);
        bbnrkk.setValue(StaticHelper.randomString(bbnrkk.getLength()));
        dsme.setBbnrkk(bbnrkk);

        PicX azkk = new PicX(20);
        azkk.setValue(StaticHelper.randomString(azkk.getLength()));
        dsme.setAzkk(azkk);

        PicX bbnras = new PicX(15);
        bbnras.setValue(StaticHelper.randomString(bbnras.getLength()));
        dsme.setBbnras(bbnras);

        PicX sasc = new PicX(3);
        sasc.setValue(StaticHelper.randomString(sasc.getLength()));
        dsme.setSasc(sasc);

        Pic9 VersionsNr = new Pic9(2);
        VersionsNr.setValue(StaticHelper.randomInt(VersionsNr.getLength()));
        dsme.setVersionsNr(VersionsNr);

        Pic9 ErstellungsDatum = new Pic9(20);
        ErstellungsDatum.setValue(StaticHelper.randomInt(ErstellungsDatum.getLength()));
        dsme.setErstellungsDatum(ErstellungsDatum);

        Pic9 FehlerKennzeichen = new Pic9(1);
        FehlerKennzeichen.setValue(StaticHelper.randomInt(FehlerKennzeichen.getLength()));
        dsme.setFehlerKennzeichen(FehlerKennzeichen);

        Pic9 FehlerAnzahl = new Pic9(1);
        FehlerAnzahl.setValue(StaticHelper.randomInt(FehlerAnzahl.getLength()));
        dsme.setFehlerAnzahl(FehlerAnzahl);

        Pic9 personengruppe = new Pic9(3);
        personengruppe.setValue(StaticHelper.randomInt(personengruppe.getLength()));
        dsme.setPersonengruppe(personengruppe);

        Pic9 abgabegrund = new Pic9(2);
        abgabegrund.setValue(StaticHelper.randomInt(abgabegrund.getLength()));
        dsme.setAbgabegrund(abgabegrund);

// Versicherungsnummer
        Versicherungsnummer Versicherungsnummer = new Versicherungsnummer(12);
        Versicherungsnummer.setValue(StaticHelper.randomString(Versicherungsnummer.getLength()));
        dsme.setVersicherungsnummer(Versicherungsnummer);

// PicX mit vorgegebenen Domains
        PicX MMME = new PicX(1, new String[]{"N","J"});
        MMME.setValue(StaticHelper.randomString(MMME.getLength()));
        dsme.setMMME(MMME);

        PicX MMNA = new PicX(1, new String[]{"N","J"});
        MMNA.setValue(StaticHelper.randomString(MMNA.getLength()));
        dsme.setMMNA(MMNA);

        PicX MMGB = new PicX(1, new String[]{"N","J"});
        MMGB.setValue(StaticHelper.randomString(MMGB.getLength()));
        dsme.setMMGB(MMGB);

        PicX MMAN = new PicX(1, new String[]{"N","J"});
        MMAN.setValue(StaticHelper.randomString(MMAN.getLength()));
        dsme.setMMAN(MMAN);

        PicX MMEU = new PicX(1, new String[]{"N","J"});
        MMEU.setValue(StaticHelper.randomString(MMEU.getLength()));
        dsme.setMMEU(MMEU);

        PicX MMUV = new PicX(1, new String[]{"N","J"});
        MMUV.setValue(StaticHelper.randomString(MMUV.getLength()));
        dsme.setMMUV(MMUV);

        PicX MMKS = new PicX(1, new String[]{"N","J"});
        MMKS.setValue(StaticHelper.randomString(MMKS.getLength()));
        dsme.setMMKS(MMKS);

        PicX MMSV = new PicX(1, new String[]{"N","J"});
        MMSV.setValue(StaticHelper.randomString(MMSV.getLength()));
        dsme.setMMSV(MMSV);

        PicX MMVR = new PicX(1, new String[]{"N","J"});
        MMVR.setValue(StaticHelper.randomString(MMVR.getLength()));
        dsme.setMMVR(MMVR);

        PicX MMRG = new PicX(1, new String[]{"N","J"});
        MMRG.setValue(StaticHelper.randomString(MMRG.getLength()));
        dsme.setMMRG(MMRG);

// Weitere PicX-Felder
        PicX intern = new PicX(1);
        intern.setValue(StaticHelper.randomString(intern.getLength()));
        dsme.setIntern(intern);

        PicX MMUEB = new PicX(1, new String[]{"1","5","6"});
        MMUEB.setValue(StaticHelper.randomString(MMUEB.getLength()));
        dsme.setMMUEB(MMUEB);

        PicX intern2 = new PicX(1);
        intern2.setValue(StaticHelper.randomString(intern2.getLength()));
        dsme.setIntern2(intern2);

        PicX MMSO = new PicX(1, new String[]{"N","J"});
        MMSO.setValue(StaticHelper.randomString(MMSO.getLength()));
        dsme.setMMSO(MMSO);

        PicX KENNZSTA = new PicX(1, new String[]{"N","J"});
        KENNZSTA.setValue(StaticHelper.randomString(KENNZSTA.getLength()));
        dsme.setKENNZSTA(KENNZSTA);

        PicX reserve2 = new PicX(1);
        reserve2.setValue(StaticHelper.randomString(reserve2.getLength()));
        dsme.setReserve2(reserve2);

        PicX VERNRKP = new PicX(2);
        VERNRKP.setValue(StaticHelper.randomString(VERNRKP.getLength()));
        dsme.setVERNRKP(VERNRKP);

        PicX MMKV = new PicX(1, new String[]{"N","J"});
        MMKV.setValue(StaticHelper.randomString(MMKV.getLength()));
        dsme.setMMKV(MMKV);

        PicX reserve3 = new PicX(1);
        reserve3.setValue(StaticHelper.randomString(reserve3.getLength()));
        dsme.setReserve3(reserve3);

        PicX intern3 = new PicX(20);
        intern3.setValue(StaticHelper.randomString(intern3.getLength()));
        dsme.setIntern3(intern3);

        PicX reserve4 = new PicX(2);
        reserve4.setValue(StaticHelper.randomString(reserve4.getLength()));
        dsme.setReserve4(reserve4);

        PicX prodId = new PicX(7);
        prodId.setValue(StaticHelper.randomString(prodId.getLength()));
        dsme.setProdId(prodId);

        PicX modId = new PicX(8);
        modId.setValue(StaticHelper.randomString(modId.getLength()));
        dsme.setModId(modId);

        PicX dsId = new PicX(32);
        dsId.setValue(StaticHelper.randomString(dsId.getLength()));
        dsme.setDsId(dsId);

        PicX ABSNRV = new PicX(15);
        ABSNRV.setValue(StaticHelper.randomString(ABSNRV.getLength()));
        dsme.setABSNRV(ABSNRV);

        PicX reserve5 = new PicX(100);
        reserve5.setValue(StaticHelper.randomString(reserve5.getLength()));
        dsme.setReserve5(reserve5);
        System.out.println(dsme.toString());



        String testme = "bugimtxpiuofqigzprggkymvhvnswojmnduenxb910000000000013872106256dbsqkvbrshkcwnsdxhpjaodjycecyezidzyzshguboruenchtyxpybhlpsutbjekimefuecinbgfpzkqogjbefrlwoxyzyhnwuf40966yajxgyyahzzeoiemgpxhkbzzfsbkfwgyadeqjxhsduwfzxsqxxiqoqvaonvnwsvfecajjsfcshopsxtqrkxblpcfnhgvjsrmxpknyeydllqbilpvjeulqfxlupteuritekwziikpjoqmbavnrtpebubwbpdwuheohsdmyjapezoylulzwjyoildgkywzvtcvhnnnfazwzaaqnic";
        dsme.fromString(testme);


        assert(dsme.toString().equals(testme));

    }


}
