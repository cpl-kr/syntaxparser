package de.platen.syntaxparser.parser.syntaxpfad;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import de.platen.syntaxparser.parser.SyntaxparserException;

public class Syntaxpfadfolge {

    private final List<SyntaxpfadMitWort> syntaxpfadeMitWort = new ArrayList<>();
    private Syntaxpfad aktuell = null;
    private int gesamtlaenge = 0;

    public Syntaxpfadfolge() {
    }

    public Syntaxpfadfolge(final List<SyntaxpfadMitWort> syntaxpfadeMitWort) {
        if (syntaxpfadeMitWort == null) {
            throw new SyntaxparserException();
        }
        this.syntaxpfadeMitWort.addAll(syntaxpfadeMitWort);
        for (final SyntaxpfadMitWort syntaxpfadMitWort : this.syntaxpfadeMitWort) {
            this.gesamtlaenge += syntaxpfadMitWort.getWort().length();
        }
    }

    public void setzeAktuellenSyntaxpfad(final Syntaxpfad syntaxpfad) {
        if ((syntaxpfad == null) || (this.aktuell != null) || !syntaxpfad.istFertig()) {
            throw new SyntaxparserException();
        }
        this.aktuell = syntaxpfad.kopiere();
    }

    public void uebernehmeAktuellenSyntaxpfad(final String wort) {
        if ((wort == null) || wort.isBlank() || (this.aktuell == null)) {
            throw new SyntaxparserException();
        }
        this.syntaxpfadeMitWort.add(new SyntaxpfadMitWort(this.aktuell.kopiere(), wort));
        this.gesamtlaenge += wort.length();
        this.aktuell = null;
    }

    public List<SyntaxpfadMitWort> getSyntaxpfadeMitWort() {
        return this.syntaxpfadeMitWort;
    }

    public Syntaxpfad getAktuell() {
        return this.aktuell;
    }

    public int getGesamtlaenge() {
        return this.gesamtlaenge;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Syntaxpfadfolge that = (Syntaxpfadfolge) o;
        return gesamtlaenge == that.gesamtlaenge && Objects.equals(syntaxpfadeMitWort, that.syntaxpfadeMitWort) && Objects.equals(aktuell, that.aktuell);
    }

    @Override
    public int hashCode() {
        return Objects.hash(syntaxpfadeMitWort, aktuell, gesamtlaenge);
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
