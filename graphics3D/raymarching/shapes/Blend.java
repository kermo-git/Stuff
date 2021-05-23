package graphics3D.raymarching.shapes;

import java.util.ArrayList;
import java.util.List;

import graphics3D.utils.Vector;

public class Blend extends RayMarchingObject {
    double k;
    List<RayMarchingObject> objects = new ArrayList<>();

    public Blend(double smoothness, RayMarchingObject ...objects) {
        k = smoothness;
        for (RayMarchingObject object : objects) {
            this.objects.add(object);
        }
    }

    @Override
    public RayMarchingObject rotate(double degX, double degY, double degZ) {
        for (RayMarchingObject object : objects) {
            object.rotate(degX, degY, degZ);
        }
        return this;
    }

    @Override
    public RayMarchingObject translate(double x, double y, double z) {
        for (RayMarchingObject object : objects) {
            object.translate(x, y, z);
        }
        return this;
    }

    // https://iquilezles.org/www/articles/smin/smin.htm
    private double smoothMin(double a, double b) {
        double h = max(k - abs(a - b), 0) / k;
        return min(a, b) - h * h * h * k / 6.0;
    }

    @Override
    public double getSignedDistance(Vector point) {
        double result = Double.MAX_VALUE;
        for (RayMarchingObject object : objects) {
            result = smoothMin(result, object.getSignedDistance(point));
        }
        return result;
    }
    
}
