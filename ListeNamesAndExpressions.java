import java.util.List;

/*
 * Classe qui fait office de structure (comme en C) afin de contenir les deux listes qui permettent de declarer les variables.
 */
class ListeNamesAndExpressions {

	private List<String> listeToTreatNames;
	private List<Expression> listeToTreatExpressions;

	public ListeNamesAndExpressions(List<String> pListeToTreatNames, List<Expression> pListeToTreatExpressions) {
		this.listeToTreatNames = pListeToTreatNames;
		this.listeToTreatExpressions = pListeToTreatExpressions;
	}

	public List<String> getListeToTreatNames() {
		return listeToTreatNames;
	}

	public List<Expression> getListeToTreatExpressions() {
		return listeToTreatExpressions;
	}
}