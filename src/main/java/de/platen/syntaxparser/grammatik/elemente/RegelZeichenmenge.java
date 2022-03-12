package de.platen.syntaxparser.grammatik.elemente;

import java.io.Serializable;
import java.util.Objects;

import de.platen.syntaxparser.grammatik.GrammatikException;

public class RegelZeichenmenge extends Regel implements Serializable
{

    private static final long serialVersionUID = 1L;

    private final Zeichenmenge zeichenmenge;

    public RegelZeichenmenge(final Symbolbezeichnung symbol, final Zeichenmenge zeichenmenge) {
        super(symbol);
        if (zeichenmenge == null) {
            throw new GrammatikException();
        }
        this.zeichenmenge = zeichenmenge;
    }

    public Zeichenmenge getZeichenmenge() {
        return zeichenmenge;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        final int result = super.hashCode();
        return (prime * result) + Objects.hash(zeichenmenge);
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (!super.equals(obj) || (getClass() != obj.getClass())) {
            return false;
        }
        final RegelZeichenmenge other = (RegelZeichenmenge) obj;
        return Objects.equals(zeichenmenge, other.zeichenmenge);
    }
}
