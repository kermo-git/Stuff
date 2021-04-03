package graphics3D;
import java.awt.Toolkit;

public class Config {
    private static Toolkit tk = Toolkit.getDefaultToolkit();
        
    public static int screenSizeX = tk.getScreenSize().width;
    public static int screenSizeY = tk.getScreenSize().height;
    public static boolean antiAliasing = true;

    public static boolean renderShadows = true;
    public static int shadowResolution = antiAliasing ? 
        4 * screenSizeX : 2 * screenSizeX;
    
    public static double 
        shadowBufferBias = 0.001,
        zBufferBias = 0.001,
        cameraFOV = 70,
        lightFOV = 120;

    public static Color ambient = new Color(1, 1, 1);
}
