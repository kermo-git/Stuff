package graphics3D.raymarching.shapes;

import graphics3D.utils.Vector;

public class Repeat extends RayMarchingObject {
    RayMarchingObject obj;
    double unitX, unitY, unitZ;
    double halfUnitX, halfUnitY, halfUnitZ;

    public Repeat(RayMarchingObject obj, double unitX, double unitY, double unitZ) {
        this.obj = obj;

        this.unitX = unitX;
        this.unitY = unitY;
        this.unitZ = unitZ;

        this.halfUnitX = 0.5 * unitX;
        this.halfUnitY = 0.5 * unitY;
        this.halfUnitZ = 0.5 * unitZ;
    }

    @Override
    public RayMarchingObject rotate(double degX, double degY, double degZ) {
        obj.rotate(degX, degY, degZ);
        return this;
    }

    @Override
    public RayMarchingObject translate(double x, double y, double z) {
        obj.translate(x, y, z);
        return this;
    }

    @Override
    public double getSignedDistance(Vector point) {
        return obj.getSignedDistance(new Vector(
            (abs(point.x) + halfUnitX) % unitX - halfUnitX,
            (abs(point.y) + halfUnitY) % unitY - halfUnitY,
            (abs(point.z) + halfUnitZ) % unitZ - halfUnitZ
        ));
    }
}
