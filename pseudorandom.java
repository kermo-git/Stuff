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
    private int[] state;
    private int index;

    public MT19937(int seed) {
        state = new int[N]; index = N;
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
        
        int y = x^((x >>> U) & D);
        y = y^((y << S) & B);
        y = y^((y << T) & C);
        return y^(y >>> L);
    }

    private void twist() {
        for (int i = 0; i < N; i++) {
            state[i] = state[(i+M)%N]^xA(concatenate(state[i], state[(i+1)%N]));
        }
        index = 0;
    }

    private int concatenate(int a, int b) {
        return (UPPER_MASK & a)^(LOWER_MASK & b);
    }

    private int xA(int x) {
        if (x%2 == 0)
            return x >>> 1;
        return (x >>> 1)^A;
    }
}

class LCG{
    private long Z;
    // private static final int M = 2147483647, A = 2147483629, C = 2147483587;
    // private static final int M = 2147483647, A = 742938285, C = 0;
    // private static final int M = 2147483647, A = 48271, C = 0;
    // private static final int M = 2147483647, A = 16807, C = 0;
    private static final int M = 2147483647, A = 2147483629, C = 2147483587;

    public LCG(long seed) {
        Z = seed;
    }

    public int nextInt() {
        Z = (A * Z + C)% M;
        return (int) Z;
    }
}

class XORshift_32{
    private int state;

    public XORshift_32(int seed) {
        state = seed;
    }

    public int nextInt() {
        state ^= state << 13;
        state ^= state >>> 17;
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
        t ^= t >>> 8;
        a = t^a^(a >>> 19);

        return a;
    }
}
