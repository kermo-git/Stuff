package graphics3D;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import java.awt.image.BufferedImage;

import graphics3D.shapes.TriangleMesh;
import graphics3D.shapes.Shape;
import graphics3D.shapes.Triangle;
import graphics3D.shapes.Vertex;


public class Scene3D {
    public static Camera camera;

    public static double[][] zBuffer;
    public static Color[][] frameBuffer;

    public static List<LightSource> lights;
    public static List<TriangleMesh> triangleMeshes;
    public static List<Shape> primitives;

    public static void clearScene() {
        camera = (Config.antiAliasing) ?
            new Camera(2 * Config.screenSizeX, 2 * Config.screenSizeY, Config.cameraFOV) :
            new Camera(Config.screenSizeX, Config.screenSizeY, Config.cameraFOV);

        lights = new ArrayList<>();
        triangleMeshes = new ArrayList<>();
        primitives = new ArrayList<>();
    }
    static { clearScene(); }


    public static void addTriangleMeshObjects(TriangleMesh ...newObjects) {
        for (TriangleMesh mesh : newObjects) {
            primitives.addAll(mesh.triangles);
        }
        triangleMeshes.addAll(Arrays.asList(newObjects));
    }
    public static void addObjects(Shape ...newObjects) {
        primitives.addAll(Arrays.asList(newObjects));
    }
    public static void addLights(LightSource ...newLights) {
        lights.addAll(Arrays.asList(newLights));
    }


    public static BufferedImage renderRasterization() {
        zBuffer = new double[camera.numPixelsX][camera.numPixelsY];
        frameBuffer = Color.getArray(camera.numPixelsX, camera.numPixelsY);

        if (Config.shadowMapping) {
            for (LightSource light : lights) {
                light.initShadowBuffer();
    
                for (TriangleMesh object : triangleMeshes) {
                    for (Vertex vertex : object.vertices) {
                        vertex.projection = light.camera.project(vertex);
                    }
                    for (Triangle triangle : object.triangles) {
                        triangle.rasterize(light.shadowBuffer, false);
                    }
                }
            }
        }
        for (TriangleMesh object : triangleMeshes) {
            for (Vertex vertex : object.vertices) {
                vertex.color = object.material.getRasterizationPhongColor(camera.location, vertex, vertex.normal);
                vertex.projection = camera.project(vertex);
            }
            for (Triangle triangle : object.triangles) {
                triangle.rasterize(zBuffer, true);
            }
        }
        if (Config.antiAliasing) {
            return Color.colorMatToReducedImg(frameBuffer);
        }
        return Color.colorMatToImg(frameBuffer);
    }


    public static BufferedImage renderZBuffer() {
        zBuffer = new double[camera.numPixelsX][camera.numPixelsY];

        for (TriangleMesh object : triangleMeshes) {
            for (Vertex vertex : object.vertices) {
                vertex.projection = camera.project(vertex);
            }
            for (Triangle triangle : object.triangles) {
                triangle.rasterize(zBuffer, false);
            }
        }
        if (Config.antiAliasing) {
            return Color.matToReducedGreyScaleImg(zBuffer);
        }
        return Color.matToGreyScaleImg(zBuffer);
    }


    public static BufferedImage renderShadowBuffer(int lightIndex) {
        LightSource light = lights.get(lightIndex);
        light.initShadowBuffer();

        for (TriangleMesh object : triangleMeshes) {
            for (Vertex v : object.vertices) {
                v.projection = light.camera.project(v);
            }
            for (Triangle triangle : object.triangles) {
                triangle.rasterize(light.shadowBuffer, false);
            }
        }
        if (Config.antiAliasing) {
            return Color.matToReducedGreyScaleImg(light.shadowBuffer);
        }
        return Color.matToGreyScaleImg(light.shadowBuffer);
    }

    
    public static BufferedImage renderRayCastingZBuffer() {
        zBuffer = new double[camera.numPixelsX][camera.numPixelsY];

        for (TriangleMesh object : triangleMeshes) {
            object.normalizeVertexNormals();
        }
        for (int x = 0; x < camera.numPixelsX; x++) {
            for (int y = 0; y < camera.numPixelsY; y++) {
                Vector ray = camera.generateRay(x, y);
                double minDistance = Double.MAX_VALUE;
        
                for (TriangleMesh object : triangleMeshes) {
                    for (Triangle triangle : object.triangles) {
                        RayIntersection hit = triangle.getIntersection(camera.location, ray);
                        if (hit != null && hit.distance < minDistance) {
                            minDistance = hit.distance;
                        }
                    }
                }
                if (minDistance < Double.MAX_VALUE) {
                    zBuffer[x][y] = minDistance;
                }
            }
        }
        if (Config.antiAliasing) {
            return Color.matToReducedGreyScaleImg(zBuffer);
        }
        return Color.matToGreyScaleImg(zBuffer);
    }


    public static BufferedImage renderRayTracing() {
        frameBuffer = Color.getArray(camera.numPixelsX, camera.numPixelsY);

        for (TriangleMesh object : triangleMeshes) {
            object.normalizeVertexNormals();
        }
        Color color;
        for (int x = 0; x < camera.numPixelsX; x++) {
            for (int y = 0; y < camera.numPixelsY; y++) {
                color = castRay(
                    camera.location, 
                    camera.generateRay(x, y), 
                    0
                );
                if (color != null) {
                    frameBuffer[x][y] = color;
                }
            }
        }
        if (Config.antiAliasing) {
            return Color.colorMatToReducedImg(frameBuffer);
        }
        return Color.colorMatToImg(frameBuffer);
    }


    private static double fresnel(Vector normal, Vector ray, double refractionIndex) {
        // TODO
        return 1;
    }


    private static Color castRay(Vector origin, Vector direction, int depth) {
        double minDistance = Double.MAX_VALUE;
        RayIntersection hit = null;

        for (Shape object : primitives) {
            RayIntersection tmpHit = object.getIntersection(origin, direction);
            if (tmpHit != null && tmpHit.distance < minDistance) {
                minDistance = tmpHit.distance;
                hit = tmpHit;
            }
        }
        if (hit == null) {
            return null;
        }
        Material material = hit.material;
        Vector hitNormal = hit.normal;
        Vector hitPoint = hit.location;
        hitPoint.add(Config.rayHitPointBias, hitNormal);

        Vector oppositeDirection = direction.getScaled(-1);
        
        if (hitNormal.dot(oppositeDirection) <= 0 &&
          !(material.type == RayTracingType.TRANSPARENT)) {
            return null;
        }
        if (material.type == RayTracingType.DIFFUSE) {
            return material.getRayTracingPhongColor(origin, hitPoint, hitNormal);
        }
        else if (depth < Config.rayTracingMaxDepth) {
            Color result = new Color();
            result.add(1, material.ambient, Config.sceneAmbientColor);

            Color reflectionColor = castRay(
                hitPoint, 
                oppositeDirection.getReflection(hitNormal),
                depth + 1
            );
            if (material.type == RayTracingType.MIRROR) {
                if (reflectionColor != null) {
                    result.add(reflectionColor);
                }
            }
            else if (material.type == RayTracingType.TRANSPARENT) {
                Color refractionColor = castRay(
                    hitPoint, 
                    direction.getRefraction(
                        hitNormal, 
                        material.refractionIndex
                    ),
                    depth + 1
                );
                double f = fresnel(hitNormal, direction, material.refractionIndex);

                if (reflectionColor != null) {
                    result.add(1, material.color, reflectionColor.getScaled(f));
                }
                if (refractionColor != null) {
                    result.add(1, material.color, refractionColor.getScaled(1 - f));
                }
            }
            return result;
        }
        return null;
    }
}
