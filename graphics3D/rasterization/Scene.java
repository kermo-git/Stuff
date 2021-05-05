package graphics3D.rasterization;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.awt.image.BufferedImage;

import graphics3D.utils.Camera;
import graphics3D.utils.Color;

public class Scene {
    public static Camera camera;
    public static double[][] depthMap;
    public static Color[][] frameBuffer;

    public static List<Light> lights;
    public static List<TriangleMesh> objects;

    public static void clearScene() {
        camera = (Config.ANTI_ALIASING) ?
            new Camera(2 * Config.SCREENSIZE_X, 2 * Config.SCREENSIZE_Y, Config.CAMERA_FOV) :
            new Camera(Config.SCREENSIZE_X, Config.SCREENSIZE_Y, Config.CAMERA_FOV);

        lights = new ArrayList<>();
        objects = new ArrayList<>();
    }
    static { clearScene(); }

    public static void addObjects(TriangleMesh ...newTriangleMeshes) {
        objects.addAll(Arrays.asList(newTriangleMeshes));
    }
    public static void addLights(Light ...newLights) {
        lights.addAll(Arrays.asList(newLights));
    }


    public static BufferedImage renderDepthMap() {
        depthMap = new double[camera.numPixelsX][camera.numPixelsY];

        for (TriangleMesh obj : objects) {
            for (Vertex vertex : obj.vertices) {
                vertex.projection = camera.project(vertex);
            }
            for (Triangle triangle : obj.triangles) {
                triangle.rasterize(depthMap, null);
            }
        }

        if (Config.ANTI_ALIASING) {
            return Color.matToReducedGreyScaleImg(depthMap);
        }
        return Color.matToGreyScaleImg(depthMap);
    }


    public static BufferedImage render() {
        frameBuffer = Color.getArray(camera.numPixelsX, camera.numPixelsY);
        long start = System.nanoTime();
        
        if (Config.SHADOWS) {
            for (Light light : lights) {
                light.initShadowBuffer();
    
                for (TriangleMesh obj : objects) {
                    for (Vertex vertex : obj.vertices) {
                        vertex.projection = light.camera.project(vertex);
                    }
                    for (Triangle triangle : obj.triangles) {
                        triangle.rasterize(light.shadowMap, null);
                    }
                }
            }
        }
        depthMap = new double[camera.numPixelsX][camera.numPixelsY];

        for (TriangleMesh obj : objects) {
            for (Vertex vertex : obj.vertices) {
                vertex.projection = camera.project(vertex);
            }
            for (Triangle triangle : obj.triangles) {
                triangle.rasterize(depthMap, frameBuffer);
            }
        }
        double duration = (double) (System.nanoTime() - start);
        System.out.println("Rendering took " + duration / 1e9 + " seconds");
        
        if (Config.ANTI_ALIASING) {
            return Color.colorMatToReducedImg(frameBuffer);
        }
        return Color.colorMatToImg(frameBuffer);
    }
}
