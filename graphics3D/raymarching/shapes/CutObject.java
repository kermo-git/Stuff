package graphics3D.raymarching.shapes;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import graphics3D.utils.Vector;

public class CutObject extends RayMarchingObject {
    RayMarchingObject original;
    List<RayMarchingObject> cuts = new ArrayList<>();

    public CutObject(RayMarchingObject original, RayMarchingObject ...cuts) {
        this.original = original;
        for (RayMarchingObject cut : cuts) {
            this.cuts.add(cut);
        }
    }

    @Override
    public RayMarchingObject rotate(double degX, double degY, double degZ) {
        original.rotate(degX, degY, degZ);
        for (RayMarchingObject cut : cuts) {
            cut.rotate(degX, degY, degZ);
        }
        return this;
    }

    @Override
    public RayMarchingObject translate(double x, double y, double z) {
        original.translate(x, y, z);
        for (RayMarchingObject cut : cuts) {
            cut.translate(x, y, z);
        }
        return this;
    }

    @Override
    public double getSignedDistance(Vector point) {
        double originalDistance = original.getSignedDistance(point);

        Iterator<RayMarchingObject> it = cuts.iterator();
        RayMarchingObject cut = it.next();

        double result = max(originalDistance, -cut.getSignedDistance(point));

        while (it.hasNext()) {
            cut = it.next();
            result = max(result, -cut.getSignedDistance(point));
        }
        return result;
    }
}
