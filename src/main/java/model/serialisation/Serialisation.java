package main.java.model.serialisation;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Fournit des fonctions utilitaires pour sérialiser et désérialiser un objet
 */
public class Serialisation {

	/**
	 * Sérialise l'objet passé en paramètre et sauvegarde le fichier obtenu à
	 * l'emplacement spécifié
	 * 
	 * @param objet  L'objet à sérialiser
	 * @param chemin Le chemin où enregistrer le fichier contenant l'objet sérialisé
	 *               <br>
	 *               <br>
	 *               <b>NOTE : </b> <i>Les fichiers sérialisés doivent se terminer
	 *               par l'extension <b>.ser</b></i>
	 * @return <code>true</code> en cas de succès, <code>false</code> sinon
	 */
	public static boolean serialiserObjet(Object objet, String chemin) {
		try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(new File(chemin)))) {
			oos.writeObject(objet);
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Désérialise un objet à partir de son emplacement
	 * 
	 * @param <T>    Le type de l'objet à désérialiser
	 * 
	 * @param classe La classe de l'objet à désérialiser
	 * @param chemin Le chemin où est enregistré le fichier contenant l'objet
	 *               sérialisé
	 * @return L'objet désérialisé en cas de succès, <code>null</code> sinon
	 */
	public static <T> T deserialiserObjet(Class<T> classe, String chemin) {
		try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(chemin))) {
			return classe.cast(ois.readObject());
		} catch (IOException | ClassNotFoundException | ClassCastException e) {
			e.printStackTrace();
			return null;
		}
	}

}
