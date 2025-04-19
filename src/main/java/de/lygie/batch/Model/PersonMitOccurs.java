package de.lygie.batch.Model;

import de.lygie.batch.Model.Cobol.AbstractCobolDatensatz;
import de.lygie.batch.Model.Cobol.Pic9;
import de.lygie.batch.Model.Cobol.PicX;

import java.util.ArrayList;

public class PersonMitOccurs extends AbstractCobolDatensatz {
    PicX vorname = new PicX(30);
    PicX nachname = new PicX(30);

    /**
     * der einfachheit wegen nehmen wir hier eine feste Zahl (biologischer) Eltern an
     */
    Person[] eltern = new Person[2];


    Pic9 anahlKinder = new Pic9(3);

    /**
     * die Anzahl der Kinder ist flexibel
     */
    ArrayList<Person> kinder = new ArrayList<Person>();


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

    public Person[] getEltern() {
        return eltern;
    }

    public void setEltern(Person[] eltern) {
        this.eltern = eltern;
    }

    public ArrayList<Person> getKinder() {
        return kinder;
    }

    public void setKinder(ArrayList<Person> kinder) {
        this.kinder = kinder;
    }

    public void addKind(Person kind) {
        this.kinder.add(kind);
        anahlKinder.setValue(kinder.size());
    }

    public Pic9 getAnahlKinder() {
        return anahlKinder;
    }
}
