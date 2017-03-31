package graphics.hw1;

public class SeamCarvingUtil {

    Matrix mEnergyMatrix;
    Matrix mAccumEnergyMatrix;

    public SeamCarvingUtil(Matrix energyMatrix, boolean isDiagonal) {
        mEnergyMatrix = energyMatrix;
        int width = energyMatrix.getN();
        int height = energyMatrix.getM();
        double min;

        Matrix accumEnergyMatrix = new Matrix(height, width);

        int y, x;
        for (y = 0; y < height; y++) {
            for (x = 0; x < width; x++) {
                min = Double.POSITIVE_INFINITY;
                if (y == 0) {
                    min = 0;
                } else {
                    if (x > 0) min = Math.min(accumEnergyMatrix.get(y - 1, x - 1), min);
                    min = Math.min(accumEnergyMatrix.get(y - 1, x), min);
                    if (x < width - 1) min = Math.min(accumEnergyMatrix.get(y - 1, x + 1), min);
                }
                accumEnergyMatrix.set(y, x, min + energyMatrix.get(y, x));
            }
        }

        mAccumEnergyMatrix = accumEnergyMatrix;

        //TODO-Roy: Test it and Compute DP vertically down (in addition to the current vertical version).
    }

    /**
     * @param isVertical
     */
    public int[] findLowestEnergySeam(boolean isVertical) {

        Matrix m = (isVertical) ? mAccumEnergyMatrix : mAccumEnergyMatrix.transpose();

        int height = m.getM();
        int width = m.getN();
        double[] lastRow = m.getRow(height - 1);
        int[] lowestSeam = new int[height];
        int j, i;

        double min = Double.POSITIVE_INFINITY;
        int minIndex = -1;
        for (j = 0; j < width; j++) {
            if (lastRow[j] < min) {
                min = lastRow[j];
                minIndex = j;
            }
        }

        j = minIndex;
        lowestSeam[height - 1] = j;
        for (i = height - 2; i >= 0; i--) {
            min = Double.POSITIVE_INFINITY;
            minIndex = 0;
            if (j > 0 && m.get(i, j - 1) < min) {
                minIndex = j - 1;
                min = m.get(i, j - 1);
            }
            if (m.get(i, j) < min) {
                minIndex = j;
                min = m.get(i, j);
            }
            if (j < width - 1 && m.get(i, j + 1) < min) {
                minIndex = j + 1;
                //min = m.get(i, j + 1);
            }

            lowestSeam[i] = minIndex;
            j = minIndex;
        }

        return lowestSeam;
    }

    //TODO-Ilai: Given the computed DP matrix to find the lowest energy seam.
}
