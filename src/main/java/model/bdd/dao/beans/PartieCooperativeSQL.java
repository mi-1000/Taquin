package main.java.model.bdd.dao.beans;

import java.io.Serializable;

/**
 * <code>{@link <a href=
 * "https://fr.wikipedia.org/wiki/JavaBeans">Java Bean</a>}</code> correspondant
 * à une table associative entre un joueur et une partie de type coopératif,
 * permettant de faire le lien à travers la couche <code>DAO</code> entre la
 * base de données et le modèle.
 */
public class PartieCooperativeSQL implements Serializable {

	private static final long serialVersionUID = 3716089497283544033L;
	
	private long id_partie, id_joueur;
	private int nb_coups;

	/**
	 * @return L'identifiant de la partie
	 */
	public long getIdPartie() {
		return id_partie;
	}

	/**
	 * @param id_partie L'identifiant de la partie à mettre à jour
	 */
	public void setIdPartie(long id_partie) {
		this.id_partie = id_partie;
	}

	/**
	 * @return L'identifiant du joueur
	 */
	public long getIdJoueur() {
		return id_joueur;
	}

	/**
	 * @param id_joueur L'identifiant du joueur à mettre à jour
	 */
	public void setIdJoueur(long id_joueur) {
		this.id_joueur = id_joueur;
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

}
