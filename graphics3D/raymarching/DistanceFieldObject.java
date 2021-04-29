package graphics3D.raymarching;

import graphics3D.utils.Matrix;
import graphics3D.utils.Vector;

public interface DistanceFieldObject {
    public void transform(Matrix rotation, Matrix translation);
    public void transform(Matrix translation);
    public double getSignedDistance(Vector point);
}
