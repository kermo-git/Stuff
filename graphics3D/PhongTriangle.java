package graphics3D;

public class PhongTriangle extends Triangle {
    public PhongTriangle(Material material, Vertex v1, Vertex v2, Vertex v3) {
        super(material, v1, v2, v3);
    }

    @Override
    protected void beforeLoop(Scene3D scene) {}

    @Override
    protected Color interpolate(Scene3D scene) {
        double z = 1.0 / zRec;

        Vector p = v1.scale(q1 * p1.zRec);
        p.add(v2.scale(q2 * p2.zRec));
        p.add(v3.scale(q3 * p3.zRec));
        p = p.scale(z);

        Vector n = v1.normal.scale(q1 * p1.zRec);
        n.add(v2.normal.scale(q2 * p2.zRec));
        n.add(v3.normal.scale(q3 * p3.zRec));
        n = n.scale(z);
        n.normalize();

        return material.illuminate(scene, p, n);
    }
}