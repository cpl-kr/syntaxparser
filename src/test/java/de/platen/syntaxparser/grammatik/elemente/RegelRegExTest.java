package de.platen.syntaxparser.grammatik.elemente;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

import de.platen.syntaxparser.grammatik.GrammatikException;

public class RegelRegExTest
{

    @Test(expected = GrammatikException.class)
    public void testKonstruktorParameterSymbolbezeichnungNull() {
        new RegelRegEx(null, new RegEx("abc"));
        fail();
    }

    @Test(expected = GrammatikException.class)
    public void testKonstruktorParameterRegExNull() {
        new RegelRegEx(new Symbolbezeichnung("symbolbezeichnung"), null);
        fail();
    }

    @Test
    public void testRegelRegEx() {
        final RegelRegEx regelRegEx1 = new RegelRegEx(new Symbolbezeichnung("symbolbezeichnung"), new RegEx("abc"));
        final RegelRegEx regelRegEx2 = new RegelRegEx(new Symbolbezeichnung("symbolbezeichnung"), new RegEx("abc"));
        assertEquals(new RegEx("abc"), regelRegEx1.getRegEx());
        assertEquals(regelRegEx1, regelRegEx2);
    }
}
