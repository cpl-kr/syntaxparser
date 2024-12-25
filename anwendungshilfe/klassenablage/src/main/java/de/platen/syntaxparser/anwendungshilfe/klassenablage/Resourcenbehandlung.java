package de.platen.syntaxparser.anwendungshilfe.klassenablage;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.file.FileSystems;

import static java.util.Objects.requireNonNull;

public class Resourcenbehandlung {

    public String erstelleVerzeichnis(final String zielverzeichnis, final String[] unterverzeichnisse) {
        requireNonNull(zielverzeichnis);
        requireNonNull(unterverzeichnisse);
        File verzeichnis = new File(zielverzeichnis);
        if (verzeichnis.exists() && verzeichnis.isDirectory()) {
            final String  fileSeparator = FileSystems.getDefault().getSeparator();
            StringBuilder stringBuilder = new StringBuilder(zielverzeichnis);
            for (String s : unterverzeichnisse) {
                stringBuilder.append(fileSeparator);
                stringBuilder.append(s);
                final String pfad = stringBuilder.toString();
                verzeichnis = new File(pfad);
                if (!verzeichnis.exists()) {
                    if (!new File(pfad).mkdir()) {
                        throw new KlassenablageException();
                    }
                } else {
                    if (verzeichnis.isFile()) {
                        throw new KlassenablageException();
                    }
                }
            }
            return stringBuilder.toString();
        } else {
            throw new KlassenablageException();
        }
    }

    public void speichereDatei(final String pfad, final String dateiname, final String inhalt) {
        requireNonNull(pfad);
        requireNonNull(dateiname);
        requireNonNull(inhalt);
        final String datei = pfad + FileSystems.getDefault().getSeparator() + dateiname;
        File file = new File(datei);
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream);
            Writer writer = new BufferedWriter(outputStreamWriter);
            writer.write(inhalt);
            writer.close();
            outputStreamWriter.close();
            fileOutputStream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
