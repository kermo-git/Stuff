package graphics3D;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.awt.image.BufferedImage;


public class Scene3D {
    public static Camera camera = (Config.antiAliasing) ?
        new Camera(2 * Config.screenSizeX, 2 * Config.screenSizeY, Config.cameraFOV) :
        new Camera(Config.screenSizeX, Config.screenSizeY, Config.cameraFOV);

    public static double[][] zBuffer = new double[camera.numPixelsX][camera.numPixelsY];
    public static Color[][] frameBuffer = Color.getArray(camera.numPixelsX, camera.numPixelsY);

    public static List<LightSource> lights = new ArrayList<>();
    public static List<Mesh> objects = new ArrayList<>();
    
    public static void addObjects(Mesh ...newObjects) {
        objects.addAll(Arrays.asList(newObjects));
    }
    public static void addLights(LightSource ...newLights) {
        lights.addAll(Arrays.asList(newLights));
    }


    public static BufferedImage draw() {
        for (LightSource light : lights) {
            for (Mesh object : objects) {
                for (Vertex v : object.vertices) {
                    v.projection = light.camera.project(v);
                }
                for (Triangle t : object.triangles) {
                    t.render(light.shadowBuffer, false);
                }
            }
        }
        for (Mesh object : objects) {
            object.calculateNormals();

            for (Vertex vertex : object.vertices) {
                vertex.color = object.material.illuminate(vertex, vertex.normal);
                vertex.projection = camera.project(vertex);
            }
            for (Triangle triangle : object.triangles) {
                triangle.render(zBuffer, true);
            }
        }
        if (Config.antiAliasing) {
            return Color.colorMatToReducedImg(frameBuffer);
        }
        return Color.colorMatToImg(frameBuffer);
    }


    public static BufferedImage drawZBuffer() {
        for (Mesh object : objects) {
            for (Triangle triangle : object.triangles) {
                triangle.render(zBuffer, false);
            }
        }
        if (Config.antiAliasing) {
            return Color.matToReducedGreyScaleImg(zBuffer);
        }
        return Color.matToGreyScaleImg(zBuffer);
    }


    public static BufferedImage drawShadowBuffer(int lightIndex) {
        LightSource light = lights.get(lightIndex);

        for (Mesh object : objects) {
            for (Vertex v : object.vertices) {
                v.projection = light.camera.project(v);
            }
            for (Triangle t : object.triangles) {
                t.render(light.shadowBuffer, false);
            }
        }
        if (Config.antiAliasing) {
            return Color.matToReducedGreyScaleImg(light.shadowBuffer);
        }
        return Color.matToGreyScaleImg(light.shadowBuffer);
    }
}
