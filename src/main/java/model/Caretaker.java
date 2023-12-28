package main.java.model;

import java.io.Serializable;
import java.util.Deque;
import java.util.LinkedList;

public class Caretaker implements Serializable {

	private Deque<Object> puzzlesMemento;
	private static int TAILLE_MAX = 4;

	public Caretaker() {
		puzzlesMemento = new LinkedList<>();
	}

	/**
	 * Ajout du memento sans dépasser TAILLE_MAX dans la file
	 * 
	 * @param puzzleMemento
	 */
	public void addMemento(Object puzzleMemento) {
		if (puzzlesMemento.size() == TAILLE_MAX) {
			puzzlesMemento.removeLast();
		}
		puzzlesMemento.addFirst(puzzleMemento);
	}

	/**
	 * Récupère la tête de file et la retire
	 * 
	 * @return Object Memento
	 */
	public Object getMemento() {
		if (puzzlesMemento.size() == 0)
			return null;
		Object res = puzzlesMemento.getFirst();
		puzzlesMemento.removeFirst();
		return res;
	}

	public int getNbMementos() {
		return puzzlesMemento.size();
	}

}
