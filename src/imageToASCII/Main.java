
package imageToASCII;

import java.awt.Color;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

import javax.imageio.ImageIO;

/**
 * @author Yang
 *
 */
class Main {
	 static String ascii = "█▓▒░─";
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		int ImageWid = 30;
		File settings =  new File("settings.txt");
		String imageName = "image.png";
		if(settings.exists()) {
			Scanner in = null;
			try {
				in = new Scanner(settings);
			
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			if(in.hasNextInt()) {
				ImageWid= in.nextInt();
				
			}else {
				System.err.println("settings.txt is not formated properly. Please have a number on the first word of the document");
			}
			if(in.hasNextLine())
				imageName = in.nextLine().trim();
			in.close();
		}else {
			System.err.println("settings.txt not found");
		}
		File image =  new File(imageName);

		double div = 255/((double)ascii.length()-1);
		try {
			BufferedImage selectedImage = ImageIO.read(image);
			selectedImage = preProcess(selectedImage , ImageWid , (int)(((double)ImageWid/(double)selectedImage.getWidth()) * selectedImage.getHeight()));
			File file = new File("target.png");
			ImageIO.write(selectedImage, "png", file);
			File outputText = new File("image.txt");
			if(!outputText.exists()) outputText.createNewFile();
			PrintWriter out = new PrintWriter(outputText);
			for( int i = 0 ; i <selectedImage.getHeight() ; i ++) {
				String packet = "";
				for ( int w = 0 ; w < selectedImage.getWidth(); w++) {
					
					 Color rgb = new Color(selectedImage.getRGB(w,i));
					 packet += ascii.charAt((int)( (double)rgb.getBlue() /(double)div ));
					 
				}
				out.println(packet);
			}out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * GreyScales Image
	 */
	static BufferedImage preProcess(BufferedImage originalImage, int targetWidth, int targetHeight) throws IOException {
	    Image resultingImage = originalImage.getScaledInstance(targetWidth, targetHeight, Image.SCALE_DEFAULT);
	    BufferedImage outputImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
	    outputImage.getGraphics().drawImage(resultingImage, 0, 0, null);
	    for ( int x = 0 ; x <targetWidth ; x++ ) {
	    	 for ( int y = 0 ; y <targetHeight ; y++ ) {
	    		 Color rgb = new Color(outputImage.getRGB(x, y));
	    		 double gray = (0.299 * (double)rgb.getRed()+ 0.587 *(double)rgb.getGreen() + 0.114*(double)rgb.getBlue());
	    		
	    		 outputImage.setRGB(x, y, new Color((int)gray,(int)gray,(int)gray).getRGB());
	 	    }
	    }
	    return outputImage;
	}

}
