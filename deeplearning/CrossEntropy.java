package deeplearning;


public class CrossEntropy extends Error {
    @Override
    public void forward(int firstSample, int lastSample) {
        if (label == null)
            return;

        int sample, feature, timeStep;
        int hash;

        error = 0;

        for (sample = firstSample; sample < lastSample; sample++) {
            for (timeStep = 0; timeStep < dim2; timeStep++) {
                for (feature = 0; feature < dim1; feature++) {

                    hash = indexHash(feature, sample, timeStep);
                    
                    if (label.value[hash] > 0) {
                        error += -Math.log(input.value[hash]);
                        break;
                    }
                }
            }
        }
        error /= lastSample - firstSample;
    }


    @Override
    public void backward(int firstSample, int lastSample) {
        int batchSize = lastSample - firstSample;
        int sample, feature, timestep;
        int hash;

        for (sample = firstSample; sample < lastSample; sample++) {
            for (timestep = 0; timestep < dim2; timestep++) {
                for (feature = 0; feature < dim1; feature++) {

                    hash = indexHash(sample, feature, timestep);
                    input.gradient[hash] += -label.value[hash] / (input.value[hash] * batchSize);
                }
            }
        }
    }
}
