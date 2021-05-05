package graphics3D.rasterization;

import graphics3D.utils.Color;
import graphics3D.utils.Vector;
import graphics3D.noise.*;

public class SceneBuilder {
    public static void buildSingleObjectScene() {
        Scene.clearScene();
        Scene.camera.lookAt(new Vector(0, 0, 0), new Vector(0, 0, 1));

        Scene.addLights(
            new Light(
                new Vector(-5, 0, 0),
                new Vector(0, 0, 7),
                new Color(0xFFFFFF)
            )
        );
        Scene.addObjects(
            new TriangleMesh(Material.JADE())
                .buildTorus(1.5, 3, 20)
                .rotate(-0.1 * Math.PI, 0, 0)
                .translate(0, 0, 7)
        );
    }


    public static void buildScene() {
        Scene.clearScene();
        Scene.camera.lookAt(new Vector(), new Vector(0, 0, 1));

        Scene.addLights(
            new Light(
                new Vector(-4, -1, 0),
                new Vector(0, 0, 7), 
                new Color(0xFF99FF)
            ),
            new Light(
                new Vector(4, 4, 0),
                new Vector(0, 0, 7), 
                new Color(0x66FF66)
            )
        );
        Scene.addObjects(
            new SmoothMesh(Material.SILVER())
                .buildFunctionPlot(50, 50, 1, 20, new PerlinNoise())
                .rotate(0.8 * Math.PI, 0, 0)
                .translate(0, 0, 12), 

            new SmoothMesh(Material.SILVER())
                .buildTorus(1, 2, 20)
                .rotate(-0.12 * Math.PI, 0, 0)
                .translate(0, 0, 7), 

            new SmoothMesh(Material.SILVER())
                .buildSphere(1.5, 20)
                .rotate(-0.12 * Math.PI, 0, 0)
                .translate(4, 2, 12), 

            new TriangleMesh(Material.SILVER())
                .buildAntiPrism(7, 4, 1)
                .rotate(0, 0, 0.5 * Math.PI)
                .rotate(0, 0.1 * Math.PI, 0)
                .translate(-5, 2, 12)
        );
    }
}
