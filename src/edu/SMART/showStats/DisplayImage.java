package edu.SMART.showStats;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.*;
 
public class DisplayImage {
	public void showImage(String path){
		//String path = "http://www.javalobby.org/images/jlmasthead_450x34.gif";
        URL url;
		try {
			//url = new URL(path);
		
        BufferedImage image = ImageIO.read(new File(path));
        JLabel label = new JLabel(new ImageIcon(image));
        JFrame f = new JFrame();
        //f.setDefaultCloseOperation();
        f.setTitle(path);
        f.getContentPane().add(label);
        f.pack();
        f.setLocation(300,300);
        f.setVisible(true);
		} catch (MalformedURLException e) {
			System.out.println("Could not show image URL exception");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("Could not show image IO exception");
			e.printStackTrace();
		}
	}
    public static void main(String[] args) throws IOException {
        
    }
}