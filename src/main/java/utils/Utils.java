package main.java.utils;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

import javax.imageio.ImageIO;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

public class Utils {
	
	/**
	 * Génére un int entre min et max inclus
	 * @param min
	 * @param max
	 * @return
	 */
	public static int getRandomNumberInRange(int min, int max) {

		if (min >= max) {
			throw new IllegalArgumentException("Max doit être supérieur à min");
		}

		Random r = new Random();
		return r.nextInt((max - min) + 1) + min;
	}
	
	public static Image getSubImage(int x, int y, int w, int h, Image stripImg)
    {
        PixelReader pr = stripImg.getPixelReader();
        WritableImage wImg = new WritableImage(w, h);
        PixelWriter pw = wImg.getPixelWriter();
        
        for( int readY = y ; readY < y + h; readY++ ) {
        	for( int readX = x; readX < x + w; readX++ ) {
                //Obtenir le pixels aux coordonnées X et Y
                Color color = pr.getColor( readX, readY );
                //Appliquer le pixel à la WritableImage à l'aide du Pixel Writer
                pw.setColor(readX-x, readY-y, color);
            }//X
        }//Y
        return wImg;
    }//
	
	// transforme une BufferedImage en byte[]
    public static byte[] bufferedImageToByteArray(BufferedImage bi, String format) throws IOException {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        if(format==null) format = "png";
        ImageIO.write(bi, format, baos);
        byte[] bytes = baos.toByteArray();
        return bytes;

    }

    // transforme un byte[] en BufferedImage
    public static BufferedImage byteArrayToBufferedImage(byte[] bytes) throws IOException {

        InputStream is = new ByteArrayInputStream(bytes);
        BufferedImage bi = ImageIO.read(is);
        return bi;

    }
    
    public static byte[] imageToByteArray(Image image, String format) throws IOException {
        BufferedImage img = SwingFXUtils.fromFXImage(image, null);
        return Utils.bufferedImageToByteArray(img, format);
    } 
	
    

    public static boolean compareByteArrays(byte[] a, byte[] b) {
    	if(a.length == b.length) {
    		for(int i=0;i<a.length;i++) {
        		if(b[i]!=a[i]) return false;
        	}
    	}else return false;
    	return true;
    }
	
}
