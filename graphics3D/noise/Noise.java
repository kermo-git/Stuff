package graphics3D.noise;

public abstract class Noise {
    /**
     * @return {@code double} between -1 and 1
     */
    public abstract double signedNoise(double x, double y, double z);

    public double noise(double x, double y, double z) {
        return 0.5 + 0.5 * signedNoise(x, y, z);
    }
    public double turbulence(double x, double y, double z) {
        double value = signedNoise(x, y, z);
        return value > 0 ? value : -value;
    }
}
