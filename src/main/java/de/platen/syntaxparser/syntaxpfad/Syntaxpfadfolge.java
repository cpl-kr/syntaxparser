package de.platen.syntaxparser.syntaxpfad;

import java.util.ArrayList;
import java.util.List;

import de.platen.syntaxparser.SyntaxparserException;

public class Syntaxpfadfolge
{

    private final List<SyntaxpfadMitWort> syntaxpfadeMitWort = new ArrayList<>();
    private Syntaxpfad aktuell = null;

    public Syntaxpfadfolge() {
    }

    public Syntaxpfadfolge(final List<SyntaxpfadMitWort> syntaxpfadeMitWort) {
        if ((syntaxpfadeMitWort == null) || syntaxpfadeMitWort.isEmpty()) {
            throw new SyntaxparserException();
        }
        this.syntaxpfadeMitWort.addAll(syntaxpfadeMitWort);
    }

    public void setzeAktuellenSyntaxpfad(final Syntaxpfad syntaxpfad) {
        if ((syntaxpfad == null) || (aktuell != null) || !syntaxpfad.istFertig()) {
            throw new SyntaxparserException();
        }
        aktuell = syntaxpfad;
    }

    public void uebernehmeAktuellenSyntaxpfad(final String wort) {
        if ((wort == null) || wort.isBlank() || (aktuell == null)) {
            throw new SyntaxparserException();
        }
        syntaxpfadeMitWort.add(new SyntaxpfadMitWort(aktuell.kopiere(), wort));
        aktuell = null;
    }

    public List<SyntaxpfadMitWort> getSyntaxpfadeMitWort() {
        return syntaxpfadeMitWort;
    }

    public Syntaxpfad getAktuell() {
        return aktuell;
    }

    @Override
    public String toString() {
        final StringBuilder stringBuilder = new StringBuilder();
        final String leerzeichen = "    ";
        stringBuilder.append("Syntaxpfadfolge\n");
        for (final SyntaxpfadMitWort syntaxpfadMitWort : syntaxpfadeMitWort) {
            stringBuilder.append(leerzeichen);
            stringBuilder.append(syntaxpfadMitWort);
            stringBuilder.append("\n");
        }
        stringBuilder.append(leerzeichen);
        stringBuilder.append("Aktuell");
        stringBuilder.append("\n");
        stringBuilder.append(leerzeichen);
        if (aktuell != null) {
            stringBuilder.append(aktuell);
        } else {
            stringBuilder.append("null");
        }
        stringBuilder.append("\n");
        return stringBuilder.toString();
    }
}
