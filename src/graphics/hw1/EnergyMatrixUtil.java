package graphics.hw1;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * EnergyMatrixUtil
 * <p>
 * Calculates energy matrix from an image.
 */
public final class EnergyMatrixUtil {

    static Matrix getEnergyMatrix(BufferedImage inImg, EnergyType energyType) {
        Matrix mEnergyMatrix, mEntropyMatrix;
        mEnergyMatrix = getGradientMatrix(inImg);

        switch (energyType) {
            case FORWARD_ENERGY:
                //WILL BE ADDED LATER
                break;
            case ENTROPY:
                mEntropyMatrix = getEntropyMatrix(inImg);
                mEnergyMatrix = mEnergyMatrix.plus(mEntropyMatrix);
            default://==case REGULAR
                break;
        }
        return (mEnergyMatrix);

    }

    private static Matrix getGradientMatrix(BufferedImage inImg) {
        int width = inImg.getWidth();
        int height = inImg.getHeight();
        int neighbours;
        double sumDiff;
        Matrix mGradientMatrix = new Matrix(height, width);

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                neighbours = -1;
                sumDiff = 0;
                for (int dx = Math.max(x - 1, 0); dx <= Math.min(x + 1, width - 1); dx++)
                    for (int dy = Math.max(y - 1, 0); dy <= Math.min(y + 1, height - 1); dy++) {
                        neighbours++;
                        sumDiff += getDiff(inImg, x, y, dx, dy);
                    }
                mGradientMatrix.set(y, x, (sumDiff / neighbours));
            }
        }

        return mGradientMatrix;
    }


    private static double getDiff(BufferedImage img, int x1, int y1, int x2, int y2) {
        Color c1 = new Color(img.getRGB(x1, y1));
        Color c2 = new Color(img.getRGB(x2, y2));
        return (Math.abs(c1.getRed() - c2.getRed()) +
                Math.abs(c1.getBlue() - c2.getBlue()) +
                Math.abs(c1.getGreen() - c2.getGreen()))
                / 3;

    }

    /**
     * Calculating the entropy matrix of each pixel. using getPmdMatrix.
     *
     * @param inImg input image to compute entropy matrix for
     * @return entropy matrix
     */
    private static Matrix getEntropyMatrix(BufferedImage inImg) {
        int width = inImg.getWidth();
        int height = inImg.getHeight();
        Matrix mEntropyMatrix = new Matrix(height, width);
        Matrix mPmnMatrix = getPmnMatrix(inImg);
        double entropy, pmn;

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                entropy = 0;
                for (int k = Math.max(x - 4, 0); k <= Math.min(x + 4, width - 1); k++) {
                    for (int j = Math.max(y - 4, 0); j <= Math.min(y + 4, height - 1); j++) {
                        pmn = mPmnMatrix.get(j, k);
                        entropy -= (pmn * Math.log(pmn));
                    }
                }
                mEntropyMatrix.set(y, x, entropy);
            }
        }

        return mEntropyMatrix;
    }


    /**
     * Calculating the "Pmn" matrix = each pixel replaced with the greyscale value friction out of the
     * 11X11 pixel window surrounding him greyscale values sum. using getGrayScaleMatrix.
     *
     * @param inImg input image to compute pmn for.
     * @return pmn matrix
     */
    private static Matrix getPmnMatrix(BufferedImage inImg) {
        int width = inImg.getWidth();
        int height = inImg.getHeight();
        double gray, sumNeighboursGray, pmn;
        Matrix mPmnMatrix = new Matrix(height, width);
        Matrix mGrayScaleMatrix = getGrayScaleMatrix(inImg);

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                sumNeighboursGray = 0;
                gray = mGrayScaleMatrix.get(y, x);
                for (int k = Math.max(x - 4, 0); k <= Math.min(x + 4, width - 1); k++) {
                    for (int j = Math.max(y - 4, 0); j <= Math.min(y + 4, height - 1); j++) {
                        sumNeighboursGray += mGrayScaleMatrix.get(j, k);
                    }
                }
                pmn = gray / sumNeighboursGray;
                mPmnMatrix.set(y, x, pmn);
            }
        }

        return mPmnMatrix;
    }


    private static Matrix getGrayScaleMatrix(BufferedImage inImg) {
        //calculates grayscale matrix using getGrayScale function.

        int width = inImg.getWidth();
        int height = inImg.getHeight();
        Matrix mGrayScaleMatrix = new Matrix(height, width);
        double gray;

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                gray = getGrayScale(inImg, x, y);
                mGrayScaleMatrix.set(y, x, gray);
            }
        }

        return mGrayScaleMatrix;
    }

    private static double getGrayScale(BufferedImage inImg, int x, int y) {
        //calculates the grayscale value using avarage of rgb values.

        int rgb, r, g, b;
        double gray;
        rgb = inImg.getRGB(x, y);

        r = (rgb >> 16) & 0xFF;
        g = (rgb >> 8) & 0xFF;
        b = (rgb & 0xFF);
        gray = (r + g + b) / 3;

        return gray;
    }
}


