package graphics3D;

public class Pixel {
    int x, y;
    double zRec;

    public Pixel(int x, int y, double zReciprocal) {
        this.x = x;
        this.y = y;
        this.zRec = zReciprocal;
    }
}
