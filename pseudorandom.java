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

class rand48 {
    private static long
            M = (1L << 48) - 1,
            A = 25214903917L,
            C = 11L;
    private long seed;

    public rand48(long seed) {
        this.seed = seed ^ A & M;
    }

    // Reference: if seed = 3, the following sequence is generated:
    //-1155099828, -1879439976, 304908421, -836442134, 288278256, -1795872892, -995758198, -1824734168, 976394918, -634239373, ...
    public int rand() {
        seed = (seed* A + C) & M;
        return (int) (seed >>> 16);
    }
}

class MS_Visual_C_rand {
    private static final long
            M = 1L << 31,
            A = 214013L,
            C = 2531011L;
    private long seed;

    public MS_Visual_C_rand(long seed) { this.seed = seed; }

    // Reference: if seed = 3, the following sequence is generated:
    // 48, 7196, 9294, 9091, 7031, 23577, 17702, 23503, 27217, 12168, ...
    public int rand() {
        seed = (seed*A + C) % M;            // same as (seed*A + C) & (M-1)
        long result = (seed/65536) % 32768; // same as (seed >>> 16) & 32767
        return (int)result;
    }
}

class ANSI_C_rand {
    private static final long
            M = 1L << 31,
            A = 1103515245L,
            C = 12345L;
    private long seed;

    public ANSI_C_rand(long seed) { this.seed = seed; }

    // Reference: if seed = 3, the following sequence is generated:
    // 17747, 7107, 10365, 8312, 20622, ...
    public int rand() {
        seed = (seed*A + C) % M;           // same as (seed*A + C) & (M-1)
        long result = (seed/65536) % 32768; // same as (seed >>> 16) & 32767
        return (int)result;
    }
}

class MINSTD {
    private static final long
            M = 2147483647,
            A = 16807; // 48271
    private long seed;
    public MINSTD(long seed) {this.seed = seed;}
    public int rand() {
        seed = (seed*A) % M;
        return (int)seed;
    }
}

class RANDU {
    private static final long
            M = 1L << 31,
            A = 65539;
    private long seed;
    public RANDU(long seed) {this.seed = seed;}
    public int rand() {
        seed = (seed*A) % M;
        return (int)seed;
    }
}
