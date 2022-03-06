package de.platen.syntaxparser.syntaxpfad;

import java.util.Objects;

import de.platen.syntaxparser.SyntaxparserException;

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
        final StringBuilder stringBuilder = new StringBuilder();
        final String leerzeichen = "    ";
        stringBuilder.append("SyntaxpfadMitWort\n");
        stringBuilder.append(leerzeichen);
        stringBuilder.append(syntaxpfad.toString());
        stringBuilder.append("\n");
        stringBuilder.append(leerzeichen);
        stringBuilder.append("Wort\n");
        stringBuilder.append(leerzeichen);
        stringBuilder.append(wort);
        stringBuilder.append("\n");
        return stringBuilder.toString();
    }
}
