package graphics3D.raymarching.shapes;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import graphics3D.utils.Vector;

public class CutObject extends RMobject {
    RMobject original;
    List<RMobject> cuts = new ArrayList<>();

    public CutObject(RMobject original, RMobject ...cuts) {
        this.original = original;
        for (RMobject cut : cuts) {
            this.cuts.add(cut);
        }
    }

    @Override
    public RMobject rotate(double degX, double degY, double degZ) {
        original.rotate(degX, degY, degZ);
        for (RMobject cut : cuts) {
            cut.rotate(degX, degY, degZ);
        }
        return this;
    }

    @Override
    public RMobject translate(double x, double y, double z) {
        original.translate(x, y, z);
        for (RMobject cut : cuts) {
            cut.translate(x, y, z);
        }
        return this;
    }

    @Override
    public double getSignedDistance(Vector point) {
        double originalDistance = original.getSignedDistance(point);

        Iterator<RMobject> it = cuts.iterator();
        RMobject cut = it.next();

        double result = max(originalDistance, -cut.getSignedDistance(point));

        while (it.hasNext()) {
            cut = it.next();
            result = max(result, -cut.getSignedDistance(point));
        }
        return result;
    }
}
