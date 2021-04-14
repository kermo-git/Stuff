package graphics3D;
import java.awt.Toolkit;

public class Config {
    private static Toolkit tk = Toolkit.getDefaultToolkit();
        
    public static int screenSizeX = 300; // tk.getScreenSize().width;
    public static int screenSizeY = 300; // tk.getScreenSize().height;
    public static boolean antiAliasing = false;

    public static boolean shadowMapping = true;
    public static int shadowResolution = antiAliasing ? 4 * screenSizeX : 2 * screenSizeX;
    
    public static double 
        shadowBufferBias = 0.001,
        rayHitPointBias = 0.001,
        zBufferBias = 0.001,
        cameraFOV = 70,
        shadowMapFOV = 120;

    public static Color sceneAmbientColor = new Color(1, 1, 1);

    public static int rayTracingMaxDepth = 5;
}
