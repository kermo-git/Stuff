package graphics3D;

import graphics3D.noise.PerlinNoise;

public class SceneBuilder {
    public static void buildScene() {
        Scene3D.camera.lookAt(new Vector(), new Vector(0, 0, 1));

        Scene3D.addLights(
            new LightSource(
                new Vector(-4, -1, 0),
                new Vector(0, 0, 7), 
                new Color(0xFF99FF), 
                new Color(0xFFFFFF)
            ),
            new LightSource(
                new Vector(4, 4, 0),
                new Vector(0, 0, 7), 
                new Color(0x66FF66), 
                new Color(0xFFFFFF)
            )
        );

        Mesh noisePlot = new Mesh(Material.SILVER(), Shading.PHONG);
        noisePlot.buildFunctionPlot(50, 50, 1, 20, new PerlinNoise());
        noisePlot.transform(
            Matrix.rotateAroundX(0.8 * Math.PI)
            .combine(Matrix.translate(0, 0, 12)
        ));

        Mesh torus = new Mesh(Material.SILVER(), Shading.PHONG);
        torus.buildTorus(1, 2, 20);
        torus.transform(
            Matrix.rotateAroundX(-0.12 * Math.PI)
            .combine(Matrix.translate(0, 0, 7)
        ));

        Mesh sphere = new Mesh(Material.SILVER(), Shading.PHONG);
        sphere.buildSphere(1.5, 20);
        sphere.transform(
            Matrix.rotateAroundX(-0.12 * Math.PI)
            .combine(Matrix.translate(4, 2, 12)
        ));

        Mesh prism = new Mesh(Material.SILVER(), Shading.FLAT);
        prism.buildAntiPrism(7, 4, 1);
        prism.transform(
            Matrix.rotateAroundZ(0.5 * Math.PI)
            .combine(Matrix.rotateAroundY(0.1 * Math.PI))
            .combine(Matrix.translate(-5, 2, 12)
        ));

        Scene3D.addObjects(noisePlot, torus, sphere, prism);
    }
}
