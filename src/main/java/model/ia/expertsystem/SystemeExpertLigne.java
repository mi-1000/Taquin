package main.java.model.ia.expertsystem;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import main.java.model.Case;
import main.java.model.EDeplacement;
import main.java.model.Puzzle;
import main.java.model.ia.IA;
import main.java.model.ia.Noeud;
import main.java.model.ia.astar.IAAStar;

public class SystemeExpertLigne {

	private static Puzzle puzzleTaquin;
	private static List<EDeplacement> solution;

	/**
	 * Deplace la case dans le puzzle et l'ajoute à la liste des solutions
	 * 
	 * @param dp enum EDeplacement
	 */
	private static void deplacerCase(EDeplacement dp) {
		Point oldCoord = new Point(puzzleTaquin.getXCaseVide(), puzzleTaquin.getYCaseVide());
		puzzleTaquin.deplacerCase(dp);
		solution.add(dp);
		if (oldCoord.equals(new Point(puzzleTaquin.getXCaseVide(), puzzleTaquin.getYCaseVide()))) {
			solution.remove(solution.size() - 1);
		}
	}

	/**
	 * Méthode de resolution de la première ligne d'un puzzle pour tout n>3
	 * 
	 * @param puzzle, Puzzle à résoudre
	 * @return List<EDeplacement>, solution pour résoudre la ligne
	 */
	public static List<EDeplacement> solveLigne(Puzzle puzzle) {
		solution = new ArrayList<>();
		puzzleTaquin = puzzle;
		if (puzzle.getTaille() > 3) {
			// [col][ligne]
			// on s'occupe déjà de la première ligne jusqu'à l'avant dernière case de la
			// ligne
			Noeud noeud = new Noeud(puzzle);

			for (int index = 0; index < puzzle.getTaille() - 1; index++) {
				while (IA.manhattanDistance(index, puzzle) != 0) {
					deplacer(puzzle, index);
				}
			}

			// on s'occupe de la dernière case de la première ligne

			// on inverse l'index de premiere ligne, derniere col AVEC troisième ligne
			// derniere col pour dire à l'algorithme de placer le premier à l'emplacement du
			// deuxième
			if (!(IA.chercherCoordonneesIndex(puzzle.getTaille() - 1, puzzle).x == puzzle.getTaille() - 1
					&& IA.chercherCoordonneesIndex(puzzle.getTaille() - 1, puzzle).y == 0)) {
				debloquerCaseVideDuCoin(puzzle, Case.INDEX_CASE_VIDE);
				if (!(IA.chercherCoordonneesIndex(puzzle.getTaille() - 1, puzzle).x == puzzle.getTaille() - 1
						&& IA.chercherCoordonneesIndex(puzzle.getTaille() - 1, puzzle).y == 0)) {
					Point point = IA.chercherCoordonneesIndex(puzzle.getTaille() - 1, puzzle);
					Point point2 = IA.chercherCoordonneesIndex(puzzle.getTaille() * 3 - 1, puzzle);
					puzzle.getGrille()[point.x][point.y] = new Case(puzzle.getTaille() * 3 - 1);
					puzzle.getGrille()[point2.x][point2.y] = new Case(puzzle.getTaille() - 1);

					while (IA.manhattanDistance(puzzle.getTaille() * 3 - 1, puzzle) != 0) {
						int man = IA.manhattanDistance(puzzle.getTaille() * 3 - 1, puzzle);

						deplacer(puzzle, puzzle.getTaille() * 3 - 1);
					}

					deplacerCaseVidePourDerniereCaseLigne(puzzle);
					deplacerDerniereCaseVersPremiereLigne(puzzle);
					Point point3 = IA.chercherCoordonneesIndex(puzzle.getTaille() - 1, puzzle);
					Point point4 = IA.chercherCoordonneesIndex(puzzle.getTaille() * 3 - 1, puzzle);
					puzzle.getGrille()[point3.x][point3.y] = new Case(puzzle.getTaille() * 3 - 1);
					puzzle.getGrille()[point4.x][point4.y] = new Case(puzzle.getTaille() - 1);
				}
			}

		}
		return solution;
	}

	/**
	 * 
	 * @param puzzle
	 */
	private static void deplacerCaseVidePourDerniereCaseLigne(Puzzle puzzle) {
		int xCaseVide = IA.chercherCoordonneesIndex(Case.INDEX_CASE_VIDE, puzzle).x;
		int yCaseVide = IA.chercherCoordonneesIndex(Case.INDEX_CASE_VIDE, puzzle).y;

		if (xCaseVide == puzzle.getTaille() - 1 && yCaseVide == 1) {
			deplacerCase(EDeplacement.DROITE);

		} else {
			if (xCaseVide == puzzle.getTaille() - 1 && yCaseVide == 3) {
				deplacerCase(EDeplacement.DROITE);

				deplacerCase(EDeplacement.BAS);

				deplacerCase(EDeplacement.BAS);

			} else {
				if (xCaseVide == puzzle.getTaille() - 2 && yCaseVide == 2) {
					deplacerCase(EDeplacement.BAS);

				}
			}

		}
	}

	/**
	 * 
	 * @param puzzle
	 */
	private static void deplacerDerniereCaseVersPremiereLigne(Puzzle puzzle) {
		deplacerCase(EDeplacement.GAUCHE);

		deplacerCase(EDeplacement.BAS);

		deplacerCase(EDeplacement.DROITE);

		deplacerCase(EDeplacement.HAUT);

		deplacerCase(EDeplacement.GAUCHE);

		deplacerCase(EDeplacement.HAUT);

		deplacerCase(EDeplacement.DROITE);

		deplacerCase(EDeplacement.BAS);

		deplacerCase(EDeplacement.BAS);

		deplacerCase(EDeplacement.GAUCHE);

		deplacerCase(EDeplacement.HAUT);

	}

	/**
	 * 
	 * @param puzzle
	 * @param index
	 */
	private static void debloquerCaseVideDuCoin(Puzzle puzzle, int index) {
		if ((index == Case.INDEX_CASE_VIDE)
				&& IA.chercherCoordonneesIndex(Case.INDEX_CASE_VIDE, puzzle).x == puzzle.getTaille() - 1
				&& IA.chercherCoordonneesIndex(Case.INDEX_CASE_VIDE, puzzle).y == 0) {
			deplacerCase(EDeplacement.HAUT);
		}
	}

	/**
	 * 
	 * @param puzzle
	 * @param index
	 */
	private static void deplacer(Puzzle puzzle, int index) {
		boolean res;

		do {
			Point coordonneesIndex = IA.chercherCoordonneesIndex(index, puzzle);
			// si la case vide est sur la même colonne et en dessous et dernière colonne
			if (IA.getDistanceY(index, puzzle) > 0 && puzzle.getYCaseVide() > coordonneesIndex.y
					&& puzzle.getXCaseVide() == coordonneesIndex.x && puzzle.getXCaseVide() == puzzle.getTaille() - 1) {
				deplacerCase(EDeplacement.DROITE);

				deplacerCase(EDeplacement.BAS);

				deplacerCase(EDeplacement.BAS);

				deplacerCase(EDeplacement.DROITE);

				deplacerCase(EDeplacement.BAS);

			}
			deplacerEnXLigne(puzzle, index, coordonneesIndex);
			deplacerEnYLigne(puzzle, index, coordonneesIndex);
			res = true;
			if (IA.getDistanceX(index, puzzle) > 0) {
				res = puzzle.getXCaseVide() - coordonneesIndex.x < 0 && puzzle.getYCaseVide() - coordonneesIndex.y == 0;
			} else {
				if (IA.getDistanceX(index, puzzle) < 0) {
					res = puzzle.getXCaseVide() - coordonneesIndex.x > 0
							&& puzzle.getYCaseVide() - coordonneesIndex.y == 0;
				} else {
					res = puzzle.getXCaseVide() - coordonneesIndex.x == 0
							&& puzzle.getYCaseVide() - coordonneesIndex.y == -1;
				}
			}
		} while (!res);
		deplacerCase(deplacerIndexVersCaseVide(puzzle, index, IA.chercherCoordonneesIndex(index, puzzle)));

	}

	/**
	 * 
	 * @param puzzle
	 * @param index
	 * @param coordonneesIndex
	 * @return
	 */
	private static EDeplacement deplacerIndexVersCaseVide(Puzzle puzzle, int index, Point coordonneesIndex) {
		if (puzzle.getXCaseVide() - coordonneesIndex.x < 0 && puzzle.getYCaseVide() - coordonneesIndex.y == 0) {
			return EDeplacement.GAUCHE;
		}
		if (puzzle.getXCaseVide() - coordonneesIndex.x > 0 && puzzle.getYCaseVide() - coordonneesIndex.y == 0) {
			return EDeplacement.DROITE;
		}
		if (puzzle.getXCaseVide() - coordonneesIndex.x == 0 && puzzle.getYCaseVide() - coordonneesIndex.y == -1) {
			return EDeplacement.HAUT;
		}
		return null;
	}

	/**
	 * 
	 * @param puzzle
	 * @param index
	 * @param coordonneesIndex
	 */
	private static void deplacerEnXLigne(Puzzle puzzle, int index, Point coordonneesIndex) {

		// cas où la case vide se trouve en dessous
		if (puzzle.getYCaseVide() == coordonneesIndex.y + 1 && puzzle.getXCaseVide() == coordonneesIndex.x) {
			deplacerCase(EDeplacement.GAUCHE);

			deplacerCase(EDeplacement.BAS);

		}

		// cas où la case à déplacer doit se retrouver vers la gauche
		if (IA.getDistanceX(index, puzzle) > 0) {
			// si la case vide ne se trouve pas à gauche de la case à déplacer
			if (!(puzzle.getXCaseVide() == coordonneesIndex.x - 1 && puzzle.getYCaseVide() == coordonneesIndex.y)) {

				// on déplace la case vide à gauche de la case à déplacer

				// si la case vide est à droite et pas sur la même ligne
				if (puzzle.getYCaseVide() != coordonneesIndex.y && puzzle.getXCaseVide() >= coordonneesIndex.x) {
					deplacerCase(EDeplacement.DROITE);

				} else {
					// si la case vide est est à gauche et pas sur la même ligne
					if (puzzle.getYCaseVide() != coordonneesIndex.y && puzzle.getXCaseVide() < coordonneesIndex.x - 1) {
						deplacerCase(EDeplacement.GAUCHE);

					} else {
						// si la case vide est sur la même ligne et à gauche
						if (puzzle.getYCaseVide() == coordonneesIndex.y
								&& puzzle.getXCaseVide() < coordonneesIndex.x - 1) {
							deplacerCase(EDeplacement.GAUCHE);

						} else {
							// si la case vide est sur la même ligne et à droite
							if (puzzle.getYCaseVide() == coordonneesIndex.y
									&& puzzle.getXCaseVide() > coordonneesIndex.x + 1) {
								deplacerCase(EDeplacement.DROITE);

							}
						}
					}
				}

			}

		} else {
			// cas où la case à déplacer doit se retrouver vers la droite
			if (IA.getDistanceX(index, puzzle) < 0) {

				if (!(puzzle.getXCaseVide() == coordonneesIndex.x + 1 && puzzle.getYCaseVide() == coordonneesIndex.y)) {

					// on déplace la case vide à gauche de la case à déplacer

					// si la case vide est à droite et pas sur la même ligne
					if (puzzle.getYCaseVide() != coordonneesIndex.y && puzzle.getXCaseVide() > coordonneesIndex.x + 1
							&& !(puzzle.getYCaseVide() == 0 && (index - 1) == puzzle.getXCaseVide() - 1)) {
						deplacerCase(EDeplacement.DROITE);

					} else {
						// si la case vide est à gauche et pas sur la même ligne
						if (puzzle.getYCaseVide() != coordonneesIndex.y
								&& puzzle.getXCaseVide() <= coordonneesIndex.x) {
							deplacerCase(EDeplacement.GAUCHE);

						} else {
							// si la case vide est sur la même ligne et à gauche
							if (puzzle.getYCaseVide() == coordonneesIndex.y
									&& puzzle.getXCaseVide() < coordonneesIndex.x - 1) {
								deplacerCase(EDeplacement.GAUCHE);

							} else {
								// si la case vide est sur la même ligne et à droite
								if (puzzle.getYCaseVide() == coordonneesIndex.y
										&& puzzle.getXCaseVide() > coordonneesIndex.x + 1
										&& !(puzzle.getYCaseVide() == 0 && (index) == puzzle.getXCaseVide() - 1)) {
									deplacerCase(EDeplacement.DROITE);

								}
							}
						}
					}
				}
			} else {
				if (!(puzzle.getXCaseVide() == coordonneesIndex.x + 1 && puzzle.getYCaseVide() == coordonneesIndex.y)) {

					// on déplace la case vide à la même colonne que de la case à déplacer

					// si la case vide est à droite et pas sur la même ligne
					if (puzzle.getYCaseVide() != coordonneesIndex.y && puzzle.getXCaseVide() >= coordonneesIndex.x
							&& !(puzzle.getYCaseVide() == 0 && (index - 1) == puzzle.getXCaseVide() - 1)) {
						deplacerCase(EDeplacement.DROITE);

					} else {
						// si la case vide est est à gauche et pas sur la même ligne
						if (puzzle.getYCaseVide() != coordonneesIndex.y && puzzle.getXCaseVide() < coordonneesIndex.x) {
							deplacerCase(EDeplacement.GAUCHE);

						} else {
							// si la case vide est sur la même ligne et à gauche
							if (puzzle.getYCaseVide() == coordonneesIndex.y
									&& puzzle.getXCaseVide() < coordonneesIndex.x - 1) {
								deplacerCase(EDeplacement.GAUCHE);

							} else {
								// si la case vide est sur la même ligne et à droite
								if (puzzle.getYCaseVide() == coordonneesIndex.y
										&& puzzle.getXCaseVide() > coordonneesIndex.x + 1) {
									deplacerCase(EDeplacement.DROITE);

								}
							}
						}
					}

				}
			}
		}
	}

	/**
	 * 
	 * @param puzzle
	 * @param index
	 * @param coordonneesIndex
	 */
	private static void deplacerEnYLigne(Puzzle puzzle, int index, Point coordonneesIndex) {

		// si la case à déplacer n'est pas sur la même ligne que là où elle doit être
		if (IA.getDistanceY(index, puzzle) != 0) {

			// on déplace la case vide à gauche ou à droite de la case à déplacer

			// si la case vide est au dessus et pas sur la même colonne
			if (puzzle.getXCaseVide() != coordonneesIndex.x && puzzle.getYCaseVide() < coordonneesIndex.y) {
				deplacerCase(EDeplacement.HAUT);

			} else {
				// si la case vide est est en dessous et pas sur la même colonne
				if (puzzle.getXCaseVide() != coordonneesIndex.x && puzzle.getYCaseVide() > coordonneesIndex.y
						&& !(puzzle.getXCaseVide() < index && puzzle.getYCaseVide() == 1)) {
					deplacerCase(EDeplacement.BAS);

				} else {
					// si la case vide est sur la même ligne et à droite de la case à déplacer
					if (puzzle.getYCaseVide() == coordonneesIndex.y && puzzle.getXCaseVide() > coordonneesIndex.x
							&& !(puzzle.getXCaseVide() < index && puzzle.getYCaseVide() == 1)) {
						deplacerCase(EDeplacement.BAS);

					} else {
						// si la case vide est sur la même ligne et à gauche de la case à déplacer
						if (puzzle.getYCaseVide() == coordonneesIndex.y && puzzle.getXCaseVide() < coordonneesIndex.x) {
							if (puzzle.getYCaseVide() == puzzle.getTaille() - 1) {
								deplacerCase(EDeplacement.BAS);

								deplacerCase(EDeplacement.GAUCHE);
							} else {
								deplacerCase(EDeplacement.HAUT);
							}

						} else {
							// si la case vide est sur la même colonne et au dessus de la case vide
							if (puzzle.getXCaseVide() == coordonneesIndex.x
									&& puzzle.getYCaseVide() < coordonneesIndex.y - 1) {
								deplacerCase(EDeplacement.HAUT);

							} else {
								// si la case vide est sur la même colonne et en dessous de la case vide
								if (puzzle.getXCaseVide() == coordonneesIndex.x
										&& puzzle.getYCaseVide() > coordonneesIndex.y + 1
										&& !(puzzle.getXCaseVide() < index && puzzle.getYCaseVide() == 1)) {
									deplacerCase(EDeplacement.BAS);

								}
							}
						}
					}
				}
			}
		} else {
			// si la case à déplacer est sur la même colonne que là où elle doit être

			// si la case vide est au dessus et pas sur la même colonne
			if (puzzle.getXCaseVide() != coordonneesIndex.x && puzzle.getYCaseVide() < coordonneesIndex.y) {
				deplacerCase(EDeplacement.HAUT);

			} else {
				// si la case vide est est en dessous et pas sur la même colonne
				if (puzzle.getXCaseVide() != coordonneesIndex.x && puzzle.getYCaseVide() > coordonneesIndex.y
						&& !(puzzle.getXCaseVide() < index && puzzle.getYCaseVide() == 1)) {
					deplacerCase(EDeplacement.BAS);

				} else {
					// si la case vide est sur la même ligne et à droite de la case à déplacer
					if (puzzle.getYCaseVide() == coordonneesIndex.y && puzzle.getXCaseVide() > coordonneesIndex.x) {
						if (puzzle.getYCaseVide() == 0) {
							deplacerCase(EDeplacement.HAUT);

							deplacerCase(EDeplacement.DROITE);

							deplacerCase(EDeplacement.DROITE);

							deplacerCase(EDeplacement.BAS);
						} else {
							deplacerCase(EDeplacement.BAS);
						}

					} else {
						// si la case vide est sur la même ligne et à gauche de la case à déplacer
						if (puzzle.getYCaseVide() == coordonneesIndex.y && puzzle.getXCaseVide() < coordonneesIndex.x) {
							deplacerCase(EDeplacement.HAUT);

						} else {
							// si la case vide est sur la même colonne et au dessus
							if (puzzle.getYCaseVide() < coordonneesIndex.y - 1
									&& puzzle.getXCaseVide() == coordonneesIndex.x) {
								deplacerCase(EDeplacement.HAUT);

							} else {
								// si la case vide est sur la même colonne et en dessous

								if (puzzle.getYCaseVide() > coordonneesIndex.y + 1
										&& puzzle.getXCaseVide() == coordonneesIndex.x
										&& !(puzzle.getXCaseVide() < index && puzzle.getYCaseVide() == 1)) {
									deplacerCase(EDeplacement.BAS);

								}
							}
						}
					}
				}
			}
		}
	}

}
