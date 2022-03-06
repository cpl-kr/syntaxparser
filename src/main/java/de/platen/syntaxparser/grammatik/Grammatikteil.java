package de.platen.syntaxparser.grammatik;

import java.util.List;
import java.util.Set;

import de.platen.syntaxparser.grammatik.elemente.Kardinalitaet;
import de.platen.syntaxparser.grammatik.elemente.Symbol;
import de.platen.syntaxparser.grammatik.elemente.Symbolbezeichnung;

public class Grammatikteil
{

    private StringBuilder stringBuilder = new StringBuilder();
    private Symbolbezeichnung symbolbezeichnung;
    private Kardinalitaet kardinalitaet;
    private List<Symbol> symbole;
    private Character von;
    private Character bis;
    private Set<Character> characters;

    public StringBuilder getStringBuilder() {
        return stringBuilder;
    }

    public void setStringBuilder(final StringBuilder stringBuilder) {
        this.stringBuilder = stringBuilder;
    }

    public Symbolbezeichnung getSymbolbezeichnung() {
        return symbolbezeichnung;
    }

    public void setSymbolbezeichnung(final Symbolbezeichnung symbolbezeichnung) {
        this.symbolbezeichnung = symbolbezeichnung;
    }

    public Kardinalitaet getKardinalitaet() {
        return kardinalitaet;
    }

    public void setKardinalitaet(final Kardinalitaet kardinalitaet) {
        this.kardinalitaet = kardinalitaet;
    }

    public List<Symbol> getSymbole() {
        return symbole;
    }

    public void setSymbole(final List<Symbol> symbole) {
        this.symbole = symbole;
    }

    public Character getVon() {
        return von;
    }

    public void setVon(final Character von) {
        this.von = von;
    }

    public Character getBis() {
        return bis;
    }

    public void setBis(final Character bis) {
        this.bis = bis;
    }

    public Set<Character> getCharacters() {
        return characters;
    }

    public void setCharacters(final Set<Character> characters) {
        this.characters = characters;
    }
}
