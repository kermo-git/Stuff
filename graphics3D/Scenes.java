package graphics3D;

import java.util.Arrays;
import java.util.List;

public class Scenes {
    public static Scene3D perlinTerrain(int screenWidth, int screenHeight) {
        Camera camera = new Camera(screenWidth, screenHeight, 70);
        camera.lookAt(new Vector(0, 0, 0), new Vector(0, 0, 1));

        LightSource light1 = new LightSource(
            new Vector(-20, 30, 50), 
            new Color(0x26CAFC), 
            new Color(0x26CAFC)
        );

        LightSource light2 = new LightSource(
            new Vector(20, 30, 50), 
            new Color(0xFC26F5), 
            new Color(0xFC26F5)
        );

        LightSource light3 = new LightSource(
            new Vector(40, -10, 0), 
            new Color(0xFFF947), 
            new Color(0xFFF947)
        );

        Mesh terrain = new Mesh(Material.SILVER(), Shading.PHONG);
        terrain.buildFunctionPlot(15, 10, 10, 50, new Perlin(4, 0.4));
        terrain.transform(
            Matrix.rotateAroundX(0.8*Math.PI)
            .combine(Matrix.translate(0, 0, 50))
        );

        Mesh object1 = new Mesh(Material.SILVER(), Shading.PHONG);
        object1.buildTorus(5, 10, 20);
        object1.transform(
            Matrix.rotateAroundX(-0.25*Math.PI)
            .combine(Matrix.translate(20, 15, 50)
        ));

        Mesh object2 = new Mesh(Material.SILVER(), Shading.PHONG);
        object2.buildSphere(5, 20);
        object2.transform(
            Matrix.rotateAroundX(-0.25*Math.PI)
            .combine(Matrix.translate(-20, 15, 50)
        ));

        Mesh object3 = new Mesh(Material.SILVER(), Shading.FLAT);
        object3.buildAntiPrism(8, 20, 5);
        object3.transform(
            Matrix.rotateAroundX(0.3*Math.PI)
            .combine(Matrix.rotateAroundY(0.2*Math.PI))
            .combine(Matrix.translate(-5, 10, 50)
        ));

        List<LightSource> lights = Arrays.asList(light1, light2);
        List<Mesh> objects = Arrays.asList(terrain, object1, object2, object3);

        return new Scene3D(camera, lights, objects);
    }


    public static Scene3D torus(int screenWidth, int screenHeight) {
        Camera camera = new Camera(screenWidth, screenHeight, 70);
        camera.lookAt(new Vector(0, 0, 0), new Vector(0, 0, 1));

        LightSource light = new LightSource(
            new Vector(10, 7, 0), 
            new Color(0xFFFFFF), 
            new Color(0xFFFFFF)
        );

        Mesh object = new Mesh(Material.EMERALD(), Shading.PHONG);
        object.buildTorus(5, 10, 20);
        object.transform(
            Matrix.rotateAroundX(-0.12*Math.PI)
            .combine(Matrix.translate(0, 0, 25)
        ));

        List<LightSource> lights = Arrays.asList(light);
        List<Mesh> objects = Arrays.asList(object);

        return new Scene3D(camera, lights, objects);
    }


    public static Scene3D sphere(int screenWidth, int screenHeight) {
        Camera camera = new Camera(screenWidth, screenHeight, 70);
        camera.lookAt(new Vector(0, 0, 0), new Vector(0, 0, 1));

        LightSource light = new LightSource(
            new Vector(10, 7, 0), 
            new Color(0xFFFFFF), 
            new Color(0xFFFFFF)
        );

        Mesh object = new Mesh(Material.GOLD(), Shading.FLAT);
        object.buildSphere(10, 10);
        object.transform(
            Matrix.rotateAroundX(-0.12*Math.PI)
            .combine(Matrix.translate(0, 0, 30)
        ));

        List<LightSource> lights = Arrays.asList(light);
        List<Mesh> objects = Arrays.asList(object);

        return new Scene3D(camera, lights, objects);
    }
}
