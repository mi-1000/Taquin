package test.java.model;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;

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
import main.java.model.partie.PartieMultijoueurCooperative;
import main.java.model.serveur.Serveur;
import main.java.utils.InvalidPortException;
import main.java.utils.NetworkUtils;
import test.java.aserveur.TestServeur;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TestPartieMultijoueurCooperative {

	private static Client client1, client2;
	private static Joueur joueur1, joueur2;
	private static PartieMultijoueurCooperative partieMultiCoop;
	private static Serveur serveur;
	private final static int TAILLE = 3;
	private final static String ip = NetworkUtils.getServeurIPV4(true);
	private final static int PORT_VALIDE = 8090;

	@BeforeAll
	public static void setUp() throws InvalidPortException, IOException, InterruptedException, ClassNotFoundException {
		joueur1 = new Joueur("Joueur hôte", null, 0);
		client1 = new Client(joueur1);
		joueur2 = new Joueur("Joueur 2", null, 0);
		client2 = new Client(joueur2);
		partieMultiCoop = new PartieMultijoueurCooperative();
		if(TestServeur.serveur == null) {
			TestServeur.serveur = new Serveur();
			TestServeur.serveur.lancerServeur(partieMultiCoop, PORT_VALIDE);
		}
		serveur = TestServeur.serveur;
		serveur.setPartie(partieMultiCoop);
//		serveur.lancerServeur(partieMultiCoop, PORT_VALIDE);
		TestServeur.serveur.setPartie(partieMultiCoop);
		client1.seConnecter(ip, PORT_VALIDE);
		client2.seConnecter(ip, PORT_VALIDE);
		TimeUnit.SECONDS.sleep(1); // attente de la connexion des joueurs
		File fi = new File("src/test/resources/testimg.jpg");
		byte[] img = Files.readAllBytes(fi.toPath());
		partieMultiCoop.lancerPartie(img, TAILLE);
	}

	@Test
	@Order(1)
	public void testPasserAuJoueurSuivant() throws IOException {
		// on met une grille où on sait qu'on peut déplacer vers le haut pour pouvoir
		// passer le tour du joueur
		int indexCourant = partieMultiCoop.getIndexJoueurCourant();
		Case[][] grille = new Case[][] { { new Case(1), new Case(2), new Case(3) },
				{ new Case(4), new Case(-1), new Case(5) }, { new Case(6), new Case(7), new Case(8) } };
		Puzzle puzzle = partieMultiCoop.getPuzzleCommun();
		puzzle.setGrille(grille);

		int indexAttendu = indexCourant + 1;
		partieMultiCoop.deplacerCase(EDeplacement.HAUT, joueur1, 1);
		
		int indexReel = partieMultiCoop.getIndexJoueurCourant();
		
		Assertions.assertEquals(indexAttendu, indexReel,"Normalement après le joueur "+indexAttendu +" c'est le joueur "+indexReel + " qui doit jouer");
	
	
		//on recommence pour le deuxieme joueur
		indexCourant = partieMultiCoop.getIndexJoueurCourant();
		indexAttendu = 0;
		partieMultiCoop.deplacerCase(EDeplacement.BAS, joueur2, 2);
		
		indexReel = partieMultiCoop.getIndexJoueurCourant();
		Assertions.assertEquals(indexAttendu, indexReel,"Normalement après le joueur "+indexAttendu +" c'est le joueur "+indexReel + " qui doit jouer");
	}
	
	@Test
	@Order(2)
	public void testPartieFinie() {
		Case[][] grille = new Case[][] { { new Case(1), new Case(4), new Case(7) },
			{ new Case(2), new Case(5), new Case(8) }, { new Case(3), new Case(6), new Case(-1) } }; //initialisation de la grille gagnante
			
		partieMultiCoop.getPuzzleCommun().setGrille(grille);
		boolean estFinie = partieMultiCoop.partieFinie();
		
		Assertions.assertTrue(estFinie);
	}
}
