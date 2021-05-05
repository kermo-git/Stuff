package graphics3D.raymarching.shapes;

import graphics3D.raymarching.Config;
import graphics3D.raymarching.Material;
import graphics3D.utils.Vector;

public abstract class RayMarchingObject {
    public Material material;
    
    public abstract RayMarchingObject rotate(double rotX, double rotY, double rotZ);
    public abstract RayMarchingObject translate(double x, double y, double z);
    public abstract double getSignedDistance(Vector point);

    public static double abs(double a) {
        return (a < 0) ? -a : a;
    }
    public static double max(double a, double b) {
        return (a > b) ? a : b;
    }
    private Vector incrementX(Vector v, double dx) {
        return new Vector(
            v.x + dx, v.y, v.z
        );
    }
    private Vector incrementY(Vector v, double dy) {
        return new Vector(
            v.x, v.y + dy, v.z
        );
    }
    private Vector incrementZ(Vector v, double dz) {
        return new Vector(
            v.x + dz, v.y, v.z + dz
        );
    }


    public Vector getNormal(Vector point) {
        double epsilon = Config.GRADIENT_EPSILON;

        double gradX = 
            getSignedDistance(incrementX(point, epsilon)) -
            getSignedDistance(incrementX(point, -epsilon));
        
        double gradY = 
            getSignedDistance(incrementY(point, epsilon)) -
            getSignedDistance(incrementY(point, -epsilon));

        double gradZ = 
            getSignedDistance(incrementZ(point, epsilon)) -
            getSignedDistance(incrementZ(point, -epsilon));

        Vector result = new Vector(gradX, gradY, gradZ);
        result.normalize();
        return result;
    }
}
