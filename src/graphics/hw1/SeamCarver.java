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

        boolean isDiagonal = true;

        int curWidth = mInputImage.getWidth();
        int curHeight = mInputImage.getHeight();

        int widthChange = width - curWidth;
        int heightChange = height - curHeight;

        // removeHorizontal is true if heightChange is negative, meaning we need to squeeze image, and positive otherwise.
        boolean removeHorizontal = (heightChange < 0);
        boolean removeVertical = (widthChange < 0);

        BufferedImage img = mInputImage;
        SeamCarvingUtil mSmcUtil;
        int changesToMake = Math.abs(heightChange) + Math.abs(widthChange);
        int currentCount, changesLeft, changes;

        System.out.format("Resizing image of size %dX%d to size %dX%d (Total of %d)\n", curWidth, curHeight,
                width, height, changesToMake);

        System.out.println("Progress: 0%");
        // NOTE: There was a test to calculate the energy and accum energy matrix once and then remove the seam also
        // from the accum matrix, it brought bad results.

        // Horizontal seam removal
        mSmcUtil = new SeamCarvingUtil(new Matrix(img).transpose().toBufferedImage(), mEnergyType);
        currentCount = 0;
        if (removeHorizontal) {
            mSmcUtil.removeSeams(heightChange, currentCount, changesToMake, isDiagonal);
            currentCount += heightChange;
        } else {
            changesLeft = heightChange;
            while (changesLeft > 0) {
                changes = Math.min(curHeight / 2, changesLeft);
                mSmcUtil.addSeams(changes, currentCount, changesToMake, isDiagonal);
                currentCount += changes;
                changesLeft -= changes;
            }
        }

        // Vertical seam removal (Transpose again)
        mSmcUtil = new SeamCarvingUtil(new Matrix(mSmcUtil.getImage()).transpose().toBufferedImage(), mEnergyType);
        if (removeVertical) {
            mSmcUtil.removeSeams(widthChange, currentCount, changesToMake, isDiagonal);
            currentCount += widthChange;
        } else {
            changesLeft = widthChange;
            while (changesLeft > 0) {
                changes = Math.min(curWidth / 2, changesLeft);
                mSmcUtil.addSeams(changes, currentCount, changesToMake, isDiagonal);
                currentCount += changes;
                changesLeft -= changes;
            }
        }

        mInputImage = mSmcUtil.getImage();
        return mInputImage;
    }

}
