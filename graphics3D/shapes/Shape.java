package graphics3D.shapes;

import graphics3D.materials.Material;
import graphics3D.Matrix;
import graphics3D.Vector;
import graphics3D.RayIntersection;

public abstract class Shape {
    public Material material;

    public abstract RayIntersection getIntersection(Vector rayOrigin, Vector rayDirection);

    public abstract void transform(Matrix rotation, Matrix translation);
    
    public abstract void transform(Matrix translation);

    protected double lowX, highX;
    
    protected void solveQuadraticEquasion(double a, double b, double c) {
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
    protected static double min(double a, double b) {
        return (a < b) ? a : b;
    }
    protected static double max(double a, double b) {
        return (a > b) ? a : b;
    }
}
