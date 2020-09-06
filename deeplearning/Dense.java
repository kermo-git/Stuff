package deeplearning;


public class Dense extends Layer {
    private Matrix weights;
    private Matrix biases;

    private final int outputFeatures;
    private int inputFeatures;


    public Dense(int outputFeatures) {
        this.outputFeatures = outputFeatures;
    }


    @Override
    public void setInput(Matrix input) {
        this.input = input;
        int samples = input.dim0;
        inputFeatures = input.dim1;

        if (weights == null) {
            weights = new Matrix(inputFeatures, outputFeatures);
            biases = new Matrix(outputFeatures);

            xavierInit();

            parameters.add(weights);
            parameters.add(biases);
        }
        else if (inputFeatures != weights.dim0)
            throw new IllegalArgumentException();

        init(samples, outputFeatures);
    }


    // https://stats.stackexchange.com/questions/373136/softmax-weights-initialization
    public void xavierInit() { // Default
        weights.gaussianFill(0, Math.sqrt(2.0/(inputFeatures + outputFeatures)));
    }
    public void heInit() { // RELU and its variants
        weights.gaussianFill(0, Math.sqrt(2.0/inputFeatures));
    }
    public void leCunInit() { // SELU
        weights.gaussianFill(0, Math.sqrt(1.0/inputFeatures));
    }


    @Override
    public void forward(int firstSample, int lastSample) {
        int inputHash, weightHash;
        int sample, out, in;
        double dotProduct;

        for (sample = firstSample; sample < lastSample; sample++) {
            for (out = 0; out < outputFeatures; out++) {

                dotProduct = 0;

                for (in = 0; in < inputFeatures; in++) {
                    inputHash = input.indexHash(sample, in);
                    weightHash = weights.indexHash(in, out);

                    dotProduct += input.value[inputHash] * weights.value[weightHash];
                }
                value[indexHash(sample, out)] = dotProduct + biases.value[out];
            }
        }
    }


    @Override
    public void backward(int firstSample, int lastSample) {
        int inputHash, weightHash;
        int sample, out, in;
        double grad;

        for (sample = firstSample; sample < lastSample; sample++) {
            for (out = 0; out < outputFeatures; out++) {

                grad = gradient[indexHash(sample, out)];
                biases.gradient[out] += grad;

                for (in = 0; in < inputFeatures; in++) {
                    inputHash = input.indexHash(sample, in);
                    weightHash = weights.indexHash(in, out);

                    input.gradient[inputHash] += weights.value[weightHash] * grad;
                    weights.gradient[weightHash] += input.value[inputHash] * grad;
                }
            }
        }
    }
}