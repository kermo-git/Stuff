package graphics3D.raymarching.shapes;

import graphics3D.raymarching.Material;
import graphics3D.utils.Vector;

public class InfiniteCylinder extends OriginRayMarchingObject {
    private double radius;

    public InfiniteCylinder(Material material, double radius) {
        this.material = material;
        this.radius = radius;
    }

    @Override
    protected double getSignedDistanceAtObjectSpace(Vector point) {
        return new Vector(point.x, 0, point.z).length() - radius;
    }
}
