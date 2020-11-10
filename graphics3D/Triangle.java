package graphics3D;

import java.awt.image.BufferedImage;

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
public class Triangle implements Comparable<Triangle> {
    Mesh mesh;
    Vertex v1, v2, v3;
    Vector normal;
    int RGB;
    double distance = 0;

    public Triangle(Vertex v1, Vertex v2, Vertex v3, Mesh mesh) {
        this.v1 = v1; v1.triangles.add(this);
        this.v2 = v2; v2.triangles.add(this);
        this.v3 = v3; v3.triangles.add(this);
        this.mesh = mesh;
    }


    public void calculateNormal() {
        normal = new Vector(v1, v3).cross(new Vector(v1, v2));
    }


    public void calculateDistance() {
        double d1 = v1.length();
        double d2 = v2.length();
        double d3 = v3.length();

        distance = (d1 > d2) ? 
        ((d1 > d3) ? d1 : d3) :
        ((d2 > d3) ? d2 : d3);
    }


    @Override
    public int compareTo(Triangle other) {
        if (distance > other.distance)
            return -1;
        else if (distance == other.distance)
            return 0;
        return 1;
    }


    private int findMidX(Pixel p1, Pixel p2, int midY) {
        double ratio = 1.0*(p2.y - midY)/(p2.y - p1.y);
        return (int)(p2.x - ratio*(p2.x - p1.x));
    }


    public void drawLine(BufferedImage screen, int x1, int x2, int y) {
        int width = screen.getWidth();

        int startX = (x1 < x2)? x1 : x2;
        int endX = (x1 < x2)? x2 : x1;

        startX = (startX >= 0)? startX : 0;
        endX = (endX < width)? endX : (width - 1);

        for (int x = startX; x <= endX; x++) {
            screen.setRGB(x, y, RGB);
        }
    }


    public void render(Scene3D scene) {
        RGB = mesh.material.illuminate(scene, v1, normal).getRGB();

        Pixel v1 = scene.project(this.v1);
        Pixel v2 = scene.project(this.v2);
        Pixel v3 = scene.project(this.v3);

        Pixel high, middle, low;

        if (v1.y < v2.y) {
            high = v1; middle = v2;
        } else {
            high = v2; middle = v1;
        }
        if (v3.y < high.y) {
            low = middle; 
            middle = high;
            high = v3;
        } 
        else if (v3.y < middle.y) {
            low = middle;  
            middle = v3;
        } else {
            low = v3;
        }

        int height = scene.screen.getHeight();
        int x1, x2;

        int startY = (high.y >= 0)? high.y : 0;
        int endY = (middle.y < height)? middle.y : (height - 1);

        if (high.y != middle.y && high.y != low.y) {
            for (int y = startY; y < endY; y++) {
                x1 = findMidX(high, middle, y);
                x2 = findMidX(high, low, y);
                drawLine(scene.screen, x1, x2, y);
            }
        }

        startY = endY;
        endY = (low.y < height)? low.y : (height - 1);

        if (low.y != middle.y && low.y != high.y) {
            for (int y = startY; y <= endY; y++) {
                x1 = findMidX(middle, low, y);
                x2 = findMidX(high, low, y);
                drawLine(scene.screen, x1, x2, y);
            }
        }
    }
}