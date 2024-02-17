/*
CSI 2120 - Section A
Project - Part 1

Group:
(your name and ID)
Aiden McGarrie - 300244967
*/

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import javax.imageio.ImageIO;


public class ColorImage {

    private int width;
    private int height;
    private int depth;
    private int[][][] pixels;

    public ColorImage (String filename) {
        try {
            BufferedImage image = ImageIO.read(new File(filename));
            if (image == null) {
                throw new IOException("Unable to read image file: " + filename);
            }
            width = image.getWidth();
            height = image.getHeight();
            pixels = new int[width][height][3];
            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {
                    int rgb = image.getRGB(i, j);
                    pixels[i][j][0] = (rgb >> 16) & 0xFF; 
                    pixels[i][j][1] = (rgb >> 8) & 0xFF; 
                    pixels[i][j][2] = rgb & 0xFF; 
                }
            }
        } catch (IOException e) {}
    }
    
    public int getWidth() {
        return width;
    }
    
    public int getHeight() {
        return height;
    }
    
    public int getDepth() {
        return depth;
    }
    
    public int[] getPixel (int i, int j) {
        return pixels[i][j];
    }
    
    public void reduceColor (int d) {
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                pixels[i][j][0] >>= (8 - d);
                pixels[i][j][1] >>= (8 - d);
                pixels[i][j][2] >>= (8 - d);
            }
        }
    }
}
