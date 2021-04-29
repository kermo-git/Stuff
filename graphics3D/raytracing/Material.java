package graphics3D.raytracing;

import graphics3D.utils.Color;
import graphics3D.utils.Vector;

public class Material {

    public Color shade(
        Vector viewVector, Vector surfacePoint, Vector normal, 
        boolean inside, int recursionDepth
    ) {
        return new Color();
    };

    public static Vector getReflectedRay(Vector ray, Vector normal) {
        double dot = 2 * ray.dot(normal);
        
        return new Vector(
            ray.x - dot * normal.x,
            ray.y - dot * normal.y,
            ray.z - dot * normal.z
        );
    }

    // http://web.cse.ohio-state.edu/~shen.94/681/Site/Slides_files/reflection_refraction.pdf
    public static double schlick(double n1, double n2, double cos) {
        double R0 = (n1 - n2) / (n1 + n2);
        double _cos = 1 - cos;
        return R0 * R0 + (1 - R0 * R0) * _cos * _cos * _cos * _cos * _cos;
    }

    public static Vector getRefractedRay(Vector ray, Vector normal, double n1, double n2) {
        double n = n1 / n2;
        double cos_1 = -ray.dot(normal);
        double cos_2_sqr = 1 - n * n * (1 - cos_1 * cos_1);

        if (cos_2_sqr < 0) {
            return null;
        }
        double cos_2 = Math.sqrt(cos_2_sqr);

        return new Vector(
            n * (cos_1 * normal.x + ray.x) - cos_2 * normal.x,
            n * (cos_1 * normal.y + ray.y) - cos_2 * normal.y,
            n * (cos_1 * normal.z + ray.z) - cos_2 * normal.z
        );
    }
}
