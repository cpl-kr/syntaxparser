package de.platen.syntaxparser.parser.syntaxpfad;

import org.junit.Test;

import de.platen.syntaxparser.parser.SyntaxparserException;
import de.platen.syntaxparser.grammatik.elemente.Symbolbezeichnung;
import de.platen.syntaxparser.grammatik.elemente.Symbolidentifizierung;
import de.platen.syntaxparser.grammatik.elemente.Symbolkennung;

public class SyntaxpfadMitWortTest
{

    @Test(expected = SyntaxparserException.class)
    public void testKonstruktorParameterSyntaxpfadNull() {
        new SyntaxpfadMitWort(null, "test");
    }

    @Test(expected = SyntaxparserException.class)
    public void testKonstruktorParameterWortNull() {
        new SyntaxpfadMitWort(new Syntaxpfad(), null);
    }

    @Test(expected = SyntaxparserException.class)
    public void testKonstruktorParameterWortBlank() {
        new SyntaxpfadMitWort(new Syntaxpfad(), " ");
    }

    @Test(expected = SyntaxparserException.class)
    public void testKonstruktorParameterSyntaxpfadNichtFertig() {
        new SyntaxpfadMitWort(new Syntaxpfad(), "test");
    }

    @Test
    public void testKonstruktor() {
        final Syntaxpfad syntaxpfad = new Syntaxpfad();
        syntaxpfad.zufuegenKnoten(
                new Symbolkennung(new Symbolbezeichnung("symbol"), new Symbolidentifizierung(0)));
        syntaxpfad.zufuegenBlatt(
                new Symbolkennung(new Symbolbezeichnung("symbol"), new Symbolidentifizierung(0)));
        new SyntaxpfadMitWort(syntaxpfad, "test");
    }
}
