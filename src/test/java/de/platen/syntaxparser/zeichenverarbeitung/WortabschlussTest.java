package de.platen.syntaxparser.zeichenverarbeitung;

import de.platen.syntaxparser.SyntaxparserException;
import de.platen.syntaxparser.grammatik.Grammatik;
import de.platen.syntaxparser.grammatik.GrammatikAufbau;
import de.platen.syntaxparser.grammatik.GrammatikLesen;
import de.platen.syntaxparser.regelverarbeitung.Verarbeitung;
import de.platen.syntaxparser.regelverarbeitung.VerarbeitungZeichenbereichUndMenge;
import de.platen.syntaxparser.syntaxpfad.Syntaxpfad;
import de.platen.syntaxparser.syntaxpfad.Syntaxpfadbehandlung;
import de.platen.syntaxparser.syntaxpfad.Syntaxpfadersteller;
import de.platen.syntaxparser.syntaxpfad.Syntaxpfadfolge;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class WortabschlussTest {

    @Test
    public void testKonstuktorParameterNull() {
        final Syntaxpfadbehandlung syntaxpfadbehandlung = Mockito.mock(Syntaxpfadbehandlung.class);
        final Syntaxpfadersteller syntaxpfadersteller = Mockito.mock(Syntaxpfadersteller.class);
        try {
            new Wortabschluss(null, syntaxpfadbehandlung);
            fail();
        } catch (SyntaxparserException e) {
        }
        try {
            new Wortabschluss(syntaxpfadersteller, null);
            fail();
        } catch (SyntaxparserException e) {
        }
    }

    @Test
    public void testSchliesseWortAbParameterNull() {
        final Syntaxpfadbehandlung syntaxpfadbehandlung = Mockito.mock(Syntaxpfadbehandlung.class);
        final Syntaxpfadersteller syntaxpfadersteller = Mockito.mock(Syntaxpfadersteller.class);
        final Wortabschluss wortabschluss = new Wortabschluss(syntaxpfadersteller, syntaxpfadbehandlung);
        try {
            wortabschluss.schliesseWortAb(null);
            fail();
        } catch (SyntaxparserException e) {
        }
    }

    @Test
    public void testSchliesseWortAbVerarbeitungNull() {
        final Grammatik grammatik = Mockito.mock(Grammatik.class);
        final Syntaxpfadbehandlung syntaxpfadbehandlung = new Syntaxpfadbehandlung(grammatik);
        final Syntaxpfadersteller syntaxpfadersteller = new Syntaxpfadersteller(grammatik);
        final Wortabschluss wortabschluss = new Wortabschluss(syntaxpfadersteller, syntaxpfadbehandlung);
        final Verarbeitungsstand verarbeitungsstand = Mockito.mock(Verarbeitungsstand.class);
        Mockito.when(verarbeitungsstand.gebeVerarbeitung()).thenReturn(null);
        assertTrue(wortabschluss.schliesseWortAb(verarbeitungsstand).isEmpty());
    }

    @Test
    public void testSchliesseWortAbIstZustandErreichtFalse() {
        final Grammatik grammatik = Mockito.mock(Grammatik.class);
        final Syntaxpfadbehandlung syntaxpfadbehandlung = new Syntaxpfadbehandlung(grammatik);
        final Syntaxpfadersteller syntaxpfadersteller = new Syntaxpfadersteller(grammatik);
        final Wortabschluss wortabschluss = new Wortabschluss(syntaxpfadersteller, syntaxpfadbehandlung);
        final Verarbeitungsstand verarbeitungsstand = Mockito.mock(Verarbeitungsstand.class);
        final Verarbeitung verarbeitung = Mockito.mock(Verarbeitung.class);
        Mockito.when(verarbeitung.istZustandErreicht()).thenReturn(false);
        Mockito.when(verarbeitungsstand.gebeVerarbeitung()).thenReturn(verarbeitung);
        assertTrue(wortabschluss.schliesseWortAb(verarbeitungsstand).isEmpty());
    }

    @Test
    public void testSchliesseWortAbIstZustandErreichtTrueAktuellNull() {
        final Grammatik grammatik = Mockito.mock(Grammatik.class);
        final Syntaxpfadbehandlung syntaxpfadbehandlung = new Syntaxpfadbehandlung(grammatik);
        final Syntaxpfadersteller syntaxpfadersteller = new Syntaxpfadersteller(grammatik);
        final Wortabschluss wortabschluss = new Wortabschluss(syntaxpfadersteller, syntaxpfadbehandlung);
        final Verarbeitungsstand verarbeitungsstand = Mockito.mock(Verarbeitungsstand.class);
        final Verarbeitung verarbeitung = Mockito.mock(Verarbeitung.class);
        Mockito.when(verarbeitung.istZustandErreicht()).thenReturn(true);
        Mockito.when(verarbeitung.gebeWort()).thenReturn("");
        final Syntaxpfadfolge syntaxpfadfolge = Mockito.mock(Syntaxpfadfolge.class);
        Mockito.when(syntaxpfadfolge.getAktuell()).thenReturn(null);
        Mockito.when(verarbeitungsstand.gebeSyntaxpfadfolge()).thenReturn(syntaxpfadfolge);
        Mockito.when(verarbeitungsstand.gebeVerarbeitung()).thenReturn(verarbeitung);
        assertTrue(wortabschluss.schliesseWortAb(verarbeitungsstand).isEmpty());
    }

    @Test
    public void testSchliesseWortAb1() {
        final List<String> regeln = Arrays.asList("S { Operand Operationsteil }\n", //
                "Operationsteil { Operator Operand }\n", //
                "Operationsteil { Operator Operand Operationsteil }\n", //
                "Operator (+-/)\n", // hier vor / ein Sternchen
                "Operand [09]\n");
        final Grammatik grammatik = erstelleGrammatik(regeln);
        final Syntaxpfadbehandlung syntaxpfadbehandlung = new Syntaxpfadbehandlung(grammatik);
        final Syntaxpfadersteller syntaxpfadersteller = new Syntaxpfadersteller(grammatik);
        final Wortabschluss wortabschluss = new Wortabschluss(syntaxpfadersteller, syntaxpfadbehandlung);
        final Verarbeitungsstand verarbeitungsstand = erstelleVerarbeitungsstand(syntaxpfadersteller, syntaxpfadbehandlung);
        verarbeitungsstand.gebeVerarbeitung().verarbeiteZeichen('1');
        final Set<Verarbeitungsstand> verarbeitungsstaende = wortabschluss.schliesseWortAb(verarbeitungsstand);
        assertNull(verarbeitungsstand.gebeVerarbeitung());
        assertNull(verarbeitungsstand.gebeSyntaxpfadfolge().getAktuell());
        assertEquals(2, verarbeitungsstaende.size());
        List<Verarbeitungsstand> verarbeitungsstaendeErgebnis = new ArrayList<>(verarbeitungsstaende);
        assertEquals(2, verarbeitungsstaendeErgebnis.size());
        assertTrue(verarbeitungsstaendeErgebnis.get(0).gebeVerarbeitung() instanceof VerarbeitungZeichenbereichUndMenge);
        assertTrue(verarbeitungsstaendeErgebnis.get(1).gebeVerarbeitung() instanceof VerarbeitungZeichenbereichUndMenge);
        assertEquals("Operator", verarbeitungsstaendeErgebnis.get(0).gebeSyntaxpfadfolge().getAktuell().gebeBlatt().getSymbolbezeichnung().getSymbolbezeichnung());
        assertEquals("Operator", verarbeitungsstaendeErgebnis.get(1).gebeSyntaxpfadfolge().getAktuell().gebeBlatt().getSymbolbezeichnung().getSymbolbezeichnung());
        final int zahl1 = verarbeitungsstaendeErgebnis.get(0).gebeSyntaxpfadfolge().getAktuell().gebeBlatt().getSymbolidentifizierung().getSymbolidentifizierung().intValue();
        final int zahl2 = verarbeitungsstaendeErgebnis.get(1).gebeSyntaxpfadfolge().getAktuell().gebeBlatt().getSymbolidentifizierung().getSymbolidentifizierung().intValue();
        assertTrue(zahl1 != zahl2);
        assertTrue((zahl1 == 3) || (zahl1 == 5));
        assertTrue((zahl2 == 3) || (zahl2 == 5));
    }

    @Test
    public void testSchliesseWortAb2() {
        final List<String> regeln = Arrays.asList("S { Zahl }\n", //
                "Zahl [09]\n");
        final Grammatik grammatik = erstelleGrammatik(regeln);
        final Syntaxpfadbehandlung syntaxpfadbehandlung = new Syntaxpfadbehandlung(grammatik);
        final Syntaxpfadersteller syntaxpfadersteller = new Syntaxpfadersteller(grammatik);
        final Wortabschluss wortabschluss = new Wortabschluss(syntaxpfadersteller, syntaxpfadbehandlung);
        final Verarbeitungsstand verarbeitungsstand = erstelleVerarbeitungsstand(syntaxpfadersteller, syntaxpfadbehandlung);
        verarbeitungsstand.gebeVerarbeitung().verarbeiteZeichen('1');
        Set<Verarbeitungsstand> verarbeitungsstaende = wortabschluss.schliesseWortAb(verarbeitungsstand);
        assertNull(verarbeitungsstand.gebeVerarbeitung());
        assertNull(verarbeitungsstand.gebeSyntaxpfadfolge().getAktuell());
        assertTrue(verarbeitungsstaende.isEmpty());
    }

    Grammatik erstelleGrammatik(final List<String> regeln) {
        final StringBuilder stringBuilder = new StringBuilder();
        for (final String regel : regeln) {
            stringBuilder.append(regel);
        }
        final String grammatiktext = stringBuilder.toString();
        final GrammatikAufbau grammatikAufbau = new GrammatikAufbau();
        final GrammatikLesen grammatikLesen = new GrammatikLesen(grammatikAufbau);
        for (int index = 0; index < grammatiktext.length(); index++) {
            grammatikLesen.verarbeiteZeichen(grammatiktext.charAt(index));
        }
        grammatikLesen.checkGrammatik();
        return grammatikLesen.getGrammatik();
    }

    Verarbeitungsstand erstelleVerarbeitungsstand(final Syntaxpfadersteller syntaxpfadersteller, final Syntaxpfadbehandlung syntaxpfadbehandlung) {
        final Set<Verarbeitungsstand> verarbeitungsstaende = new HashSet<>();
        final Set<Syntaxpfad> syntaxpfade = syntaxpfadersteller.ermittleSyntaxpfadeVonStartSymbol();
        for (final Syntaxpfad syntaxpfad : syntaxpfade) {
            final Syntaxpfadfolge syntaxpfadfolge = new Syntaxpfadfolge();
            syntaxpfadfolge.setzeAktuellenSyntaxpfad(syntaxpfad);
            verarbeitungsstaende.add(new Verarbeitungsstand(syntaxpfadfolge, syntaxpfadbehandlung.ermittleVerarbeitung(syntaxpfad.gebeBlatt())));
        }
        return new ArrayList<>(verarbeitungsstaende).get(0);
    }
}
