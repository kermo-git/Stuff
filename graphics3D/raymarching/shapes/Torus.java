package graphics3D.raymarching.shapes;

import graphics3D.utils.Vector;

public class Torus extends OriginRMObject {
    private double mainRadius, tubeRadius;

    public Torus(double mainRadius, double tubeRadius) {
        this.mainRadius = mainRadius;
        this.tubeRadius = tubeRadius;
    }

    @Override
    protected double getSignedDistanceAtObjectSpace(Vector point) {
        Vector direction = new Vector(point.x, 0, point.z);
        double x = direction.length() - mainRadius;
        return new Vector(x, point.y, 0).length() - tubeRadius;
    }
}
