package deeplearning;


// https://eli.thegreenplace.net/2016/the-softmax-function-and-its-derivative/
// https://deepnotes.io/softmax-crossentropy
public class Softmax extends Layer {
    @Override
    public void setInput(Matrix input) {
        this.input = input;
        init(input.dim0, input.dim1, input.dim2, input.dim3);
    }


    @Override
    public void forward(int firstSample, int lastSample) {
        int sample, feature, timeStep;
        int hash;
        double exp, sum, max;

        for (sample = firstSample; sample < lastSample; sample++) {
            for (timeStep = 0; timeStep < dim2; timeStep++) {

                sum = 0; max = 0;

                for (feature = 0; feature < dim1; feature++) {
                    hash = indexHash(sample, feature, timeStep);

                    if (input.value[hash] > max)
                        max = input.value[hash];
                }

                for (feature = 0; feature < dim1; feature++) {
                    hash = indexHash(sample, feature, timeStep);

                    exp = Math.exp(input.value[hash] - max);
                    sum += exp;
                    this.value[hash] = exp;
                }

                for (feature = 0; feature < dim1; feature++) {
                    hash = indexHash(sample, feature, timeStep);

                    this.value[hash] /= sum;
                }
            }
        }
    }


    @Override
    public void backward(int firstSample, int lastSample) {
        int sample, in, out, timeStep;
        int inHash, outHash;
        double inValue, outValue;

        for (sample = firstSample; sample < lastSample; sample++) {
            for (timeStep = 0; timeStep < dim2; timeStep++) {

                for (in = 0; in < dim1; in++) {
                    inHash = indexHash(sample, in, timeStep);
                    inValue = value[inHash];

                    for (out = 0; out < dim1; out++) {
                        outHash = indexHash(sample, out, timeStep);
                        outValue = value[outHash];

                        if (in == out) {
                            input.gradient[inHash] += outValue*(1 - inValue)*gradient[outHash];
                        }
                        else {
                            input.gradient[inHash] += -outValue*inValue*gradient[outHash];
                        }
                    }
                }
            }
        }
    }
}
