package main.java.model.ia;

import java.awt.Point;
import java.util.Queue;

import main.java.model.Case;
import main.java.model.Puzzle;

public class IA {

	/**
	 * Recupere le noeud minimum pour A* dans une file de Noeud
	 * 
	 * @param ouverts, file de Noeud
	 * @return Noeud ayant le f minimal, f(n) = g(n) + h(n)
	 */
	public static Noeud getMinimum(Queue<Noeud> ouverts) {
		Noeud minimumNode = null;
		int min = 9999;

		for (Noeud n : ouverts) {
			int cost = n.getG() + n.calculerH();
			if (cost < min) {
				min = cost;
				minimumNode = n;
			}
		}
		return minimumNode;
	}

	/**
	 * Cherche dans la file le noeud qui a résolu le puzzle
	 * 
	 * @param ouverts, File de Noeud
	 * @return Noeud résolu
	 */
	public static Noeud chercherNoeudResolu(Queue<Noeud> ouverts) {
		Noeud noeudResolu = null;
		for (Noeud noeud : ouverts) {
			if (noeud.getPuzzle().verifierGrille())
				noeudResolu = noeud;
		}
		return noeudResolu;
	}

	/**
	 * Calcul la distance de manhattan entre l'index d'un puzzle et la position
	 * finale à laquelle il devrait être
	 * 
	 * @param index,  int sur lequel on calcule
	 * @param puzzle, Puzzle dans lequel on calcule
	 * @return int, distance de manhattan
	 */
	public static int manhattanDistance(int index, Puzzle puzzle) {
		if (index == Case.INDEX_CASE_VIDE) {
			return Math.abs(puzzle.getXCaseVide() - (puzzle.getTaille() - 1))
					+ Math.abs(puzzle.getYCaseVide() - (puzzle.getTaille() - 1));
		}
		int distance = getDistanceXAbs(index, puzzle) + getDistanceYAbs(index, puzzle);
		return distance;
	}

	/**
	 * Cherche les coordonnées de l'index dans le puzzle, puis le retourne dans un
	 * Point
	 * 
	 * @param index,  int de l'index à chercher
	 * @param puzzle, Puzzle dans lequel chercher
	 * @return Point contenant les coordonnées à retourner
	 */
	public static Point chercherCoordonneesIndex(int index, Puzzle puzzle) {
		Case[][] grille = puzzle.getGrille();
		for (int i = 0; i < grille.length; i++) {
			for (int j = 0; j < grille.length; j++) {
				if (grille[j][i].getIndex() == index)
					return new Point(j, i);
			}
		}
		return null;
	}

	/**
	 * Calcule la distance entre l'index et la position finale en y à laquelle il
	 * devrait être pour que le puzzle devienne résolu
	 * 
	 * @param index,  int de l'index à chercher
	 * @param puzzle, Puzzle dans lequel calculer
	 * @return int, la distance de l'index en y - position finale de l'index en y
	 */
	public static int getDistanceY(int index, Puzzle puzzle) {
		Case[][] grille = puzzle.getGrille();
		int compteur = 0;
		int distance = 0;
		int xBut, yBut;
		xBut = 0;
		yBut = 0;
		for (int i = 0; i < puzzle.getTaille(); i++) {
			for (int j = 0; j < puzzle.getTaille(); j++) {
				if (compteur == index) {
					xBut = j;
					yBut = i;
				}
				compteur++;
			}
		}

		Point point = chercherCoordonneesIndex(index, puzzle);
		distance = point.y - yBut;
		return distance;
	}

	/**
	 * Calcule la distance entre l'index et la position finale en x à laquelle il
	 * devrait être pour que le puzzle devienne résolu
	 * 
	 * @param index,  int de l'index à chercher
	 * @param puzzle, Puzzle dans lequel calculer
	 * @return int, la distance de l'index en x - position finale de l'index en x
	 */
	public static int getDistanceX(int index, Puzzle puzzle) {
		Case[][] grille = puzzle.getGrille();
		int compteur = 0;
		int distance = 0;
		int xBut, yBut;
		xBut = 0;
		yBut = 0;
		for (int i = 0; i < puzzle.getTaille(); i++) {
			for (int j = 0; j < puzzle.getTaille(); j++) {
				if (compteur == index) {
					xBut = j;
					yBut = i;
				}
				compteur++;
			}
		}

		Point point = chercherCoordonneesIndex(index, puzzle);
		distance = point.x - xBut;
		return distance;
	}

	/**
	 * Calcule la distance en valeur absolue entre l'index et la position finale en
	 * y à laquelle il devrait être pour que le puzzle devienne résolu
	 * 
	 * @param index,  int de l'index à chercher
	 * @param puzzle, Puzzle dans lequel calculer
	 * @return int, la distance de l'index en valeur absolue en y - position finale
	 *         de l'index en y
	 */
	private static int getDistanceYAbs(int index, Puzzle puzzle) {
		return Math.abs(getDistanceY(index, puzzle));
	}

	/**
	 * Calcule la distance en valeur absolue entre l'index et la position finale en
	 * x à laquelle il devrait être pour que le puzzle devienne résolu
	 * 
	 * @param index,  int de l'index à chercher
	 * @param puzzle, Puzzle dans lequel calculer
	 * @return int, la distance en valeur absolue de l'index en x - position finale
	 *         de l'index en x
	 */
	private static int getDistanceXAbs(int index, Puzzle puzzle) {
		return Math.abs(getDistanceX(index, puzzle));
	}

}
