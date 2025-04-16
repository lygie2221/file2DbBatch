package de.lygie.batch.Model;

import de.lygie.batch.Model.Cobol.AbstractCobolDatensatz;
import de.lygie.batch.Model.Cobol.PicX;

public class DBNA extends AbstractCobolDatensatz {

    private PicX kennung = new PicX(4);
    private PicX fmna = new PicX(30);
    private PicX vona = new PicX(30);
    private PicX vosa = new PicX(30);
    private PicX nazu = new PicX(20);
    private PicX titel = new PicX(20);
    private PicX kennzAb = new PicX(1);

    public PicX getKennung() {
        return kennung;
    }

    public void setKennung(PicX kennung) {
        this.kennung = kennung;
    }

    public PicX getFmna() {
        return fmna;
    }

    public void setFmna(PicX fmna) {
        this.fmna = fmna;
    }

    public PicX getVona() {
        return vona;
    }

    public void setVona(PicX vona) {
        this.vona = vona;
    }

    public PicX getVosa() {
        return vosa;
    }

    public void setVosa(PicX vosa) {
        this.vosa = vosa;
    }

    public PicX getNazu() {
        return nazu;
    }

    public void setNazu(PicX nazu) {
        this.nazu = nazu;
    }

    public PicX getTitel() {
        return titel;
    }

    public void setTitel(PicX titel) {
        this.titel = titel;
    }

    public PicX getKennzAb() {
        return kennzAb;
    }

    public void setKennzAb(PicX kennzAb) {
        this.kennzAb = kennzAb;
    }
}
