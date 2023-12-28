package main.java;

import java.util.Scanner;

import main.java.model.EDeplacement;
import main.java.model.Puzzle;

public class JeuConsole {

	private Puzzle puzzle;

	public JeuConsole() {
		this.puzzle = new Puzzle(4);
		this.lancerJeuConsole();
	}

	private void lancerJeuConsole() {
		Scanner sc = new Scanner(System.in);
		do {
			String dp = "";
			do {
				System.out.println(puzzle.toStringConsole());
				System.out.println("\nQuel mouvement voulez-vous faire? \n'h':haut, 'b':bas, 'g':gauche, 'd':droite");

				dp = sc.nextLine();
				dp = dp.toLowerCase();
			} while (!dp.equals("g") && !dp.equals("d") && !dp.equals("b") && !dp.equals("h"));
			switch (dp) {
			case "g":
				puzzle.deplacerCase(EDeplacement.GAUCHE);
				break;
			case "d":
				puzzle.deplacerCase(EDeplacement.DROITE);
				break;
			case "b":
				puzzle.deplacerCase(EDeplacement.BAS);
				break;
			case "h":
				puzzle.deplacerCase(EDeplacement.HAUT);
				break;
			}
		} while (!puzzle.verifierGrille());
		System.out.println("Vous avez résolu le puzzle en " + puzzle.getNbCoups() + " coup(s)! Féliciations!");
	}
}
