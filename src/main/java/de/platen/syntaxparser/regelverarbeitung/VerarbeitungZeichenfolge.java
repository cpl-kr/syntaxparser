package de.platen.syntaxparser.regelverarbeitung;

import de.platen.syntaxparser.SyntaxparserException;
import de.platen.syntaxparser.grammatik.elemente.Symbolbezeichnung;
import de.platen.syntaxparser.grammatik.elemente.Zeichenfolge;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class VerarbeitungZeichenfolge extends Verarbeitung {

    private final Set<Zeichenfolge> zeichenfolgen = new HashSet<>();

    public VerarbeitungZeichenfolge(final Symbolbezeichnung symbolbezeichnung, final Set<Zeichenfolge> zeichenfolgen) {
        super(symbolbezeichnung);
        if ((zeichenfolgen == null) || zeichenfolgen.isEmpty()) {
            throw new SyntaxparserException();
        }
        this.zeichenfolgen.addAll(zeichenfolgen);
    }

    @Override
    public boolean verarbeiteZeichen(final Character character) {
        if (character == null) {
            throw new SyntaxparserException();
        }
        if (istGesperrt) {
            return false;
        }
        istStartzustand = false;
        if (!istZustandErreicht) {
            final String wortAktuell = wort + character;
            boolean istVerarbeitet = false;
            for (Zeichenfolge zeichenfolge : this.zeichenfolgen) {
                if (wortAktuell.length() == zeichenfolge.getZeichenfolge().length()) {
                    if (wortAktuell.equals(zeichenfolge.getZeichenfolge())) {
                        istZustandErreicht = true;
                        istGesperrt = true;
                        istVerarbeitet = true;
                    }
                } else {
                    if (zeichenfolge.getZeichenfolge().startsWith(wortAktuell)) {
                        istVerarbeitet = true;
                    }
                }
            }
            if (istVerarbeitet) {
                this.wort = wortAktuell;
                return true;
            } else {
                wort = "";
                istGesperrt = true;
            }
        }
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        VerarbeitungZeichenfolge that = (VerarbeitungZeichenfolge) o;
        return zeichenfolgen.equals(that.zeichenfolgen);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), zeichenfolgen);
    }
}
