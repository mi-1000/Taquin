package main.java.model.partie;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import main.java.model.EDeplacement;
import main.java.model.Puzzle;
import main.java.model.bdd.dao.DAOPartie;
import main.java.model.bdd.dao.DAOPartieCompetitive;
import main.java.model.bdd.dao.DAOPartieCooperative;
import main.java.model.bdd.dao.beans.PartieCompetitiveSQL;
import main.java.model.bdd.dao.beans.PartieCooperativeSQL;
import main.java.model.bdd.dao.beans.PartieSQL;
import main.java.model.joueur.Joueur;

public class PartieMultijoueurCompetitive extends PartieMultijoueur {

	private Map<Joueur, Puzzle> tablePuzzleDesJoueurs;
	private Joueur joueurGagnant;

	public PartieMultijoueurCompetitive() {
		joueurs = new ArrayList<>();
		tablePuzzleDesJoueurs = new HashMap<>();
	}

	@Override
	public void lancerPartie(byte[] image, int taillePuzzle) throws IOException {
		for (Joueur j : joueurs) {
			Puzzle puzzleDuJoueurJ = new Puzzle(taillePuzzle, image);
			tablePuzzleDesJoueurs.put(j, puzzleDuJoueurJ);
		}
	}
	
	@Override
	public void deplacerCase(EDeplacement dp, Joueur joueur, int numJoueur) throws IOException {
		Puzzle puzzleDuJoueur = tablePuzzleDesJoueurs.get(joueur);
		puzzleDuJoueur.deplacerCase(dp);
	}

	/**
	 * Vérifie si un joueur de la partie a gagné
	 * 
	 * @return boolean si un joueur a gagné
	 */
	public boolean unJoueurAGagne() {
		// parcours de tous les joueurs et si une grille est vérifiée alors la partie
		// est finie et on return true
		for (Map.Entry<Joueur, Puzzle> mapEntry : tablePuzzleDesJoueurs.entrySet()) {
			Joueur j = mapEntry.getKey();
			Puzzle p = mapEntry.getValue();
			if (p.verifierGrille()) {
				joueurGagnant = j;
				return true;
			}
		}
		return false;
	}
	
	@Override
	protected List<Object> getOutputPuzzle(Joueur j){
		List<Object> output = new ArrayList<Object>();
		output.add(this.getPuzzleDuJoueur(j));
		if(this.unJoueurAGagne()) {
			if(this.id==-1) {
				DAOPartie daop = new DAOPartie();
				PartieSQL psql = new PartieSQL();
				psql.setTailleGrille(this.taille);
				psql = daop.creer(psql);
				this.id=psql.getId();
			}
			DAOPartieCompetitive daopc = new DAOPartieCompetitive();
			PartieCompetitiveSQL pcsql = new PartieCompetitiveSQL();
			pcsql.setIdJoueur(j.getId());
			pcsql.setIdPartie(this.id);
			pcsql.setIdVainqueur(joueurGagnant.getId());
			daopc.creer(pcsql);
		}
		return output;
	}

	@Override
	public void deconnecterJoueur(Joueur j) {
		joueurs.remove(j);
		tablePuzzleDesJoueurs.remove(j);
	}

	public Puzzle getPuzzleDuJoueur(Joueur j) {
		return tablePuzzleDesJoueurs.get(j);
	}

	public Map<Joueur, Puzzle> getTablePuzzleDesJoueurs() {
		return tablePuzzleDesJoueurs;
	}

}
