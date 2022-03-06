package de.platen.syntaxparser.grammatik.elemente;

import java.util.HashMap;

import org.junit.Test;

import de.platen.syntaxparser.grammatik.GrammatikException;

public class ZeichenmengeregelnTest
{

    @Test(expected = GrammatikException.class)
    public void testKonstruktorParameterNull() {
        new Zeichenmengeregeln(null);
    }

    @Test
    public void testKonstruktor() {
        new Zeichenmengeregeln(new HashMap<>());
    }
}
