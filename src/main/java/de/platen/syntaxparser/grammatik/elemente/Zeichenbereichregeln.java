package de.platen.syntaxparser.grammatik.elemente;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

import de.platen.syntaxparser.grammatik.GrammatikException;

public class Zeichenbereichregeln
{

    private final Map<Symbolbezeichnung, Set<Zeichenbereich>> zeichenbereichregeln;

    public Zeichenbereichregeln(final Map<Symbolbezeichnung, Set<Zeichenbereich>> zeichenbereichregeln) {
        if (zeichenbereichregeln == null) {
            throw new GrammatikException();
        }
        this.zeichenbereichregeln = Collections.unmodifiableMap(zeichenbereichregeln);
    }

    public Map<Symbolbezeichnung, Set<Zeichenbereich>> get() {
        return zeichenbereichregeln;
    }
}
