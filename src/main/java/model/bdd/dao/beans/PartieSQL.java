package main.java.model.bdd.dao.beans;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * <code>{@link <a href=
 * "https://fr.wikipedia.org/wiki/JavaBeans">Java Bean</a>}</code> correspondant
 * à une partie, permettant de faire le lien à travers la couche
 * <code>DAO</code> entre la base de données et le modèle.
 */
public class PartieSQL implements Serializable {

	private static final long serialVersionUID = 1974972864521019924L;

	private long id;
	private int duree_secondes, taille_grille, nb_coups;
	private Timestamp timestamp;

	/**
	 * @return L'identifiant
	 */
	public long getId() {
		return id;
	}

	/**
	 * @param id L'identifiant à mettre à jour
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * @return La durée en secondes
	 */
	public int getDureeSecondes() {
		return duree_secondes;
	}

	/**
	 * @param duree_secondes La durée à mettre à jour
	 */
	public void setDureeSecondes(int duree_secondes) {
		this.duree_secondes = duree_secondes;
	}

	/**
	 * @return La taille de la grille
	 */
	public int getTailleGrille() {
		return taille_grille;
	}

	/**
	 * @param taille_grille La taille de la grille à mettre à jour
	 */
	public void setTailleGrille(int taille_grille) {
		this.taille_grille = taille_grille;
	}

	/**
	 * @return Le nombre de coups
	 */
	public int getNbCoups() {
		return nb_coups;
	}

	/**
	 * @param nb_coups Le nombre de coups à mettre à jour
	 */
	public void setNbCoups(int nb_coups) {
		this.nb_coups = nb_coups;
	}

	/**
	 * @return Le timestamp
	 */
	public Timestamp getTimestamp() {
		return timestamp;
	}

	/**
	 * @param timestamp Le timestamp à mettre à jour
	 */
	public void setTimestamp(Timestamp timestamp) {
		this.timestamp = timestamp;
	}

}
