package graphics3D.raytracing.shapes;

import graphics3D.raytracing.RayIntersection;
import graphics3D.utils.Matrix;
import graphics3D.utils.Vector;

public abstract class OriginRayTracingObject extends RayTracingObject {
    protected abstract RayIntersection getIntersectionAtOrigin(Vector o, Vector d);

    public Matrix rotation, rotationInv;
    public Matrix fullTransformation, fullTransformationInv;

    @Override
    public RayTracingObject rotate(double rotX, double rotY, double rotZ) {
        Matrix newRotation = 
            Matrix.rotateAroundX(rotX).combine(
            Matrix.rotateAroundY(rotY)).combine(
            Matrix.rotateAroundZ(rotZ));
        
        if (rotation != null) {
            rotation = rotation.combine(newRotation);
        } else {
            rotation = newRotation;
        }
        if (fullTransformation != null) {
            fullTransformation = fullTransformation.combine(newRotation);
        } else {
            fullTransformation = newRotation;
        }
        rotationInv = rotation.inverse();
        fullTransformationInv = fullTransformation.inverse();
        return this;
    }

    @Override
    public RayTracingObject translate(double x, double y, double z) {
        Matrix newTranslation = Matrix.translate(x, y, z);

        if (fullTransformation != null) {
            fullTransformation = fullTransformation.combine(newTranslation);
        } else {
            fullTransformation = newTranslation;
        }
        fullTransformationInv = fullTransformation.inverse();
        return this;
    }

    @Override
    public RayIntersection getIntersection(Vector rayOrigin, Vector rayDirection) {
        RayIntersection result = null;

        if (rotation != null && fullTransformation != null) {
            Vector transformedOrigin = fullTransformationInv.getTransformation(rayOrigin);
            Vector transFormedDirection = rotationInv.getTransformation(rayDirection);
    
            result = getIntersectionAtOrigin(transformedOrigin, transFormedDirection);
    
            if (result != null) {
                fullTransformation.transform(result.hitPoint);
                rotation.transform(result.normal);
            }
            return result;
        }
        else if (fullTransformation != null) {
            Vector transformedOrigin = fullTransformationInv.getTransformation(rayOrigin);
            result = getIntersectionAtOrigin(transformedOrigin, rayDirection);

            if (result != null)
                fullTransformation.transform(result.hitPoint);
            return result;
        }
        else if (rotation != null) {
            Vector transFormedDirection = rotationInv.getTransformation(rayDirection);
            result = getIntersectionAtOrigin(rayOrigin, transFormedDirection);
            
            if (result != null)
                rotation.transform(result.normal);
            return result;
        }
        return getIntersectionAtOrigin(rayOrigin, rayDirection);
    }
    
}
