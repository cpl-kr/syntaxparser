package de.platen.syntaxparser.grammatik.elemente;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

import de.platen.syntaxparser.grammatik.GrammatikException;

public class ZeichenbereichTest
{

    @Test(expected = GrammatikException.class)
    public void testKonstruktorParameterVonNull() {
        new Zeichenbereich(null, 'c');
        fail();
    }

    @Test(expected = GrammatikException.class)
    public void testKonstruktorParameterBisNull() {
        new Zeichenbereich('c', null);
        fail();
    }

    @Test(expected = GrammatikException.class)
    public void testKonstruktorVonGroesserBis() {
        new Zeichenbereich('9', '0');
        fail();
    }

    @Test
    public void testZeichenbereich() {
        final Zeichenbereich zeichenbereich1 = new Zeichenbereich('a', 'b');
        final Zeichenbereich zeichenbereich2 = new Zeichenbereich('a', 'b');
        assertEquals('a', zeichenbereich1.getVon().charValue());
        assertEquals('b', zeichenbereich1.getBis().charValue());
        assertEquals(zeichenbereich1, zeichenbereich2);
    }
}
