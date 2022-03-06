package de.platen.syntaxparser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import de.platen.syntaxparser.grammatik.Grammatik;
import de.platen.syntaxparser.grammatik.GrammatikAufbau;
import de.platen.syntaxparser.grammatik.GrammatikLesen;
import de.platen.syntaxparser.syntaxpfad.Syntaxpfad;
import de.platen.syntaxparser.syntaxpfad.SyntaxpfadMitWort;

public class ParserTest
{

    @Test(expected = SyntaxparserException.class)
    public void testKonstruktorparameterNull() {
        new Parser(null);
        fail();
    }

    @Test(expected = SyntaxparserException.class)
    public void testVerarbeiteZeichenParameterNull() {
        final List<String> regeln = Arrays.asList("S {S1 S2 S3}\n", "S1 \"test\"\n", "S2 [az]\n", "S3 (.,)");
        final Grammatik grammatik = erstelleGrammatik(regeln);
        final Parser parser = new Parser(grammatik);
        parser.verarbeiteZeichen(null);
    }

    @Test(expected = ParseException.class)
    public void testVerarbeiteZeichenParseFehler() {
        final List<String> regeln = Arrays.asList("S {S1 S2 S3}\n", "S1 \"test\"\n", "S2 [az]\n", "S3 (.,)");
        final Grammatik grammatik = erstelleGrammatik(regeln);
        final Parser parser = new Parser(grammatik);
        final String text = "test abcxyz 0\n";
        for (int index = 0; index < text.length(); index++) {
            parser.verarbeiteZeichen(text.charAt(index));
        }
    }

    @Test
    public void testVerarbeiteZeichen1() {
        final List<String> regeln = Arrays.asList("S {S1 S2 S3 S4}\n", //
                "S1 \"test1\\ test2\"\n", //
                "S2 [az]\n", //
                "S3 (.,\\\\)\n", //
                "S4 <[0-9]+,[0-9]+>");
        final Grammatik grammatik = erstelleGrammatik(regeln);
        final Parser parser = new Parser(grammatik);
        final String text = "test1\\ test2 abcxyz ....\\\\,,,, 123,456\n";
        for (int index = 0; index < text.length(); index++) {
            parser.verarbeiteZeichen(text.charAt(index));
        }
        final List<SyntaxpfadMitWort> syntaxpfadeMitWortErgebnis = parser.gebeSyntaxpfadeMitWort();
        assertEquals(4, syntaxpfadeMitWortErgebnis.size());
        assertEquals("test1 test2", syntaxpfadeMitWortErgebnis.get(0).getWort());
        assertEquals("abcxyz", syntaxpfadeMitWortErgebnis.get(1).getWort());
        assertEquals("....\\,,,,", syntaxpfadeMitWortErgebnis.get(2).getWort());
        assertEquals("123,456", syntaxpfadeMitWortErgebnis.get(3).getWort());
        checkSyntaxpfad(syntaxpfadeMitWortErgebnis.get(0).getSyntaxpfad(), Arrays.asList("S"), "S1");
        checkSyntaxpfad(syntaxpfadeMitWortErgebnis.get(1).getSyntaxpfad(), Arrays.asList("S"), "S2");
        checkSyntaxpfad(syntaxpfadeMitWortErgebnis.get(2).getSyntaxpfad(), Arrays.asList("S"), "S3");
        checkSyntaxpfad(syntaxpfadeMitWortErgebnis.get(3).getSyntaxpfad(), Arrays.asList("S"), "S4");
    }

    @Test
    public void testVerarbeiteZeichen2() {
        final List<String> regeln = Arrays.asList("S { Operand Operationsteil }\n", //
                "Operationsteil { Operator Operand }\n", //
                "Operationsteil { Operator Operand Operationsteil}\n", //
                "Operator (+-*/)\n", //
                "Operand [09]\n");
        final Grammatik grammatik = erstelleGrammatik(regeln);
        final Parser parser = new Parser(grammatik);
        final String text = "111 + 222 - 333 * 444 / 555\n";
        for (int index = 0; index < text.length(); index++) {
            parser.verarbeiteZeichen(text.charAt(index));
        }
        final List<SyntaxpfadMitWort> syntaxpfadeMitWortErgebnis = parser.gebeSyntaxpfadeMitWort();
        assertEquals(9, syntaxpfadeMitWortErgebnis.size());
        assertEquals("111", syntaxpfadeMitWortErgebnis.get(0).getWort());
        assertEquals("+", syntaxpfadeMitWortErgebnis.get(1).getWort());
        assertEquals("222", syntaxpfadeMitWortErgebnis.get(2).getWort());
        assertEquals("-", syntaxpfadeMitWortErgebnis.get(3).getWort());
        assertEquals("333", syntaxpfadeMitWortErgebnis.get(4).getWort());
        assertEquals("*", syntaxpfadeMitWortErgebnis.get(5).getWort());
        assertEquals("444", syntaxpfadeMitWortErgebnis.get(6).getWort());
        assertEquals("/", syntaxpfadeMitWortErgebnis.get(7).getWort());
        assertEquals("555", syntaxpfadeMitWortErgebnis.get(8).getWort());
        checkSyntaxpfad(syntaxpfadeMitWortErgebnis.get(0).getSyntaxpfad(), Arrays.asList("S"), "Operand");
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
        final Parser parser = new Parser(grammatik);
        final String text = "Kunde Kundennummer 123 Vorname Hans Nachname Meier Artikel Artikelbezeichnung Waschmaschine Artikelnummer 456 ; ;\n";
        for (int index = 0; index < text.length(); index++) {
            parser.verarbeiteZeichen(text.charAt(index));
        }
        final List<SyntaxpfadMitWort> syntaxpfadeMitWortErgebnis = parser.gebeSyntaxpfadeMitWort();
        assertEquals(14, syntaxpfadeMitWortErgebnis.size());
        assertEquals("Kunde", syntaxpfadeMitWortErgebnis.get(0).getWort());
        assertEquals("Kundennummer", syntaxpfadeMitWortErgebnis.get(1).getWort());
        assertEquals("123", syntaxpfadeMitWortErgebnis.get(2).getWort());
        assertEquals("Vorname", syntaxpfadeMitWortErgebnis.get(3).getWort());
        assertEquals("Hans", syntaxpfadeMitWortErgebnis.get(4).getWort());
        assertEquals("Nachname", syntaxpfadeMitWortErgebnis.get(5).getWort());
        assertEquals("Meier", syntaxpfadeMitWortErgebnis.get(6).getWort());
        assertEquals("Artikel", syntaxpfadeMitWortErgebnis.get(7).getWort());
        assertEquals("Artikelbezeichnung", syntaxpfadeMitWortErgebnis.get(8).getWort());
        assertEquals("Waschmaschine", syntaxpfadeMitWortErgebnis.get(9).getWort());
        assertEquals("Artikelnummer", syntaxpfadeMitWortErgebnis.get(10).getWort());
        assertEquals("456", syntaxpfadeMitWortErgebnis.get(11).getWort());
        assertEquals(";", syntaxpfadeMitWortErgebnis.get(12).getWort());
        assertEquals(";", syntaxpfadeMitWortErgebnis.get(13).getWort());
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

    private void checkSyntaxpfad(final Syntaxpfad syntaxpfad, final List<String> knoten, final String blatt) {
        assertEquals(knoten.size(), syntaxpfad.gebeKnotenfolge().size());
        for (int index = 0; index < knoten.size(); index++) {
            assertEquals(knoten.get(index),
                    syntaxpfad.gebeKnotenfolge().get(index).getSymbolbezeichnung().getSymbolbezeichnung());
        }
        assertEquals(blatt, syntaxpfad.gebeBlatt().getSymbolbezeichnung().getSymbolbezeichnung());
    }
}
