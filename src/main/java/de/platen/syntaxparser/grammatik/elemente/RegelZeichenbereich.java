package de.platen.syntaxparser.grammatik.elemente;

import java.util.Objects;

import de.platen.syntaxparser.grammatik.GrammatikException;

public class RegelZeichenbereich extends Regel
{

    private final Zeichenbereich zeichenbereich;

    public RegelZeichenbereich(final Symbolbezeichnung symbol, final Zeichenbereich zeichenbereich) {
        super(symbol);
        if (zeichenbereich == null) {
            throw new GrammatikException();
        }
        this.zeichenbereich = zeichenbereich;
    }

    public Zeichenbereich getZeichenbereich() {
        return zeichenbereich;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        return (prime * result) + Objects.hash(zeichenbereich);
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (!super.equals(obj) || (getClass() != obj.getClass())) {
            return false;
        }
        final RegelZeichenbereich other = (RegelZeichenbereich) obj;
        return Objects.equals(zeichenbereich, other.zeichenbereich);
    }
}
