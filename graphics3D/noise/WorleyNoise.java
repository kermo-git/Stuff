package graphics3D.noise;

import java.util.Random;

public class WorleyNoise extends Noise {
    private int numPoints;
    private int cubeSide;
    private double[][] points;
    private double maxDistance;

    public WorleyNoise(int numPoints, int cubeSide) {
        this.numPoints = numPoints;
        this.cubeSide = cubeSide;
        points = new double[numPoints][3];

        Random random = new Random();
        for (int i = 0; i < numPoints; i++) {
            points[i][0] = random.nextDouble() * cubeSide;
            points[i][1] = random.nextDouble() * cubeSide;
            points[i][2] = random.nextDouble() * cubeSide;
        }

        maxDistance = 0;
        
        for (int i = 0; i < numPoints; i++) {
            for (int j = i + 1; j < numPoints; j++) {
                double[] p1 = points[i];
                double[] p2 = points[j];

                double vx = p2[0] - p1[0];
                double vy = p2[1] - p1[1];
                double vz = p2[2] - p1[2];

                double distance = Math.sqrt(vx * vx + vy * vy + vz * vz);
                if (distance > maxDistance) {
                    maxDistance = distance;
                }
            }
        }
        maxDistance /= 2;
    }

    public WorleyNoise() {
        this(32, 64);
    }

    private double localCoordinate(double globalCoordinate) {
        if (globalCoordinate < 0) {
            return globalCoordinate % cubeSide + cubeSide;
        }
        return globalCoordinate % cubeSide;
    }

    private double min(double a, double b) {
        return (a < b) ? a : b;
    }
    
    @Override
    public double noise(double x, double y, double z) {
        double px = localCoordinate(x);
        double py = localCoordinate(y);
        double pz = localCoordinate(z);

        double minDistance = Double.MAX_VALUE;

        for (int i = 0; i < numPoints; i++) {
            double[] p = points[i];

            double vx = px - p[0];
            double vy = py - p[1];
            double vz = pz - p[2];

            double distance = Math.sqrt(vx * vx + vy * vy + vz * vz);
            if (distance < minDistance) {
                minDistance = distance;
            }
        }
        return minDistance / maxDistance;
    }

    @Override
    public double signedNoise(double x, double y, double z) {
        return 2 * (noise(x, y, z) - 0.5);
    }
}
