package graphics3D.raymarching.shapes;

import java.util.ArrayList;
import java.util.List;

import graphics3D.utils.Vector;

public class Intersection extends RayMarchingObject {
    List<RayMarchingObject> objects = new ArrayList<>();

    public Intersection(RayMarchingObject ...objects) {
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

    @Override
    public double getSignedDistance(Vector point) {
        double result = Double.MIN_VALUE;

        for (RayMarchingObject object : objects) {
            result = max(result, object.getSignedDistance(point));
        }
        return result;
    }
}