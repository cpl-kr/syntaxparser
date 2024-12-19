package de.platen.syntaxparser.klassengenerierung;

public enum Datentyp {

    INTEGER("Integer"),
    LONG("Long"),
    FLOAT("Float"),
    DOUBLE("Double"),
    BYTES("byte[]");

    final private String typ;

    Datentyp(final String typ) {
        this.typ = typ;
    }

    String getTyp() {
        return typ;
    }
}
