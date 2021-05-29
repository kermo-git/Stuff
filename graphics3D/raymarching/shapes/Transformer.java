package graphics3D.raymarching.shapes;

import graphics3D.raymarching.Config;
import graphics3D.utils.Vector;
import graphics3D.utils.Matrix;
import graphics3D.noise.Noise;

public class Transformer extends RMobject {
    OriginRMObject obj;
    Matrix fullInv;

    public Transformer(OriginRMObject obj) {
        this.obj = obj;
        fullInv = obj.fullInv;
    }

    @Override
    public RMobject rotate(double degX, double degY, double degZ) {
        obj.rotate(degX, degY, degZ);
        fullInv = obj.fullInv;
        return this;
    }

    @Override
    public RMobject translate(double x, double y, double z) {
        obj.translate(x, y, z);
        fullInv = obj.fullInv;
        return this;
    }

    private double round = 0;

    public Transformer setRound(double round) {
        this.round = round;
        return this;
    }
    private double twist = -1;

    public Transformer setTwist(double twist) {
        this.twist = twist;
        return this;
    }
    boolean elongation = false;
    double elX = 0;
    double elY = 0;
    double elZ = 0;

    public Transformer elongate(double elongationX, double elongationY, double elongationZ) {
        elongation = true;
        elX = 0.5 * elongationX;
        elY = 0.5 * elongationY;
        elZ = 0.5 * elongationZ;

        return this;
    }
    Noise noise = null;
    double noiseZoom;
    double noiseAmplifier;

    public Transformer crumble(Noise noise, double zoom, double amplifier) {
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
        Vector _point = fullInv.getTransformation(point);
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

        double distance = obj.getSignedDistanceAtObjectSpace(_point) - round;

        if (noise != null) {
            distance += noise.noise(
                _point.x / noiseZoom, 
                _point.y / noiseZoom, 
                _point.z / noiseZoom
            ) * noiseAmplifier;

            multiplier = Config.INACCURACY_MULTIPLIER;
        }
        return distance * multiplier;
    }
}
