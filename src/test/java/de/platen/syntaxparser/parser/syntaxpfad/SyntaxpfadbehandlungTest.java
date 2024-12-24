package de.platen.syntaxparser.parser.syntaxpfad;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.platen.syntaxparser.parser.regelverarbeitung.Verarbeitung;
import de.platen.syntaxparser.parser.regelverarbeitung.VerarbeitungRegEx;
import de.platen.syntaxparser.parser.regelverarbeitung.VerarbeitungZeichenbereichUndMenge;
import de.platen.syntaxparser.parser.regelverarbeitung.VerarbeitungZeichenfolge;
import org.junit.Test;
import org.mockito.Mockito;

import de.platen.syntaxparser.parser.SyntaxparserException;
import de.platen.syntaxparser.grammatik.Grammatik;
import de.platen.syntaxparser.grammatik.GrammatikAufbau;
import de.platen.syntaxparser.grammatik.GrammatikLesen;
import de.platen.syntaxparser.grammatik.elemente.Kardinalitaet;
import de.platen.syntaxparser.grammatik.elemente.RegEx;
import de.platen.syntaxparser.grammatik.elemente.RegExregeln;
import de.platen.syntaxparser.grammatik.elemente.RegelSymbole;
import de.platen.syntaxparser.grammatik.elemente.Symbol;
import de.platen.syntaxparser.grammatik.elemente.Symbolbezeichnung;
import de.platen.syntaxparser.grammatik.elemente.Symbolidentifizierung;
import de.platen.syntaxparser.grammatik.elemente.Symbolkennung;
import de.platen.syntaxparser.grammatik.elemente.Symbolregeln;
import de.platen.syntaxparser.grammatik.elemente.Zeichenbereich;
import de.platen.syntaxparser.grammatik.elemente.Zeichenbereichregeln;
import de.platen.syntaxparser.grammatik.elemente.Zeichenfolge;
import de.platen.syntaxparser.grammatik.elemente.Zeichenfolgeregeln;
import de.platen.syntaxparser.grammatik.elemente.Zeichenmenge;
import de.platen.syntaxparser.grammatik.elemente.Zeichenmengeregeln;

public class SyntaxpfadbehandlungTest
{

    @Test(expected = SyntaxparserException.class)
    public void testKonstruktorParameterNull() {
        new Syntaxpfadbehandlung(null);
        fail();
    }

    @Test(expected = SyntaxparserException.class)
    public void testErmittleVerarbeitungParameterNull() {
        final Grammatik grammatik = Mockito.mock(Grammatik.class);
        new Syntaxpfadbehandlung(grammatik).ermittleVerarbeitung(null);
        fail();
    }

    @Test
    public void testErmittleVerarbeitungZeichenfolgeregeln() {
        final Grammatik grammatik = Mockito.mock(Grammatik.class);
        Symbolkennung symbolkennung = Mockito.mock(Symbolkennung.class);
        Symbolbezeichnung symbolbezeichnung = new Symbolbezeichnung("symbolbezeichnung");
        Mockito.when(symbolkennung.getSymbolbezeichnung()).thenReturn(symbolbezeichnung);
        Map<Symbolbezeichnung, Set<Zeichenfolge>> zeichenfolgen = new HashMap<>();
        Set<Zeichenfolge> zeichenfolge = new HashSet<>();
        zeichenfolge.add(new Zeichenfolge("zeichenfolge"));
        zeichenfolgen.put(symbolbezeichnung, zeichenfolge);
        Zeichenfolgeregeln zeichenfolgeregeln = Mockito.mock(Zeichenfolgeregeln.class);
        Mockito.when(grammatik.getZeichenfolgeregeln()).thenReturn(zeichenfolgeregeln);
        Mockito.when(zeichenfolgeregeln.get()).thenReturn(zeichenfolgen);
        Verarbeitung verarbeitung = new Syntaxpfadbehandlung(grammatik).ermittleVerarbeitung(symbolkennung);
        assertTrue(verarbeitung instanceof VerarbeitungZeichenfolge);
        assertEquals(symbolbezeichnung, verarbeitung.gebeSymbolbezeichnung());
    }

    @Test
    public void testErmittleVerarbeitungRegExregeln() {
        final Grammatik grammatik = Mockito.mock(Grammatik.class);
        Symbolkennung symbolkennung = Mockito.mock(Symbolkennung.class);
        Symbolbezeichnung symbolbezeichnung = new Symbolbezeichnung("symbolbezeichnung");
        Mockito.when(symbolkennung.getSymbolbezeichnung()).thenReturn(symbolbezeichnung);
        Map<Symbolbezeichnung, Set<Zeichenfolge>> zeichenfolgen = new HashMap<>();
        Zeichenfolgeregeln zeichenfolgeregeln = Mockito.mock(Zeichenfolgeregeln.class);
        Mockito.when(grammatik.getZeichenfolgeregeln()).thenReturn(zeichenfolgeregeln);
        Mockito.when(zeichenfolgeregeln.get()).thenReturn(zeichenfolgen);
        RegExregeln regExregeln = Mockito.mock(RegExregeln.class);
        Map<Symbolbezeichnung, Set<RegEx>> regExMap = new HashMap<>();
        Set<RegEx> regExSet = new HashSet<>();
        regExSet.add(new RegEx("[a-z]"));
        regExMap.put(symbolbezeichnung, regExSet);
        Mockito.when(regExregeln.get()).thenReturn(regExMap);
        Mockito.when(grammatik.getRegExregeln()).thenReturn(regExregeln);
        Verarbeitung verarbeitung = new Syntaxpfadbehandlung(grammatik).ermittleVerarbeitung(symbolkennung);
        assertTrue(verarbeitung instanceof VerarbeitungRegEx);
        assertEquals(symbolbezeichnung, verarbeitung.gebeSymbolbezeichnung());
    }

    @Test
    public void testErmittleVerarbeitungZeichenbereichUndMengeregeln() {
        final Grammatik grammatik = Mockito.mock(Grammatik.class);
        Symbolkennung symbolkennung = Mockito.mock(Symbolkennung.class);
        Symbolbezeichnung symbolbezeichnung = new Symbolbezeichnung("symbolbezeichnung");
        Mockito.when(symbolkennung.getSymbolbezeichnung()).thenReturn(symbolbezeichnung);
        Map<Symbolbezeichnung, Set<Zeichenfolge>> zeichenfolgen = new HashMap<>();
        Zeichenfolgeregeln zeichenfolgeregeln = Mockito.mock(Zeichenfolgeregeln.class);
        Mockito.when(grammatik.getZeichenfolgeregeln()).thenReturn(zeichenfolgeregeln);
        Mockito.when(zeichenfolgeregeln.get()).thenReturn(zeichenfolgen);
        RegExregeln regExregeln = Mockito.mock(RegExregeln.class);
        Map<Symbolbezeichnung, Set<RegEx>> regExMap = new HashMap<>();
        Mockito.when(regExregeln.get()).thenReturn(regExMap);
        Mockito.when(grammatik.getRegExregeln()).thenReturn(regExregeln);
        Zeichenbereichregeln zeichenbereichregeln = Mockito.mock(Zeichenbereichregeln.class);
        Map<Symbolbezeichnung, Set<Zeichenbereich>> zeichenbereichMap = new HashMap<>();
        Set<Zeichenbereich> zeichenbereichSet = new HashSet<>();
        zeichenbereichSet.add(new Zeichenbereich('a','z'));
        zeichenbereichMap.put(symbolbezeichnung, zeichenbereichSet);
        Mockito.when(zeichenbereichregeln.get()).thenReturn(zeichenbereichMap);
        Zeichenmengeregeln zeichenmengeregeln = Mockito.mock(Zeichenmengeregeln.class);
        Map<Symbolbezeichnung, Set<Zeichenmenge>> zeichenmengeMap = new HashMap<>();
        Mockito.when(zeichenmengeregeln.get()).thenReturn(zeichenmengeMap);
        Mockito.when(grammatik.getZeichenbereichregeln()).thenReturn(zeichenbereichregeln);
        Mockito.when(grammatik.getZeichenmengeregeln()).thenReturn(zeichenmengeregeln);
        Verarbeitung verarbeitung = new Syntaxpfadbehandlung(grammatik).ermittleVerarbeitung(symbolkennung);
        assertTrue(verarbeitung instanceof VerarbeitungZeichenbereichUndMenge);
        assertEquals(symbolbezeichnung, verarbeitung.gebeSymbolbezeichnung());
    }

    @Test(expected = SyntaxparserException.class)
    public void testFindePassendeSytaxpfadeParameterSyntaxpfadeNull() {
        final Grammatik grammatik = Mockito.mock(Grammatik.class);
        new Syntaxpfadbehandlung(grammatik).findePassendeSyntaxpfade(null, "wort");
        fail();
    }

    @Test(expected = SyntaxparserException.class)
    public void testFindePassendeSytaxpfadeParameterWortNull() {
        final Grammatik grammatik = Mockito.mock(Grammatik.class);
        new Syntaxpfadbehandlung(grammatik).findePassendeSyntaxpfade(new HashSet<>(), null);
        fail();
    }

    @Test
    public void testFindePassendeSytaxpfadeWortNichtGefunden() {
        final Grammatik grammatik = mockGrammatik(new HashMap<>(), new HashMap<>(), new HashMap<>(), new HashMap<>());
        final Syntaxpfad syntaxpfad = Mockito.mock(Syntaxpfad.class);
        Mockito.when(syntaxpfad.gebeBlatt()).thenReturn(
                new Symbolkennung(new Symbolbezeichnung("nichts"), new Symbolidentifizierung(1)));
        final Set<Syntaxpfad> syntaxpfade = new HashSet<>();
        syntaxpfade.add(syntaxpfad);
        final Syntaxpfadbehandlung syntaxpfadbehandlung = new Syntaxpfadbehandlung(grammatik);
        assertTrue(syntaxpfadbehandlung.findePassendeSyntaxpfade(syntaxpfade, "wort").isEmpty());
    }

    @Test
    public void testFindePassendeSytaxpfadeWortInZeichenfolge() {
        final Set<Zeichenfolge> zeichenfolgen = new HashSet<>();
        final Zeichenfolge zeichenfolge = new Zeichenfolge("zeichenfolge");
        zeichenfolgen.add(zeichenfolge);
        final Map<Symbolbezeichnung, Set<Zeichenfolge>> mapZeichenfolgen = new HashMap<>();
        mapZeichenfolgen.put(new Symbolbezeichnung("symbol"), zeichenfolgen);
        final Grammatik grammatik = mockGrammatik(new HashMap<>(), new HashMap<>(), mapZeichenfolgen, new HashMap<>());
        final Syntaxpfad syntaxpfad = new Syntaxpfad();
        syntaxpfad.zufuegenBlatt(
                new Symbolkennung(new Symbolbezeichnung("symbol"), new Symbolidentifizierung(1)));
        final Set<Syntaxpfad> syntaxpfade = new HashSet<>();
        syntaxpfade.add(syntaxpfad);
        final Syntaxpfadbehandlung syntaxpfadbehandlung = new Syntaxpfadbehandlung(grammatik);
        assertEquals(syntaxpfade, syntaxpfadbehandlung.findePassendeSyntaxpfade(syntaxpfade, "zeichenfolge"));
    }

    @Test
    public void testFindePassendeSytaxpfadeWortInRegEx() {
        final Set<RegEx> mengeRegEx = new HashSet<>();
        final RegEx regEx = new RegEx("regEx");
        mengeRegEx.add(regEx);
        final Map<Symbolbezeichnung, Set<RegEx>> mapRegEx = new HashMap<>();
        mapRegEx.put(new Symbolbezeichnung("symbol"), mengeRegEx);
        final Grammatik grammatik = mockGrammatik(new HashMap<>(), new HashMap<>(), new HashMap<>(), mapRegEx);
        final Syntaxpfad syntaxpfad = new Syntaxpfad();
        syntaxpfad.zufuegenBlatt(
                new Symbolkennung(new Symbolbezeichnung("symbol"), new Symbolidentifizierung(1)));
        final Set<Syntaxpfad> syntaxpfade = new HashSet<>();
        syntaxpfade.add(syntaxpfad);
        final Syntaxpfadbehandlung syntaxpfadbehandlung = new Syntaxpfadbehandlung(grammatik);
        assertEquals(syntaxpfade, syntaxpfadbehandlung.findePassendeSyntaxpfade(syntaxpfade, "regEx"));
    }

    @Test
    public void testFindePassendeSytaxpfadeWortInZeichenbereich() {
        final Set<Zeichenbereich> zeichenbereiche = new HashSet<>();
        final Zeichenbereich zeichenbereich = new Zeichenbereich('a','c');
        zeichenbereiche.add(zeichenbereich);
        final Map<Symbolbezeichnung, Set<Zeichenbereich>> mapZeichenbereiche = new HashMap<>();
        mapZeichenbereiche.put(new Symbolbezeichnung("symbol"), zeichenbereiche);
        final Grammatik grammatik = mockGrammatik(mapZeichenbereiche, new HashMap<>(), new HashMap<>(),
                new HashMap<>());
        final Syntaxpfad syntaxpfad = new Syntaxpfad();
        syntaxpfad.zufuegenBlatt(
                new Symbolkennung(new Symbolbezeichnung("symbol"), new Symbolidentifizierung(1)));
        final Set<Syntaxpfad> syntaxpfade = new HashSet<>();
        syntaxpfade.add(syntaxpfad);
        final Syntaxpfadbehandlung syntaxpfadbehandlung = new Syntaxpfadbehandlung(grammatik);
        assertEquals(syntaxpfade, syntaxpfadbehandlung.findePassendeSyntaxpfade(syntaxpfade, "abc"));
    }

    @Test
    public void testFindePassendeSytaxpfadeWortInZeichenmenge() {
        final Set<Zeichenmenge> zeichenmengen = new HashSet<>();
        final Set<Character> characters = new HashSet<>();
        characters.add(',');
        characters.add('.');
        final Zeichenmenge zeichenmenge = new Zeichenmenge(characters);
        zeichenmengen.add(zeichenmenge);
        final Map<Symbolbezeichnung, Set<Zeichenmenge>> mapZeichenmengen = new HashMap<>();
        mapZeichenmengen.put(new Symbolbezeichnung("symbol"), zeichenmengen);
        final Grammatik grammatik = mockGrammatik(new HashMap<>(), mapZeichenmengen, new HashMap<>(), new HashMap<>());
        final Syntaxpfad syntaxpfad = new Syntaxpfad();
        syntaxpfad.zufuegenBlatt(
                new Symbolkennung(new Symbolbezeichnung("symbol"), new Symbolidentifizierung(1)));
        final Set<Syntaxpfad> syntaxpfade = new HashSet<>();
        syntaxpfade.add(syntaxpfad);
        final Syntaxpfadbehandlung syntaxpfadbehandlung = new Syntaxpfadbehandlung(grammatik);
        assertEquals(syntaxpfade, syntaxpfadbehandlung.findePassendeSyntaxpfade(syntaxpfade, ",..,"));
    }

    @Test
    public void testFindePassendeSytaxpfadeWortInZeichenbereichUndZeichenmenge() {
        final Set<Zeichenbereich> zeichenbereiche = new HashSet<>();
        final Zeichenbereich zeichenbereich = new Zeichenbereich('a', 'c');
        zeichenbereiche.add(zeichenbereich);
        final Map<Symbolbezeichnung, Set<Zeichenbereich>> mapZeichenbereiche = new HashMap<>();
        mapZeichenbereiche.put(new Symbolbezeichnung("symbol"), zeichenbereiche);
        final Set<Zeichenmenge> zeichenmengen = new HashSet<>();
        final Set<Character> characters = new HashSet<>();
        characters.add(',');
        characters.add('.');
        final Zeichenmenge zeichenmenge = new Zeichenmenge(characters);
        zeichenmengen.add(zeichenmenge);
        final Map<Symbolbezeichnung, Set<Zeichenmenge>> mapZeichenmengen = new HashMap<>();
        mapZeichenmengen.put(new Symbolbezeichnung("symbol"), zeichenmengen);
        final Grammatik grammatik = mockGrammatik(mapZeichenbereiche, mapZeichenmengen, new HashMap<>(),
                new HashMap<>());
        final Syntaxpfad syntaxpfad = new Syntaxpfad();
        syntaxpfad.zufuegenBlatt(
                new Symbolkennung(new Symbolbezeichnung("symbol"), new Symbolidentifizierung(1)));
        final Set<Syntaxpfad> syntaxpfade = new HashSet<>();
        syntaxpfade.add(syntaxpfad);
        final Syntaxpfadbehandlung syntaxpfadbehandlung = new Syntaxpfadbehandlung(grammatik);
        assertEquals(syntaxpfade, syntaxpfadbehandlung.findePassendeSyntaxpfade(syntaxpfade, ",.b"));
    }

    @Test(expected = SyntaxparserException.class)
    public void testfindeNaechstesSymbolParameterNull() {
        final Grammatik grammatik = Mockito.mock(Grammatik.class);
        new Syntaxpfadbehandlung(grammatik).findeNaechstesSymbol(null);
        fail();
    }

    @Test
    public void testFindeNaechstesSymbolInnerhalbDerRegelAlsBlatt() {
        final Grammatik grammatik = mockGrammatik();
        final Syntaxpfad syntaxpfadAktuell = new Syntaxpfad();
        syntaxpfadAktuell.zufuegenKnoten(
                new Symbolkennung(new Symbolbezeichnung("S"), new Symbolidentifizierung(0)));
        syntaxpfadAktuell.zufuegenKnoten(
                new Symbolkennung(new Symbolbezeichnung("S1"), new Symbolidentifizierung(1)));
        syntaxpfadAktuell.zufuegenBlatt(
                new Symbolkennung(new Symbolbezeichnung("Z1-1"), new Symbolidentifizierung(110)));
        final Syntaxpfad syntaxpfadErwartet = new Syntaxpfad();
        syntaxpfadErwartet.zufuegenKnoten(
                new Symbolkennung(new Symbolbezeichnung("S"), new Symbolidentifizierung(0)));
        syntaxpfadErwartet.zufuegenKnoten(
                new Symbolkennung(new Symbolbezeichnung("S1"), new Symbolidentifizierung(1)));
        syntaxpfadErwartet.zufuegenBlatt(
                new Symbolkennung(new Symbolbezeichnung("Z1-2"), new Symbolidentifizierung(120)));
        final Syntaxpfadbehandlung syntaxpfadbehandlung = new Syntaxpfadbehandlung(grammatik);
        final Syntaxpfad syntaxpfadErgebnis = syntaxpfadbehandlung.findeNaechstesSymbol(syntaxpfadAktuell);
        assertEquals(syntaxpfadErwartet, syntaxpfadErgebnis);
    }

    @Test
    public void testFindeNaechstesSymbolInnerhalbDerRegelNichtAlsBlatt() {
        final Grammatik grammatik = mockGrammatik();
        final Syntaxpfad syntaxpfadAktuell = new Syntaxpfad();
        syntaxpfadAktuell.zufuegenKnoten(
                new Symbolkennung(new Symbolbezeichnung("S"), new Symbolidentifizierung(0)));
        syntaxpfadAktuell.zufuegenKnoten(
                new Symbolkennung(new Symbolbezeichnung("S2"), new Symbolidentifizierung(2)));
        syntaxpfadAktuell.zufuegenBlatt(
                new Symbolkennung(new Symbolbezeichnung("Z2-1"), new Symbolidentifizierung(210)));
        final Syntaxpfad syntaxpfadErwartet = new Syntaxpfad();
        syntaxpfadErwartet.zufuegenKnoten(
                new Symbolkennung(new Symbolbezeichnung("S"), new Symbolidentifizierung(0)));
        syntaxpfadErwartet.zufuegenKnoten(
                new Symbolkennung(new Symbolbezeichnung("S2"), new Symbolidentifizierung(2)));
        syntaxpfadErwartet.zufuegenKnoten(
                new Symbolkennung(new Symbolbezeichnung("S2-2"), new Symbolidentifizierung(22)));
        final Syntaxpfadbehandlung syntaxpfadbehandlung = new Syntaxpfadbehandlung(grammatik);
        final Syntaxpfad syntaxpfadErgebnis = syntaxpfadbehandlung.findeNaechstesSymbol(syntaxpfadAktuell);
        assertEquals(syntaxpfadErwartet, syntaxpfadErgebnis);
    }

    @Test
    public void testFindeNaechstesSymbolEineEbeneHoeherAlsBlatt() {
        final Grammatik grammatik = mockGrammatik();
        final Syntaxpfad syntaxpfadAktuell = new Syntaxpfad();
        syntaxpfadAktuell.zufuegenKnoten(
                new Symbolkennung(new Symbolbezeichnung("S"), new Symbolidentifizierung(0)));
        syntaxpfadAktuell.zufuegenKnoten(
                new Symbolkennung(new Symbolbezeichnung("S3"), new Symbolidentifizierung(3)));
        syntaxpfadAktuell.zufuegenKnoten(
                new Symbolkennung(new Symbolbezeichnung("S3-1"), new Symbolidentifizierung(31)));
        syntaxpfadAktuell.zufuegenBlatt(
                new Symbolkennung(new Symbolbezeichnung("Z3-1-2"), new Symbolidentifizierung(3120)));
        final Syntaxpfad syntaxpfadErwartet = new Syntaxpfad();
        syntaxpfadErwartet.zufuegenKnoten(
                new Symbolkennung(new Symbolbezeichnung("S"), new Symbolidentifizierung(0)));
        syntaxpfadErwartet.zufuegenKnoten(
                new Symbolkennung(new Symbolbezeichnung("S3"), new Symbolidentifizierung(3)));
        syntaxpfadErwartet.zufuegenBlatt(
                new Symbolkennung(new Symbolbezeichnung("Z3-2"), new Symbolidentifizierung(320)));
        final Syntaxpfadbehandlung syntaxpfadbehandlung = new Syntaxpfadbehandlung(grammatik);
        final Syntaxpfad syntaxpfadErgebnis = syntaxpfadbehandlung.findeNaechstesSymbol(syntaxpfadAktuell);
        assertEquals(syntaxpfadErwartet, syntaxpfadErgebnis);
    }

    @Test
    public void testFindeNaechstesSymbolEineEbeneHoeherNichtAlsBlatt() {
        final Grammatik grammatik = mockGrammatik();
        final Syntaxpfad syntaxpfadAktuell = new Syntaxpfad();
        syntaxpfadAktuell.zufuegenKnoten(
                new Symbolkennung(new Symbolbezeichnung("S"), new Symbolidentifizierung(0)));
        syntaxpfadAktuell.zufuegenKnoten(
                new Symbolkennung(new Symbolbezeichnung("S4"), new Symbolidentifizierung(4)));
        syntaxpfadAktuell.zufuegenKnoten(
                new Symbolkennung(new Symbolbezeichnung("S4-1"), new Symbolidentifizierung(41)));
        syntaxpfadAktuell.zufuegenBlatt(
                new Symbolkennung(new Symbolbezeichnung("Z4-1-2"), new Symbolidentifizierung(4120)));
        final Syntaxpfad syntaxpfadErwartet = new Syntaxpfad();
        syntaxpfadErwartet.zufuegenKnoten(
                new Symbolkennung(new Symbolbezeichnung("S"), new Symbolidentifizierung(0)));
        syntaxpfadErwartet.zufuegenKnoten(
                new Symbolkennung(new Symbolbezeichnung("S4"), new Symbolidentifizierung(4)));
        syntaxpfadErwartet.zufuegenKnoten(
                new Symbolkennung(new Symbolbezeichnung("S4-2"), new Symbolidentifizierung(42)));
        final Syntaxpfadbehandlung syntaxpfadbehandlung = new Syntaxpfadbehandlung(grammatik);
        final Syntaxpfad syntaxpfadErgebnis = syntaxpfadbehandlung.findeNaechstesSymbol(syntaxpfadAktuell);
        assertEquals(syntaxpfadErwartet, syntaxpfadErgebnis);
    }

    @Test
    public void testFindeNaechstesSymbolZweiEbenenHoeherAlsBlatt() {
        final Grammatik grammatik = mockGrammatik();
        final Syntaxpfad syntaxpfadAktuell = new Syntaxpfad();
        syntaxpfadAktuell.zufuegenKnoten(
                new Symbolkennung(new Symbolbezeichnung("S"), new Symbolidentifizierung(0)));
        syntaxpfadAktuell.zufuegenKnoten(
                new Symbolkennung(new Symbolbezeichnung("S5"), new Symbolidentifizierung(5)));
        syntaxpfadAktuell.zufuegenKnoten(
                new Symbolkennung(new Symbolbezeichnung("S5-1"), new Symbolidentifizierung(51)));
        syntaxpfadAktuell.zufuegenKnoten(
                new Symbolkennung(new Symbolbezeichnung("S5-1-1"), new Symbolidentifizierung(511)));
        syntaxpfadAktuell.zufuegenBlatt(new Symbolkennung(new Symbolbezeichnung("Z5-1-1-2"),
                new Symbolidentifizierung(51120)));
        final Syntaxpfad syntaxpfadErwartet = new Syntaxpfad();
        syntaxpfadErwartet.zufuegenKnoten(
                new Symbolkennung(new Symbolbezeichnung("S"), new Symbolidentifizierung(0)));
        syntaxpfadErwartet.zufuegenKnoten(
                new Symbolkennung(new Symbolbezeichnung("S5"), new Symbolidentifizierung(5)));
        syntaxpfadErwartet.zufuegenBlatt(
                new Symbolkennung(new Symbolbezeichnung("Z5-2"), new Symbolidentifizierung(520)));
        final Syntaxpfadbehandlung syntaxpfadbehandlung = new Syntaxpfadbehandlung(grammatik);
        final Syntaxpfad syntaxpfadErgebnis = syntaxpfadbehandlung.findeNaechstesSymbol(syntaxpfadAktuell);
        assertEquals(syntaxpfadErwartet, syntaxpfadErgebnis);
    }

    @Test
    public void testFindeNaechstesSymbolZweiEbenenHoeherNichtAlsBlatt() {
        final Grammatik grammatik = mockGrammatik();
        final Syntaxpfad syntaxpfadAktuell = new Syntaxpfad();
        syntaxpfadAktuell.zufuegenKnoten(
                new Symbolkennung(new Symbolbezeichnung("S"), new Symbolidentifizierung(0)));
        syntaxpfadAktuell.zufuegenKnoten(
                new Symbolkennung(new Symbolbezeichnung("S6"), new Symbolidentifizierung(6)));
        syntaxpfadAktuell.zufuegenKnoten(
                new Symbolkennung(new Symbolbezeichnung("S6-1"), new Symbolidentifizierung(61)));
        syntaxpfadAktuell.zufuegenKnoten(
                new Symbolkennung(new Symbolbezeichnung("S6-1-1"), new Symbolidentifizierung(611)));
        syntaxpfadAktuell.zufuegenBlatt(new Symbolkennung(new Symbolbezeichnung("Z6-1-1-2"),
                new Symbolidentifizierung(61120)));
        final Syntaxpfad syntaxpfadErwartet = new Syntaxpfad();
        syntaxpfadErwartet.zufuegenKnoten(
                new Symbolkennung(new Symbolbezeichnung("S"), new Symbolidentifizierung(0)));
        syntaxpfadErwartet.zufuegenKnoten(
                new Symbolkennung(new Symbolbezeichnung("S6"), new Symbolidentifizierung(6)));
        syntaxpfadErwartet.zufuegenKnoten(
                new Symbolkennung(new Symbolbezeichnung("S6-2"), new Symbolidentifizierung(62)));
        final Syntaxpfadbehandlung syntaxpfadbehandlung = new Syntaxpfadbehandlung(grammatik);
        final Syntaxpfad syntaxpfadErgebnis = syntaxpfadbehandlung.findeNaechstesSymbol(syntaxpfadAktuell);
        assertEquals(syntaxpfadErwartet, syntaxpfadErgebnis);
    }

    @Test
    public void testFindeNaechstesSymbolKeinNaechsetsSymbol() {
        final Grammatik grammatik = mockGrammatik();
        final Syntaxpfad syntaxpfadAktuell = new Syntaxpfad();
        syntaxpfadAktuell.zufuegenKnoten(
                new Symbolkennung(new Symbolbezeichnung("S"), new Symbolidentifizierung(0)));
        syntaxpfadAktuell.zufuegenKnoten(
                new Symbolkennung(new Symbolbezeichnung("S7"), new Symbolidentifizierung(7)));
        syntaxpfadAktuell.zufuegenKnoten(
                new Symbolkennung(new Symbolbezeichnung("S7-1"), new Symbolidentifizierung(71)));
        syntaxpfadAktuell.zufuegenBlatt(
                new Symbolkennung(new Symbolbezeichnung("Z7-1-2"), new Symbolidentifizierung(7120)));
        final Syntaxpfad syntaxpfadErwartet = new Syntaxpfad();
        final Syntaxpfadbehandlung syntaxpfadbehandlung = new Syntaxpfadbehandlung(grammatik);
        final Syntaxpfad syntaxpfadErgebnis = syntaxpfadbehandlung.findeNaechstesSymbol(syntaxpfadAktuell);
        assertEquals(syntaxpfadErwartet, syntaxpfadErgebnis);
    }

    @Test
    public void testFindeNaechstesSymbolInStartregelAlsBlattVonEbeneTiefer() {
        final Grammatik grammatik = erstelleGrammatik();
        final Syntaxpfad syntaxpfadAktuell = new Syntaxpfad();
        syntaxpfadAktuell.zufuegenKnoten(
                new Symbolkennung(new Symbolbezeichnung("S"), new Symbolidentifizierung(0)));
        syntaxpfadAktuell.zufuegenKnoten(
                new Symbolkennung(new Symbolbezeichnung("S1"), new Symbolidentifizierung(1)));
        syntaxpfadAktuell.zufuegenBlatt(
                new Symbolkennung(new Symbolbezeichnung("Z1-1"), new Symbolidentifizierung(6)));
        final Syntaxpfad syntaxpfadErwartet = new Syntaxpfad();
        syntaxpfadErwartet.zufuegenKnoten(
                new Symbolkennung(new Symbolbezeichnung("S"), new Symbolidentifizierung(0)));
        syntaxpfadErwartet.zufuegenBlatt(
                new Symbolkennung(new Symbolbezeichnung("Z2"), new Symbolidentifizierung(2)));
        final Syntaxpfadbehandlung syntaxpfadbehandlung = new Syntaxpfadbehandlung(grammatik);
        final Syntaxpfad syntaxpfadErgebnis = syntaxpfadbehandlung.findeNaechstesSymbol(syntaxpfadAktuell);
        assertEquals(syntaxpfadErwartet, syntaxpfadErgebnis);
    }

    @Test
    public void testFindeNaechstesSymbolInStartregelAlsBlattInnerhalbRegel() {
        final Grammatik grammatik = erstelleGrammatik();
        final Syntaxpfad syntaxpfadAktuell = new Syntaxpfad();
        syntaxpfadAktuell.zufuegenKnoten(
                new Symbolkennung(new Symbolbezeichnung("S"), new Symbolidentifizierung(0)));
        syntaxpfadAktuell.zufuegenBlatt(
                new Symbolkennung(new Symbolbezeichnung("Z2"), new Symbolidentifizierung(2)));
        final Syntaxpfad syntaxpfadErwartet = new Syntaxpfad();
        syntaxpfadErwartet.zufuegenKnoten(
                new Symbolkennung(new Symbolbezeichnung("S"), new Symbolidentifizierung(0)));
        syntaxpfadErwartet.zufuegenBlatt(
                new Symbolkennung(new Symbolbezeichnung("Z3"), new Symbolidentifizierung(3)));
        final Syntaxpfadbehandlung syntaxpfadbehandlung = new Syntaxpfadbehandlung(grammatik);
        final Syntaxpfad syntaxpfadErgebnis = syntaxpfadbehandlung.findeNaechstesSymbol(syntaxpfadAktuell);
        assertEquals(syntaxpfadErwartet, syntaxpfadErgebnis);
    }

    @Test
    public void testFindeNaechstesSymbolInStartregelNichtAlsBlattVonEbeneTiefer() {
        final Grammatik grammatik = erstelleGrammatik();
        final Syntaxpfad syntaxpfadAktuell = new Syntaxpfad();
        syntaxpfadAktuell.zufuegenKnoten(
                new Symbolkennung(new Symbolbezeichnung("S"), new Symbolidentifizierung(0)));
        syntaxpfadAktuell.zufuegenKnoten(
                new Symbolkennung(new Symbolbezeichnung("S4"), new Symbolidentifizierung(4)));
        syntaxpfadAktuell.zufuegenBlatt(
                new Symbolkennung(new Symbolbezeichnung("Z4-1"), new Symbolidentifizierung(7)));
        final Syntaxpfad syntaxpfadErwartet = new Syntaxpfad();
        syntaxpfadErwartet.zufuegenKnoten(
                new Symbolkennung(new Symbolbezeichnung("S"), new Symbolidentifizierung(0)));
        syntaxpfadErwartet.zufuegenKnoten(
                new Symbolkennung(new Symbolbezeichnung("S5"), new Symbolidentifizierung(5)));
        final Syntaxpfadbehandlung syntaxpfadbehandlung = new Syntaxpfadbehandlung(grammatik);
        final Syntaxpfad syntaxpfadErgebnis = syntaxpfadbehandlung.findeNaechstesSymbol(syntaxpfadAktuell);
        assertEquals(syntaxpfadErwartet, syntaxpfadErgebnis);
    }

    @Test
    public void testFindeNaechstesSymbolInStartregelNichtAlsBlattInnerhalbRegel() {
        final Grammatik grammatik = erstelleGrammatik();
        final Syntaxpfad syntaxpfadAktuell = new Syntaxpfad();
        syntaxpfadAktuell.zufuegenKnoten(
                new Symbolkennung(new Symbolbezeichnung("S"), new Symbolidentifizierung(0)));
        syntaxpfadAktuell.zufuegenBlatt(
                new Symbolkennung(new Symbolbezeichnung("Z3"), new Symbolidentifizierung(3)));
        final Syntaxpfad syntaxpfadErwartet = new Syntaxpfad();
        syntaxpfadErwartet.zufuegenKnoten(
                new Symbolkennung(new Symbolbezeichnung("S"), new Symbolidentifizierung(0)));
        syntaxpfadErwartet.zufuegenKnoten(
                new Symbolkennung(new Symbolbezeichnung("S4"), new Symbolidentifizierung(4)));
        final Syntaxpfadbehandlung syntaxpfadbehandlung = new Syntaxpfadbehandlung(grammatik);
        final Syntaxpfad syntaxpfadErgebnis = syntaxpfadbehandlung.findeNaechstesSymbol(syntaxpfadAktuell);
        assertEquals(syntaxpfadErwartet, syntaxpfadErgebnis);
    }

    private Grammatik mockGrammatik(final Map<Symbolbezeichnung, Set<Zeichenbereich>> mapZeichenbereiche,
            final Map<Symbolbezeichnung, Set<Zeichenmenge>> mapZeichenmengen,
            final Map<Symbolbezeichnung, Set<Zeichenfolge>> mapZeichenfolgen,
            final Map<Symbolbezeichnung, Set<RegEx>> mapRegEx) {
        final Grammatik grammatik = Mockito.mock(Grammatik.class);
        final Zeichenbereichregeln zeichenbereichregeln = Mockito.mock(Zeichenbereichregeln.class);
        Mockito.when(zeichenbereichregeln.get()).thenReturn(mapZeichenbereiche);
        Mockito.when(grammatik.getZeichenbereichregeln()).thenReturn(zeichenbereichregeln);
        final Zeichenmengeregeln zeichenmengeregeln = Mockito.mock(Zeichenmengeregeln.class);
        Mockito.when(zeichenmengeregeln.get()).thenReturn(mapZeichenmengen);
        Mockito.when(grammatik.getZeichenmengeregeln()).thenReturn(zeichenmengeregeln);
        final Zeichenfolgeregeln zeichenfolgeregeln = Mockito.mock(Zeichenfolgeregeln.class);
        Mockito.when(zeichenfolgeregeln.get()).thenReturn(mapZeichenfolgen);
        Mockito.when(grammatik.getZeichenfolgeregeln()).thenReturn(zeichenfolgeregeln);
        final RegExregeln regExregeln = Mockito.mock(RegExregeln.class);
        Mockito.when(regExregeln.get()).thenReturn(mapRegEx);
        Mockito.when(grammatik.getRegExregeln()).thenReturn(regExregeln);
        return grammatik;
    }

    private Grammatik mockGrammatik() {
        final Map<Symbolbezeichnung, List<List<Symbol>>> mapSymbolregeln = new HashMap<>();
        final Map<Symbolbezeichnung, Set<Zeichenbereich>> mapZeichenbereiche = new HashMap<>();
        final Map<Symbolbezeichnung, Set<Zeichenmenge>> mapZeichenmengen = new HashMap<>();
        final Map<Symbolbezeichnung, Set<Zeichenfolge>> mapZeichenfolgen = new HashMap<>();
        final List<Symbol> startsymbole = new ArrayList<>();
        erzeugeRegeln(startsymbole, mapSymbolregeln, mapZeichenfolgen);
        final RegelSymbole startregel = new RegelSymbole(new Symbolbezeichnung("S"), startsymbole);
        final Grammatik grammatik = Mockito.mock(Grammatik.class);
        Mockito.when(grammatik.getStartregel()).thenReturn(startregel);
        final Symbolregeln symbolregeln = new Symbolregeln(mapSymbolregeln);
        Mockito.when(grammatik.getSymbolregeln()).thenReturn(symbolregeln);
        final Zeichenbereichregeln zeichenbereichregeln = Mockito.mock(Zeichenbereichregeln.class);
        Mockito.when(zeichenbereichregeln.get()).thenReturn(mapZeichenbereiche);
        Mockito.when(grammatik.getZeichenbereichregeln()).thenReturn(zeichenbereichregeln);
        final Zeichenmengeregeln zeichenmengeregeln = Mockito.mock(Zeichenmengeregeln.class);
        Mockito.when(zeichenmengeregeln.get()).thenReturn(mapZeichenmengen);
        Mockito.when(grammatik.getZeichenmengeregeln()).thenReturn(zeichenmengeregeln);
        final Zeichenfolgeregeln zeichenfolgeregeln = Mockito.mock(Zeichenfolgeregeln.class);
        Mockito.when(zeichenfolgeregeln.get()).thenReturn(mapZeichenfolgen);
        Mockito.when(grammatik.getZeichenfolgeregeln()).thenReturn(zeichenfolgeregeln);
        return grammatik;
    }

    private void erzeugeRegeln(final List<Symbol> startregel,
            final Map<Symbolbezeichnung, List<List<Symbol>>> mapSymbolregeln,
            final Map<Symbolbezeichnung, Set<Zeichenfolge>> mapZeichenfolgen) {
        // S { S1 S2 S3 S4 S5 S6 S7}
        startregel.add(new Symbol(
                new Symbolkennung(new Symbolbezeichnung("S1"), new Symbolidentifizierung(1)),
                Kardinalitaet.GENAU_EINMAL));
        startregel.add(new Symbol(
                new Symbolkennung(new Symbolbezeichnung("S2"), new Symbolidentifizierung(2)),
                Kardinalitaet.GENAU_EINMAL));
        startregel.add(new Symbol(
                new Symbolkennung(new Symbolbezeichnung("S3"), new Symbolidentifizierung(3)),
                Kardinalitaet.GENAU_EINMAL));
        startregel.add(new Symbol(
                new Symbolkennung(new Symbolbezeichnung("S4"), new Symbolidentifizierung(4)),
                Kardinalitaet.GENAU_EINMAL));
        startregel.add(new Symbol(
                new Symbolkennung(new Symbolbezeichnung("S5"), new Symbolidentifizierung(5)),
                Kardinalitaet.GENAU_EINMAL));
        startregel.add(new Symbol(
                new Symbolkennung(new Symbolbezeichnung("S6"), new Symbolidentifizierung(6)),
                Kardinalitaet.GENAU_EINMAL));
        startregel.add(new Symbol(
                new Symbolkennung(new Symbolbezeichnung("S7"), new Symbolidentifizierung(7)),
                Kardinalitaet.GENAU_EINMAL));
        // S1 { Z1-1 Z1-2 Z1-3 }
        final List<Symbol> symboleS1 = Arrays.asList(erzeugeSymbol("Z1-1", 110), erzeugeSymbol("Z1-2", 120),
                erzeugeSymbol("Z1-3", 130));
        final List<List<Symbol>> setSymboleS1 = new ArrayList<>();
        setSymboleS1.add(symboleS1);
        mapSymbolregeln.put(new Symbolbezeichnung("S1"), setSymboleS1);
        // S2 { Z2-1 S2-2 Z2-3 }
        final List<Symbol> symboleS2 = Arrays.asList(erzeugeSymbol("Z2-1", 210), erzeugeSymbol("S2-2", 22),
                erzeugeSymbol("Z2-3", 230));
        final List<List<Symbol>> setSymboleS2 = new ArrayList<>();
        setSymboleS2.add(symboleS2);
        mapSymbolregeln.put(new Symbolbezeichnung("S2"), setSymboleS2);
        // S2-2 { Z2-2-1 }
        final List<Symbol> symboleS2_2 = List.of(erzeugeSymbol("Z2-2-1", 2210));
        final List<List<Symbol>> setSymboleS2_2 = new ArrayList<>();
        setSymboleS2_2.add(symboleS2_2);
        mapSymbolregeln.put(new Symbolbezeichnung("S2-2"), setSymboleS2_2);
        // S3 { S3-1 Z3-2 Z3-3 }
        final List<Symbol> symboleS3 = Arrays.asList(erzeugeSymbol("S3-1", 31), erzeugeSymbol("Z3-2", 320),
                erzeugeSymbol("Z3-3", 330));
        final List<List<Symbol>> setSymboleS3 = new ArrayList<>();
        setSymboleS3.add(symboleS3);
        mapSymbolregeln.put(new Symbolbezeichnung("S3"), setSymboleS3);
        // S3-1 { Z3-1-1 Z3-1-2 }
        final List<Symbol> symboleS3_1 = Arrays.asList(erzeugeSymbol("Z3-1-1", 3110), erzeugeSymbol("Z3-1-2", 3120));
        final List<List<Symbol>> setSymboleS3_1 = new ArrayList<>();
        setSymboleS3_1.add(symboleS3_1);
        mapSymbolregeln.put(new Symbolbezeichnung("S3-1"), setSymboleS3_1);
        // S4 { S4-1 S4-2 Z4-3 }
        final List<Symbol> symboleS4 = Arrays.asList(erzeugeSymbol("S4-1", 41), erzeugeSymbol("S4-2", 42),
                erzeugeSymbol("Z4-3", 430));
        final List<List<Symbol>> setSymboleS4 = new ArrayList<>();
        setSymboleS4.add(symboleS4);
        mapSymbolregeln.put(new Symbolbezeichnung("S4"), setSymboleS4);
        // S4-1 { Z4-1-1 Z4-1-2 }
        final List<Symbol> symboleS4_1 = Arrays.asList(erzeugeSymbol("Z4-1-1", 4110), erzeugeSymbol("Z4-1-2", 4120));
        final List<List<Symbol>> setSymboleS4_1 = new ArrayList<>();
        setSymboleS4_1.add(symboleS4_1);
        mapSymbolregeln.put(new Symbolbezeichnung("S4-1"), setSymboleS4_1);
        // S4-2 { Z4-2-1 }
        final List<Symbol> symboleS4_2 = List.of(erzeugeSymbol("Z4-2-1", 4210));
        final List<List<Symbol>> setSymboleS4_2 = new ArrayList<>();
        setSymboleS4_2.add(symboleS4_2);
        mapSymbolregeln.put(new Symbolbezeichnung("S4-2"), setSymboleS4_2);
        // S5 { S5-1 Z5-2 Z5-3 }
        final List<Symbol> symboleS5 = Arrays.asList(erzeugeSymbol("S5-1", 51), erzeugeSymbol("Z5-2", 520),
                erzeugeSymbol("Z5-3", 530));
        final List<List<Symbol>> setSymboleS5 = new ArrayList<>();
        setSymboleS5.add(symboleS5);
        mapSymbolregeln.put(new Symbolbezeichnung("S5"), setSymboleS5);
        // S5-1 { S5-1-1 }
        final List<Symbol> symboleS5_1 = List.of(erzeugeSymbol("S5-1-1", 511));
        final List<List<Symbol>> setSymboleS5_1 = new ArrayList<>();
        setSymboleS5_1.add(symboleS5_1);
        mapSymbolregeln.put(new Symbolbezeichnung("S5_1"), setSymboleS5_1);
        // S5-1-1 { Z5-1-1-1 Z5-1-1-2 }
        final List<Symbol> symboleS5_1_1 = Arrays.asList(erzeugeSymbol("Z5-1-1-1", 51110),
                erzeugeSymbol("Z5-1-1-2", 51120));
        final List<List<Symbol>> setSymboleS5_1_1 = new ArrayList<>();
        setSymboleS5_1_1.add(symboleS5_1_1);
        mapSymbolregeln.put(new Symbolbezeichnung("S5-1-1"), setSymboleS5_1_1);
        // S6 { S6-1 S6-2 Z6-3 }
        final List<Symbol> symboleS6 = Arrays.asList(erzeugeSymbol("S6-1", 61), erzeugeSymbol("S6-2", 62),
                erzeugeSymbol("Z6-3", 630));
        final List<List<Symbol>> setSymboleS6 = new ArrayList<>();
        setSymboleS6.add(symboleS6);
        mapSymbolregeln.put(new Symbolbezeichnung("S6"), setSymboleS6);
        // S6-1 { S6-1-1 }
        final List<Symbol> symboleS6_1 = List.of(erzeugeSymbol("S6-1-1", 611));
        final List<List<Symbol>> setSymboleS6_1 = new ArrayList<>();
        setSymboleS6_1.add(symboleS6_1);
        mapSymbolregeln.put(new Symbolbezeichnung("S6-1"), setSymboleS6_1);
        // S6-1-1 { Z6-1-1-1 Z6-1-1-2 }
        final List<Symbol> symboleS6_1_1 = Arrays.asList(erzeugeSymbol("Z6-1-1-1", 61110),
                erzeugeSymbol("Z6-1-1-2", 61120));
        final List<List<Symbol>> setSymboleS6_1_1 = new ArrayList<>();
        setSymboleS6_1_1.add(symboleS6_1_1);
        mapSymbolregeln.put(new Symbolbezeichnung("S6-1-1"), setSymboleS6_1_1);
        // S6-2 { Z6-2-1 Z6-2-2 }
        final List<Symbol> symboleS6_2 = Arrays.asList(erzeugeSymbol("Z6-2-1", 6210), erzeugeSymbol("Z6-2-2", 6220));
        final List<List<Symbol>> setSymboleS6_2 = new ArrayList<>();
        setSymboleS6_2.add(symboleS6_2);
        mapSymbolregeln.put(new Symbolbezeichnung("S6-2"), setSymboleS6_2);
        // S7 { S7-1 }
        final List<Symbol> symboleS7 = List.of(erzeugeSymbol("S7-1", 71));
        final List<List<Symbol>> setSymboleS7 = new ArrayList<>();
        setSymboleS7.add(symboleS7);
        mapSymbolregeln.put(new Symbolbezeichnung("S7"), setSymboleS7);
        // S7-1 { Z7-1-1 Z7-1-2 }
        final List<Symbol> symboleS7_1 = Arrays.asList(erzeugeSymbol("Z7-1-1", 7110), erzeugeSymbol("Z7-1-2", 7120));
        final List<List<Symbol>> setSymboleS7_1 = new ArrayList<>();
        setSymboleS7_1.add(symboleS7_1);
        mapSymbolregeln.put(new Symbolbezeichnung("S7_1"), setSymboleS7_1);
        // Z1-1 "Z"
        // Z1-2 "Z"
        // Z1-3 "Z"
        // Z2-1 "Z"
        // Z2-3 "Z"
        // Z2-2-1 "Z"
        // Z3-2 "Z"
        // Z3-3 "Z"
        // Z3-1-1 "Z"
        // Z3-1-2 "Z"
        // Z4-3 "Z"
        // Z4-1-1 "Z"
        // Z4-1-2 "Z"
        // Z4-2-1 "Z"
        // Z5-2 "Z"
        // Z5-3 "Z"
        // Z5-1-1-1 "Z"
        // Z5-1-1-2 "Z"
        // Z6-3 "Z"
        // Z6-1-1-1 "Z"
        // Z6-1-1-2 "Z"
        // Z6-2-1 "Z"
        // Z6-2-2 "Z"
        // Z7-1-1 "Z"
        // Z7-1-2 "Z"
        final Set<Zeichenfolge> zeichenfolge = new HashSet<>();
        zeichenfolge.add(new Zeichenfolge("Z"));
        zeichenfolge.add(new Zeichenfolge("Z"));
        zeichenfolge.add(new Zeichenfolge("Z"));
        zeichenfolge.add(new Zeichenfolge("Z"));
        zeichenfolge.add(new Zeichenfolge("Z"));
        zeichenfolge.add(new Zeichenfolge("Z"));
        zeichenfolge.add(new Zeichenfolge("Z"));
        zeichenfolge.add(new Zeichenfolge("Z"));
        zeichenfolge.add(new Zeichenfolge("Z"));
        zeichenfolge.add(new Zeichenfolge("Z"));
        mapZeichenfolgen.put(new Symbolbezeichnung("Z1-1"), zeichenfolge);
        mapZeichenfolgen.put(new Symbolbezeichnung("Z1-2"), zeichenfolge);
        mapZeichenfolgen.put(new Symbolbezeichnung("Z1-3"), zeichenfolge);
        mapZeichenfolgen.put(new Symbolbezeichnung("Z2-1"), zeichenfolge);
        mapZeichenfolgen.put(new Symbolbezeichnung("Z2-3"), zeichenfolge);
        mapZeichenfolgen.put(new Symbolbezeichnung("Z2-2-1"), zeichenfolge);
        mapZeichenfolgen.put(new Symbolbezeichnung("Z3-2"), zeichenfolge);
        mapZeichenfolgen.put(new Symbolbezeichnung("Z3-3"), zeichenfolge);
        mapZeichenfolgen.put(new Symbolbezeichnung("Z3-1-1"), zeichenfolge);
        mapZeichenfolgen.put(new Symbolbezeichnung("Z3-1-2"), zeichenfolge);
        mapZeichenfolgen.put(new Symbolbezeichnung("Z4-3"), zeichenfolge);
        mapZeichenfolgen.put(new Symbolbezeichnung("Z4-1-1"), zeichenfolge);
        mapZeichenfolgen.put(new Symbolbezeichnung("Z4-1-2"), zeichenfolge);
        mapZeichenfolgen.put(new Symbolbezeichnung("Z4-2-1"), zeichenfolge);
        mapZeichenfolgen.put(new Symbolbezeichnung("Z5-2"), zeichenfolge);
        mapZeichenfolgen.put(new Symbolbezeichnung("Z5-3"), zeichenfolge);
        mapZeichenfolgen.put(new Symbolbezeichnung("Z5-1-1-1"), zeichenfolge);
        mapZeichenfolgen.put(new Symbolbezeichnung("Z5-1-1-2"), zeichenfolge);
        mapZeichenfolgen.put(new Symbolbezeichnung("Z6-3"), zeichenfolge);
        mapZeichenfolgen.put(new Symbolbezeichnung("Z6-1-1-1"), zeichenfolge);
        mapZeichenfolgen.put(new Symbolbezeichnung("Z6-1-1-2"), zeichenfolge);
        mapZeichenfolgen.put(new Symbolbezeichnung("Z6-2-1"), zeichenfolge);
        mapZeichenfolgen.put(new Symbolbezeichnung("Z6-2-2"), zeichenfolge);
        mapZeichenfolgen.put(new Symbolbezeichnung("Z7-1-1"), zeichenfolge);
        mapZeichenfolgen.put(new Symbolbezeichnung("Z7-1-2"), zeichenfolge);
    }

    private Symbol erzeugeSymbol(final String bezeichnung, final int wert) {
        return new Symbol(
                new Symbolkennung(new Symbolbezeichnung(bezeichnung), new Symbolidentifizierung(wert)),
                Kardinalitaet.GENAU_EINMAL);
    }

    private Grammatik erstelleGrammatik() {
        // S { S1 Z2 Z3 S4 S5 }
        // S1 { Z1-1 }
        // S4 { Z4-1 }
        // S5 { Z5-1 }
        // Z2 "Z"
        // Z3 "Z"
        // Z1-1 "Z"
        // Z4-1 "Z"
        // Z5-1 "Z"
        final List<String> regeln = Arrays.asList("S { S1 Z2 Z3 S4 S5 }\n", "S1 { Z1-1 }\n", "S4 { Z4-1 }\n",
                "S5 { Z5-1 }\n", "Z2 \"Z\"\n", "Z3 \"Z\"\n", "Z1-1 \"Z\"\n", "Z4-1 \"Z\"\n", "Z5-1 \"Z\"\n");
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
