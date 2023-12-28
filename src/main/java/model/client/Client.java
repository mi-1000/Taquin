package main.java.model.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.net.Socket;

import main.java.model.joueur.Joueur;
import main.java.model.partie.PartieMultijoueur;

public class Client {

	private boolean estConnecte;
	private Socket socket;
	private Joueur joueur;
	private int noClient;
	private ObjectOutputStream oos;

	public Client(Joueur joueur) throws IOException {
		this.joueur = joueur;
		this.estConnecte = false;
	}
	
	public int getNoClient() {
		return this.noClient;
	}
	
	public void setNoClient(int noClient) {
		this.noClient = noClient;
	}

	private void setConnection() {
		estConnecte = true;
	}

	public boolean getEstConnecte() {
		return estConnecte;
	}

	public void seConnecter(String ip, int port) throws IOException, ClassNotFoundException {
		socket = new Socket(ip, port);
		setConnection();
		oos = new ObjectOutputStream(socket.getOutputStream());
		this.ajouterJoueur();
	}

	public Socket getSocket() {
		return socket;
	}

	public void lancerRequete(Object content) throws IOException {
		oos.writeObject(content); // envoi de la requete au serveur
		oos.flush();
	}

	private void ajouterJoueur() throws IOException, ClassNotFoundException {
		this.lancerRequete(joueur);
		ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
		setNoClient((int) ois.readObject());
	}
	
}
