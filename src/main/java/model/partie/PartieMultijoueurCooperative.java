package main.java.model.partie;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import main.java.model.EDeplacement;
import main.java.model.Puzzle;
import main.java.model.bdd.dao.DAOPartie;
import main.java.model.bdd.dao.DAOPartieCooperative;
import main.java.model.bdd.dao.beans.PartieCooperativeSQL;
import main.java.model.bdd.dao.beans.PartieSQL;
import main.java.model.joueur.Joueur;

public class PartieMultijoueurCooperative extends PartieMultijoueur{ 

	private Puzzle puzzleCommun;
	private int indexJoueurCourant; // index qui indique quel joueur de la List<Joueur> joueurs doit jouer son tour

	/**
	 * Construit une partie multijoueur cooperative se jouant tour par tour
	 * 
	 */
	public PartieMultijoueurCooperative() {
		indexJoueurCourant = 0;
		joueurs = new ArrayList<>();
	}

	@Override
	public void lancerPartie(byte[] image, int taillePuzzle) throws IOException {
		puzzleCommun = new Puzzle(taillePuzzle, image);
	}
	
	@Override
	protected List<Object> getOutputPuzzle(Joueur j){
		List<Object> output = new ArrayList<Object>();
		output.add(this.indexJoueurCourant);
		output.add(this.puzzleCommun);
		if(this.puzzleCommun.verifierGrille()) {
			if(this.id==-1) {
				DAOPartie daop = new DAOPartie();
				PartieSQL psql = new PartieSQL();
				psql.setNbCoups(puzzleCommun.getNbCoups());
				psql.setTailleGrille(this.taille);
				psql = daop.creer(psql);
				this.id=psql.getId();
			}

			DAOPartieCooperative daopc = new DAOPartieCooperative();
			PartieCooperativeSQL pcsql = new PartieCooperativeSQL();
			pcsql.setIdJoueur(j.getId());
			pcsql.setNbCoups(puzzleCommun.getNbCoups());
			pcsql.setIdPartie(this.id);
			daopc.creer(pcsql);
		}
		return output;
	}

	@Override
	public void deconnecterJoueur(Joueur j) {
		joueurs.remove(j);
	}

	/**
	 * Incrémente l'index du joueur qui joue son tour dans la liste de joueurs
	 * 
	 * @throws IOException
	 */
	private void passerAuJoueurSuivant() throws IOException {
		this.indexJoueurCourant++;
		this.indexJoueurCourant %= joueurs.size();
	}

	/**
	 * 
	 * @param dp        EDeplacement de la case
	 * @param numJoueur numero du joueur dans la liste
	 * @throws IOException
	 */
	public void deplacerCase(EDeplacement dp, Joueur joueur, int numJoueur) throws IOException {
		if (numJoueur == this.indexJoueurCourant + 1 && !puzzleCommun.verifierGrille()) {
			puzzleCommun.deplacerCase(dp);
			passerAuJoueurSuivant();
		}
	}
	
	public boolean partieFinie() {
		return puzzleCommun.verifierGrille();
	}

	public Puzzle getPuzzleCommun() {
		return this.puzzleCommun;
	}

	private Joueur getJoueurCourant() {
		Joueur joueurCourant = joueurs.get(indexJoueurCourant);
		return joueurCourant;
	}

	public int getIndexJoueurCourant() {
		return indexJoueurCourant;
	}

}
