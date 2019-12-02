package deep_learning;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

// https://colah.github.io/
class Layer {
    protected List<Node>           inputs;
    protected List<Parameter>      parameters = new ArrayList<>();
    protected List<Operation>      ops = new ArrayList<>();
    protected List<? extends Node> outputs = new ArrayList<>();


    public Layer(Layer previous_layer) {
        inputs = (List<Node>) previous_layer.outputs;
    }
    public Layer(List<Node> inputs) {
        this.inputs = inputs;
    }
    public Layer(int input_size) {
        inputs = new ArrayList<>();
        for (int i = 0; i < input_size; i++) {
            inputs.add(new Num(0));
        }
    }


    public static void write(double[] values, List<? extends Node> nodes) {
        for (int i = 0; i < nodes.size(); i++) {
            nodes.get(i).value = values[i];
        }
    }


    public static double[] read(List<? extends Node> nodes) {
        double[] result = new double[nodes.size()];
        for (int i = 0; i < nodes.size(); i++) {
            result[i] = nodes.get(i).value;
        }
        return result;
    }


    protected void topological_sort() {
        List<Operation> sorted = new ArrayList<>();

        for (Operation op : ops) {
            for (Node arg : op.args) {
                op.topology += (ops.contains(arg))? 1 : 0;
            }
        }
        while (!ops.isEmpty()) {
            for (int i = 0; i < ops.size(); i++) {
                Operation node = ops.get(i);

                if (node.topology == 0) {
                    for (Node output : node.outputs) {
                        output.topology--;
                    }
                    ops.remove(i); i--;
                    sorted.add(node);
                }
            }
        }
        this.ops = sorted;
    }


    public void evaluate() {
        for (Operation op : ops) { op.evaluate(); }
    }


    public void backpropagation() {
        for (int i = ops.size() - 1; i >= 0; i--) {
            ops.get(i).findGradient();
        }
        for (Parameter para : parameters) {
            para.findGradient();
        }
    }


    public void update(int batch_size, double learning_rate) {
        for (Parameter para : parameters) {
            para.update(batch_size, learning_rate);
        }
    }


    public List<? extends Node> get_outputs() { return outputs; }
    public List<? extends Node> get_inputs()  { return inputs;  }

    public void set_outputs(List<? extends Node> outputs) {
        this.outputs = outputs;
    }


    public abstract class Node {
        List<Operation> outputs = new ArrayList<>();
        double grad, value;
        int topology;

        void findGradient() {
            grad = 0;
            for (Operation node: outputs) {
                grad += node.derivative_wrt(this)*node.grad;
            }
        }
    }


    class Num extends Node {
        public Num(double value) { this.value = value; }
    }


    public class Parameter extends Num {
        double grad_sum;

        public Parameter(double value) {
            super(value);
            parameters.add(this);
        }
        @Override
        void findGradient() {
            super.findGradient();
            grad_sum += grad;
        }
        void update(int batch_size, double learning_rate) {
            value = value - learning_rate*(grad_sum/batch_size);
            grad_sum = 0;
        }
    }


    public abstract class Operation extends Node {
        protected List<Node> args = new ArrayList<>();

        protected Operation() { ops.add(this); }
        protected Operation(Node arg) {
            this();
            args.add(arg);
            arg.outputs.add(this);
        }
        protected Operation(Node left, Node right) {
            this();
            args.add(left);
            args.add(right);
            left.outputs.add(this);
            right.outputs.add(this);
        }

        abstract double derivative_wrt(Node arg);
        abstract void evaluate();
    }


    public class Polynomial extends Operation {
        private int scalar;
        private int power;

        public Polynomial(int scalar, Node arg, int power) {
            super(arg);
            this.scalar = scalar;
            this.power = power;
        }
        @Override
        void evaluate() {
            value = scalar*Math.pow(args.get(0).value, power);
        }
        @Override
        double derivative_wrt(Node arg) {
            return power*scalar*Math.pow(arg.value, power - 1);
        }
    }


    public class Ln extends Operation {
        public Ln(Node arg) { super(arg); }

        @Override
        void evaluate() {
            value = Math.log(args.get(0).value);
        }
        @Override
        double derivative_wrt(Node arg) {
            return 1/arg.value;
        }
    }


    public class Exp extends Operation {
        public Exp(Node arg) { super(arg); }

        @Override
        void evaluate() {
            value = Math.exp(args.get(0).value);
        }
        @Override
        double derivative_wrt(Node arg) {
            return value;
        }
    }


    public class Sigmoid extends Operation {
        public Sigmoid(Node arg) { super(arg); }

        @Override
        void evaluate() {
            value = 1/(1 + Math.exp(-args.get(0).value));
        }
        @Override
        double derivative_wrt(Node arg) {
            return value*(1 - value);
        }
    }


    public class Tanh extends Operation {
        public Tanh(Node arg) { super(arg); }

        @Override
        void evaluate() {
            double arg_value = args.get(0).value;
            value = (Math.exp(arg_value) - Math.exp(-arg_value))/
                    (Math.exp(arg_value) + Math.exp(-arg_value));
        }
        @Override
        double derivative_wrt(Node arg) {
            return 1 - value*value;
        }
    }


    public class ReLU extends Operation {
        public ReLU(Node arg) { super(arg); }

        @Override
        void evaluate() {
            double arg_value = args.get(0).value;
            value = (arg_value > 0)? arg_value : 0;
        }
        @Override
        double derivative_wrt(Node arg) {
            return (value > 0)? 1 : 0;
        }
    }


    public class Mul extends Operation {
        public Mul(Node left, Node right) { super(left, right); }

        @Override
        void evaluate() {
            value = args.get(0).value*args.get(1).value;
        }
        @Override
        double derivative_wrt(Node arg) {
            return (arg == args.get(0))?
                    args.get(1).value :
                    args.get(0).value;
        }
    }


    public class Subtract extends Operation {
        public Subtract(Node left, Node right) { super(left, right); }

        @Override
        void evaluate() {
            value = args.get(0).value - args.get(1).value;
        }
        @Override
        double derivative_wrt(Node arg) {
            return (arg == args.get(0))? 1 : -1;
        }
    }


    public class Div extends Operation {
        public Div(Node left, Node right) { super(left, right); }

        @Override
        void evaluate() {
            value = args.get(0).value/args.get(1).value;
        }
        @Override
        double derivative_wrt(Node arg) {
            double left = args.get(0).value;
            double right = args.get(1).value;
            return (arg == args.get(0))? 1/right : -left/(right*right);
        }
    }


    public class Add extends Operation {
        public Add() { super(); }
        public Add(Node left, Node right) {super(left, right);}

        void newArg(Node arg) {
            args.add(arg);
            arg.outputs.add(this);
        }

        @Override
        void evaluate() {
            value = 0;
            for (Node node : args) {
                value += node.value;
            }
        }
        @Override
        double derivative_wrt(Node arg) { return 1; }
    }
}


class Error extends Layer {
    private List<Num> label = new ArrayList<>();
    private Operation error;

    public Error(Layer input, String error_function) { super(input); build(error_function);}

    private void build(String error_function) {
        for (Node node : inputs) label.add(new Num(0));
        switch (error_function) {
            case "squared_error": squared_error(); break;
            case "cross_entropy": cross_entropy(); break;
        }
		topological_sort();
    }

    private void squared_error() {
        Add add = new Add();
        for (int i = 0; i < label.size(); i++) {
            Subtract sub = new Subtract(inputs.get(i), label.get(i));
            add.newArg(new Polynomial(1, sub, 2));
        }
        set_error(add);
    }

    private void cross_entropy() {
        Add add = new Add();
        for (int i = 0; i < label.size(); i++) {
            add.newArg(new Mul(label.get(i), new Ln(inputs.get(i))));
        }
        set_error(new Polynomial(-1, add, 1));
    }

    public void write_label(double[] target) {
        write(target, label);
    }

    @Override
    public void evaluate() {
        super.evaluate();
        error.evaluate();
    }

    @Override
    public void backpropagation() {
        error.grad = 1;
        super.backpropagation();
    }

    private void set_error(Operation error) {
        this.error = error;
        ops.remove(error);
    }
}


class Dense extends Layer {
    private List<List<Parameter>> weights = new ArrayList<>();
    private List<Parameter> biases = new ArrayList<>();

    public Dense(Layer input, int output_size, String activation) { super(input); build(output_size, activation);}
    public Dense(List<Node> input, int output_size, String activation) { super(input); build(output_size, activation);}
    public Dense(int input_size, int output_size, String activation) { super(input_size); build(output_size, activation);}

    private void build(int output_size, String activation) {
        List<Node> output_neurons = new ArrayList<>();
        for (int row = 0; row < output_size; row++) {
            Add add = new Add();
            List<Parameter> weight_row = new ArrayList<>();
            for (Node input_neuron : inputs) {
                Parameter weight = new Parameter(Math.random());
                weight_row.add(weight);
                add.newArg(new Mul(input_neuron, weight));
            }
            Parameter bias = new Parameter(0);
            add.newArg(bias);
            output_neurons.add(add);
            weights.add(weight_row);
            biases.add(bias);
        }
        outputs = activate(output_neurons, activation);
        topological_sort();
    }

    private List<Node> activate(List<Node> vector, String activation) {
        if (activation.equals("softmax"))
            return softmax(vector);

        List<Node> result = new ArrayList<>();
        for (Node node : vector) {
            switch (activation) {
                case "relu":
                    result.add(new ReLU(node)); break;
                case "sigmoid":
                    result.add(new Sigmoid(node)); break;
                case "tanh":
                    result.add(new Tanh(node)); break;
            }
        }
        return result;
    }

    private List<Node> softmax(List<Node> nodes) {
        List<Node> new_nodes = new ArrayList<>();
        Add sum = new Add();

        for (Node node : nodes) {
            Layer.Exp exp = new Exp(node);
            sum.newArg(exp);
            new_nodes.add(new Div(exp, sum));
        }
        return new_nodes;
    }


    public double[][] get_weights() {
        double[][] result = new double[weights.size()][weights.get(0).size()];
        for (int i = 0; i < result.length; i++) {
            result[i] = read(weights.get(i));
        }
        return result;
    }
    public void set_weights(double[][] weights) {
        for (int i = 0; i < weights.length; i++) {
            write(weights[i], this.weights.get(i));
        }
    }
    public double[] get_biases() {
        return read(biases);
    }
    public void set_biases(double[] biases) {
        write(biases, this.biases);
    }
}


class LSTM extends Layer {
    private List<Num> prev_outputs = new ArrayList<>();
    private List<Num> prev_cell_state = new ArrayList<>();
    private List<Node> cell_state = new ArrayList<>();

    private List<double[]> input_history = new ArrayList<>();
    private List<double[]> output_history = new ArrayList<>();
    private List<double[]> cell_state_history = new ArrayList<>();

    private List<Dense> gates = new ArrayList<>(); // [forget, input, cell_update, output]

    public LSTM(Layer input, int output_size)    { super(input); build(output_size); }
    public LSTM(int input_size, int output_size) { super(input_size); build(output_size); }

    private void build(int output_size) {
        for (int i = 0; i < output_size; i++) {
            prev_cell_state.add(new Num(0));
            prev_outputs.add(new Num(0));
        }
        List<Node> combined = concatenate(prev_outputs, inputs);

        Dense forget_gate = new Dense(combined, output_size, "sigmoid");
        Dense input_gate = new Dense(combined, output_size, "sigmoid");
        Dense cell_update_gate = new Dense(combined, output_size, "tanh");
        Dense output_gate = new Dense(combined, output_size, "sigmoid");

        List<Node> forget = pointwise_mul(prev_cell_state, forget_gate.outputs);
        List<Node> update = pointwise_mul(input_gate.outputs, cell_update_gate.outputs);
        cell_state = pointwise_sum(forget, update);

        List<Node> tanh = new ArrayList<>();
        for (Node node : cell_state) tanh.add(new Tanh(node));
        outputs = pointwise_mul(tanh, output_gate.outputs);
        gates = Arrays.asList(forget_gate, input_gate, cell_update_gate, output_gate);
        topological_sort();
    }


    public List<Node> pointwise_sum(List<? extends Node> vector_1, List<? extends Node> vector_2) {
        List<Node> result = new ArrayList<>();
        for (int i = 0; i < vector_1.size(); i++) {
            Layer.Add add = new Add();
            add.newArg(vector_1.get(i));
            add.newArg(vector_2.get(i));
            result.add(add);
        }
        return result;
    }


    public List<Node> pointwise_mul(List<? extends Node> vector_1, List<? extends Node> vector_2) {
        List<Node> result = new ArrayList<>();
        for (int i = 0; i < vector_1.size(); i++) {
            result.add(new Mul(vector_1.get(i), vector_2.get(i)));
        }
        return result;
    }


    public List<Node> concatenate(List<? extends Node> vector_1, List<? extends Node> vector_2) {
        List<Node> result = new ArrayList<>();
        result.addAll(vector_1);
        result.addAll(vector_2);
        return result;
    }


    private void copy_values(List<? extends Node> from_nodes,
                             List<? extends Node> to_nodes) {
        for (int i = 0; i < to_nodes.size(); i++) {
            to_nodes.get(i).value = from_nodes.get(i).value;
        }
    }


    private void copy_gradients(List<? extends Node> from_nodes,
                                List<? extends Node> to_nodes) {
        for (int i = 0; i < to_nodes.size(); i++) {
            to_nodes.get(i).grad = from_nodes.get(i).grad;
        }
    }


    @Override
    public void evaluate() {
        input_history.add(read(inputs));
        for (Dense gate : gates) {
            gate.evaluate();
        }
        super.evaluate();
        output_history.add(read(outputs));
        copy_values(outputs, prev_outputs);
        cell_state_history.add(read(cell_state));
        copy_values(cell_state, prev_cell_state);
    }


    protected void restore_history(int t) {
        write(input_history.get(t), inputs);
        write(output_history.get(t - 1), prev_outputs);
        write(cell_state_history.get(t - 1), prev_cell_state);
    }


    @Override
    public void backpropagation() {
        super.backpropagation();
        for (Dense gate : gates) {
            gate.backpropagation();
        }
        copy_gradients(prev_outputs, outputs);
        copy_gradients(prev_cell_state, cell_state);
    }


    public void backpropagation_through_time(int timesteps, double learning_rate) {
        int t = output_history.size() - 1;
        for (int i = 0; i < timesteps; i++, t--) {
            backpropagation();
            restore_history(t);
        }
        update(1, learning_rate);
    }
}