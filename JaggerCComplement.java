import java.util.Stack;

/*
 * Cette classe est du code java normalement present dans le JaggerCC.JJ
 */
public class JaggerCComplement {
	/*
	 * Cette fonction prend en entrée une liste de cette forme '(a) (signe
	 * comparateur) (b) (signe comparateur) (c) (signe comparateur)...' (cette list
	 * a été dibisée en 2 stacks à cause du fait qu'il y a 2 types différents),
	 * 
	 * et elle retourne un AST en lisant la liste de droite à gauche (JaggerCC.jj
	 * lis de gauche à droite) afin d'avoir une liste de comparateurs dont les
	 * membres à gauche sont prioritaires (voir des exemples dans tests.txt pour
	 * mieux comprendre.
	 */
	public static Expression arrayToComparateurs(Stack<String> stackSigne, Stack<Expression> stackExpr)
			throws ParseException {

		if (stackSigne.isEmpty()) {
			if (!stackExpr.isEmpty()) {
				Expression expr = stackExpr.pop();
				if (stackExpr.isEmpty())
					return expr;
			}
			throw new ParseException(
					"Incoherence entre les deux stacks stackSigne et stackExpr, les tailles des stacks ne correspondent pas");

		} else {
			String signeComp = stackSigne.pop();
			Expression expr = stackExpr.pop();
			return new BinopAST(signeComp, arrayToComparateurs(stackSigne, stackExpr), expr);
		}
	}
}
