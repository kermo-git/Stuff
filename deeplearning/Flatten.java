package deeplearning;


public class Flatten extends Layer {
    @Override
    public void setInput(Matrix input) {
        this.input = input;
        init(input.dim0, input.dim1 * input.dim2 * input.dim3);
    }


    @Override
    public void forward(int firstSample, int lastSample) {
        int sample, d1, d2, d3;
        int inputHash;
        int feature;

        for (sample = firstSample; sample < lastSample; sample++) {
            feature = 0;

            for (d1 = 0; d1 < input.dim1; d1++) {
                for (d2 = 0; d2 < input.dim2; d2++) {
                    for (d3 = 0; d3 < input.dim3; d3++) {

                        inputHash = input.indexHash(sample, d1, d2, d3);
                        value[indexHash(sample, feature)] = input.value[inputHash];
                        feature++;
                    }
                }
            }
        }
    }


    @Override
    public void backward(int firstSample, int lastSample) {
        int sample, d1, d2, d3;
        int inputHash;
        int feature;

        for (sample = firstSample; sample < lastSample; sample++) {
            feature = 0;

            for (d1 = 0; d1 < input.dim1; d1++) {
                for (d2 = 0; d2 < input.dim2; d2++) {
                    for (d3 = 0; d3 < input.dim3; d3++) {

                        inputHash = input.indexHash(sample, d1, d2, d3);
                        input.gradient[inputHash] += gradient[indexHash(sample, feature)];
                        feature++;
                    }
                }
            }
        }
    }
}
