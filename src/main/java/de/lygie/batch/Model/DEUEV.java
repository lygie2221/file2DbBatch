package de.lygie.batch.Model;

import de.lygie.batch.Model.Cobol.AbstractCobolDatensatz;
import de.lygie.batch.helper.Order;

public class DEUEV extends AbstractCobolDatensatz {

    private DSME dsme = new DSME();
    private DBNA dbna = new DBNA();


    public DSME getDsme() {
        return dsme;
    }

    public void setDsme(DSME dsme) {
        this.dsme = dsme;
    }

    public DBNA getDbna() {
        return dbna;
    }

    public void setDbna(DBNA dbna) {
        this.dbna = dbna;
    }
}
