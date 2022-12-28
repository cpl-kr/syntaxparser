package de.platen.syntaxparser.zeichenverarbeitung;

import de.platen.syntaxparser.SyntaxparserException;
import de.platen.syntaxparser.regelverarbeitung.Verarbeitung;
import de.platen.syntaxparser.syntaxpfad.Syntaxpfad;
import de.platen.syntaxparser.syntaxpfad.Syntaxpfadbehandlung;
import de.platen.syntaxparser.syntaxpfad.Syntaxpfadersteller;
import de.platen.syntaxparser.syntaxpfad.Syntaxpfadfolge;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Wortabschluss {

    private final Syntaxpfadersteller syntaxpfadersteller;
    private final Syntaxpfadbehandlung syntaxpfadbehandlung;

    public Wortabschluss(final Syntaxpfadersteller syntaxpfadersteller, final Syntaxpfadbehandlung syntaxpfadbehandlung) {
        if ((syntaxpfadersteller == null) || (syntaxpfadbehandlung == null))  {
            throw new SyntaxparserException();
        }
        this.syntaxpfadersteller = syntaxpfadersteller;
        this.syntaxpfadbehandlung = syntaxpfadbehandlung;
    }

    public Set<Verarbeitungsstand> schliesseWortAb(final Verarbeitungsstand verarbeitungsstand) {
        if (verarbeitungsstand == null) {
            throw new SyntaxparserException();
        }
        final Set<Verarbeitungsstand> verarbeitungsstaende = new HashSet<>();
        final Verarbeitung verarbeitung = verarbeitungsstand.gebeVerarbeitung();
        if ((verarbeitung != null) && verarbeitung.istZustandErreicht() && (verarbeitungsstand.gebeSyntaxpfadfolge().getAktuell() != null)) {
            final Syntaxpfad syntaxpfadNaechstesSymbol = this.syntaxpfadbehandlung.findeNaechstesSymbol(verarbeitungsstand.gebeSyntaxpfadfolge().getAktuell());
            verarbeitungsstand.gebeSyntaxpfadfolge().uebernehmeAktuellenSyntaxpfad(verarbeitungsstand.gebeVerarbeitung().gebeWort());
            verarbeitungsstand.setzeVerarbeitung(null);
            if (syntaxpfadNaechstesSymbol.istFertig()) {
                final Verarbeitung verarbeitungNeu = this.syntaxpfadbehandlung.ermittleVerarbeitung(syntaxpfadNaechstesSymbol.gebeBlatt());
                verarbeitungsstand.gebeSyntaxpfadfolge().setzeAktuellenSyntaxpfad(syntaxpfadNaechstesSymbol);
                verarbeitungsstand.setzeVerarbeitung(verarbeitungNeu);
            } else {
                final List<Syntaxpfadfolge> syntaxpfadfolgen = new ArrayList<>(this.syntaxpfadersteller.behandleNaechstenKnoten(verarbeitungsstand.gebeSyntaxpfadfolge().getSyntaxpfadeMitWort(), syntaxpfadNaechstesSymbol));
                for (Syntaxpfadfolge syntaxpfadfolge : syntaxpfadfolgen) {
                    Verarbeitung verarbeitungNeu = this.syntaxpfadbehandlung.ermittleVerarbeitung(syntaxpfadfolge.getAktuell().gebeBlatt());
                    Verarbeitungsstand verarbeitungsstandNeu = new Verarbeitungsstand(syntaxpfadfolge, verarbeitungNeu);
                    verarbeitungsstaende.add(verarbeitungsstandNeu);
                }
            }
        }
        return verarbeitungsstaende;
    }
}
