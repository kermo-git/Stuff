package graphics3D.raytracing.shapes;

import graphics3D.raytracing.RayHit;
import graphics3D.utils.Matrix;
import graphics3D.utils.Vector;

public abstract class OriginRTObject extends RTobject {
    protected abstract RayHit getIntersectionAtObjectSpace(Vector o, Vector d);

    private Matrix rotMat = new Matrix();
    private Matrix rotInv = new Matrix();
    private Matrix fullMat = new Matrix();
    private Matrix fullInv = new Matrix();

    @Override
    public RTobject rotate(double degX, double degY, double degZ) {
        Matrix newRotation = Matrix.rotateDeg(degX, degY, degZ);
        
        rotMat = rotMat.combine(newRotation);
        fullMat = fullMat.combine(newRotation);

        rotInv = rotMat.inverse();
        fullInv = fullMat.inverse();
        
        return this;
    }

    @Override
    public RTobject translate(double x, double y, double z) {
        fullMat = fullMat.combine(Matrix.translate(x, y, z));
        fullInv = fullMat.inverse();
        return this;
    }

    @Override
    public RayHit getIntersection(Vector rayOrigin, Vector rayDirection) {
        Vector _origin = fullInv.getTransformation(rayOrigin);
        Vector _direction = rotInv.getTransformation(rayDirection);

        RayHit result = getIntersectionAtObjectSpace(
            _origin, 
            _direction
        );
        if (result != null) {
            fullMat.transform(result.hitPoint);
            rotMat.transform(result.normal);
        }
        return result;
    }   
}
