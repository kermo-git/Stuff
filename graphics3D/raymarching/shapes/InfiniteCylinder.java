package graphics3D.raymarching.shapes;

import graphics3D.utils.Vector;

public class InfiniteCylinder extends OriginRMObject {
    private double radius;

    public InfiniteCylinder(double radius) {
        this.radius = radius;
    }

    @Override
    protected double getSignedDistanceAtObjectSpace(Vector point) {
        return new Vector(point.x, 0, point.z).length() - radius;
    }
}
