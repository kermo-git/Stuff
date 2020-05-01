package deeplearning;
import java.util.Random;


class Operation {
    protected Operation left;
    protected Operation right;


    protected int rows, cols;
    protected double[] output;
    protected double[] gradient;


    public Operation(int rows, int cols) {
        init(rows, cols);
    }
    protected Operation() {}



    public void forward() {}
    public void backward() {}


    private void init(double std_dev) {
        Random random = new Random();

        for (int i = 0; i < output.length; i++) {
            output[i] = std_dev*random.nextGaussian();
        }
    }
    // Default initialisation
    public void xavier_init() { init(Math.sqrt(1.0/cols)); }
    // Use with RELU
    public void he_init() { init(Math.sqrt(2.0/cols)); }


    public void init() {
        if (left != null) {
            if (right != null && (left.cols != right.cols || left.rows != right.rows))
                throw new IllegalArgumentException();

            this.rows = left.rows;
            this.cols = left.cols;
        }
        output = new double[rows*cols];
        gradient = new double[rows*cols];
    }


    public void init(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;

        output = new double[rows*cols];
        gradient = new double[rows*cols];
    }


    public void update(double learning_rate) {
        for (int i = 0; i < output.length; i++) {
            output[i] -= gradient[i]*learning_rate;
        }
    }

    
    public void setInput(Operation input) {
        left = input; right = null;
    }
    public void setInput(Operation left, Operation right) {
        this.left = left; this.right = right;
    }
    protected void setGradient(int index, double grad) {
        gradient[index] = grad;
    }


    public String toString() {
        int max_len = 0;
        for (int i = 0; i < output.length; i++) {
            int len = Double.toString(output[i]).length();
            if (len > max_len)
                max_len = len;
        }
        StringBuilder sb = new StringBuilder();
        int index = 0;

        for (int row = 0; row < rows; row++) {
            sb.append("[");
            for (int col = 0; col < cols; col++) {
                String num = Double.toString(output[index]);
                int spaces = max_len - num.length();

                for (int i = 0; i < spaces; i++) {
                    sb.append(" ");
                }
                if (col < cols - 1)
                    sb.append(output[index] + ", ");
                else
                    sb.append(output[index]);
                index++;
            }
            sb.append("]\n");
        }
        return sb.toString();
    }
}


class Addition extends Operation {
    public Addition(Operation left, Operation right) {
        this.left = left;
        this.right = right;
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
        this.left = left;
        this.right = right;
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
        this.left = left;
        this.right = right;
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
        this.left = left;
        this.right = right;
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
        this.left = input;
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
        this.left = input;
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
        this.left = input;
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
        this.left = input;
    }


    @Override
    public void forward() {
        double[] input = left.output;

        for (int i = 0; i < output.length; i++) {
            double exp = Math.exp(2*input[i]);
            output[i] = (exp - 1)/(exp + 1);
        }
    }


    @Override
    public void backward() {
        for (int i = 0; i < output.length; i++) {
            left.setGradient(i, gradient[i]*(1 - output[i]*output[i]));
        }
    }
}


class Dropout extends Operation {
    private final double p;
    double[] mask;
    boolean train_mode;
    Random random = new Random();


    public Dropout(Operation input, double p) {
        this.left = input;
        if (p < 0 || p > 1)
            throw new IllegalArgumentException();
        this.p = p;
    }
    public void setTrainMode(boolean mode) {
        train_mode = mode;
    }


    public void init() {
        super.init();
        mask = new double[left.rows*left.cols];
    }


    @Override
    public void forward() {
        double[] input = left.output;

        if (train_mode) {
            for (int i = 0; i < output.length; i++) {
                mask[i] = (random.nextDouble() <= p)? 1/p : 0;
                output[i] = input[i]*mask[i];
            }
        }
        else {
            for (int i = 0; i < output.length; i++) {
                output[i] = input[i];
            }
        }
    }


    @Override
    public void backward() {
        double[] input = left.output;

        double grad;
        for (int i = 0; i < output.length; i++) {
            left.setGradient(i, gradient[i]*mask[i]);
        }
    }
}


class DotProduct extends Operation {
    public DotProduct(Operation left, Operation right) {
        this.left = left;
        this.right = right;
    }


    @Override
    public void init() {
        if (left.cols != right.rows)
            throw new IllegalArgumentException();

        this.rows = left.rows;
        this.cols = right.cols;

        output = new double[rows*cols];
        gradient = new double[rows*cols];
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
        this.left = input;
        this.right = label;
    }


    @Override
    public void forward() {
        double[] input = left.output;
        double[] label = right.output;

        double[] tmp = new double[cols];
        double[] exp_sums = new double[cols];

        int index = 0;
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                if (input[index] > tmp[col])
                    tmp[col] = input[index];
                index++;
            }
        }

        index = 0;
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                output[index] = Math.exp(output[index] - tmp[col]);
                exp_sums[col] += output[index];
                index++;
            }
        }

        index = 0;
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                output[index] /= exp_sums[col];

                if (label[index] != 0)
                    tmp[col] = -Math.log(output[index]);

                index++;
            }
        }

        for (double col_error : tmp) {
            error += col_error;
        }
        error /= cols;
    }


    @Override
    public void backward() {
        double[] label = right.output;

        for (int i = 0; i < output.length; i++) {
            left.setGradient(i, (output[i] - label[i])/cols);
        }
    }
}


class MeanSquareError extends Operation {
    private double error = 0;


    public MeanSquareError(Operation input, Operation label) {
        this.left = input;
        this.right = label;
    }


    @Override
    public void init() {}


    @Override
    public void forward() {
        double[] prediction = left.output;
        double[] label = right.output;

        double difference;
        for (int i = 0; i < prediction.length; i++) {
            difference = prediction[i] - label[i];
            error += difference*difference;
        }
        error /= prediction.length;
    }


    @Override
    public void backward() {
        double[] prediction = left.output;
        double[] label = right.output;

        for (int i = 0; i < prediction.length; i++) {
            left.setGradient(i, 2*(prediction[i] - label[i])/prediction.length);
        }
    }
}


class Dense extends Operation {
    int n;

    Operation W = new Operation();
    Operation b = new Operation();

    Operation dot = new DotProduct(W, null);
    Operation add = new Addition(dot, b);
    Operation activation;

    public Dense(int n, String activation) {
        this.n = n;

        switch (activation) {
            case "sigmoid":
                this.activation = new Sigmoid(add); break;
            case "tanh":
                this.activation = new Tanh(add); break;
            case "relu":
                this.activation = new RELU(add); break;
            case "selu":
                this.activation = new SELU(add); break;
        }
    }


    @Override
    public void init() {
        W.init(n, left.rows);
        b.init(n, 1);

        dot.setInput(W, left);
        dot.init();
        add.init();
        activation.init();

        rows = activation.rows;
        cols = activation.cols;
        output = activation.output;
    }


    @Override
    public void forward() {
        dot.forward();
        add.forward();
        activation.forward();
    }


    @Override
    public void backward() {
        activation.backward();
        add.backward();
        dot.backward();
    }
}
