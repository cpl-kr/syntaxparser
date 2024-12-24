package de.platen.syntaxparser.parser.zeichenverarbeitung;

import de.platen.syntaxparser.parser.SyntaxparserException;
import de.platen.syntaxparser.grammatik.elemente.Symbolbezeichnung;
import de.platen.syntaxparser.grammatik.elemente.Zeichenfolge;
import de.platen.syntaxparser.parser.regelverarbeitung.Verarbeitung;
import de.platen.syntaxparser.parser.regelverarbeitung.VerarbeitungZeichenfolge;
import de.platen.syntaxparser.parser.syntaxpfad.SyntaxpfadMitWort;
import de.platen.syntaxparser.parser.syntaxpfad.Syntaxpfadfolge;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class EingabewortabschlussTest {

    @Test
    public void testKonstuktorParameterNull() {
        try {
            new Eingabewortabschluss(null);
            fail();
        } catch (SyntaxparserException e) {
        }
    }

    @Test
    public void testSchliesseEingabewortAbParameterNull() {
        final Wortabschluss wortabschluss = Mockito.mock(Wortabschluss.class);
        final Eingabewortabschluss eingabewortabschluss = new Eingabewortabschluss(wortabschluss);
        try {
            eingabewortabschluss.schliesseEingabewortAb(null);
            fail();
        } catch (SyntaxparserException e) {
        }
    }

    @Test
    public void testSchliesseEingabewortAbParameterLeer() {
        final Wortabschluss wortabschluss = Mockito.mock(Wortabschluss.class);
        final Eingabewortabschluss eingabewortabschluss = new Eingabewortabschluss(wortabschluss);
        assertFalse(eingabewortabschluss.schliesseEingabewortAb(new HashSet<>()));
    }

    @Test
    public void testSchliesseEingabewortAbOhneZustandErreicht() {
        final Wortabschluss wortabschluss = Mockito.mock(Wortabschluss.class);
        final Eingabewortabschluss eingabewortabschluss = new Eingabewortabschluss(wortabschluss);
        final Set<Verarbeitungsstand> verarbeitungsstaende = new HashSet<>();
        final Syntaxpfadfolge syntaxpfadfolge = new Syntaxpfadfolge();
        final Set<Zeichenfolge> zeichenfolgen = new HashSet<>();
        final Zeichenfolge zeichenfolge = new Zeichenfolge("zeichen");
        zeichenfolgen.add(zeichenfolge);
        final Verarbeitung verarbeitung = new VerarbeitungZeichenfolge(new Symbolbezeichnung("symbol"), zeichenfolgen);
        final Verarbeitungsstand verarbeitungsstand = new Verarbeitungsstand(syntaxpfadfolge, verarbeitung);
        verarbeitungsstaende.add(verarbeitungsstand);
        verarbeitung.verarbeiteZeichen('z');
        assertFalse(eingabewortabschluss.schliesseEingabewortAb(verarbeitungsstaende));
    }

    @Test
    public void testSchliesseEingabewortAb() {
        final Verarbeitung verarbeitung = mockVerarbeitung(true, "test");
        final Verarbeitungsstand verarbeitungsstand = mockVerarbeitungsstand(verarbeitung);
        final Set<Verarbeitungsstand> verarbeitungsstaende = new HashSet<>();
        verarbeitungsstaende.add(verarbeitungsstand);
        final Wortabschluss wortabschluss = Mockito.mock(Wortabschluss.class);
        Mockito.when(wortabschluss.schliesseWortAb(Mockito.eq(verarbeitungsstand))).thenReturn(verarbeitungsstaende);
        final Eingabewortabschluss eingabewortabschluss = new Eingabewortabschluss(wortabschluss);
        assertTrue(eingabewortabschluss.schliesseEingabewortAb(verarbeitungsstaende));
    }

    private Verarbeitung mockVerarbeitung(final boolean wert, final String wort) {
        final Verarbeitung verarbeitung = Mockito.mock(Verarbeitung.class);
        Mockito.when(verarbeitung.istZustandErreicht()).thenReturn(wert);
        Mockito.when(verarbeitung.gebeWort()).thenReturn(wort);
        return verarbeitung;
    }

    private Verarbeitungsstand mockVerarbeitungsstand(final Verarbeitung verarbeitung) {
        Verarbeitungsstand verarbeitungsstand = Mockito.mock(Verarbeitungsstand.class);
        Mockito.when(verarbeitungsstand.gebeVerarbeitung()).thenReturn(verarbeitung);
        Syntaxpfadfolge syntaxpfadfolge = Mockito.mock(Syntaxpfadfolge.class);
        List<SyntaxpfadMitWort> syntaxpfadeMitWort = new ArrayList<>();
        Mockito.when(syntaxpfadfolge.getSyntaxpfadeMitWort()).thenReturn(syntaxpfadeMitWort);
        Mockito.when(verarbeitungsstand.gebeSyntaxpfadfolge()).thenReturn(syntaxpfadfolge);
        return verarbeitungsstand;
    }
}
