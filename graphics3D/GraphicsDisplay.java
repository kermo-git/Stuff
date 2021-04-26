package graphics3D;

import java.awt.image.BufferedImage;
import java.awt.Graphics;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class GraphicsDisplay extends JPanel {
    private static final long serialVersionUID = 1L;
    private static BufferedImage image;

    static {
        SceneBuilder.buildRayTracingScene();
        image = Scene3D.render();
    }

    @Override
    public void paint(Graphics g) {
        g.drawImage(image, 0, 0, this);
    }

    public static void main(String[] args) {        
        JFrame frame = new JFrame();
        frame.setTitle("3D Shapes");
        frame.setSize(Config.screenSizeX, Config.screenSizeY);
        frame.add(new GraphicsDisplay());
        frame.setVisible(true);
    }
}