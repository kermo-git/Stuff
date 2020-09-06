package deeplearning;


public class LSTM extends Layer {
    private final boolean returnSequence;
    private final int outputFeatures;


    public LSTM(int outputFeatures, boolean returnSequence) {
        this.outputFeatures = outputFeatures;
        this.returnSequence = returnSequence;
    }


    private Matrix cellState;
    private Matrix outputState = this;


    private final Gate 
        forgetGate = new SigmoidGate(), 
        inputGate = new SigmoidGate(),
        tanhGate = new TanhGate(), 
        outputGate = new SigmoidGate();


    private int inputFeatures, samples, timeSteps;
    private int firstSample, lastSample;
    private int timeStep;


    @Override
    public void setInput(Matrix input) {
        this.input = input;

        samples = input.dim0;
        inputFeatures = input.dim1;
        timeSteps = input.dim2;

        cellState = new Matrix(samples, outputFeatures, timeSteps);

        if (returnSequence)
            outputState.init(samples, outputFeatures, timeSteps);
        else
            outputState.init(samples, outputFeatures);

        forgetGate.init(); forgetGate.biases.fill(1);
        inputGate.init();
        tanhGate.init();
        outputGate.init();
    }


    @Override
    public void forward(int firstSample, int lastSample) {
        this.firstSample = firstSample;
        this.lastSample = lastSample;

        for (timeStep = 0; timeStep < timeSteps; timeStep++) {
            forgetGate.forward();
            inputGate.forward();
            tanhGate.forward();
            outputGate.forward();

            cellStateForward();
            outputStateForward();
        }
    }


    @Override
    public void backward(int firstSample, int lastSample) {
        this.firstSample = firstSample;
        this.lastSample = lastSample;

        for (timeStep = timeSteps - 1; timeStep >= 0; timeStep--) {
            outputStateBackward();
            cellStateBackward();

            if (!returnSequence && timeStep > 0) {
                timeStep--;
                outputStateForward();
                timeStep++;
            }

            forgetGate.backward();
            inputGate.backward();
            tanhGate.backward();
            outputGate.backward();
        }
    }


    @Override
    public void update(double learningRate) {
        super.update(learningRate);
        forgetGate.reset();
        inputGate.reset();
        tanhGate.reset();
        outputGate.reset();
    }


    private abstract class Gate extends Matrix {
        private Matrix weights;
        private Matrix biases;


        void init() {
            if (weights == null) {
                weights = new Matrix(inputFeatures + outputFeatures, outputFeatures);
                biases = new Matrix(outputFeatures);

                weights.gaussianFill(0, Math.sqrt(2.0 / (inputFeatures + outputFeatures))); // xavier

                LSTM.this.parameters.add(weights);
                LSTM.this.parameters.add(biases);
            } 
            else if (inputFeatures != weights.dim0 - outputFeatures)
                throw new IllegalArgumentException();

            init(samples, outputFeatures, timeSteps);
        }


        protected abstract double activate(double x);
        protected abstract double activationGrad(double f);


        void forward() {
            int inputHash, weightHash, outputHash;
            int sample, in, _out, out;

            int prevTimeStep = (returnSequence) ? timeStep - 1 : 0;
            boolean notFirstTimeStep = timeStep != 0;

            double dotProduct;

            for (sample = firstSample; sample < lastSample; sample++) {
                for (out = 0; out < outputFeatures; out++) {
                    dotProduct = 0;

                    for (in = 0; in < inputFeatures; in++) {
                        inputHash = input.indexHash(sample, in, timeStep);
                        weightHash = weights.indexHash(in, out);

                        dotProduct += input.value[inputHash] * weights.value[weightHash];
                    }
                    if (notFirstTimeStep) {
                        for (_out = 0; _out < outputFeatures; _out++) {
                            outputHash = outputState.indexHash(sample, _out, prevTimeStep);
                            weightHash = weights.indexHash(inputFeatures + _out, out);

                            dotProduct += outputState.value[outputHash] * weights.value[weightHash];
                        }
                    }
                    value[indexHash(sample, out, timeStep)] = activate(dotProduct + biases.value[out]);
                }
            }
        }


        void backward() {
            int inputHash, weightHash, outputHash, localHash;
            int sample, in, _out, out;

            int prevTimeStep = (returnSequence) ? timeStep - 1 : 0;
            boolean notFirstTimeStep = timeStep != 0;

            double grad;

            if (!returnSequence)
                outputState.resetGradient();

            for (sample = firstSample; sample < lastSample; sample++) {
                for (out = 0; out < outputFeatures; out++) {

                    localHash = indexHash(sample, out, timeStep);

                    grad = activationGrad(value[localHash]) * gradient[localHash];
                    biases.gradient[out] += grad;

                    for (in = 0; in < inputFeatures; in++) {
                        inputHash = input.indexHash(sample, in, timeStep);
                        weightHash = weights.indexHash(in, out);

                        input.gradient[inputHash] += weights.value[weightHash] * grad;
                        weights.gradient[weightHash] += input.value[inputHash] * grad;
                    }
                    if (notFirstTimeStep) {
                        for (_out = 0; _out < outputFeatures; _out++) {
                            outputHash = outputState.indexHash(sample, _out, prevTimeStep);
                            weightHash = weights.indexHash(inputFeatures + _out, out);

                            outputState.gradient[outputHash] += weights.value[weightHash] * grad;
                            weights.gradient[weightHash] += outputState.value[outputHash] * grad;
                        }
                    }
                }
            }
        }
    }


    private class SigmoidGate extends Gate {
        @Override
        protected double activate(double x) {
            return 1/(1 + Math.exp(-x));
        }
        @Override
        protected double activationGrad(double f) {
            return f*(1 - f);
        }
    }


    private class TanhGate extends Gate {
        @Override
        protected double activate(double x) {
            double exp = Math.exp(2*x);
            return (exp - 1)/(exp + 1);
        }
        @Override
        protected double activationGrad(double f) {
            return 1 - f*f;
        }
    }


    private void cellStateForward() {
        int prevHash, currentHash;
        int sample, out;

        boolean notFirstTimeStep = timeStep != 0;

        for (sample = firstSample; sample < lastSample; sample++) {
            for (out = 0; out < outputFeatures; out++) {

                currentHash = cellState.indexHash(sample, out, timeStep);

                cellState.value[currentHash] =
                    inputGate.value[currentHash] * tanhGate.value[currentHash];

                if (notFirstTimeStep) {
                    prevHash = cellState.indexHash(sample, out, timeStep - 1);

                    cellState.value[currentHash] +=
                        cellState.value[prevHash] * forgetGate.value[currentHash];
                }
            }
        }
    }


    private void cellStateBackward() {
        int prevHash, currentHash;
        int sample, out;
        double grad;

        boolean notFirstTimeStep = timeStep != 0;

        for (sample = firstSample; sample < lastSample; sample++) {
            for (out = 0; out < outputFeatures; out++) {

                currentHash = cellState.indexHash(sample, out, timeStep);
                grad = cellState.gradient[currentHash];

                if (notFirstTimeStep) {
                    prevHash = cellState.indexHash(sample, out, timeStep - 1);

                    forgetGate.gradient[currentHash] += cellState.value[prevHash] * grad;
                    cellState.gradient[prevHash] += forgetGate.value[currentHash] * grad;
                }

                inputGate.gradient[currentHash] += tanhGate.value[currentHash] * grad;
                tanhGate.gradient[currentHash] += inputGate.value[currentHash] * grad;
            }
        }
    }


    private void outputStateForward() {
        int cellHash, outputHash;
        int sample, out;

        for (sample = firstSample; sample < lastSample; sample++) {
            for (out = 0; out < outputFeatures; out++) {

                cellHash = cellState.indexHash(sample, out, timeStep);
                outputHash = (returnSequence)? cellHash : outputState.indexHash(sample, out);

                outputState.value[outputHash] =
                    outputGate.value[cellHash] * tanhGate.activate(cellState.value[cellHash]);
            }
        }
    }


    private void outputStateBackward() {
        int cellHash, outputHash;
        int sample, out;
        double tanh, grad;

        for (sample = firstSample; sample < lastSample; sample++) {
            for (out = 0; out < outputFeatures; out++) {

                cellHash = cellState.indexHash(sample, out, timeStep);
                outputHash = (returnSequence)? cellHash : outputState.indexHash(sample, out);

                grad = outputState.gradient[outputHash];
                tanh = tanhGate.activate(cellState.value[cellHash]);

                outputGate.gradient[cellHash] += tanh * grad;

                cellState.gradient[cellHash] +=
                    tanhGate.activationGrad(tanh) * outputGate.value[cellHash] * grad;
            }
        }
    }
}
