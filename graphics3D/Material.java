package graphics3D;

public class Material {
    double 
        ambientRedFilter, 
        ambientGreenFilter, 
        ambientBlueFilter;
    double 
        diffuseRedFilter, 
        diffuseGreenFilter, 
        diffuseBlueFilter;
    double 
        specularRedFilter, 
        specularGreenFilter, 
        specularBlueFilter;
    
    double shininess;


    public Material(int ambientRGB, int diffuseRGB, int specularRGB, double shininess) {
        ambientRedFilter = ((ambientRGB >> 16) & 0xFF)/255.0;
        ambientGreenFilter = ((ambientRGB >> 8) & 0xFF)/255.0;
        ambientBlueFilter = (ambientRGB & 0xFF)/255.0;

        diffuseRedFilter = ((diffuseRGB >> 16) & 0xFF)/255.0;
        diffuseGreenFilter = ((diffuseRGB >> 8) & 0xFF)/255.0;
        diffuseBlueFilter = (diffuseRGB & 0xFF)/255.0;

        specularRedFilter = ((specularRGB >> 16) & 0xFF)/255.0;
        specularGreenFilter = ((specularRGB >> 8) & 0xFF)/255.0;
        specularBlueFilter = (specularRGB & 0xFF)/255.0;

        this.shininess = shininess;
    }
    private Material() {}


    // https://www.cs.brandeis.edu/~cs155/Lecture_16.pdf
    // https://en.wikipedia.org/wiki/Phong_reflection_model

    
    int illuminationRGB(Scene3D scene, Vector point, Vector normal) {
        double red = ambientRedFilter * scene.ambientRed;
        double green = ambientGreenFilter * scene.ambientGreen;
        double blue = ambientBlueFilter * scene.ambientBlue;

        for (LightSource light : scene.lights) {
            Vector lightVec = new Vector(point, light);
            lightVec.normalize();

            double diffuseIntensity =  lightVec.dot(normal);
            
            if (diffuseIntensity > 0) {
                red += diffuseIntensity * diffuseRedFilter * light.diffuseRed;
                green += diffuseIntensity * diffuseGreenFilter * light.diffuseGreen;
                blue += diffuseIntensity * diffuseBlueFilter * light.diffuseBlue;

                Vector viewVec = point.scale(-1);
                viewVec.normalize();

                Vector reflectedVec = normal.scale(2*diffuseIntensity);
                reflectedVec.add(lightVec.scale(-1));
                
                double specularIntensity = reflectedVec.dot(viewVec);

                if (specularIntensity > 0) {
                    specularIntensity = Math.pow(specularIntensity, shininess);

                    red += specularIntensity * specularRedFilter * light.specularRed;
                    green += specularIntensity * specularGreenFilter * light.specularGreen;
                    blue += specularIntensity * specularBlueFilter * light.specularBlue;
                }
            }
        }
        int R = (int) ((red <= 255)? red : 255);
        int G = (int) ((green <= 255)? green : 255);
        int B = (int) ((blue <= 255)? blue : 255);

        return
        ((R & 0xFF) << 16) |
        ((G & 0xFF) << 8)  |
         (B & 0xFF);
    }


    // http://devernay.free.fr/cours/opengl/materials.html


    public static Material EMERALD() {
        Material m = new Material();

        m.ambientRedFilter = 0.0215;
        m.ambientGreenFilter = 0.1745;
        m.ambientBlueFilter = 0.0215;

        m.diffuseRedFilter = 0.07568;
        m.diffuseGreenFilter = 0.61424;
        m.diffuseBlueFilter = 0.07568;

        m.specularRedFilter = 0.633;
        m.specularGreenFilter = 0.727811;
        m.specularBlueFilter = 0.633;

        m.shininess = 0.6*128;

        return m;
    }


    public static Material OBSIDIAN() {
        Material m = new Material();

        m.ambientRedFilter = 0.05375;
        m.ambientGreenFilter = 0.05;
        m.ambientBlueFilter = 0.06625;

        m.diffuseRedFilter = 0.18275;
        m.diffuseGreenFilter = 0.17;
        m.diffuseBlueFilter = 0.22525;

        m.specularRedFilter = 0.332741;
        m.specularGreenFilter = 0.328634;
        m.specularBlueFilter = 0.346435;

        m.shininess = 0.3*128;

        return m;
    }


    public static Material COPPER() {
        Material m = new Material();

        m.ambientRedFilter = 0.19125;
        m.ambientGreenFilter = 0.0735;
        m.ambientBlueFilter = 0.0225;

        m.diffuseRedFilter = 0.7038;
        m.diffuseGreenFilter = 0.27048;
        m.diffuseBlueFilter = 0.0828;

        m.specularRedFilter = 0.256777;
        m.specularGreenFilter = 0.137622;
        m.specularBlueFilter = 0.086014;

        m.shininess = 0.1*128;

        return m;
    }


    public static Material GOLD() {
        Material m = new Material();

        m.ambientRedFilter = 0.24725;
        m.ambientGreenFilter = 0.1995;
        m.ambientBlueFilter = 0.0745;

        m.diffuseRedFilter = 0.75164;
        m.diffuseGreenFilter = 0.60648;
        m.diffuseBlueFilter = 0.22648;

        m.specularRedFilter = 0.628281;
        m.specularGreenFilter = 0.555802;
        m.specularBlueFilter = 0.366065;

        m.shininess = 0.4*128;

        return m;
    }


    public static Material SILVER() {
        Material m = new Material();

        m.ambientRedFilter = 0.19225;
        m.ambientGreenFilter = 0.19225;
        m.ambientBlueFilter = 0.19225;

        m.diffuseRedFilter = 0.50754;
        m.diffuseGreenFilter = 0.50754;
        m.diffuseBlueFilter = 0.50754;

        m.specularRedFilter = 0.508273;
        m.specularGreenFilter = 0.508273;
        m.specularBlueFilter = 0.508273;

        m.shininess = 0.4*128;

        return m;
    }


    public static Material CYAN_PLASTIC() {
        Material m = new Material();

        m.ambientRedFilter = 0.0;
        m.ambientGreenFilter = 0.1;
        m.ambientBlueFilter = 0.06;

        m.diffuseRedFilter = 0.0;
        m.diffuseGreenFilter = 0.50980392;
        m.diffuseBlueFilter = 0.50980392;

        m.specularRedFilter = 0.50196078;
        m.specularGreenFilter = 0.50196078;
        m.specularBlueFilter = 0.50196078;

        m.shininess = 0.25*128;

        return m;
    }
}