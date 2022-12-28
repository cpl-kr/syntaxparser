package de.platen.syntaxparser.zeichenverarbeitung;

import de.platen.syntaxparser.SyntaxparserException;
import de.platen.syntaxparser.syntaxpfad.Syntaxpfadfolge;
import de.platen.syntaxparser.regelverarbeitung.Verarbeitung;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.fail;

public class VerarbeitungsstandTest {

    @Test
    public void testKonstruktorparameterNull() {
        try {
            new Verarbeitungsstand(null, Mockito.mock(Verarbeitung.class));
            fail();
        } catch (SyntaxparserException e) {
        }
        try {
            new Verarbeitungsstand(Mockito.mock(Syntaxpfadfolge.class), null);
            fail();
        } catch (SyntaxparserException e) {
        }
    }
}
