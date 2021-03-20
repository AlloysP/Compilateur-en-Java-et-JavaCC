public class BinopAST extends Expression {

	/*
	 * Enum permettant d'identifier quel type d'operation binaire il s'agit
	 */
	public enum BinopSignes {
		PLUS("+"), MOINS("-"), MULTI("*"), DIV("/"), DIF("<>"), EQ("="), INF("<"), SUP(">"), INFEQ("<="), SUPEQ(">="),
		OR("|"), AND("&");

		BinopSignes(String pSignString) {
			this.signString = pSignString;
		}

		private final String signString;

		public String getSignString() {
			return signString;
		}
	};

	private BinopSignes binopS;
	private Expression expression_1; // Expression gauche
	private Expression expression_2; // Expression droite

	public BinopAST(String pBinopString, Expression pexpression_1, Expression pexpression_2) throws ParseException {
		this.binopS = BinopSignes.valueOf(pBinopString);
		this.expression_1 = pexpression_1;
		this.expression_2 = pexpression_2;
	}

	@Override
	public void prettyPrint() {
		System.out.print("(");
		this.expression_1.prettyPrint();
		System.out.print(binopS.getSignString());
		this.expression_2.prettyPrint();
		System.out.print(")");
	}

	@Override
	public double getNum() throws ParseException {
		if (this.myType == EnumType.NUM) { // On verifie qu'il n'y a pas de problème de type
			boolean vBool = false;
			switch (binopS.getSignString()) {
			case "+":
				return expression_1.getNum() + expression_2.getNum();
			case "-":
				return expression_1.getNum() - expression_2.getNum();
			case "*":
				return expression_1.getNum() * expression_2.getNum();
			case "/":
				if (expression_2.getNum() != 0.0)
					return expression_1.getNum() / expression_2.getNum();
				else
					throw new ParseException("Division par 0 impossible");
			case "<>":
				if (expression_1.getType() == EnumType.STRING)
					vBool = !(expression_1.getString().equals(expression_2.getString()));
				else
					vBool = expression_1.getNum() != expression_2.getNum();
				if (vBool)
					return 1.0;
				else
					return 0.0;
			case "=":
				if (expression_1.getType() == EnumType.STRING)
					vBool = expression_1.getString().equals(expression_2.getString());
				else
					vBool = expression_1.getNum() == expression_2.getNum();
				if (vBool)
					return 1.0;
				else
					return 0.0;
			case "<":
				if (expression_1.getType() == EnumType.STRING)
					vBool = (expression_1.getString().compareTo(expression_2.getString()) < 0);
				else
					vBool = expression_1.getNum() < expression_2.getNum();
				if (vBool)
					return 1.0;
				else
					return 0.0;
			case ">":
				if (expression_1.getType() == EnumType.STRING)
					vBool = (expression_1.getString().compareTo(expression_2.getString()) > 0);
				else
					vBool = expression_1.getNum() > expression_2.getNum();
				if (vBool)
					return 1.0;
				else
					return 0.0;
			case "<=":
				if (expression_1.getType() == EnumType.STRING)
					vBool = (expression_1.getString().compareTo(expression_2.getString()) <= 0);
				else
					vBool = expression_1.getNum() <= expression_2.getNum();
				if (vBool)
					return 1.0;
				else
					return 0.0;
			case ">=":
				if (expression_1.getType() == EnumType.STRING)
					vBool = (expression_1.getString().compareTo(expression_2.getString()) >= 0);
				else
					vBool = expression_1.getNum() >= expression_2.getNum();
				if (vBool)
					return 1.0;
				else
					return 0.0;
			case "&":
				vBool = expression_1.getNum() == 1.0 && expression_2.getNum() == 1.0;
				if (vBool)
					return 1.0;
				else
					return 0.0;
			case "|":
				vBool = expression_1.getNum() == 1.0 || expression_2.getNum() == 1.0;
				if (vBool)
					return 1.0;
				else
					return 0.0;
			default:
				throw new ParseException(binopS.getSignString() + " n'est pas une opérande binaire reconnue");
			}
		} else
			throw new ParseException("Cette expression '" + this.getClass().getName() + "' est de type '" + this.myType
					+ "', elle ne retourne pas de 'NUM'");
	}

	/*
	 * Dans ce cas-ci on concatène le membre gauche avec le membre droit (au moins
	 * un des 2 est un String)
	 */
	@Override
	public String getString() throws ParseException {
		if (this.myType == EnumType.STRING) {
			if (binopS.getSignString() == "+") { // Il n'y a que l'operation binaire '+' qui peut retourner un String
				String string_1, string_2;

				if (expression_1.getType() == EnumType.STRING)
					string_1 = expression_1.getString();
				else if ((int) expression_1.getNum() == expression_1.getNum()) // Si il n'y a pas de partie decimale
																					// on
																					// cast en Integer pour que ce soit
																					// plus jolie
					string_1 = Integer.toString((int) expression_1.getNum());
				else
					string_1 = Double.toString(expression_1.getNum());

				if (expression_2.getType() == EnumType.STRING)
					string_2 = expression_2.getString();
				else if ((int) expression_2.getNum() == expression_2.getNum()) // Si il n'y a pas de partie decimale
																					// on
																					// cast en Integer pour que ce soit
																					// plus jolie
					string_2 = Integer.toString((int) expression_2.getNum());
				else
					string_2 = Double.toString(expression_2.getNum());

				return string_1 + string_2;
			} else {
				throw new ParseException("Cette opération binaire ne retourne pas une String");
			}
		} else
			throw new ParseException("Cette expression '" + this.getClass().getName() + "' est de type '" + this.myType
					+ "', elle ne retourne pas de 'STRING'");
	}

	@Override
	public void initialiseTypes() throws ParseException {
		String vSignString = binopS.getSignString();

		if (vSignString == "+" // Il n'y a que l'operation binaire '+' qui peut retourner un String
				&& (expression_1.getType() == EnumType.STRING || expression_2.getType() == EnumType.STRING))
			this.myType = EnumType.STRING;
		else
			this.myType = EnumType.NUM;

		this.verification();
	}

	@Override
	public void initialiseParentsPuisTypes(Expression pParent) throws ParseException {
		this.setParent(pParent);
		this.expression_1.initialiseParentsPuisTypes(this);
		this.expression_2.initialiseParentsPuisTypes(this);

		this.initialiseTypes();
	}

	@Override
	public void execute() throws ParseException {
		this.expression_1.execute();
		this.expression_2.execute();
	}

	private void verification() throws ParseException {
		String vSignString = binopS.getSignString();

		if (vSignString == "-" | vSignString == "*" | vSignString == "/" | vSignString == "&" | vSignString == "|")
			if (expression_1.getType() == EnumType.STRING || expression_2.getType() == EnumType.STRING)
				throw new ParseException("Le binop '" + binopS.getSignString() + "' ne traite que des Num");

		if (vSignString == "<>" | vSignString == "=" | vSignString == "<" | vSignString == ">" | vSignString == "<="
				| vSignString == ">=")
			if (expression_1.getType() == EnumType.STRING || expression_2.getType() == EnumType.STRING)
				if (expression_1.getType() != EnumType.STRING || expression_2.getType() != EnumType.STRING)
					throw new ParseException("Le binop '" + binopS.getSignString()
							+ "' ne peut pas traiter un NUM d'un côté et un STRING de l'autre, "
							+ "il faut soit mettre 2 STRINGs ou soit mettre 2 NUMs");
		if (expression_1.getType() == EnumType.VOID | expression_2.getType() == EnumType.VOID)
			throw new ParseException(
					"Une des 2 Expressions est de type VOID, hors il faut que les 2 Expressions "
					+ "d'une opération binaire retournent une valeur");
	}

}
