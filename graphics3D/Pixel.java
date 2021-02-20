package graphics3D;

public class Pixel {
    int x, y;
    double zRec;

    public Pixel(int screenX, int screenY, double zReciprocal) {
        this.zRec = zReciprocal;
        this.x = screenX;
        this.y = screenY;
    }
}
