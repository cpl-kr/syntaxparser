package de.platen.syntaxparser.regelverarbeitung;

import de.platen.syntaxparser.SyntaxparserException;
import de.platen.syntaxparser.grammatik.elemente.RegEx;
import de.platen.syntaxparser.grammatik.elemente.Symbolbezeichnung;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class VerarbeitungRegExTest {

    @Test
    public void testKonstruktorparameter() {
        Symbolbezeichnung symbolbezeichnung = Mockito.mock(Symbolbezeichnung.class);
        Set<RegEx> regExMenge = new HashSet<>();
        RegEx regEx = new RegEx("[a-z]");
        regExMenge.add(regEx);
        new VerarbeitungRegEx(symbolbezeichnung, regExMenge);
    }

    @Test(expected = SyntaxparserException.class)
    public void testKonstruktorparameterNull() {
        Symbolbezeichnung symbolbezeichnung = Mockito.mock(Symbolbezeichnung.class);
        new VerarbeitungRegEx(symbolbezeichnung, null);
    }

    @Test(expected = SyntaxparserException.class)
    public void testKonstruktorparameterLeer() {
        Symbolbezeichnung symbolbezeichnung = Mockito.mock(Symbolbezeichnung.class);
        Set<RegEx> regExMenge = new HashSet<>();
        new VerarbeitungRegEx(symbolbezeichnung, regExMenge);
    }

    @Test(expected = SyntaxparserException.class)
    public void testVerarbeiteZeichenParameterNull() {
        Symbolbezeichnung symbolbezeichnung = Mockito.mock(Symbolbezeichnung.class);
        Set<RegEx> regExMenge = new HashSet<>();
        RegEx regEx = new RegEx("[a-z]");
        regExMenge.add(regEx);
        VerarbeitungRegEx verarbeitungRegEx = new VerarbeitungRegEx(symbolbezeichnung, regExMenge);
        verarbeitungRegEx.verarbeiteZeichen(null);
    }

    @Test
    public void testVerarbeiteZeichen1() {
        Symbolbezeichnung symbolbezeichnung = Mockito.mock(Symbolbezeichnung.class);
        Set<RegEx> regExMenge = new HashSet<>();
        RegEx regEx = new RegEx("[a-z]+");
        regExMenge.add(regEx);
        VerarbeitungRegEx verarbeitungRegEx = new VerarbeitungRegEx(symbolbezeichnung, regExMenge);
        assertTrue(verarbeitungRegEx.istStartzustand());
        String zeichen = "abcxyz0";
        for (int index = 0; index < 6; index++) {
            assertTrue(verarbeitungRegEx.verarbeiteZeichen(zeichen.charAt(index)));
            assertFalse(verarbeitungRegEx.istStartzustand());
            assertTrue(verarbeitungRegEx.istZustandErreicht());
            assertFalse(verarbeitungRegEx.istGesperrt());
            assertEquals(zeichen.substring(0, index + 1), verarbeitungRegEx.gebeWort());
        }
        assertFalse(verarbeitungRegEx.verarbeiteZeichen(zeichen.charAt(6)));
        assertTrue(verarbeitungRegEx.istZustandErreicht());
        assertTrue(verarbeitungRegEx.istGesperrt());
        assertEquals("abcxyz", verarbeitungRegEx.gebeWort());
    }

    @Test
    public void testVerarbeiteZeichen2() {
        Symbolbezeichnung symbolbezeichnung = Mockito.mock(Symbolbezeichnung.class);
        Set<RegEx> regExMenge = new HashSet<>();
        RegEx regEx = new RegEx("0123[a-z]+");
        regExMenge.add(regEx);
        VerarbeitungRegEx verarbeitungRegEx = new VerarbeitungRegEx(symbolbezeichnung, regExMenge);
        assertTrue(verarbeitungRegEx.istStartzustand());
        String zeichen = "0123";
        for (int index = 0; index < 4; index++) {
            assertTrue(verarbeitungRegEx.verarbeiteZeichen(zeichen.charAt(index)));
            assertFalse(verarbeitungRegEx.istStartzustand());
            assertFalse(verarbeitungRegEx.istZustandErreicht());
            assertFalse(verarbeitungRegEx.istGesperrt());
            assertEquals(zeichen.substring(0, index + 1), verarbeitungRegEx.gebeWort());
        }
        assertTrue(verarbeitungRegEx.verarbeiteZeichen('a'));
        assertTrue(verarbeitungRegEx.istZustandErreicht());
        assertFalse(verarbeitungRegEx.istGesperrt());
        assertEquals("0123a", verarbeitungRegEx.gebeWort());
        assertFalse(verarbeitungRegEx.verarbeiteZeichen('0'));
        assertTrue(verarbeitungRegEx.istZustandErreicht());
        assertTrue(verarbeitungRegEx.istGesperrt());
        assertEquals("0123a", verarbeitungRegEx.gebeWort());
    }
}
