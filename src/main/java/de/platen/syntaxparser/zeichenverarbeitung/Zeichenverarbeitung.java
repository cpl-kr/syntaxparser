package de.platen.syntaxparser.zeichenverarbeitung;

import de.platen.syntaxparser.SyntaxparserException;
import de.platen.syntaxparser.regelverarbeitung.Verarbeitung;

import java.util.HashSet;
import java.util.Set;

public class Zeichenverarbeitung {

    private final Wortabschluss wortabschluss;

    public Zeichenverarbeitung(final Wortabschluss wortabschluss) {
        if (wortabschluss == null)  {
            throw new SyntaxparserException();
        }
        this.wortabschluss = wortabschluss;
    }

    public boolean verarbeiteZeichen(final Character c, final Set<Verarbeitungsstand> verarbeitungsstaende) {
        if ((c == null) || (verarbeitungsstaende == null)) {
            throw new SyntaxparserException();
        }
        boolean konnteVerarbeitetWerden = false;
        final Set<Verarbeitungsstand> verarbeitungsstaendeZumBehalten = new HashSet<>();
        final Set<Verarbeitungsstand> verarbeitungsstaendeZumHinzufuegen = new HashSet<>();
        for (final Verarbeitungsstand verarbeitungsstand : verarbeitungsstaende) {
            boolean istVerarbeitet = false;
            final Verarbeitung verarbeitung = verarbeitungsstand.gebeVerarbeitung();
            if (verarbeitung != null) {
                if (verarbeitung.verarbeiteZeichen(c)) {
                    konnteVerarbeitetWerden = true;
                    istVerarbeitet = true;
                } else {
                    if (verarbeitung.istZustandErreicht()) {
                        if (behandleZeichenNichtVerarbeitet(c, verarbeitungsstand, verarbeitungsstaendeZumHinzufuegen)) {
                            konnteVerarbeitetWerden = true;
                            istVerarbeitet = true;
                        }
                    }
                }
            }
            if (istVerarbeitet) {
                verarbeitungsstaendeZumBehalten.add(verarbeitungsstand);
            }
        }
        verarbeitungsstaende.clear();
        verarbeitungsstaende.addAll(verarbeitungsstaendeZumBehalten);
        verarbeitungsstaende.addAll(verarbeitungsstaendeZumHinzufuegen);
        return konnteVerarbeitetWerden;
    }

    private boolean behandleZeichenNichtVerarbeitet(final Character c, final Verarbeitungsstand verarbeitungsstand, final Set<Verarbeitungsstand> verarbeitungsstaendeZumHinzufuegen) {
        boolean konnteVerarbeitetWerden = false;
        final Set<Verarbeitungsstand> verarbeitungsstaende = this.wortabschluss.schliesseWortAb(verarbeitungsstand);
        Verarbeitung verarbeitung = verarbeitungsstand.gebeVerarbeitung();
        if (verarbeitung != null) {
            if (verarbeitung.verarbeiteZeichen(c)) {
                konnteVerarbeitetWerden = true;
            }
        }
        for (Verarbeitungsstand verarbeitungsstandNeu : verarbeitungsstaende) {
            if (verarbeitungsstandNeu.gebeVerarbeitung().verarbeiteZeichen(c)) {
                konnteVerarbeitetWerden = true;
                verarbeitungsstaendeZumHinzufuegen.add(verarbeitungsstandNeu);
            }
        }
        return konnteVerarbeitetWerden;
    }
}
