package graphics3D;

import java.awt.Graphics;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class GraphicsDisplay extends JPanel {
    private static final long serialVersionUID = 1L;

    @Override
    public void paint(Graphics g) {
        g.drawImage(Scene3D.draw(), 0, 0, this);
    }

    public static void main(String[] args) {
        SceneBuilder.buildScene();
        
        JFrame frame = new JFrame();
        frame.setTitle("3D Shapes");
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.add(new GraphicsDisplay());
        frame.setVisible(true);
    }
}