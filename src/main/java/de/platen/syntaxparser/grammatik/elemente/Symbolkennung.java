package de.platen.syntaxparser.grammatik.elemente;

import java.io.Serializable;
import java.util.Objects;

import de.platen.syntaxparser.grammatik.GrammatikException;

public class Symbolkennung implements Serializable
{

    private static final long serialVersionUID = 1L;

    private final Symbolbezeichnung symbolbezeichnung;
    private final Symbolidentifizierung symbolidentifizierung;

    public Symbolkennung(final Symbolbezeichnung symbolbezeichnung, final Symbolidentifizierung symbolidentifizierung) {
        if ((symbolbezeichnung == null) || (symbolidentifizierung == null)) {
            throw new GrammatikException();
        }
        this.symbolbezeichnung = symbolbezeichnung;
        this.symbolidentifizierung = symbolidentifizierung;
    }

    public Symbolbezeichnung getSymbolbezeichnung() {
        return symbolbezeichnung;
    }

    public Symbolidentifizierung getSymbolidentifizierung() {
        return symbolidentifizierung;
    }

    @Override
    public int hashCode() {
        return Objects.hash(symbolbezeichnung, symbolidentifizierung);
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if ((obj == null) || (getClass() != obj.getClass())) {
            return false;
        }
        final Symbolkennung other = (Symbolkennung) obj;
        return Objects.equals(symbolbezeichnung, other.symbolbezeichnung)
                && Objects.equals(symbolidentifizierung, other.symbolidentifizierung);
    }

    @Override
    public String toString() {
        return "Symbolkennung: [" + symbolbezeichnung + ", " + symbolidentifizierung + "]";
    }
}
