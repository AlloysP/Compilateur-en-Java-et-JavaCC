/*
 * Cette expression est un 'Null' fait maison uniquement utilisé (pour l'instant) pour traiter le 'if then (else)?' dans Jagger.jj, 
 * afin de traiter dans un seul constructeur le cas ou il y a un else et le cas ou il n'y en a pas un.
 */
public class NullExpression extends Expression {

	public NullExpression() throws ParseException {
	}

	@Override
	public void prettyPrint() {
	}

	@Override
	public double getNum() throws ParseException {
		throw new ParseException("Une Expression NullExpression ne retourne pas de 'NUM'");
	}

	@Override
	public String getString() throws ParseException {
		throw new ParseException("Une Expression NullExpression ne retourne pas de 'STRING'");
	}

	@Override
	public void initialiseTypes() throws ParseException {
		this.myType = EnumType.VOID;
	}

	@Override
	public void initialiseParentsPuisTypes(Expression pParent) throws ParseException {
		this.setParent(this); // Un NullExpression n'a pas de parent

		this.initialiseTypes();
	}

	@Override
	public void execute() throws ParseException {
	}

}
