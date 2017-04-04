package graphics.hw1;

import java.awt.image.BufferedImage;

/**
 *
 */
public class SeamCarver {

    EnergyType mEnergyType;
    BufferedImage mInputImage;

    public SeamCarver(BufferedImage inputImage, EnergyType energyType) {
        mInputImage = inputImage;
        mEnergyType = energyType;
    }

    /**
     * Resize image to desired width and height using seam carving
     *
     * @param width  Desired width of resulting image.
     * @param height Desired height of resulting image.
     * @return Resulting image.
     */
    public BufferedImage resize(int width, int height) {

        int curWidth = mInputImage.getWidth();
        int curHeight = mInputImage.getHeight();

        int widthChange = width - curWidth;
        int heightChange = height - curHeight;

        //TODO-Roy: Use this to decide if to add or remove seams.
        // removeHorizontal is true if heightChange is negative, meaning we need to squeeze image, and positive otherwise.
        boolean removeHorizontal = (heightChange < 0);
        boolean removeVertical = (widthChange < 0);

        int i;
        BufferedImage img = mInputImage;
        SeamCarvingUtil mSmcUtil;
        int[][] imagePixels;
        int changesToMake = Math.abs(heightChange) + Math.abs(widthChange);
        int reportEvery = changesToMake / 10;
        int changedCou = 0;

        System.out.println("Progress: 0%");
        // NOTE: There was a test to calculate the energy and accum energy matrix once and then remove the seam also
        // from the accum matrix, it brought negative results.
//        Matrix energyMatrix = EnergyMatrixUtil.getEnergyMatrix(img, mEnergyType);
//        mSmcUtil = new SeamCarvingUtil(energyMatrix, true);

        // Horizontal seam removal
        for (i = 0; i < Math.abs(heightChange); i++) {
            Matrix energyMatrix = EnergyMatrixUtil.getEnergyMatrix(img, mEnergyType);
            mSmcUtil = new SeamCarvingUtil(energyMatrix);
            int[] seam = mSmcUtil.findLowestEnergySeam(false, true);
            //mSmcUtil.removeSeamFromAccumEnergyMatrix(seam, false);
            imagePixels = alterSeamFromImage(imageToPixels(img), seam, false, removeHorizontal);
            img = pixelsToImage(imagePixels);
            changedCou++;

            if (changedCou % reportEvery == 0) {
                System.out.format("Progress: %d%%%n", (int) (((float) changedCou / changesToMake) * 100));
            }
        }

        // Vertical seam removal
        for (i = 0; i < Math.abs(widthChange); i++) {
            Matrix energyMatrix = EnergyMatrixUtil.getEnergyMatrix(img, mEnergyType);
            mSmcUtil = new SeamCarvingUtil(energyMatrix);
            int[] seam = mSmcUtil.findLowestEnergySeam(true, true);
            //mSmcUtil.removeSeamFromAccumEnergyMatrix(seam, true);
            imagePixels = alterSeamFromImage(imageToPixels(img), seam, true, removeVertical);
            img = pixelsToImage(imagePixels);
            changedCou++;

            if (changedCou % reportEvery == 0) {
                System.out.format("Progress: %d%%%n", (int) (((float) changedCou / changesToMake) * 100));
            }
        }

        return img;

        //TODO: Need to make this better - it's very slow.
    }

    /**
     * Transforms BufferedImage to pixels 2d array.
     *
     * @param image Input BufferedImage.
     * @return 2d pixels array.
     */
    private int[][] imageToPixels(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        int[][] pixels = new int[height][width];
        for (int row = 0; row < height; row++) {
            image.getRGB(0, row, width, 1, pixels[row], 0, width);
        }
        return pixels;
    }

    /**
     * Transforms pixels 2d array to BufferedImage.
     *
     * @param pixels 2d pixels array.
     * @return Output BufferedImage.
     */
    public static BufferedImage pixelsToImage(int[][] pixels) {
        int width = pixels[0].length;
        int height = pixels.length;
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int row = 0; row < height; row++) {
            image.setRGB(0, row, width, 1, pixels[row], 0, width);
        }
        return image;
    }

    //TODO: Add method: addSeamToImage()

    private int[][] alterSeamFromImage(int[][] imagePixels, int[] seam, boolean isVertical, boolean isRemove) {

        int curHeight = imagePixels.length;
        int curWidth = imagePixels[0].length;
        int height = isRemove ? curHeight - 1 : curHeight + 1;
        int width = seam.length;
        if (isVertical) {
            height = seam.length;
            width = isRemove ? curWidth - 1 : curWidth + 1;
        }
        int[][] resizedImage = new int[height][width];
        int[][] alterPixels = new int[curHeight][curWidth];
        int i, j, k;

        if (isVertical) {
            // Mark which pixels to remove
            alterPixels = new int[curHeight][curWidth];
            for (i = 0; i < seam.length; i++) {
                j = seam[i];
                alterPixels[i][j] = 1;
            }
            // Remove the pixels by creating a new smaller image
            for (i = 0; i < curHeight; i++) {
                k = 0;
                for (j = 0; j < curWidth; j++) {
                    if (alterPixels[i][j] == 0) {
                        resizedImage[i][k++] = imagePixels[i][j];
                    } else if (!isRemove) {
                        // TODO: Roy - Blend the added seam by interpolation with its neighbors
                        resizedImage[i][k++] = imagePixels[i][j];
                        resizedImage[i][k++] = imagePixels[i][j];
                    }
                }
            }
        } else {
            // Mark which pixels to remove
            for (j = 0; j < seam.length; j++) {
                i = seam[j];
                alterPixels[i][j] = 1;
            }
            // Remove the pixels by creating a new smaller image
            for (j = 0; j < curWidth; j++) {
                k = 0;
                for (i = 0; i < curHeight; i++) {
                    if (alterPixels[i][j] == 0) {
                        resizedImage[k++][j] = imagePixels[i][j];
                    } else if (!isRemove) {
                        // TODO: Roy - Blend the added seam by interpolation with its neighbors
                        resizedImage[k++][j] = imagePixels[i][j];
                        resizedImage[k++][j] = imagePixels[i][j];
                    }
                }
            }
        }

        return resizedImage;
    }
}
