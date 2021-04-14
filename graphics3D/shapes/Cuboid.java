package graphics3D.shapes;

import graphics3D.Material;
import graphics3D.Vector;
import graphics3D.RayIntersection;

public class Cuboid extends OriginShape {
    public double sizeX, sizeY, sizeZ;

    public Cuboid(Material material, double sizeX, double sizeY, double sizeZ) {
        this.material = material;
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.sizeZ = sizeZ;
    }
    @Override
    public RayIntersection getTransformedIntersection(
        Vector transformedOrigin, 
        Vector transformedDirection
    ) {
        return null;
    }
}
