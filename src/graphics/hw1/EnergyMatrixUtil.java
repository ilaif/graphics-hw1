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

//    static Matrix getEnergyMatrix(BufferedImage inImg, EnergyType energyType) {
        //Roy - CHECKED(NOTES: ENTROPY CASE GOES FIRST CALCULATING ENTROPY MATRIX, AND THEN DEFAULT CASE OVERRIDES
        //THE MATRIX? SHOULD CHANGE THE ORDER/LOGIC OF CASES)
        //SEE getEnergyMatrix001 METHOD
        //I CHANGED "REGULER" TO "REGULAR"


//        Matrix m = getGradientMatrix(inImg);
//
//        switch (energyType) {
//            case ENTROPY:
//                // m = m.plus(getEntropyMatrix(inImg));...
//                break;
//            case FORWARD_ENERGY:
//                // m = getForwardEnergyMatrix(inImg);...
//                break;
//            case REGULAR:
//            default:
//                m = getGradientMatrix(inImg);
//                break;
//        }
//
//
//
//        return m;
//
//    }

    static Matrix getEnergyMatrix(BufferedImage inImg, EnergyType energyType){


        Matrix mEnergyMatrix, mEntropyMatrix;
        mEnergyMatrix=getGradientMatrix(inImg);

        switch(energyType) {
            case FORWARD_ENERGY:
                //WILL BE ADDED LATER
                break;
            case ENTROPY:
                mEntropyMatrix=getEntropyMatrix(inImg);
                mEnergyMatrix = mEnergyMatrix.plus(mEntropyMatrix);
            default://==case REGULAR
                break;
        }
        return(mEnergyMatrix);

    }

//    private static Matrix getGradientMatrix(BufferedImage inImg) {
//
//        int width = inImg.getWidth();
//        int height = inImg.getHeight();
//        int y, x, sumDiff, neighbours, i, j;
//        double diff;
//        Matrix m = new Matrix(height, width);
//
//        for (x = 0; x < width; x++) {
//            for (y = 0; y < height; y++) {
//                sumDiff = 0;
//                neighbours = 0;
//                for (i = -1; i <= 1; i++) {
//                    for (j = -1; j <= 1; j++) {
//                        diff = getDiff(inImg, x, y, i, j);
//                        if (diff > -1) {
//                            sumDiff += diff;
//                            neighbours += 1;
//                        }
//                    }
//                }
//                m.set(y, x, (sumDiff / neighbours));
//            }
//        }
//
//        return m;
//
//
//    }

    private static Matrix getGradientMatrix(BufferedImage inImg){
        int width = inImg.getWidth();
        int height = inImg.getHeight();
        int neighbours;
        double sumDiff, normalizedGradient;
        Matrix mGradientMatrix = new Matrix(height, width);

        for(int x=0; x<width ; x++) {
            for (int y = 0; y < height; y++) {
                neighbours=-1;
                sumDiff=0;
                for(int dx=Math.max(x-1,0); dx<Math.min(x+1,width);dx++)
                    for(int dy=Math.max(y-1,0); dy<Math.min(y+1,height);dy++) {
                        neighbours++;
                        sumDiff+=getDiff(inImg,x,y,dx,dy);
                    }
                normalizedGradient=(sumDiff/neighbours);
                mGradientMatrix.set(y,x,normalizedGradient);
            }
        }

    return(mGradientMatrix);
    }


    private static double getDiff(BufferedImage img, int x1, int y1, int x2, int y2) {

//        if (x2 < 0 || x2 >= img.getWidth() || y2 < 0 || y2 >= img.getHeight()) {
//            return -1;
//        }

        Color c1 = new Color(img.getRGB(x1, y1));
        Color c2 = new Color(img.getRGB(x2, y2));
        return (Math.abs(c1.getRed() - c2.getRed()) +
                Math.abs(c1.getBlue() - c2.getBlue()) +
                Math.abs(c1.getGreen() - c2.getGreen()))
                / 3;

    }

    private static Matrix getEntropyMatrix(BufferedImage inImg) {
        //calculating the entropy matrix of each pixel. using getPmdMatrix.

        int width = inImg.getWidth();
        int height = inImg.getHeight();
        Matrix mEntropyMatrix = new Matrix(height,width);
        Matrix mPmnMatrix=getPmnMatrix(inImg);
        double neighboursEntropy, entropy, pmn;
        for(int x=0; x<width; x++) {
            for (int y = 0; y < height; y++) {
                neighboursEntropy=0;
                for (int k = Math.max(x - 4, 0); k < Math.min(x + 4, width); k++) {
                    for (int j = Math.max(y - 4, 0); j < Math.min(y + 4, height); j++) {
                        pmn=mPmnMatrix.get(j,k);
                        neighboursEntropy-=(pmn*Math.log(pmn));

                    }
                }
                entropy=neighboursEntropy;
                mEntropyMatrix.set(y,x,entropy);
            }

        }
        return(mEntropyMatrix);
    }



    private static Matrix getPmnMatrix(BufferedImage inImg) {
    //calculating the "Pmn" matrix = each pixel replaced with  the grayscale value friction out of the
    //11X11 pixel window surrounding him grayscale values sum. using getGrayScaleMatrix.

        int width = inImg.getWidth();
        int height = inImg.getHeight();
        double gray, sumNeighboursGray,pmn;
        Matrix mPmnMatrix = new Matrix(height,width);
        Matrix mGrayScaleMatrix=getGrayScaleMatrix(inImg);

        for(int x=0; x<width; x++){
            for (int y = 0; y < height; y++) {
                sumNeighboursGray=0;
                gray=mGrayScaleMatrix.get(y,x);
                for(int k=Math.max(x-4,0); k<Math.min(x+4,width); k++){
                    for(int j=Math.max(y-4,0); j<Math.min(y+4,height); j++) {
                        sumNeighboursGray += mGrayScaleMatrix.get(j, k);
                    }
                }
                pmn=gray/sumNeighboursGray;
                mPmnMatrix.set(y,x,pmn);
            }
        }
        return(mPmnMatrix);
    }


    private static Matrix getGrayScaleMatrix(BufferedImage inImg) {
    //calculates grayscale matrix using getGrayScale function.
        int width = inImg.getWidth();
        int height = inImg.getHeight();
        Matrix mGrayScaleMatrix=new Matrix(height,width);
        double gray;

        for(int x=0; x<width; x++){
            for (int y = 0; y < height; y++) {
                gray=getGrayScale(inImg,x,y);
                mGrayScaleMatrix.set(y,x,gray);
            }
        }
        return(mGrayScaleMatrix);

    }

    private static double getGrayScale(BufferedImage inImg,int x,int y) {
        //calculates the grayscale value using avarage of rgb values.

        int rgb,r,g,b;
        double gray;
        rgb = inImg.getRGB(x, y);

        r = (rgb >> 16) & 0xFF;
        g = (rgb >> 8) & 0xFF;
        b = (rgb & 0xFF);
        gray = (r + g + b) / 3;
        return(gray);
    }
}


