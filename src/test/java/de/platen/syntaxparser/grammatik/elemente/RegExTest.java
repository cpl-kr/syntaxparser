package de.platen.syntaxparser.grammatik.elemente;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

import de.platen.syntaxparser.grammatik.GrammatikException;

public class RegExTest
{

    @Test(expected = GrammatikException.class)
    public void testKonstruktorParameterNull() {
        new RegEx(null);
        fail();
    }

    @Test(expected = GrammatikException.class)
    public void testKonstruktorParameterEmpty() {
        new RegEx("");
        fail();
    }

    @Test
    public void testRegEx() {
        final RegEx regex1 = new RegEx("abc");
        final RegEx regex2 = new RegEx("abc");
        assertEquals("abc", regex1.getRegex());
        assertEquals(regex1, regex2);
    }
}
