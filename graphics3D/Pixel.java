package graphics3D;

public class Pixel {
    int x, y;
    double depth;

    public Pixel(int x, int y) {
        this.x = x;
        this.y = y;
    }
    public Pixel(int x, int y, double depth) {
        this.x = x;
        this.y = y;
        this.depth = depth;  
    }
}
