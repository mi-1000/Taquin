package main.java.model;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Random;

import javax.imageio.ImageIO;

import main.java.utils.Utils;

public class Puzzle implements Serializable, Cloneable {

	public static final int TAILLE_MAX = 10;
	public static final int TAILLE_MINI = 3;
	private boolean undoUtilise, undoActive;
	private Case[][] grille;
	private byte[] image;
	private int nbCoups;
	private Caretaker ct;

	/**
	 * Définit la taille du puzzle : si inferieur à 3, remise automatiquement à 3.
	 * 
	 * @param taille du Puzzle (si 4 -> 4x4).
	 */
	public Puzzle(int taille) {
		final int TAILLE = Math.max(TAILLE_MINI, Math.min(TAILLE_MAX, taille));
		this.grille = new Case[TAILLE][TAILLE];
		this.initGrille();
		this.nbCoups = 0;
		this.ct = new Caretaker();
		this.undoUtilise = false;
		this.undoActive = true;
	}

	/**
	 * Définit l'image et la taille du puzzle : si inferieur à 3, remise
	 * automatiquement à 3.
	 * 
	 * @param imgSrc : image du puzzle
	 * @param taille du Puzzle (si 4 -> 4x4).
	 * @throws IOException
	 */
	public Puzzle(int taille, byte[] image) throws IOException {
		this(taille);
		this.image = image;
		this.decoupageImage();
	}

	/**
	 * Initialise la grille avec des cases de valeurs allant de 0 à
	 */
	private void initGrille() {
		int compteur = 0;
		for (int i = 0; i < this.grille.length; i++) {
			for (int j = 0; j < this.grille.length; j++) {
				this.grille[j][i] = new Case(compteur);
				compteur++;
			}
		}

		this.grille[this.grille.length - 1][this.grille.length - 1] = new Case(Case.INDEX_CASE_VIDE);
		this.melanger();
	}

	/**
	 * Mélange la grille (déplace la case vide de TAILLE^4 dans une direction
	 * aléatoire pour laisser le taquin soluble).
	 */
	public void melanger() {
		Random rd = new Random();
		int mult;
		if (this.grille.length != 3)
			mult = (int) Math.pow(this.grille.length, 4);
		else
			// La complexité des taquins de taille 3 est souvent trop faibles, on augmente
			// donc significativement le mélange.
			mult = (int) Math.pow(this.grille.length, 5);
		
		do {
			for (int i = 0; i < mult; i++) {
				int x = Utils.getRandomNumberInRange(0, 3);
				switch (x) {
				case 0:
					this.deplacerCase(EDeplacement.HAUT);
					break;
				case 1:
					this.deplacerCase(EDeplacement.BAS);
					break;
				case 2:
					this.deplacerCase(EDeplacement.GAUCHE);
					break;
				case 3:
					this.deplacerCase(EDeplacement.DROITE);
					break;
				}
			}
		} while (this.verifierGrille()); // Permet d'éviter de se retrouver avec une grille ordonnée malgré le mélange
	}

	/**
	 * déplace la case vide dans une direction.
	 * 
	 * @param Enumeration de direction (EDeplacement) : HAUT -> y-1, BAS -> y+1,
	 *                    GAUCHE -> x-1, DROITE -> x+1.
	 */
	public void deplacerCase(EDeplacement dp) {
		int oldCoordX = this.getXCaseVide();
		int oldCoordY = this.getYCaseVide();
		int newCoordX = oldCoordX;
		int newCoordY = oldCoordY;
		switch (dp) {
		case HAUT:
			newCoordY += 1;
			break;
		case BAS:
			newCoordY += -1;
			break;
		case GAUCHE:
			newCoordX += 1;
			break;
		case DROITE:
			newCoordX += -1;
			break;
		}
		if (newCoordX < this.grille.length && newCoordX >= 0 && newCoordY < this.grille.length && newCoordY >= 0) {
			if (ct != null && !this.undoUtilise) {
				Memento m = (Memento) this.saveToMemento();
				ct.addMemento(m);
			}
			if (this.undoUtilise) {
				this.undoActive = false;
			}
			this.echangerCase(new Point(oldCoordX, oldCoordY), new Point(newCoordX, newCoordY));
		}
	}

	public void undo() {
		Object memento = ct.getMemento();
		if (memento != null && this.undoActive) {
			this.restoreFromMemento(memento);
			this.undoUtilise = true;
		}
		if (ct.getNbMementos() == 0) {
			this.undoActive = false;
		}
	}

	/**
	 * Permet d'échanger l'emplacement des deux cases dans la grille.
	 * 
	 * @param p1 Point de la première case
	 * @param p2 Point de la deuxième case
	 */
	private void echangerCase(Point p1, Point p2) {
		try {
			Case tempCase = grille[p1.x][p1.y];
			this.grille[p1.x][p1.y] = this.grille[p2.x][p2.y];
			this.grille[p2.x][p2.y] = tempCase;
			this.nbCoups++;
		} catch (ArrayIndexOutOfBoundsException e) {
			// Ne pas déplacer les cases si les coordonnées sont éronnées
		}
	}

	/**
	 * Vérifie si la grille est dans son état final.
	 * 
	 * @return TRUE si la grille est terminée, FALSE sinon
	 */
	public boolean verifierGrille() {
		int last = -1;
		for (int i = 0; i < this.grille.length; i++) {
			for (int j = 0; j < this.grille.length; j++) {
				if (this.grille[j][i].getIndex() <= last && !(i == grille.length - 1 && j == grille.length - 1))
					return false;
				last = this.grille[j][i].getIndex();
			}
		}
		if (this.grille[grille.length - 1][grille.length - 1].getIndex() != -1)
			return false;
		return true;
	}

	public Case[][] getGrille() {
		return this.grille;
	}

	public void setGrille(Case[][] grille) {
		this.grille = grille;
	}

	/**
	 * Getter coordonnée X Case vide
	 * 
	 * @return coordonnée X de la case vide (0 à gauche -> taille-1 à droite)
	 */
	public int getXCaseVide() {
		int res = -1;
		for (int i = 0; i < this.grille.length; i++) {
			for (int j = 0; j < this.grille.length; j++) {
				if (this.grille[i][j].getIndex() == Case.INDEX_CASE_VIDE) {
					res = i;
				}
			}
		}
		return res;
	}

	/**
	 * Getter coordonnée Y Case vide
	 * 
	 * @return coordonnée Y de la case vide (0 en haut -> taille-1 à en bas)
	 */
	public int getYCaseVide() {
		int res = -1;
		for (int i = 0; i < this.grille.length; i++) {
			for (int j = 0; j < this.grille.length; j++) {
				if (this.grille[i][j].getIndex() == Case.INDEX_CASE_VIDE) {
					res = j;
				}
			}
		}
		return res;
	}

	/**
	 * Permet de découper l'image en images de tailles égales correspondant a
	 * l'index de chaque cases.
	 * 
	 * @throws IOException
	 * 
	 */
	public void decoupageImage() throws IOException {
		BufferedImage img = ImageIO.read(new ByteArrayInputStream(image));
		// Largeur et hauteur des sous-images
		int height = img.getHeight() / this.grille.length;
		int width = img.getWidth() / this.grille.length;
		int index = -1;
		// Parcours de la grille
		for (int i = 0; i < this.grille.length; i++) {
			for (int j = 0; j < this.grille.length; j++) {
				// Initialisation de la sous image
				BufferedImage subImg;
				index = this.grille[j][i].getIndex();
				if (index == -1)
					index = this.grille.length;
				subImg = img.getSubimage(width * (index % this.grille.length), height * (index / this.grille.length),
						width, height); // "Découpe" de l'image
				byte[] newImg = Utils.bufferedImageToByteArray(subImg, null);
				this.grille[j][i].setImage(newImg);
			}
		}
	}

	/**
	 * Récupération de la Case a une coordonnée
	 * 
	 * @param x : Coordonnée x de la case (de gauche à droite)
	 * @param y : Coordonnée y de la case (de haut en bas)
	 * @return La case aux coordonnées demandées.
	 * @throws ArrayIndexOutOfBoundsException
	 */
	public Case getCase(int x, int y) throws ArrayIndexOutOfBoundsException {
		return grille[x][y];
	}

	public int getTaille() {
		return this.grille.length;
	}

	@Override
	public String toString() {
		String res = "";
		for (int i = 0; i < this.grille.length; i++) {
			for (int j = 0; j < this.grille.length; j++) {
				res += this.grille[j][i];
				if (j == this.grille.length - 1)
					res += "\n";
				else
					res += " / ";
			}
		}
		return res;
	}

	public String toStringConsole() {
		String res = "";
		String line = "ABCDEFGHIJKLMNO";
		for (int i = 0; i < this.grille.length; i++) {
			for (int j = 0; j < this.grille.length; j++) {
				if (this.grille[j][i].getIndex() != Case.INDEX_CASE_VIDE) {
					res += line.charAt(this.grille[j][i].getIndex());
				} else {
					res += " ";
				}
				if (j == this.grille.length - 1)
					res += "\n";
				else
					res += " / ";
			}
		}
		return res;
	}

	public int getNbCoups() {
		return this.nbCoups;
	}

	/**
	 * Retourne la liste de déplacements possible à l'état actuel du puzzle
	 * 
	 * @return
	 */
	public List<EDeplacement> listeDeplacementsPossibles() {
		List<EDeplacement> deplacementsPossibles = new ArrayList<>();
		if (this.getXCaseVide() < this.grille.length - 1)
			deplacementsPossibles.add(EDeplacement.GAUCHE);
		if (this.getXCaseVide() > 0)
			deplacementsPossibles.add(EDeplacement.DROITE);
		if (this.getYCaseVide() < this.grille.length - 1)
			deplacementsPossibles.add(EDeplacement.HAUT);
		if (this.getYCaseVide() > 0)
			deplacementsPossibles.add(EDeplacement.BAS);
		return deplacementsPossibles;
	}

	/**
	 * Permet de savoir le déplacement opposé par rapport à une direction de type
	 * EDeplacement
	 * 
	 * @param move EDeplacement
	 * @return EDeplacement mouvement inverse de move
	 */
	public static EDeplacement inverseDeplacement(EDeplacement move) {
		switch (move) {
		case HAUT:
			return EDeplacement.BAS;
		case BAS:
			return EDeplacement.HAUT;
		case GAUCHE:
			return EDeplacement.DROITE;
		case DROITE:
			return EDeplacement.GAUCHE;
		default:
			return EDeplacement.HAUT;
		}
	}

	/**
	 * Sauvegarde l'état du puzzle (grille et nombre de coups actuels) dans un
	 * memento
	 * 
	 * @return Object de type Memento
	 */
	public Object saveToMemento() {
		Case[][] tmp = new Case[this.grille.length][this.grille.length];
		// on clone la grille car c'est un objet, pour éviter d'avoir la même référence
		// dans le memento
		for (int i = 0; i < this.grille.length; i++) {
			for (int j = 0; j < this.grille.length; j++) {
				try {
					tmp[j][i] = (Case) grille[j][i].clone();
				} catch (CloneNotSupportedException e) {
					e.printStackTrace();
				}
			}
		}
		return new Memento(tmp, nbCoups);
	}

	/**
	 * Restore depuis un memento le puzzle
	 * 
	 * @param m, Object de type Memento attendu
	 */
	public void restoreFromMemento(Object m) {
		if (m instanceof Memento) {
			Memento memento = (Memento) m;
			this.grille = memento.grille;
			this.nbCoups = memento.nbCoups;
		}
	}

	private static class Memento implements Serializable { // définition d’une classe interne pour la sauvegarde
		private Case[][] grille;
		private int nbCoups;

		public Memento(Case[][] g, int coups) {
			grille = g;
			nbCoups = coups;
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.deepHashCode(grille);
		result = prime * result + Objects.hash(grille.length, nbCoups);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Puzzle other = (Puzzle) obj;
		boolean grillesEquals = true;
		for (int i = 0; i < grille.length; i++) {
			for (int j = 0; j < grille.length; j++) {
				if (grille[j][i].getIndex() != other.grille[j][i].getIndex()) {
					grillesEquals = false;
				}
			}
		}
		return grillesEquals;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		Puzzle clonedPuzzle = (Puzzle) super.clone();
		// Clonage de la grille
		Case[][] clonedGrille = new Case[this.grille.length][this.grille.length];
		for (int i = 0; i < this.grille.length; i++) {
			for (int j = 0; j < this.grille.length; j++) {
				clonedGrille[i][j] = new Case(grille[i][j].getIndex());
			}
		}
		clonedPuzzle.grille = clonedGrille;
		return clonedPuzzle;
	}

	public boolean undoActive() {
		return this.undoActive;
	}

}
