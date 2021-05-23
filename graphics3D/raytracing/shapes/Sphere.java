package graphics3D.raytracing.shapes;

import graphics3D.raytracing.Material;
import graphics3D.raytracing.RayIntersection;
import graphics3D.utils.Vector;

public class Sphere extends RayTracingObject {
    private Vector center;
    private double radius;
    private double equasionConst;

    public Sphere(Material material, Vector center, double radius) {
        this.material = material;
        this.center = center;
        equasionConst = center.dot(center) - radius * radius;
    }

    @Override
    public RayTracingObject rotate(double degX, double degY, double degZ) {
        return this;
    }

    @Override
    public RayTracingObject translate(double x, double y, double z) {
        center = new Vector(center);
        center.x += x;
        center.y += y;
        center.z += z;
        
        equasionConst = center.dot(center) - radius * radius;
        return this;
    }

    @Override
    public RayIntersection getIntersection(Vector rayOrigin, Vector rayDirection) {
        double a = rayDirection.dot(rayDirection);
        double b = 2 * (rayOrigin.dot(rayDirection) - rayDirection.dot(center));
        double c = rayOrigin.dot(rayOrigin) - 2 * rayOrigin.dot(center) + equasionConst;

        solveQuadraticEquasion(a, b, c);

        if (lowX != lowX || 
            highX <= 0 || 
            highX == lowX) {
                
            return null;
        }
        double distance = (lowX <= 0) ? highX : lowX;
        
        Vector location = new Vector(
            rayOrigin.x + distance * rayDirection.x,
            rayOrigin.y + distance * rayDirection.y,
            rayOrigin.z + distance * rayDirection.z
        );
        Vector normal = new Vector(center, location);
        normal.normalize();
        return new RayIntersection(distance, location, normal, material);
    }
}
