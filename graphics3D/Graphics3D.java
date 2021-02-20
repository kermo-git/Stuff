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
        g.drawImage(scene.render(), 0, 0, this);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setTitle("3D Shapes");
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);

        Toolkit tk = Toolkit.getDefaultToolkit();

        Camera camera = new Camera(tk.getScreenSize().width, tk.getScreenSize().height, 70);
        camera.lookAt(new Vector(0, 0, 0), new Vector(0, 0, 35));

        LightSource light1 = new LightSource(
            new Vector(0, 10, 30), 
            new Color(0xFFFFFF), 
            new Color(0xFFFFFF)
        );

        LightSource light2 = new LightSource(
            new Vector(-30, 0, 0), 
            new Color(0xFFFFFF), 
            new Color(0xFFFFFF)
        );

        Mesh sphere = new Mesh(Material.CYAN_PLASTIC(), Shading.FLAT);
        sphere.buildSphere(7, 40);
        sphere.transform(
            Matrix.rotateAroundX(-0.25 * Math.PI)
            .combine(Matrix.translate(-10, 0, 35))
        );

        Mesh antiPrism = new Mesh(Material.CYAN_PLASTIC(), Shading.FLAT);
        antiPrism.buildBipyramid(10, 20, 5);
        antiPrism.transform(
            Matrix.rotateAroundX(-0.15 * Math.PI)
            .combine(Matrix.translate(10, 0, 35))
        );

        List<LightSource> lights = Arrays.asList(light1, light2);
        List<Mesh> objects = Arrays.asList(sphere, antiPrism);

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