package graphics3D;

public class LightSource {
    Vector location;
    double FOVdegrees;

    double[][] shadowBuffer;
    Camera camera;

    Color diffuse, specular;
    public static Color ambient = new Color(1, 1, 1);

    public LightSource(
        Vector location, Vector target, 
        Color diffuse, Color specular
    ) {
        this.location = location;
        this.diffuse = diffuse;
        this.specular = specular;

        int numPixels = Config.shadowResolution;

        shadowBuffer = new double[numPixels][numPixels];
        camera = new Camera(numPixels, numPixels, Config.lightFOV);
        camera.lookAt(location, target);
    }

    public boolean isIlluminating(Vector point) {
        if (!Config.renderShadows) {
            return true;
        }
        Pixel p = camera.project(point);

        if (p != null) {
            double bufferValue = shadowBuffer[(int) p.pixelX][(int) p.pixelY];
            return p.zRec + Config.shadowBufferBias > bufferValue;
        }
        return true;
    }
}