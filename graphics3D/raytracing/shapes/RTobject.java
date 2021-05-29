package graphics3D.raytracing.shapes;

import graphics3D.utils.Vector;
import graphics3D.raytracing.Material;
import graphics3D.raytracing.Opaque;
import graphics3D.raytracing.RayHit;

public abstract class RTobject {
    public abstract RayHit getIntersection(Vector rayOrigin, Vector rayDirection);
    public abstract RTobject rotate(double degX, double degY, double degZ);
    public abstract RTobject translate(double x, double y, double z);

    public Material material = new Opaque();

    public RTobject setMaterial(Material material) {
        this.material = material;
        return this;
    }

    protected static Vector getPointOnRay(Vector origin, Vector direction, double distance) {
        return new Vector(
            origin.x + distance * direction.x,
            origin.y + distance * direction.y,
            origin.z + distance * direction.z
        );
    }

    protected double lowX, highX;
    
    protected void solveQuadraticEquasion(double a, double b, double c) {
        double D = b * b - 4 * a * c;

        if (D < 0) {
            lowX = Double.NaN;
            highX = Double.NaN;
        }
        else if (D == 0) {
            lowX = highX = -b / (2 * a);
        } 
        else {
            D = Math.sqrt(D);
            lowX = (-b - D) / (2 * a);
            highX = (-b + D) / (2 * a);

            if (lowX > highX) {
                double temp = lowX;
                lowX = highX;
                highX = temp;
            }
        }
    }
}
