package de.platen.syntaxparser.grammatik.elemente;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import de.platen.syntaxparser.grammatik.GrammatikException;

public class RegelSymboleTest
{

    @Test(expected = GrammatikException.class)
    public void testKonstruktorParameterSymbolbezeichnungNull() {
        final List<Symbol> symbole = new ArrayList<Symbol>();
        symbole.add(new Symbol(new Symbolkennung(new Symbolbezeichnung("Symbolbezeichnung"),
                new Symbolidentifizierung(Integer.valueOf(1))), Kardinalitaet.GENAU_EINMAL));
        new RegelSymbole(null, symbole);
    }

    @Test(expected = GrammatikException.class)
    public void testKonstruktorParameterSymboleNull() {
        new RegelSymbole(new Symbolbezeichnung("Symbolbezeichnung"), null);
    }

    @Test(expected = GrammatikException.class)
    public void testKonstruktorParameterSymbolbezeichnungLeer() {
        final List<Symbol> symbole = new ArrayList<Symbol>();
        new RegelSymbole(null, symbole);
    }

    @Test
    public void testRegelSymbole() {
        final List<Symbol> symbole1 = new ArrayList<Symbol>();
        symbole1.add(new Symbol(new Symbolkennung(new Symbolbezeichnung("Symbolbezeichnung"),
                new Symbolidentifizierung(Integer.valueOf(1))), Kardinalitaet.GENAU_EINMAL));
        final List<Symbol> symbole2 = new ArrayList<Symbol>();
        symbole2.add(new Symbol(new Symbolkennung(new Symbolbezeichnung("Symbolbezeichnung"),
                new Symbolidentifizierung(Integer.valueOf(1))), Kardinalitaet.GENAU_EINMAL));
        final RegelSymbole regelSymbole1 = new RegelSymbole(new Symbolbezeichnung("RegelSymbole"), symbole1);
        final RegelSymbole regelSymbole2 = new RegelSymbole(new Symbolbezeichnung("RegelSymbole"), symbole2);
        final List<Symbol> symbole3 = new ArrayList<Symbol>();
        symbole3.add(new Symbol(new Symbolkennung(new Symbolbezeichnung("Symbolbezeichnung"),
                new Symbolidentifizierung(Integer.valueOf(1))), Kardinalitaet.GENAU_EINMAL));
        assertEquals(symbole3, regelSymbole1.getSymbole());
        assertEquals(regelSymbole1, regelSymbole2);
    }

    @Test(expected = GrammatikException.class)
    public void testIstLetztesSymbolParameterNull() {
        final RegelSymbole regelSymbole = new RegelSymbole(new Symbolbezeichnung("S"), erzeugeSymbole());
        regelSymbole.istLetztesSymbol(null);
    }

    @Test(expected = GrammatikException.class)
    public void testIstLetztesSymbolSymbolkennungNichtInRegel() {
        final RegelSymbole regelSymbole = new RegelSymbole(new Symbolbezeichnung("S"), erzeugeSymbole());
        regelSymbole.istLetztesSymbol(
                new Symbolkennung(new Symbolbezeichnung("symbol5"), new Symbolidentifizierung(Integer.valueOf(5))));
    }

    @Test
    public void testIstLetztesSymbolSymbolkennungAlsLetztesSymbol() {
        final RegelSymbole regelSymbole = new RegelSymbole(new Symbolbezeichnung("S"), erzeugeSymbole());
        assertTrue(regelSymbole.istLetztesSymbol(
                new Symbolkennung(new Symbolbezeichnung("symbol4"), new Symbolidentifizierung(Integer.valueOf(4)))));
    }

    @Test
    public void testIstLetztesSymbolSymbolkennungNichtAlsLetztesSymbol() {
        final RegelSymbole regelSymbole = new RegelSymbole(new Symbolbezeichnung("S"), erzeugeSymbole());
        assertFalse(regelSymbole.istLetztesSymbol(
                new Symbolkennung(new Symbolbezeichnung("symbol3"), new Symbolidentifizierung(Integer.valueOf(3)))));
    }

    @Test(expected = GrammatikException.class)
    public void testIstInRegelParameterNull() {
        final RegelSymbole regelSymbole = new RegelSymbole(new Symbolbezeichnung("S"), erzeugeSymbole());
        regelSymbole.istInRegel(null);
    }

    public void testIstInRegelSymbolkennungEnthalten() {
        final RegelSymbole regelSymbole = new RegelSymbole(new Symbolbezeichnung("S"), erzeugeSymbole());
        assertTrue(regelSymbole.istInRegel(
                new Symbolkennung(new Symbolbezeichnung("symbol3"), new Symbolidentifizierung(Integer.valueOf(3)))));
    }

    public void testIstInRegelSymbolkennungNichtEnthalten() {
        final RegelSymbole regelSymbole = new RegelSymbole(new Symbolbezeichnung("S"), erzeugeSymbole());
        assertTrue(regelSymbole.istInRegel(
                new Symbolkennung(new Symbolbezeichnung("symbol5"), new Symbolidentifizierung(Integer.valueOf(5)))));
    }

    @Test(expected = GrammatikException.class)
    public void testgebeNaechstesSymbolParameterNull() {
        final RegelSymbole regelSymbole = new RegelSymbole(new Symbolbezeichnung("S"), erzeugeSymbole());
        regelSymbole.gebeNaechstesSymbol(null);
    }

    @Test(expected = GrammatikException.class)
    public void testgebeNaechstesSymbolSymbolkennungNichtInRegel() {
        final RegelSymbole regelSymbole = new RegelSymbole(new Symbolbezeichnung("S"), erzeugeSymbole());
        regelSymbole.gebeNaechstesSymbol(
                new Symbolkennung(new Symbolbezeichnung("symbol5"), new Symbolidentifizierung(Integer.valueOf(5))));
    }

    @Test(expected = GrammatikException.class)
    public void testgebeNaechstesSymbolSymbolkennungIstLetztesSymbol() {
        final RegelSymbole regelSymbole = new RegelSymbole(new Symbolbezeichnung("S"), erzeugeSymbole());
        regelSymbole.gebeNaechstesSymbol(
                new Symbolkennung(new Symbolbezeichnung("symbol4"), new Symbolidentifizierung(Integer.valueOf(4))));
    }

    @Test
    public void testgebeNaechstesSymbolInnerhalbRegelSymbolkennungIstNichtLetztesSymbol() {
        final RegelSymbole regelSymbole = new RegelSymbole(new Symbolbezeichnung("S"), erzeugeSymbole());
        final Symbolkennung symbolkennung = regelSymbole.gebeNaechstesSymbol(
                new Symbolkennung(new Symbolbezeichnung("symbol3"), new Symbolidentifizierung(Integer.valueOf(3))));
        assertEquals(new Symbolkennung(new Symbolbezeichnung("symbol4"), new Symbolidentifizierung(Integer.valueOf(4))),
                symbolkennung);
    }

    private List<Symbol> erzeugeSymbole() {
        final List<Symbol> symbole = new ArrayList<>();
        symbole.add(new Symbol(
                new Symbolkennung(new Symbolbezeichnung("symbol1"), new Symbolidentifizierung(Integer.valueOf(1))),
                Kardinalitaet.GENAU_EINMAL));
        symbole.add(new Symbol(
                new Symbolkennung(new Symbolbezeichnung("symbol2"), new Symbolidentifizierung(Integer.valueOf(2))),
                Kardinalitaet.GENAU_EINMAL));
        symbole.add(new Symbol(
                new Symbolkennung(new Symbolbezeichnung("symbol3"), new Symbolidentifizierung(Integer.valueOf(3))),
                Kardinalitaet.GENAU_EINMAL));
        symbole.add(new Symbol(
                new Symbolkennung(new Symbolbezeichnung("symbol4"), new Symbolidentifizierung(Integer.valueOf(4))),
                Kardinalitaet.GENAU_EINMAL));
        return symbole;
    }
}
