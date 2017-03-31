package graphics.hw1;

import java.awt.image.BufferedImage;
import java.nio.BufferOverflowException;

/**
 * EnergyMatrixUtil
 * <p>
 * Calculates energy matrix from an image.
 */
public final class EnergyMatrixUtil {

    private static double getDiff(BufferedImage img, int x1, int y1, int x2, int y2) {
        if (x2 < 0 || x2 >= img.getWidth() || y2 < 0 || y2 >= img.getHeight()) {
            return -1;
        }
        int clr = img.getRGB(x1, y1);
        int red1 = (clr & 0x00ff0000) >> 16;
        int green1 = (clr & 0x0000ff00) >> 8;
        int blue1 = clr & 0x000000ff;
        clr = img.getRGB(x2, y2);
        int red2 = (clr & 0x00ff0000) >> 16;
        int green2 = (clr & 0x0000ff00) >> 8;
        int blue2 = clr & 0x000000ff;
        return (Math.abs(red1 - red2) + Math.abs(blue1 - blue2) + Math.abs(green1 - green2)) / 3;
    }

    static Matrix getEnergyMatrix(BufferedImage inImg, EnergyType energyType) {

        int width = inImg.getWidth();
        int height = inImg.getHeight();
        int y, x, sumDiff, neighbours, i, j;
        double diff;
        Matrix m = new Matrix(height, width);

        for (x = 0; x < width; x++) {
            for (y = 0; y < height; y++) {
                sumDiff = 0;
                neighbours = 0;
                for (i = -1; i <= 1; i++) {
                    for (j = -1; j <= 1; j++) {
                        diff = getDiff(inImg, x, y, i, j);
                        if (diff > -1) {
                            sumDiff += diff;
                            neighbours += 1;
                        }
                    }
                }
                m.set(y, x, (sumDiff / neighbours));
            }
        }

        return m;

        //TODO-Roy: Implement adding Entropy by energyType
    }
}
