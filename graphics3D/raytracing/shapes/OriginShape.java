package graphics3D.raytracing.shapes;

import graphics3D.raytracing.RayIntersection;
import graphics3D.utils.Matrix;
import graphics3D.utils.Vector;

public abstract class OriginShape extends RayTracingObject {
    protected abstract RayIntersection getIntersectionAtOrigin(
        Vector transformedOrigin, 
        Vector transFormedDirection
    );

    public Matrix rotation, rotationInv;
    public Matrix fullTransformation, fullTransformationInv;
    
    @Override
    public void transform(Matrix rotation, Matrix translation) {
        this.rotation = rotation;
        fullTransformation = rotation.combine(translation);

        rotationInv = rotation.inverse();
        fullTransformationInv = fullTransformation.inverse();
    }

    @Override
    public void transform(Matrix translation) {
        fullTransformation = translation;
        fullTransformationInv = fullTransformation.inverse();
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
