package graphics3D.raymarching.shapes;

import graphics3D.utils.Vector;

public class Sphere extends RMobject {
    private Vector center;
    private double radius;

    public Sphere(double Cx, double Cy, double Cz, double r) {
        center = new Vector(Cx, Cy, Cz);
        radius = r;
    }

    @Override
    public double getSignedDistance(Vector point) {
        return new Vector(center, point).length() - radius;
    }

    @Override
    public RMobject rotate(double degX, double degY, double degZ) {
        return this;
    }

    @Override
    public RMobject translate(double x, double y, double z) {
        center.x += x;
        center.y += y;
        center.z += z;
        return this;
    }

    @Override
    public Vector getNormal(Vector point) {
        Vector normal = new Vector(center, point);
        normal.normalize();
        return normal;
    }
}
