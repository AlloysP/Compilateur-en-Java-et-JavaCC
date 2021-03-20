import java.util.List;

public class While extends Expression {

	private final Expression conditionBlock;
	private final List<Expression> listeExpression;

	public While(Expression pConditionBlock, List<Expression> pListeExpression) {
		this.conditionBlock = pConditionBlock;
		this.listeExpression = pListeExpression;
	}

	@Override
	public void prettyPrint() {
		System.out.print("while(");
		this.conditionBlock.prettyPrint();
		System.out.println(") do(");
		for (Expression i : listeExpression) {
			i.prettyPrint();
			System.out.println();
		}
		System.out.print(")");
	}

	@Override
	public void initialiseTypes() throws ParseException {
		this.myType = EnumType.VOID;
	}

	@Override
	public void initialiseParentsPuisTypes(Expression pParent) throws ParseException {
		this.setParent(pParent);
		this.conditionBlock.initialiseParentsPuisTypes(this);
		for (Expression i : this.listeExpression) {
			i.initialiseParentsPuisTypes(this);
		}

		// Verification des types
		if (this.conditionBlock.getType() != EnumType.NUM)
			throw new ParseException("Le block conditionnel ne peut pas être de type'" + this.conditionBlock.getType()
					+ "', il doit retourner un NUM");
		
		this.initialiseTypes();
	}

	@Override
	public void execute() throws ParseException {
		this.conditionBlock.execute();
		while (this.conditionBlock.getNum() == 1.0) {
			for (Expression i : listeExpression)
				i.execute();
			this.conditionBlock.execute();
		}
	}

}
