// https://www.maths.tcd.ie/~fionn/misc/mt.php
// http://www.math.sci.hiroshima-u.ac.jp/~m-mat/MT/ARTICLES/mt.pdf

class MT19937{
    private static final int W = 32,
                             N = 624,
                             M = 397,
                             R = 31,
                             A = 0x9908B0DF,
                             U = 11,
                             D = 0xFFFFFFFF,
                             S = 7,
                             B = 0x9D2C5680,
                             T = 15,
                             C = 0xEFC60000,
                             L = 18;
    private static final long F = 1812433253;

    private static final int lower_mask = (1 << R) - 1;
    private static final int upper_mask = ~lower_mask;

    private int[] state;
    private int index;

    public MT19937(int seed) {
        state = new int[N]; index = N;
        state[0] = seed;

        for (int i = 1; i < N; i++) {
            int x = state[i-1];
            state[i] = (int)(F *(x^(x >>> (W - 2))) + i);
        }
    }

    public int nextInt() {
        if (index >= N)
            twist();

        int x = state[index];
        int y = x^((x >>> U) & D);
        y = y^((y << S) & B);
        y = y^((y << T) & C);
        index++;

        return y^(y >>> L);
    }

    private void twist() {
        for (int i = 0; i < N; i++) {
            state[i] = state[(i+ M)% N]^xA(concatenate(state[i], state[(i+1)% N]));
        }
        index = 0;
    }
    private int concatenate(int a, int b) {
        return (upper_mask & a)^(lower_mask & b);
    }
    private int xA(int x) {
        if (x%2 == 0)
            return x >>> 1;
        return (x >>> 1)^ A;
    }
}

class LCG{
    private long z;
    // m = 2147483647; a = 2147483629; c = 2147483587;
    // m = 2147483647; a = 742938285; c = 0;
    // m = 2147483647; a = 48271; c = 0;
    // m = 2147483647; a = 16807; c = 0;
    private static final int m = 2147483647;
    private static final int a = 2147483629;
    private static final int c = 2147483587;

    public LCG(long seed) {
        z = seed;
    }

    public int nextInt() {
        z = (a*z + c)%m;
        return (int)z;
    }
}

class XORshift_32{
    private int state;

    public XORshift_32(int seed) {
        state = seed;
    }

    public int nextInt() {
        state ^= state << 13;
        state ^= state >> 17;
        state ^= state << 5;
        return state;
    }
}

class XORshift_128{
    private int a;
    private int b;
    private int c;
    private int d;

    public XORshift_128(int seed) {
        a = seed;
        b = seed + 1;
        c = seed + 2;
        d = seed + 3;
    }

    public int nextInt() {
        int t = d;

        d = c;
        c = b;
        b = a;

        t ^= t << 11;
        t ^= t >> 8;
        a = t^a^(a >> 19);

        return a;
    }
}

// https://mzucker.github.io/html/perlin-noise-math-faq.html

class Perlin_1985 {
    private static final int TABLE_SIZE = 256, TABLE_MASK = 255;
    private final int[] P = new int[2*TABLE_SIZE];
    private final double[][] G = new double[TABLE_SIZE][2];
    private MT19937 generator;

    public Perlin_1985(int seed) {
        generator = new MT19937(seed);
        init();
    }

    private void normalize(double[] vec) {
        double x = vec[0], y = vec[1];
        double s = Math.sqrt(x*x + y*y);
        vec[0] = x/s;
        vec[1] = y/s;
    }

    private void init() {
        int i, k, j;
        for (i = 0; i < TABLE_SIZE; i++) {
            for (j = 0; j < 2; j++) {
                k = Math.abs(generator.nextInt());
                G[i][j] = (k%(2*TABLE_SIZE) - TABLE_SIZE)*1.0/TABLE_SIZE;
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

    private static double s_curve(double x) {
        return x*x*(3 - 2*x);
    }

    private static double lerp(double x, double a, double b) {
        return a + x*(b - a);
    }

    private static int floor(double x) {
        return (x >= 0)? (int)x : (int)x - 1;
    }

    private double influence(int grid_x, int grid_y, double vec_x, double vec_y) {
        double[] gradient = G[P[P[grid_x] + grid_y]];
        return gradient[0]*vec_x + gradient[1]*vec_y;
    }

    public double noise(double x, double y) {
        int floor_x = floor(x); double unit_x = x - floor_x;
        int floor_y = floor(y); double unit_y = y - floor_y;

        int x_0 = floor_x & TABLE_MASK;
        int y_0 = floor_y & TABLE_MASK;
        int x_1 = (x_0 + 1) & TABLE_MASK;
        int y_1 = (y_0 + 1) & TABLE_MASK;

        double s = influence(x_0, y_0, unit_x,   unit_y);
        double t = influence(x_1, y_0, unit_x-1, unit_y);
        double u = influence(x_0, y_1, unit_x,   unit_y-1);
        double v = influence(x_1, y_1, unit_x-1, unit_y-1);

        double S_x = s_curve(unit_x);
        double S_y = s_curve(unit_y);

        return lerp(S_y, lerp(S_x, s, t), lerp(S_x, u, v));
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

class Perlin_2002 {
    private static final int TABLE_SIZE = 256, TABLE_MASK = 255;
    private final int[] P = new int[2*TABLE_SIZE];
    private static final double[][] G = {{1, 1, 0}, {-1, 1, 0}, {1, -1, 0}, {-1, -1, 0},
                                         {1, 0, 1}, {-1, 0, 1}, {1, 0, -1}, {-1, 0, -1},
                                         {0, 1, 1}, {0, -1, 1}, {0, 1, -1}, {0, -1, -1},
                                         {1, 1, 0}, {-1, 1, 0}, {0, -1, 1}, {0, -1, -1}};

    private MT19937 generator;

    public Perlin_2002(int seed) {
        generator = new MT19937(seed);
        init();
    }

    private void init() {
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

    private double s_curve(double x) {
        return x*x*x*(x*(x*6 - 15) + 10);
    }

    private double lerp(double x, double a, double b) {
        return a + x*(b - a);
    }

    private static int floor(double x) {
        return (x >= 0)? (int)x : (int)x - 1;
    }

    private double influence(int grid_x, int grid_y, int grid_z, double vec_x, double vec_y, double vec_z) {
        double[] gradient = G[(P[P[P[grid_x] + grid_y] + grid_z]) & 15];
        return gradient[0]*vec_x +
               gradient[1]*vec_y +
               gradient[2]*vec_z;
    }

    public double noise(double x, double y, double z) {
        int floor_x = floor(x); double unit_x = x - floor_x;
        int floor_y = floor(y); double unit_y = y - floor_y;
        int floor_z = floor(z); double unit_z = z - floor_z;

        int x_0 = floor_x & TABLE_MASK;
        int y_0 = floor_y & TABLE_MASK;
        int z_0 = floor_z & TABLE_MASK;
        int x_1 = (x_0 + 1) & TABLE_MASK;
        int y_1 = (y_0 + 1) & TABLE_MASK;
        int z_1 = (z_0 + 1) & TABLE_MASK;

        double a = influence(x_0, y_0, z_0, unit_x,   unit_y,   unit_z);
        double b = influence(x_1, y_0, z_0, unit_x-1, unit_y,   unit_z);
        double c = influence(x_0, y_1, z_0, unit_x,   unit_y-1, unit_z);
        double d = influence(x_1, y_1, z_0, unit_x-1, unit_y-1, unit_z);
        double e = influence(x_0, y_0, z_1, unit_x,   unit_y,   unit_z-1);
        double f = influence(x_1, y_0, z_1, unit_x-1, unit_y,   unit_z-1);
        double g = influence(x_0, y_1, z_1, unit_x,   unit_y-1, unit_z-1);
        double h = influence(x_1, y_1, z_1, unit_x-1, unit_y-1, unit_z-1);

        double S_x = s_curve(unit_x);
        double S_y = s_curve(unit_y);
        double S_z = s_curve(unit_z);

        return lerp(S_z, lerp(S_y, lerp(S_x, a, b),
                                   lerp(S_x, c, d)),
                         lerp(S_y, lerp(S_x, e, f),
                                   lerp(S_x, g, h)));
    }
}