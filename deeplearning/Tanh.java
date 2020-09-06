package deeplearning;

public class Tanh extends Pointwize {
    @Override
    public double forwardOp(double x) {
        double exp = Math.exp(2*x);
        return (exp - 1)/(exp + 1);
    }
    @Override
    public double backwardOp(double f) {
        return 1 - f*f;
    }
}
