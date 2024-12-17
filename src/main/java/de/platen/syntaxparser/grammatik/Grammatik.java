package de.platen.syntaxparser.grammatik;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.platen.syntaxparser.grammatik.elemente.RegEx;
import de.platen.syntaxparser.grammatik.elemente.RegExregeln;
import de.platen.syntaxparser.grammatik.elemente.RegelSymbole;
import de.platen.syntaxparser.grammatik.elemente.Symbol;
import de.platen.syntaxparser.grammatik.elemente.Symbolbezeichnung;
import de.platen.syntaxparser.grammatik.elemente.Symbolregeln;
import de.platen.syntaxparser.grammatik.elemente.Zeichenbereich;
import de.platen.syntaxparser.grammatik.elemente.Zeichenbereichregeln;
import de.platen.syntaxparser.grammatik.elemente.Zeichenfolge;
import de.platen.syntaxparser.grammatik.elemente.Zeichenfolgeregeln;
import de.platen.syntaxparser.grammatik.elemente.Zeichenmenge;
import de.platen.syntaxparser.grammatik.elemente.Zeichenmengeregeln;

public abstract class Grammatik implements Serializable
{

    private static final long serialVersionUID = 1L;

    protected RegelSymbole startregel;
    protected final Map<Symbolbezeichnung, List<List<Symbol>>> symbolregeln = new HashMap<>();
    protected final Map<Symbolbezeichnung, Set<Zeichenbereich>> zeichenbereichregeln = new HashMap<>();
    protected final Map<Symbolbezeichnung, Set<Zeichenfolge>> zeichenfolgeregeln = new HashMap<>();
    protected final Map<Symbolbezeichnung, Set<Zeichenmenge>> zeichenmengeregeln = new HashMap<>();
    protected final Map<Symbolbezeichnung, Set<RegEx>> regExregeln = new HashMap<>();

    public RegelSymbole getStartregel() {
        return new RegelSymbole(this.startregel.getSymbolbezeichnung(), Collections.unmodifiableList(this.startregel.getSymbole()));
    }

    public Symbolregeln getSymbolregeln() {
        return new Symbolregeln(Collections.unmodifiableMap(this.symbolregeln));
    }

    public Zeichenbereichregeln getZeichenbereichregeln() {
        return new Zeichenbereichregeln(Collections.unmodifiableMap(this.zeichenbereichregeln));
    }

    public Zeichenfolgeregeln getZeichenfolgeregeln() {
        return new Zeichenfolgeregeln(Collections.unmodifiableMap(this.zeichenfolgeregeln));
    }

    public Zeichenmengeregeln getZeichenmengeregeln() {
        return new Zeichenmengeregeln(Collections.unmodifiableMap(this.zeichenmengeregeln));
    }

    public RegExregeln getRegExregeln() {
        return new RegExregeln(Collections.unmodifiableMap(this.regExregeln));
    }
}
