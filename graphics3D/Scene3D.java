package graphics3D;

import java.util.List;
import java.awt.image.BufferedImage;


public class Scene3D {
    Camera camera;
    double[][] zBuffer;
    BufferedImage canvas;

    List<LightSource> lights;
    List<Mesh> objects;

    public Scene3D(Camera camera, List<LightSource> lights, List<Mesh> objects) {
        this.camera = camera;
        this.lights = lights;
        this.objects = objects;
        clear();
    }

    private void clear() {
        zBuffer = new double[camera.screenWidth][camera.screenHeight];
        canvas = new BufferedImage(
            camera.screenWidth, 
            camera.screenHeight, 
            BufferedImage.TYPE_INT_RGB
        );
    }

    public BufferedImage renderZBuffer() {
        renderImage();
        double max = 0, min = Double.MAX_VALUE;

        for (int x = 0; x < zBuffer.length; x++) {
            for (int y = 0; y < zBuffer[0].length; y++) {
                double value = zBuffer[x][y];
                if (value > max) {
                    max = value;
                }
                if (value < min) {
                    min = value;
                }
            }
        }
        double diff = max - min;
        for (int x = 0; x < zBuffer.length; x++) {
            for (int y = 0; y < zBuffer[0].length; y++) {
                double norm = (zBuffer[x][y] - min) / diff;
                int color = new Color(norm, norm, norm).getRGBhex();
                canvas.setRGB(x, y, color);
            }
        }
        return canvas;
    }
 
    public BufferedImage renderImage() {
        clear();
        for (Mesh object : objects) {
            object.render(this);
        }
        return canvas;
    }
}
