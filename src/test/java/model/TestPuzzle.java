package test.java.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import main.java.model.Case;
import main.java.model.EDeplacement;
import main.java.model.Puzzle;
import main.java.utils.Utils;

public class TestPuzzle {

	private static Puzzle puzzle;
	private final static int TAILLE = 3;
	private int oldX, oldY, newX, newY;
	private Case[][] grille;

	@BeforeAll
	public static void setUp() {
		puzzle = new Puzzle(TAILLE);
	}

	@Test
	public void testConstructor1() {
		int tailleMauvaise = 2;
		Puzzle p = new Puzzle(tailleMauvaise);
		Assertions.assertTrue(p.getGrille().length > 2,
				"La taille du puzzle devrait être supérieure à 2, actuellement:" + tailleMauvaise);
	}

	@Test
	public void testConstructor2() {
		int tailleMauvaise = -1;
		Puzzle p2 = new Puzzle(tailleMauvaise);
		Assertions.assertTrue(p2.getGrille().length > 2,
				"La taille du puzzle devrait être supérieure à 2, actuellement:" + tailleMauvaise);
	}

	@Test
	public void testConstructor3() {
		int bonneTaille = 5;
		Puzzle p = new Puzzle(bonneTaille);
		Assertions.assertEquals(bonneTaille, p.getTaille(),
				"La taille du puzzle devrait être " + bonneTaille + ", actuellement:" + p.getTaille());
	}

	@Test
	public void testConstructorCase() {
		int indexCaseVide = Case.INDEX_CASE_VIDE;
		Case caseVide = new Case(indexCaseVide);
		Assertions.assertEquals(indexCaseVide, caseVide.getIndex(),
				"On devrait avoir une case vide construite avec l'index " + indexCaseVide + " mais on a "
						+ caseVide.getIndex());
	}

	@Test
	public void testMelangerGrille() {
		Case[][] grille1 = puzzle.getGrille().clone();
		puzzle.melanger();
		Case[][] grille2 = puzzle.getGrille();

		Assertions.assertNotEquals(grille1, grille2, "Les 2 grilles sont identiques, elles n'ont pas été mélangées.");
	}

	@Test
	public void verifierGrille() {
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
		Assertions.assertTrue(puzzle.verifierGrille(), "La grille devrait être correcte");

		puzzle.setGrille(grilleIncorrecte);
		Assertions.assertFalse(puzzle.verifierGrille(), "La grille devrait être incorrecte");
	}

	@Test
	public void testDeplacerCaseHaut() {
		// on prépare la grille avec une case vide qui ne se trouve pas en bas
		puzzle.melanger();
		grille = puzzle.getGrille();
		int x, y, posX, posY;
		x = 1;
		y = 1;
		posX = puzzle.getXCaseVide();
		posY = puzzle.getYCaseVide();
		Case caseVide = puzzle.getCase(posX, posY);
		Case caseSwap = puzzle.getCase(x, y);
		grille[posX][posY] = caseSwap;
		grille[x][y] = caseVide;

		oldX = puzzle.getXCaseVide();
		oldY = puzzle.getYCaseVide();

		puzzle.deplacerCase(EDeplacement.HAUT);

		newX = puzzle.getXCaseVide();
		newY = puzzle.getYCaseVide();
		Assertions.assertEquals(oldY, newY - 1,
				"La case vide se trouve en " + newY + ", elle devait se trouver en " + (newY - 1));
	}

	@Test
	public void testDeplacerCaseHautImpossible() {
		// on prépare la grille avec une case vide qui se trouve tout en bas
		puzzle.melanger();
		grille = puzzle.getGrille();
		int x, y, posX, posY;
		x = 1;
		y = this.TAILLE - 1;
		posX = puzzle.getXCaseVide();
		posY = puzzle.getYCaseVide();
		Case caseVide = puzzle.getCase(posX, posY);
		Case caseSwap = puzzle.getCase(x, y);
		grille[posX][posY] = caseSwap;
		grille[x][y] = caseVide;

		oldX = puzzle.getXCaseVide();
		oldY = puzzle.getYCaseVide();

		puzzle.deplacerCase(EDeplacement.HAUT);

		newX = puzzle.getXCaseVide();
		newY = puzzle.getYCaseVide();
		Assertions.assertEquals(oldY, newY,
				"Le déplacement vers le haut n'est pas possible lorsque la case vide se trouve tout en bas (" + oldY
						+ ") or elle se trouve maintenant en " + newY);
	}

	@Test
	public void testDeplacerCaseGauche() {
		// on prépare la grille avec une case vide qui ne se trouve pas à droite
		puzzle.melanger();
		grille = puzzle.getGrille();
		int x, y, posX, posY;
		x = 1;
		y = 1;
		posX = puzzle.getXCaseVide();
		posY = puzzle.getYCaseVide();
		Case caseVide = puzzle.getCase(posX, posY);
		Case caseSwap = puzzle.getCase(x, y);
		grille[posX][posY] = caseSwap;
		grille[x][y] = caseVide;

		oldX = puzzle.getXCaseVide();
		oldY = puzzle.getYCaseVide();

		puzzle.deplacerCase(EDeplacement.GAUCHE);

		newX = puzzle.getXCaseVide();
		newY = puzzle.getYCaseVide();

		Assertions.assertEquals(oldX, newX - 1,
				"La case vide se trouve en " + newX + ", elle devait se trouver en " + (newX - 1));
	}

	@Test
	public void testDeplacerCaseGaucheImpossible() {
		// on prépare la grille avec une case vide qui se trouve tout à droite
		puzzle.melanger();
		grille = puzzle.getGrille();
		int x, y, posX, posY;
		x = this.TAILLE - 1;
		y = 1;
		posX = puzzle.getXCaseVide();
		posY = puzzle.getYCaseVide();
		Case caseVide = puzzle.getCase(posX, posY);
		Case caseSwap = puzzle.getCase(x, y);
		grille[posX][posY] = caseSwap;
		grille[x][y] = caseVide;

		oldX = puzzle.getXCaseVide();
		oldY = puzzle.getYCaseVide();

		puzzle.deplacerCase(EDeplacement.HAUT);

		newX = puzzle.getXCaseVide();
		newY = puzzle.getYCaseVide();
		Assertions.assertEquals(oldX, newX,
				"Le déplacement vers la gauche n'est pas possible lorsque la case vide se trouve tout à droite (" + oldX
						+ ") or elle se trouve maintenant en " + newX);
	}

	@Test
	public void testDeplacerCaseBas() {
		// on prépare la grille avec une case vide qui ne se trouve pas en bas
		puzzle.melanger();
		grille = puzzle.getGrille();
		int x, y, posX, posY;
		x = 1;
		y = 1;
		posX = puzzle.getXCaseVide();
		posY = puzzle.getYCaseVide();
		Case caseVide = puzzle.getCase(posX, posY);
		Case caseSwap = puzzle.getCase(x, y);
		grille[posX][posY] = caseSwap;
		grille[x][y] = caseVide;

		oldX = puzzle.getXCaseVide();
		oldY = puzzle.getYCaseVide();

		puzzle.deplacerCase(EDeplacement.BAS);

		newX = puzzle.getXCaseVide();
		newY = puzzle.getYCaseVide();
		Assertions.assertEquals(oldY, newY + 1,
				"La case vide se trouve en " + newY + ", elle devait se trouver en " + (newY + 1));
	}

	@Test
	public void testDeplacerCaseBasImpossible() {
		// on prépare la grille avec une case vide qui se trouve tout en haut
		puzzle.melanger();
		Case[][] grille = puzzle.getGrille();
		int x, y, posX, posY;
		x = 1;
		y = 0;
		posX = puzzle.getXCaseVide();
		posY = puzzle.getYCaseVide();
		Case caseVide = puzzle.getCase(posX, posY);
		Case caseSwap = puzzle.getCase(x, y);
		grille[posX][posY] = caseSwap;
		grille[x][y] = caseVide;

		oldX = puzzle.getXCaseVide();
		oldY = puzzle.getYCaseVide();

		puzzle.deplacerCase(EDeplacement.BAS);

		newX = puzzle.getXCaseVide();
		newY = puzzle.getYCaseVide();
		Assertions.assertEquals(oldY, newY,
				"Le déplacement vers le haut n'est pas possible lorsque la case vide se trouve tout en haut(" + oldY
						+ ") or elle se trouve maintenant en " + newY);
	}

	@Test
	public void testDeplacerCaseDroit() {
		// on prépare la grille avec une case vide qui ne se trouve pas à gauche
		puzzle.melanger();
		Case[][] grille = puzzle.getGrille();
		int x, y, posX, posY;
		x = 1;
		y = 1;
		posX = puzzle.getXCaseVide();
		posY = puzzle.getYCaseVide();
		Case caseVide = puzzle.getCase(posX, posY);
		Case caseSwap = puzzle.getCase(x, y);
		grille[posX][posY] = caseSwap;
		grille[x][y] = caseVide;

		oldX = puzzle.getXCaseVide();
		oldY = puzzle.getYCaseVide();

		puzzle.deplacerCase(EDeplacement.DROITE);

		newX = puzzle.getXCaseVide();
		newY = puzzle.getYCaseVide();

		Assertions.assertEquals(oldX, newX + 1,
				"La case vide se trouve en " + newX + ", elle devait se trouver en " + (newX + 1));
	}

	@Test
	public void testDeplacerCaseDroitImpossible() {
		// on prépare la grille avec une case vide qui se trouve tout à gauche
		puzzle.melanger();
		Case[][] grille = puzzle.getGrille();
		int x, y, posX, posY;
		x = 0;
		y = 1;
		posX = puzzle.getXCaseVide();
		posY = puzzle.getYCaseVide();
		Case caseVide = puzzle.getCase(posX, posY);
		Case caseSwap = puzzle.getCase(x, y);
		grille[posX][posY] = caseSwap;
		grille[x][y] = caseVide;

		oldX = puzzle.getXCaseVide();
		oldY = puzzle.getYCaseVide();

		puzzle.deplacerCase(EDeplacement.DROITE);

		newX = puzzle.getXCaseVide();
		newY = puzzle.getYCaseVide();
		Assertions.assertEquals(oldX, newX,
				"Le déplacement vers la gauche n'est pas possible lorsque la case vide se trouve tout à gauche (" + oldX
						+ ") or elle se trouve maintenant en " + newX);
	}

	@Test
	public void testGetCoordonneesCaseVide() {
		int x, y;
		x = puzzle.getXCaseVide();
		y = puzzle.getYCaseVide();

		int valeurCaseRecuperee, valeurAttendue;
		valeurCaseRecuperee = puzzle.getGrille()[x][y].getIndex();
		valeurAttendue = Case.INDEX_CASE_VIDE;

		Assertions.assertEquals(valeurCaseRecuperee, valeurAttendue, "Normalement la case vide doit avoir comme index:"
				+ valeurAttendue + " or l'index de la case récupérée est:" + valeurCaseRecuperee);
	}

	@Test
	public void testGetCaseArrayIndexOutOfBounds() {
		int xAbsurde = this.TAILLE + 1;
		int y = 0;

		Assertions.assertThrows(ArrayIndexOutOfBoundsException.class, () -> {
			Case caseTmp = puzzle.getCase(xAbsurde, y);
		});
	}

	@Test
	public void testDecoupageImage() throws ArrayIndexOutOfBoundsException, IOException {
		File file = new File("src/test/resources/testimg.jpg");
		byte[] image = Files.readAllBytes(file.toPath());
		Puzzle pTest = new Puzzle(4, image);
		for (int i = 0; i < pTest.getTaille(); i++) {
			for (int j = 0; j < pTest.getTaille(); j++) {
				File fi = new File("src/test/resources/image"+pTest.getCase(j, i).getIndex()+".png");
				byte[] img = Files.readAllBytes(fi.toPath());
				Assertions.assertTrue(
								Utils.compareByteArrays(img, pTest.getCase(j, i).getImage()),
								"Les images ne correspondent pas aux images attendues en x: "
										 + j + " y: " + i + ".");
			}
		}
	}
	
	@Test
	public void testListeDeplacementsPossibles() {
		List<EDeplacement> res = new ArrayList<>();
		Case[][] grilleTest = new Case[TAILLE][TAILLE];
		int compteur = 0;
		for (int i = 0; i < TAILLE; i++) {
			for (int j = 0; j < TAILLE; j++) {
				if (!(i == TAILLE - 1 && j == TAILLE - 1)) {
					grilleTest[j][i] = new Case(compteur);
				}
				compteur++;
			}
		}

		grilleTest[TAILLE - 1][TAILLE - 1] = new Case(Case.INDEX_CASE_VIDE);
		puzzle.setGrille(grilleTest);
		
		res = puzzle.listeDeplacementsPossibles();

		assertTrue(res.contains(EDeplacement.BAS));
		assertTrue(res.contains(EDeplacement.DROITE));
	}
	
	@Test
	public void testInverseDeplacement() {
		EDeplacement dpH = EDeplacement.HAUT;
		EDeplacement dpB = EDeplacement.BAS;
		EDeplacement dpG = EDeplacement.GAUCHE;
		EDeplacement dpD = EDeplacement.DROITE;
		
		assertEquals(EDeplacement.HAUT, puzzle.inverseDeplacement(dpB));
		assertEquals(EDeplacement.BAS, puzzle.inverseDeplacement(dpH));
		assertEquals(EDeplacement.GAUCHE,puzzle.inverseDeplacement(dpD));
		assertEquals(EDeplacement.DROITE, puzzle.inverseDeplacement(dpG));
	}
	
	@Test
	public void testUndo() throws CloneNotSupportedException {

		
		Case[][] grilleTest = new Case[TAILLE][TAILLE];
		int compteur = 0;
		for (int i = 0; i < TAILLE; i++) {
			for (int j = 0; j < TAILLE; j++) {
				if (!(i == TAILLE - 1 && j == TAILLE - 1)) {
					grilleTest[j][i] = new Case(compteur);
				}
				compteur++;
			}
		}

		grilleTest[TAILLE - 1][TAILLE - 1] = new Case(Case.INDEX_CASE_VIDE);
		Puzzle p = new Puzzle(3);
		p.setGrille(grilleTest);
		Puzzle pClone = (Puzzle)p.clone();
		
		pClone.deplacerCase(EDeplacement.DROITE);
		pClone.undo();
		
		assertEquals(p, pClone);
	}
	
}
