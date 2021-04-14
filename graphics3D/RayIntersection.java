package graphics3D;

public class RayIntersection {
    public double distance;
    public Vector location;
    public Vector normal;
    public Material material;

    public RayIntersection(double distance, Vector location, Vector normal, Material material) {
        this.distance = distance;
        this.location = location;
        this.normal = normal;
        this.material = material;
    }    
}