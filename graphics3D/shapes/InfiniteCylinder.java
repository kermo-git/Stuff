package graphics3D.shapes;

import graphics3D.Material;
import graphics3D.Vector;
import graphics3D.RayIntersection;

public class InfiniteCylinder extends OriginShape {
    public double radius;

    public InfiniteCylinder(Material material, double radius) {
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
