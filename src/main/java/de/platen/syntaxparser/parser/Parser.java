package de.platen.syntaxparser.parser;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.platen.syntaxparser.grammatik.Grammatik;
import de.platen.syntaxparser.parser.syntaxpfad.SyntaxpfadMitWort;
import de.platen.syntaxparser.parser.syntaxpfad.Syntaxpfadbehandlung;
import de.platen.syntaxparser.parser.syntaxpfad.Syntaxpfadersteller;
import de.platen.syntaxparser.parser.zeichenverarbeitung.Eingabewortabschluss;
import de.platen.syntaxparser.parser.zeichenverarbeitung.Satzabschluss;
import de.platen.syntaxparser.parser.zeichenverarbeitung.Verarbeitungsstand;
import de.platen.syntaxparser.parser.zeichenverarbeitung.Wortabschluss;
import de.platen.syntaxparser.parser.zeichenverarbeitung.Zeichenverarbeitung;

import static java.util.Objects.requireNonNull;

public class Parser
{

    private boolean istSondermodus = false;
    private boolean istWortabschlussDurchgefuehrt = false;
    private final Zeichenverarbeitung zeichenverarbeitung;
    private final Eingabewortabschluss eingabewortabschluss;

    private final Satzabschluss satzabschluss;
    private final Set<Verarbeitungsstand> verarbeitungsstaendeInBearbeitung;

    public Parser(final Grammatik grammatik) {
        requireNonNull(grammatik);
        final ParserInitialisierung parserInitialisierung = new ParserInitialisierung(new Syntaxpfadersteller(grammatik), new Syntaxpfadbehandlung(grammatik));
        final Wortabschluss wortabschluss = new Wortabschluss(new Syntaxpfadersteller(grammatik), new Syntaxpfadbehandlung(grammatik));
        final Eingabewortabschluss eingabewortabschluss = new Eingabewortabschluss(wortabschluss);
        this.satzabschluss = new Satzabschluss();
        this.zeichenverarbeitung = new Zeichenverarbeitung(wortabschluss);
        this.verarbeitungsstaendeInBearbeitung = parserInitialisierung.initialisiereVerarbeitungsstaende();
        this.eingabewortabschluss = eingabewortabschluss;
    }

    public Parser(final ParserInitialisierung parserInitialisierung, final Zeichenverarbeitung zeichenverarbeitung, final Eingabewortabschluss eingabewortabschluss, final Satzabschluss satzabschluss) {
        requireNonNull(parserInitialisierung);
        this.zeichenverarbeitung = requireNonNull(zeichenverarbeitung);
        this.verarbeitungsstaendeInBearbeitung = parserInitialisierung.initialisiereVerarbeitungsstaende();
        this.eingabewortabschluss = requireNonNull(eingabewortabschluss);
        this.satzabschluss = requireNonNull(satzabschluss);
    }

    public boolean verarbeiteZeichen(final Character c) {
        if (c == null) {
            throw new SyntaxparserException();
        }
        if (!this.istSondermodus) {
            if (!istWhiteSpace(c)) {
                this.istWortabschlussDurchgefuehrt = false;
                if (c == '\\') {
                    this.istSondermodus = true;
                } else {
                    return this.zeichenverarbeitung.verarbeiteZeichen(c, this.verarbeitungsstaendeInBearbeitung);
                }
            } else {
                if (!this.istWortabschlussDurchgefuehrt) {
                    if (!this.eingabewortabschluss.schliesseEingabewortAb(this.verarbeitungsstaendeInBearbeitung)) {
                        return false;
                    }
                    this.istWortabschlussDurchgefuehrt = true;
                }
            }
        } else {
            this.istSondermodus = false;
            return this.zeichenverarbeitung.verarbeiteZeichen(c, this.verarbeitungsstaendeInBearbeitung);
        }
        return true;
    }

    public List<SyntaxpfadMitWort> ermittleSyntaxpfadeMitWort(final boolean alsAbschluss) {
        if (alsAbschluss) {
            if (!this.istWortabschlussDurchgefuehrt) {
                this.eingabewortabschluss.schliesseEingabewortAb(this.verarbeitungsstaendeInBearbeitung);
                this.istWortabschlussDurchgefuehrt = true;
            }
        } else {
            if (!this.istWortabschlussDurchgefuehrt) {
                throw new SyntaxparserException();
            }
        }
        Set<Verarbeitungsstand> verarbeitungsstaendeKopie = new HashSet<>(this.verarbeitungsstaendeInBearbeitung);
        return this.satzabschluss.schliesseSatzAb(verarbeitungsstaendeKopie);
    }

    public String ermittleSyntaxpfadeMitWortAlsString(final boolean alsAbschluss) {
        return ermittleSyntaxpfadeMitWort(alsAbschluss).toString();
    }

    private boolean istWhiteSpace(final Character c) {
        return (c == ' ') || (c == '\t') || istZeilenende(c);
    }

    private boolean istZeilenende(final Character c) {
        return (c == '\n') || (c == '\r');
    }
}
