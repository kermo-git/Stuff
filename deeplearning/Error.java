package deeplearning;

public abstract class Error extends Layer {
    protected Matrix label;
    protected double error;

    @Override
    public void setInput(Matrix input) {
        this.input = input;
    }
    public void setLabel(Matrix label) {
        this.label = label;
    }
    public Matrix getPrediction() {
        return new Matrix(input);
    }
    public double getError() {
        return error;
    }
}