package main.java.model.bdd;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import main.resources.utils.EnvironmentVariablesUtils;

public enum Connexion {
	INSTANCE;

	private Connection connection = null;

	Connexion() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection c = DriverManager.getConnection(EnvironmentVariablesUtils.getBDDURL(),
					EnvironmentVariablesUtils.getBDDUSER(), EnvironmentVariablesUtils.getBDDMDP());
			if (c != null) {
				this.connection = c;
			} else {
				Context initContext = new InitialContext();
				Context envContext = (Context) initContext.lookup("java:comp/env");
				DataSource dataSource = (DataSource) envContext.lookup("jdbc/taquin");
				this.connection = dataSource.getConnection();
			}
		} catch (NamingException | ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			System.err.println("Erreur lors de la connexion : " + e.getMessage());
		}
	}

	public static Connexion getInstance() {
		return INSTANCE;
	}

	public Connection getConnection() {
		return connection;
	}

	/**
	 * Ferme la connexion
	 */
	public void fermerConnexion() {
		if (connection != null) {
			try {
				connection.close();
			} catch (SQLException e) {

			}
			connection = null;
		}
	}

}
