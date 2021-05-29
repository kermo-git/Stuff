package graphics3D.raymarching.shapes;

import graphics3D.utils.Vector;

public class Cylinder extends OriginRMObject {
    private double radius;
    private double halfHeight;

    public Cylinder(double height, double radius) {
        this.radius = radius;
        halfHeight = 0.5 * height;
    }

    @Override
    protected double getSignedDistanceAtObjectSpace(Vector point) {
        double capDistance = abs(point.y) - halfHeight;
        double cylinderDistance = new Vector(point.x, 0, point.z).length() - radius;

        boolean inInfiniteCylinder = cylinderDistance < 0;
        boolean betweenCaps = capDistance < 0;
        
        if (inInfiniteCylinder && betweenCaps) {
            return capDistance > cylinderDistance ? capDistance : cylinderDistance;
        }
        else if (betweenCaps) {
            return cylinderDistance;
        }
        else if (inInfiniteCylinder) {
            return capDistance;
        }
        else {
            return  Math.sqrt(capDistance * capDistance + cylinderDistance * cylinderDistance);
        }
    }
}
