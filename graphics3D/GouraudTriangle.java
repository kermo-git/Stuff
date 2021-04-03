package graphics3D;

public class GouraudTriangle extends Triangle {
    public GouraudTriangle(Material material, Vertex v1, Vertex v2, Vertex v3) {
        super(material, v1, v2, v3);
    }

    @Override
    protected Color doColorCalculation() {
        Color c = new Color();
        c.add(w1 * p1.zRec, v1.color);
        c.add(w2 * p2.zRec, v2.color);
        c.add(w3 * p3.zRec, v3.color);
        c.scale(1.0 / zRec);
        return c;
    }
}
