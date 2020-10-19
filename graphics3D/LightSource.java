package graphics3D;

public class LightSource {
    Vector location;
    Color diffuseColor;
    Color specularColor;

    public LightSource(Vector location, Color diffuseColor, Color specularColor) {
        this.location = location;
        this.diffuseColor = diffuseColor;
        this.specularColor = specularColor;
    }
}