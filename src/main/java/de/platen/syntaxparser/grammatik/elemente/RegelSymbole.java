package de.platen.syntaxparser.grammatik.elemente;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import de.platen.syntaxparser.grammatik.GrammatikException;

public class RegelSymbole extends Regel implements Serializable
{

    private static final long serialVersionUID = 1L;

    private final List<Symbol> symbole;

    public RegelSymbole(final Symbolbezeichnung symbol, final List<Symbol> symbole) {
        super(symbol);
        if ((symbole == null) || symbole.isEmpty()) {
            throw new GrammatikException();
        }
        this.symbole = Collections.unmodifiableList(symbole);
    }

    public List<Symbol> getSymbole() {
        return symbole;
    }

    public boolean istLetztesSymbol(final Symbolkennung symbolkennung) {
        checkSymbolkennung(symbolkennung);
        if (symbole.size() > 0) {
            final Symbol symbol = symbole.get(symbole.size() - 1);
            return symbol.getSymbolkennung().equals(symbolkennung);
        }
        return false;
    }

    public boolean istInRegel(final Symbolkennung symbolkennung) {
        if (symbolkennung == null) {
            throw new GrammatikException();
        }
        for (final Symbol symbol : symbole) {
            if (symbol.getSymbolkennung().equals(symbolkennung)) {
                return true;
            }
        }
        return false;
    }

    public Symbolkennung gebeNaechstesSymbol(final Symbolkennung symbolkennung) {
        checkSymbolkennung(symbolkennung);
        if (!istLetztesSymbol(symbolkennung)) {
            for (int index = 0; index < symbole.size(); index++) {
                if (symbole.get(index).getSymbolkennung().equals(symbolkennung) && (index < (symbole.size() - 1))) {
                    return symbole.get(index + 1).getSymbolkennung();
                }
            }
        }
        throw new GrammatikException();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        final int result = super.hashCode();
        return (prime * result) + Objects.hash(symbole);
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (!super.equals(obj) || (getClass() != obj.getClass())) {
            return false;
        }
        final RegelSymbole other = (RegelSymbole) obj;
        return Objects.equals(symbole, other.symbole);
    }

    private void checkSymbolkennung(final Symbolkennung symbolkennung) {
        if ((symbolkennung == null) || !istInRegel(symbolkennung)) {
            throw new GrammatikException();
        }
    }

}
