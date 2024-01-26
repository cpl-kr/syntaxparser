package de.platen.syntaxparser;

import de.platen.syntaxparser.syntaxpfad.Syntaxpfad;
import de.platen.syntaxparser.syntaxpfad.Syntaxpfadbehandlung;
import de.platen.syntaxparser.syntaxpfad.Syntaxpfadersteller;
import de.platen.syntaxparser.syntaxpfad.Syntaxpfadfolge;
import de.platen.syntaxparser.zeichenverarbeitung.Verarbeitungsstand;

import java.util.HashSet;
import java.util.Set;

public class ParserInitialisierung {

    private final Syntaxpfadersteller syntaxpfadersteller;
    private final Syntaxpfadbehandlung syntaxpfadbehandlung;

    public ParserInitialisierung(final Syntaxpfadersteller syntaxpfadersteller, final Syntaxpfadbehandlung syntaxpfadbehandlung) {
        if ((syntaxpfadersteller == null) || (syntaxpfadbehandlung == null))  {
            throw new SyntaxparserException();
        }
        this.syntaxpfadersteller = syntaxpfadersteller;
        this.syntaxpfadbehandlung = syntaxpfadbehandlung;
    }

    public Set<Verarbeitungsstand> initialisiereVerarbeitungsstaende() {
        final Set<Verarbeitungsstand> verarbeitungsstaende = new HashSet<>();
        final Set<Syntaxpfad> syntaxpfade = this.syntaxpfadersteller.ermittleSyntaxpfadeVonStartSymbol();
        for (final Syntaxpfad syntaxpfad : syntaxpfade) {
            final Syntaxpfadfolge syntaxpfadfolge = new Syntaxpfadfolge();
            syntaxpfadfolge.setzeAktuellenSyntaxpfad(syntaxpfad);
            verarbeitungsstaende.add(new Verarbeitungsstand(syntaxpfadfolge, this.syntaxpfadbehandlung.ermittleVerarbeitung(syntaxpfad.gebeBlatt())));
        }
        return verarbeitungsstaende;
    }
}
