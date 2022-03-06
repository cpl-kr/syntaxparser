package de.platen.syntaxparser.syntaxpfad;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.platen.syntaxparser.SyntaxparserException;
import de.platen.syntaxparser.grammatik.Grammatik;
import de.platen.syntaxparser.grammatik.elemente.RegEx;
import de.platen.syntaxparser.grammatik.elemente.Symbolbezeichnung;
import de.platen.syntaxparser.grammatik.elemente.Symbolkennung;
import de.platen.syntaxparser.grammatik.elemente.Zeichenbereich;
import de.platen.syntaxparser.grammatik.elemente.Zeichenfolge;
import de.platen.syntaxparser.grammatik.elemente.Zeichenmenge;

public class Syntaxpfadbehandlung
{

    private final Grammatik grammatik;

    public Syntaxpfadbehandlung(final Grammatik grammatik) {
        if (grammatik == null) {
            throw new SyntaxparserException();
        }
        this.grammatik = grammatik;
    }

    public Set<Syntaxpfad> findePassendeSytaxpfade(final Set<Syntaxpfad> syntaxpfade, final String wort) {
        if ((syntaxpfade == null) || (wort == null)) {
            throw new SyntaxparserException();
        }
        final Set<Syntaxpfad> passendeSyntaxpfade = new HashSet<>();
        for (final Syntaxpfad syntaxpfad : syntaxpfade) {
            final Symbolkennung symbolkennung = syntaxpfad.gebeBlatt();
            boolean istWortGefunden = false;
            if (grammatik.getZeichenfolgeregeln().get().containsKey(symbolkennung.getSymbolbezeichnung())) {
                istWortGefunden = vergleicheZeichenfolgen(symbolkennung.getSymbolbezeichnung(), wort);
            } else {
                if (grammatik.getRegExregeln().get().containsKey(symbolkennung.getSymbolbezeichnung())) {
                    istWortGefunden = vergleicheRegEx(symbolkennung.getSymbolbezeichnung(), wort);
                } else {
                    istWortGefunden = vergleicheZeichen(symbolkennung.getSymbolbezeichnung(), wort);
                }
            }
            if (istWortGefunden) {
                passendeSyntaxpfade.add(syntaxpfad);
            }
        }
        return passendeSyntaxpfade;
    }

    public Syntaxpfad findeNaechstesSymbol(final Syntaxpfad syntaxpfad) {
        if (syntaxpfad == null) {
            throw new SyntaxparserException();
        }
        Symbolkennung symbolkennung = null;
        List<Symbolkennung> knotenfolge = syntaxpfad.gebeKnotenfolge();
        if (syntaxpfad.istFertig()) {
            symbolkennung = syntaxpfad.gebeBlatt();
        } else {
            if (syntaxpfad.gebeKnotenfolge().size() > 0) {
                symbolkennung = syntaxpfad.gebeKnotenfolge().get(syntaxpfad.gebeKnotenfolge().size() - 1);
                knotenfolge = kopiereSymbolkennungenOhneLetztesElement(syntaxpfad.gebeKnotenfolge());
            }
        }
        if (symbolkennung != null) {
            if (!grammatik.getStartregel().istInRegel(symbolkennung)) {
                if (grammatik.getSymbolregeln().istLetztesSymbolInRegel(symbolkennung)) {
                    return findeNaechstesSymbol(erstelleSyntaxpfad(knotenfolge));
                }
                final Symbolkennung symbolkennungNeu = grammatik.getSymbolregeln()
                        .gebeNaechstesSymbolInnerhalbRegel(symbolkennung);
                if (!grammatik.getSymbolregeln().get().containsKey(symbolkennungNeu.getSymbolbezeichnung())) {
                    return erstelleSyntaxpfad(knotenfolge, symbolkennungNeu);
                }
                final List<Symbolkennung> knotenfolgeNeu = kopiereSymbolkennungenMitWeiteremElement(knotenfolge,
                        symbolkennungNeu);
                return erstelleSyntaxpfad(knotenfolgeNeu);
            }
            if (!grammatik.getStartregel().istLetztesSymbol(symbolkennung)) {
                final Symbolkennung symbolkennungStartregel = grammatik.getStartregel()
                        .gebeNaechstesSymbol(symbolkennung);
                if (!grammatik.getSymbolregeln().get().containsKey(symbolkennungStartregel.getSymbolbezeichnung())) {
                    return erstelleSyntaxpfad(knotenfolge, symbolkennungStartregel);
                }
                final List<Symbolkennung> knotenfolgeNeu = kopiereSymbolkennungenMitWeiteremElement(knotenfolge,
                        symbolkennungStartregel);
                return erstelleSyntaxpfad(knotenfolgeNeu);
            }
        }
        return new Syntaxpfad();
    }

    private boolean vergleicheZeichenfolgen(final Symbolbezeichnung symbolbezeichnung, final String wort) {
        for (final Zeichenfolge zeichenfolge : grammatik.getZeichenfolgeregeln().get().get(symbolbezeichnung)) {
            if (wort.equals(zeichenfolge.getZeichenfolge())) {
                return true;
            }
        }
        return false;
    }

    private boolean vergleicheRegEx(final Symbolbezeichnung symbolbezeichnung, final String wort) {
        for (final RegEx regEx : grammatik.getRegExregeln().get().get(symbolbezeichnung)) {
            final Pattern p = Pattern.compile(regEx.getRegex());
            final Matcher m = p.matcher(wort);
            if (m.find()) {
                return true;
            }
        }
        return false;
    }

    private boolean vergleicheZeichen(final Symbolbezeichnung symbolbezeichnung, final String wort) {
        boolean istInZeichenbereich = false;
        boolean istInZeichenmenge = false;
        if (grammatik.getZeichenbereichregeln().get().containsKey(symbolbezeichnung)) {
            istInZeichenbereich = true;
        }
        if (grammatik.getZeichenmengeregeln().get().containsKey(symbolbezeichnung)) {
            istInZeichenmenge = true;
        }
        for (final Character ch : wort.toCharArray()) {
            boolean istZeichenGefunden = false;
            if (istInZeichenbereich) {
                final Set<Zeichenbereich> zeichenbereiche = grammatik.getZeichenbereichregeln().get()
                        .get(symbolbezeichnung);
                for (final Zeichenbereich zeichenbereich : zeichenbereiche) {
                    if ((ch.charValue() >= zeichenbereich.getVon().charValue())
                            && (ch.charValue() <= zeichenbereich.getBis().charValue())) {
                        istZeichenGefunden = true;
                    }
                }
            }
            if (!istZeichenGefunden && istInZeichenmenge) {
                final Set<Zeichenmenge> zeichenmengen = grammatik.getZeichenmengeregeln().get().get(symbolbezeichnung);
                for (final Zeichenmenge zeichenmenge : zeichenmengen) {
                    if (zeichenmenge.getZeichenmenge().contains(ch)) {
                        istZeichenGefunden = true;
                    }
                }
            }
            if (!istZeichenGefunden) {
                return false;
            }
        }
        return true;
    }

    private Syntaxpfad erstelleSyntaxpfad(final List<Symbolkennung> symbolkennungen, final Symbolkennung element) {
        final Syntaxpfad syntaxpfad = new Syntaxpfad();
        for (final Symbolkennung symbolkennung : symbolkennungen) {
            syntaxpfad.zufuegenKnoten(symbolkennung);
        }
        syntaxpfad.zufuegenBlatt(element);
        return syntaxpfad;
    }

    private Syntaxpfad erstelleSyntaxpfad(final List<Symbolkennung> symbolkennungen) {
        final Syntaxpfad syntaxpfad = new Syntaxpfad();
        for (int index = 0; index < symbolkennungen.size(); index++) {
            syntaxpfad.zufuegenKnoten(symbolkennungen.get(index));
        }
        return syntaxpfad;
    }

    private List<Symbolkennung> kopiereSymbolkennungenOhneLetztesElement(final List<Symbolkennung> symbolkennungen) {
        final List<Symbolkennung> symbolkennungenNeu = new ArrayList<>();
        for (int index = 0; index < (symbolkennungen.size() - 1); index++) {
            symbolkennungenNeu.add(symbolkennungen.get(index));
        }
        return symbolkennungenNeu;
    }

    private List<Symbolkennung> kopiereSymbolkennungenMitWeiteremElement(final List<Symbolkennung> symbolkennungen,
            final Symbolkennung symbolkennung) {
        final List<Symbolkennung> symbolkennungenNeu = new ArrayList<>(symbolkennungen);
        symbolkennungenNeu.add(symbolkennung);
        return symbolkennungenNeu;
    }
}
