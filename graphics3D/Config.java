package graphics3D;
// import java.awt.Toolkit;

public class Config {
    public static enum ShadowType {
        NO_SHADOWS, SHADOW_MAPPING, SHADOW_RAYS
    }
    // GENERAL
        
    public static final int screenSizeX = 600; // Toolkit.getDefaultToolkit().getScreenSize().width;
    public static final int screenSizeY = 600; // Toolkit.getDefaultToolkit().getScreenSize().height;
    public static final boolean antiAliasing = false;
    public static final double cameraFOV = 70;

    public static final boolean initialRasterization = false; 
    public static final ShadowType shadowType = ShadowType.SHADOW_RAYS;

    // RASTERIZATION

    public static final int shadowMapResolution = antiAliasing ? 4 * screenSizeX : 2 * screenSizeX;
    public static final double 
        depthMapBias = 0.001,
        shadowBufferBias = 0.001,
        shadowMapFOV = 120;

    // RAY TRACING

    public static final int rayTracingMaxDepth = 5;
    public static final double rayHitPointBias = 0.001;
}
