// https://www.iro.umontreal.ca/~lecuyer/myftp/papers/wellrng.pdf
// https://bitbucket.org/sergiu/random/src/tip/well.hpp

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