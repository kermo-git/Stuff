package graphics3D;

public class Vector {
    double x, y, z;


    public Vector(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    public Vector(Vector startPoint, Vector endPoint) {
        x = endPoint.x - startPoint.x;
        y = endPoint.y - startPoint.y;
        z = endPoint.z - startPoint.z;
    }


    public void normalize() {
        double len = length();
        x /= len;
        y /= len;
        z /= len;
    }
    public double length() {
        return Math.sqrt(x*x + y*y + z*z);
    }


    public void add(Vector v) {
        x += v.x;
        y += v.y;
        z += v.z;
    }
    public Vector scale(double scalar) {
        return new Vector(
            x*scalar,
            y*scalar,
            z*scalar
        );
    }
    public double dot(Vector v) {
        return x*v.x + y*v.y + z*v.z;
    }
    public Vector cross(Vector v) {
        return new Vector(
            y * v.z - z * v.y, 
            z * v.x - x * v.z, 
            x * v.y - y * v.x);
    }


    public void translate(double x, double y, double z) {
        this.x += x;
        this.y += y;
        this.z += z;
    }
    public void rotateAroundX(double sin, double cos) {
        double temp = cos*y - sin*z;
        z = sin*y + cos*z;
        y = temp;
    }
    public void rotateAroundY(double sin, double cos) {
        double temp = cos*x + sin*z;
        z = - sin*x + cos*z;
        x = temp;
    }
    public void rotateAroundZ(double sin, double cos) {
        double temp = cos*x - sin*y;
        y = sin*x + cos*y;
        x = temp;
    }


    public void rotateAroundX(double radians) {
        rotateAroundX(Math.sin(radians), Math.cos(radians));
    }
    public void rotateAroundY(double radians) {
        rotateAroundY(Math.sin(radians), Math.cos(radians));
    } 
    public void rotateAroundZ(double radians) {
        rotateAroundZ(Math.sin(radians), Math.cos(radians));
    } 


    public String toString() {
        return "<" + x + ", " + y + ", " + z + ">";
    }
}