package graphics3D;

import graphics3D.noise.Noise;

public class MultiColorMaterial extends Material {
    Material[] materials;
    Noise function;
    double unit;

    public MultiColorMaterial(Noise noise, Material ...materials) {
        this.materials = materials;
        this.function = noise;
        unit = 1.0 / materials.length;
    }

    @Override
    public Color illuminate(Scene3D scene, Vector point, Vector normal) {
        double value = function.noise(point.x, point.y, point.z);
        int index = (int) (value / unit);
        return materials[index].illuminate(scene, point, normal);
    }
}
