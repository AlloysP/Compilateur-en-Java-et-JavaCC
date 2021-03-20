# Compilateur

## Membres du projet 
* Alloÿs PETIT - AlloysP
* Maÿlis MONTANI - Mayisu7798

## Run le projet
### 1- Avec le terminal (System.in)
Taper `make` dans le terminal va compiler et lancer le projet en capturant ce qui est écrit dans le terminal. Puisque notre langage ignore les retours à la ligne il ne peut pas détecter les `EOL` et donc pour délimiter la saisie il faut ajouter à la fin de la ligne un `@EOF@` qui est interprété par le programme comme un `EOF` classique.

Exemple de code valide pouvant être entré au terminal: `print(1+1)@EOF@`

### 2- Avec un ou plusieurs fichiers .txt
Taper `make MesProgrammes` dans le terminal va compiler et lancer le projet en lui donnant en entrée tous les fichiers, préalablement écrits, présents dans le dossier `Programmes/MesProgrammes` ou présents dans un des sous-dossiers.

### 3- Avec la suite de tests
Taper `make check` dans le terminal va compiler et lancer le projet en lui donnant en entrée tous les fichiers présents dans les dossiers `Programmes/tests/good` et `Programmes/tests/wrong`

# Compte Rendu

## Introduction 

L'objectif de ce projet était de construire un interpréteur et un pretty-printer pour un language simple. Ce projet permet ainsi de mettre en pratique bon nombre de concepts abordés lors de la partie front-end d'un compilateur:
* La définition d'un parseur et d'un lexeur au travers de Javacc (https://javacc.org)
* La mise en place d'un AST pour représenter les différents éléments du langage
* La mise en place d'un pretty-printer permettant de s'assurer que les étapes de parsing et de scanning sont correctes
* La mise en place d'un évaluateur permettant d'interpréter le code fourni en entrée
* La mise en place de test et le packaging de l'application

La syntaxe du langage que nous devons implémenter s'apparente à celle du langage `Tiger`, présenté sur le site de l'Epita, à l'exception du switch qui est un ajout de notre part. (https://assignments.lrde.epita.fr/reference_manual/tiger_language_reference_manual/tiger_language_reference_manual.html).

## Ce que nous avons fait

* Make marche : Notre projet peut se compiler à l'aide de la commande `make`, dès lors que l'on se trouve dans le dossier principal.

* Make check marche : Notre projet inclus une série de tests qu'il est possible de lancer en utlisant la commande `make check` dans le compilateur, dans les mêmes conditions que précédemment.

* Entiers avec les bonnes priorités : Nous avons créé une classe `Num` qui définit les nombres de notre langage, et qui dans notre cas sont des `Doubles`. La classe `BinopAST` permet de manipuler ces nombres avec les opérateurs arithmétiques classiques. Les règles de calcul sont respectées grâce aux règles de grammaires définies dans le `JaggerCC.jj`.

* Primitive Print : Nous avons écrit une classe `Print` permettant d'utiliser la commande `print` dans notre langage. Le test indiqué fonctionne correctement.

* Support de if-then-else : Nous avons écrit une classe `IfThenElse` permettant d'utiliser cette commande. La commande fonctionne avec et sans le else (optionnel). Les tests indiqués fonctionnent correctement.

* Support des chaînes de caractère : Nous avons créé un type `TigerString` définissant les `String` de notre langage. Il est possible d'effectuer diverses opérations sur ce type telles que la concaténation et les comparaisons. Pour les comparaisons de type ">", "<", ">=" et "<=", nous opérons une comparaison lexicale. Les tests indiqués fonctionnent comme prévu.

* Support des variables et des scopes : Nous avons créé une classe `LetInEnd` qui inclut une `HashMap` de portée courante, dans laquelle on définit les variables à utiliser sur une portée définie. Cette classe contient également une liste d'expressions à exécuter dans le `in`. Les tests indiqués fonctionnent comme prévus.

* Affectation : Nous avons créé une classe `Affectation` qui affecte une valeur à la variable dans la `HashMap` la plus proche dans l'AST (en allant de parent en parent) et contenant cette variable. Les tests indiqués fonctionnent comme prévus.

* Support de While : Nous avons créé une classe `While` fonctionnelle comprenant une `Expression` de condition (qui doit retourner un entier 0 ou 1), et une liste d'expressions à exécuter dans le `do`. Les tests indiqués fonctionnent comme prévus.

* Support de For : Nous avons créé une classe `For` fonctionnelle contenant une liste d'expressions à exécuter pour chacune des variables de la boucle. Les tests indiqués fonctionnent comme prévus.

* Commentaires imbriqués : Nous avons implémenté les commentaires similaire à ceux du langage Tiger, ceux-ci pouvant être imbriqués et sont délimité par /* */.

* Support de Switch : Nous avons créé une classe `Switch` qui permet d'effectuer des `switch ... case ... default ...`.




## Description des fichiers

### Makefile
Le `Makefile` contient quatre modes d'exécutions : `make` qui lance le programme par défaut, `make check` qui lance le programme avec tous les exemples répertoriés dans les dossiers `tests`, `make MesProgrammes` qui lance les programmes situés dans le dossier `MesProgrammes` ainsi que tous ses sous-dossiers, et enfin `make clear` qui nettoie les fichiers produits par Java après une exécution.


### JaggerCC.jj

La fonction principale est écrite en Java entre deux mots clés `PARSER_BEGIN(Jagger)` et `PARSER_END(Jagger)`. Il s'agit d'une classe `Jagger` contenant deux fonctions `main`, l'une ne prenant pas d'argument (main par défaut) et lis l'entrée au clavier puis exécute la `mainloop`, et l'autre prenant en argument un fichier (`File`), qui lit le fichier et lance la `mainloop`. Ces fonctions seront appelées par la classe `Start` dont nous parlerons plus tard.

Nous définissons ensuite les caractères à ignorer lors de la lecture de nos lignes comme par exemple `\n`, les espaces, `\r` ou `\t`, mais également les commentaires de type `/* */`. Dans le langage Tiger donné comme exemple, nous avons remarqué que celui-ci gérait les commentaires de type `/* */` imbriqués. Nous avons donc fait en sorte d'ignorer les commentaires imbriqués en prenant en compte la profondeur de ceux-ci grâce à l'indicateur `commentDepth`. L'intervalle a ignorer pour les commentaires prends donc fin lorque l'indicateur `commentDepth` vaut 0.

Nous définissons également les `token` c'est à dire les caractères clés dans la lecture de notre fichier. Nous retrouvons par exemple les caractères `switch`, `case`, `for`, `to`, `while`, `do`, `var`, `let`, `in`, `end`, `if`, `then`, `else`, `print`, les nombres et les lettres... qui définissent ensemble les expressions de notre langage. Nous avons également ajouté un signe `@EOF@`, caractère d'échappement pour la lecture de nos fichiers tests. Celui-ci permet d'indiquer artificiellement la fin du fichier et donc de pouvoir écrire tous nos tests dans les mêmes fichiers.

Il est à noter que l'ordre d'enchaînement des fonctions qui vont suivre est important pour la priorisation des `Expressions`. Ces fonctions représentent en fait des règles écrites en JavaCC. Nous les avons écrite dans l'ordre des règles les plus prioritaires vers les moins prioritaires.

#### Fonctions du fichier JaggerCC.jj
`@fonction mainloop()` : La fonction `mainloop()` crée une instance d'`Expression` à l'aide de la fonction `expressionExec()`, initialise le parent de cette instance puis appelle la fonction `treatThisAST` de la classe `Start` sur l'instance. Elle répète ceci jusqu'à atteindre la fin du fichier.

`@fonction expressionExec()` : Définit le comportement des expressions exécutables qui ne retournent pas de valeurs, comme le `Print` par exemple. Dans ce cas, si le caractère `print` apparait au cours du parcours du fichier, alors on crée un `opérateurOR` pour ce qui est à l'intérieur des parenthèses et on `return` une instance de `Print(e1)`. Si nous ne trouvons pas le caractère `print` alors on `return` uniquement un `operateurOR`.

`@fonction operateurOR()` : Définit le comportement des expressions "ou" caractérisées par le sybole `|`. Il y a dans cet opérateur un membre gauche, duquel on crée une instance comme étant un `operateurAND` et éventuellement 0, 1 ou plusieurs `operateurAND` qui suivent. Si c'est le cas, alors on modifie l'instance précédente en `BinopAST("OR", e1, e2)`. Dans tous les cas, on retourne cette instance.

`@fonction operateurAND()` : De même que pour `operateurOR` mais définit le comportement des expressions "et" caractérisé par le symbole `&`. L'instance `e1` sera une instance de `comparaison`, éventuellement remplacée par une `BinopAST("AND", e1, e2)`.

`@fonction comparaison()` : Définit le comportement des expressions de comparaison telles que "<>" (différent), "<=", ">=", "<", ">" et "=". On commence par créer une instance de `expressionV` qu'on `push` dans une `stackExpr` puis on peut éventuellement trouver un ou plusieurs symboles de comparaison. Pour chacun, on `push` le signe de la comparaison dans la `stackSigne` puis on `push` l'expression suivante dans `stackExpr`. Si on trouve un ou plusieurs signes de comparaison, on retourne alors le résultat de la fonction `JaggerCComplement.arrayToComparateurs(stackSigne, stackExpr)`, et sinon on retourne `e1`.

`@fonction expressionV()` : Désigne les opérations "+" et "-" (moins prioritaires). On commence par créer une instance de `term` (membre gauche éventuellement). On peut ensuite trouver 0, 1 ou plusieurs symboles "+" et "-" pour lesquels on va créer une deuxième expression `term` (membre droit éventuellement) et remplacer `e1` par une `BinopAST("PLUS", e1, e2)` ou bien une `BinopAST("MOINS", e1, e2)`. On `return` ensuite `e1`.

`@fonction term()` : Désigne les opérations "*" et "/" (prioritaires). On commence par créer une instance de `expressionExecValue` (membre de gauche éventuellement). On peut ensuite trouver 0, 1 ou plusieurs symboles "*" et "/" pour lesquels on va créer une deuxième expression `expressionExecValue` (membre droit éventuellement) et remplacer `e1` par une `BinopAST("MULTI", e1, e2)` ou bien par une `BinopAST("DIV", e1, e2)`. On return ensuite `e1`.

`@fonction expressionExecValue()` : Désigne les opérateurs plus complexes tels que les expressions `LET IN END`, `SWITCH CASE ELSE END`, `IF THEN ELSE`, `WHILE DO` et `FOR TO DO`. Pour chacune de ces possibilités on a des comportements différents respectant une certaine syntaxe. Chacune des expressions entre chaque mots clés devra être instanciée de façon à respecter cette syntaxe, ça peut être par exemple une `declarationVariables`, `listeExpression`, `operateurOR`... et pour chacun des cas, on retournera une instance de l'expression choisie (dans nos classes écrites en Java, dont on parlera plus tard). Si nous ne tombons pas sur une de ces expressions, nous retournons une instance de `factor`.

`@fonction factor()` : Désigne les facteurs positifs ou négatifs (nombres), les chaines de caractères ou bien les expressions entre parenthèses simples. Il s'agit de l'expression la plus prioritaire car elle désigne les types de base de notre langage, à savoir les `Num` et les `TigerString`, mais aussi les expressions prioritaires entre parenthèses. Dans chacun des cas, on retourne une instance des classes en Java du type correspondant.

`@fonction declarationVariables()` : Désigne les déclarations de variables de type `Affectations`. Il est possible d'en avoir plusieurs à la suite. Dans cette fonction, on va alors créer deux listes contenant d'une part le nom des variables et d'autre part l'expression lui étant associée. On retourne une instance de `ListeNamesAndExpressions(listeToTreatNames, listeToTreatExpressions)`.

`@fonction listeExpression()` : Désigne les listes d'expressions enchainées séparées par des ";" soit dans une séquence soit dans un Let In End. Dans chacun des deux cas, on crée une `listeExpr` contenant chacune des expressions puis on la retourne.


### Les Fichiers en Java


#### Classe JaggerCComplement
Cette classe contient du code en Java normalement présent dans le fichier `JaggerCC.jj`. Elle ne contient qu'une seule fonction, et est non instanciée.

##### Fonction propre à JaggerCComplement
`@fonction arrayToComparateurs()` : Cette fonction prend en entrée une liste de la forme `(a) (signe comparateur) (b) (signe comparateur) (c) (signe comparateur)` (cette liste a été divisée en 2 stacks car il n'y a que 2 types différents), et elle retourne un AST en lisant la liste de droite à gauche (`JaggerCC.jj` lis de gauche à droite) afin d'avoir une liste de comparateurs dont les membres à gauche sont prioritaires (voir des exemples dans les fichiers tests pour mieux comprendre).



#### Enum EnumType
Il s'agit d'une `Enum` contenant les différents types possibles pour nos `Expressions`. Cette `Enum` sera notamment utilisée pour définir les types contenus dans l'attribut `myType` des `Expressions`. Elle contient les types `Num`, `String` et `Void`.





#### Classe Start
Il s'agit de la classe permettant de démarrer notre interpréteur.

##### Fonctions propres à Start
`@fonction main()` : Permet de lancer le programme par défaut s'il n'y a pas d'argument en entrée du main. Le programme se contentera alors de lancer la fonction `Jagger.main()` et de retourner les éventuelles erreurs. Si la fonction est lancée avec un argument en entrée désignant un nom du dossier contenant les fichiers tests, alors elle lance la fonction `Jagger.main(vFile)` pour chacun des fichiers dans ce dossier.

`@fonction treatThisAST()` : Fonction appelée par le `Parser` une fois que celui-ci a terminé de créer l'AST (qui est donné en paramètre de la fonction). Affiche le `prettyPrint()` de cette `Expression` puis exécute l'`Expression` et affiche son résultat.

`@fonction listf()` : Prends en entrée un nom de dossier et une liste de fichiers. Permet d'ajouter chacun des fichier à traiter dans un dossier à une liste.








### Les classes en Java relatives à Expression



#### Classe Abstraite Expression
Cette classe abstraite permet de lister les différentes fonctions nécessaires aux différents types d'expressions pour interragir entre elles. 

Une `Expression` peut être une valeur ou bien un exécutable, ou bien les deux. C'est pour cela que nous avons besoin que cette classe soit abstraite afin de différentier les comportements des différentes expressions possibles. Cette classe est donc faite pour réunir à la fois les types simples (`Num`, `TygerString`, `NullExpression`), mais aussi les types un peu plus élaborés (`Variable`), les fonctions simples (`Affectation`, `Print`, `BinopAST` désignant les opérations binaires simples, `Sequences` désignant les séquences d'instructions) ainsi que les blocs plus complexes (`IfThenElse`, `For`, `While`, `LetInEnd`, `Switch`). 

##### Attributs
`@attribut Expression myParent` : Initialisé à `null` au début. Désigne le parent de l'expression courante, c'est à dire la portée dans laquelle est contenue l'expression courante.

`@attribut EnumType myType` : Permet de connaitre le type de l'expression courante. Peut valoir `EnumType.NUM`, `EnumType.STRING` ou `EnumType.VOID`.

##### Fonctions
`@fonction prettyPrint()` : Permet d'afficher proprement l'expression courante. Cet affichage dépend de l'expression courante et nécessite donc d'être redéfinie par les classes qui héritent d'`Expression`.

`@fonction execute()` : Permet d'exécuter l'expression si celle-ci est exécutable, si elle n'est pas exécutable celle-ci va généralement se contenter d'exécuter les Expressions en-dessous d'elle dans l'AST (s'il y en a). Elle dépend donc de l'expression courante et doit être refédinie par les classes qui héritent d'`Expression`.

`@fonction initialiseTypes()` : Permet d'initialiser l'attribut `myType`. L'initialisation de cet attribut dépend de l'expression courante. Elle doit donc être redéfinie par les classes qui héritent d'`Expression`.

`@fonction intialiseParentsPuisTypes()` : Permet d'initialiser l'attribut `myParent` puis l'attribut `myType`. L'initialisation de ces attributs dépendent de l'expression courante. Elle doit donc être redéfinie par les classes qui héritent d'`Expression`.

`@fonction setParent()` : Setter sur l'attribut `myParent`.

`@fonction getParent()` : Getter sur l'attribut `myParent`.

`@fonction getNum()` : Getter sur la valeur si `myType` est un `Num`. Dépend de l'expression courante et doit être refédinie par les classes qui héritent d'`Expression`. Elle peut engendrer une exception si l'expression courante n'est pas du bon type.

`@fonction getString()` : Getter sur la valeur si `myType` est une `String`. Dépend de l'expression courante et doit être refédinie par les classes qui héritent d'`Expression`. Elle peut engendrer une exception si l'expression courante n'est pas du bon type.

`@fonction getType()` : Getter sur le type de l'expression courante. Renvoie l'attribut `myType`.

`@fonction getVariableHM()` : Getter sur la variable HashMap définissant toutes les variables de la portée courante, si l'expression en possède une. Dépend de l'expression courante et doit donc être redéfinie par les classes héritant d'`Expression`.

`@fonction hasVariableHashMap()` : Initialisée à `false`. Indique si l'expression courante possède ou non une HashMap définissant toutes les variables d'une portée. Elle dépend de l'expression et devra donc être redéfinie si l'expression possède une HashMap comme attribut.







#### Classe Num
La classe `Num` hérite de la classe `Expression`. Il s'agit des expressions étant des nombres (`double`). 

Cette classe est basique et représente une valeur (`double`). 

##### Attributs propres
`@attribut double number` : Valeur non fixée (non `final`) de notre `Num`.

##### Fonctions redéfinies

`@fonction prettyPrint()` : Prend en compte le fait qu'un nombre puisse être négatif et l'affiche en conséquence avec ou sans parenthèses.

`@fonction getNum()` : Retourne l'attribut `number`.

`@fonction initialiseTypes()` : Initialise l'attribut `myType` à `EnumType.NUM`. 

`@fonction initialiseParentsPuisTypes()` 

`@fonction execute()` : Un Num n'est pas exécutable. Cette fonction est donc vide.

##### Fonctions propres aux Num
`@fonction setValue()` : Setter de l'attribut `number`.







#### Classe TigerString
La classe `TigerString` hérite de la classe `Expression`. Il s'agit des expressions étant des `String`.

Cette classe est basique et représente une chaîne de caractères (`String`).

##### Attributs propres
`@attribut String stringVar` : Valeur non fixée (non `final`) de notre `String`.

##### Constructeur
`@constructeur TigerString(pStringVar)` : Initialise l'attribut `stringVar` avec la valeur passée en paramètre. Remplace éventuellement les doubles guillemets par des simples guillemets à l'intérieur de la chaine. 

##### Fonctions redéfinies
`@fonction prettyPrint()` : Affiche la `TigerString` sous la forme : `"stringVar"`

`@fonction getString()` : Getter de l'attribut `stringVar`.

`@fonction initialiseTypes()` : Initialise l'attribut `myType` à `EnumType.STRING`.

`@fonction initialiseParentsPuisTypes()`

`@fonction execute()` : Une String n'est pas un exécutable. Cette fonction est donc vide.

##### Fonctions propre aux TigerString
`@fonction setString()` : Setter de l'attribut `stringVar`.




#### Classe NullExpression
La classe `NullExpression` hérite de la classe `Expression`. Il s'agit des expression classées comme étant nulles.

Cette classe est basique et représente une expression nulle (`Null`).

##### Fonctions redéfinies
`@fonction prettyPrint()` : Ne print rien.

`@fonction getNum()` : Déclenche une exception.

`@fonction getString()` : Déclenche une exception.

`@fonction initialiseTypes()` : Initialise l'attribut `myType` à `EnumType.VOID`.

`@fonction initialiseParentsPuisTypes()` : Une `NullExpression` n'a pas de parent, on initialise donc son parent sur `this`.

`@fonction execute()` : Une `NullExpression` n'est pas un exécutable. Cette fonction est donc vide.




#### Classe Variable
La classe `Variable` hérite de la classe `Expression`. Il s'agit de variable pouvant être associé soit à un `Num`, soit à une `String` dans la `HashMap` de la portée courante. Son type définit dans l'attribut `myType` sera alors défini par le type de l'`Expression` associée.

##### Attributs propres
`@attribut String myName` : Nom de la variable. 

`@attribut boolean inForLoop` : Booléen définissant si la variable est dans une boucle for ou non.

##### Constructeurs
`@constructeur Variable(pMyName)` : Initialise l'attribut `myName` à l'aide du paramètre `pMyName` et initialise l'attribut `inForLoop` à `false`. Utilise le second constructeur.
`@constructeur Variable(pMyName, pInForLoop)` : Initialise les deux attributs avec les valeurs passées en paramètre. 

##### Fonctions redéfinies
`@fonction prettyPrint()` : Affiche sous la forme : `var myName`.

`@fonction getNum()` : Retourne le `Num` associé à `myName` dans la `HashMap` des variables de la portée courante, s'il s'agit effectivement d'un `Num`. Lance une exception sinon.

`@fonction getString()` : Retourne la `String` associée à `myName` dans la `Hashmap` des variables de la portée courante, s'il s'agit effectivement d'une `String`. Lance une exception sinon.

`@fonction initialiseTypes()` : Initialise l'attribut `myType` au type de l'`Expression` associée à `myName` dans la HashMap des variables de la portée courante. 

`@fonction initialiseParentsPuisTypes()` 

`@fonction execute()` : Une variable n'est pas exécutable. Cette fonction est donc vide.

##### Fonctions propres aux Variables
`@fonction getExpressionDansHashMap()` : Getter de l'`Expression` associée à `myName` dans la `HashMap` des variables de la portée courante, si le nom `myName` est bien présent dans cette HashMap.

`@fonction isInForLoop()` : Getter de l'attribut `inForLoop`.

`@fonction getName()` : Getter de l'attribut `myName`.


#### Classe Print
La classe `Print` hérite de la classe `Expression`. Il s'agit de l'expression désignant la fonction print de notre langage. Elle se traduit syntaxiquement par "`print()`".

##### Attributs propres
`@attribut Expression expression_1` : Expression dont il faudra afficher la valeur s'il s'agit d'un `Num` ou la `String` sinon.

##### Fonctions redéfinies
`@fonction prettyPrint()` : Affiche le `Print` sous la forme : `print(expression_1)`. 

`@fonction initialiseTypes()` : Fonction qui initialise l'attribut `myType` à `EnumType.VOID` puis lance des vérifications à l'aide de la fonction `verification()`.

`@fonction getNum()` : Retourne une erreur car une expression `Print` ne peut pas renvoyer de `Num`.

`@fonction getString()` : Retourne une erreur car une expression `Print` ne peut pas renvoyer de `String`.

`@fonction execute()` : Exécute l'attribut `expression_1` puis affiche sa valeur si son argument `myType` est un `Num` ou sa `String` s'il s'agit d'une `String`.

`@fonction initialiseParentsPuisTypes()` : Initialise son propre parent puis le parent de l'attribut `expression_1` avec `this` puis l'attribut `myType` avec la fonction `initialiseTypes()`.

##### Fonctions propres aux Print
`@fonction verification()` : Renvoie une erreur si l'attribut `expression_1` est de type `Void`. Ne fait rien si c'est un `Num` ou une `String`.




#### Classe Affectation
La classe `Affectation` hérite de la classe `Expression`. Il s'agit de l'expression désignant la fonction "=" de notre langage. Elle se traduit syntaxiquement par "`:=`".

##### Attributs propres
`@attribut Variable myVariable`

`@attribut Expression expression_1`

##### Constructeur
`@constructeur Affectation(pMyVariable, pExpression1)` : Initialise les deux attribut avec les valeurs passées en paramètre.

##### Fonctions redéfinies
`@fonction execute()` : Exécute l'`expression_1`, puis affecte à l'attribut `myVariable` la valeur de l'expression exécutée si l'attribut `myType` d'`expression_1` est un `Num`, ou sa `String` s'il s'agit d'une `String`. S'il s'agit d'un type `Void` alors l'exécution retourne une erreur.

`@fonction prettyPrint()` : Affiche l'`Affectation` sous la forme `(myVariable := expression_1)`.

`@fonction getNum()` : Retourne la valeur de l'attribut `myVariable` si son type est `Num`.

`@fonction getString()` : Retourne la `String` de l'attribut `myVariable` si son type est `String`.

`@fonction initialiseTypes()` : Retourne une erreur si les attributs `myType` de `myVariable` et `expression_1` ne correspondent pas. Si les types correspondent, alors on affecte à l'attribut `myType` la valeur de l'attribut `myType` de `expression_1`. Si `myVariable` est dans une boucle for, alors on ne peut changer la valeur de celle-ci, on retourne donc une erreur.

`@fonction initialiseParentsPuisTypes()` : Initialise son propre parent, puis les parents de `myVariable` et de `expression_1` avec `this`, et enfin initialise les types à l'aide de la fonction `initialiseTypes()`.




#### Classe BinopAST
La classe `BinopAST` hérite de la classe `Expression`. Il s'agit de l'expression désignant les opérations binaires. Elle contient une `Enum` regroupant toutes les opérations binaires possibles dans le langage et permet d'effectuer des opérations.

##### Enum BinopSignes
`@enum` : PLUS("+"), MOINS("-"), MULTI("*"), DIV("/"), DIF("<>"), EQ("="), INF("<"), SUP(">"), INFEQ("<="), SUPEQ(">="), OR("|"), AND("&")

`@attribut String signString` : Signe de l'opération sous forme de `String`.

`@constructeur BinopSignes(pSignString)` : Initialise l'attribut `signString` avec la valeur passée en paramètre.

`@fonction getSignString()` : Getter de l'attribut `signString`.

##### Attributs propres
`@attribut BinopSignes binopS` : Opérateur de l'opération binaire.

`@attribut Expression expression_1` : Premier membre de l'opération (membre de gauche).

`@attribut Expression expression_2` : Second membre de l'opération (membre de droite).

##### Fonctions redéfinies
`@fonction prettyPrint()` : Affiche l'opération binaire sous la forme : `(expression_1 binopS expression_2)`

`@fonction getNum()` : Si le type renseigné dans l'attribut `myType` est un `Num` alors on effectue l'opération indiquée selon le `binopS` indiqué. Les opérations peuvent être effectuées sur des `Expressions` de type `Num` et `String`. Si `myType` n'est pas `Num` alors on retourne une exception puisque la fonction `getNum()` doit retourner un `double`.

`@fonction getString()` : Si le type renseigné dans l'attribut `myType` est une `String` alors on ne peut effectuer que l'opération d'addition soit en castant deux chaînes de caractère, soit en convertissant les `int` ou `double` en chaines de caractère et en effectuant l'opération d'addition (concaténation de deux chaines de caractères).

`@fonction initialiseTypes()` : Récupère l'opérateur `binopS` de notre `BinopAST` puis vérifie que l'on ne fait pas d'opérations impossibles (comparaison des types de chacune des attributs `expression_1` et `expression_2`). Initialise ensuite l'attribut `myType` en `Num` s'il s'agit d'une opération renvoyant des `Num` ou sur `String` s'il s'agit d'une opération `+` de concaténation de `String`. Lance ensuite une vérification à l'aide de la fonction `vérification()`.

`@fonction initialiseParentsPuisTypes()` : Innitialise l'attribut `myParent` avec le paramètre `pParent`. Initialise ensuite les parents de chacun des membres `expression_1` et `expression_2`. Initialise enfin `myType` à l'aide de la fonction `initialiseTypes()`.

`@fonction execute()` :  Exécute les deux membres de l'opération binaire `expression_1` et `expression_2`.

##### Fonction propre à BinopAST
`@fonction verification()` : Vérifie que les opérations sont bien effectuées sur des types `String` ou `Num`. Vérifie que les opérations "-", "/", "*", "&", "|" sont bien effectuées sur des `Num`. Vérifie enfin que les deux `Expressions` soient de même type pour les opérateurs "<>", "=", "<", ">", "<=", ">=".




#### Classe Sequence
La classe `Sequence` hérite de la classe `Expression`. Il s'agit de la classe désignant les séquences d'`Expressions` entre parenthèses. 

##### Attributs propres
`@attribut List<Expression> listExp` : Liste des `Expressions` contenues entre les parenthèses.

##### Fonctions redéfinies 
`@fonction prettyPrint()` : Affiche les `Sequences` tel que : `(expression_1 ; expression_2 ; expression_3 ;)`.

`@fonction initialiseTypes()` : Récupère le type de la dernière expression de la liste d'expressions et initialise avec son attribut `myType`. Si la liste est vide, alors `myType` est initialisé avec le type `Void`.

`@fonction initialiseParentsPuisTypes()` : Initialise l'attribut `myParent` avec le paramètre `pParent`. Initialise ensuite le parent de chacune des expressions contenues dans l'attribut `listExp`. 

`@fonction execute()` : Exécute chacune des expressions contenues dans l'attribut `listExp`.

`@fonction getNum()` : Retourne la valeur de la derniere `Expression` de la liste si l'attribut `myType` est bien de type `Num`. Lance une exception sinon.

`@fonction getString()` : Retourne la `String` de la derniere `Expression` de la liste si l'attribut `myType` est bien de type `String`. Lance une exception sinon.







#### Classe ListeNamesAndExpressions
La classe `ListeNamesAndExpressions` fait office de structure. Elle contient la liste des noms et expressions à traiter.

##### Attributs propres
`@attribut List<String> listeToTreatNames` : Liste contenant les noms à traiter.

`@attribut List<Expression> listeToTreatExpressions` : Liste contenant les `Expressions` à traiter.

##### Constructeur
`@constructeur ListeNamesAndExpressions(pListeToTreatNames, pListToTreatExpressions)` : Initialise les attributs avec les valeurs passées en paramètres.

##### Fonctions propres aux ListeNamesAndExpressions
`@fonction getListeToTreatNames()` : Getter de l'attribut `listeToTreatNames`.

`@fonction getListeToTreatExpressions()` : Getter de l'attribut `listeToTreatExpressions`.





#### Classe LetInEnd
La classe `LetInEnd` hérite de la classe `Expression`. Il s'agit de l'expression désignant la fonction let [affectations] in [actions] end. Elle se traduit syntaxiquement par `let ... in ... end`.

##### Attributs propres
`@attribut List<String> listeToTreatNames` : Liste des noms à traiter.

`@attribut List<Expression> listeToTreatExpression` : Liste des `Expressions` à traiter.

`@attribut HashMap<String, Expression> variableHM` : Hashmap de la portée courante.

`@attribut List<Expression> listeExpression` : Liste des `Expressions` à exécuter.

##### Constructeur 
`@constructeur LetInEnd(pListeNamesAndExpressions, pListeExpression)` : Initialise les attributs `listToTreatExpression` et `listeToTreatExpression` avec les valeurs contenues dans `pListeNamesAndExpressions` passée en paramètre. Initialise `listeExpression` avec le paramètre restant. Crée une nouvelle `HashMap` pour la portée courante.

##### Fonctions redéfinies
`@fonction prettyPrint()` : Affiche `LetInEnd` sous la forme `let(var variable1 := expression1   var variable2 := expression2)in(listeExpression)end`. 

`@fonction getNum()` : Si l'attribut `myType` est `Num` alors on retourne la valeur de la dernière expression exécutée de `listeExpression`. Sinon, on retourne une erreur.

`@fonction getString()` : Si l'attribut `myType` est `String` alors on retourne la `String` de la dernière expression exécutée de `listeExpression`. Sinon, on retourne une erreur.

`@fonction initialiseTypes()` : Si la taille de `listeExpression` est nulle, alors on initialise `myType` à `Void`. Si elle n'est pas nulle et que sa dernière expression n'est pas nulle, alors on initialise `myType` à la valeur du type de la dernière expression de `listeExpression`. Sinon, on retourne une erreur.

`@fonction intialiseParentsPuisTypes()` : Initialise `myParent` avec  `pParent` passé en paramètre. Initialise ensuite les variables de la `HashMap` de la portée courante à l'aide de la fonction `initialiseVariableHM()` puis chacune des expressions contenues dans la `listeExpressions`. Initialise enfin le type de `LetInEnd`.

`@fonction hasVariableHashMap()` : Retourne `true`.

`@fonction getVariableHM()` : Getter de l'attribut `variableHM`.

`@fonction execute()` : Exécute chacune des instructions de `listeExpressions` dans l'ordre.


##### Fonctions propre aux LetInEnd
`@fonction initialiseVariableHM()` : Récupère chacun des noms et des `Expressions` des deux attributs `listeToTreatNames` et `listeToTreatExpressions` et les associent dans la `HashMap` attribut `variableHM` en les transformant en `Num` ou en `TigerString` en fonction du type de chacune des `Expressions` dans `listeToTreatExpressions`.





#### Classe IfThenElse
La classe `IfThenElse` hérite de la classe `Expression`. Il s'agit de l'expression désignant la fonction if [condition] then [actions] (else [actions]). Elle se traduit syntaxiquement par "`if ... then ... else ...`" ou "`if ... then ...`".

##### Attributs propres
`@attribut Expression ifBlock` : Expressions du bloc "if".

`@attribut Expression thenBlock` : Expressions du bloc "then".

`@attribut Expression elseBlock` : Expressions du bloc "else".

`@attribut boolean hasElseBlock` : Booléen indiquant si le bloc "else" est présent ou non.

##### Constructeur
`@constructeur IfThenElse(pIf, pThen, pElse)` : Initialise les trois premiers attributs aux valeurs passées en paramètres. Si la classe du bloc `pElse` est `NullExpression` alors l'attribut est initialisé à `false`. Il vaut `true` sinon. 

##### Fonctions redéfinies
`@fonction prettyPrint()` : Affiche le bloc `IfThenElse` sous la forme : `if(ifBlock)then(thenBlock)else(elseBlock)` si l'attribut `hasElseBlock` est `true`, `if(ifBlock)then(elseBlock)` sinon. 

`@fonction execute()` : Exécute le bloc "if" puis récupère sa valeur. Si celle-ci vaut "1" alors on exécute la le bloc "then". Sinon, s'il y a un bloc "else", alors on l'exécute.

`@fonction getNum()` : Si l'attribut `myType` est `Num` alors on retourne soit la valeur de `thenBlock` si la condition du `ifBlock` est vérifiée, soit la valeur de `elseBlock` sinon. Si `myType` n'est pas de type `Num` alors on déclenche une exception.

`@fonction getString()` : Si l'attribut `myType` est `String` alors on retourne soit la `String` de `thenBlock` si la condition du `ifBlock` est vérifiée, soit la `String` de `elseBlock` sinon. Si `myType` n'est pas de type `String` alors on déclenche une exception.

`@fonction initialiseTypes()` : Vérifie que le `ifBlock` retourne bien un type `Num` (puisque la condition est vérifiée ou non donc vaut soit 0 soit 1). Vérifie également que les types du `thenBlock` et du `elseBlock` (s'il en possède) sont bien du même type, et déclenche une erreur sinon. Initialise `myType` au type du `thenBlock`. S'il n'y a pas de `elseBlock`, le `IfThenElse` est de type `Void`.

`@fonction initialiseParentsPuisTypes()` : Initialise `myParent` à la valeur passée en paramètre. Initialise ensuite les parents des trois blocs `ifBlock`, `thenBlock` et `elseBlock` à `this`. Initialise enfin l'attribut `myType` à l'aide de la fonction `initialiseTypes()`.





#### Classe For
La classe `For` hérite de la classe `Expression`. Il s'agit de l'expression désignant la boucle for. Elle se traduit syntaxiquement par "`for ... := ... to ... do ...`".

##### Attributs propres
`@attribut Variable myVariable` : Nom de la variable dont la valeur va changer ("i" dans une boucle for par exemple).

`@attribut HashMap<String, Expression> variableHM` : La `HashMap` de la portée courante.

`@attribut Expression expressionStart` : Valeur de départ de la variable.

`@attribut Expression expressionEnd` : Valeur d'arrivée de la variable.

`@attribut List<Expression> listeExpression` : Liste des `Expressions` à exécuter pour chacune des variables de la boucle.

##### Constructeur
`@constructeur For(pMyVariable, pExpressionStart, pExpressionEnd, pListeExpression)` : Initialise chacun des attributs avec les valeurs passées en paramètre.

##### Fonctions redéfinies
`@fonction prettyPrint()` : Affiche le bloc `For` sous la forme : `for(myVariable := expressionStart to expressionEnd)do(listeExpression)`.

`@fonction initialiseTypes()` : Initialise l'attribut `myType` à `Void`.

`@fonction initialiseParentsPuisTypes()` :  Initialise `myParent` à la valeur passée en paramètre. Initialise ensuite les parents des blocs `expressionStart` et `expressionEnd` à la valeur des parents du `For`. Initialise la `HashMap` de la portée courante à l'aide de la fonction `initialiseVariableHM()` puis initialise le parent de `myVariable` avec `this`. Initialise les parents puis les type de chacune des expressions de `listeExpressions` et enfin initialise le type du `For` avec la fonction `initialiseTypes()` et lance les vérifications à l'aide de la fonction `verifications()`.

`@fonction execute()` : Exécute les deux `Expressions` `expressionStart` et `expressionEnd` puis récupère leurs valeurs. Exécute ensuite chacune des instructions de `listeExpression` pour chacune des valeurs situées entre la valeur de début et de fin de `For`.

`@fonction hasVariableHM()` : Retourne `true`.

`@fonction getVariableHM()` : Getter de la `HashMap` de la portée courante `variableHM`.


##### Fonctions propre aux For
`@fonction verifications()` : Vérifie que l'attribut `myVariable` est bien dans une boucle for grâce à l'appel de la fonction `isInForLoop()`. Vérifie également que le type renseigné dans `myType` de `myVariable`, `expressionStart` et `expressionEnd` soient bien des `Num`, sinon la boucle `For` n'a pas de sens.

`@fonction initialiseVariableHM()` : Met dans la `HashMap` de la portée courante la variable `myVariable` associée à la valeur de `expressionStart`.




#### Classe While
La classe `While` hérite de la classe `Expression`. Il s'agit de l'expression désignant la boucle While. Elle se traduit syntaxiquement par "`while ... do ...`".

##### Attributs propres
`@attribut Expression conditionBlock` : Bloc de la condition qui doit retourner une valeur de type 0 ou 1.

`@attribut List<Expression> listeExpression` :  Liste des `Expressions` à faire à chaque boucle.

##### Constructeur
`@constructeur While(pConditionBlock, pListExpressions)` : Initialise chacun des attributs avec les valeurs passées en paramètre.

##### Fonctions redéfinies
`@fonction prettyPrint()` : Affiche le bloc `While` sous la forme : `while(conditionBlock)do(listeExpression)`

`@fonction initialiseTypes()` : Initialise l'attribut `myType` à `Void`.

`@fonction initialiseParentsPuisTypes()` : Initialise l'attribut `myParent` avec le paramètre `pParent` de la fonction. Initialise ensuite le parent du `conditionBlock` puis ceux de chacune des expressions de `listeExpression`. Enfin, vérifie que le type du `conditionBlock` est bien `Num` (doit valoir 0 ou 1) pour savoir si la condition est bien respectée, et initialise l'attribut `myType` à l'aide de la fonction `initialiseTypes()`.

`@fonction execute()` : Exécute le `conditionBlock` et récupère sa valeur. Tant que sa valeur vaut 1 (c'est à dire est vérifiée), alors on exécute toutes les instructions contenues dans la liste `listeExpressions`.




#### Classe Switch

Notre `switch` est de la forme suivante:

	switch 2.0
		case 0.0: print(1) 
		case 1.5: print(2)
		case 2.0: print(3)
		else print("else")	
	end

Après le `switch` on écrit l'`Expression` à tester qui doit retourner une valeur (2.0 ici). On peut mettre ensuite autant de `case` qu'on le souhaite (0, 1 ou plusieurs), et pour chaque `case` on écrit entre `case` et `:` l'`Expression` de test pour accéder à ce `case` (dans l'exemple il s'agit de 0.0, 1.5 et 2.0), puis à droite en écrit ensuite l'`Expression` à exécuter si la condition est validée (print(1), print(2) et print(3) ici). Après les `cases` on écrit le `else` qui correspond au cas par défaut en écrivant `else` suivie de l'`Expression` à exécuter dans le cas où on est entré dans aucun des `cases` (`print("else")`). Le `else` est obligatoire. On termine le `Switch` en écrivant un `end` à la fin.

Un `switch` est du même type que ses `Expressions` à exécuter (à droite des `:`), qui doivent donc toutes être de même type (`VOID` ici).

Toutes les `Expressions` de test des `cases` doivent être du même type que l'`Expression` à tester (`NUM` pour cet exemple)

##### Attributs propres
`@attribut Expression expressionToTest` : Expression dont la valeur déterminera dans quel `case` nous nous placerons.

`@attribut HashMap<Expression, Expression> HMcaseExp` : 

`@attribut Expression expressionElse` : Dans le cas où l'expressionToTest n'est pas reconnu par l'un des `case` précédents, alors on exécute les instructions du `else` (comportement par défaut).

##### Constructeur
`@constructeur Switch(pExpressionToTest, pListCase, pListExp, pExpressionElse)` : Initialise `expressionToTest` et `expressionElse` avec les valeurs passées en paramètre. Crée une Hashmap pour l'attribut `HMcaseExp` et l'initialise à l'aide de la fonction `initialiseHM(pListCase, pListExp)`.

##### Fonctions redéfinies
`@fonction prettyPrint()` : Affiche le `Switch` sous la forme : `switch(expressionToTest)(case HMcaseExp.key : HMcaseExp.get(key))else()end` 

`@fonction initialiseTypes()` : Si la liste des `case` est vide, alors on initialise le type à `Void`. Sinon, on prend le type de l'expression associée à la première clé de l'attribut `HMcaseExp`. On lance ensuite les vérifications à l'aide de la fonction `verification()`.

`@fonction initialiseParentsPuisTypes()` : Initialise l'attribut `myParent` avec le paramètre `pParent`. On initialise ensuite les parents de `expressionToTest`, de toutes les expressions contenues dans les clés et les valeurs de `HMcaseExp`, et de `expressionElse`. Initialise enfin le type à l'aide de la fonction `initialiseTypes()`.

`@fonction execute()` : Exécute l'expression `expressionToTest` puis cherche dans toute la `HMcaseExp` si la valeur ou la `String` associée à la clé correspond à la valeur ou la `String` associée à `expressionToTest`. Si on trouve le `case` correspondant, alots on exécute l'instruction et on arrête l'exécution. Sinon, on lance l'`expressionElse`, le cas par défaut.

`@fonction getNum()` : Retourne la valeur de l'expression exécutée dans le bon `case` choisi ou du else si aucun `case` ne convient. S'il ne s'agit pas d'un type `Num`, lance une exception.

`@fonction getString()` : Retourne la `String` de l'expression exécutée dans le bon `case` choisi ou du else si aucun `case` ne convient. S'il ne s'agit pas d'un type `String`, lance une exception.


##### Fonctions propres aux Switch
`@fonction verification()` : Vérifie que l'attribut `myType` a bien été initialisé. Vérifie que le type des expressions valeurs de l'attribut `HMcaseExp` ainsi que `expressionElse` ont bien le même type que l'attribut `myType`. Vérifie que le type des expressions clés de l'attribut `HMcaseExp` soient du même type que l'attribut `expressionToTest`. Vérifie également que `expressionToTest` ne soit pas de type `Void`.

`@fonction initialiseHM()` : Vérifie que la taille des deux listes passées en paramètre ne soit pas différente et déclenche une erreur si ce n'est pas le cas. Remplis l'attribut `HMcaseExp` avec pour clés les valeurs de la liste `pListCase`, et pour valeur les valeurs de la liste `pListExp`.



## Sources
* https://stackoverflow.com/questions/24156948/javacc-quote-with-escape-character/24163497
* https://www.techiedelight.com/how-to-read-file-using-inputstream-java/
* https://stackoverflow.com/questions/13969511/c-comment-removal-with-javacc
* https://stackoverflow.com/questions/14676407/list-all-files-in-the-folder-and-also-sub-folders

