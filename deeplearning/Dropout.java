package deeplearning;

import java.util.Random;


public class Dropout extends Pointwize {
    protected final double keepProbability;
    protected boolean[] mask;


    public Dropout(double keepProbability) {
        if (keepProbability < 0 || keepProbability > 1)
            throw new IllegalArgumentException();
        this.keepProbability = keepProbability;
    }


    @Override
    public void setInput(Matrix input) {
        super.setInput(input);

        mask = new boolean[value.length];
        Random random = new Random();

        for (int i = 0; i < value.length; i++) {
            mask[i] = random.nextDouble() <= keepProbability;
        }
    }

    
    @Override
    protected double forwardOp(double x) {
        return mask[hash] ? x/keepProbability : 0;
    }
    @Override
    protected double backwardOp(double f) {
        return mask[hash] ? 1/keepProbability : 0;
    }
}
