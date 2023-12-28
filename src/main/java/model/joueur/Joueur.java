package main.java.model.joueur;

import java.io.Serializable;
import java.util.Objects;

/**
 * Représente un joueur.
 *
 */
public class Joueur implements Serializable {

	private String nom;
	private byte[] image;
	private long id;

	/**
	 * 
	 * @param nom   Nom du joueur
	 * @param image Image au format binaire correspondant à l'avatar du joueur
	 */
	public Joueur(String nom, byte[] image, long id) {
		this.nom = nom;
		this.image = image;
	}
	
	public long getId() {
		return this.id;
	}

	public String getNom() {
		return nom;
	}

	public byte[] getImage() {
		return image;
	}

	@Override
	public String toString() {
		return this.nom;
	}

	@Override
	public int hashCode() {
		return Objects.hash(nom);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Joueur other = (Joueur) obj;
		return Objects.equals(nom, other.nom);
	}

}
