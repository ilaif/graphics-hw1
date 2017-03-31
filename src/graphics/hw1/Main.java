package graphics.hw1;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Main {

    public static void main(String[] args) {


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
