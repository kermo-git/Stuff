package graphics3D;

public abstract class Triangle {
    Material material;
    Vertex v1, v2, v3;
    Vector normal;

    public Triangle(Material material, Vertex v1, Vertex v2, Vertex v3) {
        this.material = material;
        this.v1 = v1; v1.triangles.add(this);
        this.v2 = v2; v2.triangles.add(this);
        this.v3 = v3; v3.triangles.add(this);
    }
    private static int min(int a, int b) {
        return (a < b) ? a : b;
    }
    private static int max(int a, int b) {
        return (a > b) ? a : b;
    }

    protected Pixel p1, p2, p3;
    protected double q1, q2, q3, zRec;

    protected abstract Color interpolate(Scene3D scene);

    public void render(Scene3D scene) {
        p1 = v1.projection;
        p2 = v2.projection;
        p3 = v3.projection;
        if (p1 == null || p2 == null || p3 == null) {
            return;
        }
        
        int low_x = min(p1.x, min(p2.x, p3.x));
        int high_x = max(p1.x, max(p2.x, p3.x));
        int low_y = min(p1.y, min(p2.y, p3.y));
        int high_y = max(p1.y, max(p2.y, p3.y));

        int x, y;
        double s1, s2, s3;
        double s = (p2.x - p1.x)*(p3.y - p1.y) - (p2.y - p1.y)*(p3.x - p1.x);

        for (x = low_x; x <= high_x; x++) {
            for (y = low_y; y <= high_y; y++) {

                s1 = (x - p3.x)*(p2.y - p3.y) - (y - p3.y)*(p2.x - p3.x);
                s2 = (x - p1.x)*(p3.y - p1.y) - (y - p1.y)*(p3.x - p1.x);
                s3 = (x - p2.x)*(p1.y - p2.y) - (y - p2.y)*(p1.x - p2.x);

                if (s1 >= 0 && s2 >= 0 && s3 >= 0) {
                    q1 = s1 / s;
                    q2 = s2 / s;
                    q3 = 1 - q1 - q2;

                    zRec = q1 * p1.zRec + q2 * p2.zRec + q3 * p3.zRec;

                    if (zRec > scene.zBuffer[x][y]) {
                        scene.zBuffer[x][y] = zRec;
                        scene.canvas.setRGB(x, y, interpolate(scene).getRGBhex());
                    }
                }
            }
        }
    }
}