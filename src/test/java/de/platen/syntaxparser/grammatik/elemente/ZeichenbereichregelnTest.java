package de.platen.syntaxparser.grammatik.elemente;

import java.util.HashMap;

import org.junit.Test;

import de.platen.syntaxparser.grammatik.GrammatikException;

public class ZeichenbereichregelnTest
{

    @Test(expected = GrammatikException.class)
    public void testKonstruktorParameterNull() {
        new Zeichenbereichregeln(null);
    }

    @Test
    public void testKonstruktor() {
        new Zeichenbereichregeln(new HashMap<>());
    }
}
