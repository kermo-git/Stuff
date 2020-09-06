package deeplearning;

import java.util.LinkedList;
import java.util.List;

/**
 * Methods {@code forward()} and {@code forward(int, int)} perform some
 * computations with the {@code input.output} and store the results to
 * {@code this.output}. Methods {@code backward()} and
 * {@code backward(int, int)} calculate gradients of the cost function w.r.t
 * {@code input} and store them to {@code input.gradient} (it assumes that
 * gradients w.r.t {@code this} are already available in {@code this.gradient}).
 */
public abstract class Layer extends Matrix {
    protected Matrix input;
    protected List<Matrix> parameters = new LinkedList<>();

    @Override
    public void update(double learningRate) {
        for (Matrix matrix : parameters) {
            matrix.update(learningRate);
        }
        reset();
    }

    abstract public void setInput(Matrix input);

    abstract public void forward(int firstSample, int lastSample);
    public void forward() { forward(0, input.dim1); }

    abstract public void backward(int firstSample, int lastSample);
    public void backward() { backward(0, input.dim1); }
}