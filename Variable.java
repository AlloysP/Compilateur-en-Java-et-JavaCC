import java.util.HashMap;

public class Variable extends Expression {

	private final String myName;
	private final boolean inForLoop;

	public Variable(String pMyName) throws ParseException {
		this(pMyName, false);
	}

	public Variable(String pMyName, boolean pInForLoop) throws ParseException {
		this.myName = pMyName;
		this.inForLoop = pInForLoop;
	}

	@Override
	public void prettyPrint() {
		System.out.print(myName);
	}

	@Override
	public double getNum() throws ParseException {
		if (this.getType() == EnumType.NUM)
			return this.getExpressionDansHashMap().getNum();
		else
			throw new ParseException(
					"Cette variable est de type '" + this.getType() + "', elle ne retourne pas de NUM");
	}

	@Override
	public String getString() throws ParseException {
		if (this.getType() == EnumType.STRING)
			return this.getExpressionDansHashMap().getString();
		else
			throw new ParseException(
					"Cette variable est de type '" + this.getType() + "', elle ne retourne pas de STRING");
	}

	@Override
	public void initialiseTypes() throws ParseException {
		EnumType vMyType = this.getExpressionDansHashMap().getType();
		if (vMyType != EnumType.VOID)
			if (vMyType != null)
				this.myType = vMyType;
			else
				throw new ParseException("Le type de la variable n'a pas été initialisé dans la hashMap");
		else
			throw new ParseException("Une variable ne peut pas être de type VOID");
	}

	@Override
	public void initialiseParentsPuisTypes(Expression pParent) throws ParseException {
		this.setParent(pParent);

		this.initialiseTypes(); // Dans le cas des Variables on doit d'abord initialiser les parents avant de
								// pouvoir initialiser le type
	}

	public Expression getExpressionDansHashMap() throws ParseException {
		Expression currentNode = this;
		while (true) {
			currentNode = currentNode.getParent();
			if (currentNode.getClass() != NullExpression.class) {
				if (currentNode.hasVariableHashMap()) {
					HashMap<String, Expression> vHashMap = currentNode.getVariableHM();
					if (vHashMap.containsKey(this.myName))
						return currentNode.getVariableHM().get(this.myName);
				}
			} else
				throw new ParseException("La variable " + this.myName + " n'a pas été déclarée dans le block du LET");
		}
	}

	@Override
	public void execute() throws ParseException {
	}

	public boolean isInForLoop() {
		return this.inForLoop;
	}

	public String getName() {
		return this.myName;
	}

}
