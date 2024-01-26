package de.platen.syntaxparser.zeichenverarbeitung;

import de.platen.syntaxparser.SyntaxparserException;
import de.platen.syntaxparser.regelverarbeitung.Verarbeitung;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class ZeichenverarbeitungTest {

    @Test
    public void testKonstuktorParameterNull() {
        try {
            new Zeichenverarbeitung(null);
            fail();
        } catch (SyntaxparserException e) {
        }
    }

    @Test
    public void testVerarbeiteZeichenParameterNull() {
        final Character c = 'c';
        final Zeichenverarbeitung zeichenverarbeitung = new Zeichenverarbeitung(Mockito.mock(Wortabschluss.class));
        final Set<Verarbeitungsstand> verarbeitungsstaende = new HashSet<>();
        try {
            zeichenverarbeitung.verarbeiteZeichen(null, verarbeitungsstaende);
            fail();
        } catch (SyntaxparserException e) {
        }
        try {
            zeichenverarbeitung.verarbeiteZeichen(c, null);
            fail();
        } catch (SyntaxparserException e) {
        }
    }

    @Test
    public void testVerarbeiteZeichenParameterLeer() {
        final Character c = 'c';
        final Zeichenverarbeitung zeichenverarbeitung = new Zeichenverarbeitung(Mockito.mock(Wortabschluss.class));
        final Set<Verarbeitungsstand> verarbeitungsstaende = new HashSet<>();
        assertFalse(zeichenverarbeitung.verarbeiteZeichen(c, verarbeitungsstaende));
    }

    @Test
    public void testVerarbeiteZeichenElementLeer() {
        final Character c = 'c';
        final Zeichenverarbeitung zeichenverarbeitung = new Zeichenverarbeitung(Mockito.mock(Wortabschluss.class));
        final Set<Verarbeitungsstand> verarbeitungsstaende = new HashSet<>();
        assertFalse(zeichenverarbeitung.verarbeiteZeichen(c, verarbeitungsstaende));
        assertTrue(verarbeitungsstaende.isEmpty());
    }

    @Test
    public void testVerarbeiteZeichenZeichenVerarbeitet() {
        final Character c = 'c';
        final Zeichenverarbeitung zeichenverarbeitung = new Zeichenverarbeitung(Mockito.mock(Wortabschluss.class));
        final Set<Verarbeitungsstand> verarbeitungsstaende = new HashSet<>();
        Verarbeitungsstand verarbeitungsstand = Mockito.mock(Verarbeitungsstand.class);
        Verarbeitung verarbeitung = Mockito.mock(Verarbeitung.class);
        Mockito.when(verarbeitung.verarbeiteZeichen(Mockito.anyChar())).thenReturn(true);
        Mockito.when(verarbeitungsstand.gebeVerarbeitung()).thenReturn(verarbeitung);
        verarbeitungsstaende.add(verarbeitungsstand);
        assertTrue(zeichenverarbeitung.verarbeiteZeichen(c, verarbeitungsstaende));
        assertFalse(verarbeitungsstaende.isEmpty());
        assertEquals(1, verarbeitungsstaende.size());
        assertEquals(verarbeitungsstand, new ArrayList<>(verarbeitungsstaende).get(0));
    }

    @Test
    public void testVerarbeiteZeichenZeichenNichtVerarbeitetZustandNichtErreicht() {
        final Character c = 'c';
        final Zeichenverarbeitung zeichenverarbeitung = new Zeichenverarbeitung(Mockito.mock(Wortabschluss.class));
        final Set<Verarbeitungsstand> verarbeitungsstaende = new HashSet<>();
        Verarbeitungsstand verarbeitungsstand = Mockito.mock(Verarbeitungsstand.class);
        Verarbeitung verarbeitung = Mockito.mock(Verarbeitung.class);
        Mockito.when(verarbeitung.verarbeiteZeichen(Mockito.anyChar())).thenReturn(false);
        Mockito.when(verarbeitung.istZustandErreicht()).thenReturn(false);
        Mockito.when(verarbeitungsstand.gebeVerarbeitung()).thenReturn(verarbeitung);
        verarbeitungsstaende.add(verarbeitungsstand);
        assertFalse(zeichenverarbeitung.verarbeiteZeichen(c, verarbeitungsstaende));
        assertTrue(verarbeitungsstaende.isEmpty());
    }

    @Test
    public void testVerarbeiteZeichenZeichenNichtVerarbeitetZustandErreicht1() {
        final Character c = 'c';
        Wortabschluss wortabschluss = Mockito.mock(Wortabschluss.class);
        Mockito.when(wortabschluss.schliesseWortAb(Mockito.any(Verarbeitungsstand.class))).thenReturn(new HashSet<>());
        final Zeichenverarbeitung zeichenverarbeitung = new Zeichenverarbeitung(wortabschluss);
        final Set<Verarbeitungsstand> verarbeitungsstaende = new HashSet<>();
        Verarbeitungsstand verarbeitungsstand = Mockito.mock(Verarbeitungsstand.class);
        Verarbeitung verarbeitung = Mockito.mock(Verarbeitung.class);
        Mockito.when(verarbeitung.verarbeiteZeichen(Mockito.anyChar())).thenReturn(false).thenReturn(true);
        Mockito.when(verarbeitung.istZustandErreicht()).thenReturn(true);
        Mockito.when(verarbeitungsstand.gebeVerarbeitung()).thenReturn(verarbeitung);
        verarbeitungsstaende.add(verarbeitungsstand);
        assertTrue(zeichenverarbeitung.verarbeiteZeichen(c, verarbeitungsstaende));
        assertFalse(verarbeitungsstaende.isEmpty());
        assertEquals(1, verarbeitungsstaende.size());
        assertTrue(verarbeitungsstaende.contains(verarbeitungsstand));
    }

    @Test
    public void testVerarbeiteZeichenZeichenNichtVerarbeitetZustandErreicht2() {
        final Character c = 'c';
        Wortabschluss wortabschluss = Mockito.mock(Wortabschluss.class);
        Set<Verarbeitungsstand> verarbeitungsstaendeHinzu = new HashSet<>();
        Verarbeitungsstand verarbeitungsstandHinzu = Mockito.mock(Verarbeitungsstand.class);
        Verarbeitung verarbeitungHinzu = Mockito.mock(Verarbeitung.class);
        Mockito.when(verarbeitungHinzu.verarbeiteZeichen(Mockito.anyChar())).thenReturn(true);
        Mockito.when(verarbeitungsstandHinzu.gebeVerarbeitung()).thenReturn(verarbeitungHinzu);
        verarbeitungsstaendeHinzu.add(verarbeitungsstandHinzu);
        Mockito.when(wortabschluss.schliesseWortAb(Mockito.any(Verarbeitungsstand.class))).thenReturn(verarbeitungsstaendeHinzu);
        final Zeichenverarbeitung zeichenverarbeitung = new Zeichenverarbeitung(wortabschluss);
        final Set<Verarbeitungsstand> verarbeitungsstaende = new HashSet<>();
        Verarbeitungsstand verarbeitungsstand = Mockito.mock(Verarbeitungsstand.class);
        Verarbeitung verarbeitung = Mockito.mock(Verarbeitung.class);
        Mockito.when(verarbeitung.verarbeiteZeichen(Mockito.anyChar())).thenReturn(false).thenReturn(true);
        Mockito.when(verarbeitung.istZustandErreicht()).thenReturn(true);
        Mockito.when(verarbeitungsstand.gebeVerarbeitung()).thenReturn(verarbeitung);
        verarbeitungsstaende.add(verarbeitungsstand);
        assertTrue(zeichenverarbeitung.verarbeiteZeichen(c, verarbeitungsstaende));
        assertFalse(verarbeitungsstaende.isEmpty());
        assertEquals(2, verarbeitungsstaende.size());
        assertTrue(verarbeitungsstaende.contains(verarbeitungsstand));
        assertTrue(verarbeitungsstaende.contains(verarbeitungsstandHinzu));
    }
}
