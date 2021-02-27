package graphics3D;

public class FlatTriangle extends Triangle {
    public FlatTriangle(Material material, Vertex v1, Vertex v2, Vertex v3) {
        super(material, v1, v2, v3);
    }

    Vector surfacePoint;

    @Override
    protected Color interpolate(Scene3D scene) {
        surfacePoint = v1.scale(w1 * p1.zRec);
        surfacePoint.add(v2.scale(w2 * p2.zRec));
        surfacePoint.add(v3.scale(w3 * p3.zRec));
        surfacePoint = surfacePoint.scale(1.0 / zRec);
        
        return material.illuminate(scene, surfacePoint, normal);
    }
}
