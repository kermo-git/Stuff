package graphics3D.shapes;

import graphics3D.Material;
import graphics3D.Matrix;
import graphics3D.Vector;
import graphics3D.RayIntersection;

public abstract class Shape {
    public Material material;

    public abstract RayIntersection getIntersection(Vector rayOrigin, Vector rayDirection);

    public abstract void transform(Matrix rotation, Matrix translation);
    
    public abstract void transform(Matrix translation);
}
