import java.util.List;

public class Sequence extends Expression {

	private final List<Expression> listeExp;

	public Sequence(List<Expression> pListeExp) {
		this.listeExp = pListeExp;
	}

	@Override
	public void prettyPrint() {
		System.out.print("(");
		for (Expression i : listeExp) {
			i.prettyPrint();
			System.out.print(";");
		}
		System.out.print(")");
	}

	@Override
	public void initialiseTypes() throws ParseException {
		if (this.listeExp.size() == 0)
			this.myType = EnumType.VOID;
		else
			switch (this.listeExp.get(this.listeExp.size()-1).getType()) {
			case NUM:
				this.myType = EnumType.NUM;
				break;
			case STRING:
				this.myType = EnumType.STRING;
				break;
			case VOID:
				this.myType = EnumType.VOID;
				break;
			default:
				throw new ParseException(
						"Le type '" + this.listeExp.get(0).getType() + "' n'a pas été pris en compte dans cette fonction");
			}
	}

	@Override
	public void initialiseParentsPuisTypes(Expression pParent) throws ParseException {
		this.setParent(pParent);
		for (Expression i : this.listeExp) {
			i.initialiseParentsPuisTypes(this);
		}

		this.initialiseTypes();
	}

	@Override
	public void execute() throws ParseException {
		for (Expression i : this.listeExp) {
			i.execute();
		}
	}

	@Override
	public double getNum() throws ParseException {
		if (this.getType() == EnumType.NUM)
			return this.listeExp.get(this.listeExp.size()-1).getNum();
		else
			throw new ParseException(
					"Cette Expression Parentheses est de type '" + this.getType() + "', elle ne retourne pas de NUM");
	}

	@Override
	public String getString() throws ParseException {
		if (this.getType() == EnumType.STRING)
			return this.listeExp.get(this.listeExp.size()-1).getString();
		else
			throw new ParseException(
					"Cette Expression Parenthèse est de type '" + this.getType() + "', elle ne retourne pas de STRING");
	}

}
