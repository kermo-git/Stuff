package graphics3D;

public class LightSource {
    Vector location, target;

    double[][] shadowBuffer;
    Camera camera;

    Color color;

    public LightSource(Vector location, Vector target, Color color) {
        this.location = location;
        this.target = target;
        this.color = color;
    }

    public LightSource(Vector location, Color diffuse) {
        this(location, new Vector(), diffuse);
    }

    public void initShadowBuffer() {
        int numPixels = Config.shadowResolution;
        shadowBuffer = new double[numPixels][numPixels];
        camera = new Camera(numPixels, numPixels, Config.shadowMapFOV);
        camera.lookAt(location, target);
    }

    public boolean isIlluminating(Vector point) {
        if (!Config.shadowMapping) {
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