package deeplearning;

public class Sigmoid extends Pointwize {
    @Override
    public double forwardOp(double x) {
        return 1/(1 + Math.exp(-x));
    }
    @Override
    public double backwardOp(double f) {
        return f*(1 - f);
    }
}
