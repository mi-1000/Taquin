package test.java.model.ia;

import java.awt.Point;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import main.java.model.Case;
import main.java.model.EDeplacement;
import main.java.model.Puzzle;
import main.java.model.ia.IA;
import main.java.model.ia.Noeud;
import main.java.model.ia.expertsystem.SystemeExpertColonne;
import main.java.model.ia.expertsystem.SystemeExpertLigne;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TestIA {

	private static Puzzle puzzle;
	private final static int TAILLE = 3;

	@BeforeAll
	public static void setUp() {
		puzzle = new Puzzle(TAILLE);
	}

	@Test
	@Order(1)
	public void testManhattanDistance() {
		Case[][] grilleCorrecte = new Case[TAILLE][TAILLE];
		Case[][] grilleIncorrecte = new Case[TAILLE][TAILLE];
		int compteur = 0;
		for (int i = 0; i < TAILLE; i++) {
			for (int j = 0; j < TAILLE; j++) {
				if (!(i == TAILLE - 1 && j == TAILLE - 1)) {
					grilleCorrecte[j][i] = new Case(compteur);
					grilleIncorrecte[j][i] = new Case(compteur);
				}
				compteur++;
			}
		}

		grilleCorrecte[TAILLE - 1][TAILLE - 1] = new Case(Case.INDEX_CASE_VIDE);
		int numChangement = grilleIncorrecte[TAILLE - 1][TAILLE - 2].getIndex();
		grilleIncorrecte[TAILLE - 1][TAILLE - 2] = new Case(Case.INDEX_CASE_VIDE);
		grilleIncorrecte[TAILLE - 1][TAILLE - 1] = new Case(numChangement);

		puzzle.setGrille(grilleCorrecte);
		int manhattan0 = new Noeud(puzzle).calculerH();
		Assertions.assertEquals(0, manhattan0, "La distance de manhattan de la grille de vrait être 0");

		puzzle.setGrille(grilleIncorrecte);
		int manhattan1 = new Noeud(puzzle).calculerH();
		Assertions.assertEquals(2, manhattan1, "La distance de manhattan de la grille de vrait être 2");
	}

	@Test
	@Order(2)
	public void testChercherCoordonneesIndex() {
		Case[][] grilleCorrecte = new Case[TAILLE][TAILLE];
		int compteur = 0;
		for (int i = 0; i < TAILLE; i++) {
			for (int j = 0; j < TAILLE; j++) {
				if (!(i == TAILLE - 1 && j == TAILLE - 1)) {
					grilleCorrecte[j][i] = new Case(compteur);
				}
				compteur++;
			}
		}

		grilleCorrecte[TAILLE - 1][TAILLE - 1] = new Case(Case.INDEX_CASE_VIDE);
		puzzle.setGrille(grilleCorrecte);
		Point index1 = new Point(IA.chercherCoordonneesIndex(1, puzzle).x, IA.chercherCoordonneesIndex(1, puzzle).y);
		Assertions.assertEquals(1, index1.x, "L'index 1 devrait se retrouver en x:1");
		Assertions.assertEquals(0, index1.y, "L'index 1 devrait se retrouver en y:0");
	}

	@Test
	@Order(3)
	public void testSystemeExpertLigne() throws CloneNotSupportedException {
		Case[][] grille = { { new Case(6), new Case(4), new Case(8), new Case(1) },
				{ new Case(13), new Case(9), new Case(10), new Case(12) },
				{ new Case(3), new Case(0), new Case(2), new Case(11) },
				{ new Case(Case.INDEX_CASE_VIDE), new Case(7), new Case(14), new Case(5) } };
		puzzle.setGrille(grille);
		List<EDeplacement> solution = SystemeExpertLigne.solveLigne((Puzzle) puzzle.clone());
		for (EDeplacement dp : solution) {
			puzzle.deplacerCase(dp);
		}

		// vérification de la ligne
		boolean check = true;
		for (int i = 0; i < grille.length; i++) {
			if (puzzle.getGrille()[i][0].getIndex() != i)
				check = false;
		}
		Assertions.assertTrue(check, "La première ligne de la grille devrait être résolue");
	}

	@Test
	@Order(3)
	public void testSystemeExpertColonne() throws CloneNotSupportedException {
		Case[][] grille = { { new Case(6), new Case(4), new Case(8), new Case(1) },
				{ new Case(13), new Case(9), new Case(10), new Case(12) },
				{ new Case(3), new Case(0), new Case(2), new Case(11) },
				{ new Case(Case.INDEX_CASE_VIDE), new Case(7), new Case(14), new Case(5) } };
		puzzle.setGrille(grille);
		List<EDeplacement> solution = SystemeExpertColonne.solveColonne((Puzzle) puzzle.clone());
		for (EDeplacement dp : solution) {
			puzzle.deplacerCase(dp);
		}

		// vérification de la ligne
		boolean check = true;
		for (int i = 1; i < grille.length; i++) {
			if (puzzle.getGrille()[0][i].getIndex() != (i*puzzle.getTaille()))
				check = false;
		}
		Assertions.assertTrue(check, "La première colonne de la grille devrait être résolue");
	}

}
