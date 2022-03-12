package de.platen.syntaxparser.grammatik.elemente;

import java.io.Serializable;
import java.util.Objects;

import de.platen.syntaxparser.grammatik.GrammatikException;

public class RegelZeichenfolge extends Regel implements Serializable
{

    private static final long serialVersionUID = 1L;

    private final Zeichenfolge zeichenfolge;

    public RegelZeichenfolge(final Symbolbezeichnung symbol, final Zeichenfolge zeichenfolge) {
        super(symbol);
        if (zeichenfolge == null) {
            throw new GrammatikException();
        }
        this.zeichenfolge = zeichenfolge;
    }

    public Zeichenfolge getZeichenfolge() {
        return zeichenfolge;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        final int result = super.hashCode();
        return (prime * result) + Objects.hash(zeichenfolge);
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (!super.equals(obj) || (getClass() != obj.getClass())) {
            return false;
        }
        final RegelZeichenfolge other = (RegelZeichenfolge) obj;
        return Objects.equals(zeichenfolge, other.zeichenfolge);
    }
}
