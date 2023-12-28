package main.java.controleur;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import main.java.model.bdd.SQLUtils;
import main.java.model.bdd.dao.DAOJoueur;
import main.java.model.bdd.dao.beans.JoueurSQL;

public class StatistiquesControleur implements Initializable {

	private Stage owner;
	private ObservableList<JoueurSQL> joueursData;
	private ToggleGroup radioGroupe;

	@FXML
	private TableView<JoueurSQL> tableau;
	@FXML
	private TableColumn<JoueurSQL, String> colonnePhoto;
	@FXML
	private TableColumn<JoueurSQL, String> colonnePseudo;
	@FXML
	private TableColumn<JoueurSQL, Long> colonneVictoires;
	@FXML
	private RadioButton victoiresRadio;
	@FXML
	private RadioButton tempsRadio;
	@FXML
	private TextField inputTaille;

	public StatistiquesControleur(Stage stage) {
		this.owner = stage;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		owner.getIcons().add(new Image(getClass().getResourceAsStream("../../resources/images/logo.jpg")));

		this.radioGroupe = new ToggleGroup();
		tempsRadio.setToggleGroup(radioGroupe);
		victoiresRadio.setToggleGroup(radioGroupe);

		DAOJoueur daoj = new DAOJoueur();
		List<JoueurSQL> jsql = daoj.trouverTout();
		joueursData = FXCollections.observableArrayList(jsql);

		colonnePseudo.setCellValueFactory(new PropertyValueFactory<JoueurSQL, String>("pseudo"));
		colonnePhoto.setCellValueFactory(new PropertyValueFactory<JoueurSQL, String>("urlpp"));
		colonneVictoires.setCellValueFactory(new PropertyValueFactory<JoueurSQL, Long>("id"));

		colonnePseudo.setCellFactory(param -> new TableCell<JoueurSQL, String>() {
			@Override
			protected void updateItem(String item, boolean empty) {
				super.updateItem(item, empty);
				if (item == null || empty) {
					setText(null);
					setGraphic(null);
				} else {
					this.setAlignment(Pos.CENTER);
					setGraphic(new Label(item));
				}
				this.setItem(item);
			}
		});

		colonnePhoto.setCellFactory(param -> new TableCell<JoueurSQL, String>() {
			private ImageView imageView = new ImageView();

			@Override
			protected void updateItem(String item, boolean empty) {
				super.updateItem(item, empty);
				if (item == null || empty) {
					setText(null);
					setGraphic(null);
				} else {
					imageView.setImage(new Image(item));
					imageView.setFitWidth(50);
					imageView.setFitHeight(50);
					this.setAlignment(Pos.CENTER);
					setGraphic(imageView);
				}
				this.setItem(item);
			}
		});

		this.makeLastColumn(false, 0);
	}

	/**
	 * Méthode qui permet d'actualiser la dernière colonne en fonction des
	 * paramètres
	 * 
	 * @param victory : True si affichage des victoires, False si affichage des
	 *                meilleurs temps
	 * @param taille  : taille des taquins dont les données seront prises. si <3,
	 *                elles seront prises de tous.
	 */
	private void makeLastColumn(boolean victory, int taille) {
		colonneVictoires.setText(victory ? "Victoires" : "Meilleur temps");
		colonneVictoires.setCellFactory(param -> new TableCell<JoueurSQL, Long>() {
			@Override
			protected void updateItem(Long item, boolean empty) {
				super.updateItem(item, empty);
				if (item == null || empty) {
					setText(null);
					setGraphic(null);
				} else {
					this.setAlignment(Pos.CENTER);
					String res;
					DAOJoueur daoj = new DAOJoueur();
					JoueurSQL j = daoj.trouver(item);
					if (victory) {
						res = SQLUtils.getNbVictoires(j, taille) + "";
					} else {
						int d = SQLUtils.getTempsPartie(j, taille);
						res = d > 0 ? d + " s" : "Non défini";
					}
					setGraphic(new Label(res));
				}
				this.setItem(item);
			}
		});
		this.tableau.setItems(joueursData);
	}

	@FXML
	private void updateFiltres() {
		try {
			this.makeLastColumn(this.victoiresRadio.isSelected(), Integer.parseInt(this.inputTaille.getText()));
		} catch (NumberFormatException e) {
			this.makeLastColumn(this.victoiresRadio.isSelected(), 0);
		}

	}

}
