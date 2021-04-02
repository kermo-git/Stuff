package graphics3D.noise;

public class SineNoise extends Noise {
    @Override
    public double signedNoise(double x, double y, double z) {
        return Math.sin(x) + Math.sin(y) + Math.sin(z);
    }
}
