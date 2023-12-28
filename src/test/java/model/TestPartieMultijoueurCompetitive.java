package test.java.model;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import javafx.scene.image.Image;
import main.java.model.Case;
import main.java.model.EDeplacement;
import main.java.model.Puzzle;
import main.java.model.client.Client;
import main.java.model.joueur.Joueur;
import main.java.model.partie.PartieMultijoueurCompetitive;
import main.java.model.serveur.Serveur;
import main.java.utils.InvalidPortException;
import main.java.utils.NetworkUtils;
import test.java.aserveur.TestServeur;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TestPartieMultijoueurCompetitive {

	private static Client client1, client2;
	private static Joueur joueur1, joueur2;
	private static PartieMultijoueurCompetitive partieMultiCompetitive;
	private static Serveur serveur;
	private static final int TAILLE = 3;
	private final static String ip = NetworkUtils.getServeurIPV4(true);
	private final static int PORT_VALIDE = 8090;

	@BeforeAll
	public static void setUp() throws InvalidPortException, IOException, InterruptedException, ClassNotFoundException {
		joueur1 = new Joueur("Joueur hôte", null, 0);
		client1 = new Client(joueur1);
		joueur2 = new Joueur("Joueur 2", null, 0);
		client2 = new Client(joueur2);
		partieMultiCompetitive = new PartieMultijoueurCompetitive();
		if(TestServeur.serveur == null) {
			TestServeur.serveur = new Serveur();
			TestServeur.serveur.lancerServeur(partieMultiCompetitive, PORT_VALIDE);
		}
		serveur = TestServeur.serveur;
		serveur.setPartie(partieMultiCompetitive);
		//serveur.lancerServeur(partieMultiCompetitive, PORT_VALIDE);
		client1.seConnecter(ip, PORT_VALIDE);
		client2.seConnecter(ip, PORT_VALIDE);
		TimeUnit.SECONDS.sleep(1); // attente de la connexion des joueurs
		
		File fi = new File("src/test/resources/testimg.jpg");
		byte[] img = Files.readAllBytes(fi.toPath());
		partieMultiCompetitive.lancerPartie(img, TAILLE);
	}

	@Test
	@Order(1)
	public void testLancerPartie() throws IOException {
		int nbJoueursAvecPuzzle = partieMultiCompetitive.getTablePuzzleDesJoueurs().size();
		int nbJoueursAvecPuzzleAttendus = 2;

		Assertions.assertEquals(nbJoueursAvecPuzzleAttendus, nbJoueursAvecPuzzle);
	}

	@Test
	@Order(2)
	public void testDeplacerCase() throws IOException {
		// on prépare les données sur lequel le client/joueur 1 va agir
		Case[][] grille = new Case[][] { { new Case(1), new Case(2), new Case(3) },
				{ new Case(4), new Case(-1), new Case(5) }, { new Case(6), new Case(7), new Case(8) } };
		Puzzle puzzleJ1 = partieMultiCompetitive.getTablePuzzleDesJoueurs().get(joueur1);
		puzzleJ1.setGrille(grille);
		int oldX, oldY, newX, newY;
		oldX = puzzleJ1.getXCaseVide();
		oldY = puzzleJ1.getYCaseVide();

		partieMultiCompetitive.deplacerCase(EDeplacement.HAUT, joueur1, 1);
		newX = puzzleJ1.getXCaseVide();
		newY = puzzleJ1.getYCaseVide();

		Assertions.assertEquals(oldX, newX);
		Assertions.assertEquals(oldY + 1, newY);
	}

	@Test
	@Order(3)
	public void testUnJoueurAGagne() {
		Case[][] grille = new Case[][] { { new Case(1), new Case(4), new Case(7) },
				{ new Case(2), new Case(5), new Case(8) }, { new Case(3), new Case(6), new Case(-1) } }; // initialisation
																											// de la
																											// grille gagnante
		Puzzle puzzleJ1 = partieMultiCompetitive.getTablePuzzleDesJoueurs().get(joueur1);
		puzzleJ1.setGrille(grille); //on fait gagner le joueur 1
		
		boolean j1AGagne = partieMultiCompetitive.unJoueurAGagne();
		
		Assertions.assertTrue(j1AGagne);
	}
}
