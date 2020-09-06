package deeplearning;

public class SELU extends Pointwize {
    private static final double
        LAMBDA = 1.0507009873554804934193349852946,
        ALPHA = 1.6732632423543772848170429916717;
        
    @Override
    public double forwardOp(double x) {
        if (x > 0)
            return LAMBDA*x;
        else
            return LAMBDA*ALPHA*(Math.exp(x) - 1);
    }
    @Override
    public double backwardOp(double f) {
        if (f > 0)
            return LAMBDA;
        else
            return f + LAMBDA*ALPHA;
    }
}
