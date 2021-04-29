package graphics3D.rasterization;

import graphics3D.utils.Camera;
import graphics3D.utils.Color;
import graphics3D.utils.Pixel;
import graphics3D.utils.Vector;

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

    public void initShadowBuffer() {
        int numPixels = Config.SHADOW_RESOLUTION;
        shadowMap = new double[numPixels][numPixels];
        camera = new Camera(numPixels, numPixels, Config.SHADOWMAP_FOV);
        camera.lookAt(location, target);
    }

    public boolean isIlluminating(Vector point) {
        Pixel p = camera.project(point);

        if (p != null) {
            double bufferValue = shadowMap[(int) p.pixelX][(int) p.pixelY];
            return p.zRec + Config.SHADOWMAP_BIAS > bufferValue;
        }
        return true;
    }
}