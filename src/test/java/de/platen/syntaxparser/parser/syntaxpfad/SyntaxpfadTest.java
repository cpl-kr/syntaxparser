package de.platen.syntaxparser.parser.syntaxpfad;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import de.platen.syntaxparser.parser.SyntaxparserException;
import de.platen.syntaxparser.grammatik.elemente.Symbolbezeichnung;
import de.platen.syntaxparser.grammatik.elemente.Symbolidentifizierung;
import de.platen.syntaxparser.grammatik.elemente.Symbolkennung;

public class SyntaxpfadTest
{

    @Test
    public void testKopiere() {
        final Syntaxpfad syntaxpfad = new Syntaxpfad();
        syntaxpfad.zufuegenKnoten(
                new Symbolkennung(new Symbolbezeichnung("Knoten"), new Symbolidentifizierung(1)));
        syntaxpfad.zufuegenBlatt(
                new Symbolkennung(new Symbolbezeichnung("Blatt"), new Symbolidentifizierung(1)));
        assertEquals(syntaxpfad, syntaxpfad.kopiere());
    }

    @Test
    public void testKopiereMitNeuerWurzel() {
        final Syntaxpfad syntaxpfad1 = new Syntaxpfad();
        syntaxpfad1.zufuegenKnoten(
                new Symbolkennung(new Symbolbezeichnung("Knoten"), new Symbolidentifizierung(1)));
        syntaxpfad1.zufuegenBlatt(
                new Symbolkennung(new Symbolbezeichnung("Blatt"), new Symbolidentifizierung(1)));
        final Syntaxpfad syntaxpfad2 = new Syntaxpfad();
        syntaxpfad2.zufuegenKnoten(
                new Symbolkennung(new Symbolbezeichnung("Wurzel"), new Symbolidentifizierung(1)));
        syntaxpfad2.zufuegenKnoten(
                new Symbolkennung(new Symbolbezeichnung("Knoten"), new Symbolidentifizierung(1)));
        syntaxpfad2.zufuegenBlatt(
                new Symbolkennung(new Symbolbezeichnung("Blatt"), new Symbolidentifizierung(1)));
        assertEquals(syntaxpfad2, syntaxpfad1.kopiereMitNeuerWurzel(
                new Symbolkennung(new Symbolbezeichnung("Wurzel"), new Symbolidentifizierung(1))));
    }

    @Test(expected = SyntaxparserException.class)
    public void testKopiereMitNeuerWurzelParameterNull() {
        final Syntaxpfad syntaxpfad = new Syntaxpfad();
        syntaxpfad.zufuegenKnoten(
                new Symbolkennung(new Symbolbezeichnung("Knoten"), new Symbolidentifizierung(1)));
        syntaxpfad.zufuegenBlatt(
                new Symbolkennung(new Symbolbezeichnung("Blatt"), new Symbolidentifizierung(1)));
        syntaxpfad.kopiereMitNeuerWurzel(null);
    }

    @Test(expected = SyntaxparserException.class)
    public void testKopiereMitNeuerWurzelNichtFertig() {
        final Syntaxpfad syntaxpfad = new Syntaxpfad();
        syntaxpfad.zufuegenKnoten(
                new Symbolkennung(new Symbolbezeichnung("Knoten"), new Symbolidentifizierung(1)));
        syntaxpfad.kopiereMitNeuerWurzel(
                new Symbolkennung(new Symbolbezeichnung("Wurzel"), new Symbolidentifizierung(1)));
    }

    @Test
    public void testZufuegenKnoten() {
        final Syntaxpfad syntaxpfad = new Syntaxpfad();
        syntaxpfad.zufuegenKnoten(
                new Symbolkennung(new Symbolbezeichnung("Knoten"), new Symbolidentifizierung(1)));
    }

    @Test(expected = SyntaxparserException.class)
    public void testZufuegenKnotenParameterNull() {
        final Syntaxpfad syntaxpfad = new Syntaxpfad();
        syntaxpfad.zufuegenKnoten(null);
        fail();
    }

    @Test(expected = SyntaxparserException.class)
    public void testZufuegenKnotenParameterFertig() {
        final Syntaxpfad syntaxpfad = new Syntaxpfad();
        syntaxpfad.zufuegenKnoten(
                new Symbolkennung(new Symbolbezeichnung("Knoten1"), new Symbolidentifizierung(1)));
        syntaxpfad.zufuegenBlatt(
                new Symbolkennung(new Symbolbezeichnung("Blatt"), new Symbolidentifizierung(1)));
        syntaxpfad.zufuegenKnoten(
                new Symbolkennung(new Symbolbezeichnung("Knoten2"), new Symbolidentifizierung(1)));
        fail();
    }

    @Test
    public void testZufuegenBlatt() {
        final Syntaxpfad syntaxpfad = new Syntaxpfad();
        syntaxpfad.zufuegenKnoten(
                new Symbolkennung(new Symbolbezeichnung("Knoten"), new Symbolidentifizierung(1)));
        syntaxpfad.zufuegenBlatt(
                new Symbolkennung(new Symbolbezeichnung("Blatt"), new Symbolidentifizierung(1)));
    }

    @Test(expected = SyntaxparserException.class)
    public void testZufuegenBlattParameterNull() {
        final Syntaxpfad syntaxpfad = new Syntaxpfad();
        syntaxpfad.zufuegenKnoten(
                new Symbolkennung(new Symbolbezeichnung("Knoten"), new Symbolidentifizierung(1)));
        syntaxpfad.zufuegenBlatt(null);
        fail();
    }

    @Test(expected = SyntaxparserException.class)
    public void testZufuegenBlattFertig() {
        final Syntaxpfad syntaxpfad = new Syntaxpfad();
        syntaxpfad.zufuegenKnoten(
                new Symbolkennung(new Symbolbezeichnung("Knoten"), new Symbolidentifizierung(1)));
        syntaxpfad.zufuegenBlatt(
                new Symbolkennung(new Symbolbezeichnung("Blatt1"), new Symbolidentifizierung(1)));
        syntaxpfad.zufuegenBlatt(
                new Symbolkennung(new Symbolbezeichnung("Blatt2"), new Symbolidentifizierung(1)));
        fail();
    }

    @Test
    public void testGebeKnotenfolge() {
        final List<Symbolkennung> knoten = new ArrayList<>();
        knoten.add(new Symbolkennung(new Symbolbezeichnung("Knoten"), new Symbolidentifizierung(1)));
        final Syntaxpfad syntaxpfad = new Syntaxpfad();
        syntaxpfad.zufuegenKnoten(
                new Symbolkennung(new Symbolbezeichnung("Knoten"), new Symbolidentifizierung(1)));
        syntaxpfad.zufuegenBlatt(
                new Symbolkennung(new Symbolbezeichnung("Blatt"), new Symbolidentifizierung(1)));
        assertEquals(knoten, syntaxpfad.gebeKnotenfolge());
    }

    @Test
    public void testGebeBlatt() {
        final Symbolkennung blatt = new Symbolkennung(new Symbolbezeichnung("Blatt"),
                new Symbolidentifizierung(1));
        final Syntaxpfad syntaxpfad = new Syntaxpfad();
        syntaxpfad.zufuegenKnoten(
                new Symbolkennung(new Symbolbezeichnung("Knoten"), new Symbolidentifizierung(1)));
        syntaxpfad.zufuegenBlatt(
                new Symbolkennung(new Symbolbezeichnung("Blatt"), new Symbolidentifizierung(1)));
        assertEquals(blatt, syntaxpfad.gebeBlatt());
    }

    @Test(expected = SyntaxparserException.class)
    public void testGebeBlattNichtFertig() {
        final Syntaxpfad syntaxpfad = new Syntaxpfad();
        syntaxpfad.zufuegenKnoten(
                new Symbolkennung(new Symbolbezeichnung("Knoten"), new Symbolidentifizierung(1)));
        syntaxpfad.gebeBlatt();
        fail();
    }

    @Test
    public void testIstFertig() {
        final Syntaxpfad syntaxpfad = new Syntaxpfad();
        assertFalse(syntaxpfad.istFertig());
        syntaxpfad.zufuegenKnoten(
                new Symbolkennung(new Symbolbezeichnung("Knoten"), new Symbolidentifizierung(1)));
        assertFalse(syntaxpfad.istFertig());
        syntaxpfad.zufuegenBlatt(
                new Symbolkennung(new Symbolbezeichnung("Blatt"), new Symbolidentifizierung(1)));
        assertTrue(syntaxpfad.istFertig());
    }
}
