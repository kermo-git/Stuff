package graphics3D.shapes;

import graphics3D.Material;
import graphics3D.Vector;
import graphics3D.RayIntersection;

public class Sphere extends OriginShape {
    public double radius;

    public Sphere(Material material, double radius) {
        this.material = material;
        this.radius = radius;
    }
    @Override
    public RayIntersection getTransformedIntersection(
        Vector transformedOrigin, 
        Vector transformedDirection
    ) {
        return null;
    }
}
