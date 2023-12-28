package main.java.model.bdd.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import main.java.model.bdd.Connexion;
import main.java.model.bdd.dao.beans.JoueurSQL;
import main.java.model.bdd.dao.beans.PartieCooperativeSQL;

/**
 * La couche <code>DAO</code> secondaire qui fait le lien entre la base de
 * données et le <code>JavaBean</code>
 * {@link main.java.model.bdd.dao.beans.PartieCooperativeSQL
 * PartieCooperativeSQL}, spécifiquement pour la table
 * <code><i>partie_cooperative</i></code> de la base de données, qui enregistre
 * les informations relatives aux parties de type coopératif.
 * 
 * 
 * @see PartieCooperativeSQL
 *      <code>PartieCooperativeSQL</code><a role="link" aria-disabled="true"> -
 *      Le <code>JavaBean</code> géré par la classe</a>
 * @see DAO <code>DAO</code><a role="link" aria-disabled="true"> - La couche
 *      abstraite principale dont hérite cette classe</a>
 *
 */
public class DAOPartieCooperative extends DAO<PartieCooperativeSQL> {

	/**
	 * Table <code><i>partie_cooperative</i></code>, contenant différentes
	 * informations et statistiques sur les parties de type compétitif.
	 */
	private final String PARTIE_COOPERATIVE = "partie_cooperative";
	/**
	 * Colonne <code><i>id_joueur</i></code>, correspondant à l'identifiant du
	 * joueur.
	 */
	private final String ID_JOUEUR = "id_joueur";
	/**
	 * Colonne <code><i>id_partie</i></code>, correspondant à l'identifiant de la
	 * partie.
	 */
	private final String ID_PARTIE = "id_partie";
	/**
	 * Colonne <code><i>nb_coups</i></code>, correspondant au nombre de coups
	 * effectués.
	 */
	private final String NB_COUPS = "nb_coups";

	/**
	 * {@inheritDoc}
	 * 
	 * @param id L'identifiant de la partie
	 * @return La partie correspondant à l'identifiant passé en paramètre
	 * 
	 */
	@Override
	public PartieCooperativeSQL trouver(long id) {
		PartieCooperativeSQL partie = new PartieCooperativeSQL();
		Connection connexion = Connexion.getInstance().getConnection();
		try {
			try (PreparedStatement pstmt = connexion.prepareStatement(
					"SELECT * FROM " + PARTIE_COOPERATIVE + " WHERE " + ID_PARTIE + " = ?;",
					ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY)) {
				pstmt.setLong(1, id);
				pstmt.execute();
				try (ResultSet rs = pstmt.getResultSet()) {
					if (rs.first()) {
						partie.setIdJoueur(rs.getInt(ID_JOUEUR));
						partie.setIdPartie(id);
						partie.setNbCoups(rs.getInt(NB_COUPS));
					}
				}
			}
		} catch (SQLException e) {

		}
		return partie;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @return Une liste contenant tous les joueurs sauvegardés dans la base de
	 *         données
	 * 
	 */
	@Override
	public List<PartieCooperativeSQL> trouverTout() {
		final String ID = ID_PARTIE;
		List<PartieCooperativeSQL> res = new ArrayList<>();
		Connection connexion = Connexion.getInstance().getConnection();
		try (PreparedStatement pstmt = connexion.prepareStatement("SELECT * FROM " + PARTIE_COOPERATIVE,
				ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)) {
			try (ResultSet rs = pstmt.executeQuery()) {
				while (rs.next()) {
					long id = rs.getLong(ID);
					res.add(this.trouver(id));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return res;
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @param partie La partie à créer dans la base de données
	 * @return La partie
	 */
	@Override
	public PartieCooperativeSQL creer(PartieCooperativeSQL partie) {
		Connection connexion = Connexion.getInstance().getConnection();

		try (PreparedStatement pstmt = connexion
				.prepareStatement("INSERT INTO " + PARTIE_COOPERATIVE + " VALUES (?, ?, ?);")) {
			pstmt.setLong(1, partie.getIdPartie());
			pstmt.setLong(2, partie.getIdJoueur());
			pstmt.setInt(3, partie.getNbCoups());
			pstmt.execute();

		} catch (SQLException e) {
			
		}
		return partie;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @param partie La partie à mettre à jour dans la base de données
	 * @return La partie
	 */
	@Override
	public PartieCooperativeSQL maj(PartieCooperativeSQL partie) {
		Connection connexion = Connexion.getInstance().getConnection();
		try (PreparedStatement pstmt = connexion.prepareStatement("UPDATE " + PARTIE_COOPERATIVE + " SET " + NB_COUPS
				+ " = ? WHERE " + ID_JOUEUR + " = ? AND " + ID_PARTIE + " = ?")) {
			pstmt.setInt(1, partie.getNbCoups());
			pstmt.setLong(2, partie.getIdJoueur());
			pstmt.setLong(3, partie.getIdPartie());
			pstmt.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return partie;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @param partie La partie à supprimer de la base de données
	 */
	@Override
	public void supprimer(PartieCooperativeSQL partie) {
		Connection connexion = Connexion.getInstance().getConnection();
		try (PreparedStatement pstmt = connexion.prepareStatement(
				"DELETE FROM " + PARTIE_COOPERATIVE + " WHERE " + ID_JOUEUR + " = ? AND " + ID_PARTIE + " = ?;")) {
			pstmt.setLong(1, partie.getIdJoueur());
			pstmt.setLong(2, partie.getIdPartie());
			pstmt.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
