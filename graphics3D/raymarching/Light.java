package graphics3D.raymarching;

import graphics3D.utils.Color;
import graphics3D.utils.Vector;

public class Light {
    public Vector location = new Vector(0, 0, 0);
    public Color color = new Color(0xFFFFFF);
    public double shadowSharpness = 1;
    
    public Light() {}
    public Light(Vector location) {
        this.location = location;
    }
    public Light(Vector location, double shadowSharpness) {
        this.location = location;
        this.shadowSharpness = shadowSharpness;
    }
    public Light(Vector location, Color color) {
        this.location = location;
        this.color = color;
    }
    public Light(Vector location, Color color, double shadowSharpness) {
        this.location = location;
        this.color = color;
        this.shadowSharpness = shadowSharpness;
    }
}
