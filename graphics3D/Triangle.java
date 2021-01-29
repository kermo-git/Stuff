package graphics3D;

// https://www.javatpoint.com/computer-graphics-phong-shading

/*
 * Olgu P mingi punkt ja NP pinna normaalvektor selles punktis.
 * Kui meil on valgusmudel (näiteks Phong reflection model), saame
 * arvutada valguse intensiivsuse IP selles punktis.
 * 
 * 1, 2 ja 3 - kolmnurga tipud, vastavalt koordinaatidega
 * (X1, Y1), (X2, Y2) ja (X3, Y3).
 * 
 * A ja B - scanline lõikepunktid vastavalt külgedega 12 ja 13.
 * Nende koordinaadid on (XA, YS) ja (XB, YS).
 * 
 * P - punkt, mille valgustaset soovime leida.
 *            1
 *   
 *------A-----P-----B------- scanline (YS)
 * 
 * 2                     3 
 * 
 * Gouraud shading
 * I1, I2, I3 - tippude 1, 2 ja 3 valgustatuse tasemed (Arvutatud näiteks Phong reflection model'i abil)
 * 
 * IA = (I1*(YS - Y2) + I2*(Y1 - YS))/(Y1 - Y2)
 * IB = (I1*(YS - Y3) + I3*(Y1 - YS))/(Y1 - Y3)
 * IP = (IA*(XB - XP) + IB*(XP - XA))/(XB - XA)
 * 
 * Phong shading (mitte segi ajada Phong reflection model'iga)
 * N1, N2, N3 - tippude 1, 2 ja 3 normaalvektorid
 * 
 * NA = (N1*(YS - Y2) + N2*(Y1 - YS))/(Y1 - Y2)
 * NB = (N1*(YS - Y3) + N3*(Y1 - YS))/(Y1 - Y3)
 * NP = (NA*(XB - XP) + NB*(XP - XA))/(XB - XA)
 */
public class Triangle {
    Material material;
    Vertex v1, v2, v3;
    double distance = 0;


    public Triangle(Vertex v1, Vertex v2, Vertex v3) {
        this.v1 = v1; v1.triangles.add(this);
        this.v2 = v2; v2.triangles.add(this);
        this.v3 = v3; v3.triangles.add(this);
    }
    public Vector getNormal() {
        return new Vector(v1, v3).cross(new Vector(v1, v2));
    }
    private static int min(int a, int b) {
        return (a < b) ? a : b;
    }
    private static int max(int a, int b) {
        return (a > b) ? a : b;
    }


    public void render(Scene3D scene) {
        Pixel p1 = scene.camera.project(v1);
        Pixel p2 = scene.camera.project(v2);
        Pixel p3 = scene.camera.project(v3);
        if (p1 == null || p2 == null || p3 == null) {
            return;
        }

        int low_x = min(p1.x, min(p2.x, p3.x));
        int high_x = max(p1.x, max(p2.x, p3.x));
        int low_y = min(p1.y, min(p2.y, p3.y));
        int high_y = max(p1.y, max(p2.y, p3.y));
        
        int x, y, sum, color;
        int det1, det2, det3;
        double lambda1, lambda2, lambda3, depth;

        Vector point, normal;

        for (x = low_x; x <= high_x; x++) {
            for (y = low_y; y <= high_y; y++) {

                det1 = (x - p3.x)*(p2.y - p3.y) - (y - p3.y)*(p2.x - p3.x);
                det2 = (x - p1.x)*(p3.y - p1.y) - (y - p1.y)*(p3.x - p1.x);
                det3 = (x - p2.x)*(p1.y - p2.y) - (y - p2.y)*(p1.x - p2.x);

                if (det1 >= 0 && det2 >= 0 && det3 >= 0) {
                    sum = det1 + det2 + det3;
                    lambda1 = 1.0 * det1 / sum;
                    lambda2 = 1.0 * det2 / sum;
                    lambda3 = 1.0 * det3 / sum;

                    depth = lambda1*p1.depth + lambda2*p2.depth + lambda3*p3.depth;
                    if (depth < scene.zBuffer[x][y]) {
                        scene.zBuffer[x][y] = depth;

                        normal = v1.normal.scale(lambda1);
                        normal.add(v2.normal.scale(lambda2));
                        normal.add(v3.normal.scale(lambda3));

                        point = v1.scale(lambda1);
                        point.add(v2.scale(lambda2));
                        point.add(v3.scale(lambda3));
                        
                        color = material.illuminate(scene, point, normal).getRGBhex();
                        scene.canvas.setRGB(x, y, color);
                    }
                }
            }
        }
    }
}