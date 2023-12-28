package main.java.model.ia;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import main.java.model.Case;
import main.java.model.EDeplacement;
import main.java.model.Puzzle;

public class Noeud {
	private Puzzle puzzle;
	private Puzzle puzzleOriginel;
	private Noeud pere;
	private EDeplacement dpMinimal;
	private int g;

	public Noeud(Puzzle puzzle) {
		this.puzzle = puzzle;
		this.puzzleOriginel = puzzle;
		try {
			puzzle = (Puzzle) puzzle.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		this.g = 0;
		this.dpMinimal = EDeplacement.BAS;
	}

	public List<Noeud> successeurs() {
		List<Noeud> successeurs = new ArrayList<>();
		List<EDeplacement> dpPossibles = puzzle.listeDeplacementsPossibles();
		Collections.shuffle(dpPossibles);
		for (EDeplacement dp : dpPossibles) {
			puzzle.deplacerCase(dp);
			Noeud successeur;
			try {
				successeur = new Noeud((Puzzle) puzzle.clone());
				successeur.setPere(this);
				successeur.setG(this.g + 1);
				successeurs.add(successeur);
				successeur.setdeplacement(dp);
			} catch (CloneNotSupportedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			this.restoreToThatNode();
		}
		return successeurs;
	}

	public Puzzle getPuzzle() {
		return puzzle;
	}

	public void restoreToThatNode() {
		puzzle = puzzleOriginel;
		try {
			puzzle = (Puzzle) puzzle.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
	}

	public int calculerH() {
		this.restoreToThatNode();
		int h = 0;
		Case[][] grille = puzzle.getGrille();
		for (int i = 0; i < grille.length; i++) {
			for (int j = 0; j < grille.length; j++) {
				h += IA.manhattanDistance(grille[j][i].getIndex(), puzzle);
			}
		}
		return h;
	}

	public boolean deplacementNulPere() {
		if (this.dpMinimal == EDeplacement.BAS && this.pere.getdeplacement() == EDeplacement.HAUT)
			return true;
		if (this.dpMinimal == EDeplacement.HAUT && this.pere.getdeplacement() == EDeplacement.BAS)
			return true;
		if (this.dpMinimal == EDeplacement.GAUCHE && this.pere.getdeplacement() == EDeplacement.DROITE)
			return true;
		if (this.dpMinimal == EDeplacement.DROITE && this.pere.getdeplacement() == EDeplacement.GAUCHE)
			return true;
		return false;
	}

	public void setPere(Noeud n) {
		this.pere = n;
	}

	public Noeud getPere() {
		return pere;
	}

	public int getG() {
		return g;
	}

	public void setG(int g) {
		this.g = g;
	}

	public EDeplacement getdeplacement() {
		return dpMinimal;
	}

	public void setdeplacement(EDeplacement dp) {
		this.dpMinimal = dp;
	}

	@Override
	public int hashCode() {
		return Objects.hash(puzzle);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Noeud other = (Noeud) obj;
		return puzzle.equals(other.puzzle);
	}

	@Override
	public String toString() {
		return puzzle.toString();
	}

}
