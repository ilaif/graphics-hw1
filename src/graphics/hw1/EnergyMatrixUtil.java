package graphics.hw1;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.nio.BufferOverflowException;

/**
 * EnergyMatrixUtil
 * <p>
 * Calculates energy matrix from an image.
 */
public final class EnergyMatrixUtil {

    static Matrix getEnergyMatrix(BufferedImage inImg, EnergyType energyType) {

        Matrix m = getGradientMatrix(inImg);

        switch (energyType) {
            case ENTROPY:
                // m = m.plus(getEntropyMatrix(inImg));...
                break;
            case FORWARD_ENERGY:
                // m = getForwardEnergyMatrix(inImg);...
                break;
            case REGULER:
            default:
                m = getGradientMatrix(inImg);
                break;
        }

        //TODO-Roy: Implement adding Entropy by energyType such as: m = m.plus(getEntropy(...))

        return m;

    }

    private static Matrix getGradientMatrix(BufferedImage inImg) {

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

    private static double getDiff(BufferedImage img, int x1, int y1, int x2, int y2) {
        if (x2 < 0 || x2 >= img.getWidth() || y2 < 0 || y2 >= img.getHeight()) {
            return -1;
        }
        Color c1 = new Color(img.getRGB(x1, y1));
        Color c2 = new Color(img.getRGB(x2, y2));
        return (Math.abs(c1.getRed() - c2.getRed()) +
                Math.abs(c1.getBlue() - c2.getBlue()) +
                Math.abs(c1.getGreen() - c2.getGreen())
        ) / 3;
    }
}
