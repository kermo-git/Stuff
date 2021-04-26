package graphics3D;

import graphics3D.materials.*;
import graphics3D.shapes.*;
import graphics3D.noise.*;

public class SceneBuilder {
    public static void buildSingleObjectScene() {
        Scene3D.clearScene();
        Scene3D.camera.lookAt(new Vector(0, 0, 0), new Vector(0, 0, 1));

        Light light = new Light(
            new Vector(-5, 0, 0),
            new Vector(0, 0, 7),
            new Color(0xFFFFFF)
        );
        TriangleMesh sphere = new TriangleMesh(PhongMaterial.JADE());
        sphere.buildTorus(1.5, 3, 20);
        sphere.transform(Matrix.rotateAroundX(-0.1 * Math.PI), Matrix.translate(0, 0, 7));
        Scene3D.addLights(light);
        Scene3D.addTriangleMeshes(sphere);
    }


    public static void buildRayTracingScene() {
        Scene3D.clearScene();
        Scene3D.camera.lookAt(new Vector(0, 5, 0), new Vector(0, 5, 1));

        Light light = new Light(
            new Vector(10, 18, 0),
            new Color(0xFFFFFF)
        );

        Shape floor = new Plane(PhongMaterial.SILVER());
        // Shape ceiling = new Plane(PhongMaterial.EMERALD(), new Vector(0, 30, 0), new Vector(0, 1, 0));
        // Shape leftWall = new Plane(PhongMaterial.RUBY(), new Vector(-15, 0, 0), new Vector(1, 0, 0));
        // Shape rightWall = new Plane(PhongMaterial.JADE(), new Vector(15, 0, 0), new Vector(1, 0, 0));
        // Shape frontWall = new Plane(PhongMaterial.OBSIDIAN(), new Vector(0, 0, 40), new Vector(0, 0, 1));
        // Shape backWall = new Plane(PhongMaterial.PEARL(), new Vector(0, 0, 0), new Vector(0, 0, 1));

        Shape sphere1 = new Sphere(
            new Transparent(new Color(0x55FF55), 1.5),
            new Vector(-7, 7, 20), 5
        );
        Shape sphere2 = new Sphere(
            new PhongMaterial(new Color(0xAA2222), 0.05, 1, false),
            new Vector(7, 7, 20), 5
        );
        Scene3D.addLights(light);
        Scene3D.addShapes(floor, sphere1, sphere2);
    }


    public static void buildInsideTunnelScene() {
        Scene3D.clearScene();
        Scene3D.camera.lookAt(new Vector(-3.5, 0, 0), new Vector(-3.5, 0, 1));

        Light light = new Light(
            new Vector(0, 0, 4.5),
            new Color(0xFFFFFF)
        );

        TriangleMesh room = new TriangleMesh(PhongMaterial.GOLD());
        room.buildTorus(3, 6, 20);

        TriangleMesh object = new TriangleMesh(PhongMaterial.EMERALD());
        object.buildSphere(0.5, 20);
        object.transform(Matrix.translate(-3, 1, 4));

        Scene3D.addLights(light);
        Scene3D.addTriangleMeshes(room, object);
    }


    public static void buildRasterizationScene() {
        Scene3D.clearScene();
        Scene3D.camera.lookAt(new Vector(), new Vector(0, 0, 1));

        Scene3D.addLights(
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

        TriangleMesh noisePlot = new TriangleMesh(PhongMaterial.SILVER(), true);
        noisePlot.buildFunctionPlot(50, 50, 1, 20, new PerlinNoise());
        noisePlot.transform(
            Matrix.rotateAroundX(0.8 * Math.PI),
            Matrix.translate(0, 0, 12)
        );

        TriangleMesh torus = new TriangleMesh(PhongMaterial.SILVER(), true);
        torus.buildTorus(1, 2, 20);
        torus.transform(
            Matrix.rotateAroundX(-0.12 * Math.PI)
            .combine(Matrix.translate(0, 0, 7)
        ));

        TriangleMesh sphere = new TriangleMesh(PhongMaterial.SILVER(), true);
        sphere.buildSphere(1.5, 20);
        sphere.transform(
            Matrix.rotateAroundX(-0.12 * Math.PI),
            Matrix.translate(4, 2, 12)
        );

        TriangleMesh prism = new TriangleMesh(PhongMaterial.SILVER());
        prism.buildAntiPrism(7, 4, 1);
        prism.transform(
            Matrix.rotateAroundZ(0.5 * Math.PI).combine(Matrix.rotateAroundY(0.1 * Math.PI)),
            Matrix.translate(-5, 2, 12)
        );

        Scene3D.addTriangleMeshes(noisePlot, torus, sphere, prism);
    }
}
