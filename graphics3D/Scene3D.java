package graphics3D;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.awt.image.BufferedImage;

import graphics3D.shapes.TriangleMesh;
import graphics3D.Config.ShadowType;
import graphics3D.shapes.Shape;
import graphics3D.shapes.Triangle;
import graphics3D.shapes.Vertex;


public class Scene3D {

    public static Camera camera;
    public static double[][] depthMap;
    public static Color[][] frameBuffer;

    public static List<Light> lights;
    public static List<TriangleMesh> triangleMeshes;
    public static List<Shape> shapes;


    public static void clearScene() {
        camera = (Config.antiAliasing) ?
            new Camera(2 * Config.screenSizeX, 2 * Config.screenSizeY, Config.cameraFOV) :
            new Camera(Config.screenSizeX, Config.screenSizeY, Config.cameraFOV);

        lights = new ArrayList<>();
        triangleMeshes = new ArrayList<>();
        shapes = new ArrayList<>();
    }
    static { clearScene(); }


    public static void addTriangleMeshes(TriangleMesh ...newTriangleMeshes) {
        for (TriangleMesh mesh : newTriangleMeshes) {
            shapes.addAll(mesh.triangles);
        }
        triangleMeshes.addAll(Arrays.asList(newTriangleMeshes));
    }
    public static void addShapes(Shape ...newShapes) {
        shapes.addAll(Arrays.asList(newShapes));
    }
    public static void addLights(Light ...newLights) {
        lights.addAll(Arrays.asList(newLights));
    }


    public static BufferedImage renderDepthMap() {
        depthMap = new double[camera.numPixelsX][camera.numPixelsY];

        if (Config.initialRasterization) {
            for (TriangleMesh object : triangleMeshes) {
                for (Vertex vertex : object.vertices) {
                    vertex.projection = camera.project(vertex);
                }
                for (Triangle triangle : object.triangles) {
                    triangle.rasterize(depthMap, false);
                }
            }
        } else {
            Vector ray;
            int x, y;
            double minDistance;

            for (x = 0; x < camera.numPixelsX; x++) {
                for (y = 0; y < camera.numPixelsY; y++) {
                    ray = camera.generateRay(x, y);
                    minDistance = Double.MAX_VALUE;
            
                    for (Shape object : shapes) {
                        RayIntersection hit = object.getIntersection(camera.location, ray);
                        if (hit != null && hit.distance < minDistance) {
                            minDistance = hit.distance;
                        }
                    }
                    if (minDistance < Double.MAX_VALUE) {
                        depthMap[x][y] = minDistance;
                    }
                }
            }
        }

        if (Config.antiAliasing) {
            return Color.matToReducedGreyScaleImg(depthMap);
        }
        return Color.matToGreyScaleImg(depthMap);
    }


    public static BufferedImage render() {
        frameBuffer = Color.getArray(camera.numPixelsX, camera.numPixelsY);

        if (Config.shadowType == ShadowType.SHADOW_MAPPING) {
            for (Light light : lights) {
                light.initShadowBuffer();
    
                for (TriangleMesh object : triangleMeshes) {
                    for (Vertex vertex : object.vertices) {
                        vertex.projection = light.camera.project(vertex);
                    }
                    for (Triangle triangle : object.triangles) {
                        triangle.rasterize(light.shadowMap, false);
                    }
                }
            }
        }
        if (Config.initialRasterization) {
            depthMap = new double[camera.numPixelsX][camera.numPixelsY];

            for (TriangleMesh object : triangleMeshes) {
                for (Vertex vertex : object.vertices) {
                    vertex.projection = camera.project(vertex);
                }
                for (Triangle triangle : object.triangles) {
                    triangle.rasterize(depthMap, true);
                }
            }
        } else {
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
        }
        if (Config.antiAliasing) {
            return Color.colorMatToReducedImg(frameBuffer);
        }
        return Color.colorMatToImg(frameBuffer);
    }

    
    public static Color castRay(Vector origin, Vector direction, boolean inside, int depth) {
        if (depth > Config.rayTracingMaxDepth) {
            return new Color();
        }
        double minDistance = Double.MAX_VALUE;
        RayIntersection hit = null;

        for (Shape object : shapes) {
            RayIntersection tmpHit = object.getIntersection(origin, direction);
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
