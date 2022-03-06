package de.platen.syntaxparser.grammatik.elemente;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

import de.platen.syntaxparser.grammatik.GrammatikException;

public class Zeichenfolgeregeln
{

    private final Map<Symbolbezeichnung, Set<Zeichenfolge>> zeichenfolgeregeln;

    public Zeichenfolgeregeln(final Map<Symbolbezeichnung, Set<Zeichenfolge>> zeichenfolgeregeln) {
        if (zeichenfolgeregeln == null) {
            throw new GrammatikException();
        }
        this.zeichenfolgeregeln = Collections.unmodifiableMap(zeichenfolgeregeln);
    }

    public Map<Symbolbezeichnung, Set<Zeichenfolge>> get() {
        return zeichenfolgeregeln;
    }
}
