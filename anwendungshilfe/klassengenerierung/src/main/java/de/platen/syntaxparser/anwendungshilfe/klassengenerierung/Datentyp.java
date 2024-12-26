package de.platen.syntaxparser.anwendungshilfe.klassengenerierung;

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

    public String getTyp() {
        return typ;
    }

    public static Datentyp of(final String typ) {
        if (typ == null || typ.isBlank()) {
            return null;
        }
        Datentyp[] datentypen = Datentyp.values();
        for (Datentyp datentyp : datentypen) {
            if (datentyp.typ.equals(typ)) {
                return datentyp;
            }
        }
        return null;
    }
}
