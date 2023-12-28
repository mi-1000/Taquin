package main.java.vue;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Paths;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import main.java.controleur.MenuControleur;
import main.java.controleur.NouvellePartieControleur;

public class VueGenerale extends Stage {

	public static String theme = "espace";

	public VueGenerale(Stage primary) throws IOException {
		this.initModality(Modality.NONE);
	}

	public void changerVue(String titre, String fxmlPath, Initializable controller) throws IOException {
		FXMLLoader loader = new FXMLLoader(Paths.get(fxmlPath).toUri().toURL());
		loader.setController(controller);
		Parent root = loader.load();
		Scene scene = new Scene(root);
		this.setScene(scene);
		this.setTitle("Taquin - " + titre);
		this.updateStyle();
		this.setResizable(false);
		this.show();
	}
	
	public void updateStyle() throws MalformedURLException {
		getScene().getStylesheets().clear();
		String cssPath = "src/main/java/vue/css/"+ theme + "/" + getTitle().split("\\-")[1].trim() + ".css";
		getScene().getStylesheets().add(Paths.get(cssPath).toUri().toURL().toString());
	}

}
