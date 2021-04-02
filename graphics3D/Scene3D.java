package graphics3D;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.awt.image.BufferedImage;


public class Scene3D {
    Camera camera;
    double[][] zBuffer;
    Color[][] frameBuffer;

    List<LightSource> lights = new ArrayList<>();
    List<Mesh> objects = new ArrayList<>();

    public Scene3D(int numPixelsX, int numPixelsY, double FOVdegrees) {
        camera = new Camera(numPixelsX, numPixelsY, FOVdegrees);
        camera.lookAt(new Vector(0, 0, 0), new Vector(0, 0, 1));
        clear();
    }
    public void addObjects(Mesh ...objects) {
        this.objects.addAll(Arrays.asList(objects));
    }
    public void addLights(LightSource ...lights) {
        this.lights.addAll(Arrays.asList(lights));
    }

    private void clear() {
        zBuffer = new double[camera.numPixelsX][camera.numPixelsY];
        frameBuffer = Color.getArray(camera.numPixelsX, camera.numPixelsY);
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
                color = new Color();
                color.add(frameBuffer[x    ][y    ]);
                color.add(frameBuffer[x + 1][y    ]);
                color.add(frameBuffer[x    ][y + 1]);
                color.add(frameBuffer[x + 1][y + 1]);
                color.scale(0.25);

                result.setRGB(resultX, resultY, color.getRGBhex());
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
                frameBuffer[x][y] = new Color(norm, norm, norm);
            }
        }
    }

    public BufferedImage draw() {
        renderImage();
        return Color.downSampleMatrix(frameBuffer);
    }

    public BufferedImage drawZBuffer() {
        renderZBuffer();
        return Color.downSampleMatrix(frameBuffer);
    }
}
