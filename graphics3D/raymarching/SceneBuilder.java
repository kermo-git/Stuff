package graphics3D.raymarching;

import graphics3D.utils.Color;
import graphics3D.utils.Vector;
import graphics3D.raymarching.shapes.*;

public class SceneBuilder {
    public static void buildScene() {
        Scene.clearScene();
        Scene.camera.lookAt(new Vector(0, 0, 0), new Vector(0, 0, 1));

        Scene.addLights(
            new Light(new Vector(20, 0, 0))
        );
        Scene.addObjects(
            new Torus(new Material(new Color(0x3456DA)), 20, 5)
            .rotate(-0.1 * Math.PI, 0, 0.1 * Math.PI)
            .translate(0, 0, 40),

            new InfiniteCylinder(new Material(new Color(0xEF3CAB)), 5)
            .rotate(-0.2 * Math.PI, 0, 0.1 * Math.PI)
            .translate(5, 7, 25),

            new Sphere(new Material(new Color(0x3AFCAA)), new Vector(-8, 7, 25), 6)
        );
    }
}
