package deeplearning;

public class LeakyRELU extends Pointwize {
    @Override
    public double forwardOp(double x) {
        return (x > 0)? x : 0.01*x;
    }
    @Override
    public double backwardOp(double f) {
        return (f > 0)? 1 : 0.01;
    }
}
