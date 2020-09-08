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
        if (!(input.dim0 == label.dim0 && 
              input.dim1 == label.dim1 && 
              input.dim2 == label.dim2 && 
              input.dim3 == label.dim3)) {
            throw new IllegalArgumentException();
        }
    }
    public Matrix getPrediction() {
        return new Matrix(input);
    }
    public double getError() {
        return error;
    }
}