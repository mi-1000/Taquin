package main.java.controleur;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import main.java.model.bdd.dao.DAOJoueur;
import main.java.model.bdd.dao.beans.JoueurSQL;
import main.java.model.client.Client;
import main.java.model.joueur.Joueur;
import main.java.utils.HostedImage;
import main.java.vue.VueGenerale;

public class RecherchePartieControleur implements Initializable {

	private Stage owner;
	private JoueurSQL joueurChoisi;

	@FXML
	private TextField saisieIP;

	@FXML
	private TextField saisiePort;

	@FXML
	private MenuButton menuProfils;

	@FXML
	private Button boutonConnexion;

	public RecherchePartieControleur(Stage stage) {
		this.owner = stage;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		owner.getIcons().add(new Image(getClass().getResourceAsStream("../../resources/images/logo.jpg")));

		try {
			this.updateListeProfils();
		} catch (IOException e) {
			e.printStackTrace();
		}
		boutonConnexion.setDisable(true);

		this.saisiePort.textProperty().addListener((observable, oldValue, newValue) -> {
			this.updateEtatBouton();
		});

		this.saisieIP.textProperty().addListener((observable, oldValue, newValue) -> {
			this.updateEtatBouton();
		});
	}

	private void updateEtatBouton() {
		try {
			Integer.parseInt(saisiePort.getText());
			if (saisieIP.getText().equals("") || this.joueurChoisi == null) {
				boutonConnexion.setDisable(true);
			} else {
				boutonConnexion.setDisable(false);
			}
		} catch (NumberFormatException e) {
			boutonConnexion.setDisable(true);
		}
	}

	private void updateListeProfils() throws IOException {
		List<JoueurSQL> joueurs = new ArrayList<JoueurSQL>();
		DAOJoueur j = new DAOJoueur();
		joueurs = j.trouverTout();
		menuProfils.getItems().clear();
		if (!joueurs.isEmpty()) {
			for (JoueurSQL jactuel : joueurs) {
				MenuItem item = new MenuItem(jactuel.getPseudo());
				item.setOnAction(value -> {
					joueurChoisi = jactuel;
					this.menuProfils.setText(jactuel.getPseudo());
					this.updateEtatBouton();
				});
				menuProfils.getItems().add(item);
			}
		}
	}

	@FXML
	private void connexion(ActionEvent event) throws IOException, NumberFormatException, ClassNotFoundException {
		if (joueurChoisi != null) {
			Joueur j = new Joueur(joueurChoisi.getPseudo(), HostedImage.getImageBytes(joueurChoisi.getUrlpp()),
					joueurChoisi.getId());
			Client c = new Client(j);
			c.seConnecter(saisieIP.getText(), Integer.parseInt(saisiePort.getText()));
			((VueGenerale) this.owner).changerVue("Lobby", "src/main/resources/ui/fxml/Lobby.fxml",
					new LobbyControleur(this.owner, j, c));

		}
	}

}
