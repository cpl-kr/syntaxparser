package de.platen.syntaxparser.grammatik.elemente;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

import de.platen.syntaxparser.grammatik.GrammatikException;

public class SymbolidentifizierungTest
{

    @Test(expected = GrammatikException.class)
    public void testKonstruktorparameterNull() {
        new Symbolidentifizierung(null);
        fail();
    }

    @Test(expected = GrammatikException.class)
    public void testKonstruktorparameterKleiner0() {
        new Symbolidentifizierung(Integer.valueOf(-1));
        fail();
    }

    @Test
    public void testSymbolidentifizierung() {
        final Symbolidentifizierung symbolidentifizierung1 = new Symbolidentifizierung(Integer.valueOf(1));
        final Symbolidentifizierung symbolidentifizierung2 = new Symbolidentifizierung(Integer.valueOf(1));
        assertEquals(Integer.valueOf(1), symbolidentifizierung1.getSymbolidentifizierung());
        assertEquals(symbolidentifizierung1, symbolidentifizierung2);
    }
}
