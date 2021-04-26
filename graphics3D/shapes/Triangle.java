package graphics3D.shapes;

import graphics3D.*;
import graphics3D.materials.Material;

public class Triangle extends Shape {
    public Vector outNormal, inNormal;
    public Vertex v1, v2, v3;
    public double D = 0, sRec = 0;

    public Triangle(Material material, Vertex v1, Vertex v2, Vertex v3) {
        this.material = material;

        this.v1 = v1; v1.triangles.add(this);
        this.v2 = v2; v2.triangles.add(this);
        this.v3 = v3; v3.triangles.add(this);

        outNormal = new Vector(v1, v3).cross(new Vector(v1, v2));
        sRec = 1 / outNormal.length();

        v1.normal.add(outNormal);
        v2.normal.add(outNormal);
        v3.normal.add(outNormal);
        
        outNormal.normalize();  
        inNormal = outNormal.getScaled(-1);
        D = outNormal.dot(v1);  
    }

    @Override
    public void transform(Matrix rotation, Matrix translation) {
        Matrix fullTransformation = rotation.combine(translation);
        fullTransformation.transform(v1);
        fullTransformation.transform(v2);
        fullTransformation.transform(v3);
        rotation.transform(outNormal);
        inNormal = outNormal.getScaled(-1);
        D = outNormal.dot(v1);
    }
    @Override
    public void transform(Matrix translation) {
        translation.transform(v1);
        translation.transform(v2);
        translation.transform(v3);
        D = outNormal.dot(v1);
    }

    public void rasterize(double[][] depthMap, boolean calculateColor) {
        Pixel p1 = v1.projection;
        Pixel p2 = v2.projection;
        Pixel p3 = v3.projection;

        if (p1 == null || p2 == null || p3 == null) {
            return;
        }
        if (p1.zRec + Config.depthMapBias < depthMap[(int) p1.pixelX][(int) p1.pixelY] &&
            p2.zRec + Config.depthMapBias < depthMap[(int) p2.pixelX][(int) p2.pixelY] &&
            p3.zRec + Config.depthMapBias < depthMap[(int) p3.pixelX][(int) p3.pixelY]) {
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

                // TODO: if the triangle is facing away from the camera,
                // then the condition should be s1 > 0 && s2 > 0 && s3 > 0
                // and the normal should be reversed for shading calculations.
                
                if (s1 < 0 && s2 < 0 && s3 < 0) {
                    w1 = Math.abs(s1 / s);
                    w2 = Math.abs(s2 / s);
                    w3 = 1 - w1 - w2;
                    zRec = w1 * p1.zRec + w2 * p2.zRec + w3 * p3.zRec;

                    if (zRec > depthMap[x][y]) {
                        depthMap[x][y] = zRec;
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

        Vector viewVector = new Vector(Scene3D.camera.location, surfacePoint);
        viewVector.normalize();

        return material.shade(
            viewVector, 
            surfacePoint, 
            outNormal, 
            false, 0
        );
    };


    public RayIntersection getIntersection(Vector rayOrigin, Vector rayDirection) {
        double normalDotDirection = outNormal.dot(rayDirection);
        if (normalDotDirection == 0) {
            return null;
        }
        double distance = (D - outNormal.dot(rayOrigin)) / normalDotDirection;
        if (distance < 0) {
            return null;
        }
        Vector p = new Vector(
            rayOrigin.x + distance * rayDirection.x,
            rayOrigin.y + distance * rayDirection.y,
            rayOrigin.z + distance * rayDirection.z
        );
        Vector edge = new Vector(v1, v2);
        Vector v_p = new Vector(v1, p);

        if (edge.cross(v_p).dot(outNormal) >= 0)
            return null;

        edge = new Vector(v2, v3);
        v_p = new Vector(v2, p);

        if (edge.cross(v_p).dot(outNormal) >= 0)
            return null;

        edge = new Vector(v3, v1);
        v_p = new Vector(v3, p);

        if (edge.cross(v_p).dot(outNormal) >= 0)
            return null;

        if (normalDotDirection > 0) {
            return new RayIntersection(distance, p, inNormal, material);
        } else {
            return new RayIntersection(distance, p, outNormal, material);
        }
    }
}