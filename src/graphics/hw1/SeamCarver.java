package graphics.hw1;

import graphics.hw1.enums.EnergyType;

import java.awt.image.BufferedImage;
import java.nio.Buffer;

/**
 *
 */
public class SeamCarver {

    SeamCarvingUtil mSmcUtil;
    EnergyType mEnergyType;
    BufferedImage mInputImage;

    public SeamCarver(BufferedImage inputImage, EnergyType energyType) {
        mInputImage = inputImage;
        mEnergyType = energyType;
    }

    public BufferedImage resize(int width, int height) {

        int curWidth = mInputImage.getWidth();
        int curHeight = mInputImage.getHeight();

        int widthChange = width - curWidth;
        int heightChange = height - curHeight;

        int i;
        BufferedImage img = mInputImage;
        int[][] imagePixels;

        // Horizontal seam removal
        for (i = 0; i < Math.abs(heightChange); i++) {
            Matrix energyMatrix = EnergyMatrixUtil.getEnergyMatrix(img, mEnergyType);
            mSmcUtil = new SeamCarvingUtil(energyMatrix, true);
            int[] seam = mSmcUtil.findLowestEnergySeam(false);
            imagePixels = removeSeamFromImage(imageToPixels(img), seam, false);
            img = pixelsToImage(imagePixels);
        }

        // Vertical seam removal
        for (i = 0; i < Math.abs(widthChange); i++) {
            Matrix energyMatrix = EnergyMatrixUtil.getEnergyMatrix(img, mEnergyType);
            mSmcUtil = new SeamCarvingUtil(energyMatrix, true);
            int[] seam = mSmcUtil.findLowestEnergySeam(true);
            imagePixels = removeSeamFromImage(imageToPixels(img), seam, true);
            img = pixelsToImage(imagePixels);
        }

        return img;

        //TODO: Need to make this better it's very slow and inefficient
    }

    private int[][] imageToPixels(BufferedImage image) throws IllegalArgumentException {
        if (image == null) {
            throw new IllegalArgumentException();
        }

        int width = image.getWidth();
        int height = image.getHeight();
        int[][] pixels = new int[height][width];
        for (int row = 0; row < height; row++) {
            image.getRGB(0, row, width, 1, pixels[row], 0, width);
        }
        return pixels;
    }

    public static BufferedImage pixelsToImage(int[][] pixels) throws IllegalArgumentException {

        if (pixels == null) {
            throw new IllegalArgumentException();
        }

        int width = pixels[0].length;
        int height = pixels.length;
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int row = 0; row < height; row++) {
            image.setRGB(0, row, width, 1, pixels[row], 0, width);
        }
        return image;
    }

    private int[][] removeSeamFromImage(int[][] imagePixels, int[] seam, boolean isVertical) {

        int curHeight = imagePixels.length;
        int curWidth = imagePixels[0].length;
        int height = curHeight - 1;
        int width = seam.length;
        if (isVertical) {
            height = seam.length;
            width = curWidth - 1;
        }
        int[][] resizedImage = new int[height][width];
        int[][] removePixels = new int[curHeight][curWidth];
        int i, j, k;

        if (isVertical) {
            // Mark which pixels to remove
            removePixels = new int[curHeight][curWidth];
            for (i = 0; i < seam.length; i++) {
                j = seam[i];
                removePixels[i][j] = 1;
            }
            // Remove the pixels by creating a new smaller image
            for (i = 0; i < curHeight; i++) {
                k = 0;
                for (j = 0; j < curWidth; j++) {
                    if (removePixels[i][j] == 0) {
                        resizedImage[i][k++] = imagePixels[i][j];
                    }
                }
            }
        } else {
            // Mark which pixels to remove
            for (j = 0; j < seam.length; j++) {
                i = seam[j];
                removePixels[i][j] = 1;
            }
            // Remove the pixels by creating a new smaller image
            for (j = 0; j < curWidth; j++) {
                k = 0;
                for (i = 0; i < curHeight; i++) {
                    if (removePixels[i][j] == 0) {
                        resizedImage[k++][j] = imagePixels[i][j];
                    }
                }
            }
        }

        return resizedImage;
    }
}
