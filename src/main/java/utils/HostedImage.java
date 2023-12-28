package main.java.utils;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

/**
 * Cette classe contient des fonctions pour téléverser une image en ligne
 */
public class HostedImage {

	private static final String API_CLE = "abebd4dbb8558cabd4c1498b31675c98";
	private static final String API_URL = "https://api.imgbb.com/1/upload?key=" + API_CLE;

	/**
	 * Téléverse et héberge une image en ligne
	 * 
	 * @param cheminImage Le chemin d'accès de l'image à héberger
	 * @return L'URL de l'image hébergée en cas de succès, <code>null</code> sinon
	 */
	public static String uploadImage(String cheminImage) {
		try {
			byte[] imageBytes = Files.readAllBytes(new File(cheminImage).toPath());
			String base64Image = Base64.getEncoder().encodeToString(imageBytes);

			HttpClient httpclient = HttpClients.createDefault();
			HttpPost httppost = new HttpPost(API_URL);

			List<NameValuePair> params = new ArrayList<>(2);
			params.add(new BasicNameValuePair("image", base64Image));
			httppost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));

			HttpResponse reponse = httpclient.execute(httppost);
			HttpEntity entity = reponse.getEntity();

			String url = null;

			if (entity != null) {
				try (InputStream in = entity.getContent()) {
					BufferedReader br = new BufferedReader(new InputStreamReader(in));
					String ligne;

					StringBuilder rep = new StringBuilder();
					while ((ligne = br.readLine()) != null)
						rep.append(ligne);

					in.close();

					JSONObject json = new JSONObject(rep.toString());
					JSONObject j = json.getJSONObject("data");
					url = (String) j.get("url");
				}
			}

			return url;

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Téléverse et héberge une image en ligne
	 * 
	 * @param image Le fichier représentant l'image à héberger
	 * @return L'URL de l'image hébergée en cas de succès, <code>null</code> sinon
	 */
	public static String uploadImage(File image) {
		try {
			byte[] imageBytes = Files.readAllBytes(image.toPath());
			String base64Image = Base64.getEncoder().encodeToString(imageBytes);

			HttpClient httpclient = HttpClients.createDefault();
			HttpPost httppost = new HttpPost(API_URL);

			List<NameValuePair> params = new ArrayList<>(2);
			params.add(new BasicNameValuePair("image", base64Image));
			httppost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));

			HttpResponse reponse = httpclient.execute(httppost);
			HttpEntity entity = reponse.getEntity();

			String url = null;

			if (entity != null) {
				try (InputStream in = entity.getContent()) {
					BufferedReader br = new BufferedReader(new InputStreamReader(in));
					String ligne;

					StringBuilder rep = new StringBuilder();
					while ((ligne = br.readLine()) != null)
						rep.append(ligne);

					in.close();

					JSONObject json = new JSONObject(rep.toString());
					JSONObject j = json.getJSONObject("data");
					url = (String) j.get("url");
				}
			}

			return url;

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Téléverse et héberge une image en ligne
	 * 
	 * @param image  Le fichier représentant l'image à héberger
	 * @param format Le format du fichier (<code>png</code>, <code>jpg</code> ou
	 *               <code>gif</code>)
	 * @return L'URL de l'image hébergée en cas de succès, <code>null</code> sinon
	 */
	public static String uploadImage(BufferedImage image, String format) {
		try {
			byte[] imageBytes = Utils.bufferedImageToByteArray(image, format);
			String base64Image = Base64.getEncoder().encodeToString(imageBytes);

			HttpClient httpclient = HttpClients.createDefault();
			HttpPost httppost = new HttpPost(API_URL);

			List<NameValuePair> params = new ArrayList<>(2);
			params.add(new BasicNameValuePair("image", base64Image));
			httppost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));

			HttpResponse reponse = httpclient.execute(httppost);
			HttpEntity entity = reponse.getEntity();

			String url = null;

			if (entity != null) {
				try (InputStream in = entity.getContent()) {
					BufferedReader br = new BufferedReader(new InputStreamReader(in));
					String ligne;

					StringBuilder rep = new StringBuilder();
					while ((ligne = br.readLine()) != null)
						rep.append(ligne);

					in.close();

					JSONObject json = new JSONObject(rep.toString());
					JSONObject j = json.getJSONObject("data");
					url = (String) j.get("url");
				}
			}

			return url;

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Renvoie une image à partir d'une URL
	 * 
	 * @param url L'URL de l'image
	 * @return L'image en cas de succès, <code>null</code> sinon
	 */
	public static Image getImage(String url) {
		try {
			URL urlImage = new URL(url);
			BufferedImage img = ImageIO.read(urlImage);
			return SwingFXUtils.toFXImage(img, null);
		} catch (IOException e) {
			return null;
		}
	}

	/**
	 * Renvoie une image à partir d'une URL
	 * 
	 * @param url L'URL de l'image
	 * @return Un tableau d'octets représentant l'image en cas de succès,
	 *         <code>null</code> sinon
	 */
	public static byte[] getImageBytes(String url) {
		InputStream in = null;
		try {
			URL urlImage = new URL(url);
			in = urlImage.openStream();
			return IOUtils.toByteArray(in);
		} catch (IOException e) {
			return null;
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					return null;
				}
			}
		}
	}
}
