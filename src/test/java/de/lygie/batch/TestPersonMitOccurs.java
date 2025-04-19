package de.lygie.batch;

import de.lygie.batch.Model.Cobol.PicX;
import de.lygie.batch.Model.Person;
import de.lygie.batch.Model.PersonMitOccurs;
import de.lygie.batch.helper.StaticHelper;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

public class TestPersonMitOccurs {

    @Test
    public void testPersonMitOccurs() {
        PersonMitOccurs person = new PersonMitOccurs();

        person.setVorname(new PicX(30,"Hans"));
        person.setNachname(new PicX(30,"Dampf"));


        //Vater
        Person vater = new Person();
        vater.setVorname(new PicX(30,"Vater"));
        vater.setNachname(new PicX(30,"Dampf"));

        //Mutter
        Person mutter = new Person();
        mutter.setVorname(new PicX(30,"Mutter"));
        mutter.setNachname(new PicX(30,"Dampf"));

        Person[] eltern = {vater, mutter};
        person.setEltern(eltern);

        System.out.println(person.toString());

        Person kind1 = new Person();
        kind1.setVorname(new PicX(30,"Kind1"));
        kind1.setNachname(new PicX(30,"Dampf"));

        Person kind2 = new Person();
        kind2.setVorname(new PicX(30,"Kind2"));
        kind2.setNachname(new PicX(30,"Dampf"));

        Person kind3 = new Person();
        kind3.setVorname(new PicX(30,"Kind3"));
        kind3.setNachname(new PicX(30,"Dampf"));

        person.addKind(kind1);
        person.addKind(kind2);
        person.addKind(kind3);

        System.out.println(person.toString());

        String testHauptPerson = StaticHelper.randomString(60);
        String testVater = StaticHelper.randomString(60);
        String testMutter = StaticHelper.randomString(60);

        person = new PersonMitOccurs();
        person.fromCobolString(testHauptPerson + testVater + testMutter + "000",0, 1);
        assert(person.toString().equals(testHauptPerson+ testVater + testMutter + "000"));

        //nun testen wir eine Person mit zwei Kindern
        person = new PersonMitOccurs();
        person.fromCobolString(testHauptPerson + testVater + testMutter + "002",0, 1);
        String testString = "";
        int anzahlKinder = person.getAnahlKinder().getValue().intValue();

        for (int i = 1; i <= anzahlKinder; i++){
            Person kind = new Person();
            kind.setVorname(new PicX(30,"Kind"+i));
            kind.setNachname(new PicX(30,"Dampf"));
            testString = testString + kind.toString();
            person.addKind(kind);

        }
        System.out.println(person.toString());
        System.out.println(testHauptPerson+ testVater + testMutter + "002" + testString);
        assert(person.toString().equals(testHauptPerson+ testVater + testMutter + "002" + testString));


    }
}
