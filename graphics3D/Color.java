package graphics3D;

import java.awt.image.BufferedImage;

public class Color extends Vector {
    public Color() {}
    public Color(double red, double green, double blue) {
        super(red, green, blue);
    }
    public Color(int RGBhex) {
        int redInt = (RGBhex >> 16) & 0xFF;
        int greenInt = (RGBhex >> 8) & 0xFF;
        int blueInt = (RGBhex >> 0) & 0xFF;

        x = redInt / 255.0;
        y = greenInt / 255.0;
        z = blueInt / 255.0;
    }
    public Color(Color other) {
        super(other);
    }
    public Color getScaled(double scalar) {
        return new Color(
            x * scalar,
            y * scalar,
            z * scalar
        );
    }
    public int getRGBhex() {
        if (x < 0) x = 0;
        if (x > 1) x = 1;
        if (y < 0) y = 0;
        if (y > 1) y = 1;
        if (z < 0) z = 0;
        if (z > 1) z = 1;

        int redInt = (int) (x * 255);
        int greenInt = (int) (y * 255);
        int blueInt = (int) (z * 255);
        return redInt << 16 | greenInt << 8 | blueInt;
    }


    public static Color[][] getArray(int numPixelsX, int numPixelsY) {
        Color[][] result = new Color[numPixelsX][numPixelsY];

        for (int x = 0; x < numPixelsX; x++) {
            for (int y = 0; y < numPixelsY; y++) {
                result[x][y] = new Color();
            }
        }
        return result;
    }


    public static BufferedImage colorMatToReducedImg(Color[][] matrix) {
        int numPixelsX = matrix.length;
        int numPixelsY = matrix[0].length;

        BufferedImage result = new BufferedImage(
            numPixelsX / 2, 
            numPixelsY / 2, 
            BufferedImage.TYPE_INT_RGB
        );

        Color color;
        int resultX = 0, resultY = 0;

        for (int x = 0; x < numPixelsX; x += 2) {
            resultY = 0;
            for (int y = 0; y < numPixelsY; y += 2) {
                color = new Color();
                color.add(matrix[x    ][y    ]);
                color.add(matrix[x + 1][y    ]);
                color.add(matrix[x    ][y + 1]);
                color.add(matrix[x + 1][y + 1]);
                color.scale(0.25);

                result.setRGB(resultX, resultY, color.getRGBhex());
                resultY++;
            }
            resultX++;
        }
        return result;
    }


    public static BufferedImage colorMatToImg(Color[][] matrix) {
        int numPixelsX = matrix.length;
        int numPixelsY = matrix[0].length;

        BufferedImage result = new BufferedImage(
            numPixelsX, 
            numPixelsY, 
            BufferedImage.TYPE_INT_RGB
        );

        for (int x = 0; x < numPixelsX; x ++) {
            for (int y = 0; y < numPixelsY; y ++) {
                result.setRGB(x, y, matrix[x][y].getRGBhex());
            }
        }
        return result;
    }


    public static BufferedImage matToGreyScaleImg(double[][] matrix) {
        int numPixelsX = matrix.length;
        int numPixelsY = matrix[0].length;

        BufferedImage result = new BufferedImage(
            numPixelsX, 
            numPixelsY, 
            BufferedImage.TYPE_INT_RGB
        );
        int x, y;
        double min = Double.MAX_VALUE;
        double max = Double.MIN_VALUE;
        double value;

        for (x = 0; x < numPixelsX; x++) {
            for (y = 0; y < numPixelsY; y++) {
                value = matrix[x][y];
                if (value > max) {
                    max = value;
                }
                if (value < min) {
                    min = value;
                }
            }
        }
        double diff = max - min;
        for (x = 0; x < numPixelsX; x++) {
            for (y = 0; y < numPixelsY; y++) {
                double norm = (matrix[x][y] - min) / diff;
                result.setRGB(x, y, new Color(norm, norm, norm).getRGBhex());
            }
        }
        return result;
    }


    public static BufferedImage matToReducedGreyScaleImg(double[][] matrix) {
        int doublePixelsX = matrix.length;
        int doublePixelsY = matrix[0].length;

        int numPixelsX = doublePixelsX / 2;
        int numPixelsY = doublePixelsY / 2;

        BufferedImage result = new BufferedImage(
            numPixelsX, 
            numPixelsY, 
            BufferedImage.TYPE_INT_RGB
        );
        double[][] reducedMatrix = new double[numPixelsX][numPixelsY];

        int x, y;
        double min = Double.MAX_VALUE;
        double max = Double.MIN_VALUE;
        double value;

        for (x = 0; x < doublePixelsX; x += 2) {
            for (y = 0; y < doublePixelsY; y += 2) {
                value = (
                    matrix[x    ][y    ] + 
                    matrix[x    ][y + 1] +
                    matrix[x + 1][y    ] + 
                    matrix[x + 1][y + 1]
                ) * 0.25;

                if (value > max) {
                    max = value;
                }
                if (value < min) {
                    min = value;
                }
                reducedMatrix[x / 2][y / 2] = value;
            }
        }
        double diff = max - min;

        for (x = 0; x < numPixelsX; x++) {
            for (y = 0; y < numPixelsY; y++) {
                double norm = (reducedMatrix[x][y] - min) / diff;
                result.setRGB(x, y, new Color(norm, norm, norm).getRGBhex());
            }
        }
        return result;
    }
}