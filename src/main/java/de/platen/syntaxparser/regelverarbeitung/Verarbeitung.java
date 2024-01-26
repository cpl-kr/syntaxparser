package de.platen.syntaxparser.regelverarbeitung;

import de.platen.syntaxparser.SyntaxparserException;
import de.platen.syntaxparser.grammatik.elemente.Symbolbezeichnung;

import java.util.Objects;

public abstract class Verarbeitung {

    private final Symbolbezeichnung symbolbezeichnung;
    protected String wort = "";
    protected boolean istZustandErreicht = false;
    protected boolean istGesperrt = false;
    protected boolean istStartzustand = true;

    protected Verarbeitung(final Symbolbezeichnung symbolbezeichnung) {
        if (symbolbezeichnung == null) {
            throw new SyntaxparserException();
        }
        this.symbolbezeichnung = symbolbezeichnung;
    }

    public Symbolbezeichnung gebeSymbolbezeichnung() { return this.symbolbezeichnung; }

    public String gebeWort() { return this.wort; }

    public boolean istZustandErreicht() { return this.istZustandErreicht; }

    public boolean istGesperrt() { return this.istGesperrt; }

    public boolean istStartzustand() { return this.istStartzustand; }
    public abstract boolean verarbeiteZeichen(final Character character);

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Verarbeitung that = (Verarbeitung) o;
        return istZustandErreicht == that.istZustandErreicht && istGesperrt == that.istGesperrt && istStartzustand == that.istStartzustand && symbolbezeichnung.equals(that.symbolbezeichnung) && wort.equals(that.wort);
    }

    @Override
    public int hashCode() {
        return Objects.hash(symbolbezeichnung, wort, istZustandErreicht, istGesperrt, istStartzustand);
    }
}
