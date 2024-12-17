package de.platen.syntaxparser.grammatik.elemente;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import de.platen.syntaxparser.grammatik.GrammatikException;

public class SymbolregelnTest
{

    @Test(expected = GrammatikException.class)
    public void testKonstruktorParameterNull() {
        new Symbolregeln(null);
    }

    @Test
    public void testKonstruktor() {
        new Symbolregeln(erzeugeRegeln());
    }

    @Test(expected = GrammatikException.class)
    public void testErmittleRegelParameterNull() {
        final Symbolregeln symbolregeln = new Symbolregeln(erzeugeRegeln());
        symbolregeln.ermittleRegel(null);
    }

    @Test(expected = GrammatikException.class)
    public void testErmittleRegelParameterNichtInRegel() {
        final Symbolregeln symbolregeln = new Symbolregeln(erzeugeRegeln());
        symbolregeln.ermittleRegel(
                new Symbolkennung(new Symbolbezeichnung("symbol0"), new Symbolidentifizierung(Integer.valueOf(0))));
    }

    @Test
    public void testErmittleRegel() {
        final Symbolregeln symbolregeln = new Symbolregeln(erzeugeRegeln());
        final List<Symbol> symbole1 = Arrays.asList(erzeugeSymbol("symbol1", 1), erzeugeSymbol("symbol2", 2),
                erzeugeSymbol("symbol3", 3), erzeugeSymbol("symbol4", 4));
        final List<Symbol> symbole2 = Arrays.asList(erzeugeSymbol("symbol5", 5), erzeugeSymbol("symbol6", 6),
                erzeugeSymbol("symbol7", 7), erzeugeSymbol("symbol8", 8));
        final List<Symbol> symbole3 = Arrays.asList(erzeugeSymbol("symbol9", 9), erzeugeSymbol("symbol10", 10),
                erzeugeSymbol("symbol11", 11), erzeugeSymbol("symbol12", 12));
        final List<Symbol> symbole4 = Arrays.asList(erzeugeSymbol("symbol13", 13), erzeugeSymbol("symbol14", 14),
                erzeugeSymbol("symbol15", 15), erzeugeSymbol("symbol16", 16));
        assertEquals(symbole1, symbolregeln.ermittleRegel(
                new Symbolkennung(new Symbolbezeichnung("symbol2"), new Symbolidentifizierung(Integer.valueOf(2)))));
        assertEquals(symbole2, symbolregeln.ermittleRegel(
                new Symbolkennung(new Symbolbezeichnung("symbol6"), new Symbolidentifizierung(Integer.valueOf(6)))));
        assertEquals(symbole3, symbolregeln.ermittleRegel(
                new Symbolkennung(new Symbolbezeichnung("symbol10"), new Symbolidentifizierung(Integer.valueOf(10)))));
        assertEquals(symbole4, symbolregeln.ermittleRegel(
                new Symbolkennung(new Symbolbezeichnung("symbol14"), new Symbolidentifizierung(Integer.valueOf(14)))));
    }

    @Test(expected = GrammatikException.class)
    public void testIstLetztesSymbolInRegelParameterNull() {
        final Symbolregeln symbolregeln = new Symbolregeln(erzeugeRegeln());
        symbolregeln.istLetztesSymbolInRegel(null);
    }

    @Test(expected = GrammatikException.class)
    public void testIstLetztesSymbolInRegelParameterNichtInRegel() {
        final Symbolregeln symbolregeln = new Symbolregeln(erzeugeRegeln());
        symbolregeln.istLetztesSymbolInRegel(
                new Symbolkennung(new Symbolbezeichnung("symbol0"), new Symbolidentifizierung(Integer.valueOf(0))));
    }

    @Test
    public void testIstLetztesSymbolInRegelSymbolkennungAlsLetztesSymbol() {
        final Symbolregeln symbolregeln = new Symbolregeln(erzeugeRegeln());
        assertTrue(symbolregeln.istLetztesSymbolInRegel(
                new Symbolkennung(new Symbolbezeichnung("symbol4"), new Symbolidentifizierung(Integer.valueOf(4)))));
    }

    @Test
    public void testIstLetztesSymbolInRegelSymbolkennungNichtAlsLetztesSymbol() {
        final Symbolregeln symbolregeln = new Symbolregeln(erzeugeRegeln());
        assertFalse(symbolregeln.istLetztesSymbolInRegel(
                new Symbolkennung(new Symbolbezeichnung("symbol3"), new Symbolidentifizierung(Integer.valueOf(3)))));
    }

    @Test(expected = GrammatikException.class)
    public void testgebeNaechstesSymbolInnerhalbRegelParameterNull() {
        final Symbolregeln symbolregeln = new Symbolregeln(erzeugeRegeln());
        symbolregeln.gebeNaechstesSymbolInnerhalbRegel(null);
    }

    @Test(expected = GrammatikException.class)
    public void testgebeNaechstesSymbolInnerhalbRegelParameterNichtInRegel() {
        final Symbolregeln symbolregeln = new Symbolregeln(erzeugeRegeln());
        symbolregeln.gebeNaechstesSymbolInnerhalbRegel(
                new Symbolkennung(new Symbolbezeichnung("symbol0"), new Symbolidentifizierung(Integer.valueOf(0))));
    }

    @Test(expected = GrammatikException.class)
    public void testgebeNaechstesSymbolInnerhalbRegelSymbolkennungIstLetztesSymbol() {
        final Symbolregeln symbolregeln = new Symbolregeln(erzeugeRegeln());
        symbolregeln.gebeNaechstesSymbolInnerhalbRegel(
                new Symbolkennung(new Symbolbezeichnung("symbol4"), new Symbolidentifizierung(Integer.valueOf(4))));
    }

    @Test
    public void testgebeNaechstesSymbolInnerhalbRegelSymbolkennungIstNichtLetztesSymbol() {
        final Symbolregeln symbolregeln = new Symbolregeln(erzeugeRegeln());
        final Symbolkennung symbolkennung = symbolregeln.gebeNaechstesSymbolInnerhalbRegel(
                new Symbolkennung(new Symbolbezeichnung("symbol3"), new Symbolidentifizierung(Integer.valueOf(3))));
        assertEquals(new Symbolkennung(new Symbolbezeichnung("symbol4"), new Symbolidentifizierung(Integer.valueOf(4))),
                symbolkennung);
    }

    private Map<Symbolbezeichnung, List<List<Symbol>>> erzeugeRegeln() {
        final Map<Symbolbezeichnung, List<List<Symbol>>> symbolregeln = new HashMap<>();
        final List<Symbol> symbole1 = Arrays.asList(erzeugeSymbol("symbol1", 1), erzeugeSymbol("symbol2", 2),
                erzeugeSymbol("symbol3", 3), erzeugeSymbol("symbol4", 4));
        final List<Symbol> symbole2 = Arrays.asList(erzeugeSymbol("symbol5", 5), erzeugeSymbol("symbol6", 6),
                erzeugeSymbol("symbol7", 7), erzeugeSymbol("symbol8", 8));
        final List<Symbol> symbole3 = Arrays.asList(erzeugeSymbol("symbol9", 9), erzeugeSymbol("symbol10", 10),
                erzeugeSymbol("symbol11", 11), erzeugeSymbol("symbol12", 12));
        final List<Symbol> symbole4 = Arrays.asList(erzeugeSymbol("symbol13", 13), erzeugeSymbol("symbol14", 14),
                erzeugeSymbol("symbol15", 15), erzeugeSymbol("symbol16", 16));
        final List<List<Symbol>> regelmenge1 = new ArrayList<>();
        final List<List<Symbol>> regelmenge2 = new ArrayList<>();
        regelmenge1.add(symbole1);
        regelmenge1.add(symbole2);
        regelmenge2.add(symbole3);
        regelmenge2.add(symbole4);
        symbolregeln.put(new Symbolbezeichnung("Regeln1"), regelmenge1);
        symbolregeln.put(new Symbolbezeichnung("Regeln2"), regelmenge2);
        return symbolregeln;
    }

    private Symbol erzeugeSymbol(final String bezeichnung, final int id) {
        return new Symbol(
                new Symbolkennung(new Symbolbezeichnung(bezeichnung), new Symbolidentifizierung(Integer.valueOf(id))),
                Kardinalitaet.GENAU_EINMAL);
    }
}
