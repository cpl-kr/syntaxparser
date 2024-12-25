package de.platen.syntaxparser.anwendungshilfe.klassenablage;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.file.FileSystems;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ResourcenbehandlungTest {

    private static final String HAUPTVERZEICHNIS = "src" + FileSystems.getDefault().getSeparator() + "test" + FileSystems.getDefault().getSeparator() + "resources";
    private static final String[] UNTERVERZEICHNISSE = { "Test", "Neu"};
    private static final String DATEI_SPEICHERN_ZIELVERZEICHNIS = HAUPTVERZEICHNIS + FileSystems.getDefault().getSeparator() + "Test3";
    private static final String DATEI_SPEICHERN_DATEI = "test.txt";

    private Resourcenbehandlung resourcenbehandlung;

    @BeforeEach
    void init() {
        this.resourcenbehandlung = new Resourcenbehandlung();
    }

    @AfterEach
    void bereinigen() {
        new File(DATEI_SPEICHERN_ZIELVERZEICHNIS + FileSystems.getDefault().getSeparator() + DATEI_SPEICHERN_DATEI).delete();
        new File(HAUPTVERZEICHNIS + FileSystems.getDefault().getSeparator() + "Test1" + FileSystems.getDefault().getSeparator() + "Test" + FileSystems.getDefault().getSeparator() + "Neu").delete();
        new File(HAUPTVERZEICHNIS + FileSystems.getDefault().getSeparator() + "Test1" + FileSystems.getDefault().getSeparator() + "Test").delete();
    }

    @Test
    void testErstelleVerzeichnisZielverzeichnisExistiertNicht() {
        assertThrows(KlassenablageException.class, () -> this.resourcenbehandlung.erstelleVerzeichnis(HAUPTVERZEICHNIS + "/Test0", UNTERVERZEICHNISSE));
    }

    @Test
    void testErstelleVerzeichnisZielverzeichnisIstDatei() {
        assertThrows(KlassenablageException.class, () -> this.resourcenbehandlung.erstelleVerzeichnis(HAUPTVERZEICHNIS + "/Test", UNTERVERZEICHNISSE));
    }

    @Test
    void testErstelleVerzeichnisUnterverzeichnisIstDatei() {
        assertThrows(KlassenablageException.class, () -> this.resourcenbehandlung.erstelleVerzeichnis(HAUPTVERZEICHNIS + "/Test2", UNTERVERZEICHNISSE));
    }

    @Test
    void testErstelleVerzeichnis() {
        assertDoesNotThrow(() -> this.resourcenbehandlung.erstelleVerzeichnis(HAUPTVERZEICHNIS + "/Test1", UNTERVERZEICHNISSE));
        File file = new File(HAUPTVERZEICHNIS + FileSystems.getDefault().getSeparator() + "Test1" + FileSystems.getDefault().getSeparator() + "Test" + FileSystems.getDefault().getSeparator() + "Neu");
        assertTrue(file.exists());
        assertTrue(file.isDirectory());
    }

    @Test
    void testSpeichereDatei() throws IOException {
        final String inhalt = "inhalt";
        assertDoesNotThrow(() -> this.resourcenbehandlung.speichereDatei(DATEI_SPEICHERN_ZIELVERZEICHNIS, DATEI_SPEICHERN_DATEI, inhalt));
        File file = new File(DATEI_SPEICHERN_ZIELVERZEICHNIS + FileSystems.getDefault().getSeparator() + DATEI_SPEICHERN_DATEI);
        assertTrue(file.exists());
        assertTrue(file.isFile());
        FileInputStream fileInputStream = new FileInputStream(file);
        InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
        Reader reader = new BufferedReader(inputStreamReader);
        char[] chars = new char[(int) file.length()];
        reader.read(chars, 0, (int) file.length());
        assertEquals(inhalt, new String(chars));
    }
}
