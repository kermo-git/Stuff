package graphics3D.shapes;

import graphics3D.materials.Material;
import graphics3D.Vector;
import graphics3D.RayIntersection;

public class OpenCylinder extends OriginShape {
    private double radiusSquared, radiusRec;
    private double minY, maxY;

    public OpenCylinder(Material material, double height, double radius) {
        this.material = material;

        radiusSquared = radius * radius;
        radiusRec = 1 / radius;

        maxY = height * 0.5;
        minY = -height * 0.5;
    }
    
    @Override
    public RayIntersection getTransformedIntersection(Vector o, Vector d) {
        double a = d.x * d.x + d.z * d.z;
        double b = 2 * (o.x * d.x + o.z * d.z);
        double c = o.x * o.x + o.z * o.z - radiusSquared;

        solveQuadraticEquasion(a, b, c);
        double y = 0, distance = 0;
        boolean inside = true;

        if (lowX != lowX) {
            return null;
        }
        if (highX < 0) {
            return null;
        }
        if (lowX > 0) {
            y = o.y + lowX * d.y;
            if (y >= minY && y <= maxY) {
                distance = lowX;
                inside = false;
            }
        } 
        if (inside) {
            y = o.y + highX * d.y;
            if (y >= minY && y <= maxY) {
                distance = highX;
            } else {
                return null;
            }
        }
        Vector hitPoint = new Vector(
            o.x + distance * d.x,
            y,
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