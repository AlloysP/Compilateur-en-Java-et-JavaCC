import java.util.List;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

public class Start {

	public static void main(String args[]) {
		if (args.length == 0)
			try {
				Jagger.main();
			} catch (ParseException e) {
				e.printStackTrace();
			}
		else {
			List<File> vListeFile = new ArrayList<File>();
			Start.listf(args[0], vListeFile);
			while (!vListeFile.isEmpty()) {
				try {
					File vFile = vListeFile.get(0);
					System.out.println();
					System.out.println("==================================================> "
							+ vFile.getParentFile().getName() + "/" + vFile.getName());
					Jagger.main(vFile);
				} catch (ParseException e) {
					e.printStackTrace();
				}
				vListeFile.remove(0);
			}
		}
	}

	/*
	 * treatThisAST() est la fonction appelee par le Parseur une fois que celui-ci a
	 * finis de creer l'AST (l'AST est donne en parametre à cette fonction).
	 */
	public static void treatThisAST(Expression pAST) {
		pAST.prettyPrint(); // On affiche l'arbre
		System.out.print("\n=> ");
		try {
			pAST.execute();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		System.out.println("\n");
	}

	public static void listf(String directoryName, List<File> files) {
		File directory = new File(directoryName);

		// Get all files from a directory.
		File[] fList = directory.listFiles();
		Arrays.sort(fList);
		if (fList != null)
			for (File file : fList) {
				if (file.isFile()) {
					files.add(file);
				} else if (file.isDirectory()) {
					listf(file.getAbsolutePath(), files);
				}
			}
	}
}
