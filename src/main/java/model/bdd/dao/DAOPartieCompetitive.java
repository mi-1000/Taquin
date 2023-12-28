package main.java.model.bdd.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import main.java.model.bdd.Connexion;
import main.java.model.bdd.dao.beans.JoueurSQL;
import main.java.model.bdd.dao.beans.PartieCompetitiveSQL;

/**
 * La couche <code>DAO</code> secondaire qui fait le lien entre la base de
 * données et le <code>JavaBean</code>
 * {@link main.java.model.bdd.dao.beans.PartieCompetitiveSQL
 * PartieCompetitiveSQL}, spécifiquement pour la table
 * <code><i>partie_competitive</i></code> de la base de données, qui enregistre
 * les informations relatives aux parties de type compétitif.
 * 
 * 
 * @see PartieCompetitiveSQL
 *      <code>PartieCompetitiveSQL</code><a role="link" aria-disabled="true"> -
 *      Le <code>JavaBean</code> géré par la classe</a>
 * @see DAO <code>DAO</code><a role="link" aria-disabled="true"> - La couche
 *      abstraite principale dont hérite cette classe</a>
 *
 */
public class DAOPartieCompetitive extends DAO<PartieCompetitiveSQL> {

	/**
	 * Table <code><i>partie_competitive</i></code>, contenant différentes
	 * informations et statistiques sur les parties de type compétitif.
	 */
	private final String PARTIE_COMPETITIVE = "partie_competitive";
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
	 * Colonne <code><i>id</i></code>, correspondant à l'identifiant du joueur
	 * vainqueur.
	 */
	private final String ID_VAINQUEUR = "id_vainqueur";

	/**
	 * {@inheritDoc}
	 * 
	 * @param id L'identifiant de la partie
	 * @return La partie correspondant à l'identifiant passé en paramètre
	 * 
	 */
	@Override
	public PartieCompetitiveSQL trouver(long id) {
		PartieCompetitiveSQL partie = new PartieCompetitiveSQL();
		Connection connexion = Connexion.getInstance().getConnection();
		try {
			try (PreparedStatement pstmt = connexion.prepareStatement(
					"SELECT * FROM " + PARTIE_COMPETITIVE + " WHERE " + ID_PARTIE + " = ?;",
					ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY)) {
				pstmt.setLong(1, id);
				pstmt.execute();
				try (ResultSet rs = pstmt.getResultSet()) {
					if (rs.first()) {
						partie.setIdJoueur(rs.getInt(ID_JOUEUR));
						partie.setIdPartie(id);
						partie.setIdVainqueur(rs.getInt(ID_VAINQUEUR));
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
	public List<PartieCompetitiveSQL> trouverTout() {
		final String ID = ID_PARTIE;
		List<PartieCompetitiveSQL> res = new ArrayList<>();
		Connection connexion = Connexion.getInstance().getConnection();
		try (PreparedStatement pstmt = connexion.prepareStatement("SELECT * FROM " + PARTIE_COMPETITIVE,
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
	public PartieCompetitiveSQL creer(PartieCompetitiveSQL partie) {
		Connection connexion = Connexion.getInstance().getConnection();

		try (PreparedStatement pstmt = connexion
				.prepareStatement("INSERT INTO " + PARTIE_COMPETITIVE + " VALUES (?, ?, ?);")) {
			pstmt.setLong(1, partie.getIdJoueur());
			pstmt.setLong(2, partie.getIdPartie());
			pstmt.setLong(3, partie.getIdVainqueur());
			pstmt.execute();

		} catch (SQLException e) {
			e.printStackTrace();
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
	public PartieCompetitiveSQL maj(PartieCompetitiveSQL partie) {
		Connection connexion = Connexion.getInstance().getConnection();
		try (PreparedStatement pstmt = connexion.prepareStatement("UPDATE " + PARTIE_COMPETITIVE + " SET "
				+ ID_VAINQUEUR + " = ? WHERE " + ID_JOUEUR + " = ? AND " + ID_PARTIE + " = ?")) {
			pstmt.setLong(1, partie.getIdVainqueur());
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
	public void supprimer(PartieCompetitiveSQL partie) {
		Connection connexion = Connexion.getInstance().getConnection();
		try (PreparedStatement pstmt = connexion.prepareStatement(
				"DELETE FROM " + PARTIE_COMPETITIVE + " WHERE " + ID_JOUEUR + " = ? AND " + ID_PARTIE + " = ?;")) {
			pstmt.setLong(1, partie.getIdJoueur());
			pstmt.setLong(2, partie.getIdPartie());
			pstmt.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
