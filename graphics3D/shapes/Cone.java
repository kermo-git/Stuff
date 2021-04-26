package graphics3D.shapes;

import graphics3D.materials.Material;
import graphics3D.Vector;
import graphics3D.RayIntersection;

public class Cone extends OriginShape {
    public double height, radius;

    public Cone(Material material, double height, double radius) {
        this.material = material;
        this.height = height;
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