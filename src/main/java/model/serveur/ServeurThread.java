package main.java.model.serveur;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

import main.java.model.EDeplacement;
import main.java.model.joueur.Joueur;

public class ServeurThread extends Thread {

	private Socket socket;
	private int noConnexion; // numero du client distant
	private ObjectInputStream inputStream;
	private boolean flagJoueurAjoute = false;
	private Joueur joueur;
	private Serveur serveur;

	/**
	 * Suppose socket dejà connectée vers le client num noConnexion
	 * 
	 * @param noConnexion : num du client
	 */
	public ServeurThread(Socket socket, ThreadGroup groupe, int noConnexion, Serveur serveur) throws IOException {
		super(groupe, "ReceveurEnvoyeur");
		this.socket = socket;
		this.noConnexion = noConnexion;
		this.serveur = serveur;
		inputStream = new ObjectInputStream(socket.getInputStream());
	}

	@Override
	public void run() {
		String ligne = null;
		List<Object> liste = null;
		String reponse;
		try {
			while (!isInterrupted()) {
				if (!flagJoueurAjoute) {
					this.joueur = (Joueur) inputStream.readObject();
					serveur.getPartie().ajouterJoueur(joueur, socket);
					ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
					oos.writeObject(noConnexion);
					flagJoueurAjoute = true;
				} else {
					Object input = inputStream.readObject();
					
					if (input instanceof String)
						ligne = (String) input;
					else if (input instanceof List) {
						liste = (List<Object>) input;
						if(liste.get(0) instanceof String)
							ligne = (String) liste.get(0);
					}

					if (ligne != null) {
						System.out.println("Le client numéro " + this.noConnexion + " a envoye : "+ligne); // echo de la question sur la console
						reponse = ligne; // calcul de la reponse
						
						char c = reponse.charAt(0);
						switch (c) {
						case 'h':
							serveur.getPartie().deplacerCase(EDeplacement.HAUT, joueur, this.noConnexion);
							break;
						case 'b':
							serveur.getPartie().deplacerCase(EDeplacement.BAS, joueur, this.noConnexion);
							break;
						case 'g':
							serveur.getPartie().deplacerCase(EDeplacement.GAUCHE, joueur, this.noConnexion);
							break;
						case 'd':
							serveur.getPartie().deplacerCase(EDeplacement.DROITE, joueur, this.noConnexion);
							break;
						case 'l':
							serveur.getPartie().envoyerJoueurs(ligne, socket,joueur);
							break;
						case 's':
							serveur.getPartie().envoyerJoueurs(ligne, socket,joueur);
							break;
						case 'i':
							if(liste != null) {
									if(liste.get(1) instanceof byte[]) {
										byte[] img = (byte[]) liste.get(1);
										int taille = (int) liste.get(2);
									serveur.getPartie().setInfos(img, taille);
									}
								}
							serveur.getPartie().envoyerJoueurs(ligne,socket,joueur);
							break;
						case 'p':
							serveur.getPartie().envoyerPuzzle(ligne,socket,joueur);
							break;
						}

					}
				}

				sleep(5);
			} // while
		} catch (InterruptedException erreur) {
			/* le thread s'arrete */} catch (IOException erreur) {
			erreur.printStackTrace();
			System.out.println("Déconnexion du client numéro " + this.noConnexion);
			serveur.getPartie().deconnecterJoueur(joueur); // cas où le cient n'est plus connecté au serveur
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}// run

}
