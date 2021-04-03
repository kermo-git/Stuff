package graphics3D;

import graphics3D.noise.Noise;

public class NoiseMaterial extends Material {
    Noise noise;
    double scale;

    public NoiseMaterial(Noise noise, double scale, Material material) {
        super(material.ambient, material.diffuse, material.specular, material.shininess);
        this.scale = scale;
        this.noise = noise;
    }
    public NoiseMaterial(Noise noise, Material material) {
        this(noise, 1, material);
    }

    @Override
    public Color illuminate(Vector point, Vector normal) {
        Color result = super.illuminate(point, normal);
        result.scale(noise.noise(scale * point.x, scale * point.y, scale * point.z));
        return result;
    }
}
