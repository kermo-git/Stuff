package graphics3D.rasterization;

import java.awt.image.BufferedImage;

import graphics3D.utils.Vector;
import graphics3D.noise.*;

public class SceneBuilder {
    
    public static BufferedImage renderObject() {
        Scene.clearScene();
        Scene.camera.lookAt(new Vector(0, 0, 0), new Vector(0, 0, 1));

        Scene.addLights(
            new Light(-5, 0, 0).lookAt(0, 0, 7)
        );
        Scene.addObjects(
            new TriangleMesh(Material.JADE())
                .buildTorus(1.5, 3, 20)
                .rotate(-20, 0, 0)
                .translate(0, 0, 7)
        );
        return Scene.render();
    }


    public static BufferedImage renderScene() {
        Scene.clearScene();
        Scene.camera.lookAt(new Vector(), new Vector(0, 0, 1));

        Scene.addLights(
            new Light(new Vector(-4, -1, 0), 0xFF99FF).lookAt(0, 0, 7),
            new Light(new Vector(4, 4, 0), 0x66FF66).lookAt(0, 0, 7)
        );
        Scene.addObjects(
            new SmoothMesh(Material.SILVER())
                .buildFunctionPlot(50, 50, 1, 20, new PerlinNoise())
                .rotate(144, 0, 0)
                .translate(0, 0, 12), 

            new SmoothMesh(Material.SILVER())
                .buildTorus(1, 2, 20)
                .rotate(-21.6, 0, 0)
                .translate(0, 0, 7), 

            new SmoothMesh(Material.SILVER())
                .buildSphere(1.5, 20)
                .rotate(-21.6, 0, 0)
                .translate(4, 2, 12), 

            new TriangleMesh(Material.SILVER())
                .buildAntiPrism(7, 4, 1)
                .rotate(0, 0, 90)
                .rotate(0, 18, 0)
                .translate(-5, 2, 12)
        );
        return Scene.render();
    }
}
