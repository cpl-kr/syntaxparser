English version see below


--------------------------------------------------


# Ein universeller Syntaxparser für Domain spezifische Sprachen

Mit der vorliegenden Version des universellen Syntaxparsers für Domain spezifische Sprachen können anhand einer definierbaren Grammatik Texte geparst werden.
So ist man unabhängig von einer Trägersyntax (z.B. XML, JSON), um menschenlesbaren Maschinenkode zu handhaben.
Um den Parser zu verwenden, muss eine Grammatik in einem vorgegebenen Format erstellt werden.
Mit dieser Grammatik kann ein Text syntaktisch geprüft werden.
Bei erfolgreicher Prüfung erhält man eine Folge der eingegebenen Worte des Textes zusammen mit den zugehörigen Symbolen der Grammatik (von der Wurzel des Syntaxbaumes aus).


## Die Grammatik

Die Grammatik besteht aus Regeln mit Symbolen und Regeln mit Vorgaben für Worte:
Regeln mit Symbolen haben die Form <Symbolbezeichnung> "{" <Folge von Symbolbezeichnungen> "}"
Regeln mit Wortvorgaben sind für die Spezifizierung von Schlüsselworten,
Zeichenbereichen, Zeichenmengen und regulären Ausdrücken vorgesehen (d.h. Blätter eines Syntaxbaumes).
Regeln mit Wortvorgaben haben die Form <Symbolbezeichnung> """ <Schlüsselwort> """
oder die Form <Symbolbenzeichnung> "[" <2 Zeichen für Zeichenbereich> "\]"
oder die Form <Symbolbezeichnung> "(" <Folge von einzelnen Zeichen> ")"
oder die Form <Symbolbezeichnung> "<" <Regulärer Ausdruck> ">"
Bei Regeln mit Symbolen darf dasselbe Symbol mehrmals auf der linken Seite der Regel vorkommen.
Dies bedeutet eine Auswahl an Alternativen.
Die erste vorkommende Symbolregel wird als Startregel gewertet.
Hierbei darf das Startsymbol nur einmal auf der linken Seite einer Regel vorkommen.
Beispiel für Regel mit Symbolen:\
S { S1 S2 S3 S4 S5 }\
S5 { S6 S7 }\
S5 { S8 S9 }\
Beispiel für Regeln mit Wortvorgaben:\
S1 "Symbol"\
S2 [az]\
S3 (.,)\
S4 <[a-zA-Z]>\
Dasselbe Symbol darf sowohl Regeln für Zeichenbereiche als auch für Zeichenmengen verwendet werden.
Dies bedeutet, dass für das entsprechende Symbol sowohl die angegebenen Zeichenbereiche als auch die angegebenen Zeichenmengen erlaubt sind.
Beispiel:\
S1 [az]\
S1 [AZ]\
S1 (.,)\
Beispiel einer Grammatik für einfache mathematische Formeln:\
S { Operand Operationsteil }\
Operationsteil { Operator Operand }\
Operationsteil { Operator Operand Operationsteil }\
Operand [09]\
Operator "+"\
Operator "-"\
Operator "*"\
Operator "/"\
Eine valide Eingabe ist "111 + 222 - 333 * 444 / 555".\
Beispiel für den Aufbau einer Grammatik:
<pre>
    GrammatikAufbau grammatikAufbau = new GrammatikAufbau();
    GrammatikLesen grammatikLesen = new GrammatikLesen(grammatikAufbau);
    String grammatikText = "...";
    for (int index = 0; index < grammatikText.length(); index++) {
        grammatikLesen.verarbeiteZeichen(grammatikText.charAt(index));
    }
    grammatikLesen.checkGrammatik();
    Grammatik grammatik = grammatikLesen.getGrammatik();
</pre>
Zur Methode verarbeiteZeichen():\
Die Grammatik wird Zeichen für Zeichen eingelesen.
Als Whitespaces werden " ", "\n", "\r" und "\t" gewertet.
Das Zeichen "\" wird als Steuerzeichen gewertet, wobei das nachfolgende Zeichen als Zeichen zur weiteren Verarbeitung gewertet wird.
So können sowohl die Whitespaces als auch die Steuerzeichen """, ")", "]", ">" und "\" als Zeichen zur weiteren Verarbeitung gewertet werden.\
Zur Methode checkGrammatik():\
Die Methode prüft die Grammatik auf Konsistenz und wirft eine GrammatikException, wenn ein Fehler bei der Prüfung erkannt wird.\
Zur Methode checkGrammatikStrikt():\
Die Methode führt eine einschränkende Prüfung durch und wirft eine GrammatikException, wenn ein Fehler bei der Prüfung erkannt wird.
Die Einschränkungen hierbei sind: Es darf nicht mehrere Symbolregeln zu einem Symbol geben und
ein Symbbol einer Regel darf nicht bei derselben Regel links und rechts stehen.\
Die KLasse Grammatik ist serialisierbar.


## Alternative Möglichkeiten mit den Grammatikregeln für '+' und '*' nach Art eines regulären Ausdrucks

Möglichkeit für '+':
Beispiel für '+' nicht am Ende einer Regel:
R -> A+ B C entspricht R -> A Z C
Z -> A Z
Z -> A B
Beispiel für '+' am Ende einer Regel:
R -> B A+ entspricht R -> B Z
Z -> B A
Z -> B Z

Möglichkeit für '*':
Beispiel für '*' nicht am Ende einer Regel:
A* B C -> Z C
Z -> A B
Z -> B
Beispiel für '*' am Ende einer Regel:
B A* -> Z
Z -> B
Z -> B A


## Der Parser

Der Parser ist ein Text zeichenweise einzulesen.
Beispiel für die Verwendung des Parsers:
<pre>
    Grammatik grammatik = ....;
    Parser parser = new Parser(grammatik);
    String text = "...";
    for (int index = 0; index < text.length(); index++) {
        parser.verarbeiteZeichen(text.charAt(index);
    }
    List<SyntaxpfadMitWort> syntaxpfadeMitWort = parser.ermittleSyntaxpfadeMitWort(true);
</pre>
Zur Methode verarbeiteZeichen():\
Diese Methode gibt einen boolschen Wert zurück, true im Falle, dass das Zeichen verarbeitet werden konnte.\
Als Whitespaces werden " ", "\n", "\r" und "\t" gewertet.
Das Zeichen "\" wird als Steuerzeichen gewertet, wobei das nachfolgende Zeichen als Zeichen zur weiteren Verarbeitung gewertet wird.
So können die Whitespaces und das Steuerzeichen "\" als Zeichen zur weiteren Verarbeitung gewertet werden.
Das Ergebnis kann mit der Methode ermittleSyntaxpfadeMitWort(boolean alsAbschluss) abgeholt werden.
Das boolsche Flag "alsAbschluss" dient zur Unterscheidung, ob die Methode zum Schluss des Parsens oder als Zwischenstand aufgerufen wird.
Wird diese Methode als Zwischenstand aufgerufen, so muss vorher die Methode "verarbeiteZeichen" mit einem Whitespace aufgerufen worden sein.
Man erhält eine Liste von "SyntaxpfadMitWort" mit den Methoden getSyntaxpfad() und getWort().
Die Methode getSyntaxpfad() liefert einen Syntaxpfad mit einer Knotenfolge und einem Blatt (also Symbolen eines Syntaxbaumes), was einer Grammatikregel entspricht.


## Behandlung unterschiedlicher Versionen einer Grammatik

Wenn mit der Zeit die Grammatik geändert werden soll und
die sich daraus ergebene neue Version der Grammatik nicht abwärtskompatibel sein soll,
dann kann als Vereinbarung die erste Zeile des zu parsenden Textes die Versionsnummer darstellen.
Alternativ kann die Versionsnummer über alle Versionen der Grammatik den immer gleichen Bestandteil zu Beginn der Grammatik sein.\
Beispiel:\
S {Version Restgrammatik}\
Version [09]\
Restgrammatik ....


## Behandlung von Binärdaten

Um bei diversen Schnittstellen Binärdaten zu übertragen und
diese Binärdaten Bestandteil der Grammatik sein sollen,
kann man diese im Hex-Format oder als Base64 übertragen.
Alternativ kann man Referenzen angeben und
über eine weitere Schnittstellenmethode mit der Referenz als Parameter direkt erhalten.\
Beispiel einer Grammatikregel für das Hex-Format:\
Hex [af]\
Hex [AF]\
Hex [09]\
Beispiel einer Grammatikregel für Base64:\
Base64 [AZ]\
Base64 [az]\
Base64 [09]\
Base64 (+=)\
Beispiel einer Grammatikregel für eine Referenz (hier UUID):\
Referenz <[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}>


## Anwendungsbereiche des Parsers

Der Parser kann allgemein für Schnittstellen verwendet werden,
bei denen Daten in menschenlesbarer Form vorliegen sollen.
Auch kann er für Non-SQL Datenbanken verwendet werden.
Hierbei kann jede Datenentität durch eine Menge von Grammatikregeln dargestellt werden.


## Anwendungshilfe


### Klassengenerierung

Um bei Service-Schnittstellen nicht nur Text, sondern auch serialisierte Java-Objekte für die Service-Domäne zu behandeln,
gibt es die Möglichkeit der Klassengenerierung.
Anhand einer Grammatik können Records und Klassen erzeugt werden.
Jede Regel führt zu einem Record oder einer Klasse.

Beispiel für die Klassengenerierung:\
<pre>
    Grammatik grammatik = ....;
    Klassengenerierung klassengenerierung = new Klassengenerierung(grammatik);
    Set<PaketNameInhalt> generierteKlassen = klassengenerierung.generiere("paket");
</pre>

Beispiel für die Klassengenerierung mit einem Datentyp eines Konstruktorparameters:\
<pre>
    Grammatik grammatik = ....;
    HashMap<String, Datentyp> datentypabbildungen = new HashMap<>();
    datentypabbildungen.put("S2", Datentyp.INTEGER);
    Klassengenerierung klassengenerierung = new Klassengenerierung(grammatik, datentypabbildungen);
    Set<PaketNameInhalt> generierteKlassen = klassengenerierung.generiere("paket");
</pre>
Hierbei werden die Symbolregeln, welche zu Datenwerten führen mit einem Typ versehen (z.B. public record S2(Integer value){}).
In der Enumeration Datentyp sind die möglichen Datentypen aufgeführt.

Zur Methode generiere() der Klasse Klassengenerierung:\
Der Parameter ist der Paketname, in dem die erzeugten Records und Methoden zusammengefasst sind.
Als Ergebnis erhält man eine Menge an erzeugten Records und Klassen in Textform mit dem jeweils zugehörigem Namen der Regel,
welche auch dem Namen des Records oder der Klasse entspricht.
Diese Records und Klassen könne in einem weiteren Schritt abgespeichert werden.


### Klassenablage

Um die generierten Klassen entsprechend der Verzeichnisstruktur von Paketnamen abzulegen, gibt es die Möglichkeit der Klassenablage mit der Methode "ablegen":
<pre>
    final Resourcenbehandlung resourcenbehandlung = ....; // Erstellung eines Verzeichnis entsprechend der Verzeichnisstruktur von Paketnamen und Speichern der Dateien
    Klassenablage klassenablage = new Klassenablage(resourcenbehandlung);
    ablegen(final Set<PaketNameInhalt> generierteKlassen, final String ablageverzeichnis);
</pre>


### Maven Plugin für Klassengenerierung mit der Klassenablage

Das Maven Plugin "klassengenerator-maven-plugin" vereinigt Klassengenerierung und Ablage der generierten Klassen.
Verwendung in einer Pom-Datei:
<pre>
            <plugin>
                <groupId>de.platen.syntaxparser.anwendungshilfe</groupId>
                <artifactId>klassengenerator-maven-plugin</artifactId>
                <version>1.0.0</version>
                <executions>
                    <execution>
                        <goals>
                            <goal>kodegenerator</goal>
                        </goals>
                    </execution>
                </executions>
                <configuration>
                    <grammatikdatei>src/main/resources/Testgrammatik.txt</grammatikdatei>
                    <paketname>de.platen.demo</paketname>
                    <datentypen>Operator:Integer;Test:Long</datentypen>
                    <ablageverzeichnis>src/main/java</ablageverzeichnis>
                </configuration>
            </plugin>

</pre>
Hierbei sind als Parameter anzugeben:
* Die Grammatikdatei mit der darin enthaltenen Grammatik
* Der Paketname, unter dem die Klassen generiert werden sollen
* Die Datentypen für die Parametertypen einzelner Klassen
* Das Ablageverzeichnis, in die erzeugten Klasen abgespeichert werden


### Script zum Bauen

Mit dem Script "bauen.sh" werden die jar-Dateien (Syntaxparser, Klassengenerierung, Klassenablage, Maven Plugin) im lokalen Repository zur Verfügung gestellt.


--------------------------------------------------


# A universal syntax parser for domain specific languages

With the present version of the universal syntax parser for domain specific languages texts can be parsed based on a definable grammar.
Thus one is independent of a carrier syntax (e.g. XML, JSON) to handle human readable machine code.
To use the parser, a grammar must be created in a given format.
This grammar can be used to syntactically check a text.
If the check is successful, a sequence of the entered words of the text is obtained together with the corresponding symbols of the grammar (from the root of the syntax tree).


## The grammar

The grammar consists of rules with symbols and rules with specifications for words:
Rules with symbols are of the form <symbol name> "{" <sequence of symbol names> "}"
Rules with word defaults are for specifying keywords,
character ranges, character sets, and regular expressions (i.e., leaves of a syntax tree).
Rules with word defaults have the form <symbol name> """ <keyword> """
or the form <symbol designation> "[" <2 characters for character range> "\]"
or the form <symbol designation> "(" <sequence of single characters> ")"
or the form <symbol name> "<" <regular expression> ">".
For rules with symbols, the same symbol may appear more than once on the left-hand side of the rule.
This means a choice of alternatives.
The first appearing symbol rule is evaluated as start rule.
Here the start symbol may occur only once on the left side of a rule.
Example for rule with symbols:\
S { S1 S2 S3 S4 S5 }\
S5 { S6 S7 }\
S5 { S8 S9 }\
Example for rules with word specifications:\
S1 "symbol"\
S2 [az]\
S3 (.,)\
S4 <[a-zA-Z]>\
The same symbol may be used both rules for character ranges and for character sets.
This means that both the specified character ranges and the specified character sets are allowed for the corresponding symbol.
Example:\
S1 [az]\
S1 [AZ]\
S1 (.,)\
Example of a grammar for simple mathematical formulas:\
S { Operand Operandpart }\
operationpart { Operator Operand }\
Operandpart { Operator Operand Operandpart }\
Operand [09]\
Operator "+"\
Operator "-"\
Operator "*"\
Operator "/"\
A valid input is "111 + 222 - 333 * 444 / 555".\
Example of how to build a grammar:\
<pre>
    GrammatikAufbau grammatikAufbau = new GrammatikAufbau();
    GrammatikLesen grammatikLesen = new GrammatikLesen(grammatikAufbau);
    String grammatikText = "...";
    for (int index = 0; index < grammatikText.length(); index++) {
        grammatikLesen.verarbeiteZeichen(grammatikText.charAt(index));
    }
    grammatikLesen.checkGrammatik();
    Grammatik grammatik = grammatikLesen.getGrammatik();
</pre>
To the verarbeiteZeichen() method:\
The grammar is read in character by character.
" ", "\n", "\r" and "\t" are evaluated as whitespaces.
The character "\" is evaluated as a control character, whereas the following character is evaluated as a character for further processing.
Thus, both the whitespaces and the control characters """, ")", "]", ">", and "\" can be evaluated as characters for further processing.\
To the checkGrammatik() method:\
The method checks the grammar for consistency and throws a GrammarException if an error is detected during the check.\
To the checkGrammatikStrikt() method:\
The method performs a more restrictive check and throws a GrammarException if an error is detected during the check.
The restrictions here are: There must not be more than one symbol rule for a symbol and
a symbol of a rule must not be on the left and on the right of the same rule.\
The class Grammatik ist serializable.


## Alternative possibilities with the grammar rules for '+' and '*' in the manner of a regular expression

Possibility for '+':
Example for '+' not at the end of a rule:
R -> A+ B C corresponds to R -> A Z C
Z -> A Z
Z -> A B
Example for '+' at the end of a rule:
R -> B A+ corresponds to R -> B Z
Z -> B A
Z -> B Z

Possibility for '*':
Example for '*' not at the end of a rule:
A* B C -> Z C
Z -> A B
Z -> B
Example for '*' at the end of a rule:
B A* -> Z
Z -> B
Z -> B A


## The parser

The parser is to read in a text character by character.
Example of the use of the parser:
<pre>
    Grammatik grammatik = ....;
    Parser parser = new Parser(grammatik);
    String text = "...";
    for (int index = 0; index < text.length(); index++) {
        parser.verarbeiteZeichen(text.charAt(index);
    }
    List<SyntaxpfadMitWort> syntaxpfadeMitWort = parser.ermittleSyntaxpfadeMitWort(true);
</pre>
To the verarbeiteZeichen() method\:
This method returns a boolean value, true in the case that the ponding could be processed.\
" ", "\n", "\r" and "\t" are evaluated as whitespaces.
The character "\" is evaluated as a control character, whereas the following character is evaluated as a character for further processing.
Thus, the whitespaces and the control character "\" can be evaluated as characters for further processing.
The result can be fetched with the method ermittleSyntaxpfadeMitWort(boolean asTerminationOfParsing).
The boolean flag "alsAbschluss" serves to distinguish whether the method is called at the end of the parsing or as an intermediate state.
If this method is called as an intermediate state, the method "processcharacters" must have been called with a whitespace beforehand.
One gets a list of "SyntaxpathWithWord" with the methods getSyntaxpath() and getWord().
The getSyntaxpath() method returns a syntax path with a node sequence and a leaf (i.e. symbols of a syntax tree), which corresponds to a grammar rule.


## Handling different versions of a grammar

If, over time, the grammar is to be changed and
the resulting new version of the grammar should not be downward compatible,
then the first line of the text to be parsed can represent the version number as an agreement.
Alternatively, the version number can always be the same component at the beginning of the grammar across all versions of the grammar.\
Example:\
S {Version Restgrammatik}\
Version [09]\
Restgrammatik ....


## Handling binary data

In order to transmit binary data with various interfaces and
this binary data should be part of the grammar,
you can transmit them in hex format or as Base64.
Alternatively, one can specify references and
directly via another interface method with the reference as a parameter.\
Example of a grammar rule for the hex format:\
Hex [af]\
Hex [AF]\
Hex [09]\
Example of a grammar rule for Base64:\
Base64 [AZ]\
Base64 [az]\
Base64 [09]\
Base64 (+=)\
Example of a grammar rule for a reference (here UUID):\
Reference <[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}>


## Applications of the parser

The parser can be used generally for interfaces,
where data should be available in human-readable form.
It can also be used for non-SQL databases.
Here, each data entity can be represented by a set of grammar rules.


## Application aid


### Class generation

To handle not only text but also serialized Java objects for the service domain in service interfaces,
there is the option of class generation.
Records and classes can be generated using a grammar.
Each rule leads to a record or a class.

Example for class generation:\
<pre>
    Grammatik grammatik = ....;
    Klassengenerierung klassengenerierung = new Klassengenerierung(grammatik);
    Set<PaketNameInhalt> generierteKlassen = klassengenerierung.generiere("paket");
</pre>

Example of class generation with a datatyp of a constructor parameter:\
<pre>
    Grammatik grammatik = ....;
    HashMap<String, Datentyp> datentypabbildungen = new HashMap<>();
    datentypabbildungen.put("S2", Datentyp.INTEGER);
    Klassengenerierung klassengenerierung = new Klassengenerierung(grammatik, datentypabbildungen);
    Set<PaketNameInhalt> generierteKlassen = klassengenerierung.generiere("paket");
</pre>
The symbol rules that lead to data values are assigned a type (e.g. public record S2(Integer value){}).
The possible data types are listed in the data type enumeration.

For the generate() method of the class Klassengenerierung:\
The parameter is the package name in which the generated records and methods are summarized.
The result is a set of generated records and classes in text form with the corresponding name of the rule,
which also corresponds to the name of the record or class.
These records and classes can be saved in a further step.


### Class storage

To store the generated classes according to the directory structure of package names, there is the option of storing classes using the “store” method:
<pre>
    final Resourcenbehandlung resourcenbehandlung = ....; // Erstellung eines Verzeichnis entsprechend der Verzeichnisstruktur von Paketnamen und Speichern der Dateien
    Klassenablage klassenablage = new Klassenablage(resourcenbehandlung);
    ablegen(final Set<PaketNameInhalt> generierteKlassen, final String ablageverzeichnis);
</pre>


### Maven plugin for class generation and storage of generated classes

The Maven plugin “klassengenerator-maven-plugin” combines class generation and storage of the generated classes.
Use in a Pom file:
<pre>
            <plugin>
                <groupId>de.platen.syntaxparser.anwendungshilfe</groupId>
                <artifactId>klassengenerator-maven-plugin</artifactId>
                <version>1.0.0</version>
                <executions>
                    <execution>
                        <goals>
                            <goal>kodegenerator</goal>
                        </goals>
                    </execution>
                </executions>
                <configuration>
                    <grammatikdatei>src/main/resources/Testgrammatik.txt</grammatikdatei>
                    <paketname>de.platen.demo</paketname>
                    <datentypen>Operator:Integer;Test:Long</datentypen>
                    <ablageverzeichnis>src/main/java</ablageverzeichnis>
                </configuration>
            </plugin>

</pre>
The following parameters must be specified:
* The grammar file with the grammar it contains
* The package name under which the classes are to be generated
* The data types for the parameter types of individual classes
* The storage directory in which the generated classes are to be saved


### Script for building

The jar files (syntax parser, class generation, class storage, Maven plugin) are made available in the local repository with the script “bauen.sh”.
