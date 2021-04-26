package graphics3D;

public class Light {
    public Vector location, target;

    double[][] shadowMap;
    Camera camera;

    public Color color;

    public Light(Vector location, Vector target, Color color) {
        this.location = location;
        this.target = target;
        this.color = color;
    }

    public Light(Vector location, Color diffuse) {
        this(location, new Vector(), diffuse);
    }

    public void initShadowBuffer() {
        int numPixels = Config.shadowMapResolution;
        shadowMap = new double[numPixels][numPixels];
        camera = new Camera(numPixels, numPixels, Config.shadowMapFOV);
        camera.lookAt(location, target);
    }

    public boolean isIlluminating(Vector point) {
        Pixel p = camera.project(point);

        if (p != null) {
            double bufferValue = shadowMap[(int) p.pixelX][(int) p.pixelY];
            return p.zRec + Config.shadowBufferBias > bufferValue;
        }
        return true;
    }
}