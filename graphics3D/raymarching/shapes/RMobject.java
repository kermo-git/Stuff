package graphics3D.raymarching.shapes;

import graphics3D.raymarching.Config;
import graphics3D.raymarching.Material;
import graphics3D.raymarching.Opaque;
import graphics3D.utils.Vector;

public abstract class RMobject {    
    public abstract double getSignedDistance(Vector point);
    public abstract RMobject rotate(double degX, double degY, double degZ);
    public abstract RMobject translate(double x, double y, double z);

    public Material material = new Opaque();

    public RMobject setMaterial(Material material) {
        this.material = material;
        return this;
    }


    static double abs(double a) {
        return (a < 0) ? -a : a;
    }
    static double max(double a, double b) {
        return (a > b) ? a : b;
    }
    static double min(double a, double b) {
        return (a < b) ? a : b;
    }

    
    static Vector incrementX(Vector v, double dx) {
        return new Vector(
            v.x + dx, v.y, v.z
        );
    }
    static Vector incrementY(Vector v, double dy) {
        return new Vector(
            v.x, v.y + dy, v.z
        );
    }
    static Vector incrementZ(Vector v, double dz) {
        return new Vector(
            v.x, v.y, v.z + dz
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
