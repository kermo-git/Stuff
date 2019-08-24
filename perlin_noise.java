import java.util.Random;

// https://mzucker.github.io/html/perlin-noise-math-faq.html
// https://www.scratchapixel.com/lessons/procedural-generation-virtual-worlds/perlin-noise-part-2

class Perlin_1985 {
    private static final int TABLE_SIZE = 256, TABLE_MASK = 255;
    private final int[] P = new int[2*TABLE_SIZE];
    private final double[][] G = new double[TABLE_SIZE][2];
    private Random generator = new Random();

    public Perlin_1985() {
        int i, k, j;
        for (i = 0; i < TABLE_SIZE; i++) {
            for (j = 0; j < 2; j++) {
                k = generator.nextInt() & (2*TABLE_SIZE-1);
                G[i][j] = (k - TABLE_SIZE)*1.0/TABLE_SIZE;
            }
            normalize(G[i]);
            P[i] = i;
        }
        for (i = 0; i < TABLE_SIZE; i++) {
            k = P[i];
            j = generator.nextInt() & TABLE_MASK;
            P[i] = P[j];
            P[j] = k;
        }
        for (i = 0; i < TABLE_SIZE; i++) {
            P[TABLE_SIZE + i] = P[i];
        }
    }

    private static void normalize(double[] vec) {
        double x = vec[0], y = vec[1];
        double s = Math.sqrt(x*x + y*y);
        vec[0] = x/s;
        vec[1] = y/s;
    }

    private static double fade(double t) {
        return t*t*(3 - 2*t);
    }

    private static double lerp(double t, double a, double b) {
        return a + t*(b - a);
    }

    private static int floor(double t) {
        return (t >= 0)? (int)t : (int)t - 1;
    }

    private double influence(int Xi, int Yi, double x, double y) {
        double[] grad = G[P[P[Xi] + Yi]];
        return grad[0]*x + grad[1]*y;
    }

    public double noise(double x, double y) {
        int X0 = floor(x) & TABLE_MASK;
        int Y0 = floor(y) & TABLE_MASK;
        int X1 = (X0 + 1) & TABLE_MASK;
        int Y1 = (Y0 + 1) & TABLE_MASK;

        x -= floor(x);
        y -= floor(y);

        double s = influence(X0, Y0, x,   y);
        double t = influence(X1, Y0, x-1, y);
        double u = influence(X0, Y1, x,   y-1);
        double v = influence(X1, Y1, x-1, y-1);

        double Sx = fade(x);
        double Sy = fade(y);

        return lerp(Sy, lerp(Sx, s, t), lerp(Sx, u, v));
    }

    public double octave_noise(double x, double y, int octaves, double persistence) {
        double total = 0;
        double frequency = 1;
        double amplitude = 1;
        double max_value = 0;

        for (int i = 0; i < octaves; i++) {
            double noise = (noise(frequency*x, frequency*y) + 1)/2;
            total += amplitude*noise;
            max_value += amplitude;
            amplitude *= persistence;
            frequency *= 2;
        }
        return total/max_value;
    }
}

// https://mrl.nyu.edu/~perlin/paper445.pdf
// https://flafla2.github.io/2014/08/09/perlinnoise.html

class Perlin_2002 {
    private static final int TABLE_SIZE = 256, TABLE_MASK = 255;
    private final int[] P = new int[2*TABLE_SIZE];
    private Random generator = new Random();

    public Perlin_2002() {
        int i, k, j;
        for (i = 0; i < TABLE_SIZE; i++) {
            P[i] = i;
        }
        for (i = 0; i < TABLE_SIZE; i++) {
            k = P[i];
            j = generator.nextInt() & TABLE_MASK;
            P[i] = P[j];
            P[j] = k;
        }
        for (i = 0; i < TABLE_SIZE; i++) {
            P[TABLE_SIZE + i] = P[i];
        }
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

    private double influence(int Xi, int Yi, int Zi, double x, double y, double z) {
        int hash = P[P[P[Xi] + Yi] + Zi] & 15;
        switch (hash) {
            case 0:  return   x + y;
            case 1:  return - x + y;
            case 2:  return   x - y;
            case 3:  return - x - y;
            case 4:  return   x + z;
            case 5:  return - x + z;
            case 6:  return   x - z;
            case 7:  return - x - z;
            case 8:  return   y + z;
            case 9:  return - y + z;
            case 10: return   y - z;
            case 11: return - y - z;
            case 12: return   y + x;
            case 13: return - y + z;
            case 14: return   y - x;
            case 15: return - y - z;
            default: return 0;
        }
    }

    public double noise(double x, double y, double z) {
        int X0 = floor(x) & TABLE_MASK;
        int Y0 = floor(y) & TABLE_MASK;
        int Z0 = floor(z) & TABLE_MASK;
        int X1 = (X0 + 1) & TABLE_MASK;
        int Y1 = (Y0 + 1) & TABLE_MASK;
        int Z1 = (Z0 + 1) & TABLE_MASK;

        x -= floor(x);
        y -= floor(y);
        z -= floor(z);

        double a = influence(X0, Y0, Z0, x,   y,   z);
        double b = influence(X1, Y0, Z0, x-1, y,   z);
        double c = influence(X0, Y1, Z0, x,   y-1, z);
        double d = influence(X1, Y1, Z0, x-1, y-1, z);
        double e = influence(X0, Y0, Z1, x,   y,   z-1);
        double f = influence(X1, Y0, Z1, x-1, y,   z-1);
        double g = influence(X0, Y1, Z1, x,   y-1, z-1);
        double h = influence(X1, Y1, Z1, x-1, y-1, z-1);

        double Sx = fade(x);
        double Sy = fade(y);
        double Sz = fade(z);

        return lerp(Sz, lerp(Sy, lerp(Sx, a, b),
                                 lerp(Sx, c, d)),
                        lerp(Sy, lerp(Sx, e, f),
                                 lerp(Sx, g, h)));
    }
}
