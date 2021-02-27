package graphics3D;

import java.util.List;
import java.awt.image.BufferedImage;


public class Scene3D {
    Camera camera;
    double[][] zBuffer;
    BufferedImage frameBuffer;

    List<LightSource> lights;
    List<Mesh> objects;

    public Scene3D(Camera camera, List<LightSource> lights, List<Mesh> objects) {
        this.camera = camera;
        this.lights = lights;
        this.objects = objects;
        clear();
    }

    private void clear() {
        zBuffer = new double[camera.numPixelsX][camera.numPixelsY];
        frameBuffer = new BufferedImage(
            camera.numPixelsX, 
            camera.numPixelsY, 
            BufferedImage.TYPE_INT_RGB
        );
    }

    private BufferedImage downSample() {
        int numPixelsX = camera.numPixelsX;
        int numPixelsY = camera.numPixelsY;

        BufferedImage result = new BufferedImage(
            camera.numPixelsX / 2, 
            camera.numPixelsY / 2, 
            BufferedImage.TYPE_INT_RGB
        );

        Color color;
        int resultX = 0, resultY = 0;

        for (int x = 0; x < numPixelsX; x += 2) {
            resultY = 0;
            for (int y = 0; y < numPixelsY; y += 2) {
                color =   new Color(frameBuffer.getRGB(x    , y    ));
                color.add(new Color(frameBuffer.getRGB(x + 1, y    )));
                color.add(new Color(frameBuffer.getRGB(x    , y + 1)));
                color.add(new Color(frameBuffer.getRGB(x + 1, y + 1)));

                result.setRGB(resultX, resultY, new Color(0.25, color).getRGBhex());
                resultY++;
            }
            resultX++;
        }
        return result;
    }

    public void renderImage() {
        clear();
        for (Mesh object : objects) {
            object.render(this);
        }
    }

    public void renderZBuffer() {
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
                frameBuffer.setRGB(x, y, color);
            }
        }
    }

    public BufferedImage draw() {
        renderImage();
        return downSample();
    }

    public BufferedImage drawZBuffer() {
        renderZBuffer();
        return downSample();
    }
}
