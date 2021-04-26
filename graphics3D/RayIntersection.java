package graphics3D;

import graphics3D.materials.Material;

public class RayIntersection {
    public double distance;
    public Vector hitPoint;
    public Vector normal;
    public Material material;
    
    public RayIntersection(double distance, Vector location, Vector normal, Material material) {
        this.distance = distance;
        this.hitPoint = location;
        this.normal = normal;
        this.material = material;
    }    
}