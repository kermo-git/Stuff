package graphics3D;

import java.util.Random;

public interface Function {
    public double f(double x, double y);
}

class Perlin implements Function {
    private static final int SIZE = 256, MASK = 255;
    private final int[] P = new int[2* SIZE];
    private final double[][] G = new double[SIZE][2];

    private int octaves;
    private double persistence;

    public Perlin(int octaves, double persistence) {
        this.octaves = octaves;
        this.persistence = persistence;

        Random random = new Random();
        int i, k, j;

        for (i = 0; i < SIZE; i++) {
            for (j = 0; j < 2; j++) {
                k = random.nextInt() & (SIZE + MASK);
                G[i][j] = (double)(k - SIZE)/SIZE;
            }
            normalize(G[i]);
            P[i] = i;
        }
        for (i = 0; i < SIZE; i++) {
            k = P[i];
            j = random.nextInt() & MASK;
            P[i] = P[j];
            P[j] = k;
        }
        for (i = 0; i < SIZE; i++) {
            P[SIZE + i] = P[i];
        }
    }
    public Perlin() {
        this(1, 1);
    }

    private static void normalize(double[] vec) {
        double x = vec[0], y = vec[1];
        double s = Math.sqrt(x*x + y*y);
        vec[0] = x/s;
        vec[1] = y/s;
    }

    private static double fade(double t) {
        return t*t*t*(t*(t*6 - 15) + 10);
    }

    private static double lerp(double t, double a, double b) {
        return a + t*(b - a);
    }

    private static int floor(double t) {
        return (t >= 0)? (int)t : (int)t - 1;
    }

    private int hash(int X, int Y) {
        return P[P[X] + Y];
    }

    private double grad(int hash, double x, double y) {
        double[] gradient = G[hash];
        return gradient[0]*x + gradient[1]*y;
    }

    private double noise(double x, double y) {
        int X0 = floor(x) & MASK;
        int Y0 = floor(y) & MASK;
        int X1 = (X0 + 1) & MASK;
        int Y1 = (Y0 + 1) & MASK;

        x -= floor(x);
        y -= floor(y);

        double s = grad(hash(X0, Y0), x,   y);
        double t = grad(hash(X1, Y0), x-1, y);
        double u = grad(hash(X0, Y1), x,   y-1);
        double v = grad(hash(X1, Y1), x-1, y-1);

        double Sx = fade(x);
        double Sy = fade(y);

        return lerp(Sy, lerp(Sx, s, t), lerp(Sx, u, v));
    }

    public double f(double x, double y) {
        double total = 0;
        double frequency = 1;
        double amplitude = 1;
        double max_value = 0;

        for (int i = 0; i < octaves; i++) {
            total += amplitude*noise(frequency*x, frequency*y);
            max_value += amplitude;
            amplitude *= persistence;
            frequency *= 2;
        }
        return total/max_value;
    }
}