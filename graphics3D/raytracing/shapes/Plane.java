package graphics3D.raytracing.shapes;

import graphics3D.raytracing.RayHit;
import graphics3D.utils.Matrix;
import graphics3D.utils.Vector;

public class Plane extends RTobject {
    Vector normal = new Vector(0, 1, 0);
    Vector oppositeNormal = new Vector(0, -1, 0);
    Vector point = new Vector(0, 0, 0);
    double planeBias = 0;

    public Plane() {}
    public Plane(Vector point, Vector normal) {
        this.point = point;
        this.normal = normal;
        
        normal.normalize();
        oppositeNormal = normal.getScaled(-1);
        planeBias = point.dot(normal);
    }

    @Override
    public RTobject rotate(double degX, double degZ, double degY) {
        Matrix rotation = Matrix.rotateDeg(degX, degY, degZ);

        rotation.transform(normal);
        rotation.transform(point);

        normal.normalize();
        oppositeNormal = normal.getScaled(-1);
        planeBias = point.dot(normal);

        return this;
    }

    @Override
    public RTobject translate(double x, double y, double z) {
        Matrix translation = Matrix.translate(x, y, z);
        translation.transform(point);
        planeBias = point.dot(normal);
        return this;
    }

    @Override
    public RayHit getIntersection(Vector rayOrigin, Vector rayDirection) {
        double normalDotDirection = normal.dot(rayDirection);
        if (normalDotDirection == 0) {
            return null;
        }
        double distance = (planeBias - normal.dot(rayOrigin)) / normalDotDirection;
        if (distance < 0) {
            return null;
        }
        Vector hitPoint = getPointOnRay(rayOrigin, rayDirection, distance);
        return new RayHit(distance, hitPoint, oppositeNormal, material);
    }
}
