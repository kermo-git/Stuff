package graphics3D.raytracing.shapes;

import graphics3D.raytracing.Material;
import graphics3D.raytracing.RayIntersection;
import graphics3D.utils.Vector;

public class Cone extends OriginRayTracingObject {
    private double height;
    private double _2rSqr_div_h, rSqr_div_hSqr, rSqr, normalTan;
    private Vector bottomNormal = new Vector(0, 1, 0);

    public Cone(Material material, double height, double radius) {
        this.material = material;
        this.height = height;

        rSqr = radius * radius;
        _2rSqr_div_h = 2 * rSqr / height;
        rSqr_div_hSqr = rSqr / (height * height);
        normalTan = radius / height;
    }
    @Override
    public RayIntersection getIntersectionAtObjectSpace(Vector o, Vector d) {
        double a = 
            + d.x * d.x 
            + d.z * d.z 
            - d.y * d.y * rSqr_div_hSqr;

        double b = 2 * (
            + o.x * d.x 
            + o.z * d.z 
            - o.y * d.y * rSqr_div_hSqr
        ) 
        + d.y * _2rSqr_div_h;

        double c = 
            + o.x * o.x 
            + o.z * o.z 
            - o.y * o.y * rSqr_div_hSqr 
            + o.y * _2rSqr_div_h 
            - rSqr;

        solveQuadraticEquasion(a, b, c);

        if (lowX != lowX || highX < 0) {
            return null;
        }
        double distanceToConeSurface = (lowX < 0) ? highX : lowX;
        double distanceToBottomPlane = - o.y / d.y;

        if (distanceToBottomPlane > 0 && distanceToBottomPlane < distanceToConeSurface) {
            Vector hitPoint = getPointOnRay(o, d, distanceToBottomPlane);
            double x = hitPoint.x;
            double z = hitPoint.z;

            if (x * x + z * z <= rSqr) {
                return new RayIntersection(
                    distanceToBottomPlane, 
                    hitPoint, 
                    bottomNormal, 
                    material
                );
            }
        } 
        Vector hitPoint = getPointOnRay(o, d, distanceToConeSurface);

        if (hitPoint.y >= 0 && hitPoint.y <= height) {
            Vector normal = new Vector(
                hitPoint.x,
                0,
                hitPoint.z
            );
            normal.normalize();
            normal.y = normalTan;
            normal.normalize();

            return new RayIntersection(
                distanceToConeSurface, 
                hitPoint, 
                normal,
                material
            );
        }
        return null;
    }
}