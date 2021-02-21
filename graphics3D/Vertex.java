package graphics3D;

import java.util.List;
import java.util.ArrayList;

public class Vertex extends Vector {
    List<Triangle> triangles = new ArrayList<>();
    Vector normal;
    Color color;
    Pixel projection;

    public Vertex(double x, double y, double z) {
        super(x, y, z);
    }
}