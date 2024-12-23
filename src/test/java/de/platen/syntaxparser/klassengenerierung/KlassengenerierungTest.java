package de.platen.syntaxparser.klassengenerierung;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.HashMap;
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
        final Set<PaketNameInhalt> generierteKlassen = assertDoesNotThrow(() -> klassengenerierung.generiere("paket"));
        assertEquals(8, generierteKlassen.size());
        PaketNameInhalt paketNameInhalt = new PaketNameInhalt("paket", "S", "package paket;\n\nimport java.io.Serializable;\nimport java.util.List;\n\npublic record S(S0 s0_1, S1 s1_2, S2 s2_3, S3 s3_4, S4 s4_5) implements Serializable {}\n");
        assertTrue(generierteKlassen.contains(paketNameInhalt));
        paketNameInhalt = new PaketNameInhalt("paket", "S0", "package paket;\n\nimport java.io.Serializable;\nimport java.util.List;\n\npublic record S0(S0_1 s0_1_1, S0_2 s0_2_2) implements Serializable {}\n");
        assertTrue(generierteKlassen.contains(paketNameInhalt));
        paketNameInhalt = new PaketNameInhalt("paket", "S0_1", "package paket;\n\nimport java.io.Serializable;\nimport java.util.List;\n\npublic record S0_1(S1 s1_1) implements Serializable {}\n");
        assertTrue(generierteKlassen.contains(paketNameInhalt));
        paketNameInhalt = new PaketNameInhalt("paket", "S0_2", "package paket;\n\nimport java.io.Serializable;\nimport java.util.List;\n\npublic record S0_2(S2 s2_1) implements Serializable {}\n");
        assertTrue(generierteKlassen.contains(paketNameInhalt));
        paketNameInhalt = new PaketNameInhalt("paket", "S1", """
                package paket;

                import java.io.Serializable;
                import java.util.List;
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
        assertTrue(generierteKlassen.contains(paketNameInhalt));
        paketNameInhalt = new PaketNameInhalt("paket", "S2", "package paket;\n\nimport java.io.Serializable;\nimport java.util.List;\n\npublic record S2(String value) implements Serializable {}\n");
        assertTrue(generierteKlassen.contains(paketNameInhalt));
        paketNameInhalt = new PaketNameInhalt("paket", "S3", "package paket;\n\nimport java.io.Serializable;\nimport java.util.List;\n\npublic record S3(String value) implements Serializable {}\n");
        assertTrue(generierteKlassen.contains(paketNameInhalt));
        paketNameInhalt = new PaketNameInhalt("paket", "S4", "package paket;\n\nimport java.io.Serializable;\nimport java.util.List;\n\npublic record S4(String value) implements Serializable {}\n");
        assertTrue(generierteKlassen.contains(paketNameInhalt));
    }

    @Test
    public void testGeneriereSymbolalternativen() {
        final List<String> regeln = Arrays.asList("S { Operand Operationsteil }\n", //
                "Operationsteil { Operator Operand }\n", //
                "Operationsteil { Operator Operand Operationsteil }\n", //
                "Operator (+-*/)\n", //
                "Operand [09]\n");
        final Grammatik grammatik = erstelleGrammatik(regeln);
        final Klassengenerierung klassengenerierung = new Klassengenerierung(grammatik);
        final Set<PaketNameInhalt> generierteKlassen = assertDoesNotThrow(() -> klassengenerierung.generiere("paket.name"));
        assertEquals(7, generierteKlassen.size());
        PaketNameInhalt paketNameInhalt = new PaketNameInhalt("paket.name", "S", """
                package paket.name;

                import java.io.Serializable;
                import java.util.List;

                public record S(Operand operand_1, Operationsteil operationsteil_2) implements Serializable {}
                """);
        assertTrue(generierteKlassen.contains(paketNameInhalt));
        paketNameInhalt = new PaketNameInhalt("paket.name", "Operationsteil", """
                package paket.name;

                import java.io.Serializable;
                import java.util.List;

                import paket.name.paketnameOperationsteil.OperationsteilInterface;

                public record Operationsteil(OperationsteilInterface value) implements Serializable {}
                """);
        assertTrue(generierteKlassen.contains(paketNameInhalt));
        paketNameInhalt = new PaketNameInhalt("paket.name", "Operator", """
                package paket.name;

                import java.io.Serializable;
                import java.util.List;

                public record Operator(String value) implements Serializable {}
                """);
        assertTrue(generierteKlassen.contains(paketNameInhalt));
        paketNameInhalt = new PaketNameInhalt("paket.name", "Operand", """
                package paket.name;

                import java.io.Serializable;
                import java.util.List;

                public record Operand(String value) implements Serializable {}
                """);
        assertTrue(generierteKlassen.contains(paketNameInhalt));
        paketNameInhalt = new PaketNameInhalt("paket.name.paketnameOperationsteil", "OperationsteilInterface", """
                package paket.name.paketnameOperationsteil;

                public interface OperationsteilInterface {}
                """);
        assertTrue(generierteKlassen.contains(paketNameInhalt));
        paketNameInhalt = new PaketNameInhalt("paket.name.paketnameOperationsteil", "Operationsteil1", """
                package paket.name.paketnameOperationsteil;

                import java.io.Serializable;
                import java.util.List;

                import paket.name.Operator;
                import paket.name.Operand;


                public record Operationsteil1(Operator operator_1, Operand operand_2) implements Serializable, OperationsteilInterface {}
                """);
        assertTrue(generierteKlassen.contains(paketNameInhalt));
        paketNameInhalt = new PaketNameInhalt("paket.name.paketnameOperationsteil", "Operationsteil2", """
                package paket.name.paketnameOperationsteil;

                import java.io.Serializable;
                import java.util.List;

                import paket.name.Operator;
                import paket.name.Operand;
                import paket.name.Operationsteil;


                public record Operationsteil2(Operator operator_1, Operand operand_2, Operationsteil operationsteil_3) implements Serializable, OperationsteilInterface {}
                """);
        assertTrue(generierteKlassen.contains(paketNameInhalt));
    }

    @Test
    public void testGeneriereKardinalitaet() {
        final List<String> regeln = Arrays.asList("S { S0 S1 S2+ S3+ S4 }\n", //
                "S0 { S0_1 S0_2 }", //
                "S0_1 { S1+ }", //
                "S0_2 { S2 }", //
                "S1 \"test\"\n", //
                "S2 [az]\n", //
                "S3 (.,\\\\)\n", //
                "S4 <[0-9]+,[0-9]+>");
        final Grammatik grammatik = erstelleGrammatik(regeln);
        final Klassengenerierung klassengenerierung = new Klassengenerierung(grammatik);
        final Set<PaketNameInhalt> generierteKlassen = assertDoesNotThrow(() -> klassengenerierung.generiere("paket"));
        assertEquals(8, generierteKlassen.size());
        PaketNameInhalt paketNameInhalt = new PaketNameInhalt("paket", "S", "package paket;\n\nimport java.io.Serializable;\nimport java.util.List;\n\npublic record S(S0 s0_1, S1 s1_2, List<S2> s2_3, List<S3> s3_4, S4 s4_5) implements Serializable {}\n");
        assertTrue(generierteKlassen.contains(paketNameInhalt));
        paketNameInhalt = new PaketNameInhalt("paket", "S0", "package paket;\n\nimport java.io.Serializable;\nimport java.util.List;\n\npublic record S0(S0_1 s0_1_1, S0_2 s0_2_2) implements Serializable {}\n");
        assertTrue(generierteKlassen.contains(paketNameInhalt));
        paketNameInhalt = new PaketNameInhalt("paket", "S0_1", "package paket;\n\nimport java.io.Serializable;\nimport java.util.List;\n\npublic record S0_1(List<S1> s1_1) implements Serializable {}\n");
        assertTrue(generierteKlassen.contains(paketNameInhalt));
        paketNameInhalt = new PaketNameInhalt("paket", "S0_2", "package paket;\n\nimport java.io.Serializable;\nimport java.util.List;\n\npublic record S0_2(S2 s2_1) implements Serializable {}\n");
        assertTrue(generierteKlassen.contains(paketNameInhalt));
        paketNameInhalt = new PaketNameInhalt("paket", "S1", """
                package paket;

                import java.io.Serializable;
                import java.util.List;
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
        assertTrue(generierteKlassen.contains(paketNameInhalt));
        paketNameInhalt = new PaketNameInhalt("paket", "S2", "package paket;\n\nimport java.io.Serializable;\nimport java.util.List;\n\npublic record S2(String value) implements Serializable {}\n");
        assertTrue(generierteKlassen.contains(paketNameInhalt));
        paketNameInhalt = new PaketNameInhalt("paket", "S3", "package paket;\n\nimport java.io.Serializable;\nimport java.util.List;\n\npublic record S3(String value) implements Serializable {}\n");
        assertTrue(generierteKlassen.contains(paketNameInhalt));
        paketNameInhalt = new PaketNameInhalt("paket", "S4", "package paket;\n\nimport java.io.Serializable;\nimport java.util.List;\n\npublic record S4(String value) implements Serializable {}\n");
        assertTrue(generierteKlassen.contains(paketNameInhalt));
    }

    @Test
    public void testGeneriereExceptionZeichenfolgeMehrmals() {
        final List<String> regeln = Arrays.asList("S { Operand Operationsteil }\n", //
                "Operationsteil { Operator Operand }\n", //
                "Operationsteil { Operator Operand Operationsteil }\n", //
                "Operator \"+\"\n", //
                "Operator \"-\"\n", //
                "Operator \"*\"\n", //
                "Operator \"/\"\n", //
                "Operand [09]\n");
        final Grammatik grammatik = erstelleGrammatik(regeln);
        final Klassengenerierung klassengenerierung = new Klassengenerierung(grammatik);
        assertThrows(KlassengenerierungException.class, () -> klassengenerierung.generiere("paket"));
    }

    @Test
    public void testGeneriereMitDatentypabbildungen() {
        final List<String> regeln = Arrays.asList("S { S0 S1 S2 S3 S4 }\n", //
                "S0 { S0_1 S0_2 }", //
                "S0_1 { S1 }", //
                "S0_2 { S2 }", //
                "S1 \"test\"\n", //
                "S2 [az]\n", //
                "S3 (.,\\\\)\n", //
                "S4 <[0-9]+,[0-9]+>");
        final Grammatik grammatik = erstelleGrammatik(regeln);
        final HashMap<String, Datentyp> datentypabbildungen = new HashMap<>();
        datentypabbildungen.put("S2", Datentyp.INTEGER);
        datentypabbildungen.put("S3", Datentyp.LONG);
        datentypabbildungen.put("S4", Datentyp.BYTES);
        final Klassengenerierung klassengenerierung = new Klassengenerierung(grammatik, datentypabbildungen);
        final Set<PaketNameInhalt> generierteKlassen = assertDoesNotThrow(() -> klassengenerierung.generiere("paket"));
        assertEquals(8, generierteKlassen.size());
        PaketNameInhalt paketNameInhalt = new PaketNameInhalt("paket", "S", "package paket;\n\nimport java.io.Serializable;\nimport java.util.List;\n\npublic record S(S0 s0_1, S1 s1_2, S2 s2_3, S3 s3_4, S4 s4_5) implements Serializable {}\n");
        assertTrue(generierteKlassen.contains(paketNameInhalt));
        paketNameInhalt = new PaketNameInhalt("paket", "S0", "package paket;\n\nimport java.io.Serializable;\nimport java.util.List;\n\npublic record S0(S0_1 s0_1_1, S0_2 s0_2_2) implements Serializable {}\n");
        assertTrue(generierteKlassen.contains(paketNameInhalt));
        paketNameInhalt = new PaketNameInhalt("paket", "S0_1", "package paket;\n\nimport java.io.Serializable;\nimport java.util.List;\n\npublic record S0_1(S1 s1_1) implements Serializable {}\n");
        assertTrue(generierteKlassen.contains(paketNameInhalt));
        paketNameInhalt = new PaketNameInhalt("paket", "S0_2", "package paket;\n\nimport java.io.Serializable;\nimport java.util.List;\n\npublic record S0_2(S2 s2_1) implements Serializable {}\n");
        assertTrue(generierteKlassen.contains(paketNameInhalt));
        paketNameInhalt = new PaketNameInhalt("paket", "S1", """
                package paket;

                import java.io.Serializable;
                import java.util.List;
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
        assertTrue(generierteKlassen.contains(paketNameInhalt));
        paketNameInhalt = new PaketNameInhalt("paket", "S2", "package paket;\n\nimport java.io.Serializable;\nimport java.util.List;\n\npublic record S2(Integer value) implements Serializable {}\n");
        assertTrue(generierteKlassen.contains(paketNameInhalt));
        paketNameInhalt = new PaketNameInhalt("paket", "S3", "package paket;\n\nimport java.io.Serializable;\nimport java.util.List;\n\npublic record S3(Long value) implements Serializable {}\n");
        assertTrue(generierteKlassen.contains(paketNameInhalt));
        paketNameInhalt = new PaketNameInhalt("paket", "S4", "package paket;\n\nimport java.io.Serializable;\nimport java.util.List;\n\npublic record S4(byte[] value) implements Serializable {}\n");
        assertTrue(generierteKlassen.contains(paketNameInhalt));
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
