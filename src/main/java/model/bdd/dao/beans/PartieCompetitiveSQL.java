package main.java.model.bdd.dao.beans;

import java.io.Serializable;

/**
 * <code>{@link <a href=
 * "https://fr.wikipedia.org/wiki/JavaBeans">Java Bean</a>}</code> correspondant
 * à une table associative entre un joueur et une partie de type compétitif,
 * permettant de faire le lien à travers la couche <code>DAO</code> entre la
 * base de données et le modèle.
 */
public class PartieCompetitiveSQL implements Serializable {

	private static final long serialVersionUID = 2636352681810373717L;

	private long id_joueur, id_partie, id_vainqueur;

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
	 * @return L'identifiant du vainqueur
	 */
	public long getIdVainqueur() {
		return id_vainqueur;
	}

	/**
	 * @param id_vainqueur L'identifiant du vainqueur à mettre à jour
	 */
	public void setIdVainqueur(long id_vainqueur) {
		this.id_vainqueur = id_vainqueur;
	}

}
