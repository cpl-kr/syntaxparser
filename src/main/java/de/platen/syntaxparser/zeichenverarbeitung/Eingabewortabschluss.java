package de.platen.syntaxparser.zeichenverarbeitung;

import de.platen.syntaxparser.SyntaxparserException;
import de.platen.syntaxparser.regelverarbeitung.Verarbeitung;

import java.util.HashSet;
import java.util.Set;

public class Eingabewortabschluss {

    private final Wortabschluss wortabschluss;

    public Eingabewortabschluss(final Wortabschluss wortabschluss) {
        if (wortabschluss == null) {
            throw new SyntaxparserException();
        }
        this.wortabschluss = wortabschluss;
    }

    public boolean schliesseEingabewortAb(final Set<Verarbeitungsstand> verarbeitungsstaende) {
        if (verarbeitungsstaende == null) {
            throw new SyntaxparserException();
        }
        entferneElementeZustandNichtErreicht(verarbeitungsstaende);
        if (!schliesseWorteAb(verarbeitungsstaende)) {
            return false;
        }
        entferneKuerzere(verarbeitungsstaende);
        if (verarbeitungsstaende.isEmpty()) {
            return false;
        }
        return true;
    }

    private static void entferneElementeZustandNichtErreicht(final Set<Verarbeitungsstand> verarbeitungsstaende) {
        final Set<Verarbeitungsstand> verarbeitungsstandsfolgenZumBehalten = new HashSet<>();
        for (Verarbeitungsstand verarbeitungsstand : verarbeitungsstaende) {
            Verarbeitung verarbeitung = verarbeitungsstand.gebeVerarbeitung();
            if (!((verarbeitung != null) && !verarbeitung.istZustandErreicht() && !verarbeitung.istStartzustand())) {
                verarbeitungsstandsfolgenZumBehalten.add(verarbeitungsstand);
            }
        }
        verarbeitungsstaende.clear();
        verarbeitungsstaende.addAll(verarbeitungsstandsfolgenZumBehalten);
    }

    private boolean schliesseWorteAb(final Set<Verarbeitungsstand> verarbeitungsstaende) {
        boolean istAbschlussErfolgt = false;
        final Set<Verarbeitungsstand> verarbeitungsstaendeZumHinzufuegen = new HashSet<>();
        final Set<Verarbeitungsstand> verarbeitungsstaendeZumBehalten = new HashSet<>();
        for (Verarbeitungsstand verarbeitungsstand : verarbeitungsstaende) {
            Verarbeitung verarbeitung = verarbeitungsstand.gebeVerarbeitung();
            if (verarbeitung != null) {
                if (!verarbeitung.istStartzustand()) {
                    final Set<Verarbeitungsstand> verarbeitungsstaendeNeu = this.wortabschluss.schliesseWortAb(verarbeitungsstand);
                    verarbeitungsstaendeZumHinzufuegen.addAll(verarbeitungsstaendeNeu);
                }
            }
            istAbschlussErfolgt = true;
            verarbeitungsstaendeZumBehalten.add(verarbeitungsstand);
        }
        if (verarbeitungsstaendeZumBehalten.size() > 0) {
            verarbeitungsstaende.clear();
            verarbeitungsstaende.addAll(verarbeitungsstaendeZumBehalten);
        }
        verarbeitungsstaende.addAll(verarbeitungsstaendeZumHinzufuegen);
        return istAbschlussErfolgt;
    }

    private static void entferneKuerzere(final Set<Verarbeitungsstand> verarbeitungsstaende)  {
        int maximum = 0;
        for (final Verarbeitungsstand verarbeitungsstand : verarbeitungsstaende) {
            if (verarbeitungsstand.gebeSyntaxpfadfolge().getSyntaxpfadeMitWort().size() > maximum) {
                maximum = verarbeitungsstand.gebeSyntaxpfadfolge().getSyntaxpfadeMitWort().size();
            }
        }
        final Set<Verarbeitungsstand> verarbeitungsstaendeZumBehalten = new HashSet<>();
        for (Verarbeitungsstand verarbeitungsstand : verarbeitungsstaende) {
            if (!(verarbeitungsstand.gebeSyntaxpfadfolge().getSyntaxpfadeMitWort().size() < maximum)) {
                verarbeitungsstaendeZumBehalten.add(verarbeitungsstand);
            }
        }
        if (verarbeitungsstaendeZumBehalten.size() > 0) {
            verarbeitungsstaende.clear();
            verarbeitungsstaende.addAll(verarbeitungsstaendeZumBehalten);
        }
    }
}
