package main.java.model.ia.expertsystem;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import main.java.model.Case;
import main.java.model.EDeplacement;
import main.java.model.Puzzle;
import main.java.model.ia.IA;

public class SystemeExpertColonne {

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
	 * Méthode de resolution de la première colonne d'un puzzle pour tout n>3
	 * 
	 * @param puzzle, Puzzle à résoudre
	 * @return List<EDeplacement>, solution pour résoudre la colonne
	 */
	public static List<EDeplacement> solveColonne(Puzzle puzzle) {
		puzzleTaquin = puzzle;
		solution = new ArrayList<>();

		if (puzzle.getTaille() > 3) {

			// on déplace tous les index allant de la case deuxième ligne première colonne
			// jusqu'à l'avant dernière ligne
			for (int index = 1; index < puzzle.getTaille() - 1; index++) {
				while (IA.manhattanDistance(index * puzzle.getTaille(), puzzle) != 0) {
					deplacer(puzzle, index * puzzle.getTaille());
				}
			}

			// on s'occupe de la dernière case de la colonne de la dernière ligne
			if (puzzle.getXCaseVide() == 0 && puzzle.getYCaseVide() == puzzle.getTaille() - 1) {
				deplacerCase(EDeplacement.GAUCHE);

			}

			// on vérifie qu'elle n'est pas déjà bien placé
			if (!(IA.chercherCoordonneesIndex(puzzle.getTaille() * (puzzle.getTaille() - 1), puzzle).y == puzzle
					.getTaille() - 1
					&& (IA.chercherCoordonneesIndex(puzzle.getTaille() * (puzzle.getTaille() - 1), puzzle).x == 0))) {

				// on la remplace par l'index + 2 pour pouvoir mettre en place l'algorithme
				// d'échange
				int indexSwap = (puzzle.getTaille() * (puzzle.getTaille() - 1)) + 2;
				if (!(IA.chercherCoordonneesIndex(puzzle.getTaille() * (puzzle.getTaille() - 1), puzzle).y == puzzle
						.getTaille() - 1
						&& (IA.chercherCoordonneesIndex(puzzle.getTaille() * (puzzle.getTaille() - 1),
								puzzle).x == 0))) {

					Point point = IA.chercherCoordonneesIndex(puzzle.getTaille() * (puzzle.getTaille() - 1), puzzle);
					Point point2 = IA.chercherCoordonneesIndex(indexSwap, puzzle);

					puzzle.getGrille()[point.x][point.y] = new Case(indexSwap);
					puzzle.getGrille()[point2.x][point2.y] = new Case(puzzle.getTaille() * (puzzle.getTaille() - 1));

					while (IA.manhattanDistance(indexSwap, puzzle) != 0) {
						deplacer(puzzle, indexSwap);
					}

					Point point3 = IA.chercherCoordonneesIndex(puzzle.getTaille() * (puzzle.getTaille() - 1), puzzle);
					Point point4 = IA.chercherCoordonneesIndex(indexSwap, puzzle);
					puzzle.getGrille()[point3.x][point3.y] = new Case(indexSwap);
					puzzle.getGrille()[point4.x][point4.y] = new Case(puzzle.getTaille() * (puzzle.getTaille() - 1));

					deplacerCaseVidePourDerniereCaseColonne(puzzle); // on déplace la case vide à gauche de la case à
																		// déplacer
					deplacerDerniereCaseVersPremiereColonne(puzzle); // on déplace la case à déplacer dans le coin
				}
			}

			reduireDimensionTaquin(puzzle); // réduction de dimension du taquin pour attaquer la suite

		}
		return solution;
	}

	/**
	 * Reduction de la dimension du puzzle, on réduit en enlevant la première ligne
	 * et la première colonne
	 * 
	 * @param puzzle, Puzzle à réduire
	 */
	private static void reduireDimensionTaquin(Puzzle puzzle) {
		Case[][] grille = new Case[puzzle.getTaille() - 1][puzzle.getTaille() - 1];

		// Pour tous les index de à taille de la grille au carré
		for (int k = 0; k < grille.length * grille.length - 1; k++) {
			int minCourant = 99999999;
			int iCourant = 0;
			int jCourant = 0;
			// on cherche le minimum puis on l'attribue à sa nouvelle position dans la
			// grille diminuée
			for (int i = 1; i < puzzle.getTaille(); i++) {
				for (int j = 1; j < puzzle.getTaille(); j++) {
					if (puzzle.getGrille()[i][j].getIndex() < minCourant
							&& puzzle.getGrille()[i][j].getIndex() != Case.INDEX_CASE_VIDE) {
						minCourant = puzzle.getGrille()[i][j].getIndex();
						iCourant = i - 1;
						jCourant = j - 1;
					}
				}
			}
			puzzle.getGrille()[iCourant + 1][jCourant + 1] = new Case(9999999);
			grille[iCourant][jCourant] = new Case(k);
		}
		grille[puzzle.getXCaseVide() - 1][puzzle.getYCaseVide() - 1] = new Case(Case.INDEX_CASE_VIDE);
		puzzle.setGrille(grille);
	}

	/**
	 * Deplace la case vide à gauche de la case à déplacer
	 * 
	 * @param puzzle
	 */
	private static void deplacerCaseVidePourDerniereCaseColonne(Puzzle puzzle) {
		int indexSwap = (puzzle.getTaille() * (puzzle.getTaille() - 1));
		int xCase = IA.chercherCoordonneesIndex(indexSwap, puzzle).x;
		int yCase = IA.chercherCoordonneesIndex(indexSwap, puzzle).y;

		// si la case est même colonne au dessus
		if (xCase == puzzle.getXCaseVide() && puzzle.getYCaseVide() == yCase - 1) {
			deplacerCase(EDeplacement.DROITE);

			deplacerCase(EDeplacement.HAUT);

		}

		// si la case est à droite et même ligne
		if (puzzle.getXCaseVide() == xCase + 1 && puzzle.getYCaseVide() == yCase) {
			deplacerCase(EDeplacement.BAS);
			deplacerCase(EDeplacement.DROITE);
			deplacerCase(EDeplacement.DROITE);
			deplacerCase(EDeplacement.HAUT);
		}

		// si la case est au dessus et à droite
		if (puzzle.getXCaseVide() == xCase - 1 && puzzle.getYCaseVide() == yCase - 1) {
			deplacerCase(EDeplacement.HAUT);
		}

	}

	/**
	 * Utilise un pattern de déplacement pour mettre la case dans le coin
	 * 
	 * @param puzzle
	 */
	private static void deplacerDerniereCaseVersPremiereColonne(Puzzle puzzle) {
		deplacerCase(EDeplacement.DROITE);
		deplacerCase(EDeplacement.BAS);
		deplacerCase(EDeplacement.GAUCHE);
		deplacerCase(EDeplacement.HAUT);
		deplacerCase(EDeplacement.GAUCHE);
		deplacerCase(EDeplacement.BAS);
		deplacerCase(EDeplacement.DROITE);
		deplacerCase(EDeplacement.DROITE);
		deplacerCase(EDeplacement.HAUT);
		deplacerCase(EDeplacement.GAUCHE);
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
			// cas pour la dernière case
			if (index == (puzzle.getTaille() * (puzzle.getTaille() - 1) + 2)) {
				if (puzzle.getXCaseVide() == coordonneesIndex.x && puzzle.getYCaseVide() > coordonneesIndex.y) {
					while (puzzle.getYCaseVide() > coordonneesIndex.y) {
						deplacerCase(EDeplacement.BAS);
					}
				}
				if (puzzle.getXCaseVide() == coordonneesIndex.x - 1
						&& puzzle.getYCaseVide() == coordonneesIndex.y - 1) {
					deplacerCase(EDeplacement.GAUCHE);
				}
				if (IA.getDistanceX(index, puzzle) > 0 && puzzle.getXCaseVide() == coordonneesIndex.x) {
					deplacerCase(EDeplacement.DROITE);
					deplacerCase(EDeplacement.HAUT);
				} else {
					if (IA.getDistanceX(index, puzzle) > 0 && puzzle.getXCaseVide() < coordonneesIndex.x) {
						deplacerCase(EDeplacement.GAUCHE);
						deplacerCase(EDeplacement.HAUT);
						deplacerCase(EDeplacement.HAUT);
						deplacerCase(EDeplacement.DROITE);
					}
				}

			}
			deplacerEnXColonne(puzzle, index, coordonneesIndex);
			deplacerEnYColonne(puzzle, index, coordonneesIndex);
			res = true;
			if (IA.getDistanceY(index, puzzle) > 0) {
				res = (puzzle.getXCaseVide() == coordonneesIndex.x && puzzle.getYCaseVide() == coordonneesIndex.y - 1)
						|| (puzzle.getXCaseVide() - coordonneesIndex.x == -1
								&& puzzle.getYCaseVide() == coordonneesIndex.y);
			} else {
				if (IA.getDistanceY(index, puzzle) < 0) {
					res = (puzzle.getXCaseVide() - coordonneesIndex.x == 0
							&& puzzle.getYCaseVide() - coordonneesIndex.y == 1)
							|| (puzzle.getXCaseVide() - coordonneesIndex.x == -1
									&& puzzle.getYCaseVide() == coordonneesIndex.y);
				} else {
					res = (puzzle.getXCaseVide() - coordonneesIndex.x == -1
							&& puzzle.getYCaseVide() == coordonneesIndex.y);
				}
				if (IA.manhattanDistance(index, puzzle) == 0)
					res = true;

			}
		} while (!res);
		if (!(deplacerIndexVersCaseVide(puzzle, index, IA.chercherCoordonneesIndex(index, puzzle)) == null
				|| IA.manhattanDistance(index, puzzle) == 0)) {
			deplacerCase(deplacerIndexVersCaseVide(puzzle, index, IA.chercherCoordonneesIndex(index, puzzle)));

		}
	}

	/**
	 * 
	 * @param puzzle
	 * @param index
	 * @param coordonneesIndex
	 * @return
	 */
	private static EDeplacement deplacerIndexVersCaseVide(Puzzle puzzle, int index, Point coordonneesIndex) {
		if (puzzle.getXCaseVide() - coordonneesIndex.x == 0 && puzzle.getYCaseVide() - coordonneesIndex.y == 1
				&& puzzle.getYCaseVide() != 0) {
			return EDeplacement.BAS;
		}
		if (puzzle.getXCaseVide() - coordonneesIndex.x < 0 && puzzle.getYCaseVide() - coordonneesIndex.y == 0) {
			return EDeplacement.GAUCHE;
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
	private static void deplacerEnXColonne(Puzzle puzzle, int index, Point coordonneesIndex) {

		// cas où la case est sur la même colonne qu'elle doit être
		if (IA.getDistanceX(index, puzzle) == 0) {

			// cas où la case vide est à droite au dessus
			if (puzzle.getXCaseVide() > coordonneesIndex.x && puzzle.getYCaseVide() < coordonneesIndex.y
					&& !(puzzle.getXCaseVide() == 1 && puzzle.getYCaseVide() < (index / puzzle.getTaille()))) {
				deplacerCase(EDeplacement.DROITE);

			} else {
				// cas où la case vide est à droite en dessous
				if (puzzle.getXCaseVide() > coordonneesIndex.x && puzzle.getYCaseVide() > coordonneesIndex.y
						&& !(puzzle.getXCaseVide() == 1 && puzzle.getYCaseVide() < (index / puzzle.getTaille()))) {
					deplacerCase(EDeplacement.DROITE);

				} else {
					// cas où la case vide est à droite
					if (puzzle.getXCaseVide() == coordonneesIndex.x + 1 && puzzle.getYCaseVide() != 1
							&& !(puzzle.getXCaseVide() == 1 && puzzle.getYCaseVide() < (index / puzzle.getTaille()))) {
						deplacerCase(EDeplacement.BAS);

						deplacerCase(EDeplacement.DROITE);

					} else {
						// cas où la case vide est en dessous
						if (puzzle.getXCaseVide() == coordonneesIndex.x
								&& puzzle.getYCaseVide() == coordonneesIndex.y + 1) {
							deplacerCase(EDeplacement.GAUCHE);

							deplacerCase(EDeplacement.BAS);

							deplacerCase(EDeplacement.BAS);

							deplacerCase(EDeplacement.DROITE);

						} else {
							// cas où la case vide est au dessus ou en dessous à gauche
							if (puzzle.getXCaseVide() < coordonneesIndex.x - 1
									&& puzzle.getYCaseVide() != coordonneesIndex.y) {
								deplacerCase(EDeplacement.GAUCHE);

							} else {
								// cas où c'est l'index pour mettre la dernière case de la colonne
								if (index == (puzzle.getTaille() * (puzzle.getTaille() - 1)) + 2) {

									if (puzzle.getXCaseVide() == coordonneesIndex.x
											&& puzzle.getYCaseVide() == coordonneesIndex.y - 1) {
										deplacerCase(EDeplacement.GAUCHE);

										deplacerCase(EDeplacement.HAUT);

										deplacerCase(EDeplacement.HAUT);

										deplacerCase(EDeplacement.DROITE);

									}
								}

							}
						}
					}
				}
			}
		} else {
			// cas où la case à déplacer n'est pas sur la bonne colonne

			// cas où la case ne doit pas se déplacer à droite
			if (puzzle.getXCaseVide() == 1 && puzzle.getYCaseVide() <= (index / puzzle.getTaille())
					&& puzzle.getYCaseVide() != coordonneesIndex.y - 1) {
				deplacerCase(EDeplacement.HAUT);

				deplacerCase(EDeplacement.GAUCHE);

			} else {
				if (puzzle.getXCaseVide() == 1 && puzzle.getYCaseVide() <= (index / puzzle.getTaille())
						&& puzzle.getYCaseVide() == coordonneesIndex.y - 1) {
					deplacerCase(EDeplacement.GAUCHE);

					deplacerCase(EDeplacement.HAUT);

					deplacerCase(EDeplacement.HAUT);

					deplacerCase(EDeplacement.DROITE);

					if (!(puzzle.getXCaseVide() == 1 && puzzle.getYCaseVide() <= (index / puzzle.getTaille()))) {
						deplacerCase(EDeplacement.DROITE);

						deplacerCase(EDeplacement.BAS);

					} else {
						deplacerCase(EDeplacement.BAS);

					}
				} else {
					// cas où la case vide est au dessus à gauche
					if (puzzle.getXCaseVide() < coordonneesIndex.x && puzzle.getYCaseVide() < coordonneesIndex.y
							&& !(puzzle.getYCaseVide() == (index / puzzle.getTaille()) - 1
									&& puzzle.getXCaseVide() == 0)) {
						deplacerCase(EDeplacement.GAUCHE);

					} else {
						if ((puzzle.getYCaseVide() == (index / puzzle.getTaille()) - 1 && puzzle.getXCaseVide() == 0)) {
							deplacerCase(EDeplacement.HAUT);

						} else {
							// cas où la case vide est même ligne et à gauche
							if (puzzle.getXCaseVide() < coordonneesIndex.x - 1
									&& puzzle.getYCaseVide() == coordonneesIndex.y) {
								deplacerCase(EDeplacement.GAUCHE);

							} else {
								// cas où la case vide est en dessous à gauche
								if (puzzle.getXCaseVide() < coordonneesIndex.x
										&& puzzle.getYCaseVide() > coordonneesIndex.y) {
									deplacerCase(EDeplacement.GAUCHE);

								} else {
									// cas où la case vide est en dessous à droite
									if (puzzle.getXCaseVide() > coordonneesIndex.x
											&& puzzle.getYCaseVide() > coordonneesIndex.y
											&& !(puzzle.getXCaseVide() == 1
													&& puzzle.getYCaseVide() < (index / puzzle.getTaille()))) {
										deplacerCase(EDeplacement.DROITE);

									} else {
										// cas où la case vide est au dessus à droite
										if (puzzle.getXCaseVide() > coordonneesIndex.x
												&& puzzle.getYCaseVide() < coordonneesIndex.y
												&& !(puzzle.getXCaseVide() == 1
														&& puzzle.getYCaseVide() < (index / puzzle.getTaille()))) {
											deplacerCase(EDeplacement.DROITE);

										} else {
											// cas où la case vide est même ligne à droite
											if (puzzle.getXCaseVide() > coordonneesIndex.x
													&& puzzle.getYCaseVide() == coordonneesIndex.y) {
												// cas où on peut contourner par le haut
												if (puzzle.getYCaseVide() > (index / puzzle.getTaille()) + 1) {
													deplacerCase(EDeplacement.BAS);

													deplacerCase(EDeplacement.DROITE);

													deplacerCase(EDeplacement.DROITE);

													deplacerCase(EDeplacement.HAUT);

												} else {
													// cas où on ne peut pas contourner par le haut
													if (puzzle.getYCaseVide() != puzzle.getTaille() - 1) {
														deplacerCase(EDeplacement.HAUT);

														deplacerCase(EDeplacement.DROITE);

													} else {
														deplacerCase(EDeplacement.BAS);

														deplacerCase(EDeplacement.DROITE);

													}
												}
											} else {
												// cas où la case est juste au dessus et même colonne
												if (puzzle.getXCaseVide() == coordonneesIndex.x
														&& puzzle.getYCaseVide() == coordonneesIndex.y - 1) {
													deplacerCase(EDeplacement.DROITE);

													deplacerCase(EDeplacement.HAUT);

												} else {
													// cas où la case est au dessus même colonne
													if (puzzle.getXCaseVide() == coordonneesIndex.x
															&& puzzle.getYCaseVide() < coordonneesIndex.y) {
														// cas où la case n'est pas sur une ligne déjà faite
														if (puzzle.getYCaseVide() >= (index / puzzle.getTaille())) {
															deplacerCase(EDeplacement.DROITE);

															deplacerCase(EDeplacement.HAUT);

														} else {
															deplacerCase(EDeplacement.GAUCHE);

															deplacerCase(EDeplacement.HAUT);

															deplacerCase(EDeplacement.HAUT);

															deplacerCase(EDeplacement.DROITE);

														}
													} else {
														// cas où la case est en dessous même colonne
														if (puzzle.getXCaseVide() == coordonneesIndex.x
																&& puzzle.getYCaseVide() > coordonneesIndex.y
																&& !(puzzle.getXCaseVide() == 1
																		&& puzzle.getYCaseVide() <= (index
																				/ puzzle.getTaille()))) {
															deplacerCase(EDeplacement.DROITE);

															deplacerCase(EDeplacement.BAS);

														} else {
															// cas où la case est au dessus à gauche
															if (puzzle.getXCaseVide() == coordonneesIndex.x - 1
																	&& puzzle.getYCaseVide() == coordonneesIndex.y
																			- 1) {
																deplacerCase(EDeplacement.HAUT);

															}
														}

													}
												}
											}
										}
									}
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
	private static void deplacerEnYColonne(Puzzle puzzle, int index, Point coordonneesIndex) {
		// cas où la case est plus basse que ce qu'elle devrait être
		if (IA.getDistanceY(index, puzzle) > 0) {

			// cas où la case vide est au dessus
			if (puzzle.getYCaseVide() < coordonneesIndex.y - 1) {
				deplacerCase(EDeplacement.HAUT);

			} else {
				// cas où la case vide est en dessous
				if (puzzle.getYCaseVide() > coordonneesIndex.y
						&& puzzle.getYCaseVide() < (index / puzzle.getTaille()) + 1) {
					deplacerCase(EDeplacement.BAS);

				} else {
					// cas où la case vide est même ligne à droite
					if (puzzle.getYCaseVide() == coordonneesIndex.y
							&& puzzle.getXCaseVide() == coordonneesIndex.x + 1) {

						// cas où la case vide est à la deuxième ligne
						if (puzzle.getYCaseVide() == 1 && puzzle.getYCaseVide() >= (index / puzzle.getTaille())) {
							deplacerCase(EDeplacement.HAUT);

							deplacerCase(EDeplacement.DROITE);

							deplacerCase(EDeplacement.DROITE);

							deplacerCase(EDeplacement.BAS);

						} else {
							deplacerCase(EDeplacement.HAUT);

							deplacerCase(EDeplacement.DROITE);

						}
					} else {

						// cas où la case vide est au dessus à droite
						if (puzzle.getYCaseVide() < coordonneesIndex.y && puzzle.getXCaseVide() > coordonneesIndex.x
								&& !(puzzle.getXCaseVide() == 1
										&& puzzle.getYCaseVide() < (index / puzzle.getTaille()))) {
							deplacerCase(EDeplacement.DROITE);

						} else {
							// cas où la case vide est au dessus à gauche
							if (puzzle.getYCaseVide() < coordonneesIndex.y
									&& puzzle.getXCaseVide() < coordonneesIndex.x) {
								deplacerCase(EDeplacement.GAUCHE);

							}
						}
					}
				}
			}

		} else {

			// cas où la case est plus haute que ce qu'elle devrait être
			if (IA.getDistanceY(index, puzzle) < 0) {

				// cas où la case vide est au dessus
				if (puzzle.getYCaseVide() < coordonneesIndex.y - 1) {
					deplacerCase(EDeplacement.HAUT);

				} else {
					// cas où la case vide est en dessous
					if (puzzle.getYCaseVide() > coordonneesIndex.y
							&& !(puzzle.getYCaseVide() == (index / puzzle.getTaille()) && puzzle.getXCaseVide() == 0)) {
						deplacerCase(EDeplacement.BAS);

					} else {
						// cas où la case vide est même ligne à droite
						if (puzzle.getYCaseVide() == coordonneesIndex.y
								&& puzzle.getXCaseVide() == coordonneesIndex.x + 1) {
							if (puzzle.getYCaseVide() > 1) {
								deplacerCase(EDeplacement.BAS);

							}
						} else {
							// cas où la case vide est en dessous à gauche
							if (puzzle.getXCaseVide() < coordonneesIndex.x
									&& puzzle.getYCaseVide() > coordonneesIndex.y) {
								deplacerCase(EDeplacement.BAS);

							}
						}
					}
				}

			} else {
				// cas où la case est sur la même ligne
				if (IA.getDistanceY(index, puzzle) == 0) {

					// cas où la case vide est au dessus ligne 2
					if (puzzle.getYCaseVide() == 1 && puzzle.getXCaseVide() == coordonneesIndex.x) {
						deplacerCase(EDeplacement.GAUCHE);

						deplacerCase(EDeplacement.HAUT);
					} else {
						// cas où la case vide est au dessus
						if (puzzle.getYCaseVide() < coordonneesIndex.y) {
							// cas où c'est la dernière case de la colonne
							if (index != (puzzle.getTaille() * (puzzle.getTaille() - 1) + 2)) {
								deplacerCase(EDeplacement.DROITE);

								deplacerCase(EDeplacement.HAUT);

							}
						} else {
							// cas où la case vide est en dessous
							if (puzzle.getYCaseVide() > coordonneesIndex.y
									&& puzzle.getYCaseVide() != (index / puzzle.getTaille()) + 1) {
								deplacerCase(EDeplacement.DROITE);

								deplacerCase(EDeplacement.BAS);

							} else {
								// cas où la case vide est même ligne à droite ligne 2
								if (puzzle.getXCaseVide() == coordonneesIndex.x + 1 && puzzle.getYCaseVide() == 1) {
									deplacerCase(EDeplacement.HAUT);

									deplacerCase(EDeplacement.DROITE);

									deplacerCase(EDeplacement.DROITE);

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
