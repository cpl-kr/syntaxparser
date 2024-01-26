package de.platen.syntaxparser.grammatik.elemente;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

import de.platen.syntaxparser.grammatik.GrammatikException;

public class SymbolkennungTest
{

    @Test(expected = GrammatikException.class)
    public void testKonstruktorParameterSymbolbezeichnungNull() {
        new Symbolkennung(null, new Symbolidentifizierung(Integer.valueOf(1)));
        fail();
    }

    @Test(expected = GrammatikException.class)
    public void testKonstruktorParameterSymbolidentifizierungNull() {
        new Symbolkennung(new Symbolbezeichnung("symbol"), null);
        fail();
    }

    @Test
    public void testSymbolkennung() {
        final Symbolkennung symbolkennung1 = new Symbolkennung(new Symbolbezeichnung("symbol"),
                new Symbolidentifizierung(Integer.valueOf(1)));
        final Symbolkennung symbolkennung2 = new Symbolkennung(new Symbolbezeichnung("symbol"),
                new Symbolidentifizierung(Integer.valueOf(1)));
        assertEquals(new Symbolbezeichnung("symbol"), symbolkennung1.getSymbolbezeichnung());
        assertEquals(new Symbolbezeichnung("symbol"), symbolkennung2.getSymbolbezeichnung());
        assertEquals(new Symbolidentifizierung(Integer.valueOf(1)), symbolkennung1.getSymbolidentifizierung());
        assertEquals(new Symbolidentifizierung(Integer.valueOf(1)), symbolkennung2.getSymbolidentifizierung());
        assertEquals(symbolkennung1, symbolkennung2);
    }
}
