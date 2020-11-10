package deeplearning;

public class Test {
    public static void testConv() {
        Matrix img = Matrix.randomImages(1000, 32, 32);
        Matrix label = Matrix.randomOneHots(1000, 5);

        Model model = new Model(
            Convolution.CONV_3X3(4),
            new RELU(),
            MaxPooling.REGULAR(),
            new Flatten(),
            new Dense(5),
            new Softmax()
        );
        model.setError(new CrossEntropy());
        model.train(img, label, 10, 0.0001);
    }


    public static void testFeedForwardClassification() {
        Matrix input = new Matrix(10, 30);
        input.gaussianFill(0, 1);
        Matrix label = Matrix.randomOneHots(10, 5);

        Model model = new Model(
            new Dense(15),
            new SELU(),
            new Dense(5),
            new Softmax()
        );
        model.setError(new CrossEntropy());
        model.train(input, label, 10, 0.1);
    }


    public static void testFeedForwardRegression() {
        Matrix input = new Matrix(100, 30);
        input.gaussianFill(0, 1);
        Matrix label = new Matrix(100);
        label.uniformFill(0.0, 30.0);

        Model model = new Model(
            new Dense(15),
            new Tanh(),
            new Dense(1)
        );
        model.setError(new MeanSquareError());
        model.train(input, label, 10, 0.1);
    }


    public static void main(String[] args) {
        System.out.print("Feedforward regression\n\n");
        testFeedForwardRegression();

        System.out.print("\nFeedforward classification\n\n");
        testFeedForwardClassification();
        
        System.out.print("\nConvolution\n\n");
        testConv();
    }
}
