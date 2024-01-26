package de.platen.syntaxparser.grammatik;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.util.*;

import org.junit.Test;

import de.platen.syntaxparser.grammatik.elemente.Kardinalitaet;
import de.platen.syntaxparser.grammatik.elemente.RegEx;
import de.platen.syntaxparser.grammatik.elemente.RegelRegEx;
import de.platen.syntaxparser.grammatik.elemente.RegelSymbole;
import de.platen.syntaxparser.grammatik.elemente.RegelZeichenbereich;
import de.platen.syntaxparser.grammatik.elemente.RegelZeichenfolge;
import de.platen.syntaxparser.grammatik.elemente.RegelZeichenmenge;
import de.platen.syntaxparser.grammatik.elemente.Symbol;
import de.platen.syntaxparser.grammatik.elemente.Symbolbezeichnung;
import de.platen.syntaxparser.grammatik.elemente.Symbolidentifizierung;
import de.platen.syntaxparser.grammatik.elemente.Symbolkennung;
import de.platen.syntaxparser.grammatik.elemente.Zeichenbereich;
import de.platen.syntaxparser.grammatik.elemente.Zeichenfolge;
import de.platen.syntaxparser.grammatik.elemente.Zeichenmenge;

public class GrammatikAufbauTest
{

    private static final String SYMBOLNAME_STARTREGEL = "Startregel";
    private static final String S1 = "S1";
    private static final String S2 = "S2";

    @Test(expected = GrammatikException.class)
    public void testSetStartregelParameterNull() {
        final GrammatikAufbau grammatikAufbau = new GrammatikAufbau();
        grammatikAufbau.setStartregel(null);
        fail();
    }

    @Test
    public void testSetStartregel() {
        final Symbolbezeichnung startsymbol = new Symbolbezeichnung("Startsymbol");
        final Symbolbezeichnung symbolbezeichnung = new Symbolbezeichnung("Symbolbezeichnung");
        final List<Symbol> symbole = new ArrayList<>();
        final Symbol symbol = new Symbol(
                new Symbolkennung(symbolbezeichnung, new Symbolidentifizierung(Integer.valueOf(1))),
                Kardinalitaet.GENAU_EINMAL);
        symbole.add(symbol);
        final RegelSymbole regelSymbole = new RegelSymbole(startsymbol, symbole);
        final GrammatikAufbau grammatikAufbau = new GrammatikAufbau();
        grammatikAufbau.setStartregel(regelSymbole);
        final RegelSymbole startregel = grammatikAufbau.getStartregel();
        assertNotNull(startregel);
        assertEquals(startsymbol, startregel.getSymbolbezeichnung());
        assertEquals(symbole, startregel.getSymbole());
    }

    @Test(expected = GrammatikException.class)
    public void testSetStartregelZweimal() {
        final Symbolbezeichnung startsymbol = new Symbolbezeichnung("Startsymbol");
        final Symbolbezeichnung symbolbezeichnung = new Symbolbezeichnung("Symbolbezeichnung");
        final List<Symbol> symbole = new ArrayList<>();
        final Symbol symbol = new Symbol(
                new Symbolkennung(symbolbezeichnung, new Symbolidentifizierung(Integer.valueOf(1))),
                Kardinalitaet.GENAU_EINMAL);
        symbole.add(symbol);
        final RegelSymbole regelSymbole = new RegelSymbole(startsymbol, symbole);
        final GrammatikAufbau grammatikAufbau = new GrammatikAufbau();
        grammatikAufbau.setStartregel(regelSymbole);
        grammatikAufbau.setStartregel(regelSymbole);
        fail();
    }

    @Test
    public void testAddRegelSymbole() {
        final GrammatikAufbau grammatikAufbau = erzeugeGrammatikAufbau();
        final String[] symbolbezeichnungen = { "Symbol1", "Symbol2" };
        final Kardinalitaet[] kardinalitaeten = { Kardinalitaet.GENAU_EINMAL, Kardinalitaet.GENAU_EINMAL };
        final RegelSymbole regelSymbole = macheRegel("Symbol", symbolbezeichnungen, kardinalitaeten);
        grammatikAufbau.addRegelSymbole(regelSymbole);
    }

    @Test
    public void testAddRegelSymboleSymbolInRegelmengeVorhanden() {
        final GrammatikAufbau grammatikAufbau = erzeugeGrammatikAufbau();
        final String[] symbolbezeichnungen1 = { "Symbol1", "Symbol2" };
        final Kardinalitaet[] kardinalitaeten = { Kardinalitaet.GENAU_EINMAL, Kardinalitaet.GENAU_EINMAL };
        final RegelSymbole regelSymbole1 = macheRegel("Symbol", symbolbezeichnungen1, kardinalitaeten);
        grammatikAufbau.addRegelSymbole(regelSymbole1);
        final String[] symbolbezeichnungen2 = { "Symbol3", "Symbol4" };
        final RegelSymbole regelSymbole2 = macheRegel("Symbol", symbolbezeichnungen2, kardinalitaeten);
        grammatikAufbau.addRegelSymbole(regelSymbole2);
    }

    @Test(expected = GrammatikException.class)
    public void testAddRegelSymboleParameterNull() {
        final GrammatikAufbau grammatikAufbau = erzeugeGrammatikAufbau();
        grammatikAufbau.addRegelSymbole(null);
        fail();
    }

    @Test(expected = GrammatikException.class)
    public void testAddRegelSymboleSymbolInStartregel() {
        final GrammatikAufbau grammatikAufbau = erzeugeGrammatikAufbau();
        final String[] symbolbezeichnungen = { "Symbol1", "Symbol2" };
        final Kardinalitaet[] kardinalitaeten = { Kardinalitaet.GENAU_EINMAL, Kardinalitaet.GENAU_EINMAL };
        final RegelSymbole regelSymbole = macheRegel(SYMBOLNAME_STARTREGEL, symbolbezeichnungen, kardinalitaeten);
        grammatikAufbau.addRegelSymbole(regelSymbole);
        fail();
    }

    @Test(expected = GrammatikException.class)
    public void testAddRegelSymboleSymbolInZeichenbereichregeln() {
        final GrammatikAufbau grammatikAufbau = erzeugeGrammatikAufbau();
        final RegelZeichenbereich regelZeichenbereich = macheZeichenbereich("Zeichenbereich", 'a', 'z');
        grammatikAufbau.addRegelZeichenbereich(regelZeichenbereich);
        final String[] symbolbezeichnungen = { "Symbol1", "Symbol2" };
        final Kardinalitaet[] kardinalitaeten = { Kardinalitaet.GENAU_EINMAL, Kardinalitaet.GENAU_EINMAL };
        final RegelSymbole regelSymbole = macheRegel("Zeichenbereich", symbolbezeichnungen, kardinalitaeten);
        grammatikAufbau.addRegelSymbole(regelSymbole);
        fail();
    }

    @Test(expected = GrammatikException.class)
    public void testAddRegelSymboleSymbolInZeichenfolgeregeln() {
        final GrammatikAufbau grammatikAufbau = erzeugeGrammatikAufbau();
        final RegelZeichenfolge regelZeichenfolge = macheZeichenfolge("Zeichenfolge", "abc");
        grammatikAufbau.addRegelZeichenfolge(regelZeichenfolge);
        final String[] symbolbezeichnungen = { "Symbol1", "Symbol2" };
        final Kardinalitaet[] kardinalitaeten = { Kardinalitaet.GENAU_EINMAL, Kardinalitaet.GENAU_EINMAL };
        final RegelSymbole regelSymbole = macheRegel("Zeichenfolge", symbolbezeichnungen, kardinalitaeten);
        grammatikAufbau.addRegelSymbole(regelSymbole);
        fail();
    }

    @Test(expected = GrammatikException.class)
    public void testAddRegelSymboleSymbolInZeichenmengeregeln() {
        final GrammatikAufbau grammatikAufbau = erzeugeGrammatikAufbau();
        final RegelZeichenmenge regelZeichenmenge = macheZeichenmenge("Zeichenmenge", "abc");
        grammatikAufbau.addRegelZeichenmenge(regelZeichenmenge);
        final String[] symbolbezeichnungen = { "Symbol1", "Symbol2" };
        final Kardinalitaet[] kardinalitaeten = { Kardinalitaet.GENAU_EINMAL, Kardinalitaet.GENAU_EINMAL };
        final RegelSymbole regelSymbole = macheRegel("Zeichenmenge", symbolbezeichnungen, kardinalitaeten);
        grammatikAufbau.addRegelSymbole(regelSymbole);
        fail();
    }

    @Test(expected = GrammatikException.class)
    public void testAddRegelSymboleSymbolInRegExregeln() {
        final GrammatikAufbau grammatikAufbau = erzeugeGrammatikAufbau();
        final RegelRegEx regEx = macheRegEx("RegEx", "regex");
        grammatikAufbau.addRegelRegEx(regEx);
        final String[] symbolbezeichnungen = { "Symbol1", "Symbol2" };
        final Kardinalitaet[] kardinalitaeten = { Kardinalitaet.GENAU_EINMAL, Kardinalitaet.GENAU_EINMAL };
        final RegelSymbole regelSymbole = macheRegel("RegEx", symbolbezeichnungen, kardinalitaeten);
        grammatikAufbau.addRegelSymbole(regelSymbole);
        fail();
    }

    @Test
    public void testAddRegelZeichenbereich() {
        final GrammatikAufbau grammatikAufbau = erzeugeGrammatikAufbau();
        final RegelZeichenbereich regelZeichenbereich = macheZeichenbereich("Zeichenbereich", 'a', 'z');
        grammatikAufbau.addRegelZeichenbereich(regelZeichenbereich);
    }

    @Test
    public void testAddRegelZeichenbereichSymbolInRegelmengeVorhanden() {
        final GrammatikAufbau grammatikAufbau = erzeugeGrammatikAufbau();
        final RegelZeichenbereich regelZeichenbereich1 = macheZeichenbereich("Zeichenbereich", 'a', 'z');
        grammatikAufbau.addRegelZeichenbereich(regelZeichenbereich1);
        final RegelZeichenbereich regelZeichenbereich2 = macheZeichenbereich("Zeichenbereich", '0', '9');
        grammatikAufbau.addRegelZeichenbereich(regelZeichenbereich2);
    }

    @Test(expected = GrammatikException.class)
    public void testAddRegelZeichenbereichParameterNull() {
        final GrammatikAufbau grammatikAufbau = erzeugeGrammatikAufbau();
        grammatikAufbau.addRegelZeichenbereich(null);
        fail();
    }

    @Test(expected = GrammatikException.class)
    public void testAddRegelZeichenbereichSymbolInStartregeln() {
        final GrammatikAufbau grammatikAufbau = erzeugeGrammatikAufbau();
        final RegelZeichenbereich regelZeichenbereich = macheZeichenbereich(SYMBOLNAME_STARTREGEL, 'a', 'z');
        grammatikAufbau.addRegelZeichenbereich(regelZeichenbereich);
        fail();
    }

    @Test(expected = GrammatikException.class)
    public void testAddRegelZeichenbereichSymbolInSymbolregeln() {
        final GrammatikAufbau grammatikAufbau = erzeugeGrammatikAufbau();
        final String[] symbolbezeichnungen = { "Symbol1", "Symbol2" };
        final Kardinalitaet[] kardinalitaeten = { Kardinalitaet.GENAU_EINMAL, Kardinalitaet.GENAU_EINMAL };
        final RegelSymbole regelSymbole = macheRegel("Symbolregel1", symbolbezeichnungen, kardinalitaeten);
        grammatikAufbau.addRegelSymbole(regelSymbole);
        final RegelZeichenbereich regelZeichenbereich = macheZeichenbereich("Symbolregel1", 'a', 'z');
        grammatikAufbau.addRegelZeichenbereich(regelZeichenbereich);
        fail();
    }

    @Test(expected = GrammatikException.class)
    public void testAddRegelZeichenbereichSymbolInZeichenfolgeregeln() {
        final GrammatikAufbau grammatikAufbau = erzeugeGrammatikAufbau();
        final RegelZeichenfolge regelZeichenfolge = macheZeichenfolge("Zeichenfolge", "abc");
        grammatikAufbau.addRegelZeichenfolge(regelZeichenfolge);
        final RegelZeichenbereich regelZeichenbereich = macheZeichenbereich("Zeichenfolge", 'a', 'z');
        grammatikAufbau.addRegelZeichenbereich(regelZeichenbereich);
        fail();
    }

    @Test(expected = GrammatikException.class)
    public void testAddRegelZeichenbereichSymbolInZeichenmengeregeln() {
        final GrammatikAufbau grammatikAufbau = erzeugeGrammatikAufbau();
        final RegelZeichenmenge regelZeichenmenge = macheZeichenmenge("Zeichenmenge", "abc");
        grammatikAufbau.addRegelZeichenmenge(regelZeichenmenge);
        final RegelZeichenbereich regelZeichenbereich = macheZeichenbereich("Zeichenmenge", 'a', 'z');
        grammatikAufbau.addRegelZeichenbereich(regelZeichenbereich);
        fail();
    }

    @Test(expected = GrammatikException.class)
    public void testAddRegelZeichenbereichSymbolInRegExregeln() {
        final GrammatikAufbau grammatikAufbau = erzeugeGrammatikAufbau();
        final RegelRegEx regEx = macheRegEx("RegEx", "regex");
        grammatikAufbau.addRegelRegEx(regEx);
        final RegelZeichenbereich regelZeichenbereich = macheZeichenbereich("RegEx", 'a', 'z');
        grammatikAufbau.addRegelZeichenbereich(regelZeichenbereich);
        fail();
    }

    @Test
    public void testAddRegelZeichenfolge() {
        final GrammatikAufbau grammatikAufbau = erzeugeGrammatikAufbau();
        final RegelZeichenfolge regelZeichenfolge = macheZeichenfolge("Zeichenfolge", "zeichenfolge");
        grammatikAufbau.addRegelZeichenfolge(regelZeichenfolge);
    }

    @Test
    public void testAddRegelZeichenfolgeSymbolInRegelmengeVorhanden() {
        final GrammatikAufbau grammatikAufbau = erzeugeGrammatikAufbau();
        final RegelZeichenfolge regelZeichenfolge1 = macheZeichenfolge("Zeichenfolge", "zeichenfolge1");
        grammatikAufbau.addRegelZeichenfolge(regelZeichenfolge1);
        final RegelZeichenfolge regelZeichenfolge2 = macheZeichenfolge("Zeichenfolge", "zeichenfolge2");
        grammatikAufbau.addRegelZeichenfolge(regelZeichenfolge2);
    }

    @Test(expected = GrammatikException.class)
    public void testAddRegelZeichenfolgeParameterNull() {
        final GrammatikAufbau grammatikAufbau = erzeugeGrammatikAufbau();
        grammatikAufbau.addRegelZeichenfolge(null);
    }

    @Test(expected = GrammatikException.class)
    public void testAddRegelZeichenfolgeSymbolInStartregel() {
        final GrammatikAufbau grammatikAufbau = erzeugeGrammatikAufbau();
        final RegelZeichenfolge regelZeichenfolge = macheZeichenfolge(SYMBOLNAME_STARTREGEL, "zeichenfolge");
        grammatikAufbau.addRegelZeichenfolge(regelZeichenfolge);
        fail();
    }

    @Test(expected = GrammatikException.class)
    public void testAddRegelZeichenfolgeSymbolInSymbolregeln() {
        final GrammatikAufbau grammatikAufbau = erzeugeGrammatikAufbau();
        final String[] symbolbezeichnungen1 = { "Symbol1", "Symbol2" };
        final Kardinalitaet[] kardinalitaeten = { Kardinalitaet.GENAU_EINMAL, Kardinalitaet.GENAU_EINMAL };
        final RegelSymbole regelSymbole1 = macheRegel("Symbol", symbolbezeichnungen1, kardinalitaeten);
        grammatikAufbau.addRegelSymbole(regelSymbole1);
        final RegelZeichenfolge regelZeichenfolge = macheZeichenfolge("Symbol", "zeichenfolge");
        grammatikAufbau.addRegelZeichenfolge(regelZeichenfolge);
        fail();
    }

    @Test(expected = GrammatikException.class)
    public void testAddRegelZeichenfolgeSymbolInZeichenbereichregeln() {
        final GrammatikAufbau grammatikAufbau = erzeugeGrammatikAufbau();
        final RegelZeichenbereich regelZeichenbereich = macheZeichenbereich("Zeichenbereich", 'a', 'z');
        grammatikAufbau.addRegelZeichenbereich(regelZeichenbereich);
        final RegelZeichenfolge regelZeichenfolge = macheZeichenfolge("Zeichenbereich", "zeichenfolge");
        grammatikAufbau.addRegelZeichenfolge(regelZeichenfolge);
        fail();
    }

    @Test(expected = GrammatikException.class)
    public void testAddRegelZeichenfolgeSymbolInZeichenmengeregeln() {
        final GrammatikAufbau grammatikAufbau = erzeugeGrammatikAufbau();
        final RegelZeichenmenge regelZeichenmenge = macheZeichenmenge("Zeichenmenge", "abc");
        grammatikAufbau.addRegelZeichenmenge(regelZeichenmenge);
        final RegelZeichenfolge regelZeichenfolge = macheZeichenfolge("Zeichenmenge", "zeichenfolge");
        grammatikAufbau.addRegelZeichenfolge(regelZeichenfolge);
        fail();
    }

    @Test(expected = GrammatikException.class)
    public void testAddRegelZeichenfolgeSymbolInRegExregeln() {
        final GrammatikAufbau grammatikAufbau = erzeugeGrammatikAufbau();
        final RegelRegEx regEx = macheRegEx("RegEx", "regex");
        grammatikAufbau.addRegelRegEx(regEx);
        final RegelZeichenfolge regelZeichenfolge = macheZeichenfolge("RegEx", "zeichenfolge");
        grammatikAufbau.addRegelZeichenfolge(regelZeichenfolge);
        fail();
    }

    @Test
    public void testAddRegelZeichenmenge() {
        final GrammatikAufbau grammatikAufbau = erzeugeGrammatikAufbau();
        final RegelZeichenmenge regelZeichenmenge = macheZeichenmenge("Zeichenmenge", "abc");
        grammatikAufbau.addRegelZeichenmenge(regelZeichenmenge);
    }

    @Test
    public void testAddRegelZeichenmengeSymbolInRegelmengeVorhanden() {
        final GrammatikAufbau grammatikAufbau = erzeugeGrammatikAufbau();
        final RegelZeichenmenge regelZeichenmenge1 = macheZeichenmenge("Zeichenmenge", "abc");
        grammatikAufbau.addRegelZeichenmenge(regelZeichenmenge1);
        final RegelZeichenmenge regelZeichenmenge2 = macheZeichenmenge("Zeichenmenge", "123");
        grammatikAufbau.addRegelZeichenmenge(regelZeichenmenge2);
    }

    @Test(expected = GrammatikException.class)
    public void testAddRegelZeichenmengeParameterNull() {
        final GrammatikAufbau grammatikAufbau = erzeugeGrammatikAufbau();
        grammatikAufbau.addRegelZeichenmenge(null);
        fail();
    }

    @Test(expected = GrammatikException.class)
    public void testAddRegelZeichenmengeSymbolInStartregel() {
        final GrammatikAufbau grammatikAufbau = erzeugeGrammatikAufbau();
        final RegelZeichenmenge regelZeichenmenge = macheZeichenmenge(SYMBOLNAME_STARTREGEL, "abc");
        grammatikAufbau.addRegelZeichenmenge(regelZeichenmenge);
        fail();
    }

    @Test(expected = GrammatikException.class)
    public void testAddRegelZeichenmengeSymbolInSymbolregeln() {
        final GrammatikAufbau grammatikAufbau = erzeugeGrammatikAufbau();
        final String[] symbolbezeichnungen = { "Symbol1", "Symbol2" };
        final Kardinalitaet[] kardinalitaeten = { Kardinalitaet.GENAU_EINMAL, Kardinalitaet.GENAU_EINMAL };
        final RegelSymbole regelSymbole = macheRegel("Symbol", symbolbezeichnungen, kardinalitaeten);
        grammatikAufbau.addRegelSymbole(regelSymbole);
        final RegelZeichenmenge regelZeichenmenge = macheZeichenmenge("Symbol", "abc");
        grammatikAufbau.addRegelZeichenmenge(regelZeichenmenge);
        fail();
    }

    @Test(expected = GrammatikException.class)
    public void testAddRegelZeichenmengeSymbolInZeichenbereichregeln() {
        final GrammatikAufbau grammatikAufbau = erzeugeGrammatikAufbau();
        final RegelZeichenbereich regelZeichenbereich = macheZeichenbereich("Symbol", 'a', 'z');
        grammatikAufbau.addRegelZeichenbereich(regelZeichenbereich);
        final RegelZeichenmenge regelZeichenmenge = macheZeichenmenge("Symbol", "abc");
        grammatikAufbau.addRegelZeichenmenge(regelZeichenmenge);
        fail();
    }

    @Test(expected = GrammatikException.class)
    public void testAddRegelZeichenmengeSymbolInZeichenfolgeregeln() {
        final GrammatikAufbau grammatikAufbau = erzeugeGrammatikAufbau();
        final RegelZeichenfolge regelZeichenfolge = macheZeichenfolge("Symbol", "abc");
        grammatikAufbau.addRegelZeichenfolge(regelZeichenfolge);
        final RegelZeichenmenge regelZeichenmenge = macheZeichenmenge("Symbol", "abc");
        grammatikAufbau.addRegelZeichenmenge(regelZeichenmenge);
        fail();
    }

    @Test(expected = GrammatikException.class)
    public void testAddRegelZeichenmengeSymbolInRegExregeln() {
        final GrammatikAufbau grammatikAufbau = erzeugeGrammatikAufbau();
        final RegelRegEx regEx = macheRegEx("RegEx", "regex");
        grammatikAufbau.addRegelRegEx(regEx);
        final RegelZeichenmenge regelZeichenmenge = macheZeichenmenge("RegEx", "abc");
        grammatikAufbau.addRegelZeichenmenge(regelZeichenmenge);
        fail();
    }

    @Test
    public void testAddRegelRegEx() {
        final GrammatikAufbau grammatikAufbau = erzeugeGrammatikAufbau();
        final RegelRegEx regelRegEx = macheRegEx("RegEx", "abc");
        grammatikAufbau.addRegelRegEx(regelRegEx);
    }

    @Test
    public void testAddRegelRegExSSchonVorhanden() {
        final GrammatikAufbau grammatikAufbau = erzeugeGrammatikAufbau();
        final RegelRegEx regelRegEx1 = macheRegEx("RegEx", "abc");
        final RegelRegEx regelRegEx2 = macheRegEx("RegEx", "xyz");
        grammatikAufbau.addRegelRegEx(regelRegEx1);
        grammatikAufbau.addRegelRegEx(regelRegEx2);
    }

    @Test(expected = GrammatikException.class)
    public void testAddRegelRegExParameterNull() {
        final GrammatikAufbau grammatikAufbau = erzeugeGrammatikAufbau();
        grammatikAufbau.addRegelRegEx(null);
        fail();
    }

    @Test(expected = GrammatikException.class)
    public void testAddRegelRegExSymbolInStartregel() {
        final GrammatikAufbau grammatikAufbau = erzeugeGrammatikAufbau();
        final RegelRegEx regelRegEx = macheRegEx(SYMBOLNAME_STARTREGEL, "abc");
        grammatikAufbau.addRegelRegEx(regelRegEx);
        fail();
    }

    @Test(expected = GrammatikException.class)
    public void testAddRegelRegExSymbolInSymbolregeln() {
        final GrammatikAufbau grammatikAufbau = erzeugeGrammatikAufbau();
        final String[] symbolbezeichnungen = { "Symbol1", "Symbol2" };
        final Kardinalitaet[] kardinalitaeten = { Kardinalitaet.GENAU_EINMAL, Kardinalitaet.GENAU_EINMAL };
        final RegelSymbole regelSymbole = macheRegel("Symbol", symbolbezeichnungen, kardinalitaeten);
        grammatikAufbau.addRegelSymbole(regelSymbole);
        final RegelRegEx regelRegEx = macheRegEx("Symbol", "abc");
        grammatikAufbau.addRegelRegEx(regelRegEx);
        fail();
    }

    @Test(expected = GrammatikException.class)
    public void testAddRegelRegExSymbolInZeichenbereichregeln() {
        final GrammatikAufbau grammatikAufbau = erzeugeGrammatikAufbau();
        final RegelZeichenbereich regelZeichenbereich = macheZeichenbereich("Symbol", 'a', 'z');
        grammatikAufbau.addRegelZeichenbereich(regelZeichenbereich);
        final RegelRegEx regelRegEx = macheRegEx("Symbol", "abc");
        grammatikAufbau.addRegelRegEx(regelRegEx);
        fail();
    }

    @Test(expected = GrammatikException.class)
    public void testAddRegelRegExSymbolInZeichenfolgeregeln() {
        final GrammatikAufbau grammatikAufbau = erzeugeGrammatikAufbau();
        final RegelZeichenfolge regelZeichenfolge = macheZeichenfolge("Symbol", "abc");
        grammatikAufbau.addRegelZeichenfolge(regelZeichenfolge);
        final RegelRegEx regelRegEx = macheRegEx("Symbol", "abc");
        grammatikAufbau.addRegelRegEx(regelRegEx);
        fail();
    }

    @Test(expected = GrammatikException.class)
    public void testAddRegelRegExSymbolInZeichenmengeregeln() {
        final GrammatikAufbau grammatikAufbau = erzeugeGrammatikAufbau();
        final RegelZeichenmenge regelZeichenmenge = macheZeichenmenge("Symbol", "abc");
        grammatikAufbau.addRegelZeichenmenge(regelZeichenmenge);
        final RegelRegEx regelRegEx = macheRegEx("Symbol", "abc");
        grammatikAufbau.addRegelRegEx(regelRegEx);
        fail();
    }

    @Test
    public void testCheckGrammatik() {
        final GrammatikAufbau grammatikAufbau = erzeugeGrammatikAufbau();
        final RegelZeichenbereich regelZeichenbereich1 = macheZeichenbereich("S1", '0', '9');
        final RegelZeichenbereich regelZeichenbereich2 = macheZeichenbereich("S2", 'a', 'z');
        grammatikAufbau.addRegelZeichenbereich(regelZeichenbereich1);
        grammatikAufbau.addRegelZeichenbereich(regelZeichenbereich2);
        grammatikAufbau.checkGrammatik();
    }

    @Test(expected = GrammatikException.class)
    public void testCheckGrammatikStartregelsymbolNichtInRegeln() {
        final GrammatikAufbau grammatikAufbau = erzeugeGrammatikAufbau();
        grammatikAufbau.checkGrammatik();
        fail();
    }

    @Test(expected = GrammatikException.class)
    public void testCheckGrammatikRegelsymbolNichtInRegeln() {
        final GrammatikAufbau grammatikAufbau = erzeugeGrammatikAufbau();
        final String[] symbolbezeichnungen1 = { "Symbol1", "Symbol2" };
        final Kardinalitaet[] kardinalitaeten = { Kardinalitaet.GENAU_EINMAL, Kardinalitaet.GENAU_EINMAL };
        final RegelSymbole regelSymbole1 = macheRegel(S1, symbolbezeichnungen1, kardinalitaeten);
        grammatikAufbau.addRegelSymbole(regelSymbole1);
        final RegelSymbole regelSymbole2 = macheRegel(S2, symbolbezeichnungen1, kardinalitaeten);
        grammatikAufbau.addRegelSymbole(regelSymbole2);
        grammatikAufbau.checkGrammatik();
        fail();
    }

    @Test(expected = GrammatikException.class)
    public void testCheckGrammatikKreisStartregel() {
        final String[] symbolbezeichnungen = { SYMBOLNAME_STARTREGEL, S2 };
        final Kardinalitaet[] kardinalitaeten = { Kardinalitaet.GENAU_EINMAL, Kardinalitaet.GENAU_EINMAL };
        final RegelSymbole regelSymbole1 = macheRegel(SYMBOLNAME_STARTREGEL, symbolbezeichnungen, kardinalitaeten);
        final GrammatikAufbau grammatikAufbau = new GrammatikAufbau();
        grammatikAufbau.setStartregel(regelSymbole1);
        grammatikAufbau.checkGrammatik();
        fail();
    }

    @Test(expected = GrammatikException.class)
    public void testCheckGrammatikKreisSymbolregel() {
        final GrammatikAufbau grammatikAufbau = erzeugeGrammatikAufbau();
        final String[] symbolbezeichnungen1 = { "Symbol1", "Symbol2" };
        final Kardinalitaet[] kardinalitaeten = { Kardinalitaet.GENAU_EINMAL, Kardinalitaet.GENAU_EINMAL };
        final RegelSymbole regelSymbole1 = macheRegel("Symbol1", symbolbezeichnungen1, kardinalitaeten);
        grammatikAufbau.addRegelSymbole(regelSymbole1);
        grammatikAufbau.checkGrammatik();
        fail();
    }

    @Test
    public void testCheckGrammatikStriktOk() {
        final GrammatikAufbau grammatikAufbau = erzeugeGrammatikAufbau();
        final RegelZeichenbereich regelZeichenbereich1 = macheZeichenbereich("S1", '0', '9');
        final RegelZeichenbereich regelZeichenbereich2 = macheZeichenbereich("S2", 'a', 'z');
        grammatikAufbau.addRegelZeichenbereich(regelZeichenbereich1);
        grammatikAufbau.addRegelZeichenbereich(regelZeichenbereich2);
        grammatikAufbau.checkGrammatikStrikt();
    }


    @Test(expected = GrammatikException.class)
    public void textCheckGrammatikStriktMehrereRegeln() {
        final String[] symbolbezeichnungen = { S1, S2, "S3" };
        final Kardinalitaet[] kardinalitaeten = { Kardinalitaet.GENAU_EINMAL, Kardinalitaet.GENAU_EINMAL, Kardinalitaet.GENAU_EINMAL };
        final RegelSymbole regelSymbole = macheRegel(SYMBOLNAME_STARTREGEL, symbolbezeichnungen, kardinalitaeten);
        final GrammatikAufbau grammatikAufbau = new GrammatikAufbau();
        grammatikAufbau.setStartregel(regelSymbole);
        final RegelZeichenbereich regelZeichenbereich1 = macheZeichenbereich("S1", '0', '9');
        final RegelZeichenbereich regelZeichenbereich2 = macheZeichenbereich("S2", 'a', 'z');
        grammatikAufbau.addRegelZeichenbereich(regelZeichenbereich1);
        grammatikAufbau.addRegelZeichenbereich(regelZeichenbereich2);
        final String[] symbolbezeichnungen1 = { S1 };
        final Kardinalitaet[] kardinalitaeten1 = { Kardinalitaet.GENAU_EINMAL };
        final RegelSymbole regelSymbole1 = macheRegel("S3", symbolbezeichnungen1, kardinalitaeten1);
        grammatikAufbau.addRegelSymbole(regelSymbole1);
        final String[] symbolbezeichnungen2 = { S2 };
        final Kardinalitaet[] kardinalitaeten2 = { Kardinalitaet.GENAU_EINMAL };
        final RegelSymbole regelSymbole2 = macheRegel("S3", symbolbezeichnungen2, kardinalitaeten2);
        grammatikAufbau.addRegelSymbole(regelSymbole2);
        grammatikAufbau.checkGrammatikStrikt();
        fail();
    }

    @Test(expected = GrammatikException.class)
    public void textCheckGrammatikStriktSelbesSymbolinRegel() {
        final String[] symbolbezeichnungen = { S1, S2 };
        final Kardinalitaet[] kardinalitaeten = { Kardinalitaet.GENAU_EINMAL, Kardinalitaet.GENAU_EINMAL };
        final RegelSymbole regelSymbole = macheRegel(SYMBOLNAME_STARTREGEL, symbolbezeichnungen, kardinalitaeten);
        final GrammatikAufbau grammatikAufbau = new GrammatikAufbau();
        grammatikAufbau.setStartregel(regelSymbole);
        final RegelZeichenbereich regelZeichenbereich1 = macheZeichenbereich("S1", '0', '9');
        final RegelZeichenbereich regelZeichenbereich2 = macheZeichenbereich("S2", 'a', 'z');
        grammatikAufbau.addRegelZeichenbereich(regelZeichenbereich1);
        grammatikAufbau.addRegelZeichenbereich(regelZeichenbereich2);
        final String[] symbolbezeichnungen1 = { S1, "S3" };
        final Kardinalitaet[] kardinalitaeten1 = { Kardinalitaet.GENAU_EINMAL, Kardinalitaet.GENAU_EINMAL };
        final RegelSymbole regelSymbole1 = macheRegel("S3", symbolbezeichnungen1, kardinalitaeten1);
        grammatikAufbau.addRegelSymbole(regelSymbole1);
        grammatikAufbau.checkGrammatikStrikt();
        fail();
    }

    private GrammatikAufbau erzeugeGrammatikAufbau() {
        final String[] symbolbezeichnungen = { S1, S2 };
        final Kardinalitaet[] kardinalitaeten = { Kardinalitaet.GENAU_EINMAL, Kardinalitaet.GENAU_EINMAL };
        final RegelSymbole regelSymbole = macheRegel(SYMBOLNAME_STARTREGEL, symbolbezeichnungen, kardinalitaeten);
        final GrammatikAufbau grammatikAufbau = new GrammatikAufbau();
        grammatikAufbau.setStartregel(regelSymbole);
        return grammatikAufbau;
    }

    private RegelSymbole macheRegel(final String regelname, final String[] symbolbezeichnungen,
            final Kardinalitaet[] kardinalitaeten) {
        final Symbolbezeichnung startsymbol = new Symbolbezeichnung(regelname);
        return new RegelSymbole(startsymbol, macheSymbole(symbolbezeichnungen, kardinalitaeten));
    }

    private RegelZeichenbereich macheZeichenbereich(final String symbolname, final char von, final char bis) {
        final Zeichenbereich zeichenbereich = new Zeichenbereich(Character.valueOf(von), Character.valueOf(bis));
        return new RegelZeichenbereich(new Symbolbezeichnung(symbolname), zeichenbereich);
    }

    private RegelZeichenfolge macheZeichenfolge(final String symbolname, final String zeichenfolge) {
        return new RegelZeichenfolge(new Symbolbezeichnung(symbolname), new Zeichenfolge(zeichenfolge));
    }

    private RegelZeichenmenge macheZeichenmenge(final String symbolname, final String zeichen) {
        final Set<Character> zeichenmenge = new HashSet<>();
        for (int i = 0; i < zeichen.length(); i++) {
            zeichenmenge.add(Character.valueOf(zeichen.charAt(i)));
        }
        return new RegelZeichenmenge(new Symbolbezeichnung(symbolname), new Zeichenmenge(zeichenmenge));
    }

    private List<Symbol> macheSymbole(final String[] symbolbezeichnungen, final Kardinalitaet[] kardinalitaeten) {
        final List<Symbol> symbole = new ArrayList<>();
        for (int i = 0; i < symbolbezeichnungen.length; i++) {
            final Symbolbezeichnung symbolbezeichnung = new Symbolbezeichnung(symbolbezeichnungen[i]);
            final Symbol symbol = new Symbol(
                    new Symbolkennung(symbolbezeichnung, new Symbolidentifizierung(Integer.valueOf(i + 1))),
                    kardinalitaeten[i]);
            symbole.add(symbol);
        }
        return symbole;
    }

    private RegelRegEx macheRegEx(final String symbolname, final String regex) {
        return new RegelRegEx(new Symbolbezeichnung(symbolname), new RegEx(regex));
    }
}
