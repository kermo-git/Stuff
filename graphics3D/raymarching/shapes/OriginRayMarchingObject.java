package graphics3D.raymarching.shapes;

import graphics3D.utils.Matrix;
import graphics3D.utils.Vector;

public abstract class OriginRayMarchingObject extends RayMarchingObject {
    private Matrix transformation = new Matrix();
    private Matrix transformationInv = new Matrix();

    @Override
    public RayMarchingObject rotate(double rotX, double rotY, double rotZ) {
        transformation = transformation.combine(
            Matrix.rotateAroundX(rotX).combine(
            Matrix.rotateAroundY(rotY)).combine(
            Matrix.rotateAroundZ(rotZ))
        );
        transformationInv = transformation.inverse();
        return this;
    }

    @Override
    public RayMarchingObject translate(double x, double y, double z) {
        transformation = transformation.combine(Matrix.translate(x, y, z));
        transformationInv = transformation.inverse();
        return this;
    }

    @Override
    public double getSignedDistance(Vector point) {
        return getSignedDistanceAtObjectSpace(
            transformationInv.getTransformation(point)
        );
    }

    protected abstract double getSignedDistanceAtObjectSpace(Vector point);
}
