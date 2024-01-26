package de.platen.syntaxparser.grammatik;

import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

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

public class GrammatikLesenTest
{

    private static final String GRAMMATIK = "S {S1+ S2 S3+ S4}\nS1 {S5 S6}\nS2 [az]\nS3 \"\\\"zeichenfolge\\\"\"\nS4 (,;.:_\\)\\#\\\\\\n\\r\\t) # Kommentar\nS5 [[\\]]\nS6 <test\\>>";

    @Test(expected = GrammatikException.class)
    public void testKonstruktorParameterNull() {
        new GrammatikLesen(null);
        fail();
    }

    @Test(expected = GrammatikException.class)
    public void testVerarbeiteZeichenParameterNull() {
        final GrammatikAufbau grammatikAufbau = Mockito.mock(GrammatikAufbau.class);
        final GrammatikLesen grammatikLesen = new GrammatikLesen(grammatikAufbau);
        grammatikLesen.verarbeiteZeichen(null);
        fail();
    }

    @Test
    public void testVerarbeiteZeichen() {
        final GrammatikAufbau grammatikAufbau = Mockito.mock(GrammatikAufbau.class);
        final GrammatikLesen grammatikLesen = new GrammatikLesen(grammatikAufbau);
        for (int i = 0; i < GRAMMATIK.length(); i++) {
            grammatikLesen.verarbeiteZeichen(GRAMMATIK.charAt(i));
        }
        Mockito.verify(grammatikAufbau).setStartregel(ArgumentMatchers.eq(macheStartregel()));
        Mockito.verify(grammatikAufbau).addRegelSymbole(ArgumentMatchers.eq(macheRegelSymbole()));
        Mockito.verify(grammatikAufbau).addRegelZeichenbereich(ArgumentMatchers.eq(macheRegelZeichenbereich1()));
        Mockito.verify(grammatikAufbau).addRegelZeichenfolge(ArgumentMatchers.eq(macheRegelZeichenfolge()));
        Mockito.verify(grammatikAufbau).addRegelZeichenmenge(ArgumentMatchers.eq(macheRegelZeichenmenge()));
        Mockito.verify(grammatikAufbau).addRegelZeichenbereich(ArgumentMatchers.eq(macheRegelZeichenbereich2()));
        Mockito.verify(grammatikAufbau).addRegelRegEx(ArgumentMatchers.eq(macheRegelRegEx()));
    }

    private RegelSymbole macheStartregel() {
        final Symbolbezeichnung symbolbezeichnung = new Symbolbezeichnung("S");
        final List<Symbol> symbole = new ArrayList<>();
        final Symbol s1 = new Symbol(
                new Symbolkennung(new Symbolbezeichnung("S1"), new Symbolidentifizierung(Integer.valueOf(1))),
                Kardinalitaet.MINDESTENS_EINMAL);
        symbole.add(s1);
        final Symbol s2 = new Symbol(
                new Symbolkennung(new Symbolbezeichnung("S2"), new Symbolidentifizierung(Integer.valueOf(2))),
                Kardinalitaet.GENAU_EINMAL);
        symbole.add(s2);
        final Symbol s3 = new Symbol(
                new Symbolkennung(new Symbolbezeichnung("S3"), new Symbolidentifizierung(Integer.valueOf(3))),
                Kardinalitaet.MINDESTENS_EINMAL);
        symbole.add(s3);
        final Symbol s4 = new Symbol(
                new Symbolkennung(new Symbolbezeichnung("S4"), new Symbolidentifizierung(Integer.valueOf(4))),
                Kardinalitaet.GENAU_EINMAL);
        symbole.add(s4);
        return new RegelSymbole(symbolbezeichnung, symbole);
    }

    private RegelSymbole macheRegelSymbole() {
        final Symbolbezeichnung symbolbezeichnung = new Symbolbezeichnung("S1");
        final List<Symbol> symbole = new ArrayList<>();
        final Symbol s1 = new Symbol(
                new Symbolkennung(new Symbolbezeichnung("S5"), new Symbolidentifizierung(Integer.valueOf(5))),
                Kardinalitaet.GENAU_EINMAL);
        final Symbol s2 = new Symbol(
                new Symbolkennung(new Symbolbezeichnung("S6"), new Symbolidentifizierung(Integer.valueOf(6))),
                Kardinalitaet.GENAU_EINMAL);
        symbole.add(s1);
        symbole.add(s2);
        return new RegelSymbole(symbolbezeichnung, symbole);
    }

    private RegelZeichenbereich macheRegelZeichenbereich1() {
        final Symbolbezeichnung symbolbezeichnung = new Symbolbezeichnung("S2");
        final Zeichenbereich zeichenbereich = new Zeichenbereich('a', 'z');
        return new RegelZeichenbereich(symbolbezeichnung, zeichenbereich);
    }

    private RegelZeichenfolge macheRegelZeichenfolge() {
        final Symbolbezeichnung symbolbezeichnung = new Symbolbezeichnung("S3");
        final Zeichenfolge zeichenfolge = new Zeichenfolge("\"zeichenfolge\"");
        return new RegelZeichenfolge(symbolbezeichnung, zeichenfolge);
    }

    private RegelZeichenmenge macheRegelZeichenmenge() {
        final Symbolbezeichnung symbolbezeichnung = new Symbolbezeichnung("S4");
        final Set<Character> zeichen = new HashSet<>();
        zeichen.add(',');
        zeichen.add(';');
        zeichen.add('.');
        zeichen.add(':');
        zeichen.add('_');
        zeichen.add('#');
        zeichen.add(')');
        zeichen.add('\\');
        zeichen.add('\n');
        zeichen.add('\r');
        zeichen.add('\t');
        final Zeichenmenge zeichenmenge = new Zeichenmenge(zeichen);
        return new RegelZeichenmenge(symbolbezeichnung, zeichenmenge);
    }

    private RegelZeichenbereich macheRegelZeichenbereich2() {
        final Symbolbezeichnung symbolbezeichnung = new Symbolbezeichnung("S5");
        final Zeichenbereich zeichenbereich = new Zeichenbereich('[', ']');
        return new RegelZeichenbereich(symbolbezeichnung, zeichenbereich);
    }

    private RegelRegEx macheRegelRegEx() {
        final Symbolbezeichnung symbolbezeichnung = new Symbolbezeichnung("S6");
        final RegEx regEx = new RegEx("test>");
        return new RegelRegEx(symbolbezeichnung, regEx);
    }
}
