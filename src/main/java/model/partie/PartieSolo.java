package main.java.model.partie;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.io.Serializable;

import main.java.model.Chrono;
import main.java.model.EDeplacement;
import main.java.model.Puzzle;
import main.java.model.joueur.Joueur;
import main.java.model.serialisation.Serialisation;

/**
 * Partie pour un joueur seulement
 */
public class PartieSolo implements StrategyPartie, Serializable {

	private static final long serialVersionUID = 484952348040L;

	private Joueur joueur;
	private Puzzle puzzle;
	private PropertyChangeSupport pcs;
	private Chrono timer;

	public PartieSolo(Joueur joueur) {
		this.joueur = joueur;
		this.pcs = new PropertyChangeSupport(this);
		this.timer = new Chrono();
	}

	/**
	 * Lance une partie
	 * 
	 * @param image        Image à reconstituer
	 * @param taillePuzzle Taille de la grille (en cases)
	 * 
	 * @throws IOException -
	 */
	@Override
	public void lancerPartie(byte[] image, int taillePuzzle) throws IOException {
		this.puzzle = new Puzzle(taillePuzzle, image);
	}

	/**
	 * Déplace une case dans la direction spécifiée
	 * 
	 * @param dp Direction du déplacement
	 */
	public void deplacerCase(EDeplacement dp) {
		puzzle.deplacerCase(dp);
		pcs.firePropertyChange("property", 1, 0);
	}
	
	public void undo() {
		puzzle.undo();
		pcs.firePropertyChange("property", 1, 0);
	}

	public Puzzle getPuzzle() {
		return this.puzzle;
	}

	public Joueur getJoueur() {
		return this.joueur;
	}
	
	public Chrono getTimer() {
		return this.timer;
	}

	/**
	 * Permet d'ajouter un PCL, et d'observer la classe.
	 * 
	 * @param pcl Le <i>PropertyChangeListener</i> à appliquer
	 */
	public void addPropertyChangeListener(PropertyChangeListener pcl) {
		pcs.addPropertyChangeListener(pcl);
	}

	/**
	 * Permet de retirer un PCL.
	 * 
	 * @param pcl Le <i>PropertyChangeListener</i> à appliquer
	 */
	public void removePropertyChangeListener(PropertyChangeListener pcl) {
		pcs.removePropertyChangeListener(pcl);
	}

	/**
	 * Sérialise la partie à l'emplacement indiqué
	 * 
	 * @param chemin L'emplacement où enregistrer le fichier contenant la partie
	 *               sérialisée
	 */
	public void serialiser(String chemin) {
		Serialisation.serialiserObjet(this, chemin);
	}

}
