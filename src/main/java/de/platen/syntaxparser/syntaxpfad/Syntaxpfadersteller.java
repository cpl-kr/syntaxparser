package de.platen.syntaxparser.syntaxpfad;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.platen.syntaxparser.SyntaxparserException;
import de.platen.syntaxparser.grammatik.Grammatik;
import de.platen.syntaxparser.grammatik.elemente.Symbol;
import de.platen.syntaxparser.grammatik.elemente.Symbolidentifizierung;
import de.platen.syntaxparser.grammatik.elemente.Symbolkennung;
import de.platen.syntaxparser.grammatik.elemente.Symbolregeln;

public class Syntaxpfadersteller
{

    private final Grammatik grammatik;

    public Syntaxpfadersteller(final Grammatik grammatik) {
        if (grammatik == null) {
            throw new SyntaxparserException();
        }
        this.grammatik = grammatik;
    }

    public Set<Syntaxpfad> ermittleSyntaxpfadeVonStartSymbol() {
        return ermittleSyntaxpfade(new Symbolkennung(grammatik.getStartregel().getSymbolbezeichnung(),
                new Symbolidentifizierung(0)));
    }

    public Set<Syntaxpfad> ermittleSyntaxpfade(final Symbolkennung regel) {
        if (regel == null) {
            throw new SyntaxparserException();
        }
        if (regel.getSymbolbezeichnung().equals(grammatik.getStartregel().getSymbolbezeichnung())) {
            return behandleStartregel(regel);
        }
        if (grammatik.getSymbolregeln().get().containsKey(regel.getSymbolbezeichnung())) {
            return behandleSymbolregeln(regel);
        }
        if (grammatik.getZeichenbereichregeln().get().containsKey(regel.getSymbolbezeichnung()) || grammatik.getZeichenfolgeregeln().get().containsKey(regel.getSymbolbezeichnung()) || grammatik.getZeichenmengeregeln().get().containsKey(regel.getSymbolbezeichnung()) || grammatik.getRegExregeln().get().containsKey(regel.getSymbolbezeichnung())) {
            return behandleBlattregeln(regel);
        }
        throw new SyntaxparserException();
    }

    public Set<Syntaxpfadfolge> behandleNaechstenKnoten(final List<SyntaxpfadMitWort> syntaxpfadeMitWort, final Syntaxpfad syntaxpfadNaechstesSymbol) {
        if (syntaxpfadeMitWort == null || syntaxpfadNaechstesSymbol == null) {
            throw new SyntaxparserException();
        }
        Set<Syntaxpfadfolge> syntaxpfadfolgen = new HashSet<>();
        if (syntaxpfadNaechstesSymbol.istFertig() || syntaxpfadNaechstesSymbol.gebeKnotenfolge().isEmpty()) {
            return syntaxpfadfolgen;
        }
        final List<Symbolkennung> knotenfolge = syntaxpfadNaechstesSymbol.gebeKnotenfolge();
        final Symbolkennung symbolkennung = knotenfolge.get(knotenfolge.size() - 1);
        final List<Symbolkennung> symbolkennungen = new ArrayList<>();
        for (int index = 0; index < (knotenfolge.size() - 1); index++) {
            symbolkennungen.add(knotenfolge.get(index));
        }
        final Set<Syntaxpfad> syntaxpfadeErmittelt = ermittleSyntaxpfade(symbolkennung);
        for (final Syntaxpfad syntaxpfadErmittelt : syntaxpfadeErmittelt) {
            final List<Symbolkennung> symbolkennungenNeu = new ArrayList<>(symbolkennungen);
            symbolkennungenNeu.addAll(syntaxpfadErmittelt.gebeKnotenfolge());
            final Syntaxpfad syntaxpfadNeu = new Syntaxpfad();
            for (final Symbolkennung symbolkennungNeu : symbolkennungenNeu) {
                syntaxpfadNeu.zufuegenKnoten(symbolkennungNeu);
            }
            syntaxpfadNeu.zufuegenBlatt(syntaxpfadErmittelt.gebeBlatt());
            final Syntaxpfadfolge syntaxpfadfolgeNeu = new Syntaxpfadfolge(syntaxpfadeMitWort);
            syntaxpfadfolgeNeu.setzeAktuellenSyntaxpfad(syntaxpfadNeu);
            syntaxpfadfolgen.add(syntaxpfadfolgeNeu);
        }
        return syntaxpfadfolgen;
    }

    private Set<Syntaxpfad> behandleStartregel(final Symbolkennung regel) {
        Set<Syntaxpfad> syntaxpfade = ermittleSyntaxpfade(grammatik.getStartregel().getSymbole().get(0).getSymbolkennung());
        return kopiereSyntaxpfadeMitNeuerWurzel(syntaxpfade, regel);
    }

    private Set<Syntaxpfad> behandleSymbolregeln(final Symbolkennung regel) {
        final Set<Syntaxpfad> syntaxpfade = new HashSet<>();
        final Symbolregeln symbolregeln = grammatik.getSymbolregeln();
        final Set<List<Symbol>> symbolmenge = symbolregeln.get().get(regel.getSymbolbezeichnung());
        for (final List<Symbol> symbolliste : symbolmenge) {
            final Symbolkennung symbolbezeichnung = symbolliste.get(0).getSymbolkennung();
            final Set<Syntaxpfad> syntaxpfadeZuSymbol = ermittleSyntaxpfade(symbolbezeichnung);
            final Set<Syntaxpfad> syntaxpfadeFertig = new HashSet<>();
            final Set<Syntaxpfad> syntaxpfadeNichtFertig = new HashSet<>();
            for (final Syntaxpfad syntaxpfad : syntaxpfadeZuSymbol) {
                if (syntaxpfad.istFertig()) {
                    syntaxpfadeFertig.add(syntaxpfad);
                } else {
                    syntaxpfadeNichtFertig.add(syntaxpfad);
                }
            }
            syntaxpfade.addAll(kopiereSyntaxpfadeMitNeuerWurzel(syntaxpfadeNichtFertig, symbolbezeichnung));
            syntaxpfade.addAll(syntaxpfadeFertig);
        }
        return kopiereSyntaxpfadeMitNeuerWurzel(syntaxpfade, regel);
    }

    private Set<Syntaxpfad> behandleBlattregeln(final Symbolkennung regel) {
        final Set<Syntaxpfad> syntaxpfade = new HashSet<>();
        final Syntaxpfad syntaxpfad = new Syntaxpfad();
        syntaxpfad.zufuegenBlatt(regel);
        syntaxpfade.add(syntaxpfad);
        return syntaxpfade;
    }

    private Set<Syntaxpfad> kopiereSyntaxpfadeMitNeuerWurzel(final Set<Syntaxpfad> syntaxpfade,
            final Symbolkennung wurzel) {
        final Set<Syntaxpfad> syntaxpfadeKopie = new HashSet<>();
        for (final Syntaxpfad syntaxpfad : syntaxpfade) {
            syntaxpfadeKopie.add(syntaxpfad.kopiereMitNeuerWurzel(wurzel));
        }
        return syntaxpfadeKopie;
    }
}
