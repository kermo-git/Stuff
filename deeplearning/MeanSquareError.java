package deeplearning;


public class MeanSquareError extends Error {
    @Override
    public void forward(int firstSample, int lastSample) {
        if (label == null)
            return;
            
        error = 0;
        int sample, timeStep, hash;

        for (sample = firstSample; sample < lastSample; sample++) {
            for (timeStep = 0; timeStep < input.dim2; timeStep++) {

                hash = input.indexHash(sample, 0, timeStep);
                double diff = input.value[hash] - label.value[hash];
                error += diff * diff;
            }
        }
        error /= lastSample - firstSample;
    }


    @Override
    public void backward(int firstSample, int lastSample) {
        int batchSize = lastSample - firstSample;
        int hash;

        for (int sample = firstSample; sample < lastSample; sample++) {
            for (int timestep = 0; timestep < input.dim2; timestep++) {

                hash = input.indexHash(sample, 0, timestep);
                input.gradient[hash] += 2*(input.value[hash] - label.value[hash])/batchSize;
            }
        }
    }
}
