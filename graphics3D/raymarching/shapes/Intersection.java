package graphics3D.raymarching.shapes;

import java.util.ArrayList;
import java.util.List;

import graphics3D.utils.Vector;

public class Intersection extends RMobject {
    List<RMobject> objects = new ArrayList<>();

    public Intersection(RMobject ...objects) {
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

    @Override
    public double getSignedDistance(Vector point) {
        double result = Double.MIN_VALUE;

        for (RMobject object : objects) {
            result = max(result, object.getSignedDistance(point));
        }
        return result;
    }
}