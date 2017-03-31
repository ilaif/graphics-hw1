package graphics.hw1;

/**
 * Created by ilaif on 31/03/2017.
 */
public class SeamCarvingUtil {

    Matrix MEnergyMatrix;

    public SeamCarvingUtil(Matrix energyMatrix, int isDiagonal) {
        MEnergyMatrix = energyMatrix;

        //TODO-Ray: Compute DP vertically down.

        //TODO-Roy: Compute DP in diagonal.
    }

    /**
     * @param k number of lowest energy seam carves
     */
    public double[][] getLowestEnergySeams(int k, boolean isVertical, boolean isDiagonal) {

        //TODO-Ilai: Given the computed DP matrix to find the lowest energy seam.
    }
}
