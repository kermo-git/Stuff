package graphics3D;

public class Pixel {
    double pixelX, pixelY;
    double zRec;

    public Pixel(double pixelX, double pixelY, double zReciprocal) {
        this.pixelX = pixelX;
        this.pixelY = pixelY;
        this.zRec = zReciprocal;
    }
}
