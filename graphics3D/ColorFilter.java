package graphics3D;

class ColorFilter {
    double red, green, blue;
    
    public ColorFilter(double red, double green, double blue) {
        if ((red < 0.0 || red > 1.0) ||
            (green < 0.0 || green > 1.0) ||
            (blue < 0.0 || blue > 1.0)) {
        
            throw new IllegalArgumentException();
        }
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    public ColorFilter(Color color) {
        this.red = color.red / 255.0;
        this.green = color.green / 255.0;
        this.blue = color.blue / 255.0;
    }
}
