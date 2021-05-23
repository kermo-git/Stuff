package graphics3D.raymarching;

import graphics3D.utils.Color;
import graphics3D.utils.Vector;
import graphics3D.noise.*;
import graphics3D.raymarching.shapes.*;

public class SceneBuilder {
    public static void buildScene() {
        Scene.clearScene();
        Scene.camera.lookAt(new Vector(0, 5, 0), new Vector(0, 5, 1));
        Scene.addLights(new Light(new Vector(-10, 20, 0), 20));
        Scene.addObjects(
            new Plane()

            ,new CutObject(
                new Box(20, 20, 5)
                .setRound(3)
                .rotate(0, 40, 0)
                .translate(0, 13, 50), 
                
                new Sphere(10)
                .translate(0, 7, 50)
            )
            .setMaterial(Opaque.RUBY())

            ,new Sphere(5)
            .setMaterial(new Glass())
            .translate(0, 5, 25)
        );
    }
}
