package de.platen.syntaxparser.parser.syntaxpfad;

import java.util.Objects;

import de.platen.syntaxparser.parser.SyntaxparserException;

public class SyntaxpfadMitWort
{

    private final Syntaxpfad syntaxpfad;
    private final String wort;

    public SyntaxpfadMitWort(final Syntaxpfad syntaxpfad, final String wort) {
        if ((syntaxpfad == null) || (wort == null) || wort.isBlank() || !syntaxpfad.istFertig()) {
            throw new SyntaxparserException();
        }
        this.syntaxpfad = syntaxpfad;
        this.wort = wort;
    }

    public Syntaxpfad getSyntaxpfad() {
        return syntaxpfad;
    }

    public String getWort() {
        return wort;
    }

    @Override
    public int hashCode() {
        return Objects.hash(syntaxpfad, wort);
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if ((obj == null) || (getClass() != obj.getClass())) {
            return false;
        }
        final SyntaxpfadMitWort other = (SyntaxpfadMitWort) obj;
        return Objects.equals(syntaxpfad, other.syntaxpfad) && Objects.equals(wort, other.wort);
    }

    @Override
    public String toString() {
        return "SyntaxpfadMitWort: [" + syntaxpfad + ", " + "Wort=" + "'" + wort + "'" + "]";
    }
}
