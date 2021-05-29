package graphics3D.raytracing.shapes;

import graphics3D.raytracing.RayHit;
import graphics3D.utils.Vector;

public class InfiniteCylinder extends OriginRTObject {
    private double radiusSquared, radiusRec;

    public InfiniteCylinder(double radius) {
        radiusSquared = radius * radius;
        radiusRec = 1 / radius;
    }
    
    @Override
    public RayHit getIntersectionAtObjectSpace(Vector o, Vector d) {
        double a = d.x * d.x + d.z * d.z;
        double b = 2 * (o.x * d.x + o.z * d.z);
        double c = o.x * o.x + o.z * o.z - radiusSquared;

        double distance = 0;
        solveQuadraticEquasion(a, b, c);

        if (lowX != lowX) {
            return null;
        }
        if (highX < 0) {
            return null;
        }
        if (lowX < 0) {
            distance = highX;
        } else {
            distance = lowX;
        }
        Vector hitPoint = new Vector(
            o.x + distance * d.x,
            o.y + distance * d.y,
            o.z + distance * d.z
        );
        Vector normal = new Vector();

        normal.x = hitPoint.x * radiusRec;
        normal.z = hitPoint.z * radiusRec;

        return new RayHit(distance, hitPoint, normal, material);
    }
}
