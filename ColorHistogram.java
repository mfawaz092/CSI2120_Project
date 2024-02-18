/*
CSI 2120 - Section A
Project - Part 1

Group:
Aiden McGarrie - 300244967
Mahmoud Fawaz - 300162088
*/

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import javax.imageio.ImageIO;

public class ColorHistogram {

    private int d;
    private double[] histogram;
    
    public ColorHistogram(int d) {
        this.d = d;
        this.histogram = new double[512];
    }

    public ColorHistogram (String filename) {
        try {
            String data = new String(Files.readAllBytes(Paths.get(filename)));
            String[] values = data.split(",");
            this.histogram = new double[values.length];
            for (int i = 0; i < values.length; i++) {
                this.histogram[i] = Double.parseDouble(values[i]);
            }
        } catch (IOException e) {}
    }

    public void setImage (ColorImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        int totalPixels = width * height;
        int[][][] pixels = new int[width][height][3];

        int[] hist = new int[512];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                int[] pixel = image.getPixel(i, j);
                int index = (pixel[0] << (2 * d)) + (pixel[1] << d) + pixel[2];
                hist[index]++;
            }
        }

        this.histogram = new double[512];
        for (int i = 0; i < 512; i++) {
            this.histogram[i] = (double) hist[i] / totalPixels;
        }
    }

    public double[] getHistogram() {
        return histogram;
    }

    public double compare(ColorHistogram hist) {
        double[] otherHist = hist.getHistogram();
        double intersection = 0;
        for (int i = 0; i < histogram.length; i++) {
            intersection += Math.min(histogram[i], otherHist[i]);
        }
        return intersection;
    }

    public void ColorHistogram (String filename) {
        try {
            FileWriter writer = new FileWriter(filename);
            for (int i = 0; i < histogram.length; i++) {
                writer.write(String.valueOf(histogram[i]));
                if (i < histogram.length - 1) {
                    writer.write(",");
                }
            }
            writer.close();
        } catch (IOException e) {}
    }
}