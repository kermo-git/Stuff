package graphics3D;

import java.awt.Graphics;
import java.awt.Toolkit;
import javax.swing.JFrame;
import javax.swing.JPanel;

import graphics3D.noise.*;

public class GraphicsDisplay extends JPanel {
    private static final long serialVersionUID = 1L;

    static Scene3D scene;

    @Override
    public void paint(Graphics g) {
        g.drawImage(scene.draw(), 0, 0, this);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setTitle("3D Shapes");
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.add(new GraphicsDisplay());
        frame.setVisible(true);
    }

    static {
        Toolkit tk = Toolkit.getDefaultToolkit();
        scene = new Scene3D(
            tk.getScreenSize().width, 
            tk.getScreenSize().height,
            70
        );

        addLight();
        addTorus(
            new NoiseMaterial(
                new WorleyNoise(200, 5), 
                Material.EMERALD()
            )
        );
    }

    static void addLight() {
        scene.addLights(
            new LightSource(
                new Vector(10, 7, 0), 
                new Color(0xFFFFFF), 
                new Color(0xFFFFFF)
            )
        );
    }

    static void addTorus(Material material, Shading shading) {
        Mesh object = new Mesh(material, shading);
        object.buildTorus(5, 10, 20);
        object.transform(
            Matrix.rotateAroundX(-0.12*Math.PI)
            .combine(Matrix.translate(0, 0, 20)
        ));
        scene.addObjects(object);
    }
    static void addTorus(Material material) {
        addTorus(material, Shading.PHONG);
    }

    static void addSphere(Material material, Shading shading) {
        Mesh object = new Mesh(material, shading);
        object.buildSphere(5, 20);
        object.transform(
            Matrix.rotateAroundX(-0.12*Math.PI)
            .combine(Matrix.translate(0, 0, 15)
        ));
        scene.addObjects(object);
    }
    static void addSphere(Material material) {
        addSphere(material, Shading.PHONG);
    }

    static void addCrumbledSphere(Material material, Noise noise, Shading shading) {
        Mesh object = new Mesh(material, shading);
        object.buildSphere(5, 200);
        object.crumble(noise);
        object.transform(
            Matrix.rotateAroundX(-0.12*Math.PI)
            .combine(Matrix.translate(0, 0, 15)
        ));
        scene.addObjects(object);
    }
    static void addCrumbledSphere(Material material, Noise noise) {
        addCrumbledSphere(material, noise, Shading.PHONG);
    }

    static void addNoisePlot(Material material, Noise noise, int density, Shading shading) {
        Mesh object = new Mesh(material, shading);
        object.buildFunctionPlot(30, 20, 5, density, noise);
        object.transform(
            Matrix.rotateAroundX(0.8*Math.PI)
            .combine(Matrix.translate(0, 0, 60)
        ));
        scene.addObjects(object);
    }

    static void addNoisePlot(Material material, Noise noise, int density) {
        addNoisePlot(material, noise, density, Shading.PHONG);
    }
}