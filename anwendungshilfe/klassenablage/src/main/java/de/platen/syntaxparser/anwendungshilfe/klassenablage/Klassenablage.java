package de.platen.syntaxparser.anwendungshilfe.klassenablage;

import de.platen.syntaxparser.anwendungshilfe.klassengenerierung.PaketNameInhalt;

import java.util.Set;

public class Klassenablage {

    private static final String DATEIENDUNG = ".java";

    private final Resourcenbehandlung resourcenbehandlung;

    public Klassenablage(final Resourcenbehandlung resourcenbehandlung) {
        this.resourcenbehandlung = resourcenbehandlung;
    }

    public void ablegen(final Set<PaketNameInhalt> generierteKlassen, final String ablageverzeichnis) {
        generierteKlassen.forEach((paketNameInhalt) -> {
            final String pfad = this.resourcenbehandlung.erstelleVerzeichnis(ablageverzeichnis, paketNameInhalt.paketname().split("\\."));
            this.resourcenbehandlung.speichereDatei(pfad, paketNameInhalt.name() + DATEIENDUNG, paketNameInhalt.inhalt());
        });
    }
}
