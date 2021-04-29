package graphics3D.raytracing.shapes;

import graphics3D.raytracing.Material;
import graphics3D.raytracing.RayIntersection;
import graphics3D.utils.Matrix;
import graphics3D.utils.Vector;

public class Plane extends RayTracingObject {
    Vector normal = new Vector(0, 1, 0);
    Vector oppositeNormal = new Vector(0, -1, 0);
    Vector point = new Vector(0, 0, 0);
    double planeBias = 0;

    public Plane(Material material) {
        this.material = material;
    }
    public Plane(Material material, Vector point, Vector normal) {
        this.material = material;
        this.point = point;
        this.normal = normal;
        oppositeNormal = normal.getScaled(-1);
        planeBias = point.dot(normal);
    }

    @Override
    public void transform(Matrix rotation, Matrix translation) {
        rotation.transform(normal);
        rotation.transform(point);
        translation.transform(point);
        oppositeNormal = normal.getScaled(-1);
        planeBias = point.dot(normal);
    }

    @Override
    public void transform(Matrix translation) {
        translation.transform(point);
        planeBias = point.dot(normal);
    }

    @Override
    public RayIntersection getIntersection(Vector rayOrigin, Vector rayDirection) {
        double normalDotDirection = normal.dot(rayDirection);
        if (normalDotDirection == 0) {
            return null;
        }
        double distance = (planeBias -  normal.dot(rayOrigin)) / normalDotDirection;
        if (distance < 0) {
            return null;
        }
        Vector hitPoint = new Vector(
            rayOrigin.x + distance * rayDirection.x,
            rayOrigin.y + distance * rayDirection.y,
            rayOrigin.z + distance * rayDirection.z
        );
        if (normalDotDirection > 0) {
            return new RayIntersection(distance, hitPoint, oppositeNormal, material);
        } else {
            return new RayIntersection(distance, hitPoint, normal, material);
        }
    }
}
