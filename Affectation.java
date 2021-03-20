public class Affectation extends Expression {

	private final Variable myVariable;
	private final Expression expression_1;

	public Affectation(Variable pMyVariable, Expression pExpression_1) throws ParseException {
		this.myVariable = pMyVariable;
		this.expression_1 = pExpression_1;
	}

	@Override
	public void execute() throws ParseException {
		this.expression_1.execute();

		Expression varExpression = myVariable.getExpressionDansHashMap();
		switch (this.myType) {
		case NUM:
			((Num) varExpression).setValue(this.expression_1.getNum());
			break;
		case STRING:
			((TigerString) varExpression).setString(this.expression_1.getString());
			break;
		case VOID:
			throw new ParseException("Une Expression de type VOID ne peut pas être affectée à une variable");
		default:
			throw new ParseException(
					"Le type '" + this.expression_1.getType() + "' n'a pas été pris en compte pour cette Expression");
		}
	}

	@Override
	public void prettyPrint() {
		System.out.print("(");
		this.myVariable.prettyPrint();
		System.out.print(" := ");
		this.expression_1.prettyPrint();
		System.out.print(")");
	}

	@Override
	public double getNum() throws ParseException {
		return myVariable.getNum();
	}

	@Override
	public String getString() throws ParseException {
		return myVariable.getString();
	}

	@Override
	public void initialiseTypes() throws ParseException {
		Expression varExpression = myVariable.getExpressionDansHashMap();
		if (varExpression.getType() != this.expression_1.getType())
			throw new ParseException("Affectation impossible, la variable est de type '" + varExpression.getType()
					+ "' alors que l'Epression à lui affecter est de type '" + this.expression_1.getType() + "'");
		switch (varExpression.getType()) {
		case NUM:
			this.myType = EnumType.NUM;
			break;
		case STRING:
			this.myType = EnumType.STRING;
			break;
		case VOID:
			throw new ParseException("Une Expression de type VOID ne peut pas être affectée à une variable");
		default:
			throw new ParseException(
					"Le type '" + this.expression_1.getType() + "' n'a pas été pris en compte pour cette Expression");
		}
		
		// Verifications
		if (this.myVariable.isInForLoop())
			throw new ParseException("On ne peut pas affecter une autre valeur à la variable d'une boucle for");
	}

	@Override
	public void initialiseParentsPuisTypes(Expression pParent) throws ParseException {
		this.setParent(pParent);
		this.myVariable.initialiseParentsPuisTypes(this);
		this.expression_1.initialiseParentsPuisTypes(this);

		this.initialiseTypes();
	}
}
