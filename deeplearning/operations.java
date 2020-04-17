package deeplearning;
import java.util.Random;


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


    protected void setGradient(int index, double grad) {
        gradient[index] = grad;
    }
    public void setOutput(double[] output) {
        this.output = output;
    }
}


class Matrix extends Operation {
    public Matrix(int rows, int cols) {
        super(rows, cols);
    }


    private void init(double std_dev) {
        Random random = new Random();

        for (int i = 0; i < output.length; i++) {
            output[i] = std_dev*random.nextGaussian();
        }
    }
    // Default initialisation
    public void xavier_init() { init(Math.sqrt(1.0/cols)); }
    // Use with ReLU
    public void he_init() { init(Math.sqrt(2.0/cols)); }


    @Override
    public void forward() {}
    @Override
    public void backward() {}
    @Override
    protected void setGradient(int index, double grad) {
        gradient[index] += grad;
    }
    public void update(double learning_rate, double batch_size) {
        for (int i = 0; i < output.length; i++) {
            output[i] -= (gradient[i]/batch_size)*learning_rate;
            gradient[i] = 0;
        }
    }
}


class Addition extends Operation {
    public Addition(Operation left, Operation right) {
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
        for (int i = 0; i < output.length; i++) {
            left.setGradient(i, gradient[i]);
            right.setGradient(i, gradient[i]);
        }
    }
}


class Subtraction extends Operation {
    public Subtraction(Operation left, Operation right) {
        super(left, right);
    }


    @Override
    public void forward() {
        double[] left_out = left.output;
        double[] right_out = right.output;

        for (int i = 0; i < output.length; i++) {
            output[i] = left_out[i] - right_out[i];
        }
    }
    @Override
    public void backward() {
        for (int i = 0; i < output.length; i++) {
            left.setGradient(i, gradient[i]);
            right.setGradient(i, -gradient[i]);
        }
    }
}


class Multiplication extends Operation {
    public Multiplication(Operation left, Operation right) {
        super(left, right);
    }


    @Override
    public void forward() {
        double[] left_out = left.output;
        double[] right_out = right.output;

        for (int i = 0; i < output.length; i++) {
            output[i] = left_out[i]*right_out[i];
        }
    }
    @Override
    public void backward() {
        double[] left_out = left.output;
        double[] right_out = right.output;

        for (int i = 0; i < output.length; i++) {
            left.setGradient(i, gradient[i]*right_out[i]);
            right.setGradient(i, gradient[i]*left_out[i]);
        }
    }
}


class Division extends Operation {
    public Division(Operation left, Operation right) {
        super(left, right);
    }


    @Override
    public void forward() {
        double[] left_out = left.output;
        double[] right_out = right.output;

        for (int i = 0; i < output.length; i++) {
            output[i] = left_out[i]/right_out[i];
        }
    }
    @Override
    public void backward() {
        double[] left_out = left.output;
        double[] right_out = right.output;

        double grad, l, r;
        for (int i = 0; i < output.length; i++) {
            grad = gradient[i];
            l = left_out[i];
            r = right_out[i];
            
            left.setGradient(i, grad/r);
            right.setGradient(i, -grad*l/(r*r));
        }
    }
}


class RELU extends Operation {
    private final double leak;


    public RELU(Operation input, double leak) {
        super(input);
        this.leak = leak;
    }
    public RELU(Operation input) {
        this(input, 0);
    }


    @Override
    public void forward() {
        double[] input = left.output;

        for (int i = 0; i < output.length; i++) {
            output[i] = (input[i] > 0)? input[i] : leak*input[i];
        }
    }
    @Override
    public void backward() {
        for (int i = 0; i < output.length; i++) {
            left.setGradient(i, gradient[i]*((output[i] > 0)? 1 : leak));
        }
    }
}


class SELU extends Operation {
    private static final double
            LAMBDA = 1.0507009873554804934193349852946,
            ALPHA = 1.6732632423543772848170429916717;


    public SELU(Operation input) {
        super(input);
    }


    @Override
    public void forward() {
        double[] input = left.output;

        for (int i = 0; i < output.length; i++) {
            if (input[i] > 0)
                output[i] = LAMBDA*input[i];
            else
                output[i] = LAMBDA*ALPHA*(Math.exp(input[i]) - 1);
        }
    }
    @Override
    public void backward() {
        double[] input = left.output;
        double grad = 0;

        for (int i = 0; i < output.length; i++) {
            if (input[i] > 0)
                grad = LAMBDA;
            else
                grad = LAMBDA*ALPHA*Math.exp(input[i]);

            left.setGradient(i, gradient[i]*grad);
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
            left.setGradient(i, gradient[i]*(output[i]*(1 - output[i])));
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
        for (int i = 0; i < output.length; i++) {
            left.setGradient(i, gradient[i]*(1 - output[i]*output[i]));
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
        int grad_i = 0;

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < left.cols; col++) {
                double grad = 0;

                int right_i = col*cols;
                int this_i = row*cols;

                while (this_i < (row + 1)*cols) {
                    grad += gradient[this_i]*right.output[right_i];
                    this_i++;
                    right_i++;
                }

                left.setGradient(grad_i, grad); grad_i++;
            }
        }

        grad_i = 0;

        for (int row = 0; row < right.rows; row++) {
            for (int col = 0; col < cols; col++) {
                double grad = 0;

                int left_i = row;
                int this_i = col;

                while (this_i < output.length) {
                    grad += gradient[this_i]*left.output[left_i];
                    this_i += cols;
                    left_i += left.cols;
                }

                right.setGradient(grad_i, grad); grad_i++;
            }
        }
    }
}


class SoftmaxCrossEntropy extends Operation {
    private double error = 0;


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
        double[] label = right.output;

        for (int i = 0; i < output.length; i++) {
            left.setGradient(i, output[i] - label[i]);
        }
    }
}
