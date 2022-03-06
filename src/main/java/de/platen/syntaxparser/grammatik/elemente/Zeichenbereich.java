package de.platen.syntaxparser.grammatik.elemente;

import java.util.Objects;

import de.platen.syntaxparser.grammatik.GrammatikException;

public class Zeichenbereich
{

    private final Character von;
    private final Character bis;

    public Zeichenbereich(final Character von, final Character bis) {
        check(von);
        check(bis);
        if (von.charValue() > bis.charValue()) {
            throw new GrammatikException();
        }
        this.von = von;
        this.bis = bis;
    }

    public Character getVon() {
        return von;
    }

    public Character getBis() {
        return bis;
    }

    @Override
    public int hashCode() {
        return Objects.hash(bis, von);
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if ((obj == null) || (getClass() != obj.getClass())) {
            return false;
        }
        final Zeichenbereich other = (Zeichenbereich) obj;
        return Objects.equals(bis, other.bis) && Objects.equals(von, other.von);
    }

    private void check(final Character character) {
        if ((character == null)) {
            throw new GrammatikException();
        }
    }
}
