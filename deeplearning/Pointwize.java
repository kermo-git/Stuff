package deeplearning;


public abstract class Pointwize extends Layer {
    @Override
    public void setInput(Matrix input) {
        this.input = input;
        init(input.dim0, input.dim1, input.dim2, input.dim3);
    }


    int hash;
    protected abstract double forwardOp(double x);
    protected abstract double backwardOp(double f);


    @Override
    public void forward(int firstSample, int lastSample) {
        int sample, d1, d2, d3;

        for (sample = firstSample; sample < lastSample; sample++) {
            for (d1 = 0; d1 < dim1; d1++) {
                for (d2 = 0; d2 < dim2; d2++) {
                    for (d3 = 0; d3 < dim3; d3++) {

                        hash = indexHash(sample, d1, d2, d3);
                        value[hash] = forwardOp(input.value[hash]);
                    }
                }
            }
        }
    }


    @Override
    public void backward(int firstSample, int lastSample) {
        int sample, d1, d2, d3;
        int hash;

        for (sample = firstSample; sample < lastSample; sample++) {
            for (d1 = 0; d1 < dim1; d1++) {
                for (d2 = 0; d2 < dim2; d2++) {
                    for (d3 = 0; d3 < dim3; d3++) {

                        hash = indexHash(sample, d1, d2, d3);
                        input.gradient[hash] += backwardOp(value[hash]) * gradient[hash];
                    }
                }
            }
        }
    }
}
