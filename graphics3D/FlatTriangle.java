package graphics3D;

public class FlatTriangle extends Triangle {
    public FlatTriangle(Material material, Vertex v1, Vertex v2, Vertex v3) {
        super(material, v1, v2, v3);
    }

    Vector normal;

    @Override
    protected void beforeLoop(Scene3D scene) {
        normal = getNormal();
        normal.normalize();
    }

    @Override
    protected Color interpolate(Scene3D scene) {
        Vector p = v1.scale(q1 * p1.zRec);
        p.add(v2.scale(q2 * p2.zRec));
        p.add(v3.scale(q3 * p3.zRec));
        p = p.scale(1.0 / zRec);
        return material.illuminate(scene, p, normal);
    }
}
