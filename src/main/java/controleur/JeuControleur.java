package main.java.controleur;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public abstract class JeuControleur implements Initializable {

	protected Stage owner;

	@FXML
	protected Label chrono;
	@FXML
	protected Label victoireLabel;
	@FXML
	protected Label nbCoups;

	@FXML
	protected ImageView logoJoueur;
	@FXML
	protected Label pseudoJoueur;

	@FXML
	protected AnchorPane grille;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
	}

	/**
	 * initialise les informations du joueur?
	 */
	protected abstract void initJoueur();

	/**
	 * met a jour l'affichage des images du puzzle.
	 */
	protected abstract void updateImages();

	/**
	 * met à jour les informations de jeu(images, victoire)
	 */
	protected abstract void updateJeu();

	/**
	 * met à jour le nombre de coups et le chronomètre (si partie solo)
	 */
	protected abstract void updateInfos();

	/**
	 * met à jour l'affichage lorsqu'il y a victoire
	 */
	protected abstract void updateVictoire();

	/**
	 * met toute la fenêtre à jour.
	 */
	protected void updateAll() {
		updateJeu();
		updateInfos();
	}

	protected abstract void setKeyController();

}
