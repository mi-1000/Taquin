package test.java.aserveur;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import main.java.model.client.Client;
import main.java.model.joueur.Joueur;
import main.java.model.partie.PartieMultijoueur;
import main.java.model.partie.PartieMultijoueurCooperative;
import main.java.model.serveur.Serveur;
import main.java.utils.InvalidPortException;
import main.java.utils.NetworkUtils;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TestServeur {

	private static Client client;
	private static Joueur joueur;
	private static PartieMultijoueur partieMultiCoop;
	public static Serveur serveur;
	private final int PORT_VALIDE = 8090;
	private final int PORT_INVALIDE1 = InvalidPortException.PORT_MAX + 1;
	private final int PORT_INVALIDE2 = InvalidPortException.PORT_MIN - 1;

	@BeforeAll
	public static void setUp() {
		joueur = new Joueur("Joueur hôte", null, 0);
		try {
			client = new Client(joueur);
		} catch (IOException e) {
			e.printStackTrace();
		}
		partieMultiCoop = new PartieMultijoueurCooperative();
		serveur = new Serveur();
	}

	@Test
	@Order(1)
	public void testLancerServeurPortInvalide1() {
		Assertions.assertThrows(InvalidPortException.class, () -> {
			serveur.lancerServeur(partieMultiCoop, PORT_INVALIDE1);
		});
	}

	@Test
	@Order(1)
	public void testLancerServeurPortInvalide2() {
		Assertions.assertThrows(InvalidPortException.class, () -> {
			serveur.lancerServeur(partieMultiCoop, PORT_INVALIDE2);
		});
	}

	@Test
	@Order(3)
	public void testLancerServeur()
			throws IOException, InvalidPortException, InterruptedException, ClassNotFoundException {
		Assertions.assertDoesNotThrow(() -> {
			serveur.lancerServeur(partieMultiCoop, PORT_VALIDE);
		});

		int nombreDeConnexions = serveur.getNoConnexion();
		int nombreDeConnexionsAttendus = 0; // il doit y avoir 0 connexion au serveur
		Assertions.assertEquals(nombreDeConnexionsAttendus, nombreDeConnexions);

		nombreDeConnexionsAttendus = 1; // il doit y avoir 1 connexion au serveur
		client.seConnecter(NetworkUtils.getServeurIPV4(true), PORT_VALIDE);
		TimeUnit.SECONDS.sleep(1);
		nombreDeConnexions = serveur.getNoConnexion();
		Assertions.assertEquals(nombreDeConnexionsAttendus, nombreDeConnexions);
	}

}
