package de.platen.syntaxparser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.List;

import de.platen.syntaxparser.syntaxpfad.Syntaxpfadbehandlung;
import de.platen.syntaxparser.syntaxpfad.Syntaxpfadersteller;
import de.platen.syntaxparser.zeichenverarbeitung.Eingabewortabschluss;
import de.platen.syntaxparser.zeichenverarbeitung.Satzabschluss;
import de.platen.syntaxparser.zeichenverarbeitung.Wortabschluss;
import de.platen.syntaxparser.zeichenverarbeitung.Zeichenverarbeitung;
import org.junit.Test;

import de.platen.syntaxparser.grammatik.Grammatik;
import de.platen.syntaxparser.grammatik.GrammatikAufbau;
import de.platen.syntaxparser.grammatik.GrammatikLesen;
import de.platen.syntaxparser.syntaxpfad.Syntaxpfad;
import de.platen.syntaxparser.syntaxpfad.SyntaxpfadMitWort;
import org.mockito.Mockito;

public class ParserTest
{

    @Test
    public void testKonstruktorparameterNull() {
        final ParserInitialisierung parserInitialisierung = Mockito.mock(ParserInitialisierung.class);
        final Zeichenverarbeitung zeichenverarbeitung = Mockito.mock(Zeichenverarbeitung.class);
        final Eingabewortabschluss eingabewortabschluss = Mockito.mock(Eingabewortabschluss.class);
        final Satzabschluss satzabschluss = Mockito.mock(Satzabschluss.class);
        try {
            new Parser(null, zeichenverarbeitung, eingabewortabschluss, satzabschluss);
            fail();
        } catch (SyntaxparserException e) {
        }
        try {
            new Parser(parserInitialisierung, null, eingabewortabschluss, satzabschluss);
            fail();
        } catch (SyntaxparserException e) {
        }
        try {
            new Parser(parserInitialisierung, zeichenverarbeitung, null, satzabschluss);
            fail();
        } catch (SyntaxparserException e) {
        }
        try {
            new Parser(parserInitialisierung, zeichenverarbeitung, eingabewortabschluss, null);
            fail();
        } catch (SyntaxparserException e) {
        }
    }

    @Test
    public void testVerarbeiteZeichenParameterNull() {
        final ParserInitialisierung parserInitialisierung = Mockito.mock(ParserInitialisierung.class);
        final Zeichenverarbeitung zeichenverarbeitung = Mockito.mock(Zeichenverarbeitung.class);
        final Eingabewortabschluss eingabewortabschluss = Mockito.mock(Eingabewortabschluss.class);
        final Satzabschluss satzabschluss = Mockito.mock(Satzabschluss.class);
        final Parser parser = new Parser(parserInitialisierung, zeichenverarbeitung, eingabewortabschluss, satzabschluss);
        try {
            parser.verarbeiteZeichen(null);
            fail();
        } catch (SyntaxparserException e) {
        }
    }

    @Test
    public void testVerarbeiteZeichenParseFehler1() {
        final List<String> regeln = Arrays.asList("S {S1 S2 S3}\n", "S1 \"test\"\n", "S2 [az]\n", "S3 (.,)");
        final Grammatik grammatik = erstelleGrammatik(regeln);
        final ParserInitialisierung parserInitialisierung = new ParserInitialisierung(new Syntaxpfadersteller(grammatik), new Syntaxpfadbehandlung(grammatik));
        final Wortabschluss wortabschluss = new Wortabschluss(new Syntaxpfadersteller(grammatik), new Syntaxpfadbehandlung(grammatik));
        final Eingabewortabschluss eingabewortabschluss = new Eingabewortabschluss(wortabschluss);
        final Satzabschluss satzabschluss = new Satzabschluss();
        final Zeichenverarbeitung zeichenverarbeitung = new Zeichenverarbeitung(wortabschluss);
        final Parser parser = new Parser(parserInitialisierung, zeichenverarbeitung, eingabewortabschluss, satzabschluss);
        final String text = "test abcxyz";
        for (int index = 0; index < text.length(); index++) {
            assertTrue(parser.verarbeiteZeichen(text.charAt(index)));
        }
        assertFalse(parser.verarbeiteZeichen('0'));
    }

    @Test
    public void testVerarbeiteZeichenParseFehler2() {
        final List<String> regeln = Arrays.asList("S {S1 S2 S3}\n", "S1 \"test\"\n", "S2 [az]\n", "S3 (.,)");
        final Grammatik grammatik = erstelleGrammatik(regeln);
        final ParserInitialisierung parserInitialisierung = new ParserInitialisierung(new Syntaxpfadersteller(grammatik), new Syntaxpfadbehandlung(grammatik));
        final Wortabschluss wortabschluss = new Wortabschluss(new Syntaxpfadersteller(grammatik), new Syntaxpfadbehandlung(grammatik));
        final Eingabewortabschluss eingabewortabschluss = new Eingabewortabschluss(wortabschluss);
        final Satzabschluss satzabschluss = new Satzabschluss();
        final Zeichenverarbeitung zeichenverarbeitung = new Zeichenverarbeitung(wortabschluss);
        final Parser parser = new Parser(parserInitialisierung, zeichenverarbeitung, eingabewortabschluss, satzabschluss);
        final String text = "test abcxyz ";
        for (int index = 0; index < text.length(); index++) {
            assertTrue(parser.verarbeiteZeichen(text.charAt(index)));
        }
        assertFalse(parser.verarbeiteZeichen('0'));
    }

    @Test
    public void testVerarbeiteZeichen1a() {
        final List<String> regeln = Arrays.asList("S { S1 S2 S3 S4 }\n", //
                "S1 \"test1\\ test2\"\n", //
                "S2 [az]\n", //
                "S3 (.,\\\\)\n", //
                "S4 <[0-9]+,[0-9]+>");
        final Grammatik grammatik = erstelleGrammatik(regeln);
        final ParserInitialisierung parserInitialisierung = new ParserInitialisierung(new Syntaxpfadersteller(grammatik), new Syntaxpfadbehandlung(grammatik));
        final Wortabschluss wortabschluss = new Wortabschluss(new Syntaxpfadersteller(grammatik), new Syntaxpfadbehandlung(grammatik));
        final Eingabewortabschluss eingabewortabschluss = new Eingabewortabschluss(wortabschluss);
        final Satzabschluss satzabschluss = new Satzabschluss();
        final Zeichenverarbeitung zeichenverarbeitung = new Zeichenverarbeitung(wortabschluss);
        final Parser parser = new Parser(parserInitialisierung, zeichenverarbeitung, eingabewortabschluss, satzabschluss);
        final String text = "test1\\ test2 abcxyz ....\\\\,,,, 123,456";
        for (int index = 0; index < text.length(); index++) {
            parser.verarbeiteZeichen(text.charAt(index));
        }
        final List<SyntaxpfadMitWort> syntaxpfadeMitWortErgebnis = parser.ermittleSyntaxpfadeMitWort(true);
        assertEquals(4, syntaxpfadeMitWortErgebnis.size());
        final List<String> wortfolge = Arrays.asList("test1 test2", "abcxyz", "....\\,,,,", "123,456");
        checkWortfolge(wortfolge, syntaxpfadeMitWortErgebnis);
        checkSyntaxpfad(syntaxpfadeMitWortErgebnis.get(0).getSyntaxpfad(), List.of("S"), "S1");
        checkSyntaxpfad(syntaxpfadeMitWortErgebnis.get(1).getSyntaxpfad(), List.of("S"), "S2");
        checkSyntaxpfad(syntaxpfadeMitWortErgebnis.get(2).getSyntaxpfad(), List.of("S"), "S3");
        checkSyntaxpfad(syntaxpfadeMitWortErgebnis.get(3).getSyntaxpfad(), List.of("S"), "S4");
    }

    @Test
    public void testVerarbeiteZeichen1b() {
        final List<String> regeln = Arrays.asList("S { S1 S2 S3 S4 }\n", //
                "S1 \"test1\\ test2\"\n", //
                "S2 [az]\n", //
                "S3 (.,\\\\)\n", //
                "S4 <[0-9]+,[0-9]+>");
        final Parser parser = new Parser(erstelleGrammatik(regeln));
        final String text = "test1\\ test2 abcxyz ....\\\\,,,, 123,456";
        for (int index = 0; index < text.length(); index++) {
            parser.verarbeiteZeichen(text.charAt(index));
        }
        final List<SyntaxpfadMitWort> syntaxpfadeMitWortErgebnis = parser.ermittleSyntaxpfadeMitWort(true);
        assertEquals(4, syntaxpfadeMitWortErgebnis.size());
        final List<String> wortfolge = Arrays.asList("test1 test2", "abcxyz", "....\\,,,,", "123,456");
        checkWortfolge(wortfolge, syntaxpfadeMitWortErgebnis);
        checkSyntaxpfad(syntaxpfadeMitWortErgebnis.get(0).getSyntaxpfad(), List.of("S"), "S1");
        checkSyntaxpfad(syntaxpfadeMitWortErgebnis.get(1).getSyntaxpfad(), List.of("S"), "S2");
        checkSyntaxpfad(syntaxpfadeMitWortErgebnis.get(2).getSyntaxpfad(), List.of("S"), "S3");
        checkSyntaxpfad(syntaxpfadeMitWortErgebnis.get(3).getSyntaxpfad(), List.of("S"), "S4");
    }

    @Test
    public void testVerarbeiteZeichen2() {
        final List<String> regeln = Arrays.asList("S { Operand Operationsteil }\n", //
                "Operationsteil { Operator Operand }\n", //
                "Operationsteil { Operator Operand Operationsteil }\n", //
                "Operator \"+\"\n", //
                "Operator \"-\"\n", //
                "Operator \"*\"\n", //
                "Operator \"/\"\n", //
                "Operand [09]\n");
        final Grammatik grammatik = erstelleGrammatik(regeln);
        final ParserInitialisierung parserInitialisierung = new ParserInitialisierung(new Syntaxpfadersteller(grammatik), new Syntaxpfadbehandlung(grammatik));
        final Wortabschluss wortabschluss = new Wortabschluss(new Syntaxpfadersteller(grammatik), new Syntaxpfadbehandlung(grammatik));
        final Eingabewortabschluss eingabewortabschluss = new Eingabewortabschluss(wortabschluss);
        final Satzabschluss satzabschluss = new Satzabschluss();
        final Zeichenverarbeitung zeichenverarbeitung = new Zeichenverarbeitung(wortabschluss);
        final Parser parser = new Parser(parserInitialisierung, zeichenverarbeitung, eingabewortabschluss, satzabschluss);
        final String text = "111 + 222 - 333 * 444 / 555";
        for (int index = 0; index < text.length(); index++) {
            parser.verarbeiteZeichen(text.charAt(index));
        }
        final List<SyntaxpfadMitWort> syntaxpfadeMitWortErgebnis = parser.ermittleSyntaxpfadeMitWort(true);
        assertEquals(9, syntaxpfadeMitWortErgebnis.size());
        final List<String> wortfolge = Arrays.asList("111", "+", "222", "-", "333", "*", "444", "/", "555");
        checkWortfolge(wortfolge, syntaxpfadeMitWortErgebnis);
        checkSyntaxpfad(syntaxpfadeMitWortErgebnis.get(0).getSyntaxpfad(), List.of("S"), "Operand");
        checkSyntaxpfad(syntaxpfadeMitWortErgebnis.get(1).getSyntaxpfad(), Arrays.asList("S", "Operationsteil"),
                "Operator");
        checkSyntaxpfad(syntaxpfadeMitWortErgebnis.get(2).getSyntaxpfad(), Arrays.asList("S", "Operationsteil"),
                "Operand");
        checkSyntaxpfad(syntaxpfadeMitWortErgebnis.get(3).getSyntaxpfad(),
                Arrays.asList("S", "Operationsteil", "Operationsteil"), "Operator");
        checkSyntaxpfad(syntaxpfadeMitWortErgebnis.get(4).getSyntaxpfad(),
                Arrays.asList("S", "Operationsteil", "Operationsteil"), "Operand");
        checkSyntaxpfad(syntaxpfadeMitWortErgebnis.get(5).getSyntaxpfad(),
                Arrays.asList("S", "Operationsteil", "Operationsteil", "Operationsteil"), "Operator");
        checkSyntaxpfad(syntaxpfadeMitWortErgebnis.get(6).getSyntaxpfad(),
                Arrays.asList("S", "Operationsteil", "Operationsteil", "Operationsteil"), "Operand");
        checkSyntaxpfad(syntaxpfadeMitWortErgebnis.get(7).getSyntaxpfad(),
                Arrays.asList("S", "Operationsteil", "Operationsteil", "Operationsteil", "Operationsteil"), "Operator");
        checkSyntaxpfad(syntaxpfadeMitWortErgebnis.get(8).getSyntaxpfad(),
                Arrays.asList("S", "Operationsteil", "Operationsteil", "Operationsteil", "Operationsteil"), "Operand");
    }

    @Test
    public void testVerarbeiteZeichen3() {
        final List<String> regeln = Arrays.asList("S { Kunden }\n", //
                "Kunden { Kunde Semikolon }\n", //
                "Kunden { Kunde Komma Kunden }\n", //
                "Kunde { Kundentext Kundennummertext Zahl Person Artikelmenge }\n", //
                "Person { Vornametext Wort Nachnametext Wort }\n", //
                "Artikelmenge { Artikel Semikolon }\n", //
                "Artikelmenge { Artikel Komma Artikelmenge }\n", //
                "Artikel { Artikeltext Artikelbezeichnungtext Wort Artikelnummertext Zahl }\n", //
                //
                "Komma \",\"\n", //
                "Semikolon \";\"\n", //
                "Kundentext \"Kunde\"\n", //
                "Kundennummertext \"Kundennummer\"\n", //
                "Vornametext \"Vorname\"\n", //
                "Nachnametext \"Nachname\"\n", //
                "Artikeltext \"Artikel\"\n", //
                "Artikelbezeichnungtext \"Artikelbezeichnung\"\n", //
                "Artikelnummertext \"Artikelnummer\"\n", //
                //
                "Wort [az]\n", //
                "Wort [AZ]\n", //
                "Zahl [09]\n");
        final Grammatik grammatik = erstelleGrammatik(regeln);
        final ParserInitialisierung parserInitialisierung = new ParserInitialisierung(new Syntaxpfadersteller(grammatik), new Syntaxpfadbehandlung(grammatik));
        final Wortabschluss wortabschluss = new Wortabschluss(new Syntaxpfadersteller(grammatik), new Syntaxpfadbehandlung(grammatik));
        final Eingabewortabschluss eingabewortabschluss = new Eingabewortabschluss(wortabschluss);
        final Satzabschluss satzabschluss = new Satzabschluss();
        final Zeichenverarbeitung zeichenverarbeitung = new Zeichenverarbeitung(wortabschluss);
        final Parser parser = new Parser(parserInitialisierung, zeichenverarbeitung, eingabewortabschluss, satzabschluss);
        final String text = "Kunde Kundennummer 123 Vorname Hans Nachname Meier Artikel Artikelbezeichnung Waschmaschine Artikelnummer 456 ; ;";
        for (int index = 0; index < text.length(); index++) {
            parser.verarbeiteZeichen(text.charAt(index));
        }
        final List<SyntaxpfadMitWort> syntaxpfadeMitWortErgebnis = parser.ermittleSyntaxpfadeMitWort(true);
        assertEquals(14, syntaxpfadeMitWortErgebnis.size());
        final List<String> wortfolge = Arrays.asList("Kunde", "Kundennummer", "123", "Vorname", "Hans", "Nachname",
                "Meier", "Artikel", "Artikelbezeichnung", "Waschmaschine", "Artikelnummer", "456", ";", ";");
        checkWortfolge(wortfolge, syntaxpfadeMitWortErgebnis);
        checkSyntaxpfad(syntaxpfadeMitWortErgebnis.get(0).getSyntaxpfad(), Arrays.asList("S", "Kunden", "Kunde"),
                "Kundentext");
        checkSyntaxpfad(syntaxpfadeMitWortErgebnis.get(1).getSyntaxpfad(), Arrays.asList("S", "Kunden", "Kunde"),
                "Kundennummertext");
        checkSyntaxpfad(syntaxpfadeMitWortErgebnis.get(2).getSyntaxpfad(), Arrays.asList("S", "Kunden", "Kunde"),
                "Zahl");
        checkSyntaxpfad(syntaxpfadeMitWortErgebnis.get(3).getSyntaxpfad(),
                Arrays.asList("S", "Kunden", "Kunde", "Person"), "Vornametext");
        checkSyntaxpfad(syntaxpfadeMitWortErgebnis.get(4).getSyntaxpfad(),
                Arrays.asList("S", "Kunden", "Kunde", "Person"), "Wort");
        checkSyntaxpfad(syntaxpfadeMitWortErgebnis.get(5).getSyntaxpfad(),
                Arrays.asList("S", "Kunden", "Kunde", "Person"), "Nachnametext");
        checkSyntaxpfad(syntaxpfadeMitWortErgebnis.get(6).getSyntaxpfad(),
                Arrays.asList("S", "Kunden", "Kunde", "Person"), "Wort");
        checkSyntaxpfad(syntaxpfadeMitWortErgebnis.get(7).getSyntaxpfad(),
                Arrays.asList("S", "Kunden", "Kunde", "Artikelmenge", "Artikel"), "Artikeltext");
        checkSyntaxpfad(syntaxpfadeMitWortErgebnis.get(8).getSyntaxpfad(),
                Arrays.asList("S", "Kunden", "Kunde", "Artikelmenge", "Artikel"), "Artikelbezeichnungtext");
        checkSyntaxpfad(syntaxpfadeMitWortErgebnis.get(9).getSyntaxpfad(),
                Arrays.asList("S", "Kunden", "Kunde", "Artikelmenge", "Artikel"), "Wort");
        checkSyntaxpfad(syntaxpfadeMitWortErgebnis.get(10).getSyntaxpfad(),
                Arrays.asList("S", "Kunden", "Kunde", "Artikelmenge", "Artikel"), "Artikelnummertext");
        checkSyntaxpfad(syntaxpfadeMitWortErgebnis.get(11).getSyntaxpfad(),
                Arrays.asList("S", "Kunden", "Kunde", "Artikelmenge", "Artikel"), "Zahl");
        checkSyntaxpfad(syntaxpfadeMitWortErgebnis.get(12).getSyntaxpfad(),
                Arrays.asList("S", "Kunden", "Kunde", "Artikelmenge"), "Semikolon");
        checkSyntaxpfad(syntaxpfadeMitWortErgebnis.get(13).getSyntaxpfad(), Arrays.asList("S", "Kunden"), "Semikolon");
    }

    @Test
    public void testVerarbeiteZeichen4() {
        final List<String> regeln = Arrays.asList("S { Operand Operationsteil }\n", //
                "Operationsteil { Operator Operand }\n", //
                "Operationsteil { Operator Operand Operationsteil }\n", //
                "Operator \"+\"\n", //
                "Operator \"-\"\n", //
                "Operator \"*\"\n", //
                "Operator \"/\"\n", //
                "Operand [09]\n");
        final Grammatik grammatik = erstelleGrammatik(regeln);
        final ParserInitialisierung parserInitialisierung = new ParserInitialisierung(new Syntaxpfadersteller(grammatik), new Syntaxpfadbehandlung(grammatik));
        final Wortabschluss wortabschluss = new Wortabschluss(new Syntaxpfadersteller(grammatik), new Syntaxpfadbehandlung(grammatik));
        final Eingabewortabschluss eingabewortabschluss = new Eingabewortabschluss(wortabschluss);
        final Satzabschluss satzabschluss = new Satzabschluss();
        final Zeichenverarbeitung zeichenverarbeitung = new Zeichenverarbeitung(wortabschluss);
        final Parser parser = new Parser(parserInitialisierung, zeichenverarbeitung, eingabewortabschluss, satzabschluss);
        final String text = "111+222-333*444/555";
        for (int index = 0; index < text.length(); index++) {
            parser.verarbeiteZeichen(text.charAt(index));
        }
        final List<SyntaxpfadMitWort> syntaxpfadeMitWortErgebnis = parser.ermittleSyntaxpfadeMitWort(true);
        assertNotNull(syntaxpfadeMitWortErgebnis);
        assertEquals(9, syntaxpfadeMitWortErgebnis.size());
        final List<String> wortfolge = Arrays.asList("111", "+", "222", "-", "333", "*", "444", "/", "555");
        checkWortfolge(wortfolge, syntaxpfadeMitWortErgebnis);
        checkSyntaxpfad(syntaxpfadeMitWortErgebnis.get(0).getSyntaxpfad(), List.of("S"), "Operand");
        checkSyntaxpfad(syntaxpfadeMitWortErgebnis.get(1).getSyntaxpfad(), Arrays.asList("S", "Operationsteil"),
                "Operator");
        checkSyntaxpfad(syntaxpfadeMitWortErgebnis.get(2).getSyntaxpfad(), Arrays.asList("S", "Operationsteil"),
                "Operand");
        checkSyntaxpfad(syntaxpfadeMitWortErgebnis.get(3).getSyntaxpfad(),
                Arrays.asList("S", "Operationsteil", "Operationsteil"), "Operator");
        checkSyntaxpfad(syntaxpfadeMitWortErgebnis.get(4).getSyntaxpfad(),
                Arrays.asList("S", "Operationsteil", "Operationsteil"), "Operand");
        checkSyntaxpfad(syntaxpfadeMitWortErgebnis.get(5).getSyntaxpfad(),
                Arrays.asList("S", "Operationsteil", "Operationsteil", "Operationsteil"), "Operator");
        checkSyntaxpfad(syntaxpfadeMitWortErgebnis.get(6).getSyntaxpfad(),
                Arrays.asList("S", "Operationsteil", "Operationsteil", "Operationsteil"), "Operand");
        checkSyntaxpfad(syntaxpfadeMitWortErgebnis.get(7).getSyntaxpfad(),
                Arrays.asList("S", "Operationsteil", "Operationsteil", "Operationsteil", "Operationsteil"), "Operator");
        checkSyntaxpfad(syntaxpfadeMitWortErgebnis.get(8).getSyntaxpfad(),
                Arrays.asList("S", "Operationsteil", "Operationsteil", "Operationsteil", "Operationsteil"), "Operand");
    }

    @Test
    public void testErmittleSyntaxpfadeMitWortAlsString() {
        final List<String> regeln = Arrays.asList("S { Operand Operationsteil }\n", //
                "Operationsteil { Operator Operand }\n", //
                "Operationsteil { Operator Operand Operationsteil }\n", //
                "Operator \"+\"\n", //
                "Operator \"-\"\n", //
                "Operator \"*\"\n", //
                "Operator \"/\"\n", //
                "Operand [09]\n");
        final Grammatik grammatik = erstelleGrammatik(regeln);
        final ParserInitialisierung parserInitialisierung = new ParserInitialisierung(new Syntaxpfadersteller(grammatik), new Syntaxpfadbehandlung(grammatik));
        final Wortabschluss wortabschluss = new Wortabschluss(new Syntaxpfadersteller(grammatik), new Syntaxpfadbehandlung(grammatik));
        final Eingabewortabschluss eingabewortabschluss = new Eingabewortabschluss(wortabschluss);
        final Satzabschluss satzabschluss = new Satzabschluss();
        final Zeichenverarbeitung zeichenverarbeitung = new Zeichenverarbeitung(wortabschluss);
        final Parser parser = new Parser(parserInitialisierung, zeichenverarbeitung, eingabewortabschluss, satzabschluss);
        final String text = "111+222-333*444/555";
        for (int index = 0; index < text.length(); index++) {
            parser.verarbeiteZeichen(text.charAt(index));
        }
        final String ergebnis = parser.ermittleSyntaxpfadeMitWortAlsString(true);
        assertEquals("[SyntaxpfadMitWort: [Syntaxpfad: [[Symbolkennung: [Symbolbezeichnung='S', Symbolidentifizierung=0]], Symbolkennung: [Symbolbezeichnung='Operand', Symbolidentifizierung=1]], Wort='111'], SyntaxpfadMitWort: [Syntaxpfad: [[Symbolkennung: [Symbolbezeichnung='S', Symbolidentifizierung=0], Symbolkennung: [Symbolbezeichnung='Operationsteil', Symbolidentifizierung=2]], Symbolkennung: [Symbolbezeichnung='Operator', Symbolidentifizierung=5]], Wort='+'], SyntaxpfadMitWort: [Syntaxpfad: [[Symbolkennung: [Symbolbezeichnung='S', Symbolidentifizierung=0], Symbolkennung: [Symbolbezeichnung='Operationsteil', Symbolidentifizierung=2]], Symbolkennung: [Symbolbezeichnung='Operand', Symbolidentifizierung=6]], Wort='222'], SyntaxpfadMitWort: [Syntaxpfad: [[Symbolkennung: [Symbolbezeichnung='S', Symbolidentifizierung=0], Symbolkennung: [Symbolbezeichnung='Operationsteil', Symbolidentifizierung=2], Symbolkennung: [Symbolbezeichnung='Operationsteil', Symbolidentifizierung=7]], Symbolkennung: [Symbolbezeichnung='Operator', Symbolidentifizierung=5]], Wort='-'], SyntaxpfadMitWort: [Syntaxpfad: [[Symbolkennung: [Symbolbezeichnung='S', Symbolidentifizierung=0], Symbolkennung: [Symbolbezeichnung='Operationsteil', Symbolidentifizierung=2], Symbolkennung: [Symbolbezeichnung='Operationsteil', Symbolidentifizierung=7]], Symbolkennung: [Symbolbezeichnung='Operand', Symbolidentifizierung=6]], Wort='333'], SyntaxpfadMitWort: [Syntaxpfad: [[Symbolkennung: [Symbolbezeichnung='S', Symbolidentifizierung=0], Symbolkennung: [Symbolbezeichnung='Operationsteil', Symbolidentifizierung=2], Symbolkennung: [Symbolbezeichnung='Operationsteil', Symbolidentifizierung=7], Symbolkennung: [Symbolbezeichnung='Operationsteil', Symbolidentifizierung=7]], Symbolkennung: [Symbolbezeichnung='Operator', Symbolidentifizierung=5]], Wort='*'], SyntaxpfadMitWort: [Syntaxpfad: [[Symbolkennung: [Symbolbezeichnung='S', Symbolidentifizierung=0], Symbolkennung: [Symbolbezeichnung='Operationsteil', Symbolidentifizierung=2], Symbolkennung: [Symbolbezeichnung='Operationsteil', Symbolidentifizierung=7], Symbolkennung: [Symbolbezeichnung='Operationsteil', Symbolidentifizierung=7]], Symbolkennung: [Symbolbezeichnung='Operand', Symbolidentifizierung=6]], Wort='444'], SyntaxpfadMitWort: [Syntaxpfad: [[Symbolkennung: [Symbolbezeichnung='S', Symbolidentifizierung=0], Symbolkennung: [Symbolbezeichnung='Operationsteil', Symbolidentifizierung=2], Symbolkennung: [Symbolbezeichnung='Operationsteil', Symbolidentifizierung=7], Symbolkennung: [Symbolbezeichnung='Operationsteil', Symbolidentifizierung=7], Symbolkennung: [Symbolbezeichnung='Operationsteil', Symbolidentifizierung=7]], Symbolkennung: [Symbolbezeichnung='Operator', Symbolidentifizierung=3]], Wort='/'], SyntaxpfadMitWort: [Syntaxpfad: [[Symbolkennung: [Symbolbezeichnung='S', Symbolidentifizierung=0], Symbolkennung: [Symbolbezeichnung='Operationsteil', Symbolidentifizierung=2], Symbolkennung: [Symbolbezeichnung='Operationsteil', Symbolidentifizierung=7], Symbolkennung: [Symbolbezeichnung='Operationsteil', Symbolidentifizierung=7], Symbolkennung: [Symbolbezeichnung='Operationsteil', Symbolidentifizierung=7]], Symbolkennung: [Symbolbezeichnung='Operand', Symbolidentifizierung=4]], Wort='555']]", ergebnis);
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

    private void checkWortfolge(final List<String> wortfolge,
            final List<SyntaxpfadMitWort> syntaxpfadeMitWortErgebnis) {
        assertEquals(wortfolge.size(), syntaxpfadeMitWortErgebnis.size());
        for (int index = 0; index < wortfolge.size(); index++) {
            assertEquals(wortfolge.get(index), syntaxpfadeMitWortErgebnis.get(index).getWort());
        }
    }

    private void checkSyntaxpfad(final Syntaxpfad syntaxpfad, final List<String> knoten, final String blatt) {
        assertEquals(knoten.size(), syntaxpfad.gebeKnotenfolge().size());
        for (int index = 0; index < knoten.size(); index++) {
            assertEquals(knoten.get(index),
                    syntaxpfad.gebeKnotenfolge().get(index).getSymbolbezeichnung().getSymbolbezeichnung());
        }
        assertEquals(blatt, syntaxpfad.gebeBlatt().getSymbolbezeichnung().getSymbolbezeichnung());
    }
}
