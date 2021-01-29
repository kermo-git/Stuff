package graphics3D;

import java.util.List;
import java.util.ArrayList;
import java.awt.image.BufferedImage;


public class Scene3D {
    Camera camera;
    double[][] zBuffer;
    BufferedImage canvas;

    List<LightSource> lights;
    List<Mesh> objects;

    public Scene3D(Camera camera, List<LightSource> lights, List<Mesh> objects) {
        this.camera = camera;
        this.lights = lights;
        this.objects = objects;
        clear();
    }

    private void clear() {
        zBuffer = new double[camera.screenWidth][camera.screenHeight];
        int x, y;
        for (x = 0; x < camera.screenWidth; x++) {
            for (y = 0; y < camera.screenHeight; y++) {
                zBuffer[x][y] = Double.MAX_VALUE;
            }
        }
        canvas = new BufferedImage(
            camera.screenWidth, 
            camera.screenHeight, 
            BufferedImage.TYPE_INT_RGB
        );
    }

    // public Pixel project(Vector v) {
    //     return new Pixel(
    //         (int) (screenWidth * (v.x / (2 * v.z * tanHalfFOVX) + 0.5)),
    //         (int) (screenHeight * (v.y / (2 * v.z * tanHalfFOVY) + 0.5))
    //     );
    // }

    public BufferedImage render() {
        clear();
        List<Triangle> triangles = new ArrayList<>();

        for (Mesh object : objects) {
            object.calculateNormals();
            triangles.addAll(object.triangles);
        }
        for (Triangle t : triangles) {
            t.render(this);
        }
        return canvas;
    }
}
