package graphics3D;


import java.awt.image.BufferedImage;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;


public class Mesh {
    List<Triangle> triangles = new ArrayList<>();
    List<Vertex> vertices = new ArrayList<>();
    Material material;


    public void setMaterial(Material material) {
        this.material = material;
    }


    public void prepare() {
        for (Triangle triangle : triangles) {
            triangle.calculateNormal();
            triangle.calculateDistance();
        }
        for (Vertex vertex : vertices) {
            vertex.calculateNormal();
        }
        for (Triangle triangle : triangles) {
            triangle.normal.normalize();
        }
    }


    public void translate(double x, double y, double z) {
        for (Vertex v : vertices) {
            v.translate(x, y, z);
        }
    }
    public void rotateAroundX(double radians) {
        double sin = Math.sin(radians);
        double cos = Math.cos(radians);
        
        for (Vertex v : vertices) {
            v.rotateAroundX(sin, cos);
        }
    }
    public void rotateAroundY(double radians) {
        double sin = Math.sin(radians);
        double cos = Math.cos(radians);
        
        for (Vertex v : vertices) {
            v.rotateAroundY(sin, cos);
        }
    }
    public void rotateAroundZ(double radians) {
        double sin = Math.sin(radians);
        double cos = Math.cos(radians);
        
        for (Vertex v : vertices) {
            v.rotateAroundZ(sin, cos);
        }
    }


    protected List<Vertex> XYregularPolygon(int n, double radius) {
        List<Vertex> result = new ArrayList<>();
        double alpha = (2 * Math.PI) / n;

        for (int i = 0; i < n; i++) {
            Vertex v = new Vertex(0, radius, 0);
            v.rotateAroundZ(alpha*i);
            result.add(v);
        }
        vertices.addAll(result);
        return result;
    }
    protected List<Vertex> YZregularPolygon(int n, double radius) {
        List<Vertex> result = new ArrayList<>();
        double alpha = (2 * Math.PI) / n;

        for (int i = 0; i < n; i++) {
            Vertex v = new Vertex(0, radius, 0);
            v.rotateAroundX(alpha*i);
            result.add(v);
        }
        vertices.addAll(result);
        return result;
    }
    protected List<Vertex> XZregularPolygon(int n, double radius) {
        List<Vertex> result = new ArrayList<>();
        double alpha = (2 * Math.PI) / n;

        for (int i = 0; i < n; i++) {
            Vertex v = new Vertex(radius, 0, 0);
            v.rotateAroundY(alpha*i);
            result.add(v);
        }
        vertices.addAll(result);
        return result;
    }


    protected void buildPrismSurface(List<Vertex> circle1, List<Vertex> circle2) {
        Iterator<Vertex> it1 = circle1.iterator();
        Iterator<Vertex> it2 = circle2.iterator();

        Vertex start_1 = it1.next();
        Vertex start_2 = it2.next();

        Vertex prev_1 = start_1;
        Vertex prev_2 = start_2;

        Vertex next_1, next_2;

        while (it1.hasNext() && it2.hasNext()) {
            next_1 = it1.next();
            next_2 = it2.next();

            triangles.add(new Triangle(next_1, prev_2, prev_1, this));
            triangles.add(new Triangle(next_1, next_2, prev_2, this));

            prev_1 = next_1; 
            prev_2 = next_2;
        }
        triangles.add(new Triangle(start_1, prev_2, prev_1, this));
        triangles.add(new Triangle(start_1, start_2, prev_2, this));
    }


    protected void buildPyramidSurface(Vertex apex, List<Vertex> circle) {
        vertices.add(apex);
        Iterator<Vertex> it = circle.iterator();

        Vertex start = it.next();
        Vertex prev = start, next;

        while (it.hasNext()) {
            next = it.next();
            triangles.add(new Triangle(apex, prev, next, this));
            prev = next;
        }
        triangles.add(new Triangle(apex, prev, start, this));
    }
}


class Torus extends Mesh {

    public Torus(double innerRadius, double outerRadius, int level_of_detail) {

        double mean_radius = (outerRadius + innerRadius)/2;
        double tube_radius = (outerRadius - innerRadius)/2;
    
        double twist_rotation = Math.PI/level_of_detail;

        double triangle_side = 2*tube_radius*Math.tan(twist_rotation);
        int num_circles = (int) (2*Math.PI*outerRadius/triangle_side);

        if (num_circles%2 == 1)
            num_circles += 1;

        double curve_rotation = 2*Math.PI/num_circles;

        List<Vertex> first_circle = YZregularPolygon(level_of_detail, tube_radius);
        for (Vertex v : first_circle) {
            v.translate(0, 0, mean_radius);
        }

        List<Vertex> circle1 = first_circle, circle2 = null;

        for (int i = 1; i <= num_circles - 1; i++) {
            circle2 = YZregularPolygon(level_of_detail, tube_radius);

            for (Vertex v : circle2) {
                v.rotateAroundX(i*twist_rotation);
                v.translate(0, 0, mean_radius);
                v.rotateAroundY(-i*curve_rotation);
            }
            
            buildPrismSurface(circle1, circle2);
            circle1 = circle2;
        }
        buildPrismSurface(circle2, first_circle);
    }
}


class Prism extends Mesh {

    public Prism(int n, double height, double radius) {
        Vertex apex1 = new Vertex(0, 0.5*height, 0);
        Vertex apex2 = new Vertex(0, -0.5*height, 0);

        List<Vertex> circle1 = XZregularPolygon(n, radius);
        for (Vertex v : circle1) {
            v.translate(0, 0.5*height, 0);
        }

        List<Vertex> circle2 = XZregularPolygon(n, radius);
        for (Vertex v : circle2) {
            v.translate(0, -0.5*height, 0);
        }

        buildPyramidSurface(apex2, circle2);
        buildPrismSurface(circle1, circle2);

        Collections.reverse(circle1);
        buildPyramidSurface(apex1, circle1);
    }
}


class AntiPrism extends Mesh {
    
    public AntiPrism(int n, double height, double radius) {
        Vertex apex1 = new Vertex(0, -0.5*height, 0);
        Vertex apex2 = new Vertex(0, 0.5*height, 0);

        List<Vertex> circle1 = XZregularPolygon(n, radius);
        for (Vertex v : circle1) {
            v.translate(0, -0.5*height, 0);
            v.rotateAroundY(Math.PI/n);
        }

        List<Vertex> circle2 = XZregularPolygon(n, radius);
        for (Vertex v : circle2) {
            v.translate(0, 0.5*height, 0);
        }

        buildPyramidSurface(apex1, circle1);
        buildPrismSurface(circle2, circle1);

        Collections.reverse(circle2);
        buildPyramidSurface(apex2, circle2);
    }
}


class Bipyramid extends Mesh {

    public Bipyramid(int RGB, int n, double length, double baseRadius) {
        List<Vertex> circle = XZregularPolygon(n, baseRadius);
        buildPyramidSurface(new Vertex(0, -0.5*length, 0), circle);

        Collections.reverse(circle);
        buildPyramidSurface(new Vertex(0, 0.5*length, 0), circle);
    }
}


class Sphere extends Mesh {

    public Sphere(double radius, int num_meridians) {
        int num_parallels = num_meridians - 1;
        int num_corners = 2*num_meridians;

        double rotation_angle = 2*Math.PI/num_corners;

        List<List<Vertex>> meridians = new ArrayList<>();

        for (int i = 0; i < num_meridians; i++) {
            List<Vertex> meridian = XYregularPolygon(num_corners, radius);
            
            for (Vertex v : meridian) {
                v.rotateAroundY(i*rotation_angle);
            }
            meridians.add(meridian);
        }

        List<List<Vertex>> parallels = new ArrayList<>();

        for (int i = 0; i < num_parallels; i++) {
            parallels.add(new ArrayList<>());
        }

        for (List<Vertex> meridian : meridians) {
            List<Vertex> half_arc = meridian.subList(1, num_meridians);

            int i = 0;
            for (List<Vertex> parallel : parallels) {
                parallel.add(half_arc.get(i));
                i++;
            }
        }
        
        for (List<Vertex> meridian : meridians) {
            List<Vertex> half_arc = meridian.subList(num_meridians + 1, num_corners);
            Collections.reverse(half_arc);

            int i = 0;
            for (List<Vertex> parallel : parallels) {
                parallel.add(half_arc.get(i));
                i++;
            }
        }

        Vertex south_pole = new Vertex(0, radius, 0);
        Vertex north_pole = new Vertex(0, -radius, 0);

        List<Vertex> circle1 = parallels.get(0), circle2;
        Collections.reverse(circle1);
        buildPyramidSurface(south_pole, circle1);
        Collections.reverse(circle1);

        for (int i = 1; i < num_parallels; i++) {
            circle2 = parallels.get(i);
            buildPrismSurface(circle1, circle2);
            circle1 = circle2;
        }
        buildPyramidSurface(north_pole, circle1);
    }
}