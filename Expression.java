import java.util.HashMap;

/*
 * Liste des fonctions necessaires pour que les differentes Expression puissent interagir correctement entre elles
 */
public abstract class Expression {

	private Expression myParent = null;
	protected EnumType myType ;

	/*
	 * Print avec des System.out.println() l'AST de l'Expression
	 */
	public abstract void prettyPrint();

	/*
	 * Retourne le type de l'Expression, cad le type de la valeur retournée
	 */
	public EnumType getType() {
		return this.myType;
	}

	/*
	 * Fonction utilisée dans la fonction initialiseParents() pour initialiser le
	 * private enumType définissant le type de l'expression !!!!!!!!!!! A METTRE A
	 * JOUR
	 */
	public abstract void initialiseTypes() throws ParseException;

	/*
	 * Initialise le parent de l'expression (le parent est celui passé en
	 * paramètres) et fais ensuite appelle à la fonction initialize des Expressions
	 * en-dessous de lui dans l'AST. Initialise ensuite le type en faisant appel à
	 * initialiseMyType().
	 */
	public abstract void initialiseParentsPuisTypes(Expression pParent) throws ParseException;
	
	/*
	 * 'Run' les Expressions qui le permettent. 
	 * Exemple: un execute() sur une Expression
	 * Print va printer la valeur retournée par le sous-AST du Print
	 */
	public abstract void execute() throws ParseException;
	
	/*
	 * Retourne la valeur Double de l'Expression (Si elle existe)
	 */
	public double getNum() throws ParseException{
		throw new ParseException("Les Expressions '" + this.getClass() + "' ne retournent pas de NUM");
	}

	/*
	 * Retourne la valeur String de l'Expression (Si elle existe)
	 */
	public  String getString() throws ParseException{
		throw new ParseException("Les Expressions '" + this.getClass() + "' ne retournent pas de TIGERSTRING");
	}

	/*
	 * Retourne la HashMap contenant le nom des variables et les Expressions leur
	 * etant associees
	 */
	public HashMap<String, Expression> getVariableHM() throws ParseException{
		throw new ParseException("Les Expression '" + this.getClass() + "' n'ont pas de HashMap de variables");
	}

	public void setParent(Expression pMyParent) {
		this.myParent = pMyParent;
	}

	/*
	 * Retourne le Parent de l'Expression, cad l'Expression au-dessus dans l'AST Si
	 * l'Expression n'a pas de parent, elle retourne un NullExpression
	 */
	public Expression getParent() {
		return myParent;
	}

	/*
	 * Retourne true si l'Expression a une HashMap de variables, cad si l'Expression
	 * represente le debut d'une portee
	 */
	public boolean hasVariableHashMap() {
		return false;
	}
	
}
