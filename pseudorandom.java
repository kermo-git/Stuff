// https://www.maths.tcd.ie/~fionn/misc/mt.php
// https://www.ibm.com/support/knowledgecenter/en/SSLVMB_22.0.0/com.ibm.spss.statistics.algorithms/alg_random-numbers_mersenne.htm
// http://www.math.sci.hiroshima-u.ac.jp/~m-mat/MT/ARTICLES/mt.pdf

class MT19937 {
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
                             L = 18,
                             F = 1812433253,
                             LOWER_MASK = (1 << R) - 1,
                             UPPER_MASK = ~LOWER_MASK;

    private final int[] state = new int[N];
    private int index = N;

    public MT19937(int seed) {
        state[0] = seed;

        for (int i = 1; i < N; i++) {
            int x = state[i-1];
            state[i] = F*(x^(x >>> (W - 2))) + i;
        }
    }

    public int nextInt() {
        if (index >= N)
            twist();
        
        int x = state[index];
        index++;
        
        x = x^((x >>> U) & D);
        x = x^((x << S) & B);
        x = x^((x << T) & C);
        return x^(x >>> L);
    }

    private void twist() {
        for (int i = 0; i < N; i++) {
            state[i] = state[(i+M)%N]^xA(concatenate(state[i], state[(i+1)%N]));
        }
        index = 0;
    }

    private static int concatenate(int a, int b) {
        return (UPPER_MASK & a)^(LOWER_MASK & b);
    }

    private static int xA(int x) {
        if (x & 1 == 0)
            return x >>> 1;
        return (x >>> 1)^A;
    }
}


class LCG {
    private long seed, A, C, M;
    private int SHIFT = 0, MASK = 0xFFFFFFFF;

    private LCG(long seed, long A, long C, long M) {
        this.seed = seed; this.A = A; this.C = C; this.M = M;
    }

    public int rand() {
        seed = (seed*A + C) % M;
        return (int) ((seed >>> SHIFT) & MASK);
    }

    public static LCG MINSTD0 (long seed) {
        return new LCG(seed, 16807, 0, (1L << 31) - 1);
    }

    public static LCG MINSTD (long seed) {
        return new LCG(seed, 48271, 0, (1L << 31) - 1);
    }

    // http://physics.ucsc.edu/~peter/115/randu.pdf
    public static LCG RANDU(long seed) {
        return new LCG(seed, 65539, 0, 1L << 31);
    }

    public static LCG rand48(long seed) {
        long A = 25214903917L, C = 11L, M = 1L << 48;
        long scrambled_seed = seed ^ A & (M - 1);

        LCG result = new LCG(scrambled_seed, A, C, M);
        result.SHIFT = 16;
        return result;
    }

    public static LCG ANSI_C_standard(long seed) {
        // Reference for seed = 3 : 17747, 7107, 10365, 8312, 20622, ...
        LCG result = new LCG(seed, 1103515245L, 12345L, 1L << 31);
        result.SHIFT = 16; result.MASK = 0x7FFF;
        return result;
    }

    public static LCG MS_Visual_C(long seed) {
        // Reference for seed = 3 : 48, 7196, 9294, 9091, 7031, 23577, 17702, 23503, 27217, 12168, ...
        long unsigned_32_bit = seed & 0xFFFFFFFFL;
        
        LCG result = new LCG(unsigned_32_bit, 214013L, 2531011L, 1L << 31);
        result.SHIFT = 16; result.MASK = 0x7FFF;
        return result;
    }
}
