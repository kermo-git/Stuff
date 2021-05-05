package graphics3D.raymarching.shapes;

import graphics3D.raymarching.Material;
import graphics3D.utils.Matrix;
import graphics3D.utils.Vector;

public class Plane extends RayMarchingObject {
    private Vector point = new Vector(0, 0, 0);
    private Vector normal = new Vector(0, 1, 0);
    private double planeBias = 0;

    public Plane(Material material) {
        this.material = material;
    }
    public Plane(Material material, Vector point, Vector normal) {
        this.material = material;
        this.point = point;
        this.normal = normal;
        normal.normalize();
        planeBias = -normal.dot(point);
    }
    
    @Override
    public RayMarchingObject rotate(double rotX, double rotY, double rotZ) {
        Matrix rotation = 
            Matrix.rotateAroundX(rotX).combine(
            Matrix.rotateAroundY(rotY)).combine(
            Matrix.rotateAroundZ(rotZ));
        
        rotation.transform(point);
        rotation.transform(normal);

        normal.normalize();
        planeBias = -normal.dot(point);
        return this;
    }

    @Override
    public RayMarchingObject translate(double x, double y, double z) {
        point.x += x;
        point.y += y;
        point.z += z;
        planeBias = -normal.dot(point);
        return this;
    }

    @Override
    public double getSignedDistance(Vector point) {
        return normal.dot(point) + planeBias;
    }

    @Override
    public Vector getNormal(Vector point) {
        return new Vector(normal);
    }
    
}
