package main.java.model.partie;

import java.io.IOException;

import javafx.scene.image.Image;

public interface StrategyPartie {

	public void lancerPartie(byte[] image, int taillePuzzle) throws IOException;

}
