import java.util.HashMap;
import java.util.List;

public class For extends Expression {

	private final Variable myVariable;
	private final HashMap<String, Expression> variableHM;
	private final Expression expressionStart;
	private final Expression expressionEnd;
	private final List<Expression> listeExpression;

	public For(Variable pMyVariable, Expression pExpressionStart, Expression pExpressionEnd,
			List<Expression> pListeExpression) throws ParseException {
		this.myVariable = pMyVariable;
		this.variableHM = new HashMap<String, Expression>();
		this.expressionStart = pExpressionStart;
		this.expressionEnd = pExpressionEnd;
		this.listeExpression = pListeExpression;
	}

	@Override
	public void prettyPrint() {
		System.out.print("for(");
		this.myVariable.prettyPrint();
		System.out.print(" := ");
		this.expressionStart.prettyPrint();
		System.out.print(" to ");
		this.expressionEnd.prettyPrint();
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
		this.expressionStart.initialiseParentsPuisTypes(this.getParent()); // Cette Expression n'est pas censé avoir
		// accès à la variable du for
		this.expressionEnd.initialiseParentsPuisTypes(this.getParent()); // Idem

		this.initialiseVariableHM();
		this.myVariable.initialiseParentsPuisTypes(this);

		for (Expression i : this.listeExpression) {
			i.initialiseParentsPuisTypes(this);
		}

		this.initialiseTypes();
		this.verifications();
	}

	@Override
	public void execute() throws ParseException {
		this.expressionStart.execute();
		this.expressionEnd.execute();
//		if (this.expressionStart.getValue() > this.expressionEnd.getValue())
//			throw new ParseException(
//					"La 1ère Expression du block conditionnel de la boucle for doit avoir une valeur NUM supérieure à celle de la 2ème Expression");
		double start = expressionStart.getNum(), end = expressionEnd.getNum();
		for (double i = start; i <= end; i++) {
			((Num) this.myVariable.getExpressionDansHashMap()).setValue(i);
			for (Expression j : this.listeExpression) {
				j.execute();
			}
		}
	}

	@Override
	public boolean hasVariableHashMap() {
		return true;
	}

	@Override
	public HashMap<String, Expression> getVariableHM() throws ParseException {
		return variableHM;
	}

	private void verifications() throws ParseException {
		if (!myVariable.isInForLoop())
			throw new ParseException("La variable de la boucle for n'a pas été initialisée comme il faut");

		if (this.myVariable.getType() != EnumType.NUM)
			throw new ParseException("La variable du block conditionnel de la boucle for ne peut pas être de type'"
					+ this.myVariable.getType() + "', elle doit être de type NUM");
		else if (this.expressionStart.getType() != EnumType.NUM)
			throw new ParseException("La variable du block conditionnel de la boucle for ne peut pas être de type'"
					+ this.expressionStart.getType() + "', elle doit être de type NUM");
		else if (this.expressionEnd.getType() != EnumType.NUM)
			throw new ParseException("La variable du block conditionnel de la boucle for ne peut pas être de type'"
					+ this.expressionEnd.getType() + "', elle doit être de type NUM");
	}

	public void initialiseVariableHM() throws ParseException {
		Num vNum = new Num(this.expressionStart.getNum());
		vNum.initialiseParentsPuisTypes(new NullExpression());
		this.variableHM.put(this.myVariable.getName(), vNum); // On met la variable
																// dans
		// la HashMap
	}

}
