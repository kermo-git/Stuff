package graphics3D.raytracing;

public class Config {
    public static final int SCREENSIZE_X = 600;
    public static final int SCREENSIZE_Y = 600;
    public static final double CAMERA_FOV = 70;
    public static final boolean ANTI_ALIASING = false;

    public static final boolean SHADOWS = true;
    public static final boolean REFLECTIONS = true;
    public static final int MAX_RAY_BOUNCES = 5;
    public static final double RAY_HIT_BIAS = 0.001;
}
