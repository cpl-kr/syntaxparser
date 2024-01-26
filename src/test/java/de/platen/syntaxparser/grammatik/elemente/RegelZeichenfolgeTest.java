package de.platen.syntaxparser.grammatik.elemente;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

import de.platen.syntaxparser.grammatik.GrammatikException;

public class RegelZeichenfolgeTest
{

    @Test(expected = GrammatikException.class)
    public void testKonstruktorParameterSymbolbezeichnungNull() {
        new RegelZeichenfolge(null, new Zeichenfolge("abc"));
        fail();
    }

    @Test(expected = GrammatikException.class)
    public void testKonstruktorParameterZeichenfolgeNull() {
        new RegelZeichenbereich(new Symbolbezeichnung("symbolbezeichnung"), null);
        fail();
    }

    @Test
    public void testRegelZeichenfolge() {
        final RegelZeichenfolge regelZeichenfolge1 = new RegelZeichenfolge(new Symbolbezeichnung("symbolbezeichnung"),
                new Zeichenfolge("abc"));
        final RegelZeichenfolge regelZeichenfolge2 = new RegelZeichenfolge(new Symbolbezeichnung("symbolbezeichnung"),
                new Zeichenfolge("abc"));
        assertEquals(new Zeichenfolge("abc"), regelZeichenfolge1.getZeichenfolge());
        assertEquals(regelZeichenfolge1, regelZeichenfolge2);
    }
}
