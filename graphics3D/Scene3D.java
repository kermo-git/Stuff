package graphics3D;

import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import java.awt.image.BufferedImage;


public class Scene3D {
    private int screenWidth;
    private int screenHeight;
    BufferedImage screen;

    private double tanHalfFOVX;
    private double tanHalfFOVY;

    private List<Mesh> shapes = new ArrayList<>();
    Color ambientColor;
    List<LightSource> lights = new ArrayList<>();


    public Scene3D(int screenWidth, int screenHeight, double FOVdegrees) {
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;

        tanHalfFOVX = Math.tan(Math.toRadians(FOVdegrees/2));
        tanHalfFOVY = screenHeight * tanHalfFOVX / screenWidth;
    }


    public void setAmbientColor(Color color) {
        this.ambientColor = color;
    }
    public void addLightSources(LightSource ...lights) {
        for (LightSource light : lights) {
            this.lights.add(light);
        }
    }
    public void addShapes(Mesh ...shapes) {
        for (Mesh shape : shapes) {
            shape.prepare();
            this.shapes.add(shape);
        }
    }


    public Pixel project(Vector v) {
        return new Pixel(
            (int) (screenWidth * (v.x / (2 * v.z * tanHalfFOVX) + 0.5)),
            (int) (screenHeight * (v.y / (2 * v.z * tanHalfFOVY) + 0.5))
        );
    }


    public BufferedImage render() {
        screen = new BufferedImage(screenWidth, screenHeight, BufferedImage.TYPE_INT_RGB);
        List<Triangle> triangles = new ArrayList<>();

        for (Mesh shape : shapes) {
            triangles.addAll(shape.triangles);
        }
        Collections.sort(triangles);
        for (Triangle t : triangles) {
            t.render(this);
        }
        return screen;
    }
}
