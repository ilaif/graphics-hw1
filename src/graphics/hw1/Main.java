package graphics.hw1;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Main {

    /**
     * Parses CLI arguments and uses SeamCarver class with the arguments to product processed images.
     *
     * @param args <input image filename> <output # columns> <output # rows> <energy type> <output image filename>
     */
    public static void main(String[] args) {

        final long start = System.nanoTime();

        // Parse arguments
        if (args.length < 5) {
            System.out.println("Error: Not enough arguments: <input image filename> <output # columns> <output # rows> <energy type> <output image filename>");
            return;
        }

        int columns, rows, energyTypeInt;
        String inputImageFilepath = args[0];
        try {
            columns = Integer.parseInt(args[1]);
            rows = Integer.parseInt(args[2]);
            energyTypeInt = Integer.parseInt(args[3]);
        } catch (NumberFormatException err) {
            System.out.println("Error: Columns, Rows or Energy Type arguments are not a valid integer.");
            return;
        }

        if (columns < 0 || rows < 0 || energyTypeInt < 0 || energyTypeInt > 2) {
            System.out.println("Error: Columns, Rows or Energy Type arguments are not in a valid integer range.");
            return;
        }

        EnergyType energyType = EnergyType.values()[energyTypeInt];
        String outputImageFilepath = args[4];

        // Load input image
        BufferedImage inputImage;
        File inputFile = new File(inputImageFilepath);
        if (!inputFile.exists()) {
            System.out.println("Error: File does not exist");
            return;
        }
        try {
            inputImage = ImageIO.read(inputFile);
        } catch (IOException error) {
            System.out.println("Error: Loading image failed. Info: " + error.toString());
            return;
        }

        // Apply seam carving

        SeamCarver smc = new SeamCarver(inputImage, energyType);

        BufferedImage outputImage = smc.resize(columns, rows);

        // Save to output file path

        File outputFile = new File(outputImageFilepath);

        try {
            ImageIO.write(outputImage, "PNG", outputFile);
        } catch (IOException error) {
            System.out.println("Error: Failed saving resized image to output file. Info: " + error.toString());
            return;
        }

        final long end = System.nanoTime();
        System.out.println("Output file saved successfully");
        System.out.format("Finished in %d seconds.", (end - start) / 1000000000);

        //TODO-Ilai: Show 2 results from vertical and diagonal for 2 different images.
        //TODO-Ilai: Understand how to increase
        //TODO-Ilai: Check on two images with interpolation and without
    }
}
