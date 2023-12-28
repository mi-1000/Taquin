package main.java;

public class Main {

	public static void main(String[] args) {
		if (args.length == 0) {
			MainGUI.main(args);
		} else {
			if (args[0].equals("-c"))
				new JeuConsole();
		}
	}

}
