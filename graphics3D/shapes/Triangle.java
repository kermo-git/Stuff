package graphics3D.shapes;

import graphics3D.*;

public class Triangle extends Shape {
    public Vector normal;
    public Vertex v1, v2, v3;

    public Triangle(Material material, Vertex v1, Vertex v2, Vertex v3) {
        this.material = material;

        this.v1 = v1; v1.triangles.add(this);
        this.v2 = v2; v2.triangles.add(this);
        this.v3 = v3; v3.triangles.add(this);

        normal = new Vector(v1, v3).cross(new Vector(v1, v2));

        v1.normal.add(normal);
        v2.normal.add(normal);
        v3.normal.add(normal);
        
        normal.normalize();
    }
    private static double min(double a, double b) {
        return (a < b) ? a : b;
    }
    private static double max(double a, double b) {
        return (a > b) ? a : b;
    }

    @Override
    public void transform(Matrix rotation, Matrix translation) {
        Matrix fullTransformation = rotation.combine(translation);
        fullTransformation.transform(v1);
        fullTransformation.transform(v2);
        fullTransformation.transform(v3);
        rotation.transform(normal);
    }
    @Override
    public void transform(Matrix translation) {
        translation.transform(v1);
        translation.transform(v2);
        translation.transform(v3);
    }

    public void rasterize(double[][] depthBuffer, boolean calculateColor) {
        Pixel p1 = v1.projection;
        Pixel p2 = v2.projection;
        Pixel p3 = v3.projection;

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

        double w1, w2, w3, zRec;

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

                if (s1 < 0 && s2 < 0 && s3 < 0) {
                    w1 = Math.abs(s1 / s);
                    w2 = Math.abs(s2 / s);
                    w3 = 1 - w1 - w2;
                    zRec = w1 * p1.zRec + w2 * p2.zRec + w3 * p3.zRec;

                    if (zRec > depthBuffer[x][y]) {
                        depthBuffer[x][y] = zRec;
                        if (calculateColor) {
                            Scene3D.frameBuffer[x][y] = doColorCalculation(w1, w2, w3, zRec);
                        }
                    }
                }
            }
        }
    }


    protected Color doColorCalculation(double w1, double w2, double w3, double zRec) {
        Vector surfacePoint = new Vector();
        surfacePoint.add(w1 * v1.projection.zRec, v1);
        surfacePoint.add(w2 * v2.projection.zRec, v2);
        surfacePoint.add(w3 * v3.projection.zRec, v3);
        surfacePoint.scale(1.0 / zRec);

        return material.getRasterizationPhongColor(Scene3D.camera.location, surfacePoint, normal);
    };

    // public double getIntersectionDistance(Vector origin, Vector direction) {
    //     Vector v12 = new Vector(v1, v2);
    //     Vector v13 = new Vector(v1, v3);
    //     Vector vo = new Vector(v1, origin);

    //     double A = vo.x * v13.y - vo.y * v13.x;
    //     double B = v13.y * direction.x - v13.x * direction.y;
    //     double C = v12.x * v13.y - v12.y * v13.x;

    //     double D = vo.y * v13.z - vo.z * v13.y;
    //     double E = v13.z * direction.y - v13.y * direction.z;
    //     double F = v12.y * v13.z - v12.z * v13.y;

    //     double distance = (C * D - F * A) / (F * B - C * E);
    //     if (distance < 0) {
    //         return -1;
    //     }
    //     double w2 = (A + B * distance) / C;
    //     if (w2 < 0 || w2 > 1) {
    //         return -1;
    //     }
    //     double w3 = (vo.y - v12.y * w2 + direction.y * distance) / v13.y;
    //     if (w3 < 0 || w3 > 1) {
    //         return -1;
    //     }
    //     double w1 = 1 - w2 - w3;
    //     if (w1 < 0) {
    //         return -1;
    //     }

    //     return distance;
    // }


    public RayIntersection getIntersection(Vector origin, Vector direction) {
        Vector o_v1 = new Vector(origin, v1);
        Vector v2_v1 = new Vector(v2, v1);
        Vector v3_v1 = new Vector(v3, v1);

        Vector n = v2_v1.cross(v3_v1);
        double M = direction.dot(n);
        if (M < 0) {
            return null;
        }
        double Mx = o_v1.dot(n);
        double distance = Mx / M;
        if (distance < 0) {
            return null;
        }
        double My = direction.dot(o_v1.cross(v3_v1));
        double w2 = My / M;
        if (w2 < 0 || w2 > 1) {
            return null;
        }
        double Mz = direction.dot(v2_v1.cross(o_v1));
        double w3 = Mz / M;
        if (w3 < 0 || w3 > 1) {
            return null;
        }
        double w1 = 1 - w2 - w3;
        if (w1 < 0) {
            return null;
        }
        Vector location = new Vector(
            origin.x + distance * direction.x,
            origin.y + distance * direction.y,
            origin.z + distance * direction.z
        );
        return new RayIntersection(distance, location, normal, material);
    }
}