package graphics3D.raytracing.shapes;

import graphics3D.raytracing.RayHit;
import graphics3D.utils.Vector;

public class Cylinder extends OriginRTObject {
    private double radiusSquared, radiusRec;
    private double minY, maxY;

    public Cylinder(double height, double radius) {
        radiusSquared = radius * radius;
        radiusRec = 1 / radius;

        maxY = height * 0.5;
        minY = -height * 0.5;
    }
    
    @Override
    public RayHit getIntersectionAtObjectSpace(Vector o, Vector d) {
        double a = d.x * d.x + d.z * d.z;
        double b = 2 * (o.x * d.x + o.z * d.z);
        double c = o.x * o.x + o.z * o.z - radiusSquared;

        solveQuadraticEquasion(a, b, c);

        if (lowX != lowX || highX < 0) {
            return null;
        }
        Vector hitPoint = null, normal = null;
        double distance = 0;
        
        double nearSideHitY = o.y + lowX * d.y;
        double farSideHitY = o.y + highX * d.y;
        
        boolean under = o.y < minY;
        boolean above = o.y > maxY;

        boolean inInfiniteCylinder = lowX < 0;
        boolean inside = !(under || above) && inInfiniteCylinder;

        if (above && nearSideHitY > maxY && farSideHitY < maxY) {
            distance = (maxY - o.y) / d.y;
            hitPoint = getPointOnRay(o, d, distance);
            normal = new Vector(0, 1, 0);
        }
        else if (inside && nearSideHitY > minY && farSideHitY < minY) {
            distance = (minY - o.y) / d.y;
            hitPoint = getPointOnRay(o, d, distance);
            normal = new Vector(0, 1, 0);
        }
        else if (under && nearSideHitY < minY && farSideHitY > minY) {
            distance = (minY - o.y) / d.y;
            hitPoint = getPointOnRay(o, d, distance);
            normal = new Vector(0, 1, 0);
        }
        else if (inside && nearSideHitY < maxY && farSideHitY > maxY) {
            distance = (maxY - o.y) / d.y;
            hitPoint = getPointOnRay(o, d, distance);
            normal = new Vector(0, 1, 0);
        }
        else if (!inside && nearSideHitY >= minY && nearSideHitY <= maxY) {
            distance = lowX;
            hitPoint = getPointOnRay(o, d, distance);

            normal = new Vector(
                hitPoint.x * radiusRec,
                0,
                hitPoint.z * radiusRec
            );
        }
        else if (inside) {
            distance = highX;
            hitPoint = getPointOnRay(o, d, distance);

            normal = new Vector(
                hitPoint.x * radiusRec,
                0,
                hitPoint.z * radiusRec
            );
        }
        else {
            return null;
        }
        return new RayHit(distance, hitPoint, normal, material);
    }
}