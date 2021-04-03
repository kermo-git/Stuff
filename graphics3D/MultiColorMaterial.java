package graphics3D;

import graphics3D.noise.Noise;

public class MultiColorMaterial extends Material {
    Material[] materials;
    Noise function;
    double unit, scale;

    public MultiColorMaterial(Noise noise, double scale, Material ...materials) {
        this.materials = materials;
        this.scale = scale;
        this.function = noise;
        unit = 1.0 / materials.length;
    }
    public MultiColorMaterial(Noise noise, Material ...materials) {
        this(noise, 1, materials);
    }

    @Override
    public Color illuminate(Vector point, Vector normal) {
        double value = function.noise(scale * point.x, scale * point.y, scale * point.z);
        int index = (int) (value / unit);
        return materials[index].illuminate(point, normal);
    }
}
