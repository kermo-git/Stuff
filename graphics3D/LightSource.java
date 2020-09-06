package graphics3D;


public class LightSource extends Vector {
    int diffuseRed, diffuseGreen, diffuseBlue;

    int specularRed, specularGreen, specularBlue;

    public LightSource(double x, double y, double z, int diffuseRGB, int specularRGB) {
        super(x, y, z);

        diffuseRed = (diffuseRGB >> 16) & 0xFF;
        diffuseGreen = (diffuseRGB >> 8) & 0xFF;
        diffuseBlue = (diffuseRGB >> 0) & 0xFF;

        specularRed = (specularRGB >> 16) & 0xFF;
        specularGreen = (specularRGB >> 8) & 0xFF;
        specularBlue = (specularRGB >> 0) & 0xFF;
    }
}