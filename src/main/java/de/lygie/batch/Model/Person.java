package de.lygie.batch.Model;

import de.lygie.batch.Model.Cobol.AbstractCobolDatensatz;
import de.lygie.batch.Model.Cobol.PicX;
import de.lygie.batch.helper.Order;

public class Person extends AbstractCobolDatensatz {
    @Order(1)
    PicX vorname = new PicX(30);
    @Order(2)
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
