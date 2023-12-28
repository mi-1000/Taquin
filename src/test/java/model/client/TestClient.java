package test.java.model.client;

import java.io.IOException;
import java.net.Socket;

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
import test.java.aserveur.TestServeur;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TestClient {

	private static Client client1, client2;
	private static Joueur joueur1, joueur2;
	private static PartieMultijoueur partieMultiCoop;
	private static Serveur serveur;
	private final static int PORT_VALIDE = 8090;
	private final int PORT_INVALIDE1 = InvalidPortException.PORT_MAX + 1;
	private final int PORT_INVALIDE2 = -1;

	@BeforeAll
	public static void setUp() throws InvalidPortException {
		try {
			joueur1 = new Joueur("Joueur hôte", null, 0);
			client1 = new Client(joueur1);
			joueur2 = new Joueur("Joueur 2", null, 0);
			client2 = new Client(joueur2);
		} catch (IOException e) {

		}
		partieMultiCoop = new PartieMultijoueurCooperative();
		if (TestServeur.serveur == null) {
			TestServeur.serveur = new Serveur();
			TestServeur.serveur.lancerServeur(partieMultiCoop, PORT_VALIDE);
		}
		serveur = TestServeur.serveur;
		serveur.setPartie(partieMultiCoop);
//		serveur.lancerServeur(partieMultiCoop, PORT_VALIDE);
	}

	@Test
	@Order(1)
	public void testSeConnecterPortInvalide() {
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			client1.seConnecter(NetworkUtils.getServeurIPV4(true), PORT_INVALIDE1);
		});
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			client1.seConnecter(NetworkUtils.getServeurIPV4(true), PORT_INVALIDE2);
		});
	}

	@Test
	@Order(3)
	public void testSeConnecter() {
		Assertions.assertDoesNotThrow(() -> {
			client1.seConnecter(NetworkUtils.getServeurIPV4(true), PORT_VALIDE);
		});
		Assertions.assertDoesNotThrow(() -> {
			client2.seConnecter(NetworkUtils.getServeurIPV4(true), PORT_VALIDE);
		});

		int nbConnexions = TestServeur.serveur.getNoConnexion();
		int nbConnexionsAttendues = 2;

		Socket socketClient1 = client1.getSocket();
		Socket socketClient2 = client2.getSocket();

		Assertions.assertEquals(nbConnexionsAttendues, nbConnexions);
		Assertions.assertNotNull(socketClient1, "La socket du premier client ne devrait pas être null!");
		Assertions.assertNotNull(socketClient2, "La socket du deuxième client ne devrait pas être null!");
	}

}
