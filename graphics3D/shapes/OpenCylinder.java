package graphics3D.shapes;

import graphics3D.Material;
import graphics3D.Vector;
import graphics3D.RayIntersection;

public class OpenCylinder extends OriginShape {
    public double height, radius;

    public OpenCylinder(Material material, double height, double radius) {
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