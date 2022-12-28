package de.platen.syntaxparser.zeichenverarbeitung;

import de.platen.syntaxparser.SyntaxparserException;
import de.platen.syntaxparser.syntaxpfad.SyntaxpfadMitWort;
import de.platen.syntaxparser.syntaxpfad.Syntaxpfadfolge;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class SatzabschlussTest {

    @Test
    public void testSchliesseSatzAbParameterNull() {
        final Satzabschluss satzabschluss = new Satzabschluss();
        try {
            satzabschluss.schliesseSatzAb(null);
            fail();
        } catch (SyntaxparserException e) {
        }
    }

    @Test
    public void testSchliesseSatzAbParameterLeer() {
        final Satzabschluss satzabschluss = new Satzabschluss();
        final Set<Verarbeitungsstand> verarbeitungsstaende = new HashSet<>();
        final List<SyntaxpfadMitWort> syntaxpfadfolgenMitWort = satzabschluss.schliesseSatzAb(verarbeitungsstaende);
        assertTrue(syntaxpfadfolgenMitWort.isEmpty());
    }

    @Test
    public void testSchliesseSatzAbMehrAls1ElementMaximum() {
        final Satzabschluss satzabschluss = new Satzabschluss();
        final Verarbeitungsstand verarbeitungsstand1 = Mockito.mock(Verarbeitungsstand.class);
        final Verarbeitungsstand verarbeitungsstand2 = Mockito.mock(Verarbeitungsstand.class);
        final Set<Verarbeitungsstand> verarbeitungsstaende = new HashSet<>();
        verarbeitungsstaende.add(verarbeitungsstand1);
        verarbeitungsstaende.add(verarbeitungsstand2);
        final Syntaxpfadfolge syntaxpfadfolge1 = Mockito.mock(Syntaxpfadfolge.class);
        final Syntaxpfadfolge syntaxpfadfolge2 = Mockito.mock(Syntaxpfadfolge.class);
        final SyntaxpfadMitWort syntaxpfadMitWort1 = Mockito.mock(SyntaxpfadMitWort.class);
        final SyntaxpfadMitWort syntaxpfadMitWort2 = Mockito.mock(SyntaxpfadMitWort.class);
        final List<SyntaxpfadMitWort> syntaxpfadeMitWort1 = new ArrayList<>();
        final List<SyntaxpfadMitWort> syntaxpfadeMitWort2 = new ArrayList<>();
        syntaxpfadeMitWort1.add(syntaxpfadMitWort1);
        syntaxpfadeMitWort2.add(syntaxpfadMitWort2);
        Mockito.when(syntaxpfadfolge1.getSyntaxpfadeMitWort()).thenReturn(syntaxpfadeMitWort1);
        Mockito.when(syntaxpfadfolge2.getSyntaxpfadeMitWort()).thenReturn(syntaxpfadeMitWort2);
        Mockito.when(verarbeitungsstand1.gebeSyntaxpfadfolge()).thenReturn(syntaxpfadfolge1);
        Mockito.when(verarbeitungsstand2.gebeSyntaxpfadfolge()).thenReturn(syntaxpfadfolge2);
        try {
            satzabschluss.schliesseSatzAb(verarbeitungsstaende);
            fail();
        } catch (SyntaxparserException e) {
        }
    }

    @Test
    public void testSchliesseSatzAb() {
        final Satzabschluss satzabschluss = new Satzabschluss();
        final Verarbeitungsstand verarbeitungsstand = Mockito.mock(Verarbeitungsstand.class);
        final Set<Verarbeitungsstand> verarbeitungsstaende = new HashSet<>();
        verarbeitungsstaende.add(verarbeitungsstand);
        final Syntaxpfadfolge syntaxpfadfolge = Mockito.mock(Syntaxpfadfolge.class);
        final SyntaxpfadMitWort syntaxpfadMitWort = Mockito.mock(SyntaxpfadMitWort.class);
        final List<SyntaxpfadMitWort> syntaxpfadeMitWort = new ArrayList<>();
        syntaxpfadeMitWort.add(syntaxpfadMitWort);
        Mockito.when(syntaxpfadfolge.getSyntaxpfadeMitWort()).thenReturn(syntaxpfadeMitWort);
        Mockito.when(verarbeitungsstand.gebeSyntaxpfadfolge()).thenReturn(syntaxpfadfolge);
        final List<SyntaxpfadMitWort> syntaxpfadfolgenMitWort = satzabschluss.schliesseSatzAb(verarbeitungsstaende);
        assertEquals(1, syntaxpfadfolgenMitWort.size());
        assertEquals(syntaxpfadMitWort, syntaxpfadfolgenMitWort.get(0));
    }
}
