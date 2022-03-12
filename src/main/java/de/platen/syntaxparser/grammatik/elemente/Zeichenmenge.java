package de.platen.syntaxparser.grammatik.elemente;

import java.io.Serializable;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;

import de.platen.syntaxparser.grammatik.GrammatikException;

public class Zeichenmenge implements Serializable
{

    private static final long serialVersionUID = 1L;

    private final Set<Character> zeichenmenge;

    public Zeichenmenge(final Set<Character> zeichenmenge) {
        if ((zeichenmenge == null) || zeichenmenge.isEmpty()) {
            throw new GrammatikException();
        }
        this.zeichenmenge = zeichenmenge;
    }

    public Set<Character> getZeichenmenge() {
        return Collections.unmodifiableSet(zeichenmenge);
    }

    @Override
    public int hashCode() {
        return Objects.hash(zeichenmenge);
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if ((obj == null) || (getClass() != obj.getClass())) {
            return false;
        }
        final Zeichenmenge other = (Zeichenmenge) obj;
        return isEqualsSet(zeichenmenge, other.zeichenmenge);
    }

    private boolean isEqualsSet(final Set<?> set1, final Set<?> set2) {
        if ((set1 == null) || (set2 == null) || (set1.size() != set2.size())) {
            return false;
        }
        return set1.containsAll(set2);
    }
}
