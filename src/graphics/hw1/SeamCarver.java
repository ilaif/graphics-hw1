package graphics.hw1;

import java.awt.image.BufferedImage;

/**
 *
 */
class SeamCarver {

    private EnergyType mEnergyType;
    private BufferedImage mInputImage;

    SeamCarver(BufferedImage inputImage, EnergyType energyType) {
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
    BufferedImage resize(int width, int height) {

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
        int changesToMake = Math.abs(heightChange) + Math.abs(widthChange);
        int reportEvery = changesToMake / 10;
        int changedCou = 0;
        int[] seam;

        System.out.format("Resizing image of size %dX%d to size %dX%d (Total of %d)\n", curWidth, curHeight,
                width, height, changesToMake);

        System.out.println("Progress: 0%");
        // NOTE: There was a test to calculate the energy and accum energy matrix once and then remove the seam also
        // from the accum matrix, it brought bad results.

        // Horizontal seam removal
        mSmcUtil = new SeamCarvingUtil(new Matrix(img).transpose().toBufferedImage(), mEnergyType);
        for (i = 0; i < Math.abs(heightChange); i++) {
            mSmcUtil.removeSeam();
            changedCou++;

            if (changedCou % reportEvery == 0) {
                System.out.format("Progress: %d%%%n", (int) (((float) changedCou / changesToMake) * 100));
            }
        }
        // Transpose again
        img = new Matrix(mSmcUtil.getImage()).transpose().toBufferedImage();

        // Vertical seam removal
        mSmcUtil = new SeamCarvingUtil(img, mEnergyType);
        for (i = 0; i < Math.abs(widthChange); i++) {
            seam = mSmcUtil.findLowestEnergySeam(true);
            mSmcUtil.removeSeam();
            changedCou++;

            if (changedCou % reportEvery == 0) {
                System.out.format("Progress: %d%%%n", (int) (((float) changedCou / changesToMake) * 100));
            }
        }

        mInputImage = mSmcUtil.getImage();

        return mInputImage;
        //TODO: Need to make this better - it's very slow.
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
