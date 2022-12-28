package de.platen.syntaxparser.syntaxpfad;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import de.platen.syntaxparser.SyntaxparserException;
import de.platen.syntaxparser.grammatik.elemente.Symbolkennung;

public class Syntaxpfad
{

    private final List<Symbolkennung> knoten;
    private Symbolkennung blatt;
    private boolean istFertig;

    private Syntaxpfad(final List<Symbolkennung> knoten, final Symbolkennung blatt, final boolean istFertig) {
        this.knoten = knoten;
        this.blatt = blatt;
        this.istFertig = istFertig;
    }

    public Syntaxpfad() {
        knoten = new ArrayList<>();
        blatt = null;
        istFertig = false;
    }

    public Syntaxpfad kopiere() {
        return new Syntaxpfad(knoten, blatt, istFertig);
    }

    public Syntaxpfad kopiereMitNeuerWurzel(final Symbolkennung symbolkennung) {
        if (!istFertig || (symbolkennung == null)) {
            throw new SyntaxparserException();
        }
        final List<Symbolkennung> knotenkopie = new ArrayList<>();
        knotenkopie.add(symbolkennung);
        knotenkopie.addAll(knoten);
        return new Syntaxpfad(knotenkopie, blatt, istFertig);
    }

    public void zufuegenKnoten(final Symbolkennung symbol) {
        if (istFertig || (symbol == null)) {
            throw new SyntaxparserException();
        }
        knoten.add(symbol);
    }

    public void zufuegenBlatt(final Symbolkennung symbolkennung) {
        if (istFertig || (symbolkennung == null)) {
            throw new SyntaxparserException();
        }
        blatt = symbolkennung;
        istFertig = true;
    }

    public List<Symbolkennung> gebeKnotenfolge() {
        return Collections.unmodifiableList(knoten);
    }

    public Symbolkennung gebeBlatt() {
        if (istFertig) {
            return blatt;
        }
        throw new SyntaxparserException();
    }

    public boolean istFertig() {
        return istFertig;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public int hashCode() {
        return Objects.hash(blatt, istFertig, knoten);
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if ((obj == null) || (getClass() != obj.getClass())) {
            return false;
        }
        final Syntaxpfad other = (Syntaxpfad) obj;
        return Objects.equals(blatt, other.blatt) && (istFertig == other.istFertig)
                && Objects.equals(knoten, other.knoten);
    }

    @Override
    public String toString() {
        final StringBuilder stringBuilder = new StringBuilder();
        final String leerzeichen = "    ";
        stringBuilder.append("Syntaxpfad\n");
        stringBuilder.append(leerzeichen);
        stringBuilder.append("Knoten");
        stringBuilder.append("\n");
        for (final Symbolkennung symbolkennung : knoten) {
            stringBuilder.append(leerzeichen);
            stringBuilder.append(leerzeichen);
            stringBuilder.append(symbolkennung);
            stringBuilder.append("\n");
        }
        stringBuilder.append(leerzeichen);
        stringBuilder.append("Blatt");
        stringBuilder.append("\n");
        stringBuilder.append(leerzeichen);
        stringBuilder.append(leerzeichen);
        if (blatt != null) {
            stringBuilder.append(blatt);
        } else {
            stringBuilder.append("null");
        }
        stringBuilder.append("\n");
        stringBuilder.append(leerzeichen);
        stringBuilder.append("IstFertig");
        stringBuilder.append("\n");
        stringBuilder.append(leerzeichen);
        stringBuilder.append(leerzeichen);
        stringBuilder.append(istFertig);
        stringBuilder.append("\n");
        return stringBuilder.toString();
    }
}
