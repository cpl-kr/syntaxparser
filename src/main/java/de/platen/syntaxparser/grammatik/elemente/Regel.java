package de.platen.syntaxparser.grammatik.elemente;

import java.util.Objects;

import de.platen.syntaxparser.grammatik.GrammatikException;

public abstract class Regel
{

    private final Symbolbezeichnung symbolbezeichnung;

    public Regel(final Symbolbezeichnung symbolbezeichnung) {
        if (symbolbezeichnung == null) {
            throw new GrammatikException();
        }
        this.symbolbezeichnung = symbolbezeichnung;
    }

    public Symbolbezeichnung getSymbolbezeichnung() {
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
        final Regel other = (Regel) obj;
        return Objects.equals(symbolbezeichnung, other.symbolbezeichnung);
    }
}
