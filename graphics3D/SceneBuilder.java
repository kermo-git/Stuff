package graphics3D;

import java.util.Random;

import graphics3D.noise.*;

public class SceneBuilder {
    public static void buildSingleObject() {
        Scene3D.clearScene();
        Scene3D.camera.lookAt(new Vector(0, 0, 0), new Vector(0, 0, 1));

        LightSource light = new LightSource(
            new Vector(-5, 5, 0),
            new Color(0xFFFFFF)
        );
        Mesh sphere = new Mesh(Material.JADE(), Shading.SMOOTH);
        sphere.buildSphere(3, 10);
        sphere.transform(Matrix.rotateAroundX(-0.1 * Math.PI).combine(Matrix.translate(0, 0, 7)));
        Scene3D.addLights(light);
        Scene3D.addObjects(sphere);
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
        Mesh floor = new Mesh(Material.RED_RUBBER(), Shading.FLAT);
        floor.buildFunctionPlot(4, 4, 4, 10, new ConstantNoise());
        floor.transform(Matrix.rotateAroundX(0.9 * Math.PI).combine(Matrix.translate(0, 0, 10)));

        Mesh prism = new Mesh(Material.mirror(new Color(0xFFFFFF)), Shading.FLAT);
        prism.buildPrism(10, 12, 6);
        prism.transform(Matrix.rotateAroundX(-0.1 * Math.PI).combine(Matrix.translate(0, 4, 20)));

        Mesh sphere1 = new Mesh(Material.GREEN_RUBBER(), Shading.FLAT);
        sphere1.buildSphere(2, 10);
        sphere1.transform(Matrix.rotateAroundX(-0.1 * Math.PI).combine(Matrix.translate(0, -1, 10)));

        Mesh sphere2 = new Mesh(Material.phong(new Color(0x5555FF), 0.25 * 128), Shading.FLAT);
        sphere2.buildSphere(2, 10);
        sphere2.transform(Matrix.rotateAroundX(-0.1 * Math.PI).combine(Matrix.translate(6, 5, 13)));

        Scene3D.addLights(light1, light2);
        Scene3D.addObjects(floor, prism, sphere1, sphere2);
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

        Mesh terrain = new Mesh(Material.CYAN_PLASTIC(), Shading.SMOOTH);
        terrain.buildFunctionPlot(15, 15, 5, 20, new SimplexNoise());
        terrain.transform(
            Matrix.rotateAroundX(0.5 * Math.PI)
            .combine(Matrix.translate(0, 0, 40))
        );

        Scene3D.addLights(light1, light2);
        Scene3D.addObjects(terrain);

        Random random = new Random();
        
        for (int i = 0; i < 40; i++) {
            int randSign1 = random.nextBoolean() ? 1 : -1;
            int randSign2 = random.nextBoolean() ? 1 : -1;

            Mesh sphere = new Mesh(
                Material.phong(
                    new Color(random.nextInt()),
                    0.3 * 128
                ), 
                Shading.SMOOTH
            );
            sphere.buildSphere(1.5, 20);
            sphere.transform(Matrix.translate(
                randSign1 * 10 * random.nextDouble(), 
                randSign2 * 10 * random.nextDouble(), 
                10 + 20 * random.nextDouble()
            ));
            Scene3D.addObjects(sphere);  
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

        Mesh terrain = new Mesh(Material.SILVER(), Shading.SMOOTH);
        terrain.buildFunctionPlot(50, 50, 1, 20, new PerlinNoise());
        terrain.transform(
            Matrix.rotateAroundX(0.8 * Math.PI)
            .combine(Matrix.translate(0, 0, 12)
        ));

        Mesh torus = new Mesh(Material.SILVER(), Shading.SMOOTH);
        torus.buildTorus(1, 2, 20);
        torus.transform(
            Matrix.rotateAroundX(-0.12 * Math.PI)
            .combine(Matrix.translate(0, 0, 7)
        ));

        Mesh sphere = new Mesh(Material.SILVER(), Shading.SMOOTH);
        sphere.buildSphere(1.5, 20);
        sphere.transform(
            Matrix.rotateAroundX(-0.12 * Math.PI)
            .combine(Matrix.translate(4, 2, 12)
        ));

        Mesh antiPrism = new Mesh(Material.SILVER(), Shading.FLAT);
        antiPrism.buildAntiPrism(7, 4, 1);
        antiPrism.transform(
            Matrix.rotateAroundZ(0.5 * Math.PI)
            .combine(Matrix.rotateAroundY(0.1 * Math.PI))
            .combine(Matrix.translate(-5, 2, 12)
        ));

        Scene3D.addLights(light1, light2);
        Scene3D.addObjects(terrain, torus, sphere, antiPrism);
    }
}
