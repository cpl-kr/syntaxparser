package de.platen.syntaxparser;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.platen.syntaxparser.syntaxpfad.SyntaxpfadMitWort;
import de.platen.syntaxparser.zeichenverarbeitung.Eingabewortabschluss;
import de.platen.syntaxparser.zeichenverarbeitung.Satzabschluss;
import de.platen.syntaxparser.zeichenverarbeitung.Verarbeitungsstand;
import de.platen.syntaxparser.zeichenverarbeitung.Zeichenverarbeitung;

public class Parser
{

    private boolean istSondermodus = false;
    private boolean istWortabschlussDurchgefuehrt = false;
    private final Zeichenverarbeitung zeichenverarbeitung;
    private final Eingabewortabschluss eingabewortabschluss;

    private final Satzabschluss satzabschluss;
    private final Set<Verarbeitungsstand> verarbeitungsstaendeInBearbeitung;

    public Parser(final ParserInitialisierung parserInitialisierung, final Zeichenverarbeitung zeichenverarbeitung, final Eingabewortabschluss eingabewortabschluss, final Satzabschluss satzabschluss) {
        if ((parserInitialisierung == null) || (zeichenverarbeitung == null) || (eingabewortabschluss == null)  || (satzabschluss == null)) {
            throw new SyntaxparserException();
        }
        this.zeichenverarbeitung = zeichenverarbeitung;
        this.verarbeitungsstaendeInBearbeitung = parserInitialisierung.initialisiereVerarbeitungsstaende();
        this.eingabewortabschluss = eingabewortabschluss;
        this.satzabschluss = satzabschluss;
    }

    public void verarbeiteZeichen(final Character c) {
        if (c == null) {
            throw new SyntaxparserException();
        }
        if (!this.istSondermodus) {
            if (!istWhiteSpace(c)) {
                this.istWortabschlussDurchgefuehrt = false;
                if (c == '\\') {
                    this.istSondermodus = true;
                } else {
                    if (!this.zeichenverarbeitung.verarbeiteZeichen(c, this.verarbeitungsstaendeInBearbeitung)) {
                        throw new ParseException();
                    }
                }
            } else {
                if (!this.istWortabschlussDurchgefuehrt) {
                    if (!this.eingabewortabschluss.schliesseEingabewortAb(this.verarbeitungsstaendeInBearbeitung)) {
                        throw new ParseException();
                    }
                    this.istWortabschlussDurchgefuehrt = true;
                }
            }
        } else {
            this.istSondermodus = false;
            if (!this.zeichenverarbeitung.verarbeiteZeichen(c, this.verarbeitungsstaendeInBearbeitung)) {
                throw new ParseException();
            }
        }
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
    private boolean istWhiteSpace(final Character c) {
        return (c == ' ') || (c == '\t') || istZeilenende(c);
    }

    private boolean istZeilenende(final Character c) {
        return (c == '\n') || (c == '\r');
    }
}
