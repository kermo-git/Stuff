import java.util.Random

/**
 * An algorithm for generating smooth noise by Ken Perlin. Method {@code noise(double x, double y)}
 * returns a random value between 0 and 1. Coordinates (x and y) that are close to each other have
 * similar noise values. Method {@code fractal_noise(double x, double y, int octaves, double persistence)} 
 * adds multiple instances of the noise ("octaves") together, each subsequent noise having with twice
 * the frequency than the previous one. The noise can be visualised by using generated numbers as greyscale
 * values and plotting them on a 2D image using the given coordinates. The noise repeats after every 256 units.
 *
 * https://mzucker.github.io/html/perlin-noise-math-faq.html
 * https://www.scratchapixel.com/lessons/procedural-generation-virtual-worlds/perlin-noise-part-2
 */
class Perlin_1985 {
    private static final int SIZE = 256, MASK = 255;
    private final int[] P = new int[2* SIZE];
    private final double[][] G = new double[SIZE][2];

    public Perlin_1985() {
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

    private int hash(int X, int Y) {
        return P[P[X] + Y];
    }

    private double grad(int hash, double x, double y) {
        double[] gradient = G[hash];
        return gradient[0]*x + gradient[1]*y;
    }

    public double noise(double x, double y) {
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

    public double fractal_noise(double x, double y, int octaves, double persistence) {
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


/**
 * This class uses a slightly improved algorithm to generate smooth noise in 3D space, so {@code noise(double, double, double)}
 * requires 3 coordinates.
 *
 * https://flafla2.github.io/2014/08/09/perlinnoise.html
 * https://mrl.nyu.edu/~perlin/noise/
 */
class Perlin_2002 {
    private static final int[] P = new int[512];
    private static final int[] permutation = { 151,160,137,91,90,15,
            131,13,201,95,96,53,194,233,7,225,140,36,103,30,69,142,8,99,37,240,21,10,23,
            190, 6,148,247,120,234,75,0,26,197,62,94,252,219,203,117,35,11,32,57,177,33,
            88,237,149,56,87,174,20,125,136,171,168, 68,175,74,165,71,134,139,48,27,166,
            77,146,158,231,83,111,229,122,60,211,133,230,220,105,92,41,55,46,245,40,244,
            102,143,54, 65,25,63,161, 1,216,80,73,209,76,132,187,208, 89,18,169,200,196,
            135,130,116,188,159,86,164,100,109,198,173,186, 3,64,52,217,226,250,124,123,
            5,202,38,147,118,126,255,82,85,212,207,206,59,227,47,16,58,17,182,189,28,42,
            223,183,170,213,119,248,152, 2,44,154,163, 70,221,153,101,155,167, 43,172,9,
            129,22,39,253, 19,98,108,110,79,113,224,232,178,185, 112,104,218,246,97,228,
            251,34,242,193,238,210,144,12,191,179,162,241, 81,51,145,235,249,14,239,107,
            49,192,214, 31,181,199,106,157,184, 84,204,176,115,121,50,45,127, 4,150,254,
            138,236,205,93,222,114,67,29,24,72,243,141,128,195,78,66,215,61,156,180 };

    static { for (int i = 0; i < 256 ; i++) P[256 + i] = P[i] = permutation[i]; }

    private static int[][] G = {{1, 1, 0}, {-1, 1, 0}, {1, -1, 0}, {-1, -1, 0},
                                {1, 0, 1}, {-1, 0, 1}, {1, 0, -1}, {-1, 0, -1},
                                {0, 1, 1}, {0, -1, 1}, {0, 1, -1}, {0, -1, -1},
                                {1, 1, 0}, {0, -1, 1}, {-1, 1, 0}, {0, -1, -1}};

    private Perlin_2002() {}

    private static double fade(double t) {
        return t*t*t*(t*(t*6 - 15) + 10);
    }

    private static double lerp(double t, double a, double b) {
        return a + t*(b - a);
    }

    private static int floor(double t) {
        return (t >= 0)? (int)t : (int)t - 1;
    }

    private static int hash(int X, int Y, int Z) {
        return P[P[P[X] + Y] + Z];
    }

    private static double grad(int hash, double x, double y, double z) {
        int[] gradient = G[hash & 15];
        return gradient[0]*x + gradient[1]*y + gradient[2]*z;
    }

    public static double noise(double x, double y, double z) {
        int X0 = floor(x) & 255;
        int Y0 = floor(y) & 255;
        int Z0 = floor(z) & 255;
        int X1 = (X0 + 1) & 255;
        int Y1 = (Y0 + 1) & 255;
        int Z1 = (Z0 + 1) & 255;

        x -= floor(x);
        y -= floor(y);
        z -= floor(z);

        double a = grad(hash(X0, Y0, Z0), x,   y,   z);
        double b = grad(hash(X1, Y0, Z0), x-1, y,   z);
        double c = grad(hash(X0, Y1, Z0), x,   y-1, z);
        double d = grad(hash(X1, Y1, Z0), x-1, y-1, z);
        double e = grad(hash(X0, Y0, Z1), x,   y,   z-1);
        double f = grad(hash(X1, Y0, Z1), x-1, y,   z-1);
        double g = grad(hash(X0, Y1, Z1), x,   y-1, z-1);
        double h = grad(hash(X1, Y1, Z1), x-1, y-1, z-1);

        double Sx = fade(x);
        double Sy = fade(y);
        double Sz = fade(z);

        return lerp(Sz, lerp(Sy, lerp(Sx, a, b),
                                 lerp(Sx, c, d)),
                        lerp(Sy, lerp(Sx, e, f),
                                 lerp(Sx, g, h)));
    }
}


/**
 * A completely different algorithm for generating 3D noise also invented by Ken Perlin.
 *
 * http://staffwww.itn.liu.se/~stegu/simplexnoise/simplexnoise.pdf
 * https://www.csee.umbc.edu/~olano/s2002c36/ch02.pdf
 */
class Simplex {
    private static int i, j, k;
    private static double u, v, w;
    private Simplex() {}

    public static double noise(double x, double y, double z) {
        double s = (x + y + z)/3.0;
        i = floor(x + s);
        j = floor(y + s);
        k = floor(z + s);
        
        s = (i + j + k)/6.0;
        u = x - i + s;
        v = y - j + s;
        w = z - k + s;
        
        int i1, j1, k1, i2, j2, k2;

        if (u > v) {
            if (v > w) {
                i1 = 1; j1 = 0; k1 = 0; i2 = 1; j2 = 1; k2 = 0;
            } else if (w > u) {
                i1 = 0; j1 = 0; k1 = 1; i2 = 1; j2 = 0; k2 = 1;
            } else {
                i1 = 1; j1 = 0; k1 = 0; i2 = 1; j2 = 0; k2 = 1;
            }
        } else {
            if (w < u) {
                i1 = 0; j1 = 1; k1 = 0; i2 = 1; j2 = 1; k2 = 0;
            } else if (w > v) {
                i1 = 0; j1 = 0; k1 = 1; i2 = 0; j2 = 1; k2 = 1;
            } else {
                i1 = 0; j1 = 1; k1 = 0; i2 = 0; j2 = 1; k2 = 1;
            }
        }
        
        double n0 = influence(0, 0, 0);
        double n1 = influence(i1, j1, k1);
        double n2 = influence(i2, j2, k2);
        double n3 = influence(1, 1, 1);
        
        return 3*(n0 + n1 + n2 + n3);
    }

    private static int floor(double t) {
        return (t >= 0)? (int)t : (int)t - 1;
    }

    private static double influence(int di, int dj, int dk) {
        double s = (di + dj + dk)/6.0;
        double x = u - di + s;
        double y = v - dj + s;
        double z = w - dk + s;

        double t = 0.6 - (x*x + y*y + z*z);
        if (t < 0) return 0;

        int h = hash(i + di, j + dj, k + dk);
        return 8*t*t*t*t*grad(h, x, y, z);
    }

    private static double grad(int hash, double x, double y, double z) {
        int b10 = hash & 3,
            b2 = bit(hash, 2), b3 = bit(hash, 3),
            b4 = bit(hash, 4), b5 = bit(hash, 5);

        double p = (b10 == 1)? x : (b10 == 2)? y : z;
        double q = (b10 == 1)? y : (b10 == 2)? z : x;
        double r = (b10 == 1)? z : (b10 == 2)? x : y;

        p = (b5 == b3)? -p : p;
        q = (b5 == b4)? -q : q;
        r = (b5 != (b4 ^ b3))? -r : r;
        
        return p + ((b10 == 0)? q + r : (b2 == 0)? q : r);
    }

    private static int hash(int i, int j, int k) {
        return b(i, j, k, 0) + b(j, k, i, 1) + b(k, i, j, 2) + b(i, j, k, 3) +
               b(j, k, i, 4) + b(k, i, j, 5) + b(i, j, k, 6) + b(j, k, i, 7);
    }
    private static int bit(int i, int b) {
        return (i >> b) & 1;
    }
    private static int b(int i, int j, int k, int B) {
        return BIT_PATTERNS[(bit(i, B) << 2) | (bit(j, B) << 1) | bit(k, B)];
    }
    private static final int[] BIT_PATTERNS = {0x15, 0x38, 0x32, 0x2C, 0x0D, 0x13, 0x07, 0x2A};
}
