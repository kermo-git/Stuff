package graphics3D.noise;

public class ConstantNoise extends Noise {
    @Override
    public double signedNoise(double x, double y, double z) {
        return 0;
    }
    
}
