package de.platen.syntaxparser.grammatik.elemente;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.platen.syntaxparser.grammatik.GrammatikException;

public class Symbolregeln
{

    private final Map<Symbolbezeichnung, Set<List<Symbol>>> symbolregeln;
    private final Map<Symbolkennung, List<Symbol>> zuordnungen = new HashMap<>();

    public Symbolregeln(final Map<Symbolbezeichnung, Set<List<Symbol>>> symbolregeln) {
        if (symbolregeln == null) {
            throw new GrammatikException();
        }
        this.symbolregeln = Collections.unmodifiableMap(symbolregeln);
        macheZordnungen();
    }

    public Map<Symbolbezeichnung, Set<List<Symbol>>> get() {
        return symbolregeln;
    }

    public List<Symbol> ermittleRegel(final Symbolkennung symbolkennung) {
        checkSymbolkennung(symbolkennung);
        return Collections.unmodifiableList(zuordnungen.get(symbolkennung));
    }

    public boolean istLetztesSymbolInRegel(final Symbolkennung symbolkennung) {
        checkSymbolkennung(symbolkennung);
        final List<Symbol> symbole = ermittleRegel(symbolkennung);
        if (symbole.size() > 0) {
            final Symbol symbol = symbole.get(symbole.size() - 1);
            return symbol.getSymbolkennung().equals(symbolkennung);
        }
        return false;
    }

    public Symbolkennung gebeNaechstesSymbolInnerhalbRegel(final Symbolkennung symbolkennung) {
        checkSymbolkennung(symbolkennung);
        if (!istLetztesSymbolInRegel(symbolkennung)) {
            final List<Symbol> symbole = ermittleRegel(symbolkennung);
            for (int index = 0; index < symbole.size(); index++) {
                if (symbole.get(index).getSymbolkennung().equals(symbolkennung) && (index < (symbole.size() - 1))) {
                    return symbole.get(index + 1).getSymbolkennung();
                }
            }
        }
        throw new GrammatikException();
    }

    private void macheZordnungen() {
        final Set<Symbolbezeichnung> symbolbezeichnungen = symbolregeln.keySet();
        for (final Symbolbezeichnung symbolbezeichnung : symbolbezeichnungen) {
            final Set<List<Symbol>> symbolmenge = symbolregeln.get(symbolbezeichnung);
            for (final List<Symbol> symbole : symbolmenge) {
                for (final Symbol symbol : symbole) {
                    zuordnungen.put(symbol.getSymbolkennung(), symbole);
                }
            }
        }
    }

    private void checkSymbolkennung(final Symbolkennung symbolkennung) {
        if ((symbolkennung == null) || (zuordnungen.get(symbolkennung) == null)) {
            throw new GrammatikException();
        }
    }
}
