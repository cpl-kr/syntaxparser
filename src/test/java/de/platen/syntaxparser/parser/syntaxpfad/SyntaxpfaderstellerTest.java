package de.platen.syntaxparser.parser.syntaxpfad;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;

import de.platen.syntaxparser.parser.SyntaxparserException;
import de.platen.syntaxparser.grammatik.Grammatik;
import de.platen.syntaxparser.grammatik.GrammatikAufbau;
import de.platen.syntaxparser.grammatik.GrammatikLesen;
import de.platen.syntaxparser.grammatik.elemente.Symbolbezeichnung;
import de.platen.syntaxparser.grammatik.elemente.Symbolidentifizierung;
import de.platen.syntaxparser.grammatik.elemente.Symbolkennung;
import org.mockito.Mockito;

public class SyntaxpfaderstellerTest
{

    @Test(expected = SyntaxparserException.class)
    public void testKonstruktorparameterNull() {
        new Syntaxpfadersteller(null);
        fail();
    }

    @Test(expected = SyntaxparserException.class)
    public void testErmittleSyntaxpfadeParameterNull() {
        final List<String> regeln = Arrays.asList("S {S1 S2 S3}\n", "S1 \"test\"\n", "S2 [az]\n", "S3 (.,)");
        final Grammatik grammatik = erstelleGrammatik(regeln);
        final Syntaxpfadersteller syntaxpfadersteller = new Syntaxpfadersteller(grammatik);
        syntaxpfadersteller.ermittleSyntaxpfade(null);
        fail();
    }

    @Test(expected = SyntaxparserException.class)
    public void testErmittleSyntaxpfadeParameterNichtEnthalten() {
        final List<String> regeln = Arrays.asList("S {S1 S2 S3}\n", "S1 \"test\"\n", "S2 [az]\n", "S3 (.,)");
        final Grammatik grammatik = erstelleGrammatik(regeln);
        final Syntaxpfadersteller syntaxpfadersteller = new Syntaxpfadersteller(grammatik);
        syntaxpfadersteller.ermittleSyntaxpfade(
                new Symbolkennung(new Symbolbezeichnung("x"), new Symbolidentifizierung(1)));
        fail();
    }

    @Test
    public void testErmittleSyntaxpfade1() {
        final List<String> regeln = Arrays.asList("S {S1 S2 S3}\n", "S1 \"test\"\n", "S2 [az]\n", "S3 (.,)");
        final Grammatik grammatik = erstelleGrammatik(regeln);
        final Syntaxpfad syntaxpfad = new Syntaxpfad();
        syntaxpfad.zufuegenKnoten(
                new Symbolkennung(new Symbolbezeichnung("S"), new Symbolidentifizierung(0)));
        syntaxpfad.zufuegenBlatt(
                new Symbolkennung(new Symbolbezeichnung("S1"), new Symbolidentifizierung(1)));
        final Set<Syntaxpfad> vergleich = new HashSet<>();
        vergleich.add(syntaxpfad);
        final Syntaxpfadersteller syntaxpfadersteller = new Syntaxpfadersteller(grammatik);
        assertEquals(vergleich, syntaxpfadersteller.ermittleSyntaxpfade(
                new Symbolkennung(new Symbolbezeichnung("S"), new Symbolidentifizierung(0))));
    }

    @Test
    public void testErmittleSyntaxpfade2() {
        final List<String> regeln = Arrays.asList("S {S2 S1 S3}\n", "S1 \"test\"\n", "S2 [az]\n", "S3 (.,)");
        final Grammatik grammatik = erstelleGrammatik(regeln);
        final Syntaxpfad syntaxpfad = new Syntaxpfad();
        syntaxpfad.zufuegenKnoten(
                new Symbolkennung(new Symbolbezeichnung("S"), new Symbolidentifizierung(0)));
        syntaxpfad.zufuegenBlatt(
                new Symbolkennung(new Symbolbezeichnung("S2"), new Symbolidentifizierung(1)));
        final Set<Syntaxpfad> vergleich = new HashSet<>();
        vergleich.add(syntaxpfad);
        final Syntaxpfadersteller syntaxpfadersteller = new Syntaxpfadersteller(grammatik);
        assertEquals(vergleich, syntaxpfadersteller.ermittleSyntaxpfade(
                new Symbolkennung(new Symbolbezeichnung("S"), new Symbolidentifizierung(0))));
    }

    @Test
    public void testErmittleSyntaxpfade3() {
        final List<String> regeln = Arrays.asList("S {S3 S1 S2}\n", "S1 \"test\"\n", "S2 [az]\n", "S3 (.,)");
        final Grammatik grammatik = erstelleGrammatik(regeln);
        final Syntaxpfad syntaxpfad = new Syntaxpfad();
        syntaxpfad.zufuegenKnoten(
                new Symbolkennung(new Symbolbezeichnung("S"), new Symbolidentifizierung(0)));
        syntaxpfad.zufuegenBlatt(
                new Symbolkennung(new Symbolbezeichnung("S3"), new Symbolidentifizierung(1)));
        final Set<Syntaxpfad> vergleich = new HashSet<>();
        vergleich.add(syntaxpfad);
        final Syntaxpfadersteller syntaxpfadersteller = new Syntaxpfadersteller(grammatik);
        assertEquals(vergleich, syntaxpfadersteller.ermittleSyntaxpfade(
                new Symbolkennung(new Symbolbezeichnung("S"), new Symbolidentifizierung(0))));
    }

    @Test
    public void testErmittleSyntaxpfade4() {
        final List<String> regeln = Arrays.asList("S {S1 S2 S3}\n", "S1 {Z1 Z2 Z3\n}", "S1 {Z2 Z1 Z3\n}",
                "S2 {Z3 Z1 Z2\n}", "S3 {Z3 Z1 Z2\n}", "Z1 [az]\n", "Z2 \"Zeichenfolge\"\n", "Z3 (.,;:)");
        final Grammatik grammatik = erstelleGrammatik(regeln);
        final Syntaxpfad syntaxpfad1 = new Syntaxpfad();
        syntaxpfad1.zufuegenKnoten(
                new Symbolkennung(new Symbolbezeichnung("S"), new Symbolidentifizierung(0)));
        syntaxpfad1.zufuegenKnoten(
                new Symbolkennung(new Symbolbezeichnung("S1"), new Symbolidentifizierung(1)));
        syntaxpfad1.zufuegenBlatt(
                new Symbolkennung(new Symbolbezeichnung("Z1"), new Symbolidentifizierung(4)));
        final Syntaxpfad syntaxpfad2 = new Syntaxpfad();
        syntaxpfad2.zufuegenKnoten(
                new Symbolkennung(new Symbolbezeichnung("S"), new Symbolidentifizierung(0)));
        syntaxpfad2.zufuegenKnoten(
                new Symbolkennung(new Symbolbezeichnung("S1"), new Symbolidentifizierung(1)));
        syntaxpfad2.zufuegenBlatt(
                new Symbolkennung(new Symbolbezeichnung("Z2"), new Symbolidentifizierung(7)));
        final Set<Syntaxpfad> vergleich = new HashSet<>();
        vergleich.add(syntaxpfad1);
        vergleich.add(syntaxpfad2);
        final Syntaxpfadersteller syntaxpfadersteller = new Syntaxpfadersteller(grammatik);
        assertEquals(vergleich, syntaxpfadersteller.ermittleSyntaxpfade(
                new Symbolkennung(new Symbolbezeichnung("S"), new Symbolidentifizierung(0))));
    }

    @Test
    public void testErmittleSyntaxpfade5() {
        final List<String> regeln = Arrays.asList("S {S1 S2 S3}\n", "S1 {S4}", "S4 {Z1 Z2 Z3\n}", "S1 {Z2 Z1 Z3\n}",
                "S2 {Z3 Z1 Z2\n}", "S3 {Z3 Z1 Z2\n}", "Z1 [az]\n", "Z2 \"Zeichenfolge\"\n", "Z3 (.,;:)");
        final Grammatik grammatik = erstelleGrammatik(regeln);
        final Syntaxpfad syntaxpfad1 = new Syntaxpfad();
        syntaxpfad1.zufuegenKnoten(
                new Symbolkennung(new Symbolbezeichnung("S"), new Symbolidentifizierung(0)));
        syntaxpfad1.zufuegenKnoten(
                new Symbolkennung(new Symbolbezeichnung("S1"), new Symbolidentifizierung(1)));
        syntaxpfad1.zufuegenKnoten(
                new Symbolkennung(new Symbolbezeichnung("S4"), new Symbolidentifizierung(4)));
        syntaxpfad1.zufuegenBlatt(
                new Symbolkennung(new Symbolbezeichnung("Z1"), new Symbolidentifizierung(5)));
        final Syntaxpfad syntaxpfad2 = new Syntaxpfad();
        syntaxpfad2.zufuegenKnoten(
                new Symbolkennung(new Symbolbezeichnung("S"), new Symbolidentifizierung(0)));
        syntaxpfad2.zufuegenKnoten(
                new Symbolkennung(new Symbolbezeichnung("S1"), new Symbolidentifizierung(1)));
        syntaxpfad2.zufuegenBlatt(
                new Symbolkennung(new Symbolbezeichnung("Z2"), new Symbolidentifizierung(8)));
        final Set<Syntaxpfad> vergleich = new HashSet<>();
        vergleich.add(syntaxpfad1);
        vergleich.add(syntaxpfad2);
        final Syntaxpfadersteller syntaxpfadersteller = new Syntaxpfadersteller(grammatik);
        assertEquals(vergleich, syntaxpfadersteller.ermittleSyntaxpfade(
                new Symbolkennung(new Symbolbezeichnung("S"), new Symbolidentifizierung(0))));
    }

    @Test
    public void testErmittleSyntaxpfade6() {
        final List<String> regeln = Arrays.asList("S {S1 S2 S3}\n", "S1 <test>\n", "S2 [az]\n", "S3 (.,)");
        final Grammatik grammatik = erstelleGrammatik(regeln);
        final Syntaxpfad syntaxpfad = new Syntaxpfad();
        syntaxpfad.zufuegenKnoten(
                new Symbolkennung(new Symbolbezeichnung("S"), new Symbolidentifizierung(0)));
        syntaxpfad.zufuegenBlatt(
                new Symbolkennung(new Symbolbezeichnung("S1"), new Symbolidentifizierung(1)));
        final Set<Syntaxpfad> vergleich = new HashSet<>();
        vergleich.add(syntaxpfad);
        final Syntaxpfadersteller syntaxpfadersteller = new Syntaxpfadersteller(grammatik);
        assertEquals(vergleich, syntaxpfadersteller.ermittleSyntaxpfade(
                new Symbolkennung(new Symbolbezeichnung("S"), new Symbolidentifizierung(0))));
    }

    @Test
    public void testErmittleSyntaxpfadeVonStartsymbol() {
        final List<String> regeln = Arrays.asList("S {S1 S2 S3}\n", "S1 \"test\"\n", "S2 [az]\n", "S3 (.,)");
        final Grammatik grammatik = erstelleGrammatik(regeln);
        final Syntaxpfad syntaxpfad = new Syntaxpfad();
        syntaxpfad.zufuegenKnoten(
                new Symbolkennung(new Symbolbezeichnung("S"), new Symbolidentifizierung(0)));
        syntaxpfad.zufuegenBlatt(
                new Symbolkennung(new Symbolbezeichnung("S1"), new Symbolidentifizierung(1)));
        final Set<Syntaxpfad> vergleich = new HashSet<>();
        vergleich.add(syntaxpfad);
        final Syntaxpfadersteller syntaxpfadersteller = new Syntaxpfadersteller(grammatik);
        assertEquals(vergleich, syntaxpfadersteller.ermittleSyntaxpfadeVonStartSymbol());
    }

    @Test
    public void testBehandleNaechstenKnotenParameterNull() {
        final List<String> regeln = Arrays.asList("S {S1 S2 S3}\n", "S1 \"test\"\n", "S2 [az]\n", "S3 (.,)");
        final Grammatik grammatik = erstelleGrammatik(regeln);
        final List<SyntaxpfadMitWort> syntaxpfadeMitWort = Mockito.mock(List.class);
        final Syntaxpfad syntaxpfadNaechstesSymbol = Mockito.mock(Syntaxpfad.class);
        Syntaxpfadersteller syntaxpfadersteller = new Syntaxpfadersteller(grammatik);
        try {
            syntaxpfadersteller.behandleNaechstenKnoten(null, syntaxpfadNaechstesSymbol);
            fail();
        } catch (SyntaxparserException e) {
        }
        try {
            syntaxpfadersteller.behandleNaechstenKnoten(syntaxpfadeMitWort, null);
            fail();
        } catch (SyntaxparserException e) {
        }
    }

    @Test
    public void testBehandleNaechstenKnotenSyntaxpfadNaechstesSymbolIstFertigTrue() {
        final List<String> regeln = Arrays.asList("S {S1 S2 S3}\n", "S1 \"test\"\n", "S2 [az]\n", "S3 (.,)");
        final Grammatik grammatik = erstelleGrammatik(regeln);
        Syntaxpfadersteller syntaxpfadersteller = new Syntaxpfadersteller(grammatik);
        List<SyntaxpfadMitWort> syntaxpfadeMitWort = new ArrayList<>();
        Syntaxpfad syntaxpfad = new Syntaxpfad();
        syntaxpfad.zufuegenKnoten(new Symbolkennung(new Symbolbezeichnung("symbol1"), new Symbolidentifizierung(1)));
        syntaxpfad.zufuegenBlatt(new Symbolkennung(new Symbolbezeichnung("symbol2"), new Symbolidentifizierung(2)));
        syntaxpfadeMitWort.add(new SyntaxpfadMitWort(syntaxpfad, "wort"));
        assertTrue(syntaxpfadersteller.behandleNaechstenKnoten(syntaxpfadeMitWort, syntaxpfad).isEmpty());
    }

    @Test
    public void testBehandleNaechstenKnotensyntaxpfadNaechstesSymbolLeer() {
        final List<String> regeln = Arrays.asList("S {S1 S2 S3}\n", "S1 \"test\"\n", "S2 [az]\n", "S3 (.,)");
        final Grammatik grammatik = erstelleGrammatik(regeln);
        Syntaxpfadersteller syntaxpfadersteller = new Syntaxpfadersteller(grammatik);
        List<SyntaxpfadMitWort> syntaxpfadeMitWort = new ArrayList<>();
        Syntaxpfad syntaxpfad = new Syntaxpfad();
        syntaxpfad.zufuegenKnoten(new Symbolkennung(new Symbolbezeichnung("symbol1"), new Symbolidentifizierung(1)));
        syntaxpfad.zufuegenBlatt(new Symbolkennung(new Symbolbezeichnung("symbol2"), new Symbolidentifizierung(2)));
        syntaxpfadeMitWort.add(new SyntaxpfadMitWort(syntaxpfad, "wort"));
        assertTrue(syntaxpfadersteller.behandleNaechstenKnoten(syntaxpfadeMitWort, new Syntaxpfad()).isEmpty());
    }

    @Test
    public void testBehandleNaechstenKnoten() {
        final List<String> regeln = Arrays.asList("S {S1 S2}\n", "S1 \"test\"\n", "S2 {S3}", "S2 {S4}", "S3 [az]\n", "S4 (.,)");
        final Grammatik grammatik = erstelleGrammatik(regeln);
        Syntaxpfadersteller syntaxpfadersteller = new Syntaxpfadersteller(grammatik);
        List<SyntaxpfadMitWort> syntaxpfadeMitWort = new ArrayList<>();
        Syntaxpfad syntaxpfad = new Syntaxpfad();
        syntaxpfad.zufuegenKnoten(new Symbolkennung(new Symbolbezeichnung("S"), new Symbolidentifizierung(0)));
        syntaxpfad.zufuegenBlatt(new Symbolkennung(new Symbolbezeichnung("S1"), new Symbolidentifizierung(1)));
        syntaxpfadeMitWort.add(new SyntaxpfadMitWort(syntaxpfad, "wort"));
        Syntaxpfad syntaxpfadNaechsterKnoten = new Syntaxpfad();
        syntaxpfadNaechsterKnoten.zufuegenKnoten(new Symbolkennung(new Symbolbezeichnung("S"), new Symbolidentifizierung(0)));
        syntaxpfadNaechsterKnoten.zufuegenKnoten(new Symbolkennung(new Symbolbezeichnung("S2"), new Symbolidentifizierung(2)));
        Set<Syntaxpfadfolge> syntaxpfadfolgen = syntaxpfadersteller.behandleNaechstenKnoten(syntaxpfadeMitWort, syntaxpfadNaechsterKnoten);
        assertEquals(2, syntaxpfadfolgen.size());
        int anzahlGefunden = 0;
        for (Syntaxpfadfolge syntaxpfadfolge : syntaxpfadfolgen) {
            assertEquals(1, syntaxpfadfolge.getSyntaxpfadeMitWort().size());
            assertEquals(syntaxpfadeMitWort, syntaxpfadfolge.getSyntaxpfadeMitWort());
            Syntaxpfad syntaxpfadAktuell = syntaxpfadfolge.getAktuell();
            assertTrue(syntaxpfadAktuell.istFertig());
            assertEquals(2, syntaxpfadAktuell.gebeKnotenfolge().size());
            assertEquals("S", syntaxpfadAktuell.gebeKnotenfolge().get(0).getSymbolbezeichnung().getSymbolbezeichnung());
            assertEquals("S2", syntaxpfadAktuell.gebeKnotenfolge().get(1).getSymbolbezeichnung().getSymbolbezeichnung());
            if (syntaxpfadAktuell.gebeBlatt().getSymbolbezeichnung().getSymbolbezeichnung().equals("S3")) {
                anzahlGefunden++;
            }
            if (syntaxpfadAktuell.gebeBlatt().getSymbolbezeichnung().getSymbolbezeichnung().equals("S4")) {
                anzahlGefunden++;
            }
        }
        assertEquals(2, anzahlGefunden);
    }

    private Grammatik erstelleGrammatik(final List<String> regeln) {
        final StringBuilder stringBuilder = new StringBuilder();
        for (final String regel : regeln) {
            stringBuilder.append(regel);
        }
        final String grammatik = stringBuilder.toString();
        final GrammatikAufbau grammatikAufbau = new GrammatikAufbau();
        final GrammatikLesen grammatikLesen = new GrammatikLesen(grammatikAufbau);
        for (int index = 0; index < grammatik.length(); index++) {
            grammatikLesen.verarbeiteZeichen(grammatik.charAt(index));
        }
        grammatikLesen.checkGrammatik();
        return grammatikLesen.getGrammatik();
    }
}
