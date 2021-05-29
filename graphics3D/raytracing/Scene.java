package graphics3D.raytracing;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.awt.image.BufferedImage;

import graphics3D.raytracing.shapes.RTobject;
import graphics3D.utils.Camera;
import graphics3D.utils.Color;
import graphics3D.utils.Vector;


public class Scene {
    public static Camera camera;
    public static Color[][] frameBuffer;

    public static List<Light> lights;
    public static List<RTobject> objects;


    public static void clearScene() {
        camera = (Config.ANTI_ALIASING) ?
            new Camera(2 * Config.SCREENSIZE_X, 2 * Config.SCREENSIZE_Y, Config.CAMERA_FOV) :
            new Camera(Config.SCREENSIZE_X, Config.SCREENSIZE_Y, Config.CAMERA_FOV);

        lights = new ArrayList<>();
        objects = new ArrayList<>();
    }
    static { clearScene(); }

    public static void addObjects(RTobject ...newShapes) {
        objects.addAll(Arrays.asList(newShapes));
    }
    public static void addLights(Light ...newLights) {
        lights.addAll(Arrays.asList(newLights));
    }


    public static BufferedImage renderDepthMap() {
        double[][] depthMap = new double[camera.numPixelsX][camera.numPixelsY];

        Vector ray;
        int x, y;
        double minDistance;

        for (x = 0; x < camera.numPixelsX; x++) {
            for (y = 0; y < camera.numPixelsY; y++) {
                ray = camera.generateRay(x, y);
                minDistance = Double.MAX_VALUE;
        
                for (RTobject obj : objects) {
                    RayHit hit = obj.getIntersection(camera.location, ray);
                    if (hit != null && hit.distance < minDistance) {
                        minDistance = hit.distance;
                    }
                }
                if (minDistance < Double.MAX_VALUE) {
                    depthMap[x][y] = minDistance;
                }
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

        int x, y;
        Color color;

        for (x = 0; x < camera.numPixelsX; x++) {
            for (y = 0; y < camera.numPixelsY; y++) {

                color = castRay(
                    camera.location, 
                    camera.generateRay(x, y), 
                    false, 0
                );
                frameBuffer[x][y] = color;
            }
        }
        double duration = (double) (System.nanoTime() - start);
        System.out.println("Rendering took " + duration / 1e9 + " seconds");
        
        if (Config.ANTI_ALIASING) {
            return Color.colorMatToReducedImg(frameBuffer);
        }
        return Color.colorMatToImg(frameBuffer);
    }

    
    public static Color castRay(Vector origin, Vector direction, boolean inside, int depth) {
        if (depth > Config.MAX_RAY_BOUNCES) {
            return new Color();
        }
        double minDistance = Double.MAX_VALUE;
        RayHit hit = null;

        for (RTobject object : objects) {
            RayHit tmpHit = object.getIntersection(origin, direction);
            if (tmpHit != null && tmpHit.distance < minDistance) {
                minDistance = tmpHit.distance;
                hit = tmpHit;
            }
        }
        if (hit == null) {
            return new Color();
        }
        return hit.material.shade(direction, hit.hitPoint, hit.normal, inside, depth);
    }
}
