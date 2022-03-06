package de.platen.syntaxparser.grammatik.elemente;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import de.platen.syntaxparser.grammatik.GrammatikException;

public class RegelTest
{

    @Test(expected = GrammatikException.class)
    public void testKonstruktorParameterNull() {
        new TestRegel(null);
    }

    @Test
    public void testRegel() {
        final TestRegel testRegel1 = new TestRegel(new Symbolbezeichnung("Symbolbezeichnung"));
        final TestRegel testRegel2 = new TestRegel(new Symbolbezeichnung("Symbolbezeichnung"));
        assertEquals(new Symbolbezeichnung("Symbolbezeichnung"), testRegel1.getSymbolbezeichnung());
        assertEquals(testRegel1, testRegel2);
    }

    private static class TestRegel extends Regel
    {

        public TestRegel(final Symbolbezeichnung symbolbezeichnung) {
            super(symbolbezeichnung);
        }
    }
}
