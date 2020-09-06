package deeplearning;


// https://cs231n.github.io/convolutional-networks/
public class MaxPooling extends Layer {
    int poolSize;
    int stride;
    int inputSizeX, inputSizeY;


    public MaxPooling(int poolSize, int stride) {
        this.poolSize = poolSize;
        this.stride = stride;
    }


    public static MaxPooling OVERLAP() {
        return new MaxPooling(3, 2);
    }
    public static MaxPooling REGULAR() {
        return new MaxPooling(2, 2);
    }


    @Override
    public void setInput(Matrix input) {
        int samples = input.dim0;
        int channels = input.dim1;
        inputSizeX = input.dim2;
        inputSizeY = input.dim3;

        int outputSizeX = (inputSizeX - poolSize)/stride + 1;
        int outputSizeY = (inputSizeY - poolSize)/stride + 1;

        init(samples, channels, outputSizeX, outputSizeY);
    }


    private int sample, channel;
    private int inputX, inputY;
    private int outputX, outputY;

    
    @Override
    public void forward(int firstSample, int lastSample) {
        for (sample = firstSample; sample < lastSample; sample++) {
            for (channel = 0; channel < dim1; channel++) {

                outputX = 0;
                for (inputX = 0; inputX < inputSizeX; inputX += stride) {
                    
                    outputY = 0;
                    for (inputY = 0; inputY < inputSizeX; inputY += stride) {
                        maxPoolingForward();
                        outputY++;
                    }
                    outputX++;
                }
            }
        }
    }


    @Override
    public void backward(int firstSample, int lastSample) {

        for (sample = firstSample; sample < lastSample; sample++) {
            for (channel = 0; channel < dim1; channel++) {

                outputX = 0;
                for (inputX = 0; inputX < inputSizeX; inputX += stride) {
                    
                    outputY = 0;
                    for (inputY = 0; inputY < inputSizeX; inputY += stride) {
                        maxPoolingBackward();
                        outputY++;
                    }
                    outputX++;
                }
            }
        }
    }

    
    private void maxPoolingForward() {
        int poolX, poolY;
        int hash;
        double temp, max = 0;

        for (poolX = inputX; poolX < inputX + poolSize; poolX++) {
            for (poolY = inputY; poolY < inputY + poolSize; poolY++) {
                hash = input.indexHash(sample, channel, poolX, poolY);
                temp = input.value[hash];
                if (temp > max)
                    max = temp;
            }
        }
        value[indexHash(sample, channel, outputX, outputY)] = max;
    }

    
    private void maxPoolingBackward() {
        double grad = gradient[indexHash(sample, channel, outputX, outputY)];
        
        int poolX, poolY;
        int hash;
        double temp, max = 0;

        int maxX = inputX;
        int maxY = inputY;

        for (poolX = inputX; poolX < inputX + poolSize; poolX++) {
            for (poolY = inputY; poolY < inputY + poolSize; poolY++) {
                hash = input.indexHash(sample, channel, poolX, poolY);
                temp = input.value[hash];

                if (temp > max) {
                    max = temp;
                    maxX = poolX;
                    maxY = poolY;
                }
            }
        }
        input.gradient[indexHash(sample, channel, maxX, maxY)] += grad;
    }
}
