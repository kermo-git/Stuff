package graphics3D;

import java.util.List;
import java.util.ArrayList;

public class Vertex extends Vector {
    List<Triangle> triangles = new ArrayList<>();
    Vector normal;

    public Vertex(double x, double y, double z) {
        super(x, y, z);
    }
    public void calculateNormal() {
        normal = new Vector();
        for (Triangle t : triangles) {
            normal.add(t.getNormal());
        }
        normal.normalize();
    }
}