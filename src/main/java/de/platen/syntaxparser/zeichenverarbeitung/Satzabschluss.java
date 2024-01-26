package de.platen.syntaxparser.zeichenverarbeitung;

import de.platen.syntaxparser.SyntaxparserException;
import de.platen.syntaxparser.syntaxpfad.SyntaxpfadMitWort;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Satzabschluss {

    public List<SyntaxpfadMitWort> schliesseSatzAb(final Set<Verarbeitungsstand> verarbeitungsstaende) {
        if (verarbeitungsstaende == null) {
            throw new SyntaxparserException();
        }
        entferneMehrfachIdentische(verarbeitungsstaende);
        final List<SyntaxpfadMitWort> syntaxpfadeMitWort = new ArrayList<>();
        Verarbeitungsstand verarbeitungsstandZuUntersuchen = null;
        int maximum = 0;
        for (final Verarbeitungsstand verarbeitungsstand : verarbeitungsstaende) {
            if (verarbeitungsstand.gebeSyntaxpfadfolge().getAktuell() == null) {
                if (verarbeitungsstand.gebeSyntaxpfadfolge().getSyntaxpfadeMitWort().size() > maximum) {
                    verarbeitungsstandZuUntersuchen = verarbeitungsstand;
                    maximum = verarbeitungsstand.gebeSyntaxpfadfolge().getSyntaxpfadeMitWort().size();
                }
            }
        }
        boolean istMaximumGefunden = false;
        for (final Verarbeitungsstand verarbeitungsstand : verarbeitungsstaende) {
            if (verarbeitungsstand.gebeSyntaxpfadfolge().getAktuell() == null) {
                if (verarbeitungsstand.gebeSyntaxpfadfolge().getSyntaxpfadeMitWort().size() == maximum) {
                    if (!istMaximumGefunden) {
                        istMaximumGefunden = true;
                    } else {
                        throw new SyntaxparserException();
                    }
                }
            }
        }
        if (verarbeitungsstandZuUntersuchen != null) {
            syntaxpfadeMitWort.addAll(verarbeitungsstandZuUntersuchen.gebeSyntaxpfadfolge().getSyntaxpfadeMitWort());
        }
        return syntaxpfadeMitWort;
    }

    private static void entferneMehrfachIdentische(final Set<Verarbeitungsstand> verarbeitungsstaende) {
        if (verarbeitungsstaende.isEmpty()) {
            return;
        }
        List<Verarbeitungsstand> verarbeitungsstaendeListe = new ArrayList<>(verarbeitungsstaende);
        Set<Integer> indexmenge = new HashSet<>();
        for (int index1 = 0; index1 < verarbeitungsstaendeListe.size(); index1++) {
            for (int index2 = index1 + 1; index2 < verarbeitungsstaendeListe.size(); index2++) {
                if (verarbeitungsstaendeListe.get(index1).gebeSyntaxpfadfolge().getSyntaxpfadeMitWort().equals(verarbeitungsstaendeListe.get(index2).gebeSyntaxpfadfolge().getSyntaxpfadeMitWort())) {
                    indexmenge.add(index2);
                }
            }
        }
        verarbeitungsstaende.clear();
        for (int index = 0; index < verarbeitungsstaendeListe.size(); index++) {
            if (!indexmenge.contains(index)) {
                verarbeitungsstaende.add(verarbeitungsstaendeListe.get(index));
            }
        }
    }
}
