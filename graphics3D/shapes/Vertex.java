package graphics3D.shapes;

import java.util.List;
import java.util.ArrayList;

import graphics3D.Vector;
import graphics3D.Pixel;
import graphics3D.Color;

public class Vertex extends Vector {
    public List<Triangle> triangles = new ArrayList<>();
    public Vector normal = new Vector();
    public Color color;
    public Pixel projection, shadowBufferProjection;

    public Vertex(double x, double y, double z) {
        super(x, y, z);
    }
}