package test.java.model.bdd;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import main.java.model.bdd.Connexion;

public class TestConnexion {

	private static Connexion instance;

	@Test
	public void testGetInstance() {
		instance = Connexion.getInstance();
		Assertions.assertNotNull(instance, "L'instance de la connexion est nulle.");
	}

	@Test
	public void testFermerConnexion() throws SQLException {
		Connection c = Connexion.getInstance().getConnection();
		Assertions.assertNotNull(c, "La connexion est nulle.");
	}

}
