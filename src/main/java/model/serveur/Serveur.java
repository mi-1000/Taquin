package main.java.model.serveur;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

import main.java.model.partie.PartieMultijoueur;
import main.java.utils.InvalidPortException;
import main.java.utils.NetworkUtils;

public class Serveur{

	private boolean serverOn;
	private int noConnexion;
	private ServerSocket serverSocket;
	private PartieMultijoueur partie;

	public Serveur() {
		serverOn = false;
		noConnexion = 0;
	}

	public void lancerServeur(PartieMultijoueur partie, int port) throws InvalidPortException {
		NetworkUtils.checkPort(port);
		serverOn = true;
		this.partie = partie;
		new Thread(() -> {
			System.out.println("Lancement du serveur...");
			try {
				ThreadGroup groupe = new ThreadGroup("socketsClients"); // on fait un groupe de thread pour gérer les
																		// multiples connexion au serveur
				serverSocket = new ServerSocket(port); // on fait un serveur socket sur le port pour les
													// connexions qu'on transforme en socket
				noConnexion = 0;

				// on accepte les connexion sur la server socket et on incrémente le nombre de
				// connexions, à chaque connexion on démarre un serveurthread à l'aide du thread
				// group
				while (serverOn) {
					try {
						Socket clientSocket = serverSocket.accept();
						noConnexion++;
						ServeurThread st = new ServeurThread(clientSocket, groupe, noConnexion, this);
						st.start();
					} catch (SocketException se) {
						// this.serverOn = false;
					}

				}
			} catch (NumberFormatException | IOException e) {
				e.printStackTrace();
			}
		}).start();
	}

	public int getNoConnexion() {
		return this.noConnexion;
	}
	
	public PartieMultijoueur getPartie() {
		return partie;
	}

	public void setPartie(PartieMultijoueur partie) {
		this.partie = partie;
		this.noConnexion = 0;
	}

}
