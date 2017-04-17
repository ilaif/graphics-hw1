package graphics.hw1;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

final public class Matrix {
    private final int M;             // number of rows
    private final int N;             // number of columns
    private final double[][] data;   // M-by-N array

    // create M-by-N matrix of 0's
    public Matrix(int M, int N) {
        this.M = M;
        this.N = N;
        this.data = new double[M][N];
    }

    // create matrix based on 2d array
    public Matrix(double[][] data, boolean useExisting) {
        this.M = data.length;
        this.N = data[0].length;
        if (useExisting) {
            this.data = data;
        } else {
            this.data = new double[M][N];
            for (int i = 0; i < M; i++)
                System.arraycopy(data[i], 0, this.data[i], 0, N);
        }
    }

    /**
     * Transforms BufferedImage to Matrix.
     *
     * @param image BufferedImage.
     */
    public Matrix(BufferedImage image) {
        this.N = image.getWidth();
        this.M = image.getHeight();
        this.data = new double[this.M][this.N];

        for (int row = 0; row < this.M; row++) {
            for (int col = 0; col < this.N; col++) {
                this.set(row, col, image.getRGB(col, row));
            }
        }
    }

    public double get(int i, int j) {
        return this.data[i][j];
    }

    public void set(int i, int j, double value) {
        this.data[i][j] = value;
    }

    public int getN() {
        return this.N;
    }

    public int getM() {
        return this.M;
    }

    public double[] getRow(int n) {
        return this.data[n];
    }

    // create and return the transpose of the invoking matrix
    public Matrix transpose() {
        Matrix A = new Matrix(N, M);
        for (int i = 0; i < M; i++)
            for (int j = 0; j < N; j++)
                A.data[j][i] = this.data[i][j];
        return A;
    }

    // return C = A + B
    public Matrix plus(Matrix B) {
        Matrix A = this;
        if (B.M != A.M || B.N != A.N) throw new RuntimeException("Illegal matrix dimensions.");
        Matrix C = new Matrix(M, N);
        for (int i = 0; i < M; i++)
            for (int j = 0; j < N; j++)
                C.data[i][j] = A.data[i][j] + B.data[i][j];
        return C;
    }

    // return C = A - B
    public Matrix minus(Matrix B) {
        Matrix A = this;
        if (B.M != A.M || B.N != A.N) throw new RuntimeException("Illegal matrix dimensions.");
        Matrix C = new Matrix(M, N);
        for (int i = 0; i < M; i++)
            for (int j = 0; j < N; j++)
                C.data[i][j] = A.data[i][j] - B.data[i][j];
        return C;
    }

    // print matrix to standard output
    public void show() {
        for (int i = 0; i < M; i++) {
            for (int j = 0; j < N; j++)
                System.out.printf("%9.4f ", data[i][j]);
            System.out.println();
        }
    }

    public Matrix removeSeam(int[] seam) {
        int curHeight = this.getM();
        int curWidth = this.getN();
        int height = seam.length;
        int width = curWidth - 1;
        double[][] resizedImage = new double[height][width];
        int i, j, k;

        // Remove the pixels by creating a new smaller image
        for (i = 0; i < curHeight; i++) {
            k = 0;
            for (j = 0; j < curWidth; j++) {
                if (j != seam[i]) {
                    resizedImage[i][k++] = this.data[i][j];
                }
            }
        }

        return new Matrix(resizedImage, true);
    }

    public int averageColors(ArrayList<Integer> argbColors) {
        Color c;
        int size = argbColors.size();
        double alphaMean = 0, redMean = 0, greenMean = 0, blueMean = 0;
        for (int rgb : argbColors) {
            c = new Color(rgb);
            alphaMean += c.getAlpha();
            redMean += c.getRed();
            greenMean += c.getGreen();
            blueMean += c.getBlue();
        }
        alphaMean /= size;
        redMean /= size;
        greenMean /= size;
        blueMean /= size;

        return new Color((int) redMean, (int) greenMean, (int) blueMean, (int) alphaMean).getRGB();
    }

    public Matrix addSeam(int[] seam) {
        int curHeight = this.getM();
        int curWidth = this.getN();
        int height = seam.length;
        int width = curWidth + 1;
        double[][] resizedImage = new double[height][width];
        int i, j, k;
        ArrayList<Integer> colors;

        //TODO: Roy - What about the indices?

        for (i = 0; i < curHeight; i++) {
            k = 0;
            for (j = 0; j < curWidth; j++) {
                if (j != seam[i]) {
                    resizedImage[i][k++] = this.data[i][j];
                } else {
                    // TODO: Roy - Make sure the interpolation works

                    colors = new ArrayList<>();
                    colors.add((int) this.data[i][j]);
                    if (j > 0) {
                        colors.add((int) this.data[i][j - 1]);
                    }

                    resizedImage[i][k++] = averageColors(colors);
                    resizedImage[i][k++] = this.data[i][j];
                }
            }
        }

        return new Matrix(resizedImage, true);
    }

    /**
     * Transforms matrix to BufferedImage.
     *
     * @return Output BufferedImage.
     */
    public BufferedImage toBufferedImage() {
        int width = this.getN();
        int height = this.getM();
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                image.setRGB(col, row, (int) this.get(row, col));
            }
        }
        return image;
    }
}
