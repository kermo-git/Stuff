package graphics3D;

public class Camera {
    Matrix cameraToWorld, worldToCamera;
    Vector location;

    double halfViewPortWidth, halfViewPortHeight;
    int screenWidth, screenHeight;


    public Camera(int screenWidth, int screenHeight, double FOVdegrees) {
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;

        double halfFOVRadians = Math.toRadians(FOVdegrees / 2);

        halfViewPortWidth = Math.tan(halfFOVRadians);
        halfViewPortHeight = halfViewPortWidth * screenHeight / screenWidth;
    }


    public void lookAt(Vector from, Vector to) {
        location = from;
        Vector forward = new Vector(to, from);
        forward.normalize();

        Vector posYAxis = new Vector(0, 1, 0);
        Vector negYAxis = new Vector(0, -1, 0);
        Vector right = null;

        if (forward.equals(posYAxis) || forward.equals(negYAxis)) {
            right = new Vector(0, 0, 1);
        } else {
            right = posYAxis.cross(forward);
        }
        Vector up = forward.cross(right);
        cameraToWorld = Matrix.coordinateSystem(right, up, forward, from);
        worldToCamera = cameraToWorld.inverse();
    }


    public Pixel project(Vector v) {
        Vector result = worldToCamera.getTransformed(v);
        if (result.z > 0)
            return null;

        double depth = -result.z;
        result.x /= depth;
        result.y /= depth;
        
        if (Math.abs(result.x) > halfViewPortWidth ||
            Math.abs(result.y) > halfViewPortHeight)
            return null;
        
        double normalizedX = (halfViewPortWidth + result.x) / (2 * halfViewPortWidth);
        double normalizedY = (halfViewPortHeight + result.y) / (2 * halfViewPortHeight);

        return new Pixel(
            (int) (normalizedX * screenWidth), 
            (int) ((1 - normalizedY) * screenHeight), 
            depth
        );
    }
}
