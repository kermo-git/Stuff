package graphics3D.shapes;

import graphics3D.Matrix;
import graphics3D.RayIntersection;
import graphics3D.Vector;

public class Plane extends Shape {
    Vector normal;
    Vector point;

    public Plane(Vector point, Vector normal) {
        this.point = point;
        this.normal = normal;
    }

    @Override
    public RayIntersection getIntersection(Vector rayOrigin, Vector rayDirection) {
        return null;
    }

    @Override
    public void transform(Matrix rotation, Matrix translation) {
        rotation.transform(normal);
        rotation.transform(point);
        translation.transform(point);
    }

    @Override
    public void transform(Matrix translation) {
        translation.transform(point);
    }
    
}
