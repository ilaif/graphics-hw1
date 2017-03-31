package graphics.hw1;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Main {

    /**
     * Parses CLI arguments and uses SeamCarver class with the arguments to product processed images.
     * @param args
     */
    public static void main(String[] args) {

        //TODO-Ilai: Parse command line arguments.

        //TODO-Ilai: Call Seam Carver on arguments.

        //TODO-Ilai: Save image to output file.

        /////////////////////

        //TODO-Ilai: Show 2 results from vertical and diagonal for 2 different images.
        //TODO-Ilai: Understand how to increase
        //TODO-Ilai: Check on two images with interpolation and without

        BufferedImage img = null;
        File f = new File("./images/sunset-horizon.jpg");
        if (!f.exists()) {
            System.out.println("File does not exist");
            return;
        }
        try {
            img = ImageIO.read(f);
        } catch (IOException e) {
            System.out.println("Error loading image, error: " + e.toString());
        }

        System.out.println(img.toString());
    }
}
