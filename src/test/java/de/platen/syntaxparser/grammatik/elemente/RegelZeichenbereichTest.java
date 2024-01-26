package de.platen.syntaxparser.grammatik.elemente;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

import de.platen.syntaxparser.grammatik.GrammatikException;

public class RegelZeichenbereichTest
{

    @Test(expected = GrammatikException.class)
    public void testKonstruktorParameterSymbolbezeichnungNull() {
        new RegelZeichenbereich(null, new Zeichenbereich('a', 'b'));
        fail();
    }

    @Test(expected = GrammatikException.class)
    public void testKonstruktorParameterZeichenbereichNull() {
        new RegelZeichenbereich(new Symbolbezeichnung("symbolbezeichnung"), null);
        fail();
    }

    @Test
    public void testRegelZeichenbereich() {
        final RegelZeichenbereich regelZeichenbereich1 = new RegelZeichenbereich(
                new Symbolbezeichnung("symbolbezeichnung"), new Zeichenbereich('a', 'b'));
        final RegelZeichenbereich regelZeichenbereich2 = new RegelZeichenbereich(
                new Symbolbezeichnung("symbolbezeichnung"), new Zeichenbereich('a', 'b'));
        assertEquals(new Zeichenbereich('a', 'b'), regelZeichenbereich1.getZeichenbereich());
        assertEquals(regelZeichenbereich1, regelZeichenbereich2);
    }
}
