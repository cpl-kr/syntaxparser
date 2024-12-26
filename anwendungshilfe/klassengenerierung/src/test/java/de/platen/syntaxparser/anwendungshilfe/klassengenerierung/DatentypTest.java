package de.platen.syntaxparser.anwendungshilfe.klassengenerierung;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class DatentypTest {

    @Test
    void testOf() {
        assertNull(Datentyp.of("nichts"));
        assertNull(Datentyp.of(""));
        assertNull(Datentyp.of(" "));
        assertEquals(Datentyp.INTEGER, Datentyp.of("Integer"));
    }
}
