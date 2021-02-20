package graphics3D;

public class Color {
    private double red = 0, green = 0, blue = 0;

    public Color() {}
    public Color(double r, double g, double b) {
        red = r; green = g; blue = b;
    }
    public Color(int RGBhex) {
        int redInt = (RGBhex >> 16) & 0xFF;
        int greenInt = (RGBhex >> 8) & 0xFF;
        int blueInt = (RGBhex >> 0) & 0xFF;

        red = redInt / 255.0;
        green = greenInt / 255.0;
        blue = blueInt / 255.0;
    }
    public Color(Color color1, Color color2) {
        this(
            color1.red * color2.red,
            color1.green * color2.green,
            color1.blue * color2.blue
        );
    }
    public Color(double intensity, Color color1, Color color2) {
        this(
            intensity * color1.red * color2.red,
            intensity * color1.green * color2.green,
            intensity * color1.blue * color2.blue
        );
    }
    public Color(double intensity, Color color) {
        this(
            intensity * color.red,
            intensity * color.green,
            intensity * color.blue
        );
    }


    public void add(Color other) {
        red += other.red;
        green += other.green;
        blue += other.blue;
    }
    public int getRGBhex() {
        if (red < 0) red = 0;
        if (red > 1) red = 1;
        if (green < 0) green = 0;
        if (green > 1) green = 1;
        if (blue < 0) blue = 0;
        if (blue > 1) blue = 1;

        int redInt = (int) (red * 255);
        int greenInt = (int) (green * 255);
        int blueInt = (int) (blue * 255);
        return redInt << 16 | greenInt << 8 | blueInt;
    }
}