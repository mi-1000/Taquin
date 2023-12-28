package main.java.model.bdd;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import main.java.model.Puzzle;
import main.java.model.bdd.dao.beans.JoueurSQL;

/**
 * Fonctions utilitaires pour la base de données du jeu
 */
public class SQLUtils {

	/**
	 * Renvoie le nombre de victoires d'un joueur passé en paramètre. <br>
	 * Il est possible de spécifier une taille de grille en particulier, ou
	 * d'obtenir le résultat en général. <br>
	 * Pour ne pas tenir compte de la taille de la grille, veuillez entrer n'importe
	 * quelle valeur strictement inférieure à <code>3</code>, même si une valeur de
	 * <code>0</code> ou <code>-1</code> est conseillée pour la lisibilité.
	 * 
	 * @param joueur        Le joueur dont il faut renvoyer le nombre de victoires
	 * @param taille_grille La taille de la grille pour laquelle la recherche doit
S	 *                      s'appliquer. Pour ne pas tenir compte de ce paramètre,
	 *                      entrer une taille strictement inférieure à
	 *                      <code>3</code>.
	 * @return Le nombre de victoires du joueur. <br>
	 *         <b>Note : </b> Si la fonction renvoie <code>0</code>, il est possible
	 *         que le joueur ait effectivement <code>0</code> victoires, ou qu'il
	 *         n'ait pas été trouvé dans la base de données.
	 */
	public static int getNbVictoires(JoueurSQL joueur, int taille_grille) {
		String sql;
		int nb_victoires = 0;
		if (taille_grille < 3) {
			sql = "SELECT COUNT(DISTINCT pc.id_partie) AS nb_victoires FROM partie_competitive AS pc INNER JOIN partie AS p ON pc.id_partie = p.id WHERE pc.id_vainqueur = ?";
		} else {
			sql = "SELECT COUNT(DISTINCT pc.id_partie) AS nb_victoires FROM partie_competitive AS pc INNER JOIN partie AS p ON pc.id_partie = p.id WHERE pc.id_vainqueur = ? AND p.taille_grille = ?";
		}
		try {
			Connection connexion = Connexion.getInstance().getConnection();
			try (PreparedStatement pstmt = connexion.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE,
							ResultSet.CONCUR_READ_ONLY)) {
				if (taille_grille < 3) {
					pstmt.setLong(1, joueur.getId());
				} else {
					pstmt.setLong(1, joueur.getId());
					pstmt.setInt(2, taille_grille);
				}
				pstmt.execute();
				try (ResultSet rs = pstmt.getResultSet()) {
					if (rs.next()) {
						nb_victoires = rs.getInt("nb_victoires");
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			return nb_victoires;
		}
	}
	
	
	public static int getTempsPartie(JoueurSQL joueur, int taille_grille) {
		String sql;
		int duree = 0;
		if (taille_grille < Puzzle.TAILLE_MINI) {
			sql = "SELECT MIN(duree_secondes) AS duree FROM partie_competitive AS pc INNER JOIN partie AS p ON pc.id_partie = p.id WHERE pc.id_vainqueur = ?";
		} else {
			sql = "SELECT MIN(duree_secondes) AS duree FROM partie_competitive AS pc INNER JOIN partie AS p ON pc.id_partie = p.id WHERE pc.id_vainqueur = ? AND p.taille_grille = ?";
		}
		try {
			Connection connexion = Connexion.getInstance().getConnection();
			try (PreparedStatement pstmt = connexion.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE,
							ResultSet.CONCUR_READ_ONLY)) {
				if (taille_grille < Puzzle.TAILLE_MINI) {
					pstmt.setLong(1, joueur.getId());
				} else {
					pstmt.setLong(1, joueur.getId());
					pstmt.setInt(2, taille_grille);
				}
				pstmt.execute();
				try (ResultSet rs = pstmt.getResultSet()) {
					if (rs.next()) {
						duree = rs.getInt("duree");
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			return duree;
		}
	}
}