package graphics3D.materials;

import graphics3D.*;
import graphics3D.shapes.Shape;

public class PhongMaterial extends Material {
    Color ambient, diffuse, specular;
    double shininess;

    double reflectivity = 0, ior;

    public PhongMaterial(Color ambient, Color diffuse, Color specular, double shininess) {
        this.ambient = ambient;
        this.diffuse = diffuse;
        this.specular = specular;
        this.shininess = shininess;
    }

    public PhongMaterial(Color color, double reflectivity, double ior, boolean metallic) {
        ambient = new Color(color);
        ambient.scale(0.05);
        diffuse = color;
        this.specular = metallic ? color : new Color(0xFFFFFF);
        shininess = 0.25 * 128;
        this.reflectivity = reflectivity;
        this.ior = ior;
    }

    @Override
    public Color shade(Vector viewVector, Vector surfacePoint, Vector normal, boolean inside, int recursionDepth) {
        Color phongColor = new Color(ambient);

        Vector lightVector, reflectionVector;

        double diffuseIntensity, specularIntensity;

        lightLoop:
        for (Light light : Scene3D.lights) {
            lightVector = new Vector(light.location, surfacePoint);
            double distanceToLight = lightVector.length() - Config.rayHitPointBias;
            lightVector.normalize();

            switch (Config.shadowType) {
                case SHADOW_MAPPING:
                    if (!light.isIlluminating(surfacePoint))
                        continue lightLoop;
                    break;
                case SHADOW_RAYS:
                    RayIntersection intersection = null;

                    for (Shape object : Scene3D.shapes) {
                        intersection = object.getIntersection(light.location, lightVector);
                        if (intersection != null && intersection.distance < distanceToLight) {
                            continue lightLoop;
                        }
                    }
                    break;
                case NO_SHADOWS:
            }
            diffuseIntensity = -lightVector.dot(normal);
            
            if (diffuseIntensity > 0) {
                phongColor.add(diffuseIntensity, diffuse, light.color);
                
                reflectionVector = lightVector.getLightReflection(normal);
                specularIntensity = -reflectionVector.dot(viewVector);

                if (specularIntensity > 0) {
                    specularIntensity = Math.pow(specularIntensity, shininess);
                    phongColor.add(specularIntensity, specular, light.color);
                }
            }
        }
        if (reflectivity > 0) {
            Vector reflectionOrigin = new Vector(surfacePoint);
            reflectionOrigin.add(Config.rayHitPointBias, normal);
    
            Color reflectionColor = Scene3D.castRay(
                reflectionOrigin, 
                getReflectedRay(viewVector, normal),
                inside,
                recursionDepth + 1
            );
            reflectionColor.filter(specular);

            double cos = -viewVector.dot(normal);
            double reflectionRatio = reflectivity + (1 - reflectivity) * schlick(1, ior, cos);
            
            reflectionColor.scale(reflectionRatio);
            phongColor.scale(1 - reflectionRatio);
            phongColor.add(reflectionColor);
        }
        return phongColor;
    }

    // http://devernay.free.fr/cours/opengl/materials.html

    public static PhongMaterial EMERALD() {
        return new PhongMaterial(
            new Color(0.0215, 0.1745, 0.0215),
            new Color(0.07568, 0.61424, 0.07568),
            new Color(0.633, 0.727811, 0.633),
            0.6*128
        );
    }
    public static PhongMaterial JADE() {
        return new PhongMaterial(
            new Color(0.135, 0.2225, 0.1575),
            new Color(0.54, 0.89, 0.63),
            new Color(0.316228, 0.316228, 0.316228),
            0.1*128
        );
    }
    public static PhongMaterial OBSIDIAN() {
        return new PhongMaterial(
            new Color(0.05375, 0.05, 0.06625),
            new Color(0.18275, 0.17, 0.22525),
            new Color(0.332741, 0.328634, 0.346435),
            0.3*128
        );
    }
    public static PhongMaterial PEARL() {
        return new PhongMaterial(
            new Color(0.25, 0.20725, 0.20725),
            new Color(1, 0.829, 0.829),
            new Color(0.296648, 0.296648, 0.296648),
            0.088*128
        );
    }
    public static PhongMaterial RUBY() {
        return new PhongMaterial(
            new Color(0.1745, 0.01175, 0.01175),
            new Color(0.61424, 0.04136, 0.04136),
            new Color(0.727811, 0.626959, 0.626959),
            0.6*128
        );
    }
    public static PhongMaterial COPPER() {
        return new PhongMaterial(
            new Color(0.19125, 0.0735, 0.0225),
            new Color(0.7038, 0.27048, 0.0828),
            new Color(0.256777, 0.137622, 0.086014),
            0.1*128
        );
    }
    public static PhongMaterial GOLD() {
        return new PhongMaterial(
            new Color(0.24725, 0.1995, 0.0745),
            new Color(0.75164, 0.60648, 0.22648),
            new Color(0.628281, 0.137622, 0.366065),
            0.4*128
        );
    }
    public static PhongMaterial SILVER() {
        return new PhongMaterial(
            new Color(0.19225, 0.19225, 0.19225),
            new Color(0.50754, 0.50754, 0.50754),
            new Color(0.508273, 0.508273, 0.508273),
            0.4*128
        );
    }
    public static PhongMaterial BRONZE() {
        return new PhongMaterial(
            new Color(0.2125, 0.1275, 0.054),
            new Color(0.714, 0.4284, 0.18144),
            new Color(0.393548, 0.271906, 0.166721),
            0.2*128
        );
    }
    public static PhongMaterial CYAN_PLASTIC() {
        return new PhongMaterial(
            new Color(0.0, 0.1, 0.06),
            new Color(0.0, 0.50980392, 0.50980392),
            new Color(0.50196078, 0.50196078, 0.50196078),
            0.25*128
        );
    }
    public static PhongMaterial GREEN_PLASTIC() {
        return new PhongMaterial(
            new Color(0.0, 0.0, 0.0),
            new Color(0.1, 0.35, 0.1),
            new Color(0.45, 0.55, 0.45),
            0.25*128
        );
    }
    public static PhongMaterial RED_PLASTIC() {
        return new PhongMaterial(
            new Color(0.0, 0.0, 0.0),
            new Color(0.5, 0.0, 0.0),
            new Color(0.7, 0.6, 0.6),
            0.25*128
        );
    }
    public static PhongMaterial YELLOW_PLASTIC() {
        return new PhongMaterial(
            new Color(0.0, 0.0, 0.0),
            new Color(0.5, 0.5, 0.0),
            new Color(0.6, 0.6, 0.5),
            0.25*128
        );
    }
    public static PhongMaterial CYAN_RUBBER() {
        return new PhongMaterial(
            new Color(0.0, 0.05, 0.05),
            new Color(0.4, 0.5, 0.5),
            new Color(0.04, 0.7, 0.7),
            0.078125*128
        );
    }
    public static PhongMaterial GREEN_RUBBER() {
        return new PhongMaterial(
            new Color(0.0, 0.05, 0.0),
            new Color(0.4, 0.5, 0.4),
            new Color(0.04, 0.7, 0.04),
            0.078125*128
        );
    }
    public static PhongMaterial RED_RUBBER() {
        return new PhongMaterial(
            new Color(0.05, 0.0, 0.0),
            new Color(0.5, 0.4, 0.4),
            new Color(0.7, 0.04, 0.04),
            0.078125*128
        );
    }
    public static PhongMaterial YELLOW_RUBBER() {
        return new PhongMaterial(
            new Color(0.05, 0.05, 0.0),
            new Color(0.5, 0.5, 0.4),
            new Color(0.7, 0.7, 0.04),
            0.078125*128
        );
    }
}
