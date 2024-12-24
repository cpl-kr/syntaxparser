package de.platen.syntaxparser.parser.regelverarbeitung;

import de.platen.syntaxparser.parser.SyntaxparserException;
import de.platen.syntaxparser.grammatik.elemente.RegEx;
import de.platen.syntaxparser.grammatik.elemente.Symbolbezeichnung;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VerarbeitungRegEx extends Verarbeitung {

    private final Set<Pattern> pattern = new HashSet<>();

    public VerarbeitungRegEx(final Symbolbezeichnung symbolbezeichnung, final Set<RegEx> regExMenge) {
        super(symbolbezeichnung);
        if ((regExMenge == null) || regExMenge.isEmpty()) {
            throw new SyntaxparserException();
        }
        for (RegEx regEx : regExMenge) {
            this.pattern.add(Pattern.compile(regEx.getRegex()));
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
        final String wortAktuell = wort + character;
        boolean istGefunden = false;
        for (Pattern pattern : this.pattern) {
            final Matcher matcher = pattern.matcher(wortAktuell);
            if (matcher.matches()) {
                istZustandErreicht = true;
                istGefunden = true;
            }
        }
        if (istZustandErreicht && !istGefunden) {
            istGesperrt = true;
        } else {
            wort = wortAktuell;
            return true;
        }
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        VerarbeitungRegEx that = (VerarbeitungRegEx) o;
        return pattern.equals(that.pattern);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), pattern);
    }
}
