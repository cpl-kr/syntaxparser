package de.platen.syntaxparser.anwendungshilfe.klassenablage;

import de.platen.syntaxparser.anwendungshilfe.klassengenerierung.PaketNameInhalt;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class KlassenablageTest {

    @Test
    void testAblegen() {
        final Resourcenbehandlung resourcenbehandlung = mock(Resourcenbehandlung.class);
        Klassenablage klassenablage = new Klassenablage(resourcenbehandlung);
        final Set<PaketNameInhalt> generierteKlassen = new HashSet<>();
        final String paket1 = "paket1.paket11";
        final String paket2 = "paket2.paket22";
        final PaketNameInhalt paketNameInhalt1 = new PaketNameInhalt(paket1, "name1", "inhalt1");
        generierteKlassen.add(paketNameInhalt1);
        final PaketNameInhalt paketNameInhalt2 = new PaketNameInhalt(paket2, "name2", "inhalt2");
        generierteKlassen.add(paketNameInhalt2);
        final String ablageverzeichnis = "test1/test2/test3";
        final String pfad1 = "pfad1";
        final String pfad2 = "pfad2";
        when(resourcenbehandlung.erstelleVerzeichnis(ablageverzeichnis, paket1.split("\\."))).thenReturn(pfad1);
        when(resourcenbehandlung.erstelleVerzeichnis(ablageverzeichnis, paket2.split("\\."))).thenReturn(pfad2);
        assertDoesNotThrow(() -> klassenablage.ablegen(generierteKlassen, ablageverzeichnis));
        verify(resourcenbehandlung).erstelleVerzeichnis(ablageverzeichnis, paket1.split("\\."));
        verify(resourcenbehandlung).erstelleVerzeichnis(ablageverzeichnis, paket2.split("\\."));
        verify(resourcenbehandlung).speichereDatei(pfad1, "name1.java", "inhalt1");
        verify(resourcenbehandlung).speichereDatei(pfad2, "name2.java", "inhalt2");
    }
}
