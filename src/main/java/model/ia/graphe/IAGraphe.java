package main.java.model.ia.graphe;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

import main.java.model.EDeplacement;
import main.java.model.Puzzle;
import main.java.model.ia.IA;
import main.java.model.ia.Noeud;

public class IAGraphe {

	/**
	 * Resolution du taquin avec parcours de graphe, avec ouverts.element() on a une
	 * file FIFO
	 * 
	 * @param puzzle, Puzzle à résoudre
	 * @return List<EDeplacement>, solution du taquin 3x3
	 */
	public static List<EDeplacement> solveTaquin(Puzzle puzzle) {
		List<EDeplacement> solution = new ArrayList<>();

		if (puzzle.getTaille() == 3) {
			Deque<Noeud> ouverts = new LinkedList<>();
			ouverts.add(new Noeud(puzzle));

			Deque<Noeud> fermes = new LinkedList<>();
			boolean succes = false;
			while (!ouverts.isEmpty() && !succes) {
				Noeud n = ouverts.element();
				if (n.getPuzzle().verifierGrille())
					succes = true;
				else {
					ouverts.remove(n);
					fermes.add(n);
					for (Noeud s : n.successeurs()) {
						if (!ouverts.contains(s) && !fermes.contains(s)) {
							ouverts.add(s);
						}
					}
				}
			}
			Noeud noeudTmp = IA.chercherNoeudResolu(ouverts);
			while (noeudTmp.getPere() != null) {
				solution.add(noeudTmp.getdeplacement());
				noeudTmp = noeudTmp.getPere();
			}
			Collections.reverse(solution);
		}
		return solution;
	}

}
