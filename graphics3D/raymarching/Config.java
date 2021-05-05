package graphics3D.raymarching;

public class Config {
    public static final int SCREENSIZE_X = 600;
    public static final int SCREENSIZE_Y = 600;
    public static final double CAMERA_FOV = 70;
    public static final boolean ANTI_ALIASING = false;

    public static final boolean SHADOWS = false;
    public static final int MAX_STEPS = 100;
    public static final int MAX_DIST = 100;
    public static final double RAY_HIT_THRESHOLD = 0.001;
    public static final double GRADIENT_EPSILON = 0.001;
}
