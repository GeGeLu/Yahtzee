package game;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.LayoutManager;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.imageio.plugins.jpeg.JPEGHuffmanTable;
import javax.swing.JPanel;

public class LoadFrame extends JPanel{

    
Image image;
    public LoadFrame() {
       try {                
          image = ImageIO.read(new File("die1.png"));
          JPanel jp = new JPanel((LayoutManager) image);
          this.add(jp);
          setVisible(true);
       } catch (IOException ex) {
            // handle exception...
       }
    }

  public static void main(String args[]) {
	  LoadFrame lf = new LoadFrame();
  }

}