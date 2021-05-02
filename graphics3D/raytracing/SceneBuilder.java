package graphics3D.raytracing;

import graphics3D.raytracing.shapes.*;
import graphics3D.utils.Color;
import graphics3D.utils.Vector;

public class SceneBuilder {
    public static void buildSingleObject() {
        Scene.clearScene();
        Scene.camera.lookAt(new Vector(0, 0, 0), new Vector(0, 0, 1));

        Scene.addLights(
            new Light(
                new Vector(0, 0, 0),
                new Color(0xFFFFFF)
            )
        );
        Scene.addShapes(
            new Sphere(
                PhongMaterial.COPPER(), 
                new Vector(0, 0, 20), 5
            )
        );
    }
    public static void buildScene() {
        Scene.clearScene();
        Scene.camera.lookAt(new Vector(0, 15, 1), new Vector(0, 15, 2));

        Scene.addLights(
            new Light(
                new Vector(-14, 29, 1),
                new Color(0xFFFFFF)
            )
        );
        Scene.addShapes(
            new Cone(PhongMaterial.EMERALD(), 15, 7).translate(7, 0, 30),

            new Sphere(new GlassMaterial(), new Vector(-5, 15, 23), 5),

            new Cylinder(PhongMaterial.COPPER(), 20, 6)
            .rotate(-0.49 * Math.PI, -0.2 * Math.PI, 0)
            .translate(4, 22, 40),

            new Box(
                PhongMaterial.SILVER(), 
                PhongMaterial.CYAN_PLASTIC(), 
                PhongMaterial.SILVER(),
                30, 30, 50
            )
            .translate(0, 15, 25)
        );
    }
}
