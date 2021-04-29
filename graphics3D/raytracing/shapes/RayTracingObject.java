package graphics3D.raytracing.shapes;

import graphics3D.utils.Vector;
import graphics3D.raytracing.Material;
import graphics3D.raytracing.RayIntersection;
import graphics3D.utils.Matrix;

public abstract class RayTracingObject {
    public Material material;

    public abstract void transform(Matrix rotation, Matrix translation);
    public abstract void transform(Matrix translation);
    
    public abstract RayIntersection getIntersection(Vector rayOrigin, Vector rayDirection);

    public double lowX, highX;
    
    public void solveQuadraticEquasion(double a, double b, double c) {
        double D = b * b - 4 * a * c;

        if (D < 0) {
            lowX = Double.NaN;
            highX = Double.NaN;
        }
        else if (D == 0) {
            lowX = highX = -b / (2 * a);
        } 
        else {
            D = Math.sqrt(D);
            lowX = (-b - D) / (2 * a);
            highX = (-b + D) / (2 * a);

            if (lowX > highX) {
                double temp = lowX;
                lowX = highX;
                highX = temp;
            }
        }
    }
}
