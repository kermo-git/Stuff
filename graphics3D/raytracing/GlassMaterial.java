package graphics3D.raytracing;

import graphics3D.utils.Color;
import graphics3D.utils.Vector;

public class GlassMaterial extends Material {
    Color color = new Color(0xFFFFFF);
    double ior = 1.5, reflectivity = 0;

    public GlassMaterial() {}

    public GlassMaterial(double ior) {
        this.ior = ior;
    }
    public GlassMaterial(Color color) {
        this.color = color;
    }
    public GlassMaterial(Color color, double ior) {
        this.color = color;
        this.ior = ior;
    }
    public GlassMaterial(Color color, double ior, double reflectivity) {
        this.color = color;
        this.ior = ior;
        this.reflectivity = reflectivity;
    }

    @Override
    public Color shade(Vector viewVector, Vector surfacePoint, Vector normal, boolean inside, int recursionDepth) {
        Color reflectionColor = new Color();

        if (Config.REFLECTIONS) {
            Vector reflectionOrigin = new Vector(surfacePoint);
            reflectionOrigin.add(Config.RAY_HIT_BIAS, normal);
    
            reflectionColor = Scene.castRay(
                reflectionOrigin, 
                getReflectedRay(viewVector, normal),
                inside,
                recursionDepth + 1
            );
        }
        Vector refractedRay = null;

        if (inside) {
            refractedRay = getRefractedRay(viewVector, normal, ior, 1);
        } else {
            refractedRay = getRefractedRay(viewVector, normal, 1, ior);
        }
        if (refractedRay != null) {
            Vector refractionOrigin = new Vector(surfacePoint);
            refractionOrigin.add(-Config.RAY_HIT_BIAS, normal);

            Color refractionColor = Scene.castRay(
                refractionOrigin, 
                refractedRay,
                !inside,
                recursionDepth + 1
            );
            if (!inside) {
                refractionColor.filter(color);
            }
            if (Config.REFLECTIONS) {
                double outsideCos = inside ? 
                    -refractedRay.dot(normal) : 
                    -viewVector.dot(normal);
                    
                double reflectionRatio = reflectivity + (1 - reflectivity) * schlick(ior, 1, outsideCos);

                reflectionColor.scale(reflectionRatio);
                refractionColor.scale(1 - reflectionRatio);
                refractionColor.add(reflectionColor);
            }

            return refractionColor;
        } else {
            return reflectionColor;
        }
    }   
}