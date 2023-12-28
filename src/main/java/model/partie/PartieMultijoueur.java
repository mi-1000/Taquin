package main.java.model.partie;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import main.java.model.EDeplacement;
import main.java.model.joueur.Joueur;

public abstract class PartieMultijoueur implements StrategyPartie, Serializable {

	protected List<Joueur> joueurs;
	protected byte[] image;
	protected int taille;
	protected boolean partieLancee = false;
	protected long id = -1;

	public abstract void deplacerCase(EDeplacement dp, Joueur joueur, int numJoueur) throws IOException;

	public abstract void deconnecterJoueur(Joueur j);

	/**
	 * 
	 * @param j
	 * @param s
	 * @throws IOException
	 */
	public void ajouterJoueur(Joueur j, Socket s) throws IOException {
		joueurs.add(j);
	}

	/**
	 * Permet de definir les infos de la partie pour pouvoir les envoyer à tous les joueurs.
	 * @param img : image du puzzle
	 * @param t : taille du puzzle
	 */
	public void setInfos(byte[] img, int t) {
		this.image = img;
		this.taille = t;
	}

	/**
	 * 
	 * @param param : paramètres de la requête
	 * @param s : socket du serveur
	 * @param j : joueur qui envoi la requête
	 * @throws IOException
	 */
	public void envoyerJoueurs(String param, Socket s, Joueur j) throws IOException {
		ObjectOutputStream oop = new ObjectOutputStream(s.getOutputStream());
		List<Object> output = new ArrayList<Object>();

		output.add(param);
		output.add(joueurs);
		if (param.equals("i")) {
			output.add(this.image);
			output.add(this.taille);
			output.add(this instanceof PartieMultijoueurCooperative);
		} else if (param.equals("s")) {
			this.partieLancee = true;
		}
		
		if(partieLancee) {
			output = getOutputPuzzle(j);
			output.add(0, "s");
			output.add(1, this.joueurs);
		}
		
		oop.writeObject(output);
		oop.flush();
	}

	/**
	 * 
	 * @param j : joueur qui lance la requête
	 * @return la liste d'objets à envoyer
	 * @throws IOException
	 */
	protected abstract List<Object> getOutputPuzzle(Joueur j);
	
	/**
	 * 
	 * @param param : parametre de la requete.
	 * @param s : socket du serveur.
	 * @param j : joueur qui envoi la requête.
	 * @throws IOException
	 */
	public void envoyerPuzzle(String param, Socket s, Joueur j) throws IOException {
		List<Object> output = new ArrayList<Object>();
		ObjectOutputStream oop = new ObjectOutputStream(s.getOutputStream());

		output.add(param);
		output.addAll(getOutputPuzzle(j));

		oop.writeObject(output);
		oop.flush();
	}

	public List<Joueur> getJoueurs() {
		return this.joueurs;
	}

}
