package de.platen.syntaxparser.grammatik.elemente;

import java.util.Objects;

import de.platen.syntaxparser.grammatik.GrammatikException;

public class RegEx
{

    private final String regex;

    public RegEx(final String regex) {
        if ((regex == null) || regex.isEmpty()) {
            throw new GrammatikException();
        }
        this.regex = regex;
    }

    public String getRegex() {
        return regex;
    }

    @Override
    public int hashCode() {
        return Objects.hash(regex);
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if ((obj == null) || (getClass() != obj.getClass())) {
            return false;
        }
        final RegEx other = (RegEx) obj;
        return Objects.equals(regex, other.regex);
    }
}
