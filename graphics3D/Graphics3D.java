package graphics3D;

import java.util.Arrays;
import java.util.List;

import java.awt.Graphics;
import java.awt.Toolkit;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.event.MouseInputListener;

public class Graphics3D extends JPanel implements KeyListener, MouseInputListener {
    private static final long serialVersionUID = 1L;
    Scene3D scene;

    public Graphics3D(Scene3D scene) {
        this.scene = scene;
        addKeyListener(this);
        addMouseListener(this);
        setFocusable(true);
    }

    @Override
    public void paint(Graphics g) {
        g.drawImage(scene.renderImage(), 0, 0, this);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setTitle("3D Shapes");
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        Toolkit tk = Toolkit.getDefaultToolkit();

        Camera camera = new Camera(tk.getScreenSize().width, tk.getScreenSize().height, 70);
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
        terrain.buildFunctionPlot(10, 10, 10, 50, new Perlin(4, 0.4));
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

        Scene3D scene = new Scene3D(camera, lights, objects);
        frame.add(new Graphics3D(scene));
        frame.setVisible(true);
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // TODO Auto-generated method stub
    }

    @Override
    public void keyPressed(KeyEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void keyReleased(KeyEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void mouseClicked(MouseEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void mousePressed(MouseEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void mouseReleased(MouseEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void mouseEntered(MouseEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void mouseExited(MouseEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void mouseDragged(MouseEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void mouseMoved(MouseEvent e) {
        // TODO Auto-generated method stub

    }
}