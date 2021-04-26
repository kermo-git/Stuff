package graphics3D.materials;

import graphics3D.*;

public class Transparent extends Material {
    Color color = new Color(0xFFFFFF);
    double ior = 1.5, reflectivity = 0;

    public Transparent() {}

    public Transparent(double ior) {
        this.ior = ior;
    }
    public Transparent(Color color) {
        this.color = color;
    }
    public Transparent(Color color, double ior) {
        this.color = color;
        this.ior = ior;
    }
    public Transparent(Color color, double ior, double reflectivity) {
        this.color = color;
        this.ior = ior;
        this.reflectivity = reflectivity;
    }

    @Override
    public Color shade(Vector viewVector, Vector surfacePoint, Vector normal, boolean inside, int recursionDepth) {
        Vector reflectionOrigin = new Vector(surfacePoint);
        reflectionOrigin.add(Config.rayHitPointBias, normal);

        Color reflectionColor = Scene3D.castRay(
            reflectionOrigin, 
            getReflectedRay(viewVector, normal),
            inside,
            recursionDepth + 1
        );
        Vector refractedRay = null;

        if (inside) {
            refractedRay = getRefractedRay(viewVector, normal, ior, 1);
        } else {
            refractedRay = getRefractedRay(viewVector, normal, 1, ior);
        }
        if (refractedRay != null) {
            Vector refractionOrigin = new Vector(surfacePoint);
            refractionOrigin.add(-Config.rayHitPointBias, normal);

            Color refractionColor = Scene3D.castRay(
                refractionOrigin, 
                refractedRay,
                !inside,
                recursionDepth + 1
            );
            if (!inside) {
                refractionColor.filter(color);
            }
            double outsideCos = inside ? 
                -refractedRay.dot(normal) : 
                -viewVector.dot(normal);
                
            double reflectionRatio = reflectivity + (1 - reflectivity) * schlick(ior, 1, outsideCos);

            reflectionColor.scale(reflectionRatio);
            refractionColor.scale(1 - reflectionRatio);
            refractionColor.add(reflectionColor);

            return refractionColor;
        } else {
            return reflectionColor;
        }
    }   
}
