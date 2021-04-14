package graphics3D.shapes;

import graphics3D.Color;
import graphics3D.Material;
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

        return material.getRasterizationPhongColor(Scene3D.camera.location, surfacePoint, smoothNormal);
    }

    @Override
    public RayIntersection getIntersection(Vector origin, Vector direction) {
        Vector o_v1 = new Vector(origin, v1);
        Vector v2_v1 = new Vector(v2, v1);
        Vector v3_v1 = new Vector(v3, v1);

        Vector n = v2_v1.cross(v3_v1);
        double M  = direction.dot(n);
        if (M < 0) {
            return null;
        }
        double Mx = o_v1.dot(n);
        double distance = Mx / M;
        if (distance < 0) {
            return null;
        }
        double My = direction.dot(o_v1.cross(v3_v1));
        double w2 = My / M;
        if (w2 < 0 || w2 > 1) {
            return null;
        }
        double Mz = direction.dot(v2_v1.cross(o_v1));
        double w3 = Mz / M;
        if (w3 < 0 || w3 > 1) {
            return null;
        }
        double w1 = 1 - w2 - w3;
        if (w1 < 0) {
            return null;
        }
        Vector smoothNormal = new Vector(
            w1 * v1.normal.x + w2 * v2.normal.x + w3 * v3.normal.x,
            w1 * v1.normal.y + w2 * v2.normal.y + w3 * v3.normal.y,
            w1 * v1.normal.z + w2 * v2.normal.z + w3 * v3.normal.z
        );
        Vector location = new Vector(
            origin.x + distance * direction.x,
            origin.y + distance * direction.y,
            origin.z + distance * direction.z
        );
        return new RayIntersection(distance, location, smoothNormal, material);
    }
}