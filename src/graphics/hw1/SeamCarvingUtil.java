package graphics.hw1;

import java.awt.image.BufferedImage;

class SeamCarvingUtil {

    private Matrix mEnergyMatrix;
    private Matrix mAccumEnergyMatrix;
    private BufferedImage mImage;
    private EnergyType mEnergyType;

    /**
     * Constructor that builds the accumulated energy matrix given an energy matrix.
     */
    SeamCarvingUtil(BufferedImage img, EnergyType energyType) {
        mImage = img;
        mEnergyType = energyType;

        // Get initial energy matrix
        mEnergyMatrix = EnergyMatrixUtil.getEnergyMatrix(img, mEnergyType);

        this.updateAccumulatedEnergyMatrix();
        //TODO-Roy: Test it and Compute DP vertically down (in addition to the current vertical version).
    }

    /**
     * Finds lowest seam in the accumulated energy matrix
     *
     * @param isDiagonal Whether we are computing accumulation allowing diagonal minimization or just vertical lines.
     * @return lowest energy seam.
     */
    int[] findLowestEnergySeam(boolean isDiagonal) {
        Matrix m = mAccumEnergyMatrix;

        int height = m.getM();
        int width = m.getN();
        int[] lowestSeam = new int[height];
        int j, i;
        double min = Double.POSITIVE_INFINITY;
        int minIndex = -1;

        // First find the minimum energy in the last row.
        double[] lastRow = m.getRow(height - 1);
        for (j = 0; j < width; j++) {
            if (lastRow[j] < min) {
                min = lastRow[j];
                minIndex = j;
            }
        }

        // Iterate upwards from the last row, to find the full minimum energy seam.
        j = minIndex;
        lowestSeam[height - 1] = j;
        for (i = height - 2; i >= 0; i--) {
            min = Double.POSITIVE_INFINITY;
            minIndex = 0;
            if (j > 0 && m.get(i, j - 1) < min && isDiagonal) {
                minIndex = j - 1;
                min = m.get(i, j - 1);
            }
            if (m.get(i, j) < min) {
                minIndex = j;
                min = m.get(i, j);
            }
            if (j < width - 1 && m.get(i, j + 1) < min && isDiagonal) {
                minIndex = j + 1;
            }

            lowestSeam[i] = minIndex;
            j = minIndex;
        }

        return lowestSeam;
    }

    void updateAccumulatedEnergyMatrix() {
        int width = mEnergyMatrix.getN();
        int height = mEnergyMatrix.getM();
        double min;

        Matrix accumEnergyMatrix = new Matrix(height, width);

        int y, x;
        for (y = 0; y < height; y++) {
            for (x = 0; x < width; x++) {
                min = Double.POSITIVE_INFINITY;
                if (y == 0) { // If we are in the first row then we don't add previous rows
                    min = 0;
                } else {
                    if (x > 0) min = Math.min(accumEnergyMatrix.get(y - 1, x - 1), min);
                    min = Math.min(accumEnergyMatrix.get(y - 1, x), min);
                    if (x < width - 1) min = Math.min(accumEnergyMatrix.get(y - 1, x + 1), min);
                }

                accumEnergyMatrix.set(y, x, mEnergyMatrix.get(y, x) + min);
            }
        }

        this.mAccumEnergyMatrix = accumEnergyMatrix;
    }

    void removeSeams(int seamRemoveAmount, int currentCount, int changesToMake) {
        int i, reportEvery = changesToMake / 10;
        int seam[];

        for (i = 0; i < Math.abs(seamRemoveAmount); i++) {
            seam = findLowestEnergySeam(true);
            mImage = new Matrix(mImage).removeSeam(seam).toBufferedImage();
            mEnergyMatrix = mEnergyMatrix.removeSeam(seam);
            mEnergyMatrix = EnergyMatrixUtil.updateEnergyMatrix(mImage, mEnergyMatrix, seam, mEnergyType);
            this.updateAccumulatedEnergyMatrix();

            currentCount++;
            if (currentCount % reportEvery == 0) {
                System.out.format("Progress: %d%%%n", (int) (((float) currentCount / changesToMake) * 100));
            }
        }
    }

    void addSeams(int seamAddAmount, int currentCount, int changesToMake) {
        int i, reportEvery = changesToMake / 10;
        int seams[][] = new int[seamAddAmount][];

        Matrix originalImageMatrix = new Matrix(mImage);

        for (i = 0; i < Math.abs(seamAddAmount); i++) {
            seams[i] = findLowestEnergySeam(true);
            mImage = new Matrix(mImage).removeSeam(seams[i]).toBufferedImage();
            mEnergyMatrix = mEnergyMatrix.removeSeam(seams[i]);
            mEnergyMatrix = EnergyMatrixUtil.updateEnergyMatrix(mImage, mEnergyMatrix, seams[i], mEnergyType);
            this.updateAccumulatedEnergyMatrix();

            currentCount++;
            if (currentCount % reportEvery == 0) {
                System.out.format("Progress: %d%%%n", (int) (((float) currentCount / changesToMake) * 100));
            }
        }

        for (i = 0; i < Math.abs(seamAddAmount); i++) {
            originalImageMatrix = originalImageMatrix.addSeam(seams[i]);
        }

        mImage = originalImageMatrix.toBufferedImage();
        mEnergyMatrix = EnergyMatrixUtil.getEnergyMatrix(mImage, mEnergyType);
        this.updateAccumulatedEnergyMatrix();
    }

    BufferedImage getImage() {
        return mImage;
    }
}
