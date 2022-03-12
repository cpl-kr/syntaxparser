package de.platen.syntaxparser.grammatik.elemente;

import java.io.Serializable;
import java.util.Objects;

import de.platen.syntaxparser.grammatik.GrammatikException;

public class RegelRegEx extends Regel implements Serializable
{

    private static final long serialVersionUID = 1L;

    private final RegEx regEx;

    public RegelRegEx(final Symbolbezeichnung symbol, final RegEx regEx) {
        super(symbol);
        if (regEx == null) {
            throw new GrammatikException();
        }
        this.regEx = regEx;
    }

    public RegEx getRegEx() {
        return regEx;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        final int result = super.hashCode();
        return (prime * result) + Objects.hash(regEx);
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (!super.equals(obj) || (getClass() != obj.getClass())) {
            return false;
        }
        final RegelRegEx other = (RegelRegEx) obj;
        return Objects.equals(regEx, other.regEx);
    }
}
