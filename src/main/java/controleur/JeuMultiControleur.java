package main.java.controleur;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import main.java.model.Puzzle;
import main.java.model.client.Client;
import main.java.model.joueur.Joueur;

public abstract class JeuMultiControleur extends JeuControleur implements Initializable {

	protected Puzzle puzzle;
	protected List<Joueur> joueurs;

	protected boolean flagThreadEnd = false;
	protected Joueur joueur;

	protected Client client;

	@FXML
	protected Label aVotreTour;
	
	@FXML
	protected VBox boxJoueurs;

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		owner.getIcons().add(new Image(getClass().getResourceAsStream("../../resources/images/logo.jpg")));
		
		//Initialisation des informations de la fenêtre
		this.updateImages();
		this.initJoueur();
		this.updateJoueurs();

		//Lancement du thread d'intéraction avec le serveur.
		this.lancerThread();
	}

	/**
	 * mise a jour des images affichées en fonction de la position des cases dans la
	 * grille.
	 */
	@Override
	protected void updateImages() {
		// Définition de la taille d'une case
		double largeurCase = owner.getWidth() / this.puzzle.getTaille() * 0.5;
		Image image;
		grille.getChildren().clear();

		for (int i = 0; i < puzzle.getTaille(); i++) {
			for (int j = 0; j < puzzle.getTaille(); j++) {
				Label l = new Label();
				if (puzzle.getCase(j, i).getIndex() != -1)
					l.setText("" + ((int) puzzle.getCase(j, i).getIndex() + 1));
				l.setFont(new Font(18));
				l.setTextFill(Color.YELLOW);
				l.setPrefWidth(largeurCase);
				l.setPrefHeight(largeurCase);
				l.setAlignment(Pos.CENTER);
				l.setLayoutX(j * largeurCase);
				l.setLayoutY(i * largeurCase);

				l.setId("case" + puzzle.getCase(j, i).getIndex());

				image = new Image(new ByteArrayInputStream(puzzle.getCase(j, i).getImage()));

				Background bgi = new Background(
						new BackgroundImage(image, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
								BackgroundPosition.DEFAULT, new BackgroundSize(100, 100, true, true, true, false)));
				if (puzzle.getCase(j, i).getIndex() != -1)
					l.setBackground(bgi);
				grille.getChildren().add(l);
			}
		}
	}

	/**
	 * Cette méthode met a jour l'affichage des joueurs à la droite de l'écran.
	 */
	protected void updateJoueurs() {
		//Clear l'affichage présent
		this.boxJoueurs.getChildren().clear();
		//Parcours des joueurs : Création de chaque VBox contenant Label(pseudo) et Image du joueur
		for (Joueur j : joueurs) {
			VBox v = new VBox(); // Box dans laquelle on affichera les infos des joueurs
			v.setAlignment(Pos.CENTER);
			v.setPrefHeight(200);
			v.setPrefWidth(100);
			ImageView i = new ImageView(); // Logo du joueur
			i.setFitHeight(60);
			i.setFitWidth(60);
			Image image = new Image(new ByteArrayInputStream(this.joueur.getImage()));
			i.setImage(image);
			Label l = new Label(j.getNom()); // Pseudo du joueur
			v.setId("box" + j.getNom());
			v.getChildren().add(i);
			v.getChildren().add(l);
			//Ajout de la VBox a la fenetre.
			boxJoueurs.getChildren().add(v); // Ajout a la box principal
		}
	}

	@Override
	protected void updateJeu() {
		this.updateImages();
		if (this.puzzle.verifierGrille())
			this.updateVictoire();
	}

	@Override
	protected void updateVictoire() {
		victoireLabel.setVisible(true);
	}

	@Override
	protected void initJoueur() {
		Image image = new Image(new ByteArrayInputStream(this.joueur.getImage()));
		this.logoJoueur.setImage(image);
		this.pseudoJoueur.setText(joueur.getNom());
		this.updateInfos();
	}

	@Override
	protected void updateInfos() {
		this.nbCoups.setText("Nombre de coups : " + this.puzzle.getNbCoups());
	}

	@Override
	public void setKeyController() {
		this.owner.getScene().setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				// dpAnim(event.getCode());
				try {
					switch (event.getCode()) {
					case Z,UP:
						client.lancerRequete("h");
						break;
					case S,DOWN:
						client.lancerRequete("b");
						break;
					case Q,LEFT:
						client.lancerRequete("g");
						break;
					case D,RIGHT:
						client.lancerRequete("d");
						break;
					default:
						break;
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}
	
	/**
	 * Cette méthode permet de demander les infos au serveur et de les lire.
	 * @throws IOException
	 * @throws InterruptedException
	 */
	protected abstract void readStream() throws IOException, InterruptedException;

	protected void lancerThread() {
		new Thread(() -> {
			try {
				readStream();
			} catch (InterruptedException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}).start();

	}

}
