package graphics3D;

public class Material {
    private ColorFilter 
        ambientFilter, 
        diffuseFilter, 
        specularFilter;
    private double shininess;

    public Material(ColorFilter ambientFilter, ColorFilter diffuseFilter, 
        ColorFilter specularFilter, double shininess) {

        this.ambientFilter = ambientFilter;
        this.diffuseFilter = diffuseFilter;
        this.specularFilter = specularFilter;
        this.shininess = shininess;
    }


    // https://www.cs.brandeis.edu/~cs155/Lecture_16.pdf
    // https://en.wikipedia.org/wiki/Phong_reflection_model

    
    Color illuminate(Scene3D scene, Vector point, Vector normal) {
        Color ambientColor = scene.ambientColor;

        double red = ambientFilter.red * ambientColor.red;
        double green = ambientFilter.green * ambientColor.green;
        double blue = ambientFilter.blue * ambientColor.blue;

        double diffuseIntensity, specularIntensity;
        Color diffuseColor, specularColor;

        for (LightSource light : scene.lights) {
            Vector lightVec = new Vector(point, light.location);
            lightVec.normalize();

            diffuseIntensity =  lightVec.dot(normal);
            
            if (diffuseIntensity > 0) {
                diffuseColor = light.diffuseColor;
                red += diffuseIntensity * diffuseFilter.red * diffuseColor.red;
                green += diffuseIntensity * diffuseFilter.green * diffuseColor.green;
                blue += diffuseIntensity * diffuseFilter.blue * diffuseColor.blue;

                Vector viewVec = point.scale(-1);
                viewVec.normalize();

                Vector reflectedVec = normal.scale(2*diffuseIntensity);
                reflectedVec.add(lightVec.scale(-1));
                
                specularIntensity = reflectedVec.dot(viewVec);

                if (specularIntensity > 0) {
                    specularIntensity = Math.pow(specularIntensity, shininess);
                    specularColor = light.specularColor;

                    red += specularIntensity * specularFilter.red * specularColor.red;
                    green += specularIntensity * specularFilter.green * specularColor.green;
                    blue += specularIntensity * specularFilter.blue * specularColor.blue;
                }
            }
        }
        return new Color(
            (int) ((red <= 255)? red : 255), 
            (int) ((green <= 255)? green : 255), 
            (int) ((blue <= 255)? blue : 255));
    }


    // http://devernay.free.fr/cours/opengl/materials.html


    public static Material EMERALD() {
        return new Material(
            new ColorFilter(0.0215, 0.1745, 0.0215),
            new ColorFilter(0.07568, 0.61424, 0.07568),
            new ColorFilter(0.633, 0.727811, 0.633),
            0.6*128
        );
    }


    public static Material OBSIDIAN() {
        return new Material(
            new ColorFilter(0.05375, 0.05, 0.06625),
            new ColorFilter(0.18275, 0.17, 0.22525),
            new ColorFilter(0.332741, 0.328634, 0.346435),
            0.3*128
        );
    }


    public static Material COPPER() {
        return new Material(
            new ColorFilter(0.19125, 0.0735, 0.0225),
            new ColorFilter(0.7038, 0.27048, 0.0828),
            new ColorFilter(0.256777, 0.137622, 0.086014),
            0.1*128
        );
    }


    public static Material GOLD() {
        return new Material(
            new ColorFilter(0.24725, 0.1995, 0.0745),
            new ColorFilter(0.75164, 0.60648, 0.22648),
            new ColorFilter(0.628281, 0.137622, 0.366065),
            0.4*128
        );
    }


    public static Material SILVER() {
        return new Material(
            new ColorFilter(0.19225, 0.19225, 0.19225),
            new ColorFilter(0.50754, 0.50754, 0.50754),
            new ColorFilter(0.508273, 0.508273, 0.508273),
            0.4*128
        );
    }


    public static Material CYAN_PLASTIC() {
        return new Material(
            new ColorFilter(0.0, 0.1, 0.06),
            new ColorFilter(0.0, 0.50980392, 0.50980392),
            new ColorFilter(0.50196078, 0.50196078, 0.50196078),
            0.25*128
        );
    }
}