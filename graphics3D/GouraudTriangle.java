package graphics3D;

public class GouraudTriangle extends Triangle {
    public GouraudTriangle(Material material, Vertex v1, Vertex v2, Vertex v3) {
        super(material, v1, v2, v3);
    }

    @Override
    protected Color interpolate(Scene3D scene) {
        Color c = new Color(q1 * p1.zRec, v1.color);
        c.add(new Color(q2 * p2.zRec, v2.color));
        c.add(new Color(q3 * p3.zRec, v3.color));
        return new Color(1.0 / zRec, c);
    }
}
