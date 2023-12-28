package test.java.model.bdd.dao;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import main.java.model.bdd.dao.DAOJoueur;
import main.java.model.bdd.dao.DAOPartie;
import main.java.model.bdd.dao.DAOPartieCooperative;
import main.java.model.bdd.dao.beans.JoueurSQL;
import main.java.model.bdd.dao.beans.PartieCooperativeSQL;
import main.java.model.bdd.dao.beans.PartieSQL;

public class TestDAOPartieCooperative implements TestCRUD {

	private static DAOPartieCooperative dao;
	private static List<Long> listePartiesCooperatives = new ArrayList<>();
	private static List<Long> listeJoueurs = new ArrayList<>();
	private static List<Long> listeParties = new ArrayList<>();
	private static DAOJoueur daoJoueur;
	private static DAOPartie daoPartie;
	private static long idJoueur1, idJoueur2, idJoueur3, idJoueur4, idJoueur5, idJoueur6, idJoueur7, idPartie1,
			idPartie2, idPartie3, idPartie4, idPartie5, idPartie6;

	@BeforeAll
	public static void setUp() {
		dao = new DAOPartieCooperative();
		daoJoueur = new DAOJoueur();
		daoPartie = new DAOPartie();

		JoueurSQL j1 = new JoueurSQL();
		daoJoueur.creer(j1);
		idJoueur1 = j1.getId();

		JoueurSQL j2 = new JoueurSQL();
		daoJoueur.creer(j2);
		idJoueur2 = j2.getId();

		JoueurSQL j3 = new JoueurSQL();
		daoJoueur.creer(j3);
		idJoueur3 = j3.getId();

		JoueurSQL j4 = new JoueurSQL();
		daoJoueur.creer(j4);
		idJoueur4 = j4.getId();

		JoueurSQL j5 = new JoueurSQL();
		daoJoueur.creer(j5);
		idJoueur5 = j5.getId();

		JoueurSQL j6 = new JoueurSQL();
		daoJoueur.creer(j6);
		idJoueur6 = j6.getId();

		JoueurSQL j7 = new JoueurSQL();
		daoJoueur.creer(j7);
		idJoueur7 = j7.getId();

		PartieSQL p1 = new PartieSQL();
		daoPartie.creer(p1);
		idPartie1 = p1.getId();

		PartieSQL p2 = new PartieSQL();
		daoPartie.creer(p2);
		idPartie2 = p2.getId();

		PartieSQL p3 = new PartieSQL();
		daoPartie.creer(p3);
		idPartie3 = p3.getId();

		PartieSQL p4 = new PartieSQL();
		daoPartie.creer(p4);
		idPartie4 = p4.getId();

		PartieSQL p5 = new PartieSQL();
		daoPartie.creer(p5);
		idPartie5 = p5.getId();

		PartieSQL p6 = new PartieSQL();
		daoPartie.creer(p6);
		idPartie6 = p6.getId();
	}

	@Test
	@Override
	public void testCreate() {
		PartieCooperativeSQL p = new PartieCooperativeSQL();
		int val = 999;
		p.setIdPartie(idPartie1);
		p.setIdJoueur(idJoueur1);
		p.setNbCoups(val);
		dao.creer(p);
		listePartiesCooperatives.add(p.getIdPartie());
		listeJoueurs.add(p.getIdJoueur());
		listeParties.add(p.getIdPartie());

		Assertions.assertEquals(p.getNbCoups(), val);
	}

	@Test
	@Override
	public void testRead() {
		PartieCooperativeSQL p = new PartieCooperativeSQL();
		int val = 999;
		p.setIdPartie(idPartie1);
		p.setIdJoueur(idJoueur2);
		p.setNbCoups(val);
		dao.creer(p);
		listePartiesCooperatives.add(p.getIdPartie());
		listeJoueurs.add(p.getIdJoueur());
		listeParties.add(p.getIdPartie());

		Assertions.assertNotNull(dao.trouver(p.getIdPartie()));
	}

	@Test
	@Override
	public void testUpdate() {
		PartieCooperativeSQL p = new PartieCooperativeSQL();
		int val1 = 999, val2 = 9999;
		p.setIdPartie(idPartie2);
		p.setIdJoueur(idJoueur3);
		p.setNbCoups(val1);
		dao.creer(p);
		p.setNbCoups(val2);
		dao.maj(p);
		listePartiesCooperatives.add(p.getIdPartie());
		listeJoueurs.add(p.getIdJoueur());
		listeParties.add(p.getIdPartie());

		Assertions.assertEquals(p.getNbCoups(), val2);
	}

	@Test
	@Override
	public void testDelete() {
		PartieCooperativeSQL p = new PartieCooperativeSQL();
		int val = 999;
		p.setIdPartie(idPartie3);
		p.setIdJoueur(idJoueur4);
		p.setNbCoups(val);
		dao.creer(p);
		dao.supprimer(dao.trouver(p.getIdPartie()));

		Assertions.assertEquals(dao.trouver(p.getIdPartie()).getNbCoups(), 0);
	}

	@Test
	@Override
	public void testFindAll() {
		PartieCooperativeSQL p1 = new PartieCooperativeSQL();
		PartieCooperativeSQL p2 = new PartieCooperativeSQL();
		PartieCooperativeSQL p3 = new PartieCooperativeSQL();
		p1.setIdJoueur(idJoueur5);
		p1.setIdPartie(idPartie4);
		p2.setIdJoueur(idJoueur6);
		p2.setIdPartie(idPartie5);
		p3.setIdJoueur(idJoueur7);
		p3.setIdPartie(idPartie6);
		dao.creer(p1);
		dao.creer(p2);
		dao.creer(p3);
		List<PartieCooperativeSQL> parties = dao.trouverTout();

//		Assertions.assertTrue(parties.stream().anyMatch(item -> item.getIdPartie() == parties.get(0).getIdPartie()));
//		Assertions.assertTrue(parties.stream().anyMatch(item -> item.getIdPartie() == parties.get(1).getIdPartie()));
//		Assertions.assertTrue(parties.stream().anyMatch(item -> item.getIdPartie() == parties.get(2).getIdPartie()));
	}

	@AfterAll
	public static void cleanUp() {

		for (long id : listePartiesCooperatives) {
			dao.supprimer(dao.trouver(id));
		}

		daoPartie.supprimer(daoPartie.trouver(idPartie1));
		daoPartie.supprimer(daoPartie.trouver(idPartie2));
		daoPartie.supprimer(daoPartie.trouver(idPartie3));
		daoPartie.supprimer(daoPartie.trouver(idPartie4));
		daoPartie.supprimer(daoPartie.trouver(idPartie5));
		daoPartie.supprimer(daoPartie.trouver(idPartie6));

		daoJoueur.supprimer(daoJoueur.trouver(idJoueur1));
		daoJoueur.supprimer(daoJoueur.trouver(idJoueur2));
		daoJoueur.supprimer(daoJoueur.trouver(idJoueur3));
		daoJoueur.supprimer(daoJoueur.trouver(idJoueur4));
		daoJoueur.supprimer(daoJoueur.trouver(idJoueur5));
		daoJoueur.supprimer(daoJoueur.trouver(idJoueur6));
		daoJoueur.supprimer(daoJoueur.trouver(idJoueur7));
	}

}