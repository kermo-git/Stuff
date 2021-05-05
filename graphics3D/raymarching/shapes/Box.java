package graphics3D.raymarching.shapes;

import graphics3D.raymarching.Material;
import graphics3D.utils.Vector;

public class Box extends OriginRayMarchingObject {
    private double sx, sy, sz;

    public Box(Material material, double cubeSide) {
        this(material, cubeSide, cubeSide, cubeSide);
    }

    public Box(Material material, double sizeX, double sizeY, double sizeZ) {
        this.material = material;
        sx = 0.5 * sizeX;
        sy = 0.5 * sizeY;
        sz = 0.5 * sizeZ;
    }

    @Override
    protected double getSignedDistanceAtObjectSpace(Vector point) {
        double x = abs(point.x);
        double y = abs(point.y);
        double z = abs(point.z);

        double distX = x - sx;
        double distY = y - sy;
        double distZ = z - sz;

        if (distX < 0 && distY < 0 && distZ < 0) {
            return max(distX, max(distY, distZ));
        }
        return Math.sqrt(
            max(0, distX) * distX +
            max(0, distY) * distY + 
            max(0, distZ) * distZ
        );
    }
}
