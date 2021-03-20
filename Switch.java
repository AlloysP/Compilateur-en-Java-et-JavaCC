import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Switch extends Expression {
	private final Expression expressionToTest;
	private final HashMap<Expression, Expression> HMcaseExp;
	private final Expression expressionElse;

	public Switch(Expression pExpressionToTest, List<Expression> pListCase, List<Expression> pListExp,
			Expression pExpressionElse) throws ParseException {
		this.expressionToTest = pExpressionToTest;
		this.HMcaseExp = new HashMap<Expression, Expression>();
		this.expressionElse = pExpressionElse;

		this.initialiseHM(pListCase, pListExp);
	}

	@Override
	public void prettyPrint() {
		System.out.print("switch(");
		this.expressionToTest.prettyPrint();
		System.out.println(")(");
		for (Expression i : HMcaseExp.keySet()) {
			System.out.print("case ");
			i.prettyPrint();
			System.out.print(" : ");
			HMcaseExp.get(i).prettyPrint();
			System.out.println();
		}
		System.out.print("else ");
		this.expressionElse.prettyPrint();
		System.out.println(")end");
	}

	@Override
	public void initialiseTypes() throws ParseException {
		if (this.HMcaseExp.size() == 0)
			this.myType = EnumType.VOID;
		else
			this.myType = this.HMcaseExp.get(this.HMcaseExp.keySet().toArray()[0]).getType();

		this.verification();
	}

	@Override
	public void initialiseParentsPuisTypes(Expression pParent) throws ParseException {
		this.setParent(pParent);

		this.expressionToTest.initialiseParentsPuisTypes(this);
		for (Expression i : HMcaseExp.keySet()) {
			i.initialiseParentsPuisTypes(this);
		}
		for (Expression i : HMcaseExp.values()) {
			i.initialiseParentsPuisTypes(this);
		}
		this.expressionElse.initialiseParentsPuisTypes(this);

		this.initialiseTypes();
	}

	@Override
	public void execute() throws ParseException {
		this.expressionToTest.execute();

		boolean caseFound = false;
		if (this.expressionToTest.getType() == EnumType.NUM) {
			for (Expression i : HMcaseExp.keySet())
				if (this.expressionToTest.getNum() == i.getNum()) {
					i.execute();

					HMcaseExp.get(i).execute();
					caseFound = true;
					break;
				}
		} else
			for (Expression i : HMcaseExp.keySet())
				if (this.expressionToTest.getString() == i.getString()) {
					i.execute();

					HMcaseExp.get(i).execute();
					caseFound = true;
					break;
				}
		if (!caseFound)
			this.expressionElse.execute();
	}

	@Override
	public double getNum() throws ParseException {
		if (this.myType == EnumType.NUM) {
			if (this.expressionToTest.getType() == EnumType.NUM) {
				for (Expression i : HMcaseExp.keySet())
					if (this.expressionToTest.getNum() == i.getNum()) {
						return HMcaseExp.get(i).getNum();
					}
				return this.expressionElse.getNum();
			} else
				for (Expression i : HMcaseExp.keySet())
					if (this.expressionToTest.getString() == i.getString()) {
						return HMcaseExp.get(i).getNum();
					}
			return this.expressionElse.getNum();
		} else
			throw new ParseException("Cette expression '" + this.getClass().getName() + "' est de type '" + this.myType
					+ "', elle ne retourne pas de 'NUM'");
	}

	@Override
	public String getString() throws ParseException {
		if (this.myType == EnumType.STRING) {

			if (this.expressionToTest.getType() == EnumType.NUM) {
				for (Expression i : HMcaseExp.keySet())
					if (this.expressionToTest.getNum() == i.getNum()) {
						return HMcaseExp.get(i).getString();
					}
				return this.expressionElse.getString();
			} else
				for (Expression i : HMcaseExp.keySet())
					if (this.expressionToTest.getString() == i.getString()) {
						return HMcaseExp.get(i).getString();
					}
			return this.expressionElse.getString();

		} else
			throw new ParseException("Cette expression '" + this.getClass().getName() + "' est de type '" + this.myType
					+ "', elle ne retourne pas de 'STRING'");
	}

	private void verification() throws ParseException {
		if (this.myType == null)
			throw new ParseException("La variable myType de ce Switch n'a pas été initialisée");

		for (Expression i : HMcaseExp.values()) {
			if (i.getType() != this.myType)
				throw new ParseException(
						"Toutes les Expressions d'un Switch doivent être de même type, ce qui n'est pas le cas de ce Switch");
		}
		if (this.expressionElse.getType() != this.myType)
			throw new ParseException(
					"Toutes les Expressions d'un Switch doivent être de même type, ce qui n'est pas le cas de ce Switch");

		if (expressionToTest.getType() == EnumType.VOID)
			throw new ParseException("Les Cases d'un Switch ne peuvent pas être de type VOID");

		Set<Double> set_Test_Double = new HashSet<Double>();
		Set<String> set_Test_String = new HashSet<String>();
		for (Expression i : HMcaseExp.keySet()) {
			if (i.getType() != expressionToTest.getType())
				throw new ParseException(
						"Tous les Cases d'un Switch doivent être du même type que l'Expression à tester, "
								+ "ce qui n'est pas le cas dans ce Switch");
			if (expressionToTest.getType() == EnumType.NUM) {
				if (!set_Test_Double.add(i.getNum()))
					throw new ParseException("Le case '" + i.getNum() + "' est présent plus d'une fois dans ce Switch");
			} else if (!set_Test_String.add(i.getString()))
				throw new ParseException("Le case '" + i.getString() + "' est présent plus d'une fois dans ce Switch");

		}
	}

	private void initialiseHM(List<Expression> pListCase, List<Expression> pListExp) throws ParseException {
		if (pListCase.size() != pListExp.size())
			throw new ParseException("Les tailles des listes pListCase et pListExp ne correspondent pas");

		while (!pListCase.isEmpty()) {
			if (HMcaseExp.put(pListCase.get(0), pListExp.get(0)) != null)
				throw new ParseException(
						"Le case '" + pListCase.get(0) + "' est présent plus d'une fois dans ce Switch");
			pListCase.remove(0);
			pListExp.remove(0);
		}
	}
}
