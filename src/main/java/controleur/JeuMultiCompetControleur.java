package main.java.controleur;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import main.java.model.Puzzle;
import main.java.model.client.Client;
import main.java.model.joueur.Joueur;
import main.java.vue.VueJeuMultiCoop;

public class JeuMultiCompetControleur extends JeuMultiControleur implements Initializable {

	/**
	 * 
	 * @param stage  : fenêtre dans laquelle la scene est affichée
	 * @param partie : partie jouée
	 * @throws IOException : Exception lors d'un problème de lecture de l'image
	 */
	public JeuMultiCompetControleur(Stage stage, Joueur joueur, List<Joueur> joueurs, Puzzle puzzle, Client client)
			throws IOException {
		this.owner = stage;
		this.joueur = joueur;
		this.client = client;
		this.puzzle = puzzle;
		this.joueurs = joueurs;

	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		super.initialize(location, resources);
		this.aVotreTour.setVisible(false);
		this.aVotreTour.setManaged(false);
	}

	@Override
	protected void readStream() throws IOException, InterruptedException {

		this.client.lancerRequete("p");

		while (!this.puzzle.verifierGrille()) {
			Platform.runLater(() -> {

				try {
					ObjectInputStream ois;
					ois = new ObjectInputStream(client.getSocket().getInputStream());
					Object oisObj = ois.readObject();

					if (oisObj instanceof List) {
						List<Object> tab = (List<Object>) oisObj;
						if (tab.get(0).equals("p")) {
							this.puzzle = (Puzzle) tab.get(1);
							updateAll();
						}
						client.lancerRequete("p");
					}

				} catch (IOException | ClassNotFoundException e) {
					e.printStackTrace();
				}
			});
			Thread.sleep(500);
		}
	}

	@Override
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
