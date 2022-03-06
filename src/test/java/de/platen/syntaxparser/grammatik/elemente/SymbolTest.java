package de.platen.syntaxparser.grammatik.elemente;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

import de.platen.syntaxparser.grammatik.GrammatikException;

public class SymbolTest
{

    @Test(expected = GrammatikException.class)
    public void testKonstruktorParameterSymbolkennungNull() {
        new Symbol(null, Kardinalitaet.GENAU_EINMAL);
        fail();
    }

    @Test(expected = GrammatikException.class)
    public void testKonstruktorParameterKardinalitaetNull() {
        new Symbol(new Symbolkennung(new Symbolbezeichnung("Symbolbezeichnung"),
                new Symbolidentifizierung(Integer.valueOf(1))), null);
        fail();
    }

    @Test
    public void testSymbol() {
        final Symbol symbol1 = new Symbol(new Symbolkennung(new Symbolbezeichnung("Symbolbezeichnung"),
                new Symbolidentifizierung(Integer.valueOf(1))), Kardinalitaet.GENAU_EINMAL);
        final Symbol symbol2 = new Symbol(new Symbolkennung(new Symbolbezeichnung("Symbolbezeichnung"),
                new Symbolidentifizierung(Integer.valueOf(1))), Kardinalitaet.GENAU_EINMAL);
        assertEquals(new Symbolkennung(new Symbolbezeichnung("Symbolbezeichnung"),
                new Symbolidentifizierung(Integer.valueOf(1))), symbol1.getSymbolkennung());
        assertEquals(Kardinalitaet.GENAU_EINMAL, symbol1.getKardinalitaet());
        assertEquals(symbol1, symbol2);
    }
}
