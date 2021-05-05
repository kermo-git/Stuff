package graphics3D.raymarching;

import graphics3D.raymarching.shapes.RayMarchingObject;
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
        ambient.scale(0.1);
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

    
    public double getShadowMultiplier(Vector surfacePoint, Vector lightVector, double distanceToLight, double shadowSharpness) {
        Vector currentPosition = new Vector(surfacePoint);

        double closestPassDist = Double.MAX_VALUE;
        double travelDist = Config.RAY_HIT_THRESHOLD;
        double result = 1;
        
        while (travelDist < distanceToLight) {
            currentPosition = Scene.getPointOnRay(surfacePoint, lightVector, travelDist);
            double currentMinDistance = Double.MAX_VALUE;

            for (RayMarchingObject obj : Scene.objects) {
                double distance = obj.getSignedDistance(currentPosition);
    
                if (Math.abs(distance) <= Config.RAY_HIT_THRESHOLD) {
                    return 0;
                }
                if (distance < currentMinDistance)
                    currentMinDistance = distance;
            }
            if (currentMinDistance < closestPassDist) {
                closestPassDist = currentMinDistance;
                double newResult = shadowSharpness * closestPassDist / travelDist;
                result = newResult < result ? newResult : result;
            }
            travelDist += currentMinDistance;
        }
        return result;
    }


    public Color shade(Vector surfacePoint, Vector normal) {
        Vector viewVector = new Vector(surfacePoint, Scene.camera.location);
        viewVector.normalize();
        
        Color result = new Color(ambient);
        Vector lightVector, reflectionVector;
        double diffuseIntensity, specularIntensity, distanceToLight, shadowMultiplier;

        for (Light light : Scene.lights) {
            lightVector = new Vector(surfacePoint, light.location);
            distanceToLight = lightVector.length();
            lightVector.normalize();

            shadowMultiplier = 1;

            if (Config.SHADOWS) {
                shadowMultiplier = 
                    getShadowMultiplier(
                        surfacePoint, 
                        lightVector, 
                        distanceToLight, 
                        light.shadowSharpness
                    );
            }
            diffuseIntensity = shadowMultiplier * lightVector.dot(normal);
            
            if (diffuseIntensity > 0) {
                result.add(diffuseIntensity, diffuse, light.color);

                reflectionVector = getReflectedRay(lightVector.getScaled(-1), normal);
                specularIntensity = shadowMultiplier * reflectionVector.dot(viewVector);

                if (specularIntensity > 0) {
                    specularIntensity = Math.pow(specularIntensity, shininess);
                    result.add(specularIntensity, specular, light.color);
                }
            }
        }
        return result;
    }
}