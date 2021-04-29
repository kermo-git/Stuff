package graphics3D.raytracing.shapes;

import graphics3D.raytracing.Material;
import graphics3D.raytracing.RayIntersection;
import graphics3D.utils.Vector;

public class Torus extends OriginShape {
    public double mainRadius, tubeRadius;

    public Torus(Material material, double height, double radius) {
        this.material = material;
        this.mainRadius = height;
        this.tubeRadius = radius;
    }
    @Override
    public RayIntersection getIntersectionAtOrigin(
        Vector transformedOrigin, 
        Vector transformedDirection
    ) {
        return null;
    }
}