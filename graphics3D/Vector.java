package graphics3D;

public class Vector {
    double x = 0, y = 0, z = 0;

    public Vector() {}
    public Vector(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    public Vector(Vector startPoint, Vector endPoint) {
        this(
            endPoint.x - startPoint.x,
            endPoint.y - startPoint.y,
            endPoint.z - startPoint.z
        );
    }
    @Override
    public boolean equals(Object other) {
        if (other instanceof Vector) {
            Vector v = (Vector) other;
            return x == v.x && y == v.y && z == v.z;
        }
        return false;
    }


    // phi - [0 - 2pi], theta - [0 - pi]
    double radius = 0, phi = 0, theta = 0;

    private void updateSpericalCoordinates() {
        radius = Math.sqrt(x*x + y*y + z*z);
        theta = Math.acos(y / radius);
        phi = Math.atan(x / z);
    }
    private void updateCartesianCoordinates() {
        x = radius * Math.sin(theta) * Math.cos(phi);
        y = radius * Math.cos(phi);
        z = radius * Math.sin(theta) * Math.sin(phi);
    }
    private void rotate(double phiRadians, double thetaRadians) {
        double fullRotation = 2 * Math.PI;

        phi += phiRadians % fullRotation;
        if (phi < 0) {
            phi = fullRotation + phi;
        }
        else if (phi > fullRotation) {
            phi -= fullRotation;
        }
        theta += thetaRadians % Math.PI;
        if (theta < 0) {
            theta = -theta;
        } 
        else if (theta > Math.PI) {
            theta = 2 * Math.PI - theta;
        }
    }


    public void normalize() {
        double len = length();
        x /= len;
        y /= len;
        z /= len;
    }
    public double length() {
        return Math.sqrt(x * x + y * y + z * z);
    }


    public void add(Vector v) {
        x += v.x;
        y += v.y;
        z += v.z;
    }
    public Vector scale(double scalar) {
        return new Vector(
            x * scalar,
            y * scalar,
            z * scalar
        );
    }
    public double dot(Vector v) {
        return x*v.x + y*v.y + z*v.z;
    }
    public Vector cross(Vector v) {
        return new Vector(
            y * v.z - z * v.y, 
            z * v.x - x * v.z, 
            x * v.y - y * v.x
        );
    }


    public Vector getReflection(Vector normal) {
        double dot = this.dot(normal);
        Vector reflection = normal.scale(2 * dot);
        reflection.add(this.scale(-1));
        return reflection;
    }
    public Vector getRefraction(Vector normal, double refractionIndex) {
        return null;
    }
}