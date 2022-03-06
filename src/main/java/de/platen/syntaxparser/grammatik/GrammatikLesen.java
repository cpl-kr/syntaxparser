package de.platen.syntaxparser.grammatik;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import de.platen.syntaxparser.grammatik.elemente.Kardinalitaet;
import de.platen.syntaxparser.grammatik.elemente.RegEx;
import de.platen.syntaxparser.grammatik.elemente.RegelRegEx;
import de.platen.syntaxparser.grammatik.elemente.RegelSymbole;
import de.platen.syntaxparser.grammatik.elemente.RegelZeichenbereich;
import de.platen.syntaxparser.grammatik.elemente.RegelZeichenfolge;
import de.platen.syntaxparser.grammatik.elemente.RegelZeichenmenge;
import de.platen.syntaxparser.grammatik.elemente.Symbol;
import de.platen.syntaxparser.grammatik.elemente.Symbolbezeichnung;
import de.platen.syntaxparser.grammatik.elemente.Symbolidentifizierung;
import de.platen.syntaxparser.grammatik.elemente.Symbolkennung;
import de.platen.syntaxparser.grammatik.elemente.Zeichenbereich;
import de.platen.syntaxparser.grammatik.elemente.Zeichenfolge;
import de.platen.syntaxparser.grammatik.elemente.Zeichenmenge;

public class GrammatikLesen
{

    private enum REGEL {
        START, SYMBOLE, ZEICHENBEREICH, ZEICHENFOLGE, ZEICHENMENGE, REGEX, ENDE
    }

    private REGEL regel = REGEL.START;
    private boolean istInKommentar = false;
    private boolean istSondermodus = false;
    private boolean istAufLinkeSeite = true;
    private boolean istInBearbeitung = false;
    private final Grammatikteil grammatikteil = new Grammatikteil();
    private final GrammatikAufbau grammatikAufbau;
    private int identifizierung = 1;

    public GrammatikLesen(final GrammatikAufbau grammatikAufbau) {
        if (grammatikAufbau == null) {
            throw new GrammatikException();
        }
        this.grammatikAufbau = grammatikAufbau;
    }

    public void verarbeiteZeichen(final Character c) {
        if (c == null) {
            throw new GrammatikException();
        }
        if (!istInKommentar) {
            if (!istSondermodus) {
                if (!istWhiteSpace(c)) {
                    if (c == '#') {
                        istInKommentar = true;
                        schliesseTeilbearbeitungAb();
                    } else {
                        if (c == '\\') {
                            istSondermodus = true;
                        } else {
                            behandleRegel(c);
                        }
                    }
                } else {
                    schliesseTeilbearbeitungAb();
                }
            } else {
                Character ch = c;
                if (c == 'n') {
                    ch = '\n';
                }
                if (c == 'r') {
                    ch = '\r';
                }
                if (c == 't') {
                    ch = '\t';
                }
                behandleRegel(ch);
                istSondermodus = false;
            }
        } else {
            if (istZeilenende(c)) {
                istInKommentar = false;
            }
        }
    }

    public boolean istFertig() {
        return regel == REGEL.ENDE;
    }

    public void checkGrammatik() {
        grammatikAufbau.checkGrammatik();
    }

    public Grammatik getGrammatik() {
        return grammatikAufbau;
    }

    private boolean istWhiteSpace(final Character c) {
        return (c == ' ') || (c == '\t') || istZeilenende(c);
    }

    private boolean istZeilenende(final Character c) {
        return (c == '\n') || (c == '\r');
    }

    private void behandleRegel(final Character c) {
        if (istAufLinkeSeite) {
            if (!istInBearbeitung) {
                istInBearbeitung = true;
                final StringBuilder stringBuilder = new StringBuilder();
                grammatikteil.setStringBuilder(stringBuilder);
                stringBuilder.append(c);
            } else {
                grammatikteil.getStringBuilder().append(c);
            }
        } else {
            if (!istInBearbeitung) {
                regel = ermittleRegel(c);
                initialisiereGrammatikteil();
                istInBearbeitung = true;
            } else {
                bearbeiteRegelteil(c);
            }
        }
    }

    private void schliesseTeilbearbeitungAb() {
        if (istAufLinkeSeite) {
            if (istInBearbeitung) {
                istInBearbeitung = false;
                final String name = grammatikteil.getStringBuilder().toString();
                final Symbolbezeichnung symbolbezeichnung = new Symbolbezeichnung(name);
                grammatikteil.setSymbolbezeichnung(symbolbezeichnung);
                istAufLinkeSeite = false;
            }
        } else {
            if (((regel == REGEL.START) || (regel == REGEL.SYMBOLE)) && istInBearbeitung) {
                final String bezeichnung = grammatikteil.getStringBuilder().toString();
                if (!bezeichnung.isEmpty()) {
                    final Symbol symbol = new Symbol(
                            new Symbolkennung(new Symbolbezeichnung(bezeichnung),
                                    new Symbolidentifizierung(Integer.valueOf(identifizierung))),
                            grammatikteil.getKardinalitaet());
                    identifizierung++;
                    grammatikteil.getSymbole().add(symbol);
                    grammatikteil.setStringBuilder(new StringBuilder());
                    grammatikteil.setKardinalitaet(Kardinalitaet.GENAU_EINMAL);
                }
            }
        }
    }

    private REGEL ermittleRegel(final Character c) {
        if (c == '{') {
            if (regel == REGEL.START) {
                return REGEL.START;
            }
            return REGEL.SYMBOLE;
        }
        if (c == '[') {
            return REGEL.ZEICHENBEREICH;
        }
        if (c == '"') {
            return REGEL.ZEICHENFOLGE;
        }
        if (c == '(') {
            return REGEL.ZEICHENMENGE;
        }
        if (c == '<') {
            return REGEL.REGEX;
        }
        throw new GrammatikException();
    }

    private void initialisiereGrammatikteil() {
        if ((regel == REGEL.START) || (regel == REGEL.SYMBOLE)) {
            grammatikteil.setStringBuilder(new StringBuilder());
            grammatikteil.setKardinalitaet(Kardinalitaet.GENAU_EINMAL);
            grammatikteil.setSymbole(new ArrayList<>());
        }
        if (regel == REGEL.ZEICHENFOLGE) {
            grammatikteil.setStringBuilder(new StringBuilder());
        }
        if (regel == REGEL.ZEICHENMENGE) {
            grammatikteil.setCharacters(new HashSet<>());
        }
        if (regel == REGEL.ZEICHENBEREICH) {
            grammatikteil.setVon(null);
            grammatikteil.setBis(null);
        }
        if (regel == REGEL.REGEX) {
            grammatikteil.setStringBuilder(new StringBuilder());
        }
    }

    private void bearbeiteRegelteil(final Character c) {
        if ((regel == REGEL.START) || (regel == REGEL.SYMBOLE)) {
            if (c != '}') {
                if (c == '+') {
                    grammatikteil.setKardinalitaet(Kardinalitaet.MINDESTENS_EINMAL);
                } else {
                    grammatikteil.getStringBuilder().append(c);
                }
            } else {
                schliesseTeilbearbeitungAb();
                final Symbolbezeichnung symbolbezeichnung = grammatikteil.getSymbolbezeichnung();
                final List<Symbol> symbole = grammatikteil.getSymbole();
                final RegelSymbole regelSymbole = new RegelSymbole(symbolbezeichnung, symbole);
                if (regel == REGEL.START) {
                    grammatikAufbau.setStartregel(regelSymbole);
                } else {
                    grammatikAufbau.addRegelSymbole(regelSymbole);
                }
                schliesseBearbeitungAb();
            }
        }
        if (regel == REGEL.ZEICHENFOLGE) {
            if ((c != '"') || istSondermodus) {
                grammatikteil.getStringBuilder().append(c);
            } else {
                final Symbolbezeichnung symbolbezeichnung = grammatikteil.getSymbolbezeichnung();
                final Zeichenfolge zeichenfolge = new Zeichenfolge(grammatikteil.getStringBuilder().toString());
                final RegelZeichenfolge regelZeichenfolge = new RegelZeichenfolge(symbolbezeichnung, zeichenfolge);
                grammatikAufbau.addRegelZeichenfolge(regelZeichenfolge);
                schliesseBearbeitungAb();
            }
        }
        if (regel == REGEL.ZEICHENMENGE) {
            if ((c != ')') || istSondermodus) {
                grammatikteil.getCharacters().add(c);
            } else {
                final Symbolbezeichnung symbolbezeichnung = grammatikteil.getSymbolbezeichnung();
                final Zeichenmenge zeichenmenge = new Zeichenmenge(grammatikteil.getCharacters());
                final RegelZeichenmenge regelZeichenmenge = new RegelZeichenmenge(symbolbezeichnung, zeichenmenge);
                grammatikAufbau.addRegelZeichenmenge(regelZeichenmenge);
                schliesseBearbeitungAb();
            }
        }
        if (regel == REGEL.ZEICHENBEREICH) {
            if ((c != ']') || istSondermodus) {
                if (grammatikteil.getVon() == null) {
                    grammatikteil.setVon(c);
                } else {
                    if (grammatikteil.getBis() == null) {
                        grammatikteil.setBis(c);
                    }
                }
            } else {
                final Symbolbezeichnung symbolbezeichnung = grammatikteil.getSymbolbezeichnung();
                final Zeichenbereich zeichenbereich = new Zeichenbereich(grammatikteil.getVon(),
                        grammatikteil.getBis());
                final RegelZeichenbereich regelZeichenbereich = new RegelZeichenbereich(symbolbezeichnung,
                        zeichenbereich);
                grammatikAufbau.addRegelZeichenbereich(regelZeichenbereich);
                schliesseBearbeitungAb();
            }
        }
        if (regel == REGEL.REGEX) {
            if ((c != '>') || istSondermodus) {
                grammatikteil.getStringBuilder().append(c);
            } else {
                final Symbolbezeichnung symbolbezeichnung = grammatikteil.getSymbolbezeichnung();
                final RegEx regEx = new RegEx(grammatikteil.getStringBuilder().toString());
                final RegelRegEx regelRegEx = new RegelRegEx(symbolbezeichnung, regEx);
                grammatikAufbau.addRegelRegEx(regelRegEx);
                schliesseBearbeitungAb();
            }
        }
    }

    private void schliesseBearbeitungAb() {
        istInBearbeitung = false;
        istAufLinkeSeite = true;
        regel = REGEL.ENDE;
    }
}
