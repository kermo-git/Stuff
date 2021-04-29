package graphics3D.rasterization;

import graphics3D.utils.Color;
import graphics3D.utils.Vector;

public class Material {
    Color ambient = new Color(0x222222);
    Color diffuse = new Color(0xFFFFFF);
    Color specular = new Color(0xFFFFFF);
    double shininess = 0.25 * 128;

    
    public Material() {}
    public Material(Color ambient, Color diffuse, Color specular, double shininess) {
        this.ambient = new Color(ambient);
        this.diffuse = new Color(diffuse);
        this.specular = new Color(specular);
        this.shininess = shininess;
    }
    public Material(Color color) {
        ambient = new Color(color);
        ambient.scale(0.05);
        diffuse = new Color(color);
    }


    public static Vector getReflectedRay(Vector ray, Vector normal) {
        double dot = 2 * ray.dot(normal);
        
        return new Vector(
            ray.x - dot * normal.x,
            ray.y - dot * normal.y,
            ray.z - dot * normal.z
        );
    }


    public Color shade(Vector surfacePoint, Vector normal) {
        Color result = new Color(ambient);
        Vector lightVector, reflectionVector, viewVector;
        double diffuseIntensity, specularIntensity;

        for (Light light : Scene.lights) {
            lightVector = new Vector(light.location, surfacePoint);
            lightVector.normalize();

            if (Config.SHADOWS) {
                if (!light.isIlluminating(surfacePoint))
                    continue;
            }
            diffuseIntensity = -lightVector.dot(normal);
            
            if (diffuseIntensity > 0) {
                result.add(diffuseIntensity, diffuse, light.color);

                if (Config.SPECULAR_HIGHLIGHTS) {
                    reflectionVector = getReflectedRay(lightVector, normal);
                    viewVector = new Vector(Scene.camera.location, surfacePoint);
                    viewVector.normalize();
                    specularIntensity = -reflectionVector.dot(viewVector);

                    if (specularIntensity > 0) {
                        specularIntensity = Math.pow(specularIntensity, shininess);
                        result.add(specularIntensity, specular, light.color);
                    }
                }
            }
        }
        return result;
    }

    // http://devernay.free.fr/cours/opengl/materials.html

    public static Material EMERALD() {
        return new Material(
            new Color(0.0215, 0.1745, 0.0215),
            new Color(0.07568, 0.61424, 0.07568),
            new Color(0.633, 0.727811, 0.633),
            0.6*128
        );
    }
    public static Material JADE() {
        return new Material(
            new Color(0.135, 0.2225, 0.1575),
            new Color(0.54, 0.89, 0.63),
            new Color(0.316228, 0.316228, 0.316228),
            0.1*128
        );
    }
    public static Material OBSIDIAN() {
        return new Material(
            new Color(0.05375, 0.05, 0.06625),
            new Color(0.18275, 0.17, 0.22525),
            new Color(0.332741, 0.328634, 0.346435),
            0.3*128
        );
    }
    public static Material PEARL() {
        return new Material(
            new Color(0.25, 0.20725, 0.20725),
            new Color(1, 0.829, 0.829),
            new Color(0.296648, 0.296648, 0.296648),
            0.088*128
        );
    }
    public static Material RUBY() {
        return new Material(
            new Color(0.1745, 0.01175, 0.01175),
            new Color(0.61424, 0.04136, 0.04136),
            new Color(0.727811, 0.626959, 0.626959),
            0.6*128
        );
    }
    public static Material COPPER() {
        return new Material(
            new Color(0.19125, 0.0735, 0.0225),
            new Color(0.7038, 0.27048, 0.0828),
            new Color(0.256777, 0.137622, 0.086014),
            0.1*128
        );
    }
    public static Material GOLD() {
        return new Material(
            new Color(0.24725, 0.1995, 0.0745),
            new Color(0.75164, 0.60648, 0.22648),
            new Color(0.628281, 0.137622, 0.366065),
            0.4*128
        );
    }
    public static Material SILVER() {
        return new Material(
            new Color(0.19225, 0.19225, 0.19225),
            new Color(0.50754, 0.50754, 0.50754),
            new Color(0.508273, 0.508273, 0.508273),
            0.4*128
        );
    }
    public static Material BRONZE() {
        return new Material(
            new Color(0.2125, 0.1275, 0.054),
            new Color(0.714, 0.4284, 0.18144),
            new Color(0.393548, 0.271906, 0.166721),
            0.2*128
        );
    }
    public static Material CYAN_PLASTIC() {
        return new Material(
            new Color(0.0, 0.1, 0.06),
            new Color(0.0, 0.50980392, 0.50980392),
            new Color(0.50196078, 0.50196078, 0.50196078),
            0.25*128
        );
    }
    public static Material GREEN_PLASTIC() {
        return new Material(
            new Color(0.0, 0.0, 0.0),
            new Color(0.1, 0.35, 0.1),
            new Color(0.45, 0.55, 0.45),
            0.25*128
        );
    }
    public static Material RED_PLASTIC() {
        return new Material(
            new Color(0.0, 0.0, 0.0),
            new Color(0.5, 0.0, 0.0),
            new Color(0.7, 0.6, 0.6),
            0.25*128
        );
    }
    public static Material YELLOW_PLASTIC() {
        return new Material(
            new Color(0.0, 0.0, 0.0),
            new Color(0.5, 0.5, 0.0),
            new Color(0.6, 0.6, 0.5),
            0.25*128
        );
    }
    public static Material CYAN_RUBBER() {
        return new Material(
            new Color(0.0, 0.05, 0.05),
            new Color(0.4, 0.5, 0.5),
            new Color(0.04, 0.7, 0.7),
            0.078125*128
        );
    }
    public static Material GREEN_RUBBER() {
        return new Material(
            new Color(0.0, 0.05, 0.0),
            new Color(0.4, 0.5, 0.4),
            new Color(0.04, 0.7, 0.04),
            0.078125*128
        );
    }
    public static Material RED_RUBBER() {
        return new Material(
            new Color(0.05, 0.0, 0.0),
            new Color(0.5, 0.4, 0.4),
            new Color(0.7, 0.04, 0.04),
            0.078125*128
        );
    }
    public static Material YELLOW_RUBBER() {
        return new Material(
            new Color(0.05, 0.05, 0.0),
            new Color(0.5, 0.5, 0.4),
            new Color(0.7, 0.7, 0.04),
            0.078125*128
        );
    }
}
