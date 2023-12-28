package main.java.controleur;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import main.java.model.bdd.dao.DAOJoueur;
import main.java.model.bdd.dao.beans.JoueurSQL;
import main.java.model.client.Client;
import main.java.model.joueur.Joueur;
import main.java.model.partie.ContextePartie;
import main.java.model.partie.PartieMultijoueur;
import main.java.model.partie.PartieMultijoueurCompetitive;
import main.java.model.partie.PartieMultijoueurCooperative;
import main.java.model.partie.PartieSolo;
import main.java.model.serveur.Serveur;
import main.java.utils.HostedImage;
import main.java.utils.InvalidPortException;
import main.java.utils.NetworkUtils;
import main.java.utils.Utils;
import main.java.vue.VueGenerale;
import main.java.vue.VueJeuSolo;

public class NouvellePartieControleur implements Initializable {

	private Stage owner;
	private JoueurSQL joueurChoisi;
	private Image imageChoisie;
	private ToggleGroup radioGroupe;

	@FXML
	private RadioButton soloRadio;
	@FXML
	private RadioButton multiCoopRadio;
	@FXML
	private RadioButton multiCompetRadio;
	@FXML
	private TextField saisiePort;
	@FXML
	private Button lancerBouton;

	@FXML
	private MenuButton menuProfils;

	@FXML
	private ImageView imageJoueur;
	@FXML
	private Label pseudoJoueur;

	@FXML
	private TextField saisieTaille;

	@FXML
	private ImageView imagePerso;

	public NouvellePartieControleur(Stage stage) throws IOException {
		this.owner = stage;
		this.imageChoisie = new Image(new File("src/test/resources/testimg.jpg").toURI().toURL().toString());
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		owner.getIcons().add(new Image(getClass().getResourceAsStream("../../resources/images/logo.jpg")));

		this.menuProfils.addEventHandler(MouseEvent.MOUSE_PRESSED, (MouseEvent event) -> this.handleMenuClick(event));

		this.radioGroupe = new ToggleGroup();

		EventHandler<? super MouseEvent> l = (MouseEvent event) -> this.handleRadioClick(event);

		this.multiCompetRadio.setToggleGroup(radioGroupe);
		this.multiCompetRadio.addEventHandler(MouseEvent.MOUSE_PRESSED, l);

		this.multiCoopRadio.setToggleGroup(radioGroupe);
		this.multiCoopRadio.addEventHandler(MouseEvent.MOUSE_PRESSED, l);

		this.soloRadio.setToggleGroup(radioGroupe);
		this.soloRadio.addEventHandler(MouseEvent.MOUSE_PRESSED, l);

		this.saisiePort.textProperty().addListener((observable, oldValue, newValue) -> {
			this.updateEtatBoutonLancer();
		});
		this.saisieTaille.textProperty().addListener((observable, oldValue, newValue) -> {
			this.updateEtatBoutonLancer();
		});

		this.updateImagePartie();
		this.updateEtatBoutonLancer();
	}

	private void updateInfosJoueur() throws IOException {
		imageJoueur.setImage(HostedImage.getImage(joueurChoisi.getUrlpp()));
		pseudoJoueur.setText(this.joueurChoisi.getPseudo());
	}

	private void updateJoueurs() throws IOException {
		List<JoueurSQL> joueurs = new ArrayList<JoueurSQL>();
		DAOJoueur j = new DAOJoueur();
		joueurs = j.trouverTout();
		menuProfils.getItems().clear();
		if (!joueurs.isEmpty()) {
			for (JoueurSQL jactuel : joueurs) {
				MenuItem item = new MenuItem(jactuel.getPseudo());
				item.setOnAction(value -> {
					try {
						this.setJoueurChoisi(jactuel);
						this.menuProfils.setText(jactuel.getPseudo());
						this.updateEtatBoutonLancer();
					} catch (IOException e) {
						e.printStackTrace();
					}
				});
				menuProfils.getItems().add(item);
			}
		}
	}

	private void updateImagePartie() {
		imagePerso.setImage(this.imageChoisie);
	}

	private void setJoueurChoisi(JoueurSQL j) throws IOException {
		this.joueurChoisi = j;
		this.updateInfosJoueur();
	}

	@FXML
	private void changerImageBouton(ActionEvent event) throws IOException {
		FileChooser fileChooser = new FileChooser();
		fileChooser.getExtensionFilters()
				.add(new ExtensionFilter("Images (*.jpg, *.jpeg, *.png)", "*.jpg", "*.jpeg", "*.png"));
		fileChooser.setTitle("Open Resource File");
		File file = fileChooser.showOpenDialog(this.owner);
		if (file != null) {
			imageChoisie = new Image(file.toURI().toString(), 500, 500, false, false);
			this.updateImagePartie();
		}
	}

	@FXML
	private void creerProfilBouton(ActionEvent event) throws IOException {
		VueGenerale vm = new VueGenerale(this.owner);
		vm.changerVue("Creation profil", "src/main/resources/ui/fxml/CreationProfil.fxml",
				new CreerProfilControleur(vm));
	}

	private void handleMenuClick(MouseEvent event) {
		try {
			this.updateJoueurs();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void handleRadioClick(MouseEvent event) {
		if (event.getSource().equals(this.multiCoopRadio) || event.getSource().equals(this.multiCompetRadio)) {
			saisiePort.setManaged(true);
			saisiePort.setVisible(true);
		} else {
			saisiePort.setManaged(false);
			saisiePort.setVisible(false);
		}
		this.updateEtatBoutonLancer();
	}

	private void updateEtatBoutonLancer() {
		if (this.radioGroupe.getSelectedToggle() == null || this.saisieTaille.getText().equals("")
				|| this.joueurChoisi == null || (saisiePort.isVisible() && saisiePort.getText().equals(""))) {
			this.lancerBouton.setDisable(true);
		} else {
			this.lancerBouton.setDisable(false);
		}
	}

	private void erreurLancement() {
		System.out.println("erreurlancement");
		// TODO
	}

	@FXML
	private void lancerPartie(ActionEvent event) throws IOException, ClassNotFoundException {
		if (joueurChoisi != null) {
			try {
				byte[] img = HostedImage.getImageBytes(joueurChoisi.getUrlpp());
				Joueur j = new Joueur(joueurChoisi.getPseudo(), img, joueurChoisi.getId());
				int taille = Integer.parseInt(this.saisieTaille.getText());
				if (soloRadio.isSelected()) {
					PartieSolo p = new PartieSolo(j);
					p.lancerPartie(Utils.imageToByteArray(imageChoisie, null), taille);
					new VueJeuSolo(p);
				} else if (multiCoopRadio.isSelected() || multiCompetRadio.isSelected()) {
					try {
						boolean estCoop = multiCoopRadio.isSelected();
						VueGenerale vg = new VueGenerale(this.owner);

						ContextePartie cp = new ContextePartie(j);

						PartieMultijoueur p;

						if (estCoop)
							p = new PartieMultijoueurCooperative();
						else
							p = new PartieMultijoueurCompetitive();

						cp.setStrategy(p);

						Serveur s = new Serveur();
						s.lancerServeur(p, Integer.parseInt(this.saisiePort.getText()));

						Client c = new Client(j);
						c.seConnecter(NetworkUtils.getServeurIPV4(true), Integer.parseInt(this.saisiePort.getText()));

						LobbyControleur lc = new LobbyControleur(vg, p, j, estCoop,
								Utils.imageToByteArray(imageChoisie, null), taille, c);

						vg.changerVue("Lobby", "src/main/resources/ui/fxml/Lobby.fxml", lc);
					} catch (InvalidPortException e) {
						// TODO
						e.printStackTrace();
					}
				}
			} catch (NumberFormatException e) {
				this.erreurLancement();
			}
		}
	}

	@FXML
	private void retourBouton(ActionEvent event) throws IOException {
		((VueGenerale) this.owner).changerVue("Menu", "src/main/resources/ui/fxml/MenuPrincipal.fxml",
				new MenuControleur(this.owner));
	}

}
