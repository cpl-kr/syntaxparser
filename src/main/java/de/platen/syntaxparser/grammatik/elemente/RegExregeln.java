package de.platen.syntaxparser.grammatik.elemente;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

import de.platen.syntaxparser.grammatik.GrammatikException;

public class RegExregeln
{

    private final Map<Symbolbezeichnung, Set<RegEx>> regExregeln;

    public RegExregeln(final Map<Symbolbezeichnung, Set<RegEx>> regExregeln) {
        if (regExregeln == null) {
            throw new GrammatikException();
        }
        this.regExregeln = Collections.unmodifiableMap(regExregeln);
    }

    public Map<Symbolbezeichnung, Set<RegEx>> get() {
        return regExregeln;
    }
}
