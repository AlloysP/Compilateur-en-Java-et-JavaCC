public class IfThenElse extends Expression {

	private Expression ifBlock;
	private Expression thenBlock;
	private Expression elseBlock;
	private boolean hasElseBlock; // Pour differencier les if then else des if then

	public IfThenElse(Expression pIf, Expression pThen, Expression pElse) throws ParseException {
		this.ifBlock = pIf;
		this.thenBlock = pThen;
		this.elseBlock = pElse;
		this.hasElseBlock = pElse.getClass() != NullExpression.class; // True s'il y a un else, false sinon
	}

	@Override
	public void prettyPrint() {
		System.out.print("if(");
		ifBlock.prettyPrint();
		System.out.print(")then(");
		thenBlock.prettyPrint();
		System.out.print(")");
		if (this.hasElseBlock) {
			System.out.print("else(");
			elseBlock.prettyPrint();
			System.out.print(")");
		}
	}

	/*
	 * Lors de l'execution du if then (else) le bloc if est toujours execute (s'il
	 * est executable) et en fonction de la valeur retournee par le block if on
	 * execute le bloc then ou le block else (si celui-ci est executable)
	 */
	@Override
	public void execute() throws ParseException {
		this.ifBlock.execute();

		double ifBlockValue = ifBlock.getNum();
		if (ifBlockValue == 1.0) {
			this.thenBlock.execute();
		} else if (hasElseBlock && ifBlockValue == 0.0) {
			this.elseBlock.execute();
		}
	}

	@Override
	public double getNum() throws ParseException {
		if (this.myType == EnumType.NUM) {
			double ifBlockValue = ifBlock.getNum();
			if (ifBlockValue == 1.0)
				return thenBlock.getNum();
			else
				return elseBlock.getNum();
		} else
			throw new ParseException("Cette expression '" + this.getClass().getName() + "' est de type '" + this.myType
					+ "', elle ne retourne pas de 'NUM'");
	}

	@Override
	public String getString() throws ParseException {
		if (this.myType == EnumType.STRING) {
			double ifBlockValue = ifBlock.getNum();
			if (ifBlockValue == 1.0)
				return thenBlock.getString();
			else
				return elseBlock.getString();
		} else
			throw new ParseException("Cette expression '" + this.getClass().getName() + "' est de type '" + this.myType
					+ "', elle ne retourne pas de 'STRING'");
	}

	@Override
	public void initialiseTypes() throws ParseException {
		if (ifBlock.getType() != EnumType.NUM)
			throw new ParseException("Le block du if est censé retourner une valeur de type Num");

		if (this.hasElseBlock) {
			if (thenBlock.getType() == elseBlock.getType())
				this.myType =  thenBlock.getType();
			else
				throw new ParseException(
						"Le block du then est de type '" + thenBlock.getType() + "' et le block du else est de type '"
								+ elseBlock.getType() + "', hors ils doivent être de même type");
		} else
			this.myType =  EnumType.VOID; // S'il n'y a pas de else, le if then n'est pas type
	}

	@Override
	public void initialiseParentsPuisTypes(Expression pParent) throws ParseException {
		this.setParent(pParent);
		ifBlock.initialiseParentsPuisTypes(this);
		thenBlock.initialiseParentsPuisTypes(this);
		elseBlock.initialiseParentsPuisTypes(this);
		
		this.initialiseTypes();
	}

}
