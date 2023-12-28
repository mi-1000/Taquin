package main.java.model.ia.random;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import main.java.model.EDeplacement;
import main.java.model.Puzzle;
import main.java.model.ia.Noeud;
import main.java.utils.Utils;

public class IARandom {

	/**
	 * Solveur en déplacements aléatoires (taille < 4)
	 * 
	 * @param puzzle
	 * @return
	 */
	public static List<EDeplacement> solveTaquin(Puzzle puzzle) {
		List<EDeplacement> solution = new ArrayList<>();

		if (puzzle.getTaille() == 3) {
			boolean succes = false;
			Noeud noeudCourant = new Noeud(puzzle);
			while (!succes) {
				if (noeudCourant.getPuzzle().verifierGrille()) {
					succes = true;
				}
				List<Noeud> successeurs = noeudCourant.successeurs();
				Noeud successeurChoisi;
				do {
					int random = Utils.getRandomNumberInRange(0, successeurs.size() - 1);
					successeurChoisi = successeurs.get(random);
				} while (successeurChoisi.deplacementNulPere());
				successeurChoisi.setPere(noeudCourant);
				noeudCourant = successeurChoisi;
			}

			if(noeudCourant.getPuzzle().getXCaseVide() == noeudCourant.getPuzzle().getTaille() - 1) {
				solution.add(EDeplacement.HAUT);
			}else {
				solution.add(EDeplacement.GAUCHE);
			}
			do{
				solution.add(noeudCourant.getdeplacement());
				noeudCourant = noeudCourant.getPere();
			}while (noeudCourant.getPere() != null);
		}
		Collections.reverse(solution);
		return solution;
	}

}
