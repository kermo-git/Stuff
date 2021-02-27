package graphics3D;

public class PhongTriangle extends Triangle {
    public PhongTriangle(Material material, Vertex v1, Vertex v2, Vertex v3) {
        super(material, v1, v2, v3);
    }

    Vector surfacePoint, smoothNormal;

    @Override
    protected Color interpolate(Scene3D scene) {
        double z = 1.0 / zRec;

        surfacePoint = v1.scale(w1 * p1.zRec);
        surfacePoint.add(v2.scale(w2 * p2.zRec));
        surfacePoint.add(v3.scale(w3 * p3.zRec));
        surfacePoint = surfacePoint.scale(z);

        smoothNormal = v1.normal.scale(w1 * p1.zRec);
        smoothNormal.add(v2.normal.scale(w2 * p2.zRec));
        smoothNormal.add(v3.normal.scale(w3 * p3.zRec));
        smoothNormal = smoothNormal.scale(z);
        smoothNormal.normalize();

        return material.illuminate(scene, surfacePoint, smoothNormal);
    }
}