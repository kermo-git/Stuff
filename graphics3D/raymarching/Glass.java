package graphics3D.raymarching;

import graphics3D.utils.Color;
import graphics3D.utils.Vector;

public class Glass extends Material {
    Color color = new Color(0xFFFFFF);
    double reflectivity = 0, ior = 1.5;
    double oppositeReflectivity = 1;

    public Glass() {}
    public Glass(int colorHEX) {
        this.color = new Color(colorHEX);
    }
    public Glass(int colorHEX, double reflectivity, double ior) {
        this.color = new Color(colorHEX);
        this.reflectivity = reflectivity;
        oppositeReflectivity = 1 - reflectivity;
        this.ior = ior;
    }

    @Override
    public Color shade(Vector incidentRay, Vector surfacePoint, Vector normal, int depth) {
        if (depth > Config.MAX_BOUNCES) {
            return new Color();
        }
        boolean inside = false;
        double iCos = -incidentRay.dot(normal);

        if (iCos < 0) {
            inside = true;
            normal = normal.getScaled(-1);
            iCos = -iCos;
        }

        Color result = new Color();

        if (Config.REFLECTIONS) {
            Vector reflectedRay = getReflectedRay(incidentRay, normal);
            Vector reflectionOrigin = getPointOnRay(surfacePoint, normal, Config.RAY_HIT_BIAS);
            result = Scene.castRay(reflectionOrigin, reflectedRay, depth + 1);
        }

        Vector refractedRay = inside ?
            getRefractedRay(incidentRay, normal, ior, 1) :
            getRefractedRay(incidentRay, normal, 1, ior);

        if (refractedRay != null) {
            Vector refractionOrigin = getPointOnRay(surfacePoint, normal, -Config.RAY_HIT_BIAS);
            Color refractionColor = Scene.castRay(refractionOrigin, refractedRay, depth + 1);

            if (!inside) {
                refractionColor.filter(color);
            }
            if (Config.REFLECTIONS) {
                double reflectionRatio = 0;

                if (inside) {
                    double tCos = -refractedRay.dot(normal);
                    reflectionRatio = schlick(ior, 1, tCos);
                } else {
                    reflectionRatio = schlick(1, ior, iCos);
                }
                reflectionRatio = reflectivity + oppositeReflectivity * reflectionRatio;
                refractionColor.scale(1 - reflectionRatio);
                result.scale(reflectionRatio);
            }
            result.add(refractionColor);
        }
        return result;
    }
}
