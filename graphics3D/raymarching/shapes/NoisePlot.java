package graphics3D.raymarching.shapes;

import graphics3D.noise.Noise;
import graphics3D.raymarching.Config;
import graphics3D.utils.Vector;

public class NoisePlot extends OriginRMObject {
    Noise noise;
    double zoom = 1;

    public NoisePlot(Noise noise, double zoom) {
        this.noise = noise;
        this.zoom = zoom;
    }

    @Override
    protected double getSignedDistanceAtObjectSpace(Vector point) {
        double noiseValue = noise.noise(point.x / zoom, point.z / zoom, 0) * zoom;
        return (point.y - noiseValue) * Config.INACCURACY_MULTIPLIER;
    }

    @Override
    public double getSignedDistance(Vector point) {
        return super.getSignedDistance(point);
    }
}
