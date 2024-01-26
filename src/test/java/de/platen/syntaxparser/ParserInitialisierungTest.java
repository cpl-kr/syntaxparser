package de.platen.syntaxparser;

import de.platen.syntaxparser.grammatik.Grammatik;
import de.platen.syntaxparser.grammatik.GrammatikAufbau;
import de.platen.syntaxparser.grammatik.GrammatikLesen;
import de.platen.syntaxparser.syntaxpfad.Syntaxpfadbehandlung;
import de.platen.syntaxparser.syntaxpfad.Syntaxpfadersteller;
import de.platen.syntaxparser.zeichenverarbeitung.Verarbeitungsstand;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

public class ParserInitialisierungTest {

    @Test
    public void testKonstruktorparameterNull() {
        final Syntaxpfadersteller syntaxpfadersteller = Mockito.mock(Syntaxpfadersteller.class);
        final Syntaxpfadbehandlung syntaxpfadbehandlung = Mockito.mock(Syntaxpfadbehandlung.class);
        try {
            new ParserInitialisierung(null, syntaxpfadbehandlung);
            fail();
        } catch (SyntaxparserException e) {
        }
        try {
            new ParserInitialisierung(syntaxpfadersteller, null);
            fail();
        } catch (SyntaxparserException e) {
        }
    }

    @Test
    public void testInitialisiereVerarbeitungsstaende() {
        final List<String> regeln = Arrays.asList("S { R }\n", //
                "R { Z1 }\n", //
                "R { Z2 }\n", //
                "Z1 (+-*/)\n", //
                "Z2 [09]\n");
        Grammatik grammatik = erstelleGrammatik(regeln);
        final Syntaxpfadersteller syntaxpfadersteller = new Syntaxpfadersteller(grammatik);
        final Syntaxpfadbehandlung syntaxpfadbehandlung = new Syntaxpfadbehandlung(grammatik);
        ParserInitialisierung parserInitialisierung = new ParserInitialisierung(syntaxpfadersteller, syntaxpfadbehandlung);
        Set<Verarbeitungsstand> verarbeitungsstaende = parserInitialisierung.initialisiereVerarbeitungsstaende();
        assertNotNull(verarbeitungsstaende);
        assertEquals(2, verarbeitungsstaende.size());
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
