package graphics3D;

public class LightSource {
    Vector location;
    Color diffuse, specular;

    public static Color ambient = new Color(1, 1, 1);

    public LightSource(Vector location, Color diffuse, Color specular) {
        this.location = location;
        this.diffuse = diffuse;
        this.specular = specular;
    }
}