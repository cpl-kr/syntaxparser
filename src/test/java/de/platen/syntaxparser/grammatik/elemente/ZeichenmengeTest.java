package de.platen.syntaxparser.grammatik.elemente;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import de.platen.syntaxparser.grammatik.GrammatikException;

public class ZeichenmengeTest
{

    @Test(expected = GrammatikException.class)
    public void testKonstruktorParameterNull() {
        new Zeichenmenge(null);
        fail();
    }

    @Test(expected = GrammatikException.class)
    public void testKonstruktorParameterEmpty() {
        new Zeichenmenge(new HashSet<>());
        fail();
    }

    @Test
    public void testZeichenmenge() {
        final Set<Character> menge1 = new HashSet<>();
        menge1.add('a');
        final Zeichenmenge zeichenmenge1 = new Zeichenmenge(menge1);
        final Zeichenmenge zeichenmenge2 = new Zeichenmenge(menge1);
        final Set<Character> menge2 = new HashSet<>();
        menge2.add('a');
        assertEquals(menge2, zeichenmenge1.getZeichenmenge());
        assertEquals(zeichenmenge1, zeichenmenge2);
    }
}
