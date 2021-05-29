package graphics3D.raymarching;

import graphics3D.raymarching.shapes.RMobject;
import graphics3D.utils.Color;
import graphics3D.utils.Vector;

public class Opaque extends Material {
    Color ambient = new Color(0x222222);
    Color diffuse = new Color(0xFFFFFF);
    Color specular = new Color(0xFFFFFF);
    double shininess = 0.25 * 128;

    public Opaque() {}
    
    public Opaque(int colorHEX, double shininess) {
        this.ambient = new Color(colorHEX);
        ambient.scale(0.1);
        this.diffuse = new Color(colorHEX);
        this.shininess = shininess;
    }
    double reflectivity = 0, ior = 1.3;
    double oppositeReflectivity = 1;

    public Opaque withProperties(double reflectivity, double ior) {
        this.reflectivity = reflectivity;
        oppositeReflectivity = 1 - reflectivity;
        this.ior = ior;
        return this;
    }


    public Color shade(Vector incidentray, Vector surfacePoint, Vector normal, int depth) {
        double cos = -incidentray.dot(normal);
        if (cos < 0) {
            normal = normal.getScaled(-1);
            cos = -cos;
        }
        Vector _surfacePoint = getPointOnRay(surfacePoint, normal, Config.RAY_HIT_BIAS);
        Color result = getDiffuseColor(incidentray, _surfacePoint, normal);

        if (Config.REFLECTIONS) {
            if (depth < Config.MAX_BOUNCES) {
                Vector reflectedRay = getReflectedRay(incidentray, normal);
                Color reflectionColor = Scene.castRay(_surfacePoint, reflectedRay, depth + 1);
                double reflectionRatio = reflectivity + oppositeReflectivity * schlick(1, ior, cos);
                
                result.scale(1 - reflectionRatio);
                result.add(reflectionRatio, reflectionColor, specular);
            }
        }
        return result;
    }

    
    private Color getDiffuseColor(Vector incidentRay, Vector surfacePoint, Vector normal) {
        Color result = new Color(ambient);

        Vector lightVector;
        Vector reflectionVector;

        double diffuseIntensity;
        double specularIntensity;
        double distanceToLight;
        double shadow;

        for (Light light : Scene.lights) {
            lightVector = new Vector(surfacePoint, light.location);
            distanceToLight = lightVector.length();
            lightVector.normalize();
            shadow = 1;

            if (Config.SHADOWS) {
                shadow = getShadow(surfacePoint, lightVector, distanceToLight, light.shadowSharpness);
            }
            diffuseIntensity = shadow * lightVector.dot(normal);
            
            if (diffuseIntensity > 0) {
                result.add(diffuseIntensity, diffuse, light.color);

                reflectionVector = getReflectedRay(lightVector.getScaled(-1), normal);
                specularIntensity = shadow * -reflectionVector.dot(incidentRay);

                if (specularIntensity > 0) {
                    specularIntensity = Math.pow(specularIntensity, shininess);
                    result.add(specularIntensity, specular, light.color);
                }
            }
        }
        return result;
    }


    private double getShadow(Vector surfacePoint, Vector lightVector, double distanceToLight, int shadowSharpness) {
        Vector currentPosition;
        double currentDistance = 0;
        double result = 1;
        
        while (currentDistance < distanceToLight) {
            currentPosition = getPointOnRay(surfacePoint, lightVector, currentDistance);
            double minDistance = Double.MAX_VALUE;

            for (RMobject obj : Scene.objects) {
                double distance = Math.abs(obj.getSignedDistance(currentPosition));
    
                if (distance <= Config.RAY_HIT_THRESHOLD)
                    return 0;
                if (distance < minDistance)
                    minDistance = distance;
            }
            if (Config.SOFT_SHADOWS) {
                result = Math.min(result, shadowSharpness * minDistance / currentDistance);
            }
            currentDistance += minDistance;
        }
        return result;
    }

    private Opaque(Color ambient, Color diffuse, Color specular, double shininess) {
        this.ambient = new Color(ambient);
        this.diffuse = new Color(diffuse);
        this.specular = new Color(specular);
        this.shininess = shininess;
    }

    // http://devernay.free.fr/cours/opengl/materials.html

    public static Opaque EMERALD() {
        return new Opaque(
            new Color(0.0215, 0.1745, 0.0215),
            new Color(0.07568, 0.61424, 0.07568),
            new Color(0.633, 0.727811, 0.633),
            0.6*128
        );
    }
    public static Opaque JADE() {
        return new Opaque(
            new Color(0.135, 0.2225, 0.1575),
            new Color(0.54, 0.89, 0.63),
            new Color(0.316228, 0.316228, 0.316228),
            0.1*128
        );
    }
    public static Opaque OBSIDIAN() {
        return new Opaque(
            new Color(0.05375, 0.05, 0.06625),
            new Color(0.18275, 0.17, 0.22525),
            new Color(0.332741, 0.328634, 0.346435),
            0.3*128
        );
    }
    public static Opaque PEARL() {
        return new Opaque(
            new Color(0.25, 0.20725, 0.20725),
            new Color(1, 0.829, 0.829),
            new Color(0.296648, 0.296648, 0.296648),
            0.088*128
        );
    }
    public static Opaque RUBY() {
        return new Opaque(
            new Color(0.1745, 0.01175, 0.01175),
            new Color(0.61424, 0.04136, 0.04136),
            new Color(0.727811, 0.626959, 0.626959),
            0.6*128
        );
    }
    public static Opaque COPPER() {
        return new Opaque(
            new Color(0.19125, 0.0735, 0.0225),
            new Color(0.7038, 0.27048, 0.0828),
            new Color(0.256777, 0.137622, 0.086014),
            0.1*128
        );
    }
    public static Opaque GOLD() {
        return new Opaque(
            new Color(0.24725, 0.1995, 0.0745),
            new Color(0.75164, 0.60648, 0.22648),
            new Color(0.628281, 0.137622, 0.366065),
            0.4*128
        );
    }
    public static Opaque SILVER() {
        return new Opaque(
            new Color(0.19225, 0.19225, 0.19225),
            new Color(0.50754, 0.50754, 0.50754),
            new Color(0.508273, 0.508273, 0.508273),
            0.4*128
        );
    }
    public static Opaque BRONZE() {
        return new Opaque(
            new Color(0.2125, 0.1275, 0.054),
            new Color(0.714, 0.4284, 0.18144),
            new Color(0.393548, 0.271906, 0.166721),
            0.2*128
        );
    }
    public static Opaque CYAN_PLASTIC() {
        return new Opaque(
            new Color(0.0, 0.1, 0.06),
            new Color(0.0, 0.50980392, 0.50980392),
            new Color(0.50196078, 0.50196078, 0.50196078),
            0.25*128
        );
    }
    public static Opaque GREEN_PLASTIC() {
        return new Opaque(
            new Color(0.0, 0.0, 0.0),
            new Color(0.1, 0.35, 0.1),
            new Color(0.45, 0.55, 0.45),
            0.25*128
        );
    }
    public static Opaque RED_PLASTIC() {
        return new Opaque(
            new Color(0.0, 0.0, 0.0),
            new Color(0.5, 0.0, 0.0),
            new Color(0.7, 0.6, 0.6),
            0.25*128
        );
    }
    public static Opaque YELLOW_PLASTIC() {
        return new Opaque(
            new Color(0.0, 0.0, 0.0),
            new Color(0.5, 0.5, 0.0),
            new Color(0.6, 0.6, 0.5),
            0.25*128
        );
    }
    public static Opaque CYAN_RUBBER() {
        return new Opaque(
            new Color(0.0, 0.05, 0.05),
            new Color(0.4, 0.5, 0.5),
            new Color(0.04, 0.7, 0.7),
            0.078125*128
        );
    }
    public static Opaque GREEN_RUBBER() {
        return new Opaque(
            new Color(0.0, 0.05, 0.0),
            new Color(0.4, 0.5, 0.4),
            new Color(0.04, 0.7, 0.04),
            0.078125*128
        );
    }
    public static Opaque RED_RUBBER() {
        return new Opaque(
            new Color(0.05, 0.0, 0.0),
            new Color(0.5, 0.4, 0.4),
            new Color(0.7, 0.04, 0.04),
            0.078125*128
        );
    }
    public static Opaque YELLOW_RUBBER() {
        return new Opaque(
            new Color(0.05, 0.05, 0.0),
            new Color(0.5, 0.5, 0.4),
            new Color(0.7, 0.7, 0.04),
            0.078125*128
        );
    }
}