package de.lygie.batch.Model;

import de.lygie.batch.Model.Cobol.AbstractCobolDatensatz;
import de.lygie.batch.Model.Cobol.PicX;

public class Person extends AbstractCobolDatensatz {
    PicX vorname = new PicX(30);
    PicX nachname = new PicX(30);

    public PicX getVorname() {
        return vorname;
    }

    public void setVorname(PicX vorname) {
        this.vorname = vorname;
    }

    public PicX getNachname() {
        return nachname;
    }

    public void setNachname(PicX nachname) {
        this.nachname = nachname;
    }
}
