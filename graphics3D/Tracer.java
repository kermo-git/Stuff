package graphics3D;

import java.util.List;

class Line {
    Vector origin;
    Vector direction;

    Line(Vector origin, Vector direction) {
        this.origin = origin;
        this.direction = direction;
    }

    void normalize() {
        direction.normalize();
    }
}

public class Tracer {
    int maxDepth;
    List<Triangle> triangles;
    List<LightSource> lights;
    
    Line getHitPointNormal(Triangle triangle, Line ray) {
        // TODO
        return null;
    }

    Line getRefractedRay(Line normal, Line ray, double refractionIndex) {
        // TODO
        return null;
    }

    Line getReflectedRay(Line normal, Line ray) {
        // TODO
        return null;       
    }

    double fresnel(Line normal, Line ray, double refractionIndex) {
        return 1;
    }

    Color trace(Line ray, int depth) {
        double minDistance = Double.MAX_VALUE;
        Triangle hit = null;
        Line hitNormal = null;
        Color result = new Color();

        for (Triangle triangle : triangles) {
            Line normal = getHitPointNormal(triangle, ray);
            if (normal != null) {
                double distance = new Vector(ray.origin, normal.origin).length();
                if (distance < minDistance) {
                    hit = triangle;
                    hitNormal = normal;
                    minDistance = distance;
                }
            }
        }
        if (hit == null) {
            return result;
        }
        Color diffuseColor = new Color();
        for (LightSource light : lights) {
            Vector lightVector = new Vector(light.location, hitNormal.origin);
            Line lightRay = new Line(light.location, lightVector);

            for (Triangle triangle : triangles) {
                if (getHitPointNormal(triangle, lightRay) != null) {
                    break;
                }
            }
        }
        if (depth < maxDepth) {
            Line refraction = getRefractedRay(hitNormal, ray, hit.material.refractionIndex);
            Line reflection = getReflectedRay(hitNormal, ray);
    
            Color reflectionColor = trace(reflection, depth - 1);
            Color refractionColor = trace(refraction, depth - 1);
            double f = fresnel(hitNormal, ray, hit.material.refractionIndex);

        }
        return result;
    }
}
