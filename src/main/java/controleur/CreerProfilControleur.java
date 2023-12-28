package main.java.controleur;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javax.imageio.ImageIO;

import org.imgscalr.Scalr;

import javafx.embed.swing.SwingFXUtils;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import main.java.model.bdd.dao.DAOJoueur;
import main.java.model.bdd.dao.beans.JoueurSQL;
import main.java.utils.HostedImage;

public class CreerProfilControleur implements Initializable {

	private Stage owner;
	private BufferedImage image;
	private String extension;

	@FXML
	private ImageView imageJoueur;

	@FXML
	private TextField saisiePseudo;
	
	@FXML
	private Button creerBouton;

	public CreerProfilControleur(Stage stage) throws IOException {
		this.owner = stage;
		this.image = ImageIO.read(new File("src/main/resources/images/defaulticon.png"));
	}

	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		owner.getIcons().add(new Image(getClass().getResourceAsStream("../../resources/images/logo.jpg")));
		this.creerBouton.setDisable(true);
		
		this.updateImage();
		this.addImageAction();
		
		saisiePseudo.textProperty().addListener((observable, oldValue, newValue) -> {
		    if(newValue.equals("")) {
		    	creerBouton.setDisable(true);
		    }else {
		    	creerBouton.setDisable(false);
		    }
		});
	}
	
	/**
	 * Met à jour l'affichage de l'image sélectionnée
	 */
	private void updateImage() {
		image = Scalr.resize(image, Scalr.Mode.FIT_EXACT, 250, 250);
		this.imageJoueur.setImage(SwingFXUtils.toFXImage(this.image, null));
	}

	/**
	 * Permet de sélectionner une image provenant de son ordinateur.
	 */
	private void addImageAction() {
		//Ajout d'un écouteur de clic sur l'image.
		this.imageJoueur.addEventHandler(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				//Affichage d'une fenêtre de choix de fichiers
				FileChooser fileChooser = new FileChooser();
				fileChooser.getExtensionFilters()
						.add(new ExtensionFilter("Images (*.jpg, *.jpeg, *.png)", "*.jpg", "*.jpeg", "*.png"));
				fileChooser.setTitle("Sélectionner une image");
				File file = fileChooser.showOpenDialog(owner);
				if (file != null) {
					try {
						//On resize l'image pour être sur de traîter systématiquement les mêmes tailles.
						image = Scalr.resize(ImageIO.read(file), Scalr.Mode.FIT_EXACT, 1000, 1000);
						extension = file.getName().substring(file.getName().lastIndexOf(".") + 1,
								file.getName().length());
						if (extension.equals("jpeg"))
							extension = "jpg";
					} catch (Exception e) {
						e.printStackTrace();
					}
					updateImage();
				}
			}
		});
	}

	
	@FXML
	public void creerProfilBouton() {
		//Si les informations ont été remplies
		if (this.image != null && !this.saisiePseudo.getText().equals("")) {
			//Création d'un nouveau joueur à l'aide de la DAO.
			DAOJoueur dao = new DAOJoueur();
			JoueurSQL joueur = new JoueurSQL();
			joueur.setPseudo(this.saisiePseudo.getText());
			joueur.setUrlpp(HostedImage.uploadImage(image, extension));
			dao.creer(joueur);
			this.owner.close();
		}
	}

}
