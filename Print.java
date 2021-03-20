public class Print extends Expression {

	private Expression expression_1;

	public Print(Expression pExpression_1) throws ParseException {
		this.expression_1 = (Expression) pExpression_1;
	}

	@Override
	public void prettyPrint() {
		System.out.print("print(");
		expression_1.prettyPrint();
		System.out.print(")");
	}

	@Override
	public double getNum() throws ParseException {
		throw new ParseException("Une expression Print ne retourne pas de 'NUM'");
	}

	@Override
	public String getString() throws ParseException {
		throw new ParseException("Une expression Print ne retourne pas de 'STRING'");
	}

	@Override
	public void initialiseTypes() throws ParseException {
		this.myType = EnumType.VOID;

		this.verification();
	}

	@Override
	public void execute() throws ParseException {
		this.expression_1.execute();

		if (expression_1.getType() == EnumType.NUM)
			System.out.println(expression_1.getNum());
		else
			System.out.println(expression_1.getString());
	}

	@Override
	public void initialiseParentsPuisTypes(Expression pParent) throws ParseException {
		this.setParent(pParent);
		expression_1.initialiseParentsPuisTypes(this);

		this.initialiseTypes();
	}

	private void verification() throws ParseException {
		switch (expression_1.getType()) {
		case VOID:
			throw new ParseException("L'Expression dans le Print ne peut pas être de type VOID");
		case NUM:
			break;
		case STRING:
			break;
		default:
			throw new ParseException(
					"Le type '" + this.expression_1.getType() + "' n'a pas été pris en compte pour cette Expression");
		
		}
	}

}
