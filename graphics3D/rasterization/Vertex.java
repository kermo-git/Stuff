package graphics3D.rasterization;

import java.util.List;
import java.util.ArrayList;

import graphics3D.utils.Pixel;
import graphics3D.utils.Vector;

public class Vertex extends Vector {
    public List<Triangle> triangles = new ArrayList<>();
    public Vector normal = new Vector();
    public Pixel projection;

    public Vertex(double x, double y, double z) {
        super(x, y, z);
    }
}