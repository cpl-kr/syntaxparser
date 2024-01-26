package de.platen.syntaxparser.grammatik.elemente;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

import de.platen.syntaxparser.grammatik.GrammatikException;

public class ZeichenfolgeTest
{

    @Test(expected = GrammatikException.class)
    public void testKonstruktorParameterNull() {
        new Zeichenfolge(null);
        fail();
    }

    @Test(expected = GrammatikException.class)
    public void testKonstruktorParameterBlank() {
        new Zeichenfolge(" ");
        fail();
    }

    @Test(expected = GrammatikException.class)
    public void testKonstruktorParameterEmpty() {
        new Zeichenfolge("");
        fail();
    }

    @Test
    public void testZeichenfolge() {
        final Zeichenfolge zeichenfolge1 = new Zeichenfolge("abc");
        final Zeichenfolge zeichenfolge2 = new Zeichenfolge("abc");
        assertEquals("abc", zeichenfolge1.getZeichenfolge());
        assertEquals(zeichenfolge1, zeichenfolge2);
    }
}
