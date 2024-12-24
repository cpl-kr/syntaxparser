package de.platen.syntaxparser.parser.regelverarbeitung;

import de.platen.syntaxparser.parser.SyntaxparserException;
import de.platen.syntaxparser.grammatik.elemente.Symbolbezeichnung;
import de.platen.syntaxparser.grammatik.elemente.Zeichenbereich;
import de.platen.syntaxparser.grammatik.elemente.Zeichenmenge;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class VerarbeitungZeichenbereichUndMenge extends Verarbeitung {

    private final Set<Zeichenbereich> zeichenbereiche = new HashSet<>();
    private final Set<Zeichenmenge> zeichenmengen = new HashSet<>();

    public VerarbeitungZeichenbereichUndMenge(final Symbolbezeichnung symbolbezeichnung,
                                              final Set<Zeichenbereich> zeichenbereiche, final Set<Zeichenmenge> zeichenmengen) {
        super(symbolbezeichnung);
        if (((zeichenbereiche == null) || zeichenbereiche.isEmpty()) && ((zeichenmengen == null) || zeichenmengen.isEmpty())) {
            throw new SyntaxparserException();
        }
        if (zeichenbereiche != null) {
            this.zeichenbereiche.addAll(zeichenbereiche);
        }
        if (zeichenmengen != null) {
            this.zeichenmengen.addAll(zeichenmengen);
        }
    }

    @Override
    public boolean verarbeiteZeichen(final Character character) {
        if (character == null) {
            throw new SyntaxparserException();
        }
        istStartzustand = false;
        if (istGesperrt) {
            return false;
        }
        boolean istGefunden = false;
        for (Zeichenbereich zeichenbereich : this.zeichenbereiche) {
            if ((character >= zeichenbereich.getVon()) && (character <= zeichenbereich.getBis())) {
                istGefunden = true;
            }
        }
        for (Zeichenmenge zeichenmenge : this.zeichenmengen) {
            if (zeichenmenge.getZeichenmenge().contains(character)) {
                istGefunden = true;
            }
        }
        if (istGefunden) {
            this.wort = this.wort + character;
            this.istZustandErreicht = true;
            return true;
        } else {
            this.istGesperrt = true;
        }
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        VerarbeitungZeichenbereichUndMenge that = (VerarbeitungZeichenbereichUndMenge) o;
        return zeichenbereiche.equals(that.zeichenbereiche) && zeichenmengen.equals(that.zeichenmengen);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), zeichenbereiche, zeichenmengen);
    }
}
