package graphics3D.raymarching.shapes;

import graphics3D.raymarching.Material;
import graphics3D.utils.Vector;

public class Torus extends OriginRayMarchingObject {
    private double mainRadius, tubeRadius;

    public Torus(Material material, double mainRadius, double tubeRadius) {
        this.material = material;
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
