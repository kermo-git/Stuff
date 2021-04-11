package graphics3D;

public class GouraudTriangle extends Triangle {
    public GouraudTriangle(Material material, Vertex v1, Vertex v2, Vertex v3) {
        super(material, v1, v2, v3);
    }

    @Override
    protected Color doColorCalculation(double w1, double w2, double w3, double zRec) {
        Color c = new Color();
        c.add(w1 * v1.projection.zRec, v1.color);
        c.add(w2 * v2.projection.zRec, v2.color);
        c.add(w3 * v3.projection.zRec, v3.color);
        c.scale(1.0 / zRec);
        return c;
    }
}
