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
    Vertex v1, v2, v3;
    Vector normal;
    int RGB;
    double distance = 0;

    public Triangle(Vertex v1, Vertex v2, Vertex v3) {
        this.v1 = v1; v1.triangles.add(this);
        this.v2 = v2; v2.triangles.add(this);
        this.v3 = v3; v3.triangles.add(this);
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


    private int find_mid_x(int x1, int y1, int x2, int y2, int y) {
        double ratio = 1.0*(y2 - y)/(y2 - y1);
        return (int)(x2 - ratio*(x2 - x1));
    }


    public void draw_line(BufferedImage img, int x1, int x2, int y) {
        int w = img.getWidth();

        int start_x = (x1 < x2)? x1 : x2;
        int end_x = (x1 < x2)? x2 : x1;

        start_x = (start_x >= 0)? start_x : 0;
        end_x = (end_x < w)? end_x : (w - 1);

        for (int x = start_x; x <= end_x; x++) {
            img.setRGB(x, y, RGB);
        }
    }


    public void render(Scene3D scene, BufferedImage img) {
        Vertex high, middle, low;

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

        int rx1 = scene.projectX(high.x, high.z);
        int ry1 = scene.projectY(high.y, high.z);

        int rx2 = scene.projectX(middle.x, middle.z);
        int ry2 = scene.projectY(middle.y, middle.z);
        
        int rx3 = scene.projectX(low.x, low.z);
        int ry3 = scene.projectY(low.y, low.z);

        int h = img.getHeight();
        int x1, x2;

        int start_y = (ry1 >= 0)? ry1 : 0;
        int end_y = (ry2 < h)? ry2 : (h - 1);

        if (ry1 != ry2 && ry1 != ry3) {
            for (int y = start_y; y < end_y; y++) {
                x1 = find_mid_x(rx1, ry1, rx2, ry2, y);
                x2 = find_mid_x(rx1, ry1, rx3, ry3, y);
                draw_line(img, x1, x2, y);
            }
        }

        start_y = end_y;
        end_y = (ry3 < h)? ry3 : (h - 1);

        if (ry3 != ry2 && ry3 != ry1) {
            for (int y = start_y; y <= end_y; y++) {
                x1 = find_mid_x(rx2, ry2, rx3, ry3, y);
                x2 = find_mid_x(rx1, ry1, rx3, ry3, y);
                draw_line(img, x1, x2, y);
            }
        }
    }
}