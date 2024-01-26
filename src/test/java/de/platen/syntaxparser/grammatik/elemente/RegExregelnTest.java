package de.platen.syntaxparser.grammatik.elemente;

import java.util.HashMap;

import org.junit.Test;

import de.platen.syntaxparser.grammatik.GrammatikException;

public class RegExregelnTest
{

    @Test(expected = GrammatikException.class)
    public void testKonstruktorParameterNull() {
        new RegExregeln(null);
    }

    @Test
    public void testKonstruktor() {
        new RegExregeln(new HashMap<>());
    }
}
