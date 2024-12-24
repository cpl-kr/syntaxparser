package de.platen.syntaxparser.anwendungshilfe.klassengenerierung;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.platen.syntaxparser.grammatik.Grammatik;
import de.platen.syntaxparser.grammatik.elemente.Kardinalitaet;
import de.platen.syntaxparser.grammatik.elemente.Symbol;
import de.platen.syntaxparser.grammatik.elemente.Symbolbezeichnung;
import de.platen.syntaxparser.grammatik.elemente.Zeichenfolge;

public class Klassengenerierung {

    private static final String ERSETZTEIL_PAKETNAME = "<PAKETNAME>";
    private static final String ERSETZTEIL_NAME = "<NAME>";
    private static final String ERSETZTEIL_PARAMETER = "<PARAMETER>";
    private static final String ERSETZTEIL_VALUE = "<VALUE>";
    private static final String ERSETZTEIL_IMPLEMENTS = "<IMPLEMENTS>";
    private static final String ERSETZTEIL_IMPORT = "<IMPORT>";
    private static final String MUSTER_RECORD = "package "
            + ERSETZTEIL_PAKETNAME
            + ";\n\nimport java.io.Serializable;\nimport java.util.List;\n\npublic record "
            + ERSETZTEIL_NAME
            + "("
            + ERSETZTEIL_PARAMETER
            + ") implements Serializable {}\n";
    private static final String MUSTER_RECORD_HAUPTKLASSE = "package "
            + ERSETZTEIL_PAKETNAME
            + ";\n\nimport java.io.Serializable;\nimport java.util.List;\n\nimport <IMPORT>;\n\npublic record "
            + ERSETZTEIL_NAME
            + "("
            + ERSETZTEIL_PARAMETER
            + ") implements Serializable {}\n";
    private static final String MUSTER_RECORD_PARAMETERKLASSE = "package "
            + ERSETZTEIL_PAKETNAME
            + ";\n\nimport java.io.Serializable;\nimport java.util.List;\n\n<IMPORT>\n\npublic record "
            + ERSETZTEIL_NAME
            + "("
            + ERSETZTEIL_PARAMETER
            + ") implements Serializable, <IMPLEMENTS> {}\n";
    private static final String TAB = "    ";
    private static final String MUSTER_MEMBER = TAB + "private final String value = \"" + ERSETZTEIL_VALUE + "\";\n";
    private static final String MUSTER_GETTER = TAB + "public String value() {\n" +
            "        return value;\n" +
            "    }\n";
    private static final String MUSTER_EQUALS = "    @Override\n" +
            "    public boolean equals(Object o) {\n" +
            "        if (!(o instanceof " + ERSETZTEIL_NAME + " " + ERSETZTEIL_NAME + ")) return false;\n" +
            "        return Objects.equals(value, " + ERSETZTEIL_NAME + ".value);\n" +
            "    }\n";
    private static final String MUSTER_HASHCODE = """
                @Override
                public int hashCode() {
                    return Objects.hashCode(value);
                }
            """;
    private static final String MUSTER_TOSTRING = "    @Override\n" +
            "    public String toString() {\n" +
            "        return \"" + ERSETZTEIL_NAME + "{\" +\n" +
            "                \"value='\" + value + '\\'' +\n" +
            "                '}';\n" +
            "    }\n";
    private static final String MUSTER_KLASSE = "package "
            + ERSETZTEIL_PAKETNAME
            + ";\n\n"
            + "import java.io.Serializable;\nimport java.util.List;\nimport java.util.Objects;\n\npublic class "
            + ERSETZTEIL_NAME
            + " implements Serializable {\n\n"
            + MUSTER_MEMBER
            + "\n"
            + MUSTER_GETTER
            + "\n"
            + MUSTER_EQUALS
            + "\n"
            + MUSTER_HASHCODE
            + "\n"
            + MUSTER_TOSTRING
            + "}\n";
    private static final String MUSTER_INTERFACE = "package <PAKETNAME>;\n" +
            "\n" +
            "public interface <NAME> {}\n";
    private static final String PARAMETER_DATENTYP = "String value";
    private static final String PARAMETER_NAME = "value";
    private static final String PARAMETER_POSTFIX = "_";
    private static final String INTERFACE_POSTFIX = "Interface";

    private final Grammatik grammatik;
    private final Map<String, Datentyp> abbildungDatentypen;

    public Klassengenerierung(final Grammatik grammatik) {
        this.grammatik = requireNonNull(grammatik);
        this.abbildungDatentypen = new HashMap<>();
    }

    public Klassengenerierung(final Grammatik grammatik, final Map<String, Datentyp> abbildungDatentypen) {
        this.grammatik = requireNonNull(grammatik);
        this.abbildungDatentypen = requireNonNull(abbildungDatentypen);
    }

    public Set<PaketNameInhalt> generiere(final String paketname) {
        checkBedingungen();
        final Set<Symbolbezeichnung> symbolbezeichnungen = new HashSet<>();
        symbolbezeichnungen.addAll(this.grammatik.getRegExregeln().get().keySet());
        symbolbezeichnungen.addAll(this.grammatik.getZeichenbereichregeln().get().keySet());
        symbolbezeichnungen.addAll(this.grammatik.getZeichenmengeregeln().get().keySet());
        final Set<PaketNameInhalt> erzeugteKlassen = new HashSet<>(erzeugeBlattklassen(paketname, symbolbezeichnungen, this.abbildungDatentypen));
        erzeugteKlassen.addAll(erzeugeBlattklassenZeichenfolge(paketname, this.grammatik.getZeichenfolgeregeln()
                                                                                        .get()));
        erzeugteKlassen.addAll(erzeugeSymbolregelnKlassen(paketname, this.grammatik.getSymbolregeln().get()));
        erzeugteKlassen.add(erzeugeSymbolregelklasse(paketname, this.grammatik.getStartregel().getSymbolbezeichnung()
                                                                              .getSymbolbezeichnung(), this.grammatik.getStartregel()
                                                                                                                     .getSymbole()));
        return erzeugteKlassen;
    }

    private void checkBedingungen() {
        this.grammatik.getZeichenfolgeregeln().get().values()
                      .forEach((menge) -> {
                          if (menge.size() > 1) {
                              throw new KlassengenerierungException();
                          }
                      });
    }

    private static List<PaketNameInhalt> erzeugeBlattklassen(final String paketname,
                                                             final Set<Symbolbezeichnung> symbolbezeichnungen,
                                                             final Map<String, Datentyp> datentypen) {
        final List<PaketNameInhalt> erzeugteKlassen = new ArrayList<>();
        symbolbezeichnungen.forEach((symbolbezeichnung) -> {
            String parameter = PARAMETER_DATENTYP;
            Datentyp datentyp = datentypen.get(symbolbezeichnung.getSymbolbezeichnung());
            if (datentyp != null) {
                parameter = datentyp.getTyp() + " " + PARAMETER_NAME;
            }
            erzeugteKlassen.add(erzeugeKlasse(paketname, symbolbezeichnung.getSymbolbezeichnung(), parameter));
        });
        return erzeugteKlassen;
    }

    private static List<PaketNameInhalt> erzeugeBlattklassenZeichenfolge(final String paketname,
                                                                         final Map<Symbolbezeichnung, Set<Zeichenfolge>> zeichenfolgen) {
        final List<PaketNameInhalt> erzeugteKlassen = new ArrayList<>();
        final Set<Symbolbezeichnung> symbolbezeichnungen = zeichenfolgen.keySet();
        symbolbezeichnungen.forEach((symbolbezeichnung) -> {
            final Set<Zeichenfolge> zeichenfolge = zeichenfolgen.get(symbolbezeichnung);
            erzeugteKlassen.add(erzeugeKlasseZeichenfolge(paketname, symbolbezeichnung.getSymbolbezeichnung(), zeichenfolge.stream()
                                                                                                                           .toList()
                                                                                                                           .get(0)
                                                                                                                           .getZeichenfolge()));
        });
        return erzeugteKlassen;
    }

    private static List<PaketNameInhalt> erzeugeSymbolregelnKlassen(final String paketname,
                                                                    final Map<Symbolbezeichnung, List<List<Symbol>>> symbole) {
        final List<PaketNameInhalt> erzeugteKlassen = new ArrayList<>();
        symbole.forEach((symbolbezeichnung, listen) -> {
            final String symbolname = symbolbezeichnung.getSymbolbezeichnung();
            if (listen.size() == 1) {
                erzeugteKlassen.add(erzeugeSymbolregelklasse(paketname, symbolname, listen.get(0)));
            } else {
                if (listen.size() > 1) {
                    final String interfacename = symbolname + INTERFACE_POSTFIX;
                    final String unterpaketname = paketname + "." + paketname.replace(".", "") + symbolname;
                    erzeugteKlassen.add(erzeugeInterface(unterpaketname, interfacename));
                    erzeugteKlassen.add(erzeugeHauptklasse(paketname, symbolname, interfacename + " " + PARAMETER_NAME, unterpaketname + "." + interfacename));
                    int nummer = 1;
                    for (List<Symbol> symbolliste : listen) {
                        final String symbolnameAlternativ = symbolname + nummer;
                        erzeugteKlassen.add(erzeugeSymbolregelparameterklasse(unterpaketname, symbolnameAlternativ, symbolliste, paketname, interfacename));
                        nummer++;
                    }
                }
            }
        });
        return erzeugteKlassen;
    }

    private static PaketNameInhalt erzeugeSymbolregelklasse(final String paketname,
                                                            final String symbolname,
                                                            final List<Symbol> symbole) {
        final StringBuilder parameter = new StringBuilder();
        if (!symbole.isEmpty()) {
            final String symbolnameSymbol = symbole.get(0).getSymbolkennung().getSymbolbezeichnung()
                                                   .getSymbolbezeichnung();
            if (symbole.get(0).getKardinalitaet().equals(Kardinalitaet.GENAU_EINMAL)) {
                parameter.append(symbolnameSymbol);
            } else {
                parameter.append("List<");
                parameter.append(symbolnameSymbol);
                parameter.append(">");
            }
            parameter.append(" ");
            parameter.append(erzeugeParameterbezeichnung(symbolnameSymbol, 1));
        }
        int nummer = 2;
        if (symbole.size() > 1) {
            for (int index = 1; index < symbole.size(); index++) {
                parameter.append(", ");
                final String symbolnameSymbol = symbole.get(index).getSymbolkennung().getSymbolbezeichnung()
                                                       .getSymbolbezeichnung();
                if (symbole.get(index).getKardinalitaet().equals(Kardinalitaet.GENAU_EINMAL)) {
                    parameter.append(symbolnameSymbol);
                } else {
                    parameter.append("List<");
                    parameter.append(symbolnameSymbol);
                    parameter.append(">");
                }
                parameter.append(" ").append(erzeugeParameterbezeichnung(symbolnameSymbol, nummer));
                nummer++;
            }
        }
        return erzeugeKlasse(paketname, symbolname, parameter.toString());
    }

    private static PaketNameInhalt erzeugeSymbolregelparameterklasse(final String unterpaketname,
                                                                     final String symbolname,
                                                                     final List<Symbol> symbole,
                                                                     final String paketname,
                                                                     final String zusatzImplements) {
        final StringBuilder parameter = new StringBuilder();
        if (!symbole.isEmpty()) {
            final String symbolnameSymbol = symbole.get(0).getSymbolkennung().getSymbolbezeichnung()
                                                   .getSymbolbezeichnung();
            if (symbole.get(0).getKardinalitaet().equals(Kardinalitaet.GENAU_EINMAL)) {
                parameter.append(symbolnameSymbol);
            } else {
                parameter.append("List<");
                parameter.append(symbolnameSymbol);
                parameter.append(">");
            }
            parameter.append(" ");
            parameter.append(erzeugeParameterbezeichnung(symbolnameSymbol, 1));
        }
        int nummer = 2;
        final StringBuilder zusatzImport = new StringBuilder();
        if (symbole.size() > 1) {
            zusatzImport.append("import ").append(paketname).append(".")
                        .append(symbole.get(0).getSymbolkennung().getSymbolbezeichnung().getSymbolbezeichnung())
                        .append(";\n");
            for (int index = 1; index < symbole.size(); index++) {
                parameter.append(", ");
                final String symbolnameSymbol = symbole.get(index).getSymbolkennung().getSymbolbezeichnung()
                                                       .getSymbolbezeichnung();
                String einzelimport = "import " + paketname + "." + symbolnameSymbol + ";\n";
                zusatzImport.append(einzelimport);
                if (symbole.get(index).getKardinalitaet().equals(Kardinalitaet.GENAU_EINMAL)) {
                    parameter.append(symbolnameSymbol);
                } else {
                    parameter.append("List<");
                    parameter.append(symbolnameSymbol);
                    parameter.append(">");
                }
                parameter.append(" ").append(erzeugeParameterbezeichnung(symbolnameSymbol, nummer));
                nummer++;
            }
        }
        return erzeugeParameterKlasse(unterpaketname, symbolname, parameter.toString(), zusatzImport.toString(), zusatzImplements);
    }

    private static PaketNameInhalt erzeugeKlasse(final String paketname, final String symbolname,
                                                 final String parameter) {
        final String inhalt = MUSTER_RECORD.replace(ERSETZTEIL_PAKETNAME, paketname)
                                           .replace(ERSETZTEIL_NAME, symbolname)
                                           .replace(ERSETZTEIL_PARAMETER, parameter);
        return new PaketNameInhalt(paketname, symbolname, inhalt);
    }

    private static PaketNameInhalt erzeugeHauptklasse(final String paketname, final String symbolname,
                                                      final String parameter, final String zusatzImport) {
        final String inhalt = MUSTER_RECORD_HAUPTKLASSE.replace(ERSETZTEIL_PAKETNAME, paketname)
                                                       .replace(ERSETZTEIL_NAME, symbolname)
                                                       .replace(ERSETZTEIL_PARAMETER, parameter)
                                                       .replace(ERSETZTEIL_IMPORT, zusatzImport);
        return new PaketNameInhalt(paketname, symbolname, inhalt);
    }

    private static PaketNameInhalt erzeugeParameterKlasse(final String paketname, final String symbolname,
                                                          final String parameter, final String zusatzImport,
                                                          final String zusatzImplements) {
        final String inhalt = MUSTER_RECORD_PARAMETERKLASSE.replace(ERSETZTEIL_PAKETNAME, paketname)
                                                           .replace(ERSETZTEIL_NAME, symbolname)
                                                           .replace(ERSETZTEIL_PARAMETER, parameter)
                                                           .replace(ERSETZTEIL_IMPORT, zusatzImport)
                                                           .replace(ERSETZTEIL_IMPLEMENTS, zusatzImplements);
        return new PaketNameInhalt(paketname, symbolname, inhalt);
    }

    private static PaketNameInhalt erzeugeKlasseZeichenfolge(final String paketname, final String symbolname,
                                                             final String parameter) {
        final String inhalt = MUSTER_KLASSE.replace(ERSETZTEIL_PAKETNAME, paketname)
                                           .replace(ERSETZTEIL_NAME, symbolname)
                                           .replace(ERSETZTEIL_VALUE, parameter);
        return new PaketNameInhalt(paketname, symbolname, inhalt);
    }

    private static PaketNameInhalt erzeugeInterface(final String paketname, final String symbolname) {
        final String inhalt = MUSTER_INTERFACE.replace(ERSETZTEIL_PAKETNAME, paketname)
                                              .replace(ERSETZTEIL_NAME, symbolname);
        return new PaketNameInhalt(paketname, symbolname, inhalt);
    }

    private static String erzeugeParameterbezeichnung(final String parameter, final int nummer) {
        if (parameter.length() > 1) {
            String ersterBuchstabe = parameter.substring(0, 1).toLowerCase();
            return ersterBuchstabe + parameter.substring(1) + PARAMETER_POSTFIX + nummer;
        } else {
            return parameter.toLowerCase();
        }
    }
}
