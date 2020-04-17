package deeplearning;


abstract class Operation {
    protected Operation left = null;
    protected Operation right = null;

    protected int rows, cols;
    protected double[] output;
    protected double[] gradient;

    protected Operation(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;

        output = new double[rows*cols];
        gradient = new double[rows*cols];
    }

    protected Operation(Operation input) {
        this(input.rows, input.cols);
        left = input;
    }

    protected Operation(Operation left, Operation right) {
        this(left.rows, left.cols);

        if (left.cols != right.cols || left.rows != right.rows)
            throw new IllegalArgumentException();

        this.left = left;
        this.right = right;
    }

    public abstract void forward();
    public abstract void backward();
}


class Add extends Operation {
    public Add(Operation left, Operation right) {
        super(left, right);
    }

    @Override
    public void forward() {
        double[] left_out = left.output;
        double[] right_out = right.output;

        for (int i = 0; i < output.length; i++) {
            output[i] = left_out[i] + right_out[i];
        }
    }

    @Override
    public void backward() {
        double[] left_grad = left.gradient;
        double[] right_grad = right.gradient;

        for (int i = 0; i < output.length; i++) {
            left_grad[i] = gradient[i];
            right_grad[i] = gradient[i];
        }
    }
}


class ReLU extends Operation {
    public ReLU(Operation input) {
        super(input);
    }

    @Override
    public void forward() {
        double[] input = left.output;

        for (int i = 0; i < output.length; i++) {
            output[i] = (input[i] > 0)? input[i] : 0;
        }
    }

    @Override
    public void backward() {
        double[] grad = left.gradient;

        for (int i = 0; i < output.length; i++) {
            grad[i] = gradient[i]*((output[i] > 0)? 1 : 0);
        }
    }
}


class Sigmoid extends Operation {
    public Sigmoid(Operation input) {
        super(input);
    }

    @Override
    public void forward() {
        double[] input = left.output;

        for (int i = 0; i < output.length; i++) {
            output[i] = 1/(1 + Math.exp(-input[i]));
        }
    }

    @Override
    public void backward() {
        double[] grad = left.gradient;

        for (int i = 0; i < output.length; i++) {
            grad[i] = gradient[i]*(output[i]*(1 - output[i]));
        }
    }
}


class Tanh extends Operation {
    public Tanh(Operation input) {
        super(input);
    }

    @Override
    public void forward() {
        double[] input = left.output;

        for (int i = 0; i < output.length; i++) {
            double pos_exp = Math.exp(input[i]);
            double neg_exp = Math.exp(-input[i]);

            output[i] = (pos_exp - neg_exp)/(pos_exp + neg_exp);
        }
    }

    @Override
    public void backward() {
        double[] grad = left.gradient;

        for (int i = 0; i < output.length; i++) {
            grad[i] = gradient[i]*(1 - output[i]*output[i]);
        }
    }
}


class DotProduct extends Operation {
    public DotProduct(Operation left, Operation right) {
        super(left.rows, right.cols);

        if (left.cols != right.rows)
            throw new IllegalArgumentException();

        this.left = left;
        this.right = right;
    }

    @Override
    public void forward() {
        double[] left_out = left.output;
        double[] right_out = right.output;
        int index = 0;

        for (int row_start = 0; row_start < left_out.length; row_start += left.cols) {
            for (int col_start = 0; col_start < cols; col_start++) {

                int i = row_start;
                int j = col_start;

                while (j < right_out.length) {
                    output[index] += left_out[i]*right_out[j];
                    i++; j += cols;
                }
                index++;
            }
        }
    }

    @Override
    public void backward() {
        // TODO
    }
}


class SoftmaxCrossEntropy extends Operation {
    double error = 0;

    public SoftmaxCrossEntropy(Operation input, Operation label) {
        super(input, label);
    }

    @Override
    public void forward() {
        double[] input = left.output;
        double max_input = input[0];

        for (int i = 0; i < output.length; i++) {
            max_input = (input[i] > max_input)? input[i] : max_input;
        }

        double exp_sum = 0;

        for (int i = 0; i < output.length; i++) {
            exp_sum += (output[i] = Math.exp(input[i] - max_input));
        }

        double[] label = right.output;
        error = 0;

        for (int i = 0; i < output.length; i++) {
            output[i] /= exp_sum;
            if (label[i] != 0)
                error -= Math.log(output[i]);
        }
    }

    @Override
    public void backward() {
        double[] grad = left.gradient;
        double[] label = right.output;

        for (int i = 0; i < output.length; i++) {
            grad[i] = output[i] - label[i];
        }
    }
}
