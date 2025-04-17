package de.lygie.batch.Model;

import de.lygie.batch.Model.Cobol.AbstractCobolDatensatz;
import de.lygie.batch.Model.Cobol.Pic9;
import de.lygie.batch.Model.Cobol.PicX;

public class DSME extends AbstractCobolDatensatz {
    private PicX kennung            = new PicX(4);
    private PicX verfahren          = new PicX(5);
    private PicX absenderNummer     = new PicX(15);
    private PicX empfaegernummer    = new PicX(15);
    private Pic9 VersionsNr         = new Pic9(2);
    private Pic9 ErstellungsDatum   = new Pic9(20);
    private Pic9 FehlerKennzeichen  = new Pic9(1);
    private Pic9 FehlerAnzahl       = new Pic9(1);
    private Versicherungsnummer Versicherungsnummer= new Versicherungsnummer(12);
    private PicX reserve            = new PicX(2);
    private PicX bbnrvu             = new PicX(15);
    private PicX azvu               = new PicX(20);
    private PicX bbnrkk             = new PicX(15);
    private PicX azkk               = new PicX(20);
    private PicX bbnras             = new PicX(15);
    private Pic9 personengruppe     = new Pic9(3);
    private Pic9 abgabegrund        = new Pic9(2);
    private PicX sasc               = new PicX(3);

    private PicX MMME	= new PicX(1, new String[]{"N","J"});
    private PicX MMNA	= new PicX(1, new String[]{"N","J"});
    private PicX MMGB	= new PicX(1, new String[]{"N","J"});
    private PicX MMAN	= new PicX(1, new String[]{"N","J"});
    private PicX MMEU	= new PicX(1, new String[]{"N","J"});
    private PicX MMUV	= new PicX(1, new String[]{"N","J"});
    private PicX MMKS	= new PicX(1, new String[]{"N","J"});
    private PicX MMSV	= new PicX(1, new String[]{"N","J"});
    private PicX MMVR	= new PicX(1, new String[]{"N","J"});
    private PicX MMRG	= new PicX(1, new String[]{"N","J"});

    private PicX intern = new PicX(1);
    private PicX MMUEB	= new PicX(1, new String[]{"1","5","6"});
    private PicX intern2= new PicX(1);
    private PicX MMSO	= new PicX(1, new String[]{"N","J"});
    private PicX KENNZSTA=new PicX(1, new String[]{"N","J"});
    private PicX reserve2= new PicX(1);
    private PicX VERNRKP = new PicX(2);
    private PicX MMKV	= new PicX(1, new String[]{"N","J"});
    private PicX reserve3= new PicX(1);
    private PicX intern3= new PicX(20);
    private PicX reserve4= new PicX(2);
    private PicX prodId = new PicX(7);
    private PicX modId  = new PicX(8);
    private PicX dsId   = new PicX(32);
    private PicX ABSNRV = new PicX(15);
    private PicX reserve5= new PicX(100);

    public PicX getKennung() {
        return kennung;
    }

    public void setKennung(PicX kennung) {
        this.kennung = kennung;
    }

    public PicX getVerfahren() {
        return verfahren;
    }

    public void setVerfahren(PicX verfahren) {
        this.verfahren = verfahren;
    }

    public PicX getAbsenderNummer() {
        return absenderNummer;
    }

    public void setAbsenderNummer(PicX absenderNummer) {
        this.absenderNummer = absenderNummer;
    }

    public PicX getEmpfaegernummer() {
        return empfaegernummer;
    }

    public void setEmpfaegernummer(PicX empfaegernummer) {
        this.empfaegernummer = empfaegernummer;
    }

    public Pic9 getVersionsNr() {
        return VersionsNr;
    }

    public void setVersionsNr(Pic9 versionsNr) {
        VersionsNr = versionsNr;
    }

    public Pic9 getErstellungsDatum() {
        return ErstellungsDatum;
    }

    public void setErstellungsDatum(Pic9 erstellungsDatum) {
        ErstellungsDatum = erstellungsDatum;
    }

    public Pic9 getFehlerKennzeichen() {
        return FehlerKennzeichen;
    }

    public void setFehlerKennzeichen(Pic9 fehlerKennzeichen) {
        FehlerKennzeichen = fehlerKennzeichen;
    }

    public Pic9 getFehlerAnzahl() {
        return FehlerAnzahl;
    }

    public void setFehlerAnzahl(Pic9 fehlerAnzahl) {
        FehlerAnzahl = fehlerAnzahl;
    }

    public Versicherungsnummer getVersicherungsnummer() {
        return Versicherungsnummer;
    }

    public void setVersicherungsnummer(Versicherungsnummer versicherungsnummer) {
        Versicherungsnummer = versicherungsnummer;
    }

    public PicX getReserve() {
        return reserve;
    }

    public void setReserve(PicX reserve) {
        this.reserve = reserve;
    }

    public PicX getBbnrvu() {
        return bbnrvu;
    }

    public void setBbnrvu(PicX bbnrvu) {
        this.bbnrvu = bbnrvu;
    }

    public PicX getAzvu() {
        return azvu;
    }

    public void setAzvu(PicX azvu) {
        this.azvu = azvu;
    }

    public PicX getBbnrkk() {
        return bbnrkk;
    }

    public void setBbnrkk(PicX bbnrkk) {
        this.bbnrkk = bbnrkk;
    }

    public PicX getAzkk() {
        return azkk;
    }

    public void setAzkk(PicX azkk) {
        this.azkk = azkk;
    }

    public PicX getBbnras() {
        return bbnras;
    }

    public void setBbnras(PicX bbnras) {
        this.bbnras = bbnras;
    }

    public Pic9 getPersonengruppe() {
        return personengruppe;
    }

    public void setPersonengruppe(Pic9 personengruppe) {
        this.personengruppe = personengruppe;
    }

    public Pic9 getAbgabegrund() {
        return abgabegrund;
    }

    public void setAbgabegrund(Pic9 abgabegrund) {
        this.abgabegrund = abgabegrund;
    }

    public PicX getSasc() {
        return sasc;
    }

    public void setSasc(PicX sasc) {
        this.sasc = sasc;
    }

    public PicX getMMME() {
        return MMME;
    }

    public void setMMME(PicX MMME) {
        this.MMME = MMME;
    }

    public PicX getMMNA() {
        return MMNA;
    }

    public void setMMNA(PicX MMNA) {
        this.MMNA = MMNA;
    }

    public PicX getMMGB() {
        return MMGB;
    }

    public void setMMGB(PicX MMGB) {
        this.MMGB = MMGB;
    }

    public PicX getMMAN() {
        return MMAN;
    }

    public void setMMAN(PicX MMAN) {
        this.MMAN = MMAN;
    }

    public PicX getMMEU() {
        return MMEU;
    }

    public void setMMEU(PicX MMEU) {
        this.MMEU = MMEU;
    }

    public PicX getMMUV() {
        return MMUV;
    }

    public void setMMUV(PicX MMUV) {
        this.MMUV = MMUV;
    }

    public PicX getMMKS() {
        return MMKS;
    }

    public void setMMKS(PicX MMKS) {
        this.MMKS = MMKS;
    }

    public PicX getMMSV() {
        return MMSV;
    }

    public void setMMSV(PicX MMSV) {
        this.MMSV = MMSV;
    }

    public PicX getMMVR() {
        return MMVR;
    }

    public void setMMVR(PicX MMVR) {
        this.MMVR = MMVR;
    }

    public PicX getMMRG() {
        return MMRG;
    }

    public void setMMRG(PicX MMRG) {
        this.MMRG = MMRG;
    }

    public PicX getIntern() {
        return intern;
    }

    public void setIntern(PicX intern) {
        this.intern = intern;
    }

    public PicX getMMUEB() {
        return MMUEB;
    }

    public void setMMUEB(PicX MMUEB) {
        this.MMUEB = MMUEB;
    }

    public PicX getIntern2() {
        return intern2;
    }

    public void setIntern2(PicX intern2) {
        this.intern2 = intern2;
    }

    public PicX getMMSO() {
        return MMSO;
    }

    public void setMMSO(PicX MMSO) {
        this.MMSO = MMSO;
    }

    public PicX getKENNZSTA() {
        return KENNZSTA;
    }

    public void setKENNZSTA(PicX KENNZSTA) {
        this.KENNZSTA = KENNZSTA;
    }

    public PicX getReserve2() {
        return reserve2;
    }

    public void setReserve2(PicX reserve2) {
        this.reserve2 = reserve2;
    }

    public PicX getVERNRKP() {
        return VERNRKP;
    }

    public void setVERNRKP(PicX VERNRKP) {
        this.VERNRKP = VERNRKP;
    }

    public PicX getMMKV() {
        return MMKV;
    }

    public void setMMKV(PicX MMKV) {
        this.MMKV = MMKV;
    }

    public PicX getReserve3() {
        return reserve3;
    }

    public void setReserve3(PicX reserve3) {
        this.reserve3 = reserve3;
    }

    public PicX getIntern3() {
        return intern3;
    }

    public void setIntern3(PicX intern3) {
        this.intern3 = intern3;
    }

    public PicX getReserve4() {
        return reserve4;
    }

    public void setReserve4(PicX reserve4) {
        this.reserve4 = reserve4;
    }

    public PicX getProdId() {
        return prodId;
    }

    public void setProdId(PicX prodId) {
        this.prodId = prodId;
    }

    public PicX getModId() {
        return modId;
    }

    public void setModId(PicX modId) {
        this.modId = modId;
    }

    public PicX getDsId() {
        return dsId;
    }

    public void setDsId(PicX dsId) {
        this.dsId = dsId;
    }

    public PicX getABSNRV() {
        return ABSNRV;
    }

    public void setABSNRV(PicX ABSNRV) {
        this.ABSNRV = ABSNRV;
    }

    public PicX getReserve5() {
        return reserve5;
    }

    public void setReserve5(PicX reserve5) {
        this.reserve5 = reserve5;
    }
}
