package graphics3D.raytracing;

import graphics3D.raytracing.shapes.*;
import graphics3D.utils.Color;
import graphics3D.utils.Matrix;
import graphics3D.utils.Vector;

public class SceneBuilder {
    public static void buildScene() {
        Scene.clearScene();
        Scene.camera.lookAt(new Vector(0, 15, 1), new Vector(0, 15, 2));

        Light light = new Light(
            new Vector(-14, 29, 1),
            new Color(0xFFFFFF)
        );

        RayTracingObject floor = new Plane(PhongMaterial.CYAN_PLASTIC());
        RayTracingObject ceiling = new Plane(PhongMaterial.CYAN_PLASTIC(), new Vector(0, 30, 0), new Vector(0, 1, 0));

        RayTracingObject leftWall = new Plane(PhongMaterial.GOLD(), new Vector(-15, 0, 0), new Vector(1, 0, 0));
        RayTracingObject rightWall = new Plane(PhongMaterial.GOLD(), new Vector(15, 0, 0), new Vector(1, 0, 0));

        RayTracingObject frontWall = new Plane(PhongMaterial.GOLD(), new Vector(0, 0, 50), new Vector(0, 0, 1));
        RayTracingObject backWall = new Plane(PhongMaterial.GOLD(), new Vector(0, 0, 0), new Vector(0, 0, 1));

        RayTracingObject cylinder = new Cylinder(new GlassMaterial(new Color(0x55BB55)), 9, 5);
        cylinder.transform(Matrix.rotateAroundX(-0.15 * Math.PI), Matrix.translate(-5, 13, 20));
        
        Scene.addLights(light);
        Scene.addShapes(cylinder, floor, ceiling, leftWall, rightWall, frontWall, backWall);
    }
}
