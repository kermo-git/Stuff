package graphics3D;

public class Color {
    int red, green, blue;

    public Color(int red, int green, int blue) {
        if ((red < 0 || red > 255) ||
            (green < 0 || green > 255) ||
            (blue < 0 || blue > 255)) {
            
            throw new IllegalArgumentException();
        }
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    public Color(int RGB) {
        red = (RGB >> 16) & 0xFF;
        blue = (RGB >> 8) & 0xFF;
        green = (RGB >> 0) & 0xFF;
    }

    public int getRGB() {
        return
        ((red & 0xFF) << 16) |
        ((green & 0xFF) << 8) |
        ((blue & 0xFF) << 0);
    }
}
