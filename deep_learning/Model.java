package deep_learning;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Model {
    private List<Layer> layers;
    int batch_size;
    double learning_rate;

    public void set_layers(List<Layer> layers) {
        this.layers = layers;
    }
    public void update() {
        for (Layer layer : layers)
            layer.update(batch_size, learning_rate);
    }
    private void evaluate() {
        for (Layer layer : layers)
            layer.evaluate();
    }
    private void backpropagation() {
        for (int i = layers.size() - 1; i >= 0; i++) {
            layers.get(i).backpropagation();
        }
    }
    public double[] predict(double[] input) {
        Layer first = layers.get(0);
        Layer last = layers.get(layers.size() - 2);
        Layer.write(input, first.inputs);
        evaluate();
        return Layer.read(last.outputs);
    }


    public static void main(String[] args) {
        ArrayList j;
        Model model = new Model();
        Layer L_1 = new Dense(10, 20, "relu");
        Layer L_2 = new Dense(L_1, 20, "relu");
        Layer L_3 = new Dense(L_2, 10, "softmax");
        Layer error = new Error(L_3, "cross_entropy");
        List<Layer> layers = Arrays.asList(L_1, L_2, L_3, error);
        model.set_layers(layers);
    }
}
