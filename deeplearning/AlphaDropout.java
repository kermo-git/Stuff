package deeplearning;


public class AlphaDropout extends Dropout {
    private static final double
            ALPHA_PRIME = -1.7580993408473766;
    private final double a, b;


    public AlphaDropout(double keepProbability) {
        super(keepProbability);
        
        a = Math.sqrt(
            keepProbability + ALPHA_PRIME*ALPHA_PRIME*keepProbability*(1 - keepProbability)
        );
        b = -a*(1 - keepProbability)*ALPHA_PRIME;
    }


    @Override
    protected double forwardOp(double x) {
        return a * (mask[hash] ? x : ALPHA_PRIME) + b;
    }
    @Override
    protected double backwardOp(double f) {
        return mask[hash] ? a : 0;
    }
}
