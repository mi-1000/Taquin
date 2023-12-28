package main.java.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class Chrono implements Serializable{

	int time = 0;
	boolean isRunning = false; //Booleen qui gère si le thread tourne ou pas.
	private final PropertyChangeSupport support = new PropertyChangeSupport(this);

	/*
	 * Lance le chronomètre. Incrémente la variable seconde toutes les 1000ms.
	 */
	public void lancerChrono() {
		isRunning = true;
		Runnable r = new Runnable() {
			@Override
			public void run() {
				while (isRunning) {
					try {
						Thread.sleep(10);
						time++;
						support.firePropertyChange("property", 0, 1);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		};
		Thread th = new Thread(r);
		th.setDaemon(true);
		th.start();
	}

	/*
	 * interrompt le chronomètre
	 */
	public void stopChrono() {
		isRunning = false;
	}

	/**
	 * 
	 * @return String qui contient le temps écoulé : m:s. exemple : 6:22.
	 */
	public String getMS() {
//		int seconds = time / 100;
//		int m = (int) (seconds / 60);
//		int s = (int) (seconds % 60);
//		String r = "";
//		if (m > 0) {
//			r += m + ":";
//		}
//		if (s > 0) {
//			r += s;
//		}
//		if (m <= 0 && s <= 0) {
//			r = "0";
//		}
//		return r;
		
		Date date = new Date(time*10);
		DateFormat formatter = new SimpleDateFormat("mm:ss.SSS", Locale.FRANCE);
		formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
		String dateFormatted = formatter.format(date);
		return dateFormatted;
	}
	
	public int getTime() {
		return this.time;
	}
	
	public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
	    support.addPropertyChangeListener(propertyName, listener);
	  }

}
