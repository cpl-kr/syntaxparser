package de.platen.syntaxparser.parser;

public class SyntaxparserException extends RuntimeException
{

    private static final long serialVersionUID = 1L;

    public SyntaxparserException() {
    }

    public SyntaxparserException(final Throwable e) {
        super(e);
    }
}
