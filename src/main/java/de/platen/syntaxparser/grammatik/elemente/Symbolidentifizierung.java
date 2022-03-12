package de.platen.syntaxparser.grammatik.elemente;

import java.io.Serializable;
import java.util.Objects;

import de.platen.syntaxparser.grammatik.GrammatikException;

public class Symbolidentifizierung implements Serializable
{

    private static final long serialVersionUID = 1L;

    private final Integer symbolidentifizierung;

    public Symbolidentifizierung(final Integer symbolidentifizierung) {
        if ((symbolidentifizierung == null) || (symbolidentifizierung < 0)) {
            throw new GrammatikException();
        }
        this.symbolidentifizierung = symbolidentifizierung;
    }

    public Integer getSymbolidentifizierung() {
        return symbolidentifizierung;
    }

    @Override
    public int hashCode() {
        return Objects.hash(symbolidentifizierung);
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if ((obj == null) || (getClass() != obj.getClass())) {
            return false;
        }
        final Symbolidentifizierung other = (Symbolidentifizierung) obj;
        return Objects.equals(symbolidentifizierung, other.symbolidentifizierung);
    }

    @Override
    public String toString() {
        return "Symbolidentifizierung [symbolidentifizierung=" + symbolidentifizierung + "]";
    }
}
