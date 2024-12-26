package de.platen.syntaxparser.anwendungshilfe.mavenplugin.kodegenerator;

import de.platen.syntaxparser.anwendungshilfe.klassenablage.Klassenablage;
import de.platen.syntaxparser.anwendungshilfe.klassenablage.Resourcenbehandlung;
import de.platen.syntaxparser.anwendungshilfe.klassengenerierung.Datentyp;
import de.platen.syntaxparser.anwendungshilfe.klassengenerierung.Klassengenerierung;
import de.platen.syntaxparser.anwendungshilfe.klassengenerierung.PaketNameInhalt;
import de.platen.syntaxparser.grammatik.Grammatik;
import de.platen.syntaxparser.grammatik.GrammatikAufbau;
import de.platen.syntaxparser.grammatik.GrammatikLesen;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Mojo(name = "kodegenerator", defaultPhase = LifecyclePhase.VALIDATE)
public class Kodegenerator extends AbstractMojo {

    @Parameter(property = "grammatikdatei")
    String grammatikdatei;

    @Parameter(property = "paketname")
    String paketname;

    @Parameter(property = "datentypen")
    String datentypen;

    @Parameter(property = "ablageverzeichnis")
    String ablageverzeichnis;

    @Override
    public void execute() {
        getLog().info("Grammatikdatei: " + this.grammatikdatei);
        getLog().info("Paketname: " + this.paketname);
        getLog().info("Datentypabbildungen: " + this.datentypen);
        getLog().info("Ablageverzeichnis: " + this.ablageverzeichnis);
        Map<String, Datentyp> mapping = erstelleDatentypabbildungen(this.datentypen);
        final Grammatik grammatik;
        try {
            grammatik = erstelleGrammatik(this.grammatikdatei);
            final Set<PaketNameInhalt> generierteKlassen = generiereKlassen(grammatik, this.paketname, mapping);
            speichereKlassen(generierteKlassen, this.ablageverzeichnis);
        } catch (IOException e) {
            getLog().error(e);
        }
    }

    private static Map<String, Datentyp> erstelleDatentypabbildungen(final String mapping) {
        Map<String, Datentyp> datentypabbildungen = new HashMap<>();
        String[] teile = mapping.split(";");
        for (String s : teile) {
            String[] teil = s.split(":");
            datentypabbildungen.put(teil[0], Datentyp.of(teil[1]));
        }
        return datentypabbildungen;
    }

    private static Grammatik erstelleGrammatik(final String dateiname) throws IOException {
        GrammatikAufbau grammatikAufbau = new GrammatikAufbau();
        GrammatikLesen grammatikLesen = new GrammatikLesen(grammatikAufbau);
        File file = new File(dateiname);
        FileInputStream fileInputStream = new FileInputStream(file);
        InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
        Reader reader = new BufferedReader(inputStreamReader);
        int c;
        do {
            c = reader.read();
            grammatikLesen.verarbeiteZeichen((char) c);
        } while (c != -1);
        grammatikLesen.checkGrammatik();
        return grammatikLesen.getGrammatik();
    }

    private static Set<PaketNameInhalt> generiereKlassen(final Grammatik grammatik, final String paketname, final Map<String, Datentyp> mapping) {
        final Klassengenerierung klassengenerierung = new Klassengenerierung(grammatik, mapping);
        return klassengenerierung.generiere(paketname);
    }

    private static void speichereKlassen(final Set<PaketNameInhalt> generierteKlassen, final String ablageverzeichnis) {
        final Resourcenbehandlung resourcenbehandlung = new Resourcenbehandlung();
        final Klassenablage klassenablage = new Klassenablage(resourcenbehandlung);
        klassenablage.ablegen(generierteKlassen, ablageverzeichnis);
    }
}
