package graphics3D;

public class FlatTriangle extends Triangle {
    public FlatTriangle(Material material, Vertex v1, Vertex v2, Vertex v3) {
        super(material, v1, v2, v3);
    }

    Vector surfacePoint;

    @Override
    protected Color doColorCalculation() {
        surfacePoint = new Vector();
        surfacePoint.add(w1 * p1.zRec, v1);
        surfacePoint.add(w2 * p2.zRec, v2);
        surfacePoint.add(w3 * p3.zRec, v3);
        surfacePoint.scale(1.0 / zRec);
        
        return material.illuminate(surfacePoint, normal);
    }
}
