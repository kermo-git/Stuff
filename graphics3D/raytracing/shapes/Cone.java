package graphics3D.raytracing.shapes;

import graphics3D.raytracing.Material;
import graphics3D.raytracing.RayIntersection;
import graphics3D.utils.Vector;

public class Cone extends OriginShape {
    public double height, radius;

    public Cone(Material material, double height, double radius) {
        this.material = material;
        this.height = height;
        this.radius = radius;
    }
    @Override
    public RayIntersection getIntersectionAtOrigin(
        Vector transformedOrigin, 
        Vector transformedDirection
    ) {
        return null;
    }
}