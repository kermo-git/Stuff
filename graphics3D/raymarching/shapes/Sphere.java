package graphics3D.raymarching.shapes;

import graphics3D.utils.Vector;

public class Sphere extends TransformableObject {
    private double radius;

    public Sphere(double radius) {
        this.radius = radius;
    }

    @Override
    protected double getSignedDistanceAtObjectSpace(Vector point) {
        return point.length() - radius;
    }
}
