package graphics3D;

public class SimpleFlatTriangle extends Triangle {
    public SimpleFlatTriangle(Material material, Vertex v1, Vertex v2, Vertex v3) {
        super(material, v1, v2, v3);
    }
    @Override
    protected Color interpolate(Scene3D scene) {
        return v1.color;
    }
}
