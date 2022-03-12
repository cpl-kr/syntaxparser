package de.platen.syntaxparser.grammatik.elemente;

import java.io.Serializable;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

import de.platen.syntaxparser.grammatik.GrammatikException;

public class Zeichenmengeregeln implements Serializable
{

    private static final long serialVersionUID = 1L;

    private final Map<Symbolbezeichnung, Set<Zeichenmenge>> zeichenmengeregeln;

    public Zeichenmengeregeln(final Map<Symbolbezeichnung, Set<Zeichenmenge>> zeichenmengeregeln) {
        if (zeichenmengeregeln == null) {
            throw new GrammatikException();
        }
        this.zeichenmengeregeln = Collections.unmodifiableMap(zeichenmengeregeln);
    }

    public Map<Symbolbezeichnung, Set<Zeichenmenge>> get() {
        return zeichenmengeregeln;
    }
}
