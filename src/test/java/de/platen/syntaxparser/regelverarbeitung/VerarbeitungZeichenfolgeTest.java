package de.platen.syntaxparser.regelverarbeitung;

import de.platen.syntaxparser.SyntaxparserException;
import de.platen.syntaxparser.grammatik.elemente.Symbolbezeichnung;
import de.platen.syntaxparser.grammatik.elemente.Zeichenfolge;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class VerarbeitungZeichenfolgeTest {

    @Test
    public void testKonstruktorparameter() {
        Symbolbezeichnung symbolbezeichnung = Mockito.mock(Symbolbezeichnung.class);
        Set<Zeichenfolge> zeichenfolgen = new HashSet<>();
        Zeichenfolge zeichenfolge = new Zeichenfolge("test");
        zeichenfolgen.add(zeichenfolge);
        new VerarbeitungZeichenfolge(symbolbezeichnung, zeichenfolgen);
    }

    @Test(expected = SyntaxparserException.class)
    public void testKonstruktorparameterNull() {
        Symbolbezeichnung symbolbezeichnung = Mockito.mock(Symbolbezeichnung.class);
        new VerarbeitungZeichenfolge(symbolbezeichnung, null);
    }

    @Test(expected = SyntaxparserException.class)
    public void testKonstruktorparameterLeer() {
        Symbolbezeichnung symbolbezeichnung = Mockito.mock(Symbolbezeichnung.class);
        Set<Zeichenfolge> zeichenfolgen = new HashSet<>();
        new VerarbeitungZeichenfolge(symbolbezeichnung, zeichenfolgen);
    }

    @Test(expected = SyntaxparserException.class)
    public void testVerarbeiteZeichenParameterNull() {
        Symbolbezeichnung symbolbezeichnung = Mockito.mock(Symbolbezeichnung.class);
        Set<Zeichenfolge> zeichenfolgen = new HashSet<>();
        Zeichenfolge zeichenfolge = new Zeichenfolge("test");
        zeichenfolgen.add(zeichenfolge);
        VerarbeitungZeichenfolge verarbeitungZeichenfolge = new VerarbeitungZeichenfolge(symbolbezeichnung, zeichenfolgen);
        verarbeitungZeichenfolge.verarbeiteZeichen(null);
    }

    @Test
    public void testVerarbeiteZeichen1() {
        Symbolbezeichnung symbolbezeichnung = Mockito.mock(Symbolbezeichnung.class);
        Set<Zeichenfolge> zeichenfolgen = new HashSet<>();
        Zeichenfolge zeichenfolge = new Zeichenfolge("zeichenfolge");
        zeichenfolgen.add(zeichenfolge);
        VerarbeitungZeichenfolge verarbeitungZeichenfolge = new VerarbeitungZeichenfolge(symbolbezeichnung, zeichenfolgen);
        assertTrue(verarbeitungZeichenfolge.istStartzustand());
        assertFalse(verarbeitungZeichenfolge.verarbeiteZeichen('a'));
        assertFalse(verarbeitungZeichenfolge.istStartzustand());
        assertEquals("", verarbeitungZeichenfolge.gebeWort());
        assertFalse(verarbeitungZeichenfolge.istZustandErreicht());
        assertTrue(verarbeitungZeichenfolge.istGesperrt());
    }

    @Test
    public void testVerarbeiteZeichen2() {
        Symbolbezeichnung symbolbezeichnung = Mockito.mock(Symbolbezeichnung.class);
        Set<Zeichenfolge> zeichenfolgen = new HashSet<>();
        Zeichenfolge zeichenfolge = new Zeichenfolge("zeichenfolge");
        zeichenfolgen.add(zeichenfolge);
        VerarbeitungZeichenfolge verarbeitungZeichenfolge = new VerarbeitungZeichenfolge(symbolbezeichnung, zeichenfolgen);
        assertTrue(verarbeitungZeichenfolge.istStartzustand());
        String zeichen = "zeichenfolgea";
        for (int index = 0; index < 11; index++) {
            assertTrue(verarbeitungZeichenfolge.verarbeiteZeichen(zeichen.charAt(index)));
            assertFalse(verarbeitungZeichenfolge.istStartzustand());
            assertFalse(verarbeitungZeichenfolge.istZustandErreicht());
            assertFalse(verarbeitungZeichenfolge.istGesperrt());
            assertEquals(zeichen.substring(0, index + 1), verarbeitungZeichenfolge.gebeWort());
        }
        assertTrue(verarbeitungZeichenfolge.verarbeiteZeichen(zeichen.charAt(11)));
        assertTrue(verarbeitungZeichenfolge.istZustandErreicht());
        assertTrue(verarbeitungZeichenfolge.istGesperrt());
        assertEquals("zeichenfolge", verarbeitungZeichenfolge.gebeWort());
        assertFalse(verarbeitungZeichenfolge.verarbeiteZeichen(zeichen.charAt(12)));
        assertTrue(verarbeitungZeichenfolge.istZustandErreicht());
        assertTrue(verarbeitungZeichenfolge.istGesperrt());
        assertEquals("zeichenfolge", verarbeitungZeichenfolge.gebeWort());
    }

    @Test
    public void testVerarbeiteZeichen3() {
        Symbolbezeichnung symbolbezeichnung = Mockito.mock(Symbolbezeichnung.class);
        Set<Zeichenfolge> zeichenfolgen = new HashSet<>();
        Zeichenfolge zeichenfolge = new Zeichenfolge("zeichenfolge");
        zeichenfolgen.add(zeichenfolge);
        VerarbeitungZeichenfolge verarbeitungZeichenfolge = new VerarbeitungZeichenfolge(symbolbezeichnung, zeichenfolgen);
        assertTrue(verarbeitungZeichenfolge.istStartzustand());
        String zeichen = "zeichenafolge";
        for (int index = 0; index < 7; index++) {
            assertTrue(verarbeitungZeichenfolge.verarbeiteZeichen(zeichen.charAt(index)));
            assertFalse(verarbeitungZeichenfolge.istStartzustand());
            assertFalse(verarbeitungZeichenfolge.istZustandErreicht());
            assertFalse(verarbeitungZeichenfolge.istGesperrt());
            assertEquals(zeichen.substring(0, index + 1), verarbeitungZeichenfolge.gebeWort());
        }
        assertFalse(verarbeitungZeichenfolge.verarbeiteZeichen(zeichen.charAt(7)));
        assertFalse(verarbeitungZeichenfolge.istZustandErreicht());
        assertTrue(verarbeitungZeichenfolge.istGesperrt());
        assertEquals("", verarbeitungZeichenfolge.gebeWort());
        for (int index = 8; index < 13; index++) {
            assertFalse(verarbeitungZeichenfolge.verarbeiteZeichen(zeichen.charAt(index)));
            assertFalse(verarbeitungZeichenfolge.istZustandErreicht());
            assertTrue(verarbeitungZeichenfolge.istGesperrt());
            assertEquals("", verarbeitungZeichenfolge.gebeWort());
        }
    }

    @Test
    public void testVerarbeiteZeichen4() {
        Symbolbezeichnung symbolbezeichnung = Mockito.mock(Symbolbezeichnung.class);
        Set<Zeichenfolge> zeichenfolgen = new HashSet<>();
        Zeichenfolge zeichenfolge1 = new Zeichenfolge("zeichen");
        Zeichenfolge zeichenfolge2 = new Zeichenfolge("zeichenfolge");
        zeichenfolgen.add(zeichenfolge1);
        zeichenfolgen.add(zeichenfolge2);
        VerarbeitungZeichenfolge verarbeitungZeichenfolge = new VerarbeitungZeichenfolge(symbolbezeichnung, zeichenfolgen);
        assertTrue(verarbeitungZeichenfolge.istStartzustand());
        String zeichen = "zeichen";
        for (int index = 0; index < 7; index++) {
            assertTrue(verarbeitungZeichenfolge.verarbeiteZeichen(zeichen.charAt(index)));
            assertFalse(verarbeitungZeichenfolge.istStartzustand());
        }
        assertFalse(verarbeitungZeichenfolge.verarbeiteZeichen('f'));
        assertTrue(verarbeitungZeichenfolge.istZustandErreicht());
        assertTrue(verarbeitungZeichenfolge.istGesperrt());
        assertEquals("zeichen", verarbeitungZeichenfolge.gebeWort());
    }
}
