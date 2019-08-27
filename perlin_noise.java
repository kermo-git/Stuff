import java.util.Random

// https://mzucker.github.io/html/perlin-noise-math-faq.html
// https://www.scratchapixel.com/lessons/procedural-generation-virtual-worlds/perlin-noise-part-2
// https://www.mrl.nyu.edu/~perlin/doc/oscar.html#noise

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

// https://flafla2.github.io/2014/08/09/perlinnoise.html
// https://mrl.nyu.edu/~perlin/paper445.pdf
// https://mrl.nyu.edu/~perlin/noise/

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
        hash = hash & 15;
        double u = (hash < 8)? x : y;
        double v = (hash < 4)? y : (hash==12 || hash==14)? x : z;
        return ((hash & 1) == 0 ? u : -u) + ((hash & 2) == 0 ? v : -v);
    }

    public static double noise(double x, double y, double z) {
        int X0 = floor(x) & 255;
        int Y0 = floor(y) & 255;
        int Z0 = floor(z) & 255;
        int X1 = X0 + 1;
        int Y1 = Y0 + 1;
        int Z1 = Z0 + 1;

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
