package graphics3D.raymarching.shapes;

import graphics3D.utils.Matrix;
import graphics3D.utils.Vector;

public class Plane extends RMobject {
    private Vector referencePoint = new Vector(0, 0, 0);
    private Vector normal = new Vector(0, 1, 0);
    private double planeBias = 0;

    public Plane() {}
    public Plane(Vector referencePoint, Vector normal) {
        this.referencePoint = referencePoint;
        this.normal = normal;
        normal.normalize();
        planeBias = -normal.dot(referencePoint);
    }
    
    @Override
    public RMobject rotate(double degX, double degY, double degZ) {
        Matrix rotation = Matrix.rotateDeg(degX, degY, degZ);
        
        rotation.transform(referencePoint);
        rotation.transform(normal);

        normal.normalize();
        planeBias = -normal.dot(referencePoint);
        return this;
    }

    @Override
    public RMobject translate(double x, double y, double z) {
        referencePoint.x += x;
        referencePoint.y += y;
        referencePoint.z += z;
        planeBias = -normal.dot(referencePoint);
        return this;
    }

    @Override
    public double getSignedDistance(Vector point) {
        return normal.dot(point) + planeBias;
    }

    @Override
    public Vector getNormal(Vector point) {
        return new Vector(normal);
    }
}
