package graphics3D.shapes;

import graphics3D.Color;
import graphics3D.materials.Material;
import graphics3D.Vector;
import graphics3D.RayIntersection;
import graphics3D.Scene3D;

public class SmoothTriangle extends Triangle {
    public SmoothTriangle(Material material, Vertex v1, Vertex v2, Vertex v3) {
        super(material, v1, v2, v3);
    }

    @Override
    protected Color doColorCalculation(double w1, double w2, double w3, double zRec) {
        double z = 1.0 / zRec;
        double m1 = w1 * v1.projection.zRec;
        double m2 = w2 * v2.projection.zRec;
        double m3 = w3 * v3.projection.zRec;

        Vector surfacePoint = new Vector();
        surfacePoint.add(m1, v1);
        surfacePoint.add(m2, v2);
        surfacePoint.add(m3, v3);
        surfacePoint.scale(z);

        Vector smoothNormal = new Vector();
        smoothNormal.add(m1, v1.normal);
        smoothNormal.add(m2, v2.normal);
        smoothNormal.add(m3, v3.normal);
        smoothNormal.scale(z);
        smoothNormal.normalize();

        Vector viewVector = new Vector(Scene3D.camera.location, surfacePoint);
        viewVector.normalize();

        return material.shade(
            viewVector, 
            surfacePoint, 
            smoothNormal, 
            false, 0
        );
    }

    @Override
    public RayIntersection getIntersection(Vector rayOrigin, Vector rayDirection) {
        double normalDotDirection = outNormal.dot(rayDirection);

        if (normalDotDirection == 0)
            return null;
        
        double distance = (D - outNormal.dot(rayOrigin)) / normalDotDirection;
        if (distance < 0)
            return null;

        Vector p = new Vector(
            rayOrigin.x + distance * rayDirection.x,
            rayOrigin.y + distance * rayDirection.y,
            rayOrigin.z + distance * rayDirection.z
        );

        Vector edge = new Vector(v1, v2);
        Vector v_p = new Vector(v1, p);
        Vector cross1 = edge.cross(v_p);

        if (cross1.dot(outNormal) >= 0) 
            return null;

        edge = new Vector(v2, v3);
        v_p = new Vector(v2, p);
        Vector cross2 = edge.cross(v_p);

        if (cross2.dot(outNormal) >= 0) 
            return null;

        edge = new Vector(v3, v1);
        v_p = new Vector(v3, p);
        Vector cross3 = edge.cross(v_p);

        if (cross3.dot(outNormal) >= 0) 
            return null;

        double w1 = cross2.length() * sRec;
        double w2 = cross3.length() * sRec;
        double w3 = cross1.length() * sRec;

        if (normalDotDirection > 0) {
            Vector smoothNormal = new Vector(
                -(w1 * v1.normal.x + w2 * v2.normal.x + w3 * v3.normal.x),
                -(w1 * v1.normal.y + w2 * v2.normal.y + w3 * v3.normal.y),
                -(w1 * v1.normal.z + w2 * v2.normal.z + w3 * v3.normal.z)
            );
            return new RayIntersection(distance, p, smoothNormal, material);
        } else {
            Vector smoothNormal = new Vector(
                w1 * v1.normal.x + w2 * v2.normal.x + w3 * v3.normal.x,
                w1 * v1.normal.y + w2 * v2.normal.y + w3 * v3.normal.y,
                w1 * v1.normal.z + w2 * v2.normal.z + w3 * v3.normal.z
            );
            return new RayIntersection(distance, p, smoothNormal, material);
        }
    }
}