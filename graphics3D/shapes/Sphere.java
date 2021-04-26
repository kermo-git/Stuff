package graphics3D.shapes;

import graphics3D.materials.Material;
import graphics3D.Vector;
import graphics3D.Matrix;
import graphics3D.RayIntersection;

public class Sphere extends Shape {
    private Vector center;
    private double radius;
    private double equasionConst;

    public Sphere(Material material, Vector center, double radius) {
        this.material = material;
        this.center = center;
        equasionConst = center.dot(center) - radius * radius;
    }

    @Override
    public void transform(Matrix rotation, Matrix translation) {
        transform(translation);
    }

    @Override
    public void transform(Matrix translation) {
        translation.transform(center);
        equasionConst = center.dot(center) - radius * radius;
    }

    @Override
    public RayIntersection getIntersection(Vector rayOrigin, Vector rayDirection) {
        double a = rayDirection.dot(rayDirection);
        double b = 2 * (rayOrigin.dot(rayDirection) - rayDirection.dot(center));
        double c = rayOrigin.dot(rayOrigin) - 2 * rayOrigin.dot(center) + equasionConst;

        solveQuadraticEquasion(a, b, c);

        if (lowX != lowX || highX <= 0 || highX == lowX) {
            return null;
        }
        if (lowX <= 0) {
            Vector hitPoint = new Vector(
                rayOrigin.x + highX * rayDirection.x,
                rayOrigin.y + highX * rayDirection.y,
                rayOrigin.z + highX * rayDirection.z
            );
            Vector normal = new Vector(hitPoint, center);
            normal.normalize();
            return new RayIntersection(highX, hitPoint, normal, material);
        }
        Vector location = new Vector(
            rayOrigin.x + lowX * rayDirection.x,
            rayOrigin.y + lowX * rayDirection.y,
            rayOrigin.z + lowX * rayDirection.z
        );
        Vector normal = new Vector(center, location);
        normal.normalize();
        return new RayIntersection(lowX, location, normal, material);
    }
}
