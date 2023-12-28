package main.java.model.bdd.dao;

import java.util.List;

/**
 * La couche <code>DAO</code> principale dont vont hériter tous les
 * <code>DAO</code> de toutes les tables
 *
 * @param <T> Le type du <code>JavaBean</code>
 */
public abstract class DAO<T> {


	/**
	 * Permet de récupérer un objet via son identifiant.
	 *
	 * @param id L'identifiant
	 * @return L'objet correspondant à l'identifiant
	 */
	public abstract T trouver(long id);
	
	/**
	 * Permet de récupérer tous les objets de la base de données.
	 *
	 * @return Une liste d'objets correspondants
	 */
	public abstract List<T> trouverTout();

	/**
	 * Permet de créer une entrée dans la base de données.
	 *
	 * @param obj L'objet à créer
	 * @return L'objet créé
	 */
	public abstract T creer(T obj);

	/**
	 * Permet de mettre à jour les données d'une entrée dans la base.
	 *
	 * @param obj L'objet à mettre à jour
	 * @return L'objet mis à jour
	 */
	public abstract T maj(T obj);

	/**
	 * Permet la suppression d'une entrée de la base.
	 *
	 * @param obj L'objet à supprimer
	 */
	public abstract void supprimer(T obj);
}
