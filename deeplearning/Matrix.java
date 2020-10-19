package deeplearning;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.awt.image.BufferedImage;


/**
 * A numerical matrix supporting up to 4 dimensions. It stores all the values
 * as a 1D array in {@code output}. As a deep learning utility, it also stores
 * derivatives of the cost function w.r.t itself (a.k.a "gradients") in {@code gradients}.
 */
public class Matrix {
    protected int dim0, dim1, dim2, dim3;
    protected double[] value;
    protected double[] gradient;


    public Matrix() {}

    public Matrix(Matrix other) {
        init(other.dim0, other.dim1, other.dim2, other.dim3);
        System.arraycopy(other.value, 0, value, 0, value.length);
    }
    public Matrix(int dim0, int dim1, int dim2, int dim3) {
        init(dim0, dim1, dim2, dim3);
    }
    public Matrix(int dim0, int dim1, int dim2) {
        init(dim0, dim1, dim2, 1);
    }
    public Matrix(int dim0, int dim1) {
        init(dim0, dim1, 1, 1);
    }
    public Matrix(int dim0) {
        init(dim0, 1, 1, 1);
    }


    protected void init(int dim0, int dim1, int dim2, int dim3) {
        if (!(dim0 > 0 && dim1 > 0 && dim2 > 0 && dim3 > 0)) {
            throw new IllegalArgumentException();
        }
        this.dim0 = dim0;
        this.dim1 = dim1;
        this.dim2 = dim2;
        this.dim3 = dim3;

        value = new double[dim0*dim1*dim2*dim3];
        gradient = new double[value.length];
    }
    protected void init(int dim0, int dim1, int dim2) {
        init(dim0, dim1, dim2, 1);
    }
    protected void init(int dim0, int dim1) {
        init(dim0, dim1, 1, 1);
    }
    protected void init(int dim0) {
        init(dim0, 1, 1, 1);
    }


    public int getDim0() { return dim0; }
    public int getDim1() { return dim1; }
    public int getDim2() { return dim2; }
    public int getDim3() { return dim3; }


    public int indexHash(int x, int y, int z, int w) {
        return x + dim0*(y + dim1*(z + dim2*w));
    }
    public int indexHash(int x, int y, int z) {
        return x + dim0*(y + dim1*z);
    }
    public int indexHash(int x, int y) {
        return x + dim0*y;
    }
    

    public double getValue(int hash) {
        if (hash >= 0 && hash < value.length)
            return value[hash];
        throw new IllegalArgumentException();
    }
    public void setValue(int hash, double value) {
        if (hash >= 0 && hash < this.value.length)
            this.value[hash] = value;
        else
            throw new IllegalArgumentException();
    }


    public void update(double learningRate) {
        for (int i = 0; i < value.length; i++)
            value[i] -= learningRate * gradient[i];

        gradient = new double[value.length];
    }
    public void reset() {
        value = new double[value.length];
        gradient = new double[value.length];
    }
    public void resetGradient() {
        gradient = new double[value.length];
    }


    /*
     * STORING DATA
     */


    public void storeVector(int sample, double[] vector, int timeStep) {
        if (vector.length == dim1) {
        if (sample >= 0 && sample < dim0) {
        if (timeStep >= 0 && timeStep < dim2) {

            for (int i = 0; i < vector.length; i++) {
                value[indexHash(sample, i, timeStep)] = vector[i];
            }
            return;
        }}}
        throw new IllegalArgumentException();
    }
    public void storeVector(int sample, double[] vector) {
        storeVector(sample, vector, 0);
    }


    public void storeVector(int sample, Collection<? extends Number> vector, int timeStep) {
        double[] temp = new double[vector.size()];

        int i = 0;
        for (Number d : vector) {
            temp[i] = d.doubleValue();
            i++;
        }
        storeVector(sample, temp, timeStep);
    }
    public void storeVector(int sample, Collection<? extends Number> vector) {
        storeVector(sample, vector, 0);
    }


    public Matrix(List<BufferedImage> images) {
        int width = images.get(0).getWidth();
        int height = images.get(0).getHeight();
        init(images.size(), 3, width, height);

        BufferedImage img;
        int RGB, red, green, blue;

        for (int sample = 0; sample < images.size(); sample++) {
            img = images.get(sample);

            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    RGB = img.getRGB(x, y);

                    red = (RGB >> 16) & 0xFF;
                    green = (RGB >> 8) & 0xFF;
                    blue = RGB & 0xFF;

                    value[indexHash(sample, 0, x, y)] = red;
                    value[indexHash(sample, 1, x, y)] = green;
                    value[indexHash(sample, 2, x, y)] = blue;
                }
            }
        }
    }


    /*
     * FILLING WITH RANDOM VALUES
     */


    public void fill(double value) {
        Arrays.fill(this.value, value);
    }
    public void gaussianFill(double mean, double std_dev) {
        Random random = new Random();
        for (int i = 0; i < value.length; i++)
            value[i] = random.nextGaussian()*std_dev + mean;
    }
    public void uniformFill(int a, int b) {
        Random random = new Random();
        for (int i = 0; i < value.length; i++)
            value[i] = a + random.nextInt(b - a + 1);
    }
    public void uniformFill(double a, double b) {
        Random random = new Random();
        for (int i = 0; i < value.length; i++)
            value[i] = a + random.nextDouble()*b;
    }
    public void binaryFill(double p) {
        if (p < 0 || p > 1)
            throw new IllegalArgumentException();

        Random random = new Random();
        for (int i = 0; i < value.length; i++)
            value[i] = (random.nextDouble() <= p)? 1 : 0;
    }


    /*
     * GENERATING RANDOM DATASETS
     */


    public static Matrix randomImages(int numImages, int width, int height) {
        Random random = new Random();
        Matrix images = new Matrix(numImages, 3, width, height);

        for (int i = 0; i < images.value.length; i++) {
            images.value[i] = random.nextInt(256);
        }
        return images;
    }

    
    public static Matrix randomOneHots(int samples, int classes, int timeSteps) {
        Random random = new Random();
        Matrix data = new Matrix(samples, classes, timeSteps);

        for (int s = 0; s < samples; s++) {
            for (int t = 0; t < timeSteps; t++) {
                int c = random.nextInt(classes);
                int hash = data.indexHash(s, c, t);
                data.value[hash] = 1;
            }
        }
        return data;
    }
    public static Matrix randomOneHots(int samples, int classes) {
        return randomOneHots(samples, classes, 1);
    }


    @Override
    public String toString() {
        int max_len = 0;

        for (double d : value) {
            if (Double.toString(d).length() > max_len)
                max_len = Double.toString(d).length();
        }

        StringBuilder dashed_line = new StringBuilder();
        for (int a0 = 0; a0 < dim0 *(max_len + 2); a0++)
            dashed_line.append('-');
        dashed_line.append('\n');

        StringBuilder sb = new StringBuilder();
        if (dim3 > 1)
            sb.append(dashed_line);

        int index = 0;

        for (int a3 = 0; a3 < dim3; a3++) {
            for (int a2 = 0; a2 < dim2; a2++) {
                for (int a1 = 0; a1 < dim1; a1++) {

                    sb.append("[");
                    for (int a0 = 0; a0 < dim0; a0++) {
                        String str = Double.toString(value[index]);

                        for (int l = 0; l < max_len - str.length(); l++) {
                            sb.append(" ");
                        }
                        sb.append(str);
                        if (a0 < dim0 - 1)
                            sb.append(", ");
                        index++;
                    }
                    sb.append("]\n");
                }
                if (a2 < dim2 - 1)
                    sb.append('\n');
            }
            if (dim3 > 1)
                sb.append(dashed_line);
        }
        return sb.toString();
    }
}