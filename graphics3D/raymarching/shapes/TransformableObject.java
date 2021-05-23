package graphics3D.raymarching.shapes;

import graphics3D.noise.Noise;
import graphics3D.raymarching.Config;
import graphics3D.utils.Matrix;
import graphics3D.utils.Vector;

public abstract class TransformableObject extends RayMarchingObject {
    private Matrix transformation = new Matrix();
    private Matrix transformationInv = new Matrix();

    @Override
    public RayMarchingObject rotate(double degX, double degY, double degZ) {
        transformation = transformation.combine(
            Matrix.rotateDeg(degX, degY, degZ)
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
    private double round = 0;

    public TransformableObject setRound(double round) {
        this.round = round;
        return this;
    }
    private double twist = -1;

    public TransformableObject setTwist(double twist) {
        this.twist = twist;
        return this;
    }
    boolean elongation = false;
    double elX = 0;
    double elY = 0;
    double elZ = 0;

    public TransformableObject elongate(double elongationX, double elongationY, double elongationZ) {
        elongation = true;
        elX = 0.5 * elongationX;
        elY = 0.5 * elongationY;
        elZ = 0.5 * elongationZ;

        return this;
    }
    Noise noise = null;
    double noiseZoom;
    double noiseAmplifier;

    public TransformableObject crumble(Noise noise, double zoom, double amplifier) {
        this.noise = noise;
        noiseZoom = zoom;
        noiseAmplifier = zoom * amplifier;
        return this;
    }

    void elongate(Vector point) {
        if (point.x <= 0) {
            point.x = min(0, point.x + elX);
        } else {
            point.x = max(0, point.x - elX);
        }
        if (point.y <= 0) {
            point.y = min(0, point.y + elY);
        } else {
            point.y = max(0, point.y - elY);
        }
        if (point.z <= 0) {
            point.z = min(0, point.z + elZ);
        } else {
            point.z = max(0, point.z - elZ);
        }
    }

    @Override
    public double getSignedDistance(Vector point) {
        Vector _point = transformationInv.getTransformation(point);
        double multiplier = 1;
        
        if (twist > 0) {
            Matrix.rotateAroundY(
                twist * _point.y
            ).transform(_point);

            multiplier = Config.INACCURACY_MULTIPLIER;
        }
        if (elongation) {
            elongate(_point);
        }
        double distance = getSignedDistanceAtObjectSpace(_point) - round;

        if (noise != null) {
            distance += noise.noise(
                point.x / noiseZoom, 
                point.y / noiseZoom, 
                point.z / noiseZoom
            ) * noiseAmplifier;

            multiplier = Config.INACCURACY_MULTIPLIER;
        }
        return distance * multiplier;
    }

    protected abstract double getSignedDistanceAtObjectSpace(Vector point);
}
