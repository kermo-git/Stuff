package graphics3D;

public class PhongTriangle extends Triangle {
    public PhongTriangle(Material material, Vertex v1, Vertex v2, Vertex v3) {
        super(material, v1, v2, v3);
    }

    Vector surfacePoint, smoothNormal;

    @Override
    protected Color doColorCalculation() {
        double z = 1.0 / zRec;

        surfacePoint = new Vector();
        surfacePoint.add(w1 * p1.zRec, v1);
        surfacePoint.add(w2 * p2.zRec, v2);
        surfacePoint.add(w3 * p3.zRec, v3);
        surfacePoint.scale(z);

        smoothNormal = new Vector();
        smoothNormal.add(w1 * p1.zRec, v1.normal);
        smoothNormal.add(w2 * p2.zRec, v2.normal);
        smoothNormal.add(w3 * p3.zRec, v3.normal);
        smoothNormal.scale(z);
        smoothNormal.normalize();

        return material.illuminate(surfacePoint, smoothNormal);
    }
}