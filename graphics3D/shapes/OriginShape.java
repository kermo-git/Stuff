package graphics3D.shapes;

import graphics3D.RayIntersection;
import graphics3D.Vector;
import graphics3D.Matrix;

public abstract class OriginShape extends Shape {
    protected abstract RayIntersection getTransformedIntersection(
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
    
            result = getTransformedIntersection(transformedOrigin, transFormedDirection);
    
            fullTransformation.transform(result.location);
            rotation.transform(result.normal);

            return result;
        }
        else if (fullTransformation != null) {
            Vector transformedOrigin = fullTransformationInv.getTransformation(rayOrigin);
            result = getTransformedIntersection(transformedOrigin, rayDirection);
            fullTransformation.transform(result.location);
            return result;
        }
        else if (rotation != null) {
            Vector transFormedDirection = rotationInv.getTransformation(rayDirection);
            result = getTransformedIntersection(rayOrigin, transFormedDirection);
            rotation.transform(result.normal);
            return result;
        }
        return getTransformedIntersection(rayOrigin, rayDirection);
    }
    
}
