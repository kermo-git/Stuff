package graphics3D.raymarching;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.awt.image.BufferedImage;

import graphics3D.raymarching.shapes.RMobject;
import graphics3D.utils.Camera;
import graphics3D.utils.Color;
import graphics3D.utils.Vector;


public class Scene {
    public static Camera camera;
    public static Color[][] frameBuffer;

    public static List<Light> lights;
    public static List<RMobject> objects;


    public static void clearScene() {
        camera = (Config.ANTI_ALIASING) ?
            new Camera(2 * Config.SCREENSIZE_X, 2 * Config.SCREENSIZE_Y, Config.CAMERA_FOV) :
            new Camera(Config.SCREENSIZE_X, Config.SCREENSIZE_Y, Config.CAMERA_FOV);

        lights = new ArrayList<>();
        objects = new ArrayList<>();
    }
    static { clearScene(); }

    public static void addObjects(RMobject ...newShapes) {
        objects.addAll(Arrays.asList(newShapes));
    }
    public static void addLights(Light ...newLights) {
        lights.addAll(Arrays.asList(newLights));
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
                    0
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

    public static Vector getPointOnRay(Vector origin, Vector direction, double distance) {
        return new Vector(
            origin.x + distance * direction.x,
            origin.y + distance * direction.y,
            origin.z + distance * direction.z
        );
    }
    
    public static Color castRay(Vector origin, Vector direction, int depth) {
        Vector currentPosition;
        double traveledDistance = 0;

        while (traveledDistance < Config.MAX_DIST) {
            currentPosition = getPointOnRay(origin, direction, traveledDistance);
            double minDistance = Double.MAX_VALUE;

            for (RMobject obj : objects) {
                double distance = Math.abs(obj.getSignedDistance(currentPosition));
    
                if (distance <= Config.RAY_HIT_THRESHOLD) {
                    return obj.material.shade(direction, currentPosition, obj.getNormal(currentPosition), depth);
                }
                if (distance < minDistance)
                    minDistance = distance;
            }
            traveledDistance += minDistance;
        }
        return new Color();
    }
}
