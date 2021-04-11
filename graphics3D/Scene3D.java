package graphics3D;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.awt.image.BufferedImage;


public class Scene3D {
    public static Camera camera;

    public static double[][] zBuffer;
    public static Color[][] frameBuffer;

    public static List<LightSource> lights;
    public static List<Mesh> objects;
    public static List<Primitive> primitives;

    public static void clearScene() {
        camera = (Config.antiAliasing) ?
            new Camera(2 * Config.screenSizeX, 2 * Config.screenSizeY, Config.cameraFOV) :
            new Camera(Config.screenSizeX, Config.screenSizeY, Config.cameraFOV);

        lights = new ArrayList<>();
        objects = new ArrayList<>();
        primitives = new ArrayList<>();
    }
    static { clearScene(); }


    public static void addObjects(Mesh ...newObjects) {
        for (Mesh object : newObjects) {
            primitives.addAll(object.triangles);
        }
        objects.addAll(Arrays.asList(newObjects));
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
    
                for (Mesh object : objects) {
                    for (Vertex vertex : object.vertices) {
                        vertex.projection = light.camera.project(vertex);
                    }
                    for (Triangle triangle : object.triangles) {
                        triangle.rasterize(light.shadowBuffer, false);
                    }
                }
            }
        }
        for (Mesh object : objects) {
            object.doNormalCalculations();

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

        for (Mesh object : objects) {
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

        for (Mesh object : objects) {
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

        for (Mesh object : objects) {
            object.doNormalCalculations();
        }
        for (int x = 0; x < camera.numPixelsX; x++) {
            for (int y = 0; y < camera.numPixelsY; y++) {
                Vector ray = camera.generateRay(x, y);
                double distance = Double.MAX_VALUE;
        
                for (Mesh object : objects) {
                    for (Triangle triangle : object.triangles) {
                        double t = triangle.getIntersectionDistance(camera.location, ray);
                        if (t > 0 && t < distance) {
                            distance = t;
                        }
                    }
                }
                if (distance < Double.MAX_VALUE) {
                    zBuffer[x][y] = distance;
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

        for (Mesh object : objects) {
            object.doNormalCalculations();
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
        double hitDistance = Double.MAX_VALUE;
        Primitive hitObject = null;
        Vector hitPoint = null;
        Vector hitNormal = null;

        for (Primitive object : primitives) {
            double distance = object.getIntersectionDistance(origin, direction);
            if (distance > 0 && distance < hitDistance) {
                hitObject = object;
                hitDistance = distance;
            }
        }
        if (hitObject == null) {
            return null;
        }
        hitPoint = new Vector(origin);
        hitPoint.add(hitDistance, direction);
        hitNormal = hitObject.getNormal(hitPoint);
        hitPoint.add(Config.rayHitPointBias, hitNormal);

        Vector oppositeDirection = direction.getScaled(-1);
        Material material = hitObject.material;
        
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
