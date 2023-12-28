package main.java.model.api;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Locale;

import com.mysql.cj.util.StringUtils;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import main.java.model.bdd.Connexion;
import main.java.model.bdd.dao.DAOJoueur;

/**
 * <i>Servlet</i> faisant le lien entre le serveur et les objets correspondant
 * aux joueurs
 */
@WebServlet(name = "Classement", urlPatterns = { "/classement" })
public class ServletClassement extends HttpServlet {

	private static final long serialVersionUID = 5719132411446135771L;
	private static DAOJoueur dao = new DAOJoueur();

	/**
	 * Performe une requête <b>GET</b> permettant de récupérer le classement des
	 * joueurs
	 *
	 * <br>
	 * <br>
	 * 
	 * Liste des paramètres acceptés dans l'URL de la requête : <br>
	 * <ul>
	 * <li><b>min</b> : La borne minimale du classement (obligatoirement supérieure
	 * ou égale à <b>1</b>)</li>
	 * <li><b>max</b> : La borne maximale du classement (obligatoirement supérieure
	 * ou égale à <b><i>min</i></b>)</li>
	 * <li><b>tri</b> : L'ordre de tri (valeurs possibles : <b><i>ASC</i></b> ou
	 * <b><i>DESC</i></b>)</li>
	 * <li><b>filtre</b> : La valeur par rapport à laquelle filtrer les résultats
	 * (valeurs possibles : <b><i>nb_victoires</i></b>,
	 * <b><i>ratio_victoires</i></b>, <b><i>temps_partie</i></b>,
	 * <b><i>nb_coups</i></b>)</li>
	 * <li><b><i>[<u>OPTIONNEL</u>]</i> taille_grille</b> : La taille de la grille
	 * pour laquelle récupérer les informations (obligatoirement supérieure à 3)</li>
	 * </ul>
	 * 
	 * <br>
	 * <br>
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public void doGet(HttpServletRequest requete, HttpServletResponse reponse) {
		reponse.setCharacterEncoding("UTF-8");
		reponse.setContentType("application/json");

		String min = requete.getParameter("min");
		String max = requete.getParameter("max");
		String tri = requete.getParameter("tri");
		String filtre = requete.getParameter("filtre");
		String taille_grille = requete.getParameter("taille_grille");

		StringBuilder json = new StringBuilder("{\r\n");
		
		Connection connexion = Connexion.getInstance().getConnection();

		switch (filtre.toLowerCase(Locale.FRANCE)) {
		case "nb_victoires":
			gererNbVictoires(taille_grille, tri, min, max, connexion, json);
			break;
		case "ratio_victoires":
			gererRatioVictoires(taille_grille, tri, min, max, connexion, json);
			break;
		case "temps_partie":
			gererTempsPartie(taille_grille, tri, min, max, connexion, json);
			break;
		case "nb_coups":
			gererNbCoups(taille_grille, tri, min, max, connexion, json);
			break;
		default:
			json.append("Le filtre est incorrect !\r\n");
		}
		try {
			json.append("}");
			ServletOutputStream out = reponse.getOutputStream();
			out.println(json.toString());
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Performe une requête <b>POST</b> sur les données des joueurs
	 * 
	 * <br>
	 * <br>
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public void doPost(HttpServletRequest requete, HttpServletResponse reponse) {

	}

	/**
	 * @param taille_grille Le paramètre <code>taille_grille</code> de la requête
	 */
	private static void verifierTailleGrille(String taille_grille) throws Exception {
		if (taille_grille == null) {
			return;
		}
		if (!StringUtils.isStrictlyNumeric(taille_grille)) {
			throw new Exception("Le paramètre doit être un nombre !");
		}
		int res = Integer.parseInt(taille_grille);
		if (res < 3) {
			throw new Exception("Le paramètre doit être supérieur ou égal à 3 !");
		}
	}

	/**
	 * @param min Le paramètre <code>tri</code> de la requête
	 */
	private static void verifierTri(String tri) throws Exception {
		if (!(tri.toLowerCase(Locale.FRANCE).equals("asc") || tri.toLowerCase(Locale.FRANCE).equals("desc"))) {
			throw new Exception("Le paramètre doit être égal à « ASC » ou « DESC » !");
		}
	}

	/**
	 * @param min Le paramètre <code>min</code> de la requête
	 */
	private static void verifierMin(String min) throws Exception {
		if (!StringUtils.isStrictlyNumeric(min)) {
			throw new Exception("Le paramètre doit être un nombre !");
		}
		int res = Integer.parseInt(min);
		if (res <= 0) {
			throw new Exception("Le paramètre doit être supérieur ou égal à 1 !");
		}
	}

	/**
	 * @param max Le paramètre <code>max</code> de la requête
	 */
	private static void verifierMax(String min, String max) throws Exception {
		verifierMin(min);
		if (!StringUtils.isStrictlyNumeric(max)) {
			throw new Exception("Le paramètre doit être un nombre !");
		}
		int res = Integer.parseInt(max);
		if (res < Integer.parseInt(min)) {
			throw new Exception("Le paramètre doit être supérieur ou égal au mimimum !");
		}
	}
	
	private static void gererNbVictoires(String taille_grille, String tri, String min, String max, Connection connexion, StringBuilder json) {
		String sql;
		if (taille_grille == null) {
			sql = "SELECT id_joueur, COUNT(*) AS nb_victoires FROM partie_competitive AS pc INNER JOIN partie AS p ON pc.id_partie = p.id WHERE pc.id_vainqueur = id_joueur GROUP BY id_joueur ORDER BY nb_victoires %s LIMIT ? OFFSET ?";
		} else {
			sql = "SELECT id_joueur, COUNT(*) AS nb_victoires FROM partie_competitive AS pc INNER JOIN partie AS p ON pc.id_partie = p.id WHERE pc.id_vainqueur = id_joueur AND p.taille_grille = ? ORDER BY nb_victoires %s LIMIT ? OFFSET ?";
		}
		try {
			verifierTailleGrille(taille_grille);
			verifierTri(tri);
			verifierMin(min);
			verifierMax(min, max);
			sql = String.format(sql, tri);
			try (PreparedStatement pstmt = connexion.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY)) {
				if (taille_grille != null) {
					pstmt.setInt(1, Integer.parseInt(taille_grille));
					pstmt.setInt(2, Integer.parseInt(max) - Integer.parseInt(min) + 1);
					pstmt.setInt(3, Integer.parseInt(min) - 1);
				} else {
					pstmt.setInt(1, Integer.parseInt(max) - Integer.parseInt(min) + 1);
					pstmt.setInt(2, Integer.parseInt(min) - 1);
				}
				pstmt.execute();
				try (ResultSet rs = pstmt.getResultSet()) {
					int i = 1;
					while (rs.next()) {
						long id = rs.getLong("id_joueur");
						int nb_victoires = rs.getInt("nb_victoires");
						if (dao.trouver(id).getPseudo() == null) break;
						json.append(i);
						json.append(": {id_joueur: ");
						json.append(id);
						json.append(", pseudo: '");
						json.append(dao.trouver(id).getPseudo());
						json.append("', nb_victoires: ");
						json.append(nb_victoires);
						json.append("},\r\n");
						i++;
					}
					json.deleteCharAt(json.length() - 3); // Suppression de la dernière virgule
				}
			}
		} catch (SQLException e) {
			json.append("erreur_sql: '");
			json.append(e.getMessage());
			json.append("',\r\n");
			e.printStackTrace();
		} catch (Exception e) {
			json.append("erreur: '");
			json.append(e.getMessage());
			json.append(" - ");
			json.append(e.getStackTrace());
			json.append(" - ");
			json.append(e.getClass());
			json.append("',\r\n");
			e.printStackTrace();
		}
	}
	
	private static void gererRatioVictoires(String taille_grille, String tri, String min, String max, Connection connexion, StringBuilder json) {
		String sql;
		if (taille_grille == null) {
			sql = "SELECT id_joueur, COUNT(*) AS nb_victoires, COUNT(*) / (SELECT COUNT(*) FROM partie_competitive AS pc2 WHERE pc2.id_vainqueur = id_joueur) AS ratio_victoires FROM partie_competitive AS pc INNER JOIN partie AS p ON pc.id_partie = p.id WHERE pc.id_vainqueur = id_joueur GROUP BY id_joueur ORDER BY ratio_victoires %s LIMIT ? OFFSET ?";
		} else {
			sql = "SELECT id_joueur, COUNT(*) AS nb_victoires, COUNT(*) / (SELECT COUNT(*) FROM partie_competitive AS pc2 INNER JOIN partie AS p ON pc2.id_partie = p.id WHERE pc2.id_vainqueur = id_joueur AND p.taille_grille = ?) AS ratio_victoires FROM partie_competitive AS pc INNER JOIN partie AS p ON pc.id_partie = p.id WHERE pc.id_vainqueur = id_joueur AND taille_grille = ? GROUP BY id_joueur ORDER BY ratio_victoires %s LIMIT ? OFFSET ?";
		}
		try {
			verifierTailleGrille(taille_grille);
			verifierTri(tri);
			verifierMin(min);
			verifierMax(min, max);
			sql = String.format(sql, tri);
			try (PreparedStatement pstmt = connexion.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY)) {
				if (taille_grille != null) {
					pstmt.setInt(1, Integer.parseInt(taille_grille));
					pstmt.setInt(2, Integer.parseInt(taille_grille));
					pstmt.setInt(3, Integer.parseInt(max) - Integer.parseInt(min) + 1);
					pstmt.setInt(4, Integer.parseInt(min) - 1);
				} else {
					pstmt.setInt(1, Integer.parseInt(max) - Integer.parseInt(min) + 1);
					pstmt.setInt(2, Integer.parseInt(min) - 1);
				}
				pstmt.execute();
				try (ResultSet rs = pstmt.getResultSet()) {
					int i = 1;
					while (rs.next()) {
						long id = rs.getLong("id_joueur");
						double ratio_victoires = rs.getDouble("ratio_victoires");
						if (dao.trouver(id).getPseudo() == null) break;
						json.append(i);
						json.append(": {id_joueur: ");
						json.append(id);
						json.append(", pseudo: '");
						json.append(dao.trouver(id).getPseudo());
						json.append("', ratio_victoires: ");
						json.append(ratio_victoires);
						json.append("},\r\n");
						i++;
					}
					json.deleteCharAt(json.length() - 3); // Suppression de la dernière virgule
				}
			}
		} catch (SQLException e) {
			json.append("erreur_sql: '");
			json.append(e.getMessage());
			json.append("',\r\n");
			e.printStackTrace();
		} catch (Exception e) {
			json.append("erreur: '");
			json.append(e.getMessage());
			json.append(" - ");
			json.append(e.getStackTrace());
			json.append(" - ");
			json.append(e.getClass());
			json.append("',\r\n");
			e.printStackTrace();
		}
	}

	private static void gererTempsPartie(String taille_grille, String tri, String min, String max, Connection connexion, StringBuilder json) {
		String sql;
		if (taille_grille == null) {
			sql = "SELECT id, duree_secondes AS temps_partie FROM partie ORDER by temps_partie %s LIMIT ? OFFSET ?";
		} else {
			sql = "SELECT id, duree_secondes AS temps_partie FROM partie WHERE taille_grille = ? ORDER by temps_partie %s LIMIT ? OFFSET ?";
		}
		try {
			verifierTailleGrille(taille_grille);
			verifierTri(tri);
			verifierMin(min);
			verifierMax(min, max);
			sql = String.format(sql, tri);
			try (PreparedStatement pstmt = connexion.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY)) {
				if (taille_grille != null) {
					pstmt.setInt(1, Integer.parseInt(taille_grille));
					pstmt.setInt(2, Integer.parseInt(max) - Integer.parseInt(min) + 1);
					pstmt.setInt(3, Integer.parseInt(min) - 1);
				} else {
					pstmt.setInt(1, Integer.parseInt(max) - Integer.parseInt(min) + 1);
					pstmt.setInt(2, Integer.parseInt(min) - 1);
				}
				pstmt.execute();
				try (ResultSet rs = pstmt.getResultSet()) {
					int i = 1;
					while (rs.next()) {
						long id = rs.getLong("id");
						int duree_partie = rs.getInt("temps_partie");
						json.append(i);
						json.append(": {id_partie: ");
						json.append(id);
						json.append(", duree_partie: ");
						json.append(duree_partie);
						json.append("},\r\n");
						i++;
					}
					json.deleteCharAt(json.length() - 3); // Suppression de la dernière virgule
				}
			}
		} catch (SQLException e) {
			json.append("erreur_sql: '");
			json.append(e.getMessage());
			json.append("',\r\n");
			e.printStackTrace();
		} catch (Exception e) {
			json.append("erreur: '");
			json.append(e.getMessage());
			json.append(" - ");
			json.append(e.getStackTrace());
			json.append(" - ");
			json.append(e.getClass());
			json.append("',\r\n");
			e.printStackTrace();
		}
	}
	
	private static void gererNbCoups(String taille_grille, String tri, String min, String max, Connection connexion, StringBuilder json) {
		String sql;
		if (taille_grille == null) {
			sql = "SELECT id, nb_coups FROM partie ORDER by nb_coups %s LIMIT ? OFFSET ?";
		} else {
			sql = "SELECT id, nb_coups FROM partie WHERE taille_grille = ? ORDER by nb_coups %s LIMIT ? OFFSET ?";
		}
		try {
			verifierTailleGrille(taille_grille);
			verifierTri(tri);
			verifierMin(min);
			verifierMax(min, max);
			sql = String.format(sql, tri);
			try (PreparedStatement pstmt = connexion.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY)) {
				if (taille_grille != null) {
					pstmt.setInt(1, Integer.parseInt(taille_grille));
					pstmt.setInt(2, Integer.parseInt(max) - Integer.parseInt(min) + 1);
					pstmt.setInt(3, Integer.parseInt(min) - 1);
				} else {
					pstmt.setInt(1, Integer.parseInt(max) - Integer.parseInt(min) + 1);
					pstmt.setInt(2, Integer.parseInt(min) - 1);
				}
				pstmt.execute();
				try (ResultSet rs = pstmt.getResultSet()) {
					int i = 1;
					while (rs.next()) {
						long id = rs.getLong("id");
						int nb_coups = rs.getInt("nb_coups");
						json.append(i);
						json.append(": {id_partie: ");
						json.append(id);
						json.append(", nb_coups: ");
						json.append(nb_coups);
						json.append("},\r\n");
						i++;
					}
					json.deleteCharAt(json.length() - 3); // Suppression de la dernière virgule
				}
			}
		} catch (SQLException e) {
			json.append("erreur_sql: '");
			json.append(e.getMessage());
			json.append("',\r\n");
			e.printStackTrace();
		} catch (Exception e) {
			json.append("erreur: '");
			json.append(e.getMessage());
			json.append(" - ");
			json.append(e.getStackTrace());
			json.append(" - ");
			json.append(e.getClass());
			json.append("',\r\n");
			e.printStackTrace();
		}
	}
	
}