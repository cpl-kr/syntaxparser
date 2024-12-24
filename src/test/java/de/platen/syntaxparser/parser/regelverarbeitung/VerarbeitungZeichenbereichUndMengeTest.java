package de.platen.syntaxparser.parser.regelverarbeitung;

import de.platen.syntaxparser.parser.SyntaxparserException;
import de.platen.syntaxparser.grammatik.elemente.Symbolbezeichnung;
import de.platen.syntaxparser.grammatik.elemente.Zeichenbereich;
import de.platen.syntaxparser.grammatik.elemente.Zeichenmenge;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class VerarbeitungZeichenbereichUndMengeTest {

    @Test
    public void testKonstruktor() {
        final Symbolbezeichnung symbolbezeichnung = Mockito.mock(Symbolbezeichnung.class);
        final Set<Zeichenbereich > zeichenbereiche = new HashSet<>();
        Zeichenbereich zeichenbereich = new Zeichenbereich('a', 'z');
        zeichenbereiche.add(zeichenbereich);
        final Set<Zeichenmenge> zeichenmengen = new HashSet<>();
        Set<Character> menge = new HashSet<>();
        menge.add('.');
        Zeichenmenge zeichenmenge = new Zeichenmenge(menge);
        zeichenmengen.add(zeichenmenge);
        new VerarbeitungZeichenbereichUndMenge(symbolbezeichnung, zeichenbereiche, zeichenmengen);
    }

    @Test
    public void testKonstruktorParameter2Null() {
        final Symbolbezeichnung symbolbezeichnung = Mockito.mock(Symbolbezeichnung.class);
        final Set<Zeichenmenge> zeichenmengen = new HashSet<>();
        Set<Character> menge = new HashSet<>();
        menge.add('.');
        Zeichenmenge zeichenmenge = new Zeichenmenge(menge);
        zeichenmengen.add(zeichenmenge);
        new VerarbeitungZeichenbereichUndMenge(symbolbezeichnung, null, zeichenmengen);
    }

    @Test
    public void testKonstruktorParameter3Null() {
        final Symbolbezeichnung symbolbezeichnung = Mockito.mock(Symbolbezeichnung.class);
        final Set<Zeichenbereich > zeichenbereiche = new HashSet<>();
        Zeichenbereich zeichenbereich = new Zeichenbereich('a', 'z');
        zeichenbereiche.add(zeichenbereich);
        new VerarbeitungZeichenbereichUndMenge(symbolbezeichnung, zeichenbereiche, null);
    }

    @Test(expected = SyntaxparserException.class)
    public void testKonstruktorparameterBeideNull() {
        final Symbolbezeichnung symbolbezeichnung = Mockito.mock(Symbolbezeichnung.class);
        new VerarbeitungZeichenbereichUndMenge(symbolbezeichnung, null, null);
    }

    @Test(expected = SyntaxparserException.class)
    public void testKonstruktorparameterBeideLeer() {
        final Symbolbezeichnung symbolbezeichnung = Mockito.mock(Symbolbezeichnung.class);
        final Set<Zeichenbereich > zeichenbereiche = new HashSet<>();
        final Set<Zeichenmenge> zeichenmengen = new HashSet<>();
        new VerarbeitungZeichenbereichUndMenge(symbolbezeichnung, zeichenbereiche, zeichenmengen);
    }

    @Test(expected = SyntaxparserException.class)
    public void testVerarbeiteZeichenParameterNull() {
        final Symbolbezeichnung symbolbezeichnung = Mockito.mock(Symbolbezeichnung.class);
        final Set<Zeichenbereich > zeichenbereiche = new HashSet<>();
        Zeichenbereich zeichenbereich = new Zeichenbereich('a', 'z');
        zeichenbereiche.add(zeichenbereich);
        final Set<Zeichenmenge> zeichenmengen = new HashSet<>();
        Set<Character> menge = new HashSet<>();
        menge.add('.');
        Zeichenmenge zeichenmenge = new Zeichenmenge(menge);
        zeichenmengen.add(zeichenmenge);
        VerarbeitungZeichenbereichUndMenge verarbeitungZeichenbereichUndMenge = new VerarbeitungZeichenbereichUndMenge(symbolbezeichnung, zeichenbereiche, zeichenmengen);
        verarbeitungZeichenbereichUndMenge.verarbeiteZeichen(null);
    }

    @Test
    public void testVerarbeiteZeichen1() {
        final Symbolbezeichnung symbolbezeichnung = Mockito.mock(Symbolbezeichnung.class);
        final Set<Zeichenbereich > zeichenbereiche = new HashSet<>();
        Zeichenbereich zeichenbereich = new Zeichenbereich('a', 'z');
        zeichenbereiche.add(zeichenbereich);
        final Set<Zeichenmenge> zeichenmengen = new HashSet<>();
        VerarbeitungZeichenbereichUndMenge verarbeitungZeichenbereichUndMenge = new VerarbeitungZeichenbereichUndMenge(symbolbezeichnung, zeichenbereiche, zeichenmengen);
        assertTrue(verarbeitungZeichenbereichUndMenge.istStartzustand());
        assertFalse(verarbeitungZeichenbereichUndMenge.verarbeiteZeichen('0'));
        assertFalse(verarbeitungZeichenbereichUndMenge.istStartzustand());
        assertFalse(verarbeitungZeichenbereichUndMenge.istZustandErreicht());
        assertTrue(verarbeitungZeichenbereichUndMenge.istGesperrt());
        assertEquals("", verarbeitungZeichenbereichUndMenge.gebeWort());
    }

    @Test
    public void testVerarbeiteZeichen2() {
        final Symbolbezeichnung symbolbezeichnung = Mockito.mock(Symbolbezeichnung.class);
        final Set<Zeichenbereich > zeichenbereiche = new HashSet<>();
        Zeichenbereich zeichenbereich = new Zeichenbereich('a', 'z');
        zeichenbereiche.add(zeichenbereich);
        final Set<Zeichenmenge> zeichenmengen = new HashSet<>();
        VerarbeitungZeichenbereichUndMenge verarbeitungZeichenbereichUndMenge = new VerarbeitungZeichenbereichUndMenge(symbolbezeichnung, zeichenbereiche, zeichenmengen);
        assertTrue(verarbeitungZeichenbereichUndMenge.istStartzustand());
        String zeichen = "abc";
        for (int index = 0; index < 3; index++) {
            assertTrue(verarbeitungZeichenbereichUndMenge.verarbeiteZeichen(zeichen.charAt(index)));
            assertFalse(verarbeitungZeichenbereichUndMenge.istStartzustand());
            assertTrue(verarbeitungZeichenbereichUndMenge.istZustandErreicht);
            assertFalse(verarbeitungZeichenbereichUndMenge.istGesperrt());
            assertEquals(zeichen.substring(0, index + 1), verarbeitungZeichenbereichUndMenge.gebeWort());
        }
        assertFalse(verarbeitungZeichenbereichUndMenge.verarbeiteZeichen('0'));
        assertTrue(verarbeitungZeichenbereichUndMenge.istZustandErreicht);
        assertTrue(verarbeitungZeichenbereichUndMenge.istGesperrt());
        assertEquals(zeichen, verarbeitungZeichenbereichUndMenge.gebeWort());
        assertFalse(verarbeitungZeichenbereichUndMenge.verarbeiteZeichen('a'));
        assertTrue(verarbeitungZeichenbereichUndMenge.istZustandErreicht);
        assertTrue(verarbeitungZeichenbereichUndMenge.istGesperrt());
        assertEquals(zeichen, verarbeitungZeichenbereichUndMenge.gebeWort());
    }

    @Test
    public void testVerarbeiteZeichen3() {
        final Symbolbezeichnung symbolbezeichnung = Mockito.mock(Symbolbezeichnung.class);
        final Set<Zeichenbereich > zeichenbereiche = new HashSet<>();
        final Set<Zeichenmenge> zeichenmengen = new HashSet<>();
        Set<Character> menge = new HashSet<>();
        menge.add('.');
        menge.add(',');
        menge.add(';');
        Zeichenmenge zeichenmenge = new Zeichenmenge(menge);
        zeichenmengen.add(zeichenmenge);
        VerarbeitungZeichenbereichUndMenge verarbeitungZeichenbereichUndMenge = new VerarbeitungZeichenbereichUndMenge(symbolbezeichnung, zeichenbereiche, zeichenmengen);
        assertTrue(verarbeitungZeichenbereichUndMenge.istStartzustand());
        assertFalse(verarbeitungZeichenbereichUndMenge.verarbeiteZeichen('-'));
        assertFalse(verarbeitungZeichenbereichUndMenge.istStartzustand());
        assertFalse(verarbeitungZeichenbereichUndMenge.istZustandErreicht);
        assertTrue(verarbeitungZeichenbereichUndMenge.istGesperrt());
        assertEquals("", verarbeitungZeichenbereichUndMenge.gebeWort());
    }

    @Test
    public void testVerarbeiteZeichen4() {
        final Symbolbezeichnung symbolbezeichnung = Mockito.mock(Symbolbezeichnung.class);
        final Set<Zeichenbereich > zeichenbereiche = new HashSet<>();
        final Set<Zeichenmenge> zeichenmengen = new HashSet<>();
        Set<Character> menge = new HashSet<>();
        menge.add('.');
        menge.add(',');
        menge.add(';');
        Zeichenmenge zeichenmenge = new Zeichenmenge(menge);
        zeichenmengen.add(zeichenmenge);
        VerarbeitungZeichenbereichUndMenge verarbeitungZeichenbereichUndMenge = new VerarbeitungZeichenbereichUndMenge(symbolbezeichnung, zeichenbereiche, zeichenmengen);
        assertTrue(verarbeitungZeichenbereichUndMenge.istStartzustand());
        String zeichen = ".,;";
        for (int index = 0; index < 3; index++) {
            assertTrue(verarbeitungZeichenbereichUndMenge.verarbeiteZeichen(zeichen.charAt(index)));
            assertFalse(verarbeitungZeichenbereichUndMenge.istStartzustand());
            assertTrue(verarbeitungZeichenbereichUndMenge.istZustandErreicht);
            assertFalse(verarbeitungZeichenbereichUndMenge.istGesperrt());
            assertEquals(zeichen.substring(0, index + 1), verarbeitungZeichenbereichUndMenge.gebeWort());
        }
        assertFalse(verarbeitungZeichenbereichUndMenge.verarbeiteZeichen('-'));
        assertTrue(verarbeitungZeichenbereichUndMenge.istZustandErreicht);
        assertTrue(verarbeitungZeichenbereichUndMenge.istGesperrt());
        assertEquals(zeichen, verarbeitungZeichenbereichUndMenge.gebeWort());
        assertFalse(verarbeitungZeichenbereichUndMenge.verarbeiteZeichen(';'));
        assertTrue(verarbeitungZeichenbereichUndMenge.istZustandErreicht);
        assertTrue(verarbeitungZeichenbereichUndMenge.istGesperrt());
        assertEquals(zeichen, verarbeitungZeichenbereichUndMenge.gebeWort());
    }
}
