package de.platen.syntaxparser.klassengenerierung;

import java.util.ArrayList;
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
    private static final String MUSTER_RECORD = "package "
            + ERSETZTEIL_PAKETNAME
            + ";\n\nimport java.io.Serializable;\n\npublic record "
            + ERSETZTEIL_NAME
            + "("
            + ERSETZTEIL_PARAMETER
            + ") implements Serializable {}\n";
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
    private static final String MUSTER_HASHCODE = "    @Override\n" +
            "    public int hashCode() {\n" +
            "        return Objects.hashCode(value);\n" +
            "    }\n";
    private static final String MUSTER_TOSTRING = "    @Override\n" +
            "    public String toString() {\n" +
            "        return \"" + ERSETZTEIL_NAME + "{\" +\n" +
            "                \"value='\" + value + '\\'' +\n" +
            "                '}';\n" +
            "    }\n";
    private static final String MUSTER_KLASSE = "package "
            + ERSETZTEIL_PAKETNAME
            + ";\n\n"
            + "import java.io.Serializable;\nimport java.util.Objects;\n\npublic class "
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
    private static final String PARAMETER_DATENTYP = "String value";
    private static final String PARAMETER_POSTFIX = "_";

    private final Grammatik grammatik;

    public Klassengenerierung(final Grammatik grammatik) {
        this.grammatik = grammatik;
    }

    public Set<NameInhalt> generiere(final String paketname) {
        checkBedingungen();
        final Set<Symbolbezeichnung> symbolbezeichnungen = new HashSet<>();
        symbolbezeichnungen.addAll(this.grammatik.getRegExregeln().get().keySet());
        symbolbezeichnungen.addAll(this.grammatik.getZeichenbereichregeln().get().keySet());
        symbolbezeichnungen.addAll(this.grammatik.getZeichenmengeregeln().get().keySet());
        final Set<NameInhalt> erzeugteKlassen = new HashSet<>(erzeugeBlattklassen(paketname, symbolbezeichnungen));
        erzeugteKlassen.addAll(erzeugeBlattklassenZeichenfolge(paketname, this.grammatik.getZeichenfolgeregeln()
                                                                                        .get()));
        erzeugteKlassen.addAll(erzeugeSymbolregelnKlassen(paketname, this.grammatik.getSymbolregeln().get()));
        erzeugteKlassen.add(erzeugeSymbolregelklasse(paketname, this.grammatik.getStartregel().getSymbolbezeichnung()
                                                                              .getSymbolbezeichnung(), this.grammatik.getStartregel()
                                                                                                                     .getSymbole()));
        return erzeugteKlassen;
    }

    private void checkBedingungen() {
        this.grammatik.getSymbolregeln().get().values()
                      .forEach((list) -> {
                          if (list.size() > 1) {
                              throw new KlassengenerierungException();
                          }
                          list.forEach((symbole) -> symbole.forEach((symbol) -> {
                              if (!symbol.getKardinalitaet().equals(Kardinalitaet.GENAU_EINMAL)) {
                                  throw new KlassengenerierungException();
                              }
                          }));
                      });
        this.grammatik.getZeichenfolgeregeln().get().values()
                      .forEach((menge) -> {
                          if (menge.size() > 1) {
                              throw new KlassengenerierungException();
                          }
                          menge.forEach((zeichenfolge) -> {
                              if (zeichenfolge.getZeichenfolge().contains(" ")) {
                                  throw new KlassengenerierungException();
                              }
                              if (zeichenfolge.getZeichenfolge().contains("\t")) {
                                  throw new KlassengenerierungException();
                              }
                              if (zeichenfolge.getZeichenfolge().contains("\n")) {
                                  throw new KlassengenerierungException();
                              }
                              if (zeichenfolge.getZeichenfolge().contains("\r")) {
                                  throw new KlassengenerierungException();
                              }
                          });
                      });
    }

    private static List<NameInhalt> erzeugeBlattklassen(final String paketname,
                                                        final Set<Symbolbezeichnung> symbolbezeichnungen) {
        List<NameInhalt> erzeugteKlassen = new ArrayList<>();
        symbolbezeichnungen.forEach((symbolbezeichnung) -> erzeugteKlassen.add(erzeugeKlasse(paketname, symbolbezeichnung.getSymbolbezeichnung(), PARAMETER_DATENTYP)));
        return erzeugteKlassen;
    }

    private static List<NameInhalt> erzeugeBlattklassenZeichenfolge(final String paketname,
                                                                    final Map<Symbolbezeichnung, Set<Zeichenfolge>> zeichenfolgen) {
        List<NameInhalt> erzeugteKlassen = new ArrayList<>();
        Set<Symbolbezeichnung> symbolbezeichnungen = zeichenfolgen.keySet();
        symbolbezeichnungen.forEach((symbolbezeichnung) -> {
            Set<Zeichenfolge> zeichenfolge = zeichenfolgen.get(symbolbezeichnung);
            erzeugteKlassen.add(erzeugeKlasseZeichenfolge(paketname, symbolbezeichnung.getSymbolbezeichnung(), zeichenfolge.stream()
                                                                                                                      .toList()
                                                                                                                      .get(0)
                                                                                                                           .getZeichenfolge()));
        });
        return erzeugteKlassen;
    }

    private static List<NameInhalt> erzeugeSymbolregelnKlassen(final String paketname,
                                                               final Map<Symbolbezeichnung, List<List<Symbol>>> symbole) {
        final List<NameInhalt> erzeugteKlassen = new ArrayList<>();
        symbole.forEach((symbolbezeichnung, listen) -> erzeugteKlassen.add(erzeugeSymbolregelklasse(paketname, symbolbezeichnung.getSymbolbezeichnung(), listen.get(0))));
        return erzeugteKlassen;
    }

    private static NameInhalt erzeugeSymbolregelklasse(final String paketname,
                                                       final String symbolname,
                                                       final List<Symbol> symbole) {
        StringBuilder parameter = new StringBuilder();
        if (!symbole.isEmpty()) {
            final String symbolnameSymbol = symbole.get(0).getSymbolkennung().getSymbolbezeichnung()
                                                   .getSymbolbezeichnung();
            parameter.append(symbolnameSymbol);
            parameter.append(" ");
            parameter.append(erzeugeParameterbezeichnung(symbolnameSymbol, 1));
        }
        int nummer = 2;
        if (symbole.size() > 1) {
            for (int index = 1; index < symbole.size(); index++) {
                parameter.append(", ");
                final String symbolnameSymbol = symbole.get(index).getSymbolkennung().getSymbolbezeichnung()
                                                       .getSymbolbezeichnung();
                parameter.append(symbolnameSymbol);
                parameter.append(" ").append(erzeugeParameterbezeichnung(symbolnameSymbol, nummer));
                nummer++;
            }
        }
        return erzeugeKlasse(paketname, symbolname, parameter.toString());
    }

    private static NameInhalt erzeugeKlasse(final String paketname, final String symbolname,
                                            final String parameter) {
        final String inhalt = MUSTER_RECORD.replace(ERSETZTEIL_PAKETNAME, paketname)
                                           .replace(ERSETZTEIL_NAME, symbolname)
                                           .replace(ERSETZTEIL_PARAMETER, parameter);
        return new NameInhalt(symbolname, inhalt);
    }

    private static NameInhalt erzeugeKlasseZeichenfolge(final String paketname, final String symbolname,
                                                        final String parameter) {
        final String inhalt = MUSTER_KLASSE.replace(ERSETZTEIL_PAKETNAME, paketname)
                                           .replace(ERSETZTEIL_NAME, symbolname)
                                           .replace(ERSETZTEIL_VALUE, parameter);
        return new NameInhalt(symbolname, inhalt);
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
