package graphics3D;

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
        TriangleMesh sphere = new TriangleMesh(Material.JADE());
        sphere.buildTorus(1.5, 3, 20);
        sphere.transform(Matrix.rotateAroundX(-0.1 * Math.PI), Matrix.translate(0, 0, 7));
        Scene3D.addLights(light);
        Scene3D.addTriangleMeshes(sphere);
    }

    public static void buildRayTracingScene() {
        Scene3D.clearScene();
        Scene3D.camera.lookAt(new Vector(0, 5, 0), new Vector(0, 0, 30));

        Light light = new Light(
            new Vector(0, 10, 30),
            new Color(0xFFFFFF)
        );

        TriangleMesh floor = new TriangleMesh(Material.SILVER(), true);
        floor.buildFunctionPlot(1, 1, 200, 10, new ConstantNoise());
        floor.transform(Matrix.rotateAroundX(1 * Math.PI), Matrix.translate(0, 0, 0));

        TriangleMesh ball1 = new TriangleMesh(Material.EMERALD());
        ball1.buildSphere(3, 10);
        ball1.transform(
            Matrix.translate(-2, 3, 40)
        );

        TriangleMesh ball2 = new TriangleMesh(Material.RUBY());
        ball2.buildSphere(3, 10);
        ball2.transform(
            Matrix.translate(-8, 3, 40)
        );

        TriangleMesh mirror = new TriangleMesh(Material.mirror(new Color(0x88FFFF)));
        mirror.buildPrism(4, 10, 10);
        mirror.transform(
            Matrix.rotateAroundY(0.4 * Math.PI),
            Matrix.translate(5, 5, 55)
        );

        TriangleMesh glass = new TriangleMesh(Material.transparent(new Color(0xFF8888), 1.5), true);
        glass.buildSphere(5, 20);
        glass.transform(
            Matrix.translate(-5, 5, 20)
        );

        Scene3D.addLights(light);
        Scene3D.addTriangleMeshes(floor, ball1, ball2, mirror, glass);
    }


    public static void buildInsideTunnelScene() {
        Scene3D.clearScene();
        Scene3D.camera.lookAt(new Vector(-3.5, 0, 0), new Vector(-3.5, 0, 1));

        Light light = new Light(
            new Vector(0, 0, 4.5),
            new Color(0xFFFFFF)
        );

        TriangleMesh room = new TriangleMesh(Material.GOLD());
        room.buildTorus(3, 6, 20);

        TriangleMesh object = new TriangleMesh(Material.EMERALD());
        object.buildSphere(0.5, 20);
        object.transform(Matrix.translate(-3, 1, 4));

        Scene3D.addLights(light);
        Scene3D.addTriangleMeshes(room, object);
    }
}
