// http://www.iro.umontreal.ca/~panneton/WELLRNG.html
// https://bitbucket.org/sergiu/random/src/tip/well.hpp

abstract class WELL {
    protected static final int W = 32,
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
        UPPER_MASK = 0xFFFFFFFF >>> (W - P);
        LOWER_MASK = ~UPPER_MASK;
        STATE = new int[R];
    }

    public void init(int[] seed) {
        for (int i = 0; i < seed.length; i++)
            STATE[i] = seed[i];
    }

    private int map(int i) { return (state_i + i) % R; }

    protected static int MATRIX_0() { return 0; }
    protected static int MATRIX_1(int x) { return x; }
    protected static int MATRIX_2(int x, int t) {
        return (t >= 0)? x >>> t : x << -t;
    }
    protected static int MATRIX_3(int x, int t) {
        return (t >= 0)? x^(x >>> t) : x^(x << -t);
    }
    protected static int MATRIX_4(int x, int a) {
        return ((x & 1) == 0)?
                a^(x >>> 1) : x >>> 1;
    }
    protected static int MATRIX_5(int x, int t, int b) {
        return (t >= 0)?
                x^((x << t) & b):
                x^((x >>> -t) & b);
    }
    protected static int MATRIX_6(int x, int r, int ds, int dt, int a) {
        int result = ((x << r)^(x >>> (W - r))) & ds;
        return ((x & dt) != 0)? a^result : result;
    }
    protected int TEMPER(int x, int b, int c) {
        int z = x^((x << 7) & b);
        return z^((z << 15) & c);
    }


    protected abstract int T_0(int x);
    protected abstract int T_1(int x);
    protected abstract int T_2(int x);
    protected abstract int T_3(int x);
    protected abstract int T_4(int x);
    protected abstract int T_5(int x);
    protected abstract int T_6(int x);
    protected abstract int T_7(int x);

    public int rand() {
        int z0 = (STATE[map(R-1)] & LOWER_MASK) | (STATE[map(R-2)] & UPPER_MASK);
        int z1 = T_0(STATE[state_i]) ^ T_1(STATE[map(M1)]);
        int z2 = T_2(STATE[map(M2)]) ^ T_3(STATE[map(M3)]);
        int z3 = z1 ^ z2;
        int z4 = T_4(z0) ^ T_5(z1) ^ T_6(z2) ^ T_7(z3);

        STATE[state_i] = z3;
        state_i = map(R - 1);
        STATE[state_i] = z4;
        return z4;
    }
}

class WELL512a extends WELL {
    public WELL512a() {
        super(16, 0, 13, 9, 5);
    }
    @Override
    protected int T_0(int x) { return MATRIX_3(x, -16); }
    @Override
    protected int T_1(int x) { return MATRIX_3(x, -15); }
    @Override
    protected int T_2(int x) { return MATRIX_3(x, 11); }
    @Override
    protected int T_3(int x) { return MATRIX_0(); }
    @Override
    protected int T_4(int x) { return MATRIX_3(x, -2); }
    @Override
    protected int T_5(int x) { return MATRIX_3(x, -18); }
    @Override
    protected int T_6(int x) { return MATRIX_3(x, -28); }
    @Override
    protected int T_7(int x) { return MATRIX_5(x, -5, A1); }
}

class WELL1024a extends WELL {
    public WELL1024a() {
        super(32, 0, 3, 24, 10);
    }
    @Override
    protected int T_0(int x) { return MATRIX_1(x); }
    @Override
    protected int T_1(int x) { return MATRIX_3(x, 8); }
    @Override
    protected int T_2(int x) { return MATRIX_3(x, -19); }
    @Override
    protected int T_3(int x) { return MATRIX_3(x, -14); }
    @Override
    protected int T_4(int x) { return MATRIX_3(x, -11); }
    @Override
    protected int T_5(int x) { return MATRIX_3(x, -7); }
    @Override
    protected int T_6(int x) { return MATRIX_3(x, -13); }
    @Override
    protected int T_7(int x) { return MATRIX_0(); }
}

class WELL19937a extends WELL {
    public WELL19937a() {
        super(624, 31, 70, 179, 449);
    }
    @Override
    protected int T_0(int x) { return MATRIX_3(x, -25); }
    @Override
    protected int T_1(int x) { return MATRIX_3(x, 27); }
    @Override
    protected int T_2(int x) { return MATRIX_2(x, 9); }
    @Override
    protected int T_3(int x) { return MATRIX_3(x, 1); }
    @Override
    protected int T_4(int x) { return MATRIX_1(x); }
    @Override
    protected int T_5(int x) { return MATRIX_3(x, -9); }
    @Override
    protected int T_6(int x) { return MATRIX_3(x, -21); }
    @Override
    protected int T_7(int x) { return MATRIX_3(x, 21); }
}

class WELL19937c extends WELL19937a {
    public WELL19937c() {
        super();
    }
    @Override
    public int rand() {
        return TEMPER(super.rand(), 0xE46E1700, 0x9B868000);
    }
}

class WELL23209b extends WELL {
    public WELL23209b() {
        super(726, 23, 610, 175, 662);
    }
    @Override
    protected int T_0(int x) { return MATRIX_4(x, A5); }
    @Override
    protected int T_1(int x) { return MATRIX_1(x); }
    @Override
    protected int T_2(int x) { return MATRIX_6(x, 15, 30, 15, A6); }
    @Override
    protected int T_3(int x) { return MATRIX_3(x, -24); }
    @Override
    protected int T_4(int x) { return MATRIX_3(x, -26); }
    @Override
    protected int T_5(int x) { return MATRIX_1(x); }
    @Override
    protected int T_6(int x) { return MATRIX_0(); }
    @Override
    protected int T_7(int x) { return MATRIX_3(x, 16); }
}

class WELL44497a extends WELL {
    public WELL44497a() {
        super(1391, 15, 23, 481, 229);
    }
    @Override
    protected int T_0(int x) { return MATRIX_3(x, -24); }
    @Override
    protected int T_1(int x) { return MATRIX_3(x, 30); }
    @Override
    protected int T_2(int x) { return MATRIX_3(x, -10); }
    @Override
    protected int T_3(int x) { return MATRIX_2(x, -26); }
    @Override
    protected int T_4(int x) { return MATRIX_1(x); }
    @Override
    protected int T_5(int x) { return MATRIX_3(x, 20); }
    @Override
    protected int T_6(int x) { return MATRIX_6(x, 9, 14, 5, A7); }
    @Override
    protected int T_7(int x) { return MATRIX_1(x); }
}

class WELL44497b extends WELL44497a {
    public WELL44497b() {
        super();
    }
    @Override
    public int rand() {
        return TEMPER(super.rand(), 0x93DD1400, 0xFA118000);
    }
}
