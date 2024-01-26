package de.platen.syntaxparser.grammatik.elemente;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

import de.platen.syntaxparser.grammatik.GrammatikException;

public class SymbolbezeichnungTest
{

    @Test(expected = GrammatikException.class)
    public void testKonstruktorParameterNull() {
        new Symbolbezeichnung(null);
        fail();
    }

    @Test(expected = GrammatikException.class)
    public void testKonstruktorParmameterBlank() {
        new Symbolbezeichnung("");
        fail();
    }

    @Test
    public void testSymbolbezeichnung() {
        final Symbolbezeichnung symbolbezeichnung1 = new Symbolbezeichnung("Symbolbezeichnung");
        final Symbolbezeichnung symbolbezeichnung2 = new Symbolbezeichnung("Symbolbezeichnung");
        assertEquals("Symbolbezeichnung", symbolbezeichnung1.getSymbolbezeichnung());
        assertEquals(symbolbezeichnung1, symbolbezeichnung2);
    }
}
