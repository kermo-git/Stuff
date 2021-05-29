package graphics3D.raymarching;

public class Config {
    public static final int SCREENSIZE_X = 600;
    public static final int SCREENSIZE_Y = 600;
    public static final double CAMERA_FOV = 70;
    public static final boolean ANTI_ALIASING = false;

    public static final int MAX_DIST = 1000;
    public static final double RAY_HIT_THRESHOLD = 0.001;
    public static final double GRADIENT_EPSILON = 0.001;
    
    public static final double INACCURACY_MULTIPLIER = 0.5;

    public static final double RAY_HIT_BIAS = 0.005;
    
    public static final boolean SHADOWS = true;
    public static final boolean SOFT_SHADOWS = true;

    public static final boolean REFLECTIONS = true;
    public static final int MAX_BOUNCES = 5;
}
