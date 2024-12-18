package de.platen.syntaxparser.klassengenerierung;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import de.platen.syntaxparser.grammatik.Grammatik;
import de.platen.syntaxparser.grammatik.GrammatikAufbau;
import de.platen.syntaxparser.grammatik.GrammatikLesen;
import org.junit.Test;

public class KlassengenerierungTest {

    @Test
    public void testGeneriere() {
        final List<String> regeln = Arrays.asList("S { S0 S1 S2 S3 S4 }\n", //
                "S0 { S0_1 S0_2 }", //
                "S0_1 { S1 }", //
                "S0_2 { S2 }", //
                "S1 \"test\"\n", //
                "S2 [az]\n", //
                "S3 (.,\\\\)\n", //
                "S4 <[0-9]+,[0-9]+>");
        final Grammatik grammatik = erstelleGrammatik(regeln);
        final Klassengenerierung klassengenerierung = new Klassengenerierung(grammatik);
        final Set<NameInhalt> generierteKlassen = assertDoesNotThrow(() -> klassengenerierung.generiere("paket"));
        assertEquals(8, generierteKlassen.size());
        NameInhalt nameInhalt = new NameInhalt("S", "package paket;\n\nimport java.io.Serializable;\n\npublic record S(S0 s0_1, S1 s1_2, S2 s2_3, S3 s3_4, S4 s4_5) implements Serializable {}\n");
        assertTrue(generierteKlassen.contains(nameInhalt));
        nameInhalt = new NameInhalt("S0", "package paket;\n\nimport java.io.Serializable;\n\npublic record S0(S0_1 s0_1_1, S0_2 s0_2_2) implements Serializable {}\n");
        assertTrue(generierteKlassen.contains(nameInhalt));
        nameInhalt = new NameInhalt("S0_1", "package paket;\n\nimport java.io.Serializable;\n\npublic record S0_1(S1 s1_1) implements Serializable {}\n");
        assertTrue(generierteKlassen.contains(nameInhalt));
        nameInhalt = new NameInhalt("S0_2", "package paket;\n\nimport java.io.Serializable;\n\npublic record S0_2(S2 s2_1) implements Serializable {}\n");
        assertTrue(generierteKlassen.contains(nameInhalt));
        nameInhalt = new NameInhalt("S1", """
                package paket;

                import java.io.Serializable;
                import java.util.Objects;

                public class S1 implements Serializable {

                    private final String value = "test";

                    public String value() {
                        return value;
                    }

                    @Override
                    public boolean equals(Object o) {
                        if (!(o instanceof S1 S1)) return false;
                        return Objects.equals(value, S1.value);
                    }

                    @Override
                    public int hashCode() {
                        return Objects.hashCode(value);
                    }

                    @Override
                    public String toString() {
                        return "S1{" +
                                "value='" + value + '\\'' +
                                '}';
                    }
                }
                """);
        assertTrue(generierteKlassen.contains(nameInhalt));
        nameInhalt = new NameInhalt("S2", "package paket;\n\nimport java.io.Serializable;\n\npublic record S2(String value) implements Serializable {}\n");
        assertTrue(generierteKlassen.contains(nameInhalt));
        nameInhalt = new NameInhalt("S3", "package paket;\n\nimport java.io.Serializable;\n\npublic record S3(String value) implements Serializable {}\n");
        assertTrue(generierteKlassen.contains(nameInhalt));
        nameInhalt = new NameInhalt("S4", "package paket;\n\nimport java.io.Serializable;\n\npublic record S4(String value) implements Serializable {}\n");
        assertTrue(generierteKlassen.contains(nameInhalt));
    }

    @Test
    public void testGeneriereExceptionSymbolalternativen() {
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
        final Klassengenerierung klassengenerierung = new Klassengenerierung(grammatik);
        assertThrows(KlassengenerierungException.class, () -> klassengenerierung.generiere("paket"));
    }

    @Test
    public void testGeneriereExceptionKardinalitaet() {
        final List<String> regeln = Arrays.asList("S { S0 S1 S2 S3 S4 }\n", //
                "S0 { S0_1 S0_2 }", //
                "S0_1 { S1+ }", //
                "S0_2 { S2 }", //
                "S1 \"test\"\n", //
                "S2 [az]\n", //
                "S3 (.,\\\\)\n", //
                "S4 <[0-9]+,[0-9]+>");
        final Grammatik grammatik = erstelleGrammatik(regeln);
        final Klassengenerierung klassengenerierung = new Klassengenerierung(grammatik);
        assertThrows(KlassengenerierungException.class, () -> klassengenerierung.generiere("paket"));
    }

    @Test
    public void testGeneriereExceptionZeichenfolgeMitWhitespace() {
        final List<String> regeln = Arrays.asList("S { S0 S1 S2 S3 S4 }\n", //
                "S0 { S0_1 S0_2 }", //
                "S0_1 { S1 }", //
                "S0_2 { S2 }", //
                "S1 \"test\\ test\"\n", //
                "S2 [az]\n", //
                "S3 (.,\\\\)\n", //
                "S4 <[0-9]+,[0-9]+>");
        final Grammatik grammatik = erstelleGrammatik(regeln);
        final Klassengenerierung klassengenerierung = new Klassengenerierung(grammatik);
        assertThrows(KlassengenerierungException.class, () -> klassengenerierung.generiere("paket"));
    }

    private static Grammatik erstelleGrammatik(final List<String> regeln) {
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
