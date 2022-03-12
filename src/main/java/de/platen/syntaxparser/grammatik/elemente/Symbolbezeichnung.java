package de.platen.syntaxparser.grammatik.elemente;

import java.io.Serializable;
import java.util.Objects;

import de.platen.syntaxparser.grammatik.GrammatikException;

public class Symbolbezeichnung implements Serializable
{

    private static final long serialVersionUID = 1L;

    private final String symbolbezeichnung;

    public Symbolbezeichnung(final String symbolbezeichnung) {
        if ((symbolbezeichnung == null) || symbolbezeichnung.isBlank()) {
            throw new GrammatikException();
        }
        this.symbolbezeichnung = symbolbezeichnung;
    }

    public String getSymbolbezeichnung() {
        return symbolbezeichnung;
    }

    @Override
    public int hashCode() {
        return Objects.hash(symbolbezeichnung);
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if ((obj == null) || (getClass() != obj.getClass())) {
            return false;
        }
        final Symbolbezeichnung other = (Symbolbezeichnung) obj;
        return Objects.equals(symbolbezeichnung, other.symbolbezeichnung);
    }

    @Override
    public String toString() {
        return "Symbolbezeichnung [symbolbezeichnung=" + symbolbezeichnung + "]";
    }
}
