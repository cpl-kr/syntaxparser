package de.platen.syntaxparser.grammatik.elemente;

import java.io.Serializable;
import java.util.Objects;

import de.platen.syntaxparser.grammatik.GrammatikException;

public class Symbol implements Serializable
{

    private static final long serialVersionUID = 1L;

    private final Symbolkennung symbolkennung;
    private final Kardinalitaet kardinalitaet;

    public Symbol(final Symbolkennung symbolkennung, final Kardinalitaet kardinalitaet) {
        if ((symbolkennung == null) || (kardinalitaet == null)) {
            throw new GrammatikException();
        }
        this.symbolkennung = symbolkennung;
        this.kardinalitaet = kardinalitaet;
    }

    public Symbolkennung getSymbolkennung() {
        return symbolkennung;
    }

    public Kardinalitaet getKardinalitaet() {
        return kardinalitaet;
    }

    @Override
    public int hashCode() {
        return Objects.hash(kardinalitaet, symbolkennung);
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if ((obj == null) || (getClass() != obj.getClass())) {
            return false;
        }
        final Symbol other = (Symbol) obj;
        return (kardinalitaet == other.kardinalitaet) && Objects.equals(symbolkennung, other.symbolkennung);
    }
}
