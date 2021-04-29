package graphics3D.raytracing.shapes;

import graphics3D.raytracing.Material;
import graphics3D.raytracing.RayIntersection;
import graphics3D.utils.Vector;

public class Cuboid extends OriginShape {
    public double sizeX, sizeY, sizeZ;

    public Cuboid(Material material, double sizeX, double sizeY, double sizeZ) {
        this.material = material;
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.sizeZ = sizeZ;
    }
    @Override
    public RayIntersection getIntersectionAtOrigin(
        Vector transformedOrigin, 
        Vector transformedDirection
    ) {
        return null;
    }
}
