package de.lygie.batch.Model;

import de.lygie.batch.Model.Cobol.Pic9;
import de.lygie.batch.Model.Cobol.PicX;

public class DSME {
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


}
