package graphics3D.raymarching;

import graphics3D.utils.Color;
import graphics3D.utils.Vector;

public class Light {
    public Vector location = new Vector(0, 0, 0);
    public Color color = new Color(0xFFFFFF);
    public int shadowSharpness = 10;
    
    public Light() {}
    public Light(double x, double y, double z) {
        this.location = new Vector(x, y, z);
    }
    public Light(Vector location, int colorHEX) {
        this.location = location;
        this.color = new Color(colorHEX);
    }

    public Light setShadowSharpness(int shadowSharpness) {
        this.shadowSharpness = shadowSharpness;
        return this;
    }
}
