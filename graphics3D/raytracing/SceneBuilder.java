package graphics3D.raytracing;

import java.awt.image.BufferedImage;

import graphics3D.raytracing.shapes.*;
import graphics3D.utils.Vector;

public class SceneBuilder {
    public static BufferedImage renderObject() {
        Scene.clearScene();
        Scene.camera.lookAt(new Vector(0, 0, 0), new Vector(0, 0, 1));

        Scene.addLights(new Light(0, 0, 0));
        Scene.addObjects(
            new Cylinder(20, 5)
            .setMaterial(Opaque.COPPER())
            .rotate(90, -30, 0)
            .translate(0, 0, 30)
        );
        return Scene.render();
    }

    public static BufferedImage renderScene() {
        Scene.clearScene();
        Scene.camera.lookAt(new Vector(0, 15, 1), new Vector(0, 15, 2));

        Scene.addLights(new Light(-14, 29, 1));
        Scene.addObjects(
            new Cone(15, 7)
            .setMaterial(Opaque.EMERALD())
            .translate(7, 0, 30),

            new Sphere(-5, 15, 23, 5)
            .setMaterial(new Glass()),

            new Cylinder(20, 6)
            .setMaterial(Opaque.COPPER())
            .rotate(-88.2, -36, 0)
            .translate(4, 22, 40),

            new Box(30, 30, 50)
            .setMaterial(Opaque.SILVER())
            .translate(0, 15, 25)
        );
        return Scene.render();
    }
}
