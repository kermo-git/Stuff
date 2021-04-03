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
    private static double min(double a, double b) {
        return (a < b) ? a : b;
    }
    private static double max(double a, double b) {
        return (a > b) ? a : b;
    }

    protected Pixel p1, p2, p3;
    protected double w1, w2, w3, zRec;

    protected abstract Color doColorCalculation();

    public void render(double[][] depthBuffer, boolean calculateColor) {
        p1 = v1.projection;
        p2 = v2.projection;
        p3 = v3.projection;
        
        if (p1 == null || p2 == null || p3 == null) {
            return;
        }
        if (p1.zRec + Config.zBufferBias < depthBuffer[(int) p1.pixelX][(int) p1.pixelY] &&
            p2.zRec + Config.zBufferBias < depthBuffer[(int) p2.pixelX][(int) p2.pixelY] &&
            p3.zRec + Config.zBufferBias < depthBuffer[(int) p3.pixelX][(int) p3.pixelY]) {
            return;
        }

        double p32y = p2.pixelY - p3.pixelY;
        double p32x = p2.pixelX - p3.pixelX;

        double p13y = p3.pixelY - p1.pixelY;
        double p13x = p3.pixelX - p1.pixelX;

        double p21y = p1.pixelY - p2.pixelY;
        double p21x = p1.pixelX - p2.pixelX;

        double s = p13x * p21y - p13y * p21x;

        int low_x = (int) min(p1.pixelX, min(p2.pixelX, p3.pixelX));
        int high_x = (int) max(p1.pixelX, max(p2.pixelX, p3.pixelX));
        int low_y = (int) min(p1.pixelY, min(p2.pixelY, p3.pixelY));
        int high_y = (int) max(p1.pixelY, max(p2.pixelY, p3.pixelY));
        
        int x, y;
        double pixelCenterX, pixelCenterY;
        double s1, s2, s3;

        for (x = low_x; x <= high_x; x++) {
            for (y = low_y; y <= high_y; y++) {

                pixelCenterX = x + 0.5;
                pixelCenterY = y + 0.5;
                
                s1 = (pixelCenterX - p3.pixelX) * p32y - 
                     (pixelCenterY - p3.pixelY) * p32x;

                s2 = (pixelCenterX - p1.pixelX) * p13y - 
                     (pixelCenterY - p1.pixelY) * p13x;

                s3 = (pixelCenterX - p2.pixelX) * p21y - 
                     (pixelCenterY - p2.pixelY) * p21x;

                if (s1 >= 0 && s2 >= 0 && s3 >= 0) {
                    w1 = Math.abs(s1 / s);
                    w2 = Math.abs(s2 / s);
                    w3 = 1 - w1 - w2;
                    zRec = w1 * p1.zRec + w2 * p2.zRec + w3 * p3.zRec;
                    
                    if (zRec > depthBuffer[x][y]) {
                        depthBuffer[x][y] = zRec;
                        if (calculateColor) {
                            Scene3D.frameBuffer[x][y] = doColorCalculation();
                        }
                    }
                }
            }
        }
    }
}