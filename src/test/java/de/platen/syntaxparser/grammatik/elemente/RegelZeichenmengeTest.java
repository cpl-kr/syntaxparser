package de.platen.syntaxparser.grammatik.elemente;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import de.platen.syntaxparser.grammatik.GrammatikException;

public class RegelZeichenmengeTest
{

    @Test(expected = GrammatikException.class)
    public void testKonstruktorParameterSymbolbezeichnungNull() {
        final Set<Character> menge = new HashSet<>();
        menge.add('a');
        new RegelZeichenmenge(null, new Zeichenmenge(menge));
        fail();
    }

    @Test(expected = GrammatikException.class)
    public void testKonstruktorParameterZeichenmengeNull() {
        new RegelZeichenbereich(new Symbolbezeichnung("symbolbezeichnung"), null);
        fail();
    }

    @Test
    public void testRegelZeichenmenge() {
        final Set<Character> menge1 = new HashSet<>();
        menge1.add('a');
        final Set<Character> menge2 = new HashSet<>();
        menge2.add('a');
        final RegelZeichenmenge regelZeichenmenge1 = new RegelZeichenmenge(new Symbolbezeichnung("symbolbezeichnung"),
                new Zeichenmenge(menge1));
        final RegelZeichenmenge regelZeichenmenge2 = new RegelZeichenmenge(new Symbolbezeichnung("symbolbezeichnung"),
                new Zeichenmenge(menge2));
        final Set<Character> menge3 = new HashSet<>();
        menge3.add('a');
        assertEquals(new Zeichenmenge(menge3), regelZeichenmenge1.getZeichenmenge());
        assertEquals(regelZeichenmenge1, regelZeichenmenge2);
    }
}
