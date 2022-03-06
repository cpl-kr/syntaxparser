package de.platen.syntaxparser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.platen.syntaxparser.grammatik.Grammatik;
import de.platen.syntaxparser.grammatik.elemente.Symbolkennung;
import de.platen.syntaxparser.syntaxpfad.Syntaxpfad;
import de.platen.syntaxparser.syntaxpfad.SyntaxpfadMitWort;
import de.platen.syntaxparser.syntaxpfad.Syntaxpfadbehandlung;
import de.platen.syntaxparser.syntaxpfad.Syntaxpfadersteller;
import de.platen.syntaxparser.syntaxpfad.Syntaxpfadfolge;

public class Parser
{

    private final Syntaxpfadersteller syntaxpfadersteller;
    private final Syntaxpfadbehandlung syntaxpfadbehandlung;
    private boolean istSondermodus = false;
    private StringBuilder stringBuilder = new StringBuilder();
    private final Set<Syntaxpfadfolge> syntaxpfadfolgen = new HashSet<>();

    public Parser(final Grammatik grammatik) {
        if (grammatik == null) {
            throw new SyntaxparserException();
        }
        syntaxpfadersteller = new Syntaxpfadersteller(grammatik);
        syntaxpfadbehandlung = new Syntaxpfadbehandlung(grammatik);
        final Set<Syntaxpfad> syntaxpfade = syntaxpfadersteller.ermittleSyntaxpfadeVonStartSymbol();
        for (final Syntaxpfad syntaxpfad : syntaxpfade) {
            final Syntaxpfadfolge syntaxpfadfolge = new Syntaxpfadfolge();
            syntaxpfadfolge.setzeAktuellenSyntaxpfad(syntaxpfad);
            syntaxpfadfolgen.add(syntaxpfadfolge);
        }
    }

    public void verarbeiteZeichen(final Character c) {
        if (c == null) {
            throw new SyntaxparserException();
        }
        if (!istSondermodus) {
            if (!istWhiteSpace(c)) {
                if (c == '\\') {
                    istSondermodus = true;
                } else {
                    stringBuilder.append(c);
                }
            } else {
                final String wort = stringBuilder.toString();
                stringBuilder = new StringBuilder();
                verarbeiteWort(wort);
            }
        } else {
            stringBuilder.append(c);
            istSondermodus = false;
        }
    }

    public List<SyntaxpfadMitWort> gebeSyntaxpfadeMitWort() {
        List<SyntaxpfadMitWort> syntaxpfadeMitWort = null;
        for (final Syntaxpfadfolge syntaxpfadfolge : syntaxpfadfolgen) {
            if (syntaxpfadfolge.getAktuell() == null) {
                if (syntaxpfadeMitWort != null) {
                    throw new ParseException();
                }
                syntaxpfadeMitWort = Collections.unmodifiableList(syntaxpfadfolge.getSyntaxpfadeMitWort());
            }
        }
        if (syntaxpfadeMitWort == null) {
            throw new ParseException();
        }
        return syntaxpfadeMitWort;
    }

    private boolean istWhiteSpace(final Character c) {
        return (c == ' ') || (c == '\t') || istZeilenende(c);
    }

    private boolean istZeilenende(final Character c) {
        return (c == '\n') || (c == '\r');
    }

    private void verarbeiteWort(final String wort) {
        final Set<Syntaxpfadfolge> syntaxpfadfolgenZumLoeschen = new HashSet<>();
        final Set<Syntaxpfadfolge> syntaxpfadfolgenZumHinzufuegen = new HashSet<>();
        for (final Syntaxpfadfolge syntaxpfadfolge : syntaxpfadfolgen) {
            final Syntaxpfad syntaxpfad = syntaxpfadfolge.getAktuell();
            if (syntaxpfad != null) {
                final Set<Syntaxpfad> syntaxpfade = new HashSet<>();
                syntaxpfade.add(syntaxpfad);
                final Set<Syntaxpfad> syntaxpfadePassend = syntaxpfadbehandlung.findePassendeSytaxpfade(syntaxpfade,
                        wort);
                if (!syntaxpfadePassend.isEmpty()) {
                    for (final Syntaxpfad syntaxpfadPassend : syntaxpfadePassend) {
                        behandlePassendenSyntaxpfad(wort, syntaxpfadfolgenZumLoeschen, syntaxpfadfolgenZumHinzufuegen,
                                syntaxpfadfolge, syntaxpfadPassend);
                    }
                } else {
                    syntaxpfadfolgenZumLoeschen.add(syntaxpfadfolge);
                }
            } else {
                syntaxpfadfolgenZumLoeschen.add(syntaxpfadfolge);
            }
        }
        syntaxpfadfolgen.removeAll(syntaxpfadfolgenZumLoeschen);
        syntaxpfadfolgen.addAll(syntaxpfadfolgenZumHinzufuegen);
        if (syntaxpfadfolgen.isEmpty()) {
            throw new ParseException();
        }
    }

    private void behandlePassendenSyntaxpfad(final String wort, final Set<Syntaxpfadfolge> syntaxpfadfolgenZumLoeschen,
            final Set<Syntaxpfadfolge> syntaxpfadfolgenZumHinzufuegen, final Syntaxpfadfolge syntaxpfadfolge,
            final Syntaxpfad syntaxpfadPassend) {
        final Syntaxpfad syntaxpfadLeer = new Syntaxpfad();
        final Syntaxpfad syntaxpfadNaechstesSymbol = syntaxpfadbehandlung.findeNaechstesSymbol(syntaxpfadPassend);
        if (syntaxpfadNaechstesSymbol.equals(syntaxpfadLeer)) {
            syntaxpfadfolge.uebernehmeAktuellenSyntaxpfad(wort);
        } else {
            if (syntaxpfadNaechstesSymbol.istFertig()) {
                syntaxpfadfolge.uebernehmeAktuellenSyntaxpfad(wort);
                syntaxpfadfolge.setzeAktuellenSyntaxpfad(syntaxpfadNaechstesSymbol);
            } else {
                behandleNaechstenKnoten(wort, syntaxpfadfolgenZumLoeschen, syntaxpfadfolgenZumHinzufuegen,
                        syntaxpfadfolge, syntaxpfadNaechstesSymbol);
            }
        }
    }

    private void behandleNaechstenKnoten(final String wort, final Set<Syntaxpfadfolge> syntaxpfadfolgenZumLoeschen,
            final Set<Syntaxpfadfolge> syntaxpfadfolgenZumHinzufuegen, final Syntaxpfadfolge syntaxpfadfolge,
            final Syntaxpfad syntaxpfadNaechstesSymbol) {
        final List<Symbolkennung> knotenfolge = syntaxpfadNaechstesSymbol.gebeKnotenfolge();
        final Symbolkennung symbolkennung = knotenfolge.get(knotenfolge.size() - 1);
        final List<Symbolkennung> symbolkennungen = new ArrayList<>();
        for (int index = 0; index < (knotenfolge.size() - 1); index++) {
            symbolkennungen.add(knotenfolge.get(index));
        }
        final Set<Syntaxpfad> syntaxpfadeErmittelt = syntaxpfadersteller.ermittleSyntaxpfade(symbolkennung);
        syntaxpfadfolge.uebernehmeAktuellenSyntaxpfad(wort);
        if (!syntaxpfadeErmittelt.isEmpty()) {
            for (final Syntaxpfad syntaxpfadErmittelt : syntaxpfadeErmittelt) {
                final List<Symbolkennung> symbokennungenNeu = new ArrayList<>(symbolkennungen);
                symbokennungenNeu.addAll(syntaxpfadErmittelt.gebeKnotenfolge());
                final Syntaxpfad syntaxpfadNeu = new Syntaxpfad();
                for (final Symbolkennung symbolkennungNeu : symbokennungenNeu) {
                    syntaxpfadNeu.zufuegenKnoten(symbolkennungNeu);
                }
                syntaxpfadNeu.zufuegenBlatt(syntaxpfadErmittelt.gebeBlatt());
                final Syntaxpfadfolge syntaxpfadfolgeNeu = new Syntaxpfadfolge(syntaxpfadfolge.getSyntaxpfadeMitWort());
                syntaxpfadfolgeNeu.setzeAktuellenSyntaxpfad(syntaxpfadNeu);
                syntaxpfadfolgenZumHinzufuegen.add(syntaxpfadfolgeNeu);
            }
            syntaxpfadfolgenZumLoeschen.add(syntaxpfadfolge);
        }
    }
}
