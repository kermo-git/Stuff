package deeplearning;

import java.util.List;
import java.util.LinkedList;
import java.util.ListIterator;


public class Model {
    private final List<Layer> layers = new LinkedList<>();
    private Error error;


    public Model() {}
    public Model(Layer ...layers) {
        addLayers(layers);
    }


    public void addLayers(Layer ...layers) {
        for (Layer layer : layers) {
            this.layers.add(layer);
        }
    }
    public void setError(Error error) {
        this.error = error;
    }


    public void train(Matrix input, Matrix label, int batchSize, int epochs, double learningRate) {
        setInput(input, label);

        int numSamples = input.dim0;
        int firstSample, lastSample;

        for (int i = 1; i <= epochs; i++) {
            System.out.println("Epoch: " + i);
            firstSample = 0;

            while (firstSample < numSamples) {
                lastSample = Math.min(firstSample + batchSize, numSamples);

                forward(firstSample, lastSample);
                backward(firstSample, lastSample, learningRate);

                firstSample = lastSample;
            }
            forward();
            System.out.println("Error: " + error.getError() + '\n');
        }
        // TODO: remove dropouts
    }


    public void train(Matrix input, Matrix label, int epochs, double learningRate) {
        setInput(input, label);
        for (int i = 1; i <= epochs; i++) {
            System.out.println("Epoch: " + i);
            forward();
            System.out.println("Error: " + error.getError());
            backward(learningRate);
        }
    }


    public double test(Matrix input, Matrix label) {
        setInput(input, label);
        forward();
        return error.getError();
    }


    public Matrix predict(Matrix input) {
        setInput(input);
        forward();
        return error.getPrediction();
    }


    private void setInput(Matrix input, Matrix label) {
        if (label != null && input.dim0 != label.dim0)
            throw new IllegalArgumentException();

        Matrix previous = input;

        for (Layer layer : layers) {
            layer.setInput(previous);
            previous = layer;
        }
        error.setInput(previous);

        if (label != null) {
            error.setLabel(label);
        }
    }

    
    private void setInput(Matrix input) {
        setInput(input, null);
    }


    private void forward(int firstSample, int lastSample) {
        for (Layer layer : layers)
            layer.forward(firstSample, lastSample);
        error.forward(firstSample, lastSample);
    }


    private void forward() {
        for (Layer layer : layers)
            layer.forward();
        error.forward();
    }


    private void backward(int firstSample, int lastSample, double learningRate) {
        error.backward(firstSample, lastSample);

        ListIterator<Layer> it = layers.listIterator(layers.size());

        while (it.hasPrevious()) {
            Layer layer = it.previous();
            layer.backward(firstSample, lastSample);
            layer.update(learningRate);
        }
    }


    private void backward(double learningRate) {
        error.backward();

        ListIterator<Layer> it = layers.listIterator(layers.size());

        while (it.hasPrevious()) {
            Layer layer = it.previous();
            layer.backward();
            layer.update(learningRate);
        }
    }
}