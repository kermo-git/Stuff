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
        double imageX = result.x / depth;
        double imageY = result.y / depth;
        
        if (Math.abs(imageX) > halfViewPortWidth ||
            Math.abs(imageY) > halfViewPortHeight)
            return null;
        
        double normalizedX = (halfViewPortWidth + imageX) / (2 * halfViewPortWidth);
        double normalizedY = (halfViewPortHeight + imageY) / (2 * halfViewPortHeight);

        int screenX = (int) (normalizedX * screenWidth);
        int screenY = (int) ((1 - normalizedY) * screenHeight);

        return new Pixel(screenX, screenY, 1.0 / depth);
    }
}
