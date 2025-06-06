package de.lygie.batch;

import de.lygie.batch.Model.Cobol.Pic9;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TestPic9 {

    @Test
    public void testToString_EinfacheZahl() {
        Pic9 dv = new Pic9(6, 2);
        dv.setValue("1");
        assertEquals("0000,01", dv.toString());
    }

    @Test
    public void testToString_LangerWertOhneKomma() {
        Pic9 dv = new Pic9(6, 2);
        dv.setValue("1234567");
        assertEquals("2345,67", dv.toString());
    }

    @Test
    public void testToString_LangerWertMitPunkt() {
        Pic9 dv = new Pic9(6, 2);
        dv.setDecimal(".");
        dv.setValue("12345.67");
        assertEquals("2345.67", dv.toString());
    }

    @Test
    public void testToString_GekuerzterWert() {
        Pic9 dv = new Pic9(5, 1);
        dv.setDecimal(".");
        dv.setValue("12345.67");
        assertEquals("3456.7", dv.toString());
    }

    @Test
    public void testToString() {
        Pic9 dv = new Pic9(5, 1);
        dv.setDecimal(",");
        dv.setValue("5.67");
        assertEquals("0056,7", dv.toString());
    }

    @Test
    public void testToStringOhneKomma() {
        Pic9 dv = new Pic9(5, 1);
        dv.setDecimal(",");
        dv.setValue("567");
        assertEquals("0056,7", dv.toString());
    }
}
