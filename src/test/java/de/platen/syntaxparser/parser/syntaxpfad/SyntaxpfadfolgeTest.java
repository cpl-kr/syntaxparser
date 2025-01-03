package de.platen.syntaxparser.parser.syntaxpfad;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.mockito.Mockito;

import de.platen.syntaxparser.parser.SyntaxparserException;
import de.platen.syntaxparser.grammatik.elemente.Symbolbezeichnung;
import de.platen.syntaxparser.grammatik.elemente.Symbolidentifizierung;
import de.platen.syntaxparser.grammatik.elemente.Symbolkennung;

public class SyntaxpfadfolgeTest
{

    @Test(expected = SyntaxparserException.class)
    public void testKonstruktorParameterNull() {
        new Syntaxpfadfolge(null);
    }

    @Test
    public void testKonstruktor() {
        final List<SyntaxpfadMitWort> syntaxpfade = erzeugeSyntaxpfadeMitWort();
        final Syntaxpfadfolge syntaxpfadfolge = new Syntaxpfadfolge(syntaxpfade);
        assertEquals(syntaxpfade, syntaxpfadfolge.getSyntaxpfadeMitWort());
        assertEquals("wort".length(), syntaxpfadfolge.getGesamtlaenge());
    }

    @Test(expected = SyntaxparserException.class)
    public void testSetzeAktuellenSyntaxpfadParameterNull() {
        final Syntaxpfadfolge syntaxpfadfolge = new Syntaxpfadfolge(erzeugeSyntaxpfadeMitWort());
        syntaxpfadfolge.setzeAktuellenSyntaxpfad(null);
    }

    @Test(expected = SyntaxparserException.class)
    public void testSetzeAktuellenSyntaxpfadAktuellNichtNull() {
        final Syntaxpfadfolge syntaxpfadfolge = new Syntaxpfadfolge(erzeugeSyntaxpfadeMitWort());
        final Syntaxpfad syntaxpfad1 = Mockito.mock(Syntaxpfad.class);
        Mockito.when(syntaxpfad1.istFertig()).thenReturn(true);
        Mockito.when(syntaxpfad1.kopiere()).thenReturn(new Syntaxpfad());
        final Syntaxpfad syntaxpfad2 = Mockito.mock(Syntaxpfad.class);
        Mockito.when(syntaxpfad2.istFertig()).thenReturn(true);
        Mockito.when(syntaxpfad2.kopiere()).thenReturn(new Syntaxpfad());
        syntaxpfadfolge.setzeAktuellenSyntaxpfad(syntaxpfad1);
        syntaxpfadfolge.setzeAktuellenSyntaxpfad(syntaxpfad2);
    }

    @Test(expected = SyntaxparserException.class)
    public void testSetzeAktuellenSyntaxpfadSyntaxpfadNichtFertig() {
        final Syntaxpfadfolge syntaxpfadfolge = new Syntaxpfadfolge(erzeugeSyntaxpfadeMitWort());
        syntaxpfadfolge.setzeAktuellenSyntaxpfad(new Syntaxpfad());
    }

    @Test
    public void testSetzeAktuellenSyntaxpfad() {
        final Syntaxpfadfolge syntaxpfadfolge = new Syntaxpfadfolge(erzeugeSyntaxpfadeMitWort());
        final Syntaxpfad syntaxpfad = Mockito.mock(Syntaxpfad.class);
        Mockito.when(syntaxpfad.istFertig()).thenReturn(true);
        syntaxpfadfolge.setzeAktuellenSyntaxpfad(syntaxpfad);
    }

    @Test(expected = SyntaxparserException.class)
    public void testUebernehmeAktuellenSyntaxpfadParameterNull() {
        final Syntaxpfadfolge syntaxpfadfolge = new Syntaxpfadfolge(erzeugeSyntaxpfadeMitWort());
        final Syntaxpfad syntaxpfad = Mockito.mock(Syntaxpfad.class);
        Mockito.when(syntaxpfad.istFertig()).thenReturn(true);
        syntaxpfadfolge.setzeAktuellenSyntaxpfad(syntaxpfad);
        syntaxpfadfolge.uebernehmeAktuellenSyntaxpfad(null);
    }

    @Test(expected = SyntaxparserException.class)
    public void testUebernehmeAktuellenSyntaxpfadWortBlank() {
        final Syntaxpfadfolge syntaxpfadfolge = new Syntaxpfadfolge(erzeugeSyntaxpfadeMitWort());
        final Syntaxpfad syntaxpfad = Mockito.mock(Syntaxpfad.class);
        Mockito.when(syntaxpfad.istFertig()).thenReturn(true);
        syntaxpfadfolge.setzeAktuellenSyntaxpfad(syntaxpfad);
        syntaxpfadfolge.uebernehmeAktuellenSyntaxpfad(" ");
    }

    @Test(expected = SyntaxparserException.class)
    public void testUebernehmeAktuellenSyntaxpfadAktuellNull() {
        final Syntaxpfadfolge syntaxpfadfolge = new Syntaxpfadfolge(erzeugeSyntaxpfadeMitWort());
        syntaxpfadfolge.uebernehmeAktuellenSyntaxpfad("wort");
    }

    @Test
    public void testUebernehmeAktuellenSyntaxpfad() {
        final Syntaxpfadfolge syntaxpfadfolge = new Syntaxpfadfolge();
        final Syntaxpfad syntaxpfad1 = new Syntaxpfad();
        syntaxpfad1.zufuegenKnoten(
                new Symbolkennung(new Symbolbezeichnung("symbol1"), new Symbolidentifizierung(1)));
        syntaxpfad1.zufuegenBlatt(
                new Symbolkennung(new Symbolbezeichnung("symbol2"), new Symbolidentifizierung(2)));
        syntaxpfadfolge.setzeAktuellenSyntaxpfad(syntaxpfad1);
        syntaxpfadfolge.uebernehmeAktuellenSyntaxpfad("wort");
        assertNull(syntaxpfadfolge.getAktuell());
        assertEquals("wort".length(), syntaxpfadfolge.getGesamtlaenge());
        final List<SyntaxpfadMitWort> syntaxpfadeMitWort = new ArrayList<>();
        final Syntaxpfad syntaxpfad2 = new Syntaxpfad();
        syntaxpfad2.zufuegenKnoten(
                new Symbolkennung(new Symbolbezeichnung("symbol1"), new Symbolidentifizierung(1)));
        syntaxpfad2.zufuegenBlatt(
                new Symbolkennung(new Symbolbezeichnung("symbol2"), new Symbolidentifizierung(2)));
        final SyntaxpfadMitWort syntaxpfadMitWort = new SyntaxpfadMitWort(syntaxpfad2, "wort");
        syntaxpfadeMitWort.add(syntaxpfadMitWort);
        assertEquals(syntaxpfadeMitWort, syntaxpfadfolge.getSyntaxpfadeMitWort());
    }

    @Test
    public void testUebernehmeAktuellenSyntaxpfadZweimal() {
        final Syntaxpfadfolge syntaxpfadfolge = new Syntaxpfadfolge();
        final Syntaxpfad syntaxpfad1 = new Syntaxpfad();
        syntaxpfad1.zufuegenKnoten(
                new Symbolkennung(new Symbolbezeichnung("symbol1"), new Symbolidentifizierung(1)));
        syntaxpfad1.zufuegenBlatt(
                new Symbolkennung(new Symbolbezeichnung("symbol2"), new Symbolidentifizierung(2)));
        syntaxpfadfolge.setzeAktuellenSyntaxpfad(syntaxpfad1);
        syntaxpfadfolge.uebernehmeAktuellenSyntaxpfad("wort1");
        assertEquals("wort1".length(), syntaxpfadfolge.getGesamtlaenge());
        final Syntaxpfad syntaxpfad2 = new Syntaxpfad();
        syntaxpfad2.zufuegenKnoten(
                new Symbolkennung(new Symbolbezeichnung("symbol3"), new Symbolidentifizierung(3)));
        syntaxpfad2.zufuegenBlatt(
                new Symbolkennung(new Symbolbezeichnung("symbol4"), new Symbolidentifizierung(4)));
        syntaxpfadfolge.setzeAktuellenSyntaxpfad(syntaxpfad2);
        syntaxpfadfolge.uebernehmeAktuellenSyntaxpfad("wort2");
        assertEquals("wort1".length() + "wort2".length(), syntaxpfadfolge.getGesamtlaenge());
    }

    private List<SyntaxpfadMitWort> erzeugeSyntaxpfadeMitWort() {
        final List<SyntaxpfadMitWort> syntaxpfade = new ArrayList<>();
        final Syntaxpfad syntaxpfad = Mockito.mock(Syntaxpfad.class);
        Mockito.when(syntaxpfad.istFertig()).thenReturn(true);
        final SyntaxpfadMitWort syntaxpfadMitWort = new SyntaxpfadMitWort(syntaxpfad, "wort");
        syntaxpfade.add(syntaxpfadMitWort);
        return syntaxpfade;
    }
}
