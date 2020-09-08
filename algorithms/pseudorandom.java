package algorithms;

/*
 * This is a very simple pseudorandom number generator, called linear congruential generator.
 * If seeded wih X(0), then the N'th "random" number in the sequence is defined as
 * X(N) = (X(N-1)*A + C) % M
 * where A, C and M are parameters.
 *
 * https://en.wikipedia.org/wiki/Linear_congruential_generator
 */
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

    public static LCG MINSTD(long seed) {
        return new LCG(seed, 48271, 0, (1L << 31) - 1);
    }

    public static LCG RANDU(long seed) {
        return new LCG(seed, 65539, 0, 1L << 31);
    }

    public static LCG javaRandom(long seed) {
        long A = 25214903917L, C = 11L, M = 1L << 48;
        long scrambled_seed = seed ^ A & (M - 1);

        LCG result = new LCG(scrambled_seed, A, C, M);
        result.SHIFT = 16;
        return result;
    }

    public static LCG ANSI_C(long seed) {
        LCG result = new LCG(seed, 1103515245L, 12345L, 1L << 31);
        result.SHIFT = 16; result.MASK = 0x7FFF;
        return result;
    }

    public static LCG MS_Visual_Cpp(long seed) {
        LCG result = new LCG(seed, 214013L, 2531011L, 1L << 31);
        result.SHIFT = 16; result.MASK = 0x7FFF;
        return result;
    }
}


/*
 * Another random number generator, called Mersenne Twister. It can generate
 * a sequence of 2^19937 pseudorandom numbers before the sequence repeats.
 *
 * https://www.maths.tcd.ie/~fionn/misc/mt.php
 */
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

    public int rand() {
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
        if ((x & 1) == 0)
            return x >>> 1;
        return (x >>> 1)^A;
    }
}


/*
 * A very complicated pseudorandom number generator. It's really over the top.
 *
 * https://www.iro.umontreal.ca/~lecuyer/myftp/papers/wellrng.pdf
 * https://bitbucket.org/sergiu/random/src/tip/well.hpp
 */
abstract class WELL {
    protected static final int
            W = 32,
            A1 = 0xDA442D24,
            A2 = 0xD3E43FFD,
            A3 = 0x8BDCB91E,
            A4 = 0x86A9D87E,
            A5 = 0xA8C296D1,
            A6 = 0x5D6B45CC,
            A7 = 0xB729FCEC;

    private int R, P, M1, M2, M3, UPPER_MASK, LOWER_MASK, state_i = 0;
    private int[] STATE;

    protected WELL(int r, int p, int m1, int m2, int m3) {
        R = r; P = p; M1 = m1; M2 = m2; M3 = m3;
        UPPER_MASK = ~0 << P;
        LOWER_MASK = ~UPPER_MASK;
        STATE = new int[R];
    }

    public void init(int[] seed) {
        for (int i = 0; i < seed.length; i++)
            STATE[i] = seed[i];
    }

    protected static int MAT0() { return 0; }
    protected static int MAT1(int x) { return x; }
    protected static int MAT2(int x, int t) {
        return (t >= 0)? x >>> t : x << -t;
    }
    protected static int MAT3(int x, int t) {
        return (t >= 0)? x^(x >>> t) : x^(x << -t);
    }
    protected static int MAT4(int x, int a) {
        return ((x & 1) == 1)?
                a^(x >>> 1) : x >>> 1;
    }
    protected static int MAT5(int x, int t, int b) {
        return (t >= 0)?
                x^((x >>> t) & b):
                x^((x << -t) & b);
    }
    protected static int MAT6(int x, int q, int a, int ds, int dt) {
        int result = ((x << q)^(x >>> (W - q))) & ds;
        return ((x & dt) != 0)? a^result : result;
    }
    protected static int TEMPER(int x, int b, int c) {
        x ^= (x << 7) & b;
        x ^= (x << 15) & c;
        return x;
    }
    private int map(int i) { return (state_i + i) % R; }


    protected abstract int T0(int x);
    protected abstract int T1(int x);
    protected abstract int T2(int x);
    protected abstract int T3(int x);
    protected abstract int T4(int x);
    protected abstract int T5(int x);
    protected abstract int T6(int x);
    protected abstract int T7(int x);

    public int rand() {
        int z0 = (STATE[map(R-1)] & UPPER_MASK) | (STATE[map(R-2)] & LOWER_MASK);
        int z1 = T0(STATE[state_i]) ^ T1(STATE[map(M1)]);
        int z2 = T2(STATE[map(M2)]) ^ T3(STATE[map(M3)]);
        int z3 = z1 ^ z2;
        int z4 = T4(z0) ^ T5(z1) ^ T6(z2) ^ T7(z3);

        STATE[state_i] = z3;
        state_i = map(R - 1);
        STATE[state_i] = z4;
        return z4;
    }
}

class WELL512a extends WELL {
    public WELL512a() { super(16, 0, 13, 9, 5); }
    
    protected int T0(int x) { return MAT3(x, -16); }
    protected int T1(int x) { return MAT3(x, -15); }
    protected int T2(int x) { return MAT3(x, 11); }
    protected int T3(int x) { return MAT0(); }
    protected int T4(int x) { return MAT3(x, -2); }
    protected int T5(int x) { return MAT3(x, -18); }
    protected int T6(int x) { return MAT2(x, -28); }
    protected int T7(int x) { return MAT5(x, -5, A1); }
}

class WELL1024a extends WELL {
    public WELL1024a() { super(32, 0, 3, 24, 10); }
    
    protected int T0(int x) { return MAT1(x); }
    protected int T1(int x) { return MAT3(x, 8); }
    protected int T2(int x) { return MAT3(x, -19); }
    protected int T3(int x) { return MAT3(x, -14); }
    protected int T4(int x) { return MAT3(x, -11); }
    protected int T5(int x) { return MAT3(x, -7); }
    protected int T6(int x) { return MAT3(x, -13); }
    protected int T7(int x) { return MAT0(); }
}

class WELL19937a extends WELL {
    public WELL19937a() { super(624, 31, 70, 179, 449); }
    
    protected int T0(int x) { return MAT3(x, -25); }
    protected int T1(int x) { return MAT3(x, 27); }
    protected int T2(int x) { return MAT2(x, 9); }
    protected int T3(int x) { return MAT3(x, 1); }
    protected int T4(int x) { return MAT1(x); }
    protected int T5(int x) { return MAT3(x, -9); }
    protected int T6(int x) { return MAT3(x, -21); }
    protected int T7(int x) { return MAT3(x, 21); }
}

class WELL19937c extends WELL19937a {
    public WELL19937c() { super(); }
    
    @Override
    public int rand() {
        return TEMPER(super.rand(), 0xE46E1700, 0x9B868000);
    }
}

class WELL23209b extends WELL {
    public WELL23209b() { super(726, 23, 610, 175, 662); }
    
    protected int T0(int x) { return MAT4(x, A5); }
    protected int T1(int x) { return MAT1(x); }
    protected int T2(int x) { return MAT6(x, 15, A6, 0xFFFEFFFF, 0x00000002); }
    protected int T3(int x) { return MAT3(x, -24); }
    protected int T4(int x) { return MAT3(x, -26); }
    protected int T5(int x) { return MAT1(x); }
    protected int T6(int x) { return MAT0(); }
    protected int T7(int x) { return MAT3(x, 16); }
}

class WELL44497a extends WELL {
    public WELL44497a() { super(1391, 15, 23, 481, 229); }
    
    protected int T0(int x) { return MAT3(x, -24); }
    protected int T1(int x) { return MAT3(x, 30); }
    protected int T2(int x) { return MAT3(x, -10); }
    protected int T3(int x) { return MAT2(x, -26); }
    protected int T4(int x) { return MAT1(x); }
    protected int T5(int x) { return MAT3(x, 20); }
    protected int T6(int x) { return MAT6(x, 9, A7, 0xFBFFFFFF, 0x00020000); }
    protected int T7(int x) { return MAT1(x); }
}

class WELL44497b extends WELL44497a {
    public WELL44497b() { super(); }
    
    @Override
    public int rand() {
        return TEMPER(super.rand(), 0x93DD1400, 0xFA118000);
    }
}
