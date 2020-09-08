package deeplearning;

public class LeakyRELU extends Pointwize {
    @Override
    public void setInput(Matrix input) {
        if (input instanceof Dense && input != this.input) {
            ((Dense) input).heInit();
        }
        super.setInput(input);
    }
    @Override
    public double forwardOp(double x) {
        return (x > 0)? x : 0.01*x;
    }
    @Override
    public double backwardOp(double f) {
        return (f > 0)? 1 : 0.01;
    }
}
