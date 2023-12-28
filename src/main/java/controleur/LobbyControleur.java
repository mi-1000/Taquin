package main.java.controleur;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import main.java.model.Puzzle;
import main.java.model.client.Client;
import main.java.model.joueur.Joueur;
import main.java.model.partie.PartieMultijoueur;
import main.java.vue.VueJeuMultiCompet;
import main.java.vue.VueJeuMultiCoop;

public class LobbyControleur implements Initializable {

	private Joueur joueur;
	private boolean estHote;
	private boolean estCoop;
	private Stage owner;
	private byte[] img;
	private int taille;
	private Client client;
	private PartieMultijoueur partie;
	private boolean flagLancement = false;
	private boolean flagThreadEnd = false;

	private List<Joueur> joueurs = new ArrayList<>();

	@FXML
	private Button lancerPartie;

	@FXML
	private ImageView imagePuzzle;

	@FXML
	private HBox boxJoueurs;

	@FXML
	private Label labelTaille;

	@FXML
	private Label labelType;

	public LobbyControleur(Stage stage, Joueur j, Client client) {
		this.owner = stage;
		this.estHote = false;
		this.client = client;
		this.joueur = j;
	}

	public LobbyControleur(Stage stage, PartieMultijoueur partie, Joueur j, boolean estCoop, byte[] img, int taille,
			Client client) {
		this(stage, j, client);
		this.estHote = true;
		this.estCoop = estCoop;
		this.img = img;
		this.taille = taille;
		this.partie = partie;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		owner.getIcons().add(new Image(getClass().getResourceAsStream("../../resources/images/logo.jpg")));
		
		//Affichage du bouton de lancement de partie seulement pour l'hôte.
		lancerPartie.setManaged(estHote);
		this.updateInfos();
		this.lancerThread();
	}

	/**
	 * Met à jour l'affichage des joueurs
	 */
	private void updateJoueurs() {
		this.boxJoueurs.getChildren().clear();
		for (Joueur j : joueurs) {
			VBox v = new VBox(); // Box dans laquelle on affichera les infos des joueurs
			v.setAlignment(Pos.CENTER);
			v.setPrefHeight(200);
			v.setPrefWidth(100);
			v.setSpacing(5);
			ImageView i = new ImageView(); // Logo du joueur
			i.setFitHeight(60);
			i.setFitWidth(60);
			Image image = new Image(new ByteArrayInputStream(j.getImage()));
			i.setImage(image);
			Label l = new Label(j.getNom()); // Pseudo du joueur
			v.setId("box" + j.getNom());
			v.getChildren().add(i);
			v.getChildren().add(l);
			boxJoueurs.getChildren().add(v); // Ajout a la box principal
		}
	}

	/**
	 * Met à jour les informations affichées
	 */
	private void updateInfos() {
		if (img != null && taille > 2) {
			this.imagePuzzle.setImage(new Image(new ByteArrayInputStream(this.img)));
			this.labelTaille.setText("Taille : " + this.taille);
			this.labelType.setText("Partie " + (estCoop ? "coopérative" : "compétitive"));
		}
	}

	@FXML
	private void lancerPartieMulti() throws IOException {
		if (this.joueurs.size() > 1) {
			partie.lancerPartie(this.img, taille);
			this.flagLancement = true;
		}
	}

	/**
	 * Permet de demander et lire les informations du serveur
	 * @throws IOException
	 * @throws ClassNotFoundException
	 * @throws InterruptedException
	 */
	private void readStream() throws IOException, ClassNotFoundException, InterruptedException {

		if (!this.estHote)
			this.client.lancerRequete("i");

		while (!flagThreadEnd) {
			Platform.runLater(() -> {
				List<Joueur> j;

				try {
					ObjectInputStream ois;
					ois = new ObjectInputStream(client.getSocket().getInputStream());

					Object oisObj = ois.readObject();

					if (oisObj instanceof List) {

						List<Object> tab = (List<Object>) oisObj;


						if (tab.get(0).equals("i") && !estHote) {

							// 0: param, 1: joueurs, 2: image, 3: taille, 4: estCoop
							
							this.estCoop = (boolean) tab.get(4);
							this.taille = (int) tab.get(3);
							this.img = (byte[]) tab.get(2);
							this.updateInfos();
							client.lancerRequete("l");

						} else if (tab.get(0).equals("s") && this.estCoop) {
							int indexJ = (int) tab.get(2);
							Puzzle puzzleC = (Puzzle) tab.get(3);
							VueJeuMultiCoop vj = new VueJeuMultiCoop(client.getNoClient(), joueur, this.joueurs, indexJ,
									puzzleC, this.client);
							flagThreadEnd = true;
							this.owner.close();

						} else if (tab.get(0).equals("s") && !this.estCoop) {
							Puzzle puzzleI = (Puzzle) tab.get(2);
							VueJeuMultiCompet vj = new VueJeuMultiCompet(joueur, this.joueurs, puzzleI, this.client);
							flagThreadEnd = true;
							this.owner.close();

						} else if (flagLancement)
							client.lancerRequete("s");
						else
							client.lancerRequete("l");

						if (tab.get(1) instanceof List) {

							j = (List<Joueur>) tab.get(1);
							this.joueurs = new ArrayList<>(j);
							this.updateJoueurs();

						}
					}
				} catch (IOException | ClassNotFoundException e) {
					e.printStackTrace();
				}

			});

			Thread.sleep(500);
		}

	}

	private void initStream() throws IOException {
		if (estHote) {
			List<Object> output = new ArrayList<Object>();
			output.add("i");
			output.add(this.img);
			output.add(this.taille);
			client.lancerRequete(output);
		} else {
			client.lancerRequete("i");
		}
	}

	private void lancerThread() {
		new Thread(() -> {
			try {
				readStream();
			} catch (InterruptedException | ClassNotFoundException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}).start();
		try {
			this.initStream();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
