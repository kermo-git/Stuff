package graphics3D.raymarching;

import graphics3D.utils.Color;
import graphics3D.utils.Vector;

public class Light {
    public Vector location = new Vector(0, 0, 0);
    public Color color = new Color(0xFFFFFF);
    public int shadowSharpness = 1;
    
    public Light() {}
    public Light(Vector location) {
        this.location = location;
    }
    public Light(Vector location, int shadowSharpness) {
        this.location = location;
        this.shadowSharpness = shadowSharpness;
    }
    public Light(Vector location, Color color) {
        this.location = location;
        this.color = color;
    }
    public Light(Vector location, Color color, int shadowSharpness) {
        this.location = location;
        this.color = color;
        this.shadowSharpness = shadowSharpness;
    }
}
