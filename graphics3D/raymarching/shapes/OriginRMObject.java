package graphics3D.raymarching.shapes;

import graphics3D.utils.Matrix;
import graphics3D.utils.Vector;

public abstract class OriginRMObject extends RMobject {
    Matrix fullMat = new Matrix();
    Matrix fullInv = new Matrix();

    @Override
    public RMobject rotate(double degX, double degY, double degZ) {
        fullMat = fullMat.combine(Matrix.rotateDeg(degX, degY, degZ));
        fullInv = fullMat.inverse();
        return this;
    }

    @Override
    public RMobject translate(double x, double y, double z) {
        fullMat = fullMat.combine(Matrix.translate(x, y, z));
        fullInv = fullMat.inverse();
        return this;
    }

    @Override
    public double getSignedDistance(Vector point) {
        return getSignedDistanceAtObjectSpace(fullInv.getTransformation(point));
    }

    abstract double getSignedDistanceAtObjectSpace(Vector point);
}
