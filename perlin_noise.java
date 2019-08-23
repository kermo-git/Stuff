import java.util.Random;

// https://mzucker.github.io/html/perlin-noise-math-faq.html

class Perlin_1985 {
    private static final int TABLE_SIZE = 256, TABLE_MASK = 255;
    private final int[] P = new int[2*TABLE_SIZE];
    private final double[][] G = new double[TABLE_SIZE][2];
    private Random generator;

    public Perlin_1985() {
        generator = new Random();
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
        int floor_x = floor(x);
        int floor_y = floor(y);
        
        double unit_x = x - floor_x;
        double unit_y = y - floor_y;

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
    private Random generator;

    public Perlin_2002() {
        generator = new Random();
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
        int hash = P[P[P[grid_x] + grid_y] + grid_z] & 15;
        switch (hash) {
            case 0: return    vec_x + vec_y;
            case 1: return    vec_x + vec_z;
            case 2: return    vec_z + vec_y;
            case 3: return  - vec_x + vec_y;
            case 4: return  - vec_x + vec_z;
            case 5: return  - vec_z + vec_y;
            case 6: return    vec_x - vec_y;
            case 7: return    vec_x - vec_z;
            case 8: return    vec_z - vec_y;
            case 9: return  - vec_x - vec_y;
            case 10: return - vec_x - vec_z;
            case 11: return - vec_z - vec_y;
            case 12: return   vec_x + vec_y;
            case 13: return - vec_x + vec_y;
            case 14: return - vec_z + vec_y;
            case 15: return - vec_z - vec_y;
        }
        return 0;
    }

    public double noise(double x, double y, double z) {
        int floor_x = floor(x); 
        int floor_y = floor(y); 
        int floor_z = floor(z);
        
        double unit_x = x - floor_x;
        double unit_y = y - floor_y;
        double unit_z = z - floor_z;

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
