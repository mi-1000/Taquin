package main.java.model.ia;

import java.util.List;

import main.java.model.EDeplacement;
import main.java.model.Puzzle;
import main.java.model.ia.expertsystem.SystemeExpert;

public class TestIA {

	public static void main(String[] args) {
		Puzzle puzzle = new Puzzle(4);
		System.out.println(puzzle);
		List<EDeplacement> solution = SystemeExpert.solveTaquin(puzzle);
		System.out.println("************************ SOLUTION TROUVEE *************************");
		for (EDeplacement dp : solution) {
			puzzle.deplacerCase(dp);
			System.out.println(puzzle);
		}
		System.out.println("NB COUPS: " + solution.size());
	}
}
