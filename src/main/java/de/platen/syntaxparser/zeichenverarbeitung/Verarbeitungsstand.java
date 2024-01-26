package de.platen.syntaxparser.zeichenverarbeitung;

import de.platen.syntaxparser.SyntaxparserException;
import de.platen.syntaxparser.syntaxpfad.Syntaxpfadfolge;
import de.platen.syntaxparser.regelverarbeitung.Verarbeitung;

import java.util.Objects;

public class Verarbeitungsstand {

    private final Syntaxpfadfolge syntaxpfadfolge;
    private Verarbeitung verarbeitung;

    public Verarbeitungsstand(final Syntaxpfadfolge syntaxpfadfolge, final Verarbeitung verarbeitung) {
        if ((syntaxpfadfolge == null) || (verarbeitung == null)) {
            throw new SyntaxparserException();
        }
        this.syntaxpfadfolge = syntaxpfadfolge;
        this.verarbeitung = verarbeitung;
    }

    public void setzeVerarbeitung(final Verarbeitung verarbeitung) { this.verarbeitung = verarbeitung; }

    public Syntaxpfadfolge gebeSyntaxpfadfolge() { return this.syntaxpfadfolge; }

    public Verarbeitung gebeVerarbeitung() { return this.verarbeitung; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Verarbeitungsstand that = (Verarbeitungsstand) o;
        return syntaxpfadfolge.equals(that.syntaxpfadfolge) && Objects.equals(verarbeitung, that.verarbeitung);
    }

    @Override
    public int hashCode() {
        return Objects.hash(syntaxpfadfolge, verarbeitung);
    }
}
