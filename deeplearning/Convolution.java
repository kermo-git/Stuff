package deeplearning;


// https://cs231n.github.io/convolutional-networks/
public class Convolution extends Layer {
    private int filterSize;
    private int numFilters;
    private int stride;
    private int zeroPadding;
    
    private Matrix filters;
    private Matrix biases;

    private int paddedSizeX, paddedSizeY;


    public Convolution(int numFilters, int filterSize, int stride, int zeroPadding) {
        this.filterSize = filterSize;
        this.numFilters = numFilters;
        this.stride = stride;
        this.zeroPadding = zeroPadding;
    }


    public static Convolution CONV_3X3(int numFilters) {
        return new Convolution(numFilters, 3, 1, 1);
    }


    public static Convolution CONV_5X5(int numFilters) {
        return new Convolution(numFilters, 5, 1, 2);
    }


    @Override
    public void setInput(Matrix input) {
        int numSamples = input.dim0;
        int inputChannels = input.dim1;

        filters = new Matrix(numFilters, inputChannels, filterSize, filterSize);
        biases = new Matrix(numFilters);
        
        parameters.add(filters);
        parameters.add(biases);

        paddedSizeX = input.dim2 + 2 * zeroPadding;
        paddedSizeY = input.dim3 + 2 * zeroPadding;

        int outputSizeX = (paddedSizeX - filterSize) / stride + 1;
        int outputSizeY = (paddedSizeY - filterSize) / stride + 1;

        init(numSamples, numFilters, outputSizeX, outputSizeY);
    }


    private int sample, filter;
    private int paddedX, paddedY;
    private int outputX, outputY;
    private double bias;

    
    @Override
    public void forward(int firstSample, int lastSample) {
        for (sample = firstSample; sample < lastSample; sample++) {
            for (filter = 0; filter < numFilters; filter++) {
                bias = biases.value[filter];

                outputX = 0;
                for (paddedX = 0; paddedX < paddedSizeX; paddedX += stride) {
                    
                    outputY = 0;
                    for (paddedY = 0; paddedY < paddedSizeY; paddedY += stride) {
                        convolutionForward();
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
            for (filter = 0; filter < numFilters; filter++) {

                outputX = 0;
                for (paddedX = 0; paddedX < paddedSizeX; paddedX += stride) {
                    
                    outputY = 0;
                    for (paddedY = 0; paddedY < paddedSizeY; paddedY += stride) {
                        convolutionBackward();
                        outputY++;
                    }
                    outputX++;
                }
            }
        }
    }

    
    private void convolutionForward() {
        int inputX, inputY;
        int filterX, filterY;
        int inputChannel;
        int inputHash, filterHash;
        double dotProduct = 0;

        inputX = paddedX - zeroPadding;

        for (filterX = 0; filterX < filterSize; filterX++) {

            if (inputX < 0) {
                inputX++; continue;
            }
            if (inputX >= input.dim2) {
                break;
            }
            inputY = paddedY - zeroPadding;
            
            for (filterY = 0; filterY < filterSize; filterY++) {

                if (inputY < 0) {
                    inputY++; continue;
                }
                if (inputY >= input.dim3) {
                    break;
                }

                for (inputChannel = 0; inputChannel < filters.dim1; inputChannel++) {

                    filterHash = filters.indexHash(filter, inputChannel, filterX, filterY);
                    inputHash = input.indexHash(sample, inputChannel, inputX, inputY);

                    dotProduct += input.value[inputHash] * filters.value[filterHash];
                }
                inputY++;
            } 
            inputX++;
        }
        value[indexHash(sample, filter, outputX, outputY)] = dotProduct + bias;
    }

    
    private void convolutionBackward() {
        double grad = gradient[indexHash(sample, filter, outputX, outputY)];
        biases.gradient[filter] += grad;

        int inputX, inputY;
        int filterX, filterY;
        int inputChannel;
        int inputHash, filterHash;

        inputX = paddedX - zeroPadding;

        for (filterX = 0; filterX < filterSize; filterX++) {

            if (inputX < 0) {
                inputX++; continue;
            }
            if (inputX >= input.dim2) {
                break;
            }
            inputY = paddedY - zeroPadding;

            for (filterY = 0; filterY < filterSize; filterY++) {

                if (inputY < 0) {
                    inputY++; continue;
                }
                if (inputY >= input.dim3) {
                    break;
                }

                for (inputChannel = 0; inputChannel < filters.dim1; inputChannel++) {

                    filterHash = filters.indexHash(filter, inputChannel, filterX, filterY);
                    inputHash = input.indexHash(sample, inputChannel, inputX, inputY);

                    input.gradient[inputHash] += filters.value[filterHash] * grad;
                    filters.gradient[filterHash] += input.value[inputHash] * grad;
                }
                inputY++;
            } 
            inputX++;
        }
    }
}
