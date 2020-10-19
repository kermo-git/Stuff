package graphics3D;

import java.awt.Graphics;
import java.awt.Toolkit;
import javax.swing.JFrame;
import javax.swing.JPanel;


public class Shapes extends JPanel {
    private static final long serialVersionUID = 1L;
    Scene scene;


    public Shapes(Scene scene) {
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

        Scene scene = new Scene(
            tk.getScreenSize().width, 
            tk.getScreenSize().height, 
            70
        );

        LightSource light = new LightSource(
            new Vector(7, -7, 0), 
            new Color(0xFFFFFF), 
            new Color(0xFFFFFF));

        Mesh mesh = new Torus(10, 15, 20);
        mesh.setMaterial(Material.CYAN_PLASTIC());
        mesh.rotateAroundX(0.15*Math.PI);
        mesh.translate(0, 0, 20);

        scene.addLightSources(light);
        scene.setAmbientColor(new Color(0xFFFFFF));
        scene.addShapes(mesh);

        frame.add(new Shapes(scene));
        frame.setVisible(true);
    }
}