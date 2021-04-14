package graphics3D;

import java.util.Random;

import graphics3D.shapes.*;
import graphics3D.noise.*;

public class SceneBuilder {
    public static void buildSingleObject() {
        Scene3D.clearScene();
        Scene3D.camera.lookAt(new Vector(0, 0, 0), new Vector(0, 0, 1));

        LightSource light = new LightSource(
            new Vector(-5, 0, 0),
            new Vector(0, 0, 7),
            new Color(0xFFFFFF)
        );
        TriangleMesh sphere = new TriangleMesh(Material.JADE());
        sphere.buildTorus(1.5, 3, 20);
        sphere.transform(Matrix.rotateAroundX(-0.1 * Math.PI), Matrix.translate(0, 0, 7));
        Scene3D.addLights(light);
        Scene3D.addTriangleMeshObjects(sphere);
    }

    public static void buildRayTracingScene() {
        Scene3D.clearScene();
        Scene3D.camera.lookAt(new Vector(0, 0, 0), new Vector(0, 0, 1));

        LightSource light1 = new LightSource(
            new Vector(5, 5, 10),
            new Color(0x888888)
        );
        LightSource light2 = new LightSource(
            new Vector(-5, 0, 10),
            new Color(0x888888)
        );
        TriangleMesh floor = new TriangleMesh(Material.RED_RUBBER());
        floor.buildFunctionPlot(4, 4, 4, 10, new ConstantNoise());
        floor.transform(Matrix.rotateAroundX(0.9 * Math.PI), Matrix.translate(0, 0, 10));

        TriangleMesh prism = new TriangleMesh(Material.mirror(new Color(0xFFFFFF)));
        prism.buildPrism(10, 12, 6);
        prism.transform(Matrix.rotateAroundX(-0.1 * Math.PI), Matrix.translate(0, 4, 20));

        TriangleMesh sphere1 = new TriangleMesh(Material.GREEN_RUBBER());
        sphere1.buildSphere(2, 10);
        sphere1.transform(Matrix.rotateAroundX(-0.1 * Math.PI), Matrix.translate(0, -1, 10));

        TriangleMesh sphere2 = new TriangleMesh(Material.phong(new Color(0x5555FF), 0.25 * 128));
        sphere2.buildSphere(2, 10);
        sphere2.transform(Matrix.rotateAroundX(-0.1 * Math.PI), Matrix.translate(6, 5, 13));

        Scene3D.addLights(light1, light2);
        Scene3D.addTriangleMeshObjects(floor, prism, sphere1, sphere2);
    }

    
    public static void generateBalls() {
        Scene3D.clearScene();
        Scene3D.camera.lookAt(new Vector(0, 0, 0), new Vector(0, 0, 1));

        LightSource light1 = new LightSource(
            new Vector(-8, 0, 0), 
            new Vector(0, 0, 10),
            new Color(0x888888)
        );

        LightSource light2 = new LightSource(
            new Vector(8, 0, 0), 
            new Vector(0, 0, 10),
            new Color(0x888888)
        );

        TriangleMesh terrain = new TriangleMesh(Material.CYAN_PLASTIC(), true);
        terrain.buildFunctionPlot(15, 15, 5, 20, new SimplexNoise());
        terrain.transform(
            Matrix.rotateAroundX(0.5 * Math.PI),
            Matrix.translate(0, 0, 40)
        );

        Scene3D.addLights(light1, light2);
        Scene3D.addTriangleMeshObjects(terrain);

        Random random = new Random();
        
        for (int i = 0; i < 40; i++) {
            int randSign1 = random.nextBoolean() ? 1 : -1;
            int randSign2 = random.nextBoolean() ? 1 : -1;

            TriangleMesh sphere = new TriangleMesh(
                Material.phong(
                    new Color(random.nextInt()),
                    0.3 * 128
                ), 
                true
            );
            sphere.buildSphere(1.5, 20);
            sphere.transform(Matrix.translate(
                randSign1 * 10 * random.nextDouble(), 
                randSign2 * 10 * random.nextDouble(), 
                10 + 20 * random.nextDouble()
            ));
            Scene3D.addTriangleMeshObjects(sphere);  
        }
    }


    public static void buildScene() {
        Scene3D.clearScene();
        Scene3D.camera.lookAt(new Vector(), new Vector(0, 0, 1));

        LightSource light1 = new LightSource(
            new Vector(-4, -1, 0),
            new Vector(0, 0, 7), 
            new Color(0xFF99FF)
        );

        LightSource light2 = new LightSource(
            new Vector(4, 4, 0),
            new Vector(0, 0, 7), 
            new Color(0x66FF66)
        );

        TriangleMesh terrain = new TriangleMesh(Material.SILVER(), true);
        terrain.buildFunctionPlot(50, 50, 1, 20, new PerlinNoise());
        terrain.transform(
            Matrix.rotateAroundX(0.8 * Math.PI),
            Matrix.translate(0, 0, 12)
        );

        TriangleMesh torus = new TriangleMesh(Material.SILVER(), true);
        torus.buildTorus(1, 2, 20);
        torus.transform(
            Matrix.rotateAroundX(-0.12 * Math.PI),
            Matrix.translate(0, 0, 7)
        );

        TriangleMesh sphere = new TriangleMesh(Material.SILVER(), true);
        sphere.buildSphere(1.5, 20);
        sphere.transform(Matrix.translate(4, 2, 12));

        TriangleMesh antiPrism = new TriangleMesh(Material.SILVER(), true);
        antiPrism.buildAntiPrism(7, 4, 1);
        antiPrism.transform(
            Matrix.rotateAroundZ(0.5 * Math.PI).combine(Matrix.rotateAroundY(0.1 * Math.PI)),
            Matrix.translate(-5, 2, 12)
        );

        Scene3D.addLights(light1, light2);
        Scene3D.addTriangleMeshObjects(terrain, torus, sphere, antiPrism);
    }
}
