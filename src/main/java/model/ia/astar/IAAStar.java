package main.java.model.ia.astar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

import main.java.model.EDeplacement;
import main.java.model.Puzzle;
import main.java.model.ia.IA;
import main.java.model.ia.Noeud;

public class IAAStar {

	/**
	 * Resolution du taquin avec l'algorithme A*, utilisation de la distance de
	 * manhattan, cherche une solution uniquement si la taille du puzzle est égale à
	 * 3
	 * 
	 * @param puzzle
	 * @return List<EDeplacement> solution
	 */
	public static List<EDeplacement> solveTaquin(Puzzle puzzle) {
		List<EDeplacement> solution = new ArrayList<>();
		Deque<Noeud> ouverts = new LinkedList<>();

		if (puzzle.getTaille() == 3) {
			ouverts.add(new Noeud(puzzle));

			Deque<Noeud> fermes = new LinkedList<>();
			boolean succes = false;
			// tant qu'on a des ouverts et que le taquin n'est pas résolu
			while (!ouverts.isEmpty() && !succes) {
				Noeud n = IA.getMinimum(ouverts);
				if (n.getPuzzle().verifierGrille())
					succes = true;
				else {
					ouverts.remove(n);
					fermes.add(n);
					// pour tous les successeurs
					for (Noeud s : n.successeurs()) {
						if (!ouverts.contains(s) && !fermes.contains(s)) {
							ouverts.add(s);
							s.setPere(n);
						} else {
							if (s.getG() > n.getG() + Math.abs(s.getG() - n.getG())) {
								s.setPere(n);
								if (fermes.contains(s)) {
									fermes.remove(s);
									ouverts.add(s);
								}
							}
						}
					}
				}
			}

			// une fois résolu on retrace l'arbre pour faire la liste de déplacements
			Noeud noeudTmp = IA.chercherNoeudResolu(ouverts);
			while (noeudTmp.getPere() != null) {
				solution.add(noeudTmp.getdeplacement());
				noeudTmp = noeudTmp.getPere();
			}
			Collections.reverse(solution);
			for (EDeplacement dp : solution) {
				puzzle.deplacerCase(dp);
			}
		}
		return solution;
	}
}
