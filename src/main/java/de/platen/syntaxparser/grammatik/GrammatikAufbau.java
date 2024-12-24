package de.platen.syntaxparser.grammatik;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.platen.syntaxparser.grammatik.elemente.Kardinalitaet;
import de.platen.syntaxparser.grammatik.elemente.RegEx;
import de.platen.syntaxparser.grammatik.elemente.RegelRegEx;
import de.platen.syntaxparser.grammatik.elemente.RegelSymbole;
import de.platen.syntaxparser.grammatik.elemente.RegelZeichenbereich;
import de.platen.syntaxparser.grammatik.elemente.RegelZeichenfolge;
import de.platen.syntaxparser.grammatik.elemente.RegelZeichenmenge;
import de.platen.syntaxparser.grammatik.elemente.Symbol;
import de.platen.syntaxparser.grammatik.elemente.Symbolbezeichnung;
import de.platen.syntaxparser.grammatik.elemente.Zeichenbereich;
import de.platen.syntaxparser.grammatik.elemente.Zeichenfolge;
import de.platen.syntaxparser.grammatik.elemente.Zeichenmenge;

public class GrammatikAufbau extends Grammatik
{

    private static final long serialVersionUID = 1L;

    public void setStartregel(final RegelSymbole regelSymbole) {
        if ((startregel != null) || (regelSymbole == null)) {
            throw new GrammatikException();
        }
        startregel = regelSymbole;
    }

    public void addRegelSymbole(final RegelSymbole regelSymbole) {
        if ((regelSymbole == null) || regelSymbole.getSymbolbezeichnung().equals(startregel.getSymbolbezeichnung())
                || regExregeln.containsKey(regelSymbole.getSymbolbezeichnung())
                || zeichenbereichregeln.containsKey(regelSymbole.getSymbolbezeichnung())
                || zeichenfolgeregeln.containsKey(regelSymbole.getSymbolbezeichnung())
                || zeichenmengeregeln.containsKey(regelSymbole.getSymbolbezeichnung())) {
            throw new GrammatikException();
        }
        if (symbolregeln.containsKey(regelSymbole.getSymbolbezeichnung())) {
            symbolregeln.get(regelSymbole.getSymbolbezeichnung()).add(regelSymbole.getSymbole());
        } else {
            final List<List<Symbol>> symbole = new ArrayList<>();
            symbole.add(regelSymbole.getSymbole());
            symbolregeln.put(regelSymbole.getSymbolbezeichnung(), symbole);
        }
    }

    public void addRegelZeichenbereich(final RegelZeichenbereich regelZeichenbereich) {
        if ((regelZeichenbereich == null)
                || regelZeichenbereich.getSymbolbezeichnung().equals(startregel.getSymbolbezeichnung())
                || regExregeln.containsKey(regelZeichenbereich.getSymbolbezeichnung())
                || symbolregeln.containsKey(regelZeichenbereich.getSymbolbezeichnung())
                || zeichenfolgeregeln.containsKey(regelZeichenbereich.getSymbolbezeichnung())
                || zeichenmengeregeln.containsKey(regelZeichenbereich.getSymbolbezeichnung())) {
            throw new GrammatikException();
        }
        if (zeichenbereichregeln.containsKey(regelZeichenbereich.getSymbolbezeichnung())) {
            zeichenbereichregeln.get(regelZeichenbereich.getSymbolbezeichnung())
                    .add(regelZeichenbereich.getZeichenbereich());
        } else {
            final Set<Zeichenbereich> zeichenbereiche = new HashSet<>();
            zeichenbereiche.add(regelZeichenbereich.getZeichenbereich());
            zeichenbereichregeln.put(regelZeichenbereich.getSymbolbezeichnung(), zeichenbereiche);
        }
    }

    public void addRegelZeichenfolge(final RegelZeichenfolge regelZeichenfolge) {
        if ((regelZeichenfolge == null)
                || regelZeichenfolge.getSymbolbezeichnung().equals(startregel.getSymbolbezeichnung())
                || regExregeln.containsKey(regelZeichenfolge.getSymbolbezeichnung())
                || symbolregeln.containsKey(regelZeichenfolge.getSymbolbezeichnung())
                || zeichenbereichregeln.containsKey(regelZeichenfolge.getSymbolbezeichnung())
                || zeichenmengeregeln.containsKey(regelZeichenfolge.getSymbolbezeichnung())) {
            throw new GrammatikException();
        }
        if (zeichenfolgeregeln.containsKey(regelZeichenfolge.getSymbolbezeichnung())) {
            zeichenfolgeregeln.get(regelZeichenfolge.getSymbolbezeichnung()).add(regelZeichenfolge.getZeichenfolge());
        } else {
            final Set<Zeichenfolge> zeichenfolgen = new HashSet<>();
            zeichenfolgen.add(regelZeichenfolge.getZeichenfolge());
            zeichenfolgeregeln.put(regelZeichenfolge.getSymbolbezeichnung(), zeichenfolgen);
        }
    }

    public void addRegelZeichenmenge(final RegelZeichenmenge regelZeichenmenge) {
        if ((regelZeichenmenge == null)
                || regelZeichenmenge.getSymbolbezeichnung().equals(startregel.getSymbolbezeichnung())
                || regExregeln.containsKey(regelZeichenmenge.getSymbolbezeichnung())
                || symbolregeln.containsKey(regelZeichenmenge.getSymbolbezeichnung())
                || zeichenbereichregeln.containsKey(regelZeichenmenge.getSymbolbezeichnung())
                || zeichenfolgeregeln.containsKey(regelZeichenmenge.getSymbolbezeichnung())) {
            throw new GrammatikException();
        }
        if (zeichenmengeregeln.containsKey(regelZeichenmenge.getSymbolbezeichnung())) {
            zeichenmengeregeln.get(regelZeichenmenge.getSymbolbezeichnung()).add(regelZeichenmenge.getZeichenmenge());
        } else {
            final Set<Zeichenmenge> zeichemengen = new HashSet<>();
            zeichemengen.add(regelZeichenmenge.getZeichenmenge());
            zeichenmengeregeln.put(regelZeichenmenge.getSymbolbezeichnung(), zeichemengen);
        }
    }

    public void addRegelRegEx(final RegelRegEx regelRegEx) {
        if ((regelRegEx == null) || regelRegEx.getSymbolbezeichnung().equals(startregel.getSymbolbezeichnung())
                || symbolregeln.containsKey(regelRegEx.getSymbolbezeichnung())
                || zeichenmengeregeln.containsKey(regelRegEx.getSymbolbezeichnung())
                || zeichenbereichregeln.containsKey(regelRegEx.getSymbolbezeichnung())
                || zeichenfolgeregeln.containsKey(regelRegEx.getSymbolbezeichnung())) {
            throw new GrammatikException();
        }
        if (regExregeln.containsKey(regelRegEx.getSymbolbezeichnung())) {
            regExregeln.get(regelRegEx.getSymbolbezeichnung()).add(regelRegEx.getRegEx());
        } else {
            final Set<RegEx> regEx = new HashSet<>();
            regEx.add(regelRegEx.getRegEx());
            regExregeln.put(regelRegEx.getSymbolbezeichnung(), regEx);
        }
    }

    public void checkGrammatik() {
        if (startregel.getSymbole().get(0).getSymbolkennung().getSymbolbezeichnung()
                .equals(startregel.getSymbolbezeichnung())) {
            throw new GrammatikException();
        }
        for (final Symbolbezeichnung symbolbezeichnung : symbolregeln.keySet()) {
            for (final List<Symbol> symbole : symbolregeln.get(symbolbezeichnung)) {
                if (symbole.get(0).getSymbolkennung().getSymbolbezeichnung().equals(symbolbezeichnung)) {
                    throw new GrammatikException();
                }
            }
        }
        for (final Symbol symbol : startregel.getSymbole()) {
            if (!symbolregeln.containsKey(symbol.getSymbolkennung().getSymbolbezeichnung()) && //
                    !regExregeln.containsKey(symbol.getSymbolkennung().getSymbolbezeichnung()) && //
                    !zeichenbereichregeln.containsKey(symbol.getSymbolkennung().getSymbolbezeichnung()) && //
                    !zeichenfolgeregeln.containsKey(symbol.getSymbolkennung().getSymbolbezeichnung()) && //
                    !zeichenmengeregeln.containsKey(symbol.getSymbolkennung().getSymbolbezeichnung())) {
                throw new GrammatikException();
            }
        }
        for (final Symbolbezeichnung symbolbezeichnung : symbolregeln.keySet()) {
            final List<List<Symbol>> symbole = symbolregeln.get(symbolbezeichnung);
            for (final List<Symbol> symbolliste : symbole) {
                for (final Symbol symbol : symbolliste) {
                    if (!symbolregeln.containsKey(symbol.getSymbolkennung().getSymbolbezeichnung()) && //
                            !regExregeln.containsKey(symbol.getSymbolkennung().getSymbolbezeichnung()) && //
                            !zeichenbereichregeln.containsKey(symbol.getSymbolkennung().getSymbolbezeichnung()) && //
                            !zeichenfolgeregeln.containsKey(symbol.getSymbolkennung().getSymbolbezeichnung()) && //
                            !zeichenmengeregeln.containsKey(symbol.getSymbolkennung().getSymbolbezeichnung())) {
                        throw new GrammatikException();
                    }
                }
            }
        }
        checkZweiGleicheSymboleKardinalitaet(startregel.getSymbole());
        for (final Symbolbezeichnung symbolbezeichnung : symbolregeln.keySet()) {
            final List<List<Symbol>> symbole = symbolregeln.get(symbolbezeichnung);
            for (final List<Symbol> symbolliste : symbole) {
                checkZweiGleicheSymboleKardinalitaet(symbolliste);
            }
        }
        // TODO Kaedinaltätsprüfung für zwei gleiche Symbole, wobei das Folgesymbol indirekt durch erstes selbes Symbol in der Symbolregel enthalten ist
    }

    public void checkGrammatikStrikt() {
        checkGrammatik();
        for (final Symbolbezeichnung symbolbezeichnung : symbolregeln.keySet()) {
            final List<List<Symbol>> symbole = symbolregeln.get(symbolbezeichnung);
            if (symbole.size() != 1) {
                throw new GrammatikException();
            }
            for (List<Symbol> symbolListe : symbole) {
                for (Symbol symbol : symbolListe ) {
                    if (symbol.getSymbolkennung().getSymbolbezeichnung().equals(symbolbezeichnung)) {
                        throw new GrammatikException();
                    }
                }
            }
        }
    }

    private void checkZweiGleicheSymboleKardinalitaet(final List<Symbol> symbole) {
        if (symbole.size() > 1) {
            for (int index = 0; index < symbole.size(); index++) {
                if ((index + 1) < symbole.size()) {
                    final String symbolbezeichnung1 = symbole.get(index).getSymbolkennung().getSymbolbezeichnung().getSymbolbezeichnung();
                    final String symbolbezeichnung2 = symbole.get(index + 1).getSymbolkennung().getSymbolbezeichnung().getSymbolbezeichnung();
                    final Kardinalitaet kardinalitaet1 = symbole.get(index).getKardinalitaet();
                    final Kardinalitaet kardinalitaet2 = symbole.get(index + 1).getKardinalitaet();
                    if (symbolbezeichnung1.equals(symbolbezeichnung2) && kardinalitaet1.equals(Kardinalitaet.MINDESTENS_EINMAL)) {
                        throw new GrammatikException();
                    }
                }
            }
        }
    }
}
