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


    public static BufferedImage renderRasterization() {
        zBuffer = new double[camera.numPixelsX][camera.numPixelsY];
        frameBuffer = Color.getArray(camera.numPixelsX, camera.numPixelsY);

        if (Config.shadowMapping) {
            for (Light light : lights) {
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
        Light light = lights.get(lightIndex);
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

        for (int x = 0; x < camera.numPixelsX; x++) {
            for (int y = 0; y < camera.numPixelsY; y++) {
                Vector ray = camera.generateRay(x, y);
                double minDistance = Double.MAX_VALUE;
        
                for (Shape object : shapes) {
                    RayIntersection hit = object.getIntersection(camera.location, ray);
                    if (hit != null && hit.distance < minDistance) {
                        minDistance = hit.distance;
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
                color = trace(
                    camera.location, 
                    camera.generateRay(x, y), 
                    1, false, 0
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


    private static double schlick(double ior_i, double ior_t, double cos_i) {
        double R0 = (ior_i - ior_t) / (ior_i + ior_t);
        double _cos_i = 1 - cos_i;
        return R0 * R0 + (1 - R0 * R0) * _cos_i * _cos_i * _cos_i * _cos_i * _cos_i;
    }


    private static Color trace(Vector origin, Vector direction, double ior_t, boolean insideObject, int depth) {
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
            return null;
        }
        Material hitMaterial = hit.material;
        Vector hitNormal = hit.normal;
        Vector hitPoint = hit.location;
        Vector lightDirection = direction.getScaled(-1);
        
        if (hitMaterial.type == RayTracingType.DIFFUSE) {
            hitPoint.add(Config.rayHitPointBias, hitNormal);
            return hitMaterial.getRayTracingPhongColor(origin, hitPoint, hitNormal);
        }
        if (depth > Config.rayTracingMaxDepth) {
            return null;
        }
        if (hitMaterial.type == RayTracingType.MIRROR) {
            hitPoint.add(Config.rayHitPointBias, hitNormal);

            Color reflectionColor = trace(
                hitPoint, 
                lightDirection.getReflection(hitNormal),
                ior_t,
                insideObject,
                depth + 1
            );
            if (reflectionColor != null) {
                reflectionColor.filter(hitMaterial.color);
            }
            return reflectionColor;
        }
        if (hitMaterial.type == RayTracingType.TRANSPARENT) {
            Vector normal_t = hitNormal;
            Vector normal_i = hitNormal.getScaled(-1);
            double cos_t = lightDirection.dot(normal_t);
            double ior_i = insideObject ? 1 : hitMaterial.ior;
            double n = ior_t / ior_i;

            Vector reflectionOrigin = new Vector(hitPoint);
            reflectionOrigin.add(Config.rayHitPointBias, normal_t);

            Color reflectionColor = trace(
                reflectionOrigin, 
                lightDirection.getReflection(normal_t),
                ior_t,
                insideObject,
                depth + 1
            );
            double reflectionRatio = 1, refractionRatio = 0;
            Color refractionColor = null;

            double cos_i_sqr = 1 - n * n * (1 - cos_t * cos_t);

            if (cos_i_sqr >= 0) {
                double cos_i = Math.sqrt(cos_i_sqr);
                Vector incident = new Vector(
                    n * (cos_t * normal_t.x - lightDirection.x) + cos_i * normal_i.x,
                    n * (cos_t * normal_t.y - lightDirection.y) + cos_i * normal_i.y,
                    n * (cos_t * normal_t.z - lightDirection.z) + cos_i * normal_i.z
                );
                Vector refractionOrigin = new Vector(hitPoint);
                refractionOrigin.add(Config.rayHitPointBias, normal_i);

                refractionColor = trace(
                    refractionOrigin, 
                    incident,
                    ior_i,
                    !insideObject,
                    depth + 1
                );
                reflectionRatio = schlick(ior_i, ior_t, cos_t);
                refractionRatio = 1 - schlick(ior_i, ior_t, cos_i);
            } 
            if (reflectionColor == null && refractionColor == null) {
                return null;
            }
            Color result = new Color();
                
            if (reflectionColor != null) {
                reflectionColor.scale(reflectionRatio);
                result.add(reflectionColor);
            }
            if (refractionColor != null) {
                if (insideObject) {
                    refractionColor.filter(hitMaterial.color);
                }
                refractionColor.scale(refractionRatio);
                result.add(refractionColor);
            }
            return result;
        }
        return null;
    }
}
