package graphics3D.rasterization;

import graphics3D.utils.Color;
import graphics3D.utils.Matrix;
import graphics3D.utils.Vector;
import graphics3D.noise.*;

public class SceneBuilder {
    public static void buildSingleObjectScene() {
        Scene.clearScene();
        Scene.camera.lookAt(new Vector(0, 0, 0), new Vector(0, 0, 1));

        Light light = new Light(
            new Vector(-5, 0, 0),
            new Vector(0, 0, 7),
            new Color(0xFFFFFF)
        );
        TriangleMesh sphere = new TriangleMesh(Material.JADE());
        sphere.buildTorus(1.5, 3, 20);
        sphere.transform(Matrix.rotateAroundX(-0.1 * Math.PI), Matrix.translate(0, 0, 7));
        Scene.addLights(light);
        Scene.addTriangleMeshes(sphere);
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

        TriangleMesh noisePlot = new SmoothMesh(Material.SILVER());
        noisePlot.buildFunctionPlot(50, 50, 1, 20, new PerlinNoise());
        noisePlot.transform(
            Matrix.rotateAroundX(0.8 * Math.PI),
            Matrix.translate(0, 0, 12)
        );

        TriangleMesh torus = new SmoothMesh(Material.SILVER());
        torus.buildTorus(1, 2, 20);
        torus.transform(
            Matrix.rotateAroundX(-0.12 * Math.PI)
            .combine(Matrix.translate(0, 0, 7)
        ));

        TriangleMesh sphere = new SmoothMesh(Material.SILVER());
        sphere.buildSphere(1.5, 20);
        sphere.transform(
            Matrix.rotateAroundX(-0.12 * Math.PI),
            Matrix.translate(4, 2, 12)
        );

        TriangleMesh prism = new TriangleMesh(Material.SILVER());
        prism.buildAntiPrism(7, 4, 1);
        prism.transform(
            Matrix.rotateAroundZ(0.5 * Math.PI).combine(Matrix.rotateAroundY(0.1 * Math.PI)),
            Matrix.translate(-5, 2, 12)
        );

        Scene.addTriangleMeshes(noisePlot, torus, sphere, prism);
    }
}
