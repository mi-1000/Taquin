package test.java.model.bdd.dao;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import main.java.model.bdd.dao.DAOPartie;
import main.java.model.bdd.dao.beans.PartieSQL;

public class TestDAOPartie implements TestCRUD {

	private static DAOPartie dao;
	private static List<Long> listeParties = new ArrayList<>();

	@BeforeAll
	public static void setUp() {
		dao = new DAOPartie();
	}

	@Test
	@Override
	public void testCreate() {
		PartieSQL p = new PartieSQL();
		int val = 999;
		p.setDureeSecondes(val);
		p.setTailleGrille(val);
		p.setNbCoups(val);
		dao.creer(p);
		listeParties.add(p.getId());

		Assertions.assertEquals(dao.trouver(p.getId()).getDureeSecondes(), val);
		Assertions.assertEquals(dao.trouver(p.getId()).getTailleGrille(), val);
		Assertions.assertEquals(dao.trouver(p.getId()).getNbCoups(), val);
	}

	@Test
	@Override
	public void testRead() {
		PartieSQL p = new PartieSQL();
		dao.creer(p);
		listeParties.add(p.getId());

		Assertions.assertNotNull(dao.trouver(p.getId()));
	}

	@Test
	@Override
	public void testUpdate() {
		PartieSQL p = new PartieSQL();
		int val = 999;
		int val2 = 9999;
		p.setDureeSecondes(val);
		p.setTailleGrille(val);
		p.setNbCoups(val);
		dao.creer(p);
		p.setDureeSecondes(val2);
		p.setTailleGrille(val2);
		p.setNbCoups(val2);
		dao.maj(p);
		listeParties.add(p.getId());

		Assertions.assertEquals(dao.trouver(p.getId()).getDureeSecondes(), val2);
		Assertions.assertEquals(dao.trouver(p.getId()).getTailleGrille(), val2);
		Assertions.assertEquals(dao.trouver(p.getId()).getNbCoups(), val2);
	}

	@Test
	@Override
	public void testDelete() {
		PartieSQL p = new PartieSQL();
		int val = 999;
		p.setDureeSecondes(val);
		p.setTailleGrille(val);
		p.setNbCoups(val);
		dao.creer(p);
		dao.supprimer(dao.trouver(p.getId()));

		Assertions.assertEquals(dao.trouver(p.getId()).getDureeSecondes(), 0);
		Assertions.assertEquals(dao.trouver(p.getId()).getTailleGrille(), 0);
		Assertions.assertEquals(dao.trouver(p.getId()).getNbCoups(), 0);
	}
	
	@Test
	@Override
	public void testFindAll() {
		PartieSQL p1 = new PartieSQL();
		PartieSQL p2 = new PartieSQL();
		PartieSQL p3 = new PartieSQL();
		dao.creer(p1);
		dao.creer(p2);
		dao.creer(p3);
		List<PartieSQL> parties = dao.trouverTout();
		listeParties.add(p1.getId());
		listeParties.add(p2.getId());
		listeParties.add(p3.getId());
		
		Assertions.assertTrue(parties.stream().anyMatch(item -> item.equals(parties.get(0))));
		Assertions.assertTrue(parties.stream().anyMatch(item -> item.equals(parties.get(1))));
		Assertions.assertTrue(parties.stream().anyMatch(item -> item.equals(parties.get(2))));
	}

	@AfterAll
	public static void cleanUp() {
		for (long id : listeParties) {
			dao.supprimer(dao.trouver(id));
		}
	}

}