package graphics3D.raytracing;

import graphics3D.utils.Color;
import graphics3D.utils.Vector;

public class Light {
    public Vector location;
    public Color color;

    public Light(Vector location, Color color) {
        this.location = location;
        this.color = color;
    }
}