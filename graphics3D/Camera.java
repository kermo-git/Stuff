package graphics3D;

public class Camera {
    Matrix cameraToWorld, worldToCamera;
    Vector location;

    double pixelSize, halfPixelSize;
    double halfImageWidth, halfImageHeight;
    double imageWidth, imageHeight;
    int numPixelsX, numPixelsY;


    public Camera(int numPixelsX, int numPixelsY, double FOVdegrees) {
        this.numPixelsX = 2 * numPixelsX;
        this.numPixelsY = 2 * numPixelsY;

        double halfFOVRadians = Math.toRadians(FOVdegrees / 2);

        halfImageWidth = Math.tan(halfFOVRadians);
        halfImageHeight = halfImageWidth * numPixelsY / numPixelsX;

        imageWidth = 2 * halfImageWidth;
        imageHeight = 2 * halfImageHeight;

        pixelSize = 2 * halfImageWidth / numPixelsX;
        halfPixelSize = halfImageWidth / numPixelsX;

        if (pixelSize != (2 * halfImageHeight / numPixelsY)) {
            throw new RuntimeException();
        }
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
        
        if (Math.abs(imageX) > halfImageWidth ||
            Math.abs(imageY) > halfImageHeight)
            return null;
        
        imageX += halfImageWidth;
        imageY += halfImageHeight;

        int pixelX = (int) (numPixelsX * imageX / imageWidth);
        int pixelY = (int) ((1 - imageY / imageHeight) * numPixelsY);

        return new Pixel(pixelX, pixelY, 1.0 / depth);
    }
}
