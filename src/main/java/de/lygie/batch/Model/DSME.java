package de.lygie.batch.Model;

import de.lygie.batch.Model.Cobol.Pic9;
import de.lygie.batch.Model.Cobol.PicX;

public class DSME {
    private PicX kennung = new PicX(4);
    private PicX verfahren = new PicX(5);
    private PicX absenderNummer = new PicX(15);
    private PicX empfaegernummer = new PicX(15);
    private Pic9 VersionsNr = new Pic9(2);


}
