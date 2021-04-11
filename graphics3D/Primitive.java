package graphics3D;

public abstract class Primitive {
    Material material;
    abstract double getIntersectionDistance(Vector rayOrigin, Vector rayDirection);
    abstract Vector getNormal(Vector surfacePoint);
}
