/*
CSI 2120 - Section A
Project - Part 1

Group:
(your name and ID)
Aiden McGarrie - 300244967
*/

import java.io.File;

public class SimilaritySearch {

    public static void main(String[] args) {

        String queryImageFilename = args[0];
        String datasetDirectory = args[1];

        ColorImage queryImage = new ColorImage(queryImageFilename);
        if (queryImage.getWidth() == 0 || queryImage.getHeight() == 0) {
            System.out.println("Error: Unable to load query image.");
            return;
        }

        queryImage.reduceColor(3); 

        File datasetDir = new File(datasetDirectory);
        File[] datasetFiles = datasetDir.listFiles();
        if (datasetFiles == null || datasetFiles.length == 0) {
            System.out.println("Error: No images found in the dataset directory.");
            return;
        }

        ColorHistogram[] datasetHistograms = new ColorHistogram[datasetFiles.length];
        int validFilesCount = 0;
        for (File datasetFile : datasetFiles) {
            if (datasetFile.isFile() && isImageFile(datasetFile)) {
                String datasetImageFilename = datasetFile.getPath();
                ColorImage datasetImage = new ColorImage(datasetImageFilename);
                if (datasetImage.getWidth() == 0 || datasetImage.getHeight() == 0) {
                    System.out.println("Error: Unable to load dataset image: " + datasetImageFilename);
                    continue;
                }
                datasetImage.reduceColor(3); 
                ColorHistogram histogram = new ColorHistogram(3);
                histogram.setImage(datasetImage);
                datasetHistograms[validFilesCount++] = histogram;
            }
        }

        if (validFilesCount == 0) {
            System.out.println("Error: No valid images found in the dataset directory.");
            return;
        }

        ColorHistogram queryHistogram = new ColorHistogram(3);
        queryHistogram.setImage(queryImage);

        double[] intersections = new double[validFilesCount];
        for (int i = 0; i < validFilesCount; i++) {
            intersections[i] = queryHistogram.compare(datasetHistograms[i]);
        }

        int[] indicesOfImages = new int[Math.min(5, validFilesCount)];
        for (int i = 0; i < indicesOfImages.length; i++) {
            double maxIntersection = -1;
            int maxIndex = -1;
            for (int j = 0; j < validFilesCount; j++) {
                if (intersections[j] > maxIntersection) {
                    maxIntersection = intersections[j];
                    maxIndex = j;
                }
            }
            indicesOfImages[i] = maxIndex;
            intersections[maxIndex] = -1; 
        }

        System.out.println("Top 5 most similar images to " + queryImageFilename + " :");
        for (int i : indicesOfImages) {
            System.out.println(datasetFiles[i].getName());
        }
    }

    private static boolean isImageFile(File file) {
        String name = file.getName().toLowerCase();
        return name.endsWith(".jpg");
    }
}
