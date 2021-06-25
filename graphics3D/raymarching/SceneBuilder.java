package graphics3D.raymarching;

import java.awt.image.BufferedImage;

import graphics3D.utils.Vector;
import graphics3D.noise.*;
import graphics3D.raymarching.shapes.*;

public class SceneBuilder {
    public static BufferedImage renderObject() {
        Scene.clearScene();
        Scene.camera.lookAt(new Vector(0, 0, 0), new Vector(0, 0, 1));

        Scene.addLights(new Light(-20, 0, 20).setShadowSharpness(20));

        Scene.addObjects(
            new Transformer(new Box(10, 60, 20))
            .setRound(3)
            .setTwist(0.1)
            .crumble(new SimplexNoise(), 2, 0.2)
            .setMaterial(Opaque.EMERALD())
            .rotate(0, 0, 60)
            .translate(0, 0, 60)
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

    public static BufferedImage renderScene2() {
        Scene.clearScene();
        Scene.camera.lookAt(new Vector(0, 5, 0), new Vector(0, 5, 1));

        Scene.addLights(new Light(-10, 20, 0).setShadowSharpness(20));

        Scene.addObjects(
            new Plane(),

            new CutObject(
                new Transformer(new Box(20, 20, 5))
                .setRound(3)
                .rotate(0, 40, 0)
                .translate(0, 13, 50), 
                
                new Sphere(0, 7, 50, 10)
            )
            .setMaterial(Opaque.RUBY()),
            
            // new Sphere(0, 5, 25, 5)
            // .setMaterial(new Glass())

            new Torus(7, 2)
            .setMaterial(new Glass())
            .rotate(-40, 0, 0)
            .translate(0, 7, 25)
        );
        return Scene.render();
    }
}
