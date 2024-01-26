package de.platen.syntaxparser.regelverarbeitung;

import de.platen.syntaxparser.SyntaxparserException;
import de.platen.syntaxparser.grammatik.elemente.Symbolbezeichnung;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class VerarbeitungTest {

    @Test
    public void testKonstruktorparameter() {
        Symbolbezeichnung symbolbezeichnung = Mockito.mock(Symbolbezeichnung.class);
        Verarbeitung verarbeitung = new Verarbeitung(symbolbezeichnung) {
            @Override
            public boolean verarbeiteZeichen(Character character) {
                return false;
            }
        };
        assertEquals(symbolbezeichnung, verarbeitung.gebeSymbolbezeichnung());
    }

    @Test(expected = SyntaxparserException.class)
    public void testKonstruktorparameterNull() {
        new Verarbeitung(null) {
            @Override
            public boolean verarbeiteZeichen(Character character) {
                return false;
            }
        };
        fail();
    }
}
