package graphics3D;

import java.util.Arrays;
import java.util.List;
import java.awt.Graphics;
import java.awt.Toolkit;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Graphics3D extends JPanel {
    private static final long serialVersionUID = 1L;
    Scene3D scene;

    public Graphics3D(Scene3D scene) {
        this.scene = scene;
    }
    @Override
    public void paint(Graphics g) {
        g.drawImage(scene.render(), 0, 0, this);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setTitle("3D Shapes");
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);

        Toolkit tk = Toolkit.getDefaultToolkit();

        Camera camera = new Camera(
            tk.getScreenSize().width, 
            tk.getScreenSize().height, 
            70
        );
        camera.lookAt(new Vector(0, 0, 0), new Vector(0, 0, 1));

        LightSource yellowLight = new LightSource(
            new Vector(-20, 7, 20), 
            new Color(0xFFFB2B), 
            new Color(0xFFFB2B)
        );
        LightSource blueLight = new LightSource(
            new Vector(20, 4, 30), 
            new Color(0x2BCAFF), 
            new Color(0x2BCAFF)
        );
        LightSource magentaLight = new LightSource(
            new Vector(-5, 15, 30), 
            new Color(0xFF2BB1), 
            new Color(0xFF2BB1)
        );

        Matrix translation = Matrix.translate(0, 0, 35);
        Matrix rotation1 = Matrix.rotateAroundX(-0.1 * Math.PI);
        Matrix rotation2 = Matrix.rotateAroundX(-0.25 * Math.PI);

        Mesh torus = new Torus(10, 15, 20);
        torus.setMaterial(Material.SILVER());
        torus.transform(rotation1.combine(translation));

        Mesh sphere = new Sphere(7, 20);
        sphere.setMaterial(Material.SILVER());
        sphere.transform(rotation2.combine(translation));

        Mesh antiPrism = new AntiPrism(8, 8, 3);
        antiPrism.setMaterial(Material.SILVER());
        antiPrism.transform(rotation2.combine(Matrix.translate(10, 10, 35)));

        List<LightSource> lights = Arrays.asList(yellowLight, blueLight, magentaLight);
        List<Mesh> objects = Arrays.asList(torus, sphere, antiPrism);

        Scene3D scene = new Scene3D(camera, lights, objects);
        frame.add(new Graphics3D(scene));
        frame.setVisible(true);
    }
}