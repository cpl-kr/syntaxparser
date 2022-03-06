package de.platen.syntaxparser.grammatik.elemente;

import java.util.Objects;

import de.platen.syntaxparser.grammatik.GrammatikException;

public class Zeichenfolge
{

    private final String zeichenfolge;

    public Zeichenfolge(final String zeichenfolge) {
        if ((zeichenfolge == null) || zeichenfolge.isBlank() || zeichenfolge.isEmpty()) {
            throw new GrammatikException();
        }
        this.zeichenfolge = zeichenfolge;
    }

    public String getZeichenfolge() {
        return zeichenfolge;
    }

    @Override
    public int hashCode() {
        return Objects.hash(zeichenfolge);
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if ((obj == null) || (getClass() != obj.getClass())) {
            return false;
        }
        final Zeichenfolge other = (Zeichenfolge) obj;
        return Objects.equals(zeichenfolge, other.zeichenfolge);
    }
}
