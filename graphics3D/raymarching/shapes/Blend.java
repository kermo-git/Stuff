package graphics3D.raymarching.shapes;

import java.util.ArrayList;
import java.util.List;

import graphics3D.utils.Vector;

public class Blend extends RMobject {
    double k;
    List<RMobject> objects = new ArrayList<>();

    public Blend(double smoothness, RMobject ...objects) {
        k = smoothness;
        for (RMobject object : objects) {
            this.objects.add(object);
        }
    }

    @Override
    public RMobject rotate(double degX, double degY, double degZ) {
        for (RMobject object : objects) {
            object.rotate(degX, degY, degZ);
        }
        return this;
    }

    @Override
    public RMobject translate(double x, double y, double z) {
        for (RMobject object : objects) {
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
        for (RMobject object : objects) {
            result = smoothMin(result, object.getSignedDistance(point));
        }
        return result;
    }
    
}
