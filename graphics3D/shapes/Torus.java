package graphics3D.shapes;

import graphics3D.Material;
import graphics3D.Vector;
import graphics3D.RayIntersection;

public class Torus extends OriginShape {
    public double mainRadius, tubeRadius;

    public Torus(Material material, double height, double radius) {
        this.material = material;
        this.mainRadius = height;
        this.tubeRadius = radius;
    }
    @Override
    public RayIntersection getTransformedIntersection(
        Vector transformedOrigin, 
        Vector transformedDirection
    ) {
        return null;
    }
}