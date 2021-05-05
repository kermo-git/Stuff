package graphics3D.raytracing.shapes;

import graphics3D.raytracing.RayIntersection;
import graphics3D.utils.Matrix;
import graphics3D.utils.Vector;

public abstract class OriginRayTracingObject extends RayTracingObject {
    protected abstract RayIntersection getIntersectionAtObjectSpace(Vector o, Vector d);

    private Matrix rotation = new Matrix();
    private Matrix rotationInv = new Matrix();
    private Matrix fullTransformation = new Matrix();
    private Matrix fullTransformationInv = new Matrix();

    @Override
    public RayTracingObject rotate(double rotX, double rotY, double rotZ) {
        Matrix newRotation = 
            Matrix.rotateAroundX(rotX).combine(
            Matrix.rotateAroundY(rotY)).combine(
            Matrix.rotateAroundZ(rotZ));
        
        rotation = rotation.combine(newRotation);
        fullTransformation = fullTransformation.combine(newRotation);

        rotationInv = rotation.inverse();
        fullTransformationInv = fullTransformation.inverse();
        
        return this;
    }

    @Override
    public RayTracingObject translate(double x, double y, double z) {
        fullTransformation = fullTransformation.combine(Matrix.translate(x, y, z));
        fullTransformationInv = fullTransformation.inverse();
        return this;
    }

    @Override
    public RayIntersection getIntersection(Vector rayOrigin, Vector rayDirection) {
        Vector transformedOrigin = fullTransformationInv.getTransformation(rayOrigin);
        Vector transFormedDirection = rotationInv.getTransformation(rayDirection);

        RayIntersection result = getIntersectionAtObjectSpace(
            transformedOrigin, 
            transFormedDirection
        );
        if (result != null) {
            fullTransformation.transform(result.hitPoint);
            rotation.transform(result.normal);
        }
        return result;
    }   
}
