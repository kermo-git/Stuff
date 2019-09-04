/**
 * A Java implementation of Ken Perlin's Simplex noise function, based mostly on his
 * 
 * <a href="https://www.csee.umbc.edu/~olano/s2002c36/ch02.pdf">
 * original implementation
 * </a>.
 * 
 * Some parts of the code are taken from
 * 
 * <a href="http://staffwww.itn.liu.se/~stegu/simplexnoise/simplexnoise.pdf">
 * Stefan Gustafson's implementation
 * </a>.
 * 
 * This code gives exactly the same results as the original one, but it's more readable and commented.
 *
 * @author Kermo Saarse
 * @since 27.08.2019
 */
class Simplex {

    /**
     * Don't let anyone instantiate this class.
     */
    private Simplex() {}

    /**
     * Absolute {@code int} coordinates of the simplex origin in (i, j, k)-space.
     */
    private static int i, j, k;

    /**
     * {@code double} components of the distance vector from the 
     * (simplex origin -> point being evaluated) in (x, y, z)-space.
     */
    private static double u, v, w;

    /**
     * 3D noise function. Given a point in (x, y, z) space, it returns
     * a pseudo-random noise value in the range [-1, 1].
     *
     * @param x an x-coordinate
     * @param y a y-coordinate
     * @param z a z-coordinate
     * @return the noise value at (x, y, z) in the range [-1, 1]
     */
    public static double noise(double x, double y, double z) {
        // Skew the input coordinates to (i, j, k)-space and find the origin of the current simplex.
        double s = (x + y + z)/3.0;
        i = floor(x + s);
        j = floor(y + s);
        k = floor(z + s);
        // Unskew the origin back to (x, y, z) space and find the distance vector (origin -> input point)
        s = (i + j + k)/6.0;
        u = x - i + s;
        v = y - j + s;
        w = z - k + s;
        
        /* Determine which simplex we're in. This means finding the coordinates for each of the simplex corners 
        relative to the origin corner in (i, j, k)-space. The origin itself is (0, 0, 0) and the rest are 
        (i1, j1, k1), (i2, j2, k2) and (1, 1, 1). This part is based on Stefan Gustafson's code.*/
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
        // Calculate noise contributions from every corner.
        double n0 = influence(0, 0, 0);
        double n1 = influence(i1, j1, k1);
        double n2 = influence(i2, j2, k2);
        double n3 = influence(1, 1, 1);
        // Add all the contributions together and scale the result to [-1, 1].
        return 3*(n0 + n1 + n2 + n3);
    }

    /**
     * Creates 3D fractal noise by adding multiple noises together. Each subsequent noise, called
     * an octave, has twice the frequency of the previous octave. This method is taken from
     * <a href="https://flafla2.github.io/2014/08/09/perlinnoise.html"> here </a>.
     *
     * @param x an x-coordinate
     * @param y a y-coordinate
     * @param z a z-coordinate
     * @param octaves the number of octaves
     * @param persistence For octave i, amplitude = persistence^i
     * @return the noise value at (x, y, z) in the range [-1, 1]
     */
    public static double fractal_noise(double x, double y, double z, int octaves, double persistence) {
        double total = 0;
        double frequency = 1;
        double amplitude = 1;
        double max_value = 0;

        for (int i = 0; i < octaves; i++) {
            total += amplitude*noise(frequency*x, frequency*y, frequency*z);
            max_value += amplitude;
            amplitude *= persistence;
            frequency *= 2;
        }
        return total/max_value;
    }

    /**
     * This is a lot faster than using {@code (int) Math.floor(t)}. Taken
     * from Stephan Gustafson's implementation.
     *
     * @param t a {@code double} value
     * @return the largest (closest to positive infinity) {@code int} value
     *         that is less than or equal to t
     */
    private static int floor(double t) {
        return (t >= 0)? (int)t : (int)t - 1;
    }

    /**
     * Calculates the noise contribution from a simplex corner, given its coordinates
     * relative to the origin in (i, j, k) space.
     *
     * @param di the i-coordinate of the corner
     * @param dj the j-coordinate of the corner
     * @param dk the k-coordinate of the corner
     * @return the result
     */
    private static double influence(int di, int dj, int dk) {
        // Skew the input coordinates to (x, y, z)-space and find the distance vector
        // (corner -> point being evaluated)
        double s = (di + dj + dk)/6.0;
        double x = u - di + s;
        double y = v - dj + s;
        double z = w - dk + s;
        
        // Calculate the result
        double t = 0.6 - (x*x + y*y + z*z);
        if (t < 0) return 0;
        // Find the absolute coordinates of the corner in (i, j, k)-space and calculate its hashed gradient index.
        int h = hash(i + di, j + dj, k + dk);
        return 8*t*t*t*t*grad(h, x, y, z);
    }

    /**
     * Using a hashed gradient index of a simplex corner, it picks a gradient vector from
     * the following set of vectors: <br><br>
	 *
     * (1, 1, 0), (-1, 1, 0), (1, -1, 0), (-1, -1, 0) <br>
     * (0, 1, 1), (0, -1, 1), (0, 1, -1), (0, -1, -1) <br>
     * (1, 0, 1), (-1, 0, 1), (1, 0, -1), (-1, 0, -1) <br><br>
	 *
     * It then calculates the dot product between the gradient vector and the distance
     * vector (corner -> point being evaluated, (x, y, z)-space) given as an argument.
     *
     * @param hash the hashed gradient index
     * @param x the x-component of the distance vector
     * @param y the y-component of the distance vector
     * @param z the z-component of the distance vector
     * @return the dot product
     */
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

    /**
     * Finds the pseudo-random gradient index for a corner using its absolute coordinates
     * in (i, j, k)-space.
     *
     * @param i the i-coordinate of the corner
     * @param j the j-coordinate of the corner
     * @param k the k-coordinate of the corner
     * @return the hash value.
     */
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
