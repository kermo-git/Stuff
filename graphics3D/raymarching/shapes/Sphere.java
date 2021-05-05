package graphics3D.raymarching.shapes;

import graphics3D.raymarching.Material;
import graphics3D.utils.Vector;

public class Sphere extends RayMarchingObject {
    private Vector center;
    private double radius;

    public Sphere(Material material, Vector center, double radius) {
        this.material = material;
        this.center = center;
        this.radius = radius;
    }

    @Override
    public RayMarchingObject rotate(double rotX, double rotY, double rotZ) {
        return this;
    }

    @Override
    public RayMarchingObject translate(double x, double y, double z) {
        center.x += x;
        center.y += y;
        center.z += z;
        return this;
    }

    @Override
    public double getSignedDistance(Vector point) {
        return new Vector(center, point).length() - radius;
    }

    @Override
    public Vector getNormal(Vector point) {
        Vector result = new Vector(center, point);
        result.normalize();
        return result;
    }
    
}
