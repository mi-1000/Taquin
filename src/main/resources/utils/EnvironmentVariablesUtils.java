package main.resources.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Variables de propriétés
 */
public class EnvironmentVariablesUtils {

	private static final String ENVIRONMENT_VARIABLES_FILE = "ENVIRONMENT.properties";

	/**
	 * @return L'URL de la base de données
	 */
	public static String getBDDURL() {
		return openEnvironmentFile("BDD_URL");
	}

	/**
	 * @return L'utilisateur de la base de données
	 */
	public static String getBDDUSER() {
		return openEnvironmentFile("BDD_UTILISATEUR");
	}

	/**
	 * @return Le mot de passe de la base de données
	 */
	public static String getBDDMDP() {
		return openEnvironmentFile("BDD_MDP");
	}

	/**
	 * Ouvre le fichier d'environnement.
	 *
	 * @param field Champ du fichier
	 * @return La valeur du champ
	 */
    private static String openEnvironmentFile(String field) {
        String res = "";
        try (InputStream input = new FileInputStream("src/main/resources/utils/" + ENVIRONMENT_VARIABLES_FILE)) {

            Properties prop = new Properties();

            // Charge le fichier contenant les propriétés
            prop.load(input);

            // Récupère la valeur de la propriété
            res = prop.getProperty(field);

        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return res;
    }

}
