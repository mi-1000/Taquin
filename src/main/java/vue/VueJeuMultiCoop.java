package main.java.vue;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import main.java.controleur.JeuMultiCoopControleur;
import main.java.model.Puzzle;
import main.java.model.client.Client;
import main.java.model.joueur.Joueur;

public class VueJeuMultiCoop extends Stage{

	public VueJeuMultiCoop(int numJoueur, Joueur joueur,
			List<Joueur> joueurs, int numJoueurCourant, Puzzle puzzle, Client client) throws IOException {
		this.initModality(Modality.NONE);
		FXMLLoader loader = new FXMLLoader(Paths.get("src/main/resources/ui/fxml/JeuMultiCoop.fxml").toUri().toURL());
		this.setWidth(Screen.getPrimary().getBounds().getWidth()/2);
        this.setHeight(Screen.getPrimary().getBounds().getHeight()/2);
        this.setResizable(false);
        JeuMultiCoopControleur controller = new JeuMultiCoopControleur(this, numJoueur, joueur,
        		joueurs, numJoueurCourant, puzzle, client);
        loader.setController(controller);
        
        this.setTitle("Taquin - Jeu Multijoueur Coopératif");
        Parent root = loader.load();
        Scene scene = new Scene(root);        
        scene.getStylesheets().clear();
		String cssPath = "src/main/java/vue/css/"+ VueGenerale.theme + "/jeu multijoueur.css";
		scene.getStylesheets().add(Paths.get(cssPath).toUri().toURL().toString());
        
        this.setScene(scene);
        controller.setKeyController();
        this.show();
	}
	
}
