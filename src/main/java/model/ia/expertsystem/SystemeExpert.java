package main.java.model.ia.expertsystem;

import java.util.ArrayList;
import java.util.List;

import main.java.model.EDeplacement;
import main.java.model.Puzzle;
import main.java.model.ia.astar.IAAStar;

public class SystemeExpert {

	/**
	 * Résolution du puzzle à l'aide du système et expert et de A* lorsque le puzzle
	 * est réduit en 3x3
	 * 
	 * @param puzzle
	 * @return List<EDeplacement> solution
	 */
	public static List<EDeplacement> solveTaquin(Puzzle puzzle) {
		List<EDeplacement> solution = new ArrayList<>();
		try {
			Puzzle puzzleCopy = (Puzzle) puzzle.clone();
			while (puzzleCopy.getTaille() > 3) {
				solution.addAll(SystemeExpertLigne.solveLigne(puzzleCopy));
				solution.addAll(SystemeExpertColonne.solveColonne(puzzleCopy));
			}
			solution.addAll(IAAStar.solveTaquin(puzzleCopy));
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return solution;
	}
}
