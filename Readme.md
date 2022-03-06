English version see below


--------------------------------------------------


# Ein universeller Syntaxparser für Domain spezifische Sprachen

Mit der vorliegenden Version 1.0.0 des universellen Syntaxparsers für Domain spezifische Sprachen können anhand einer definierbaren Grammatik Texte geparst werden.  
So ist man unabhängig von einer Trägersyntax (z.B. XML, JSON), um menschenlesbaren Maschinekode zu handhaben.  
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
Bei Regeln mit Symbolen darf dasselbe Symbol mehrmals auf der linken Seiute der Regel vorkommen.  
Dies bedeuetet eine Auswahl an Alternativen.  
Die erste vokommende Symbolregel wird als Startregel gwertet.  
Hier bei darf das Startsymbol nur einmal auf der linken Seite einer Regel vorkommen.  
Beispiel für Regel mit Symbolen:  
S { S1 S2 S3 S4 S5 }  
S5 { S6 S7 }  
S5 { S8 S9 }  
Beispiel für Regeln mit Wortvorgaben:  
S1 "Symbol"  
S2 [az]  
S3 (.,)  
S4 [a-zA-Z]  
Dasselbe Symbol darf sowohl Regeln für Zeichenbereiche als auch für Zeichenmengen verwendet werden.  
Dies bedeutet, dass für das entsprechende Symbol sowohl die angegebenen Zeichenbereiche als auch die angegebenen Zeichenmengen erlaubt sind.  
Beispiel:  
S1 [az]  
S1 [AZ]  
S1 (.,)   
Beispiel einer Grammatik für einfache mathematische Formeln:  
S { Operand Operationsteil }  
Operationsteil { Operator Operand }  
Operationsteil { Operator Operand Operationsteil }  
Operand [09]  
Operator (+-*/)  
Eine valide Eingabe ist "111 + 222 - 333 * 444 / 555 "  
Beispiel für den Aufbau einer Grammatik:  
<pre>
    GrammatikAufbau grammatikAufbau = new GrammatikAufbau();  
    GrammatikLesen grammatikLesen = new GrammatikLesen(grammatikAufbau);  
    String grammatikText = "...";  
    for (int index = 0; index < grammatik.length(); index++) {  
        grammatikLesen.verarbeiteZeichen(grammatikText.charAt(index));  
    }  
    grammatikLesen.checkGrammatik();  
    Grammatik grammatik = grammatikLesen.getGrammatik();  
</pre>
Zur Methode verarbeiteZeichen():  
Die Grammatik wird Zeichen für Zeichen eingelesen.  
Als Whitespaces werden " ", "\n", "\r" und "\t" gewertet.  
Das Zeichen "\" wird als Steuerzeichen gewertet, wobei das nachfolgende Zeichen als Zeichen zur weiteren Verarbeitung gewertet wird.  
So können sowohl die Whitespaces als auch die Steuerzeichen """, ")", "]", ">" und "\" als Zeichen zur weiteren Verarbeitung gewertet werden.  


## Der Parser

Der Parser ist ein Text zeichenweise einzulesen.  
Die Syntaktische Prüfung geschieht nach dem Erkennen eines Wortes.  
Beispiel für die Verwendung des Parsers:  
<pre>
    Grammatik grammatik = ....;  
    Parser parser = new Parser(grammatik);  
    String text = "...";  
    for (int index = 0; index < grammatik.length(); index++) {  
        parser.verarbeiteZeichen(text.charAt(index);  
    }  
    List<SyntaxpfadMitWort> syntaxpfadeMitWort = parser.gebeSyntaxpfadeMitWort();  
</pre>
Zur Methode verarbeiteZeichen():  
Als Whitespaces werden " ", "\n", "\r" und "\t" gewertet.  
Das Zeichen "\" wird als Steuerzeichen gewertet, wobei das nachfolgende Zeichen als Zeichen zur weiteren Verarbeitung gewertet wird.  
So können die Whitespaces und das Steuerzeichen "\" als Zeichen zur weiteren Verarbeitung gewertet werden.  
Bei dieser Version des Syntaxparsers muss nach jedem und auch nach dem letzten Wort eines zu parsenden Textes zusätzlich ein weiterer Aufruf von verarbeiteZeichen() mit einem Whitespace (ohne vorangestelltes "\") erfolgen,
da die syntaktische Prüfung wortweise stattfindet.  
Das Ergebnis kann mit der Methode gebeSyntaxpfadeMitWort() abgeholt werden.  
Man erhält eine Liste von "SyntaxpfadMitWort" mit den Methoden getSyntaxpfad() und getWort().  
Die Methode getSyntaxpfad() liefert einen Syntaxpfad mit einer Knotenfolge  und einem Blatt (also Symbolen eines Syntaxbaumes).  


--------------------------------------------------


# A universal syntax parser for domain specific languages

With the present version 1.0.0 of the universal syntax parser for domain specific languages texts can be parsed based on a definable grammar.  
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
Example for rule with symbols:  
S { S1 S2 S3 S4 S5 }  
S5 { S6 S7 }  
S5 { S8 S9 }  
Example for rules with word specifications:  
S1 "symbol"  
S2 [az]  
S3 (.,)  
S4 [a-zA-Z]  
The same symbol may be used both rules for character ranges and for character sets.  
This means that both the specified character ranges and the specified character sets are allowed for the corresponding symbol.  
Example:  
S1 [az]  
S1 [AZ]  
S1 (.,)   
Example of a grammar for simple mathematical formulas:  
S { operand operand part }  
operation part { operator operand }  
Operand part { Operator Operand Operand part }  
Operand [09]  
Operator (+-*/)  
A valid input is "111 + 222 - 333 * 444 / 555 "  
Example of how to build a grammar:  
<pre>
    GrammarConstruction grammarConstruction = new GrammarConstruction();  
    GrammarRead grammarRead = new GrammarRead(grammarConstruction);  
    String grammatikText = "...";  
    for (int index = 0; index < grammatik.length(); index++) {  
        grammatikRead.ProcessChar(grammatikText.charAt(index));  
    }  
    grammarRead.checkGrammar();  
    grammar grammar = grammarRead.getGrammar();  
</pre>
To the processcharacters() method:  
The grammar is read in character by character.  
" ", "\n", "\r" and "\t" are evaluated as whitespaces.  
The character "\" is evaluated as a control character, whereas the following character is evaluated as a character for further processing.  
Thus, both the whitespaces and the control characters """, ")", "]", ">", and "\" can be evaluated as characters for further processing.  


## The parser

The parser is to read in a text character by character.  
The syntactic check happens after the recognition of a word.  
Example of the use of the parser:  
<pre>
    grammar grammar = ....;  
    Parser parser = new Parser(grammar);  
    String text = "...";  
    for (int index = 0; index < grammatik.length(); index++) {  
        parser.processchar(text.charAt(index);  
    }  
    List<SyntaxpathWithWord> syntaxpathWithWord = parser.giveSyntaxpathWithWord();  
</pre>
To the processcharacters() method:  
" ", "\n", "\r" and "\t" are evaluated as whitespaces.  
The character "\" is evaluated as a control character, whereas the following character is evaluated as a character for further processing.  
Thus, the whitespaces and the control character "\" can be evaluated as characters for further processing.  
With this version of the syntax parser, after each and also after the last word of a text to be parsed there must be an additional call to processcharacter() with a whitespace (without preceding "\"),
because the syntactic check is done word by word.  
The result can be fetched with the method giveSyntaxpathsWithWord().  
One gets a list of "SyntaxpathWithWord" with the methods getSyntaxpath() and getWord().  
The getSyntaxpath() method returns a syntax path with a node sequence and a leaf (i.e. symbols of a syntax tree).  
