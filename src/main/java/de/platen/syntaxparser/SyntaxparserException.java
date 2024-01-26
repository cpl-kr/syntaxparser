package de.platen.syntaxparser;

public class SyntaxparserException extends RuntimeException
{

    private static final long serialVersionUID = 1L;

    public SyntaxparserException() {
    }

    public SyntaxparserException(final Throwable e) {
        super(e);
    }
}
