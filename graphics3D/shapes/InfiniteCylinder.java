package graphics3D.shapes;

import graphics3D.materials.Material;
import graphics3D.Vector;
import graphics3D.RayIntersection;

public class InfiniteCylinder extends OriginShape {
    private double radiusSquared, radiusRec;

    public InfiniteCylinder(Material material, double radius) {
        this.material = material;
        radiusSquared = radius * radius;
        radiusRec = 1 / radius;
    }
    
    @Override
    public RayIntersection getTransformedIntersection(Vector o, Vector d) {
        double a = d.x * d.x + d.z * d.z;
        double b = 2 * (o.x * d.x + o.z * d.z);
        double c = o.x * o.x + o.z * o.z - radiusSquared;

        double distance = 0;
        boolean inside = false;
        solveQuadraticEquasion(a, b, c);

        if (lowX != lowX) {
            return null;
        }
        if (highX < 0) {
            return null;
        }
        if (lowX < 0) {
            inside = true;
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

        if (inside) {
            normal.x = -hitPoint.x * radiusRec;
            normal.z = -hitPoint.z * radiusRec;
        } else {
            normal.x = hitPoint.x * radiusRec;
            normal.z = hitPoint.z * radiusRec;
        }
        return new RayIntersection(distance, hitPoint, normal, material);
    }
}
