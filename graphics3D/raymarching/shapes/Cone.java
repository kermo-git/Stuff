package graphics3D.raymarching.shapes;

import graphics3D.utils.Vector;

public class Cone extends OriginRMObject {
    double height, radius;
    double k, krec, k_krec;

    public Cone(double height, double radius) {
        this.height = height;
        this.radius = radius;

        k = - height / radius;
        krec = 1 / k;
        k_krec = k + krec;
    }

    @Override
    double getSignedDistanceAtObjectSpace(Vector point) {
        double xp = new Vector(point.x, 0, point.z).length();
        double yp = point.y;
        Vector p = new Vector(xp, yp, 0);
        
        double xc = (yp + krec * xp - height) / k_krec;
        double yc = k * xc + height;

        if (yc > height) {
            Vector t = new Vector(0, height, 0);
            return new Vector(t, p).length();
        }
        else if (yc < 0) {
            if (xp < radius) {
                return -yp;
            }
            Vector r = new Vector(radius, 0, 0);
            return new Vector(r, p).length();
        }
        else {
            if (yp < 0) {
                return -yp;
            }
            Vector c = new Vector(xc, yc, 0);
            double distance = new Vector(c, p).length();

            double coneHeight = k * xp + height;
            if (yp < coneHeight) {
                return -min(yp, distance);
            }
            return distance;
        }
    }
}
