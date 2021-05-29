package graphics3D.raytracing.shapes;

import graphics3D.raytracing.RayHit;
import graphics3D.utils.Vector;

public class Box extends OriginRTObject {
    private double minX, minY, minZ;
    private double maxX, maxY, maxZ;

    public Box(double sizeX, double sizeY, double sizeZ) {
        minX = -sizeX * 0.5;
        minY = -sizeY * 0.5;
        minZ = -sizeZ * 0.5;

        maxX = sizeX * 0.5;
        maxY = sizeY * 0.5;
        maxZ = sizeZ * 0.5;
    }

    @Override
    public RayHit getIntersectionAtObjectSpace(Vector o, Vector d) {
        double tmp, t;
        Vector normal = null;

        double t_minX = (minX - o.x) / d.x;
        double t_maxX = (maxX - o.x) / d.x;

        if (t_minX > t_maxX) {
            tmp = t_minX;
            t_minX = t_maxX;
            t_maxX = tmp;
        }
        double t_minY = (minY - o.y) / d.y;
        double t_maxY = (maxY - o.y) / d.y;

        if (t_minY > t_maxY) {
            tmp = t_minY;
            t_minY = t_maxY;
            t_maxY = tmp;
        }
        if (t_maxX < t_minY || t_maxY < t_minX) {
            return null;
        }
        double t_minZ = (minZ - o.z) / d.z;
        double t_maxZ = (maxZ - o.z) / d.z;

        if (t_minZ > t_maxZ) {
            tmp = t_minZ;
            t_minZ = t_maxZ;
            t_maxZ = tmp;
        }
        if (
            t_maxX < t_minZ || 
            t_maxZ < t_minX ||
            t_maxY < t_minZ ||
            t_maxZ < t_minY
        ) 
        { return null; }

        // If looking from outside

        if (t_minX > t_minY) {
            t = t_minX;
            normal = new Vector(1, 0, 0);
        } else {
            t = t_minY;
            normal = new Vector(0, 1, 0);
        }
        if (t_minZ > t) {
            t = t_minZ;
            normal = new Vector(0, 0, 1);
        }
        if (t >= 0) {
            return new RayHit(t, getPointOnRay(o, d, t), normal, material);
        }

        // If looking from inside

        if (t_maxX < t_maxY) {
            t = t_maxX;
            normal = new Vector(1, 0, 0);
        } else {
            t = t_maxY;
            normal = new Vector(0, 1, 0);
        }
        if (t_maxZ < t) {
            t = t_maxZ;
            normal = new Vector(0, 0, 1);
        }
        if (t >= 0) {
            return new RayHit(t, getPointOnRay(o, d, t), normal, material);
        }
        return null;
    }
}
