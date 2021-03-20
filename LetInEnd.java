import java.util.HashMap;
import java.util.List;

public class LetInEnd extends Expression {

	private List<String> listeToTreatNames;
	private List<Expression> listeToTreatExpressions;
	private final HashMap<String, Expression> variableHM;
	private final List<Expression> listeExpression;

	public LetInEnd(ListeNamesAndExpressions pListeNamesAndExpressions, List<Expression> pListExpression)
			throws ParseException {
		this.listeToTreatNames = pListeNamesAndExpressions.getListeToTreatNames();
		this.listeToTreatExpressions = pListeNamesAndExpressions.getListeToTreatExpressions();
		this.variableHM = new HashMap<String, Expression>();
		this.listeExpression = pListExpression;
	}

	@Override
	public void prettyPrint() {
		System.out.println("let(");

		for (String i : variableHM.keySet()) {
			System.out.print("var " + i + " := ");
			this.variableHM.get(i).prettyPrint();
			System.out.println();
		}

		System.out.println(")in(");

		for (int i = 0; i < listeExpression.size(); i++) {
			listeExpression.get(i).prettyPrint();
			if (i < listeExpression.size() - 1)
				System.out.print(";");
			System.out.println();
		}
		System.out.print(")end");
	}

	@Override
	public double getNum() throws ParseException {
		if (this.getType() == EnumType.NUM)
			return this.listeExpression.get(listeExpression.size() - 1).getNum();
		else
			throw new ParseException("Ce 'LET IN END' est de type '" + this.getType() + "', il ne retourne pas de NUM");
	}

	@Override
	public String getString() throws ParseException {
		if (this.getType() == EnumType.STRING)
			return this.listeExpression.get(listeExpression.size() - 1).getString();
		else
			throw new ParseException(
					"Ce 'LET IN END' est de type '" + this.getType() + "', il ne retourne pas de STRING");
	}

	@Override
	public void initialiseTypes() throws ParseException {
		if (listeExpression.size() > 0) {
			Expression vLastExpression = this.listeExpression.get(listeExpression.size() - 1);
			if (vLastExpression.getType() == null)
				throw new ParseException("Le type de la dernière expression du LET IN END n'a pas été initialisé");
			else
				this.myType = vLastExpression.getType();
		}
		else
			this.myType = EnumType.VOID;
	}

	@Override
	public void initialiseParentsPuisTypes(Expression pParent) throws ParseException {
		this.setParent(pParent);

		this.initialiseVariableHM(); // On initialise la liste des variables de la fonction avant d'initialiser les
										// Expressions du block in. Sinon cela poserait probleme pour les Let In end
										// imbriques.

		for (Expression i : listeExpression) {
			i.initialiseParentsPuisTypes(this);
		}

		this.initialiseTypes();
	}

	@Override
	public boolean hasVariableHashMap() {
		return true;
	}

	@Override
	public HashMap<String, Expression> getVariableHM() throws ParseException {
		return variableHM;
	}

	@Override
	public void execute() throws ParseException {
		for (int i = 0; i < listeExpression.size(); i++) {
			this.listeExpression.get(i).execute();
		}
	}

	/*
	 * Initialise la HashMap de variables grace à la HashMap d'Expression donnee
	 * dans le constructeur. Toutes les Expressions de toTreatHM sont transformées
	 * soit en TigerString soit en Num.
	 */
	private void initialiseVariableHM() throws ParseException {
		while (!listeToTreatNames.isEmpty()) {
			String varName = listeToTreatNames.get(0);
			Expression varExpression = listeToTreatExpressions.get(0);
			
			if(this.variableHM.containsKey(varName))
				throw new ParseException(
						"La variable '" + varName + "' ne peut pas être déclarée plusieurs fois dans le block Let de ce LET IN END");

			varExpression.initialiseParentsPuisTypes(this);

			// System.out.println(i);
			switch (varExpression.getType()) {
			case NUM:
				Num vNum = new Num(varExpression.getNum());
				vNum.initialiseParentsPuisTypes(new NullExpression());
				variableHM.put(varName, vNum);
				break;
			case STRING:
				TigerString vTigerString = new TigerString(varExpression.getString());
				vTigerString.initialiseParentsPuisTypes(new NullExpression());
				this.variableHM.put(varName, vTigerString);
				break;
			case VOID:
				throw new ParseException("Une Expression de type VOID ne peut pas être utilisée dans une déclaration");
			default:
				throw new ParseException(
						"Le type '" + varExpression.getType() + "' n'a pas été pris en compte dans cette fonction");
			}

			listeToTreatNames.remove(0);
			listeToTreatExpressions.remove(0);
		}
	}

}
