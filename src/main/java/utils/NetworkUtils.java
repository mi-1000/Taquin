package main.java.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.SocketException;
import java.net.URL;
import java.net.UnknownHostException;

public class NetworkUtils {

	/**
	 * @return String l'IPv4 de la machine
	 */
	private static String getLocalIPV4() {
		String ip = "";
		try (final DatagramSocket socket = new DatagramSocket()) {
			socket.connect(InetAddress.getByName("8.8.8.8"), 10002);
			ip = socket.getLocalAddress().getHostAddress();
		} catch (SocketException | UnknownHostException e) {
			e.printStackTrace();
		}
		return ip;
	}

	/**
	 * Cherche l'ip de la machine sur le réseau local ou public en fonction de lan
	 * <code>boolean</code>
	 * 
	 * @param lan, boolean
	 * @return String de l'IP publique ou locale
	 */
	public static String getServeurIPV4(boolean lan) {
		String ip = "";
		if (lan) {
			ip = getLocalIPV4();
		} else {
			URL url;
			try {
				url = new URL("http://checkip.amazonaws.com/"); // requête sur le serveur d'amazon
				BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream())); // on récupère le
																									// résultat de la
																									// requête
				ip = br.readLine(); // on stocke la ligne dans la chaîne ip
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return ip;
	}
	
	public static void checkPort(int port) throws InvalidPortException{
		if (!(port >= 1024 && port <= 65535)) {
			throw new InvalidPortException(port);
		}
	}
}
