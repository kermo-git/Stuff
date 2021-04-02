package graphics3D;

import java.awt.image.BufferedImage;

import graphics3D.noise.*;

public class PixelArt {
    public static double[][] circularGradient(int numPixelsX, int numPixelsY) {
        double[][] result = new double[numPixelsX][numPixelsY];
        int centerX = numPixelsX / 2;
        int centerY = numPixelsY / 2;
        double fromCornerToCenter = Math.sqrt(centerX * centerX + centerY * centerY);

        for (int x = 0; x < numPixelsX; x ++) {
            for (int y = 0; y < numPixelsY; y ++) {
                int toCenterX = Math.abs(centerX - x);
                int toCenterY = Math.abs(centerY - x);
                double distanceToCenter = Math.sqrt(toCenterX * toCenterX + toCenterY * toCenterY);
                result[x][y] = distanceToCenter / fromCornerToCenter;
            }
        }
        return result;
    }

    public static BufferedImage island(int octaves, int units) {
        Noise noise = new FractalNoise(new SimplexNoise(), octaves, 0.3);

        int numPixelsX = (int) Math.pow(2, octaves);
        int numPixelsY = (int) Math.pow(2, octaves);

        Color[][] result = Color.getArray((int) Math.pow(2, octaves), (int) Math.pow(2, octaves));
        double[][] filter = circularGradient(numPixelsX, numPixelsY);

        Color color = null; double elevation;

        for (int x = 0; x < numPixelsX; x ++) {
            for (int y = 0; y < numPixelsY; y ++) {
                elevation = noise.signedNoise(
                    1.0 * x * units / numPixelsX, 
                    1.0 * y * units / numPixelsX, 
                    0
                );
                elevation *= filter[x][y];
                if (elevation < 0) {
                    color = new Color(0x041B7A);
                }
                else if (elevation < 0.05) {
                    color = new Color(0xFFDA85);
                }
                else if (elevation < 0.5) {
                    color = new Color(0x007A3B);
                }
                else if (elevation < 0.8) {
                    color = new Color(0x909090);
                }
                else {
                    color = new Color(0xF2F2F2);
                }
                result[x][y] = color;
            }
        }
        return Color.matrixToImage(result);
    }
}
