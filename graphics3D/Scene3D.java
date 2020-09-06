package graphics3D;

import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import javax.swing.JFrame;
import javax.swing.JPanel;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;


public class Scene3D extends JPanel {
    private static final long serialVersionUID = 1L;

    int screenWidth;
    int screenHeight;

    double tanHalfFOVX;
    double tanHalfFOVY;


    public Scene3D(int screenWidth, int screenHeight, double FOVdegrees) {
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;

        tanHalfFOVX = Math.tan(Math.toRadians(FOVdegrees/2));
        tanHalfFOVY = screenHeight * tanHalfFOVX / screenWidth;
    }

    
    public int projectX(double x, double z) {
        return (int) (screenWidth * (x / (2 * z * tanHalfFOVX) + 0.5));
    }
    public int projectY(double y, double z) {
        return (int) (screenHeight * (y / (2 * z * tanHalfFOVY) + 0.5));
    }


    List<LightSource> lights = new ArrayList<>();


    public void addLightSources(LightSource ...lights) {
        for (LightSource light : lights) {
            this.lights.add(light);
        }
    }


    int ambientRed;
    int ambientGreen;
    int ambientBlue;

    
    public void setAmbientLight(int RGB) {
        ambientRed = (RGB >> 16) & 0xFF;
        ambientGreen = (RGB >> 8) & 0xFF;
        ambientBlue = RGB & 0xFF;
    }


    List<Mesh> shapes = new ArrayList<>();


    public void addShapes(Mesh ...shapes) {
        for (Mesh shape : shapes) {
            this.shapes.add(shape);
            shape.prepare(this);
        }
        Collections.sort(this.shapes);
    }
    

    @Override
    public void paint(Graphics g) {
        BufferedImage img = new BufferedImage(screenWidth, screenHeight, BufferedImage.TYPE_INT_RGB);
        for (Mesh shape : shapes) {
            shape.render(this, img);
        }
        g.drawImage(img, 0, 0, this);
    }


    public static void main(String[] args) {
        LightSource light1 = new LightSource(-5, -10, 20, 0x00FFB7, 0x00FFB7);
        LightSource light2 = new LightSource(5, -7, 15, 0xFF00E6, 0xFF00E6);
        LightSource light3 = new LightSource(-10, 10, 10, 0xFFFF00, 0xFFFF00);

        Mesh mesh = new Torus(5, 10, 50);
        mesh.setMaterial(Material.SILVER());
        mesh.rotateAroundX(0.15*Math.PI);
        mesh.translate(0, 0, 20);

        JFrame frame = new JFrame();
        frame.setTitle("3D Shapes");
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);

        Toolkit tk = Toolkit.getDefaultToolkit();

        Scene3D scene = new Scene3D(
            tk.getScreenSize().width, 
            tk.getScreenSize().height, 
            70
        );
        scene.addLightSources(light1, light2, light3);
        scene.setAmbientLight(0xFFFFFF);
        scene.addShapes(mesh);

        frame.add(scene);
        frame.setVisible(true);
    }
}