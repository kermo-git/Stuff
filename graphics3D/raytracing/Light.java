package graphics3D.raytracing;

import graphics3D.utils.Color;
import graphics3D.utils.Vector;

public class Light {
    public Vector location = new Vector(0, 0, 0);
    public Color color = new Color(0xFFFFFF);

    public Light() {}
    public Light(Vector location) {
        this.location = location;
    }
    public Light(Vector location, Color color) {
        this.location = location;
        this.color = color;
    }
}