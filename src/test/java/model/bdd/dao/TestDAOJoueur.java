package test.java.model.bdd.dao;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import main.java.model.bdd.dao.DAOJoueur;
import main.java.model.bdd.dao.beans.JoueurSQL;

public class TestDAOJoueur implements TestCRUD {

	private static DAOJoueur dao;
	private static List<Long> listeJoueurs = new ArrayList<>();

	@BeforeAll
	public static void setUp() {
		dao = new DAOJoueur();
	}

	@Test
	@Override
	public void testCreate() {
		JoueurSQL j = new JoueurSQL();
		String pseudo = "Test Créer";
		j.setPseudo(pseudo);
		j.setUrlpp(pseudo);
		dao.creer(j);
		listeJoueurs.add(j.getId());
		
		JoueurSQL j2 = dao.trouver(j.getId());

		Assertions.assertEquals(j2.getPseudo(), pseudo);
	}

	@Test
	@Override
	public void testRead() {
		JoueurSQL j = new JoueurSQL();
		dao.creer(j);
		listeJoueurs.add(j.getId());

		Assertions.assertNotNull(dao.trouver(j.getId()));
	}

	@Test
	@Override
	public void testUpdate() {
		 JoueurSQL j = new JoueurSQL();
		 String pseudo = "Test Màj";
		 String pseudo2 = "Retest Màj";
		 j.setPseudo(pseudo);
		 dao.creer(j);
		 j.setPseudo(pseudo2);
		 dao.maj(j);
		 listeJoueurs.add(j.getId());

		 Assertions.assertEquals(j.getPseudo(), pseudo2);
	}

	@Test
	@Override
	public void testDelete() {
		JoueurSQL j = new JoueurSQL();
		String pseudo = "Test Supprimer";
		j.setPseudo(pseudo);
		dao.creer(j);
		dao.supprimer(dao.trouver(j.getId()));

		Assertions.assertNull(dao.trouver(j.getId()).getPseudo());
	}
	
	@Test
	@Override
	public void testFindAll() {
		JoueurSQL j1 = new JoueurSQL();
		JoueurSQL j2 = new JoueurSQL();
		JoueurSQL j3 = new JoueurSQL();
		dao.creer(j1);
		dao.creer(j2);
		dao.creer(j3);
		List<JoueurSQL> joueurs = dao.trouverTout();
		listeJoueurs.add(j1.getId());
		listeJoueurs.add(j2.getId());
		listeJoueurs.add(j3.getId());

		Assertions.assertTrue(joueurs.stream().anyMatch(item -> item.equals(joueurs.get(0))));
		Assertions.assertTrue(joueurs.stream().anyMatch(item -> item.equals(joueurs.get(1))));
		Assertions.assertTrue(joueurs.stream().anyMatch(item -> item.equals(joueurs.get(2))));
	}

	@AfterAll
	public static void cleanUp() {
		for (long id : listeJoueurs) {
			dao.supprimer(dao.trouver(id));
		}
	}

}