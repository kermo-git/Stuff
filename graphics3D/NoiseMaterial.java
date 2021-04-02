package graphics3D;

import graphics3D.noise.Noise;

public class NoiseMaterial extends Material {
    Noise noise;

    public NoiseMaterial(Noise noise, Material material) {
        super(material.ambient, material.diffuse, material.specular, material.shininess);
        this.noise = noise;
    }

    @Override
    public Color illuminate(Scene3D scene, Vector point, Vector normal) {
        Color result = super.illuminate(scene, point, normal);
        result.scale(noise.noise(point.x, point.y, point.z));
        return result;
    }
}
