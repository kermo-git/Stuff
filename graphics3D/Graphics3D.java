package graphics3D;

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

        Scene3D scene = new Scene3D(
            tk.getScreenSize().width, 
            tk.getScreenSize().height, 
            70
        );

        LightSource light = new LightSource(
            new Vector(20, -7, 0), 
            new Color(0xFFFFFF), 
            new Color(0xFFFFFF));

        Mesh torus = new Torus(10, 15, 20);
        torus.setMaterial(Material.CYAN_PLASTIC());
        torus.rotateAroundX(0.1*Math.PI);
        torus.translate(0, 0, 35);

        Mesh sphere = new Sphere(7, 20);
        sphere.setMaterial(Material.COPPER());
        sphere.rotateAroundX(0.25*Math.PI);
        sphere.translate(0, 0, 35);

        scene.addLightSources(light);
        scene.setAmbientColor(new Color(0xFFFFFF));
        scene.addShapes(torus, sphere);

        frame.add(new Graphics3D(scene));
        frame.setVisible(true);
    }
}