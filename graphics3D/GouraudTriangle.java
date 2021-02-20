package graphics3D;

public class GouraudTriangle extends Triangle {
    public GouraudTriangle(Material material, Vertex v1, Vertex v2, Vertex v3) {
        super(material, v1, v2, v3);
    }

    Vector normal;
    Color c1, c2, c3;

    @Override
    protected void beforeLoop(Scene3D scene) {
        c1 = material.illuminate(scene, v1, v1.normal);
        c2 = material.illuminate(scene, v2, v2.normal);
        c3 = material.illuminate(scene, v3, v3.normal);
    }

    @Override
    protected Color interpolate(Scene3D scene) {
        Color c = new Color(q1 * p1.zRec, c1);
        c.add(new Color(q2 * p2.zRec, c2));
        c.add(new Color(q3 * p3.zRec, c3));
        return new Color(1.0 / zRec, c);
    }
}
