#include <iostream>
#include <stdlib.h>
#include <time.h>
#include <math.h>
#include <vector>
using namespace std;

void dot_product(double *matrix, double *column, double *result, int rows, int cols) {
	double sum; int index = 0;
	for (int r = 0; r < rows; r++) {
		sum = 0;
		for (int c = 0; c < cols; c++) {
			sum += matrix[index]*column[c];
			index++;
		}
		result[r] = sum;
	}
}

void add(double *array_1, double *array_2, double *result, int size) {
	for (int i = 0; i < size; i++) {
		result[i] = array_1[i] + array_2[i];
	}
}

void randomize(double *array, double start, double end, int size) {
	for (int i = 0; i < size; i++) {
		array[i] = (end - start)*rand()/RAND_MAX + start;
	}
}

struct Layer {
	double *A; // Activations
	double *B; // Biases
	double *W; // Weights
	double *dB; // Derivatives of the cost function with respect to biases
	double *dW; // Derivatives of the cost function with respect to weights
			
	int SIZE; int MATRIX;
		
	Layer(int size, int input) {
		SIZE = size;
		A = new double[SIZE];
			
		if (input > 0) {
			MATRIX = SIZE*input;
			
			B = new double[SIZE];
			dB = new double[SIZE];
			W = new double[MATRIX];
			dW = new double[MATRIX];
			
			randomize(W, -0.2, 0.2, MATRIX);
	    }
	}
	~Layer() {
		delete[] A;
		if (B != NULL) {
			delete[] B;
			delete[] dB;
			delete[] W;
			delete[] dW;
		}
	}
		
	virtual double activation_func(double x)=0;
	virtual double activation_func_derivative(int i)=0;
			
	void set_A(double *input) {
		for (int i = 0; i < SIZE; i++)
			A[i] = input[i];
	}
	virtual void activate(Layer *input) {
		dot_product(W, input->A, A, SIZE, input->SIZE);
		for (int s = 0; s < SIZE; s++) {
			A[s] = activation_func(A[s] + B[s]);
		}
	}
		
	void add_dW(double *new_dW) {add(dW, new_dW, dW, MATRIX);}
		
	void add_dB(double *new_dB) {add(dB, new_dB, dB, SIZE);}
		
	void update_parameters(double learning_rate, int batch_size) {
		double factor = -learning_rate/batch_size;
		for (int i = 0; i < SIZE; i++) {
			B[i] += dB[i]*factor; dB[i] = 0;
		}
		for (int i = 0; i < MATRIX; i++) {
			W[i] += dW[i]*factor; dW[i] = 0;
		}
	}
};

struct Sigmoid:public Layer {
	Sigmoid(int size, int input_size):Layer(size, input_size) {}
	
	double activation_func(double x) {
		return 1/(1 + exp(-x));
	}
	double activation_func_derivative(int i) {
		double f = A[i];
		return f*(1-f);
	}
};

struct Tanh:public Layer {
	Tanh(int size, int input_size):Layer(size, input_size) {}
		
	double activation_func(double x) {
		return (exp(x) - exp(-x))/(exp(x) + exp(-x));
	}
	double activation_func_derivative(int i) {
		double f = A[i];
		return 1 - f*f;
	}
};

struct ReLU:public Layer {
	ReLU(int size, int input_size):Layer(size, input_size) {}
		
	double activation_func(double x) {
		return (x > 0)? x:0;
	}
	double activation_func_derivative(int i) {
		double f = A[i];
		return (f > 0)? 1:0;
	}
};

struct Softmax:public Layer {
	Softmax(int size, int input_size):Layer(size, input_size) {}
	
	double activation_func(double x) {}
	double activation_func_derivative(int i) {
		double f = A[i];
		return f*(1-f);
	}
	void activate(Layer *input) {
		dot_product(W, input->A, A, SIZE, input->SIZE);
		
		double sum = 0;
		for (int s = 0; s < SIZE; s++) {
			A[s] = A[s] + B[s];
			sum += exp(A[s]);
		}
		for (int s = 0; s < SIZE; s++)
			A[s] = exp(A[s])/sum;
	}
};

class NeuralNetwork {
	protected:
		vector<Layer *> layers;
		double *dA; // Derivatives of the cost function with respect to activations
		int L;
		
		void get_output(double *input) {
			layers[0]->set_A(input);
			for (L = 1; L < layers.size(); L++) {
				layers[L]->activate(layers[L-1]);
			}
		}
		
		virtual double cost(double *label)=0;
		
		virtual void get_dA(double *label)=0;
		
		void get_dA() {
			int size = layers[L]->SIZE;
			int next_size = layers[L+1]->SIZE;
			double *result = new double[size];
			
			for (int k = 0; k < size; k++) {
				double sum = 0;
				for (int j = 0; j < next_size; j++) {
			    	sum += layers[L+1]->W[j*size+k]*
			    	       layers[L+1]->activation_func_derivative(j)*
				       dA[j];
				}
				result[k] = sum;
			}
			delete[] dA; dA = result;
		}
		
		void get_dW() {
			int size = layers[L]->SIZE;
			int prev_size = layers[L-1]->SIZE;
			int dW_index = 0;
			double result[layers[L]->MATRIX];
							  
			for (int j = 0; j < size; j++) {
				for (int k = 0; k < prev_size; k++) {
				    result[dW_index] = layers[L-1]->A[k]*
				    		       layers[L]->activation_func_derivative(j)*
						       dA[j];
					dW_index++;
				}
			}
			layers[L]->add_dW(result);
		}
		
		void get_dB() {
			int size = layers[L]->SIZE;
			double result[size];
							  
			for (int j = 0; j < size; j++) {
				result[j] = layers[L]->activation_func_derivative(j)*dA[j];
			}
			layers[L]->add_dB(result);
		}
		
		void backpropagation(double *label) {
			get_dA(label);
			
			for (L = layers.size()-1;;) {
				get_dB();
				get_dW();
				
				L--;
				if (L >= 1)
					get_dA();
				else break;
			}
			delete[] dA;
		}
		
		NeuralNetwork(int input_size) {
			layers = vector<Layer *>();
			layers.push_back(new Sigmoid(input_size, 0));
		}
		~NeuralNetwork() {
			for (L = 0; L < layers.size(); L++) {
				delete layers[L];
			}
		}
	
	public:
		void add_layer(int size, string activation) {
			int input_size = layers[layers.size() - 1]->SIZE;
			
			if (activation == "sigmoid")
				layers.push_back(new Sigmoid(size, input_size));
			else if (activation == "tanh")
				layers.push_back(new Tanh(size, input_size));
			else if (activation == "relu")
				layers.push_back(new ReLU(size, input_size));
			else if (activation == "softmax")
				layers.push_back(new Softmax(size, input_size));
		}
		
		void train_minibatch(vector<double *>& inputs,
				     vector<double *>& labels, double learning_rate) {
			int size = inputs.size();
			for (int s = 0; s < size; s++) {
				get_output(inputs[s]);
				backpropagation(labels[s]);
			}
			for (L = 1; L < layers.size(); L++) {
				layers[L]->update_parameters(learning_rate, size);
			}
		}
};

class SquareErrorNetwork:public NeuralNetwork{
	
	double cost(double *label) {
		double *output = layers[layers.size()-1]->A;
		int size = layers[layers.size()-1]->SIZE;
		double result = 0;
		
		for (int i = 0; i < size; i++) {
			result += pow((output[i] - label[i]), 2);
		}
		return result;
	}
	
	void get_dA(double *label) {
		double *output = layers[layers.size()-1]->A;
		int size = layers[layers.size()-1]->SIZE;
		dA = new double[size];
		
		for (int i = 0; i < size; i++) {
			dA[i] = 2*(output[i] - label[i]);
		}
	}
	public: SquareErrorNetwork(int input_size):NeuralNetwork(input_size) {}
};

class CrossEntropyNetwork:public NeuralNetwork {
	
	double cost(double *label) {
		double *output = layers[layers.size()-1]->A;
		int size = layers[layers.size()-1]->SIZE;
		double result = 0;
		
		for (int i = 0; i < size; i++) {
			result += label[i]*log(output[i]);
		}
		return -result;
	}
	
	void get_dA(double *label) {
		double *output = layers[layers.size()-1]->A;
		int size = layers[layers.size()-1]->SIZE;
		dA = new double[size];
		
		for (int i = 0; i < size; i++) {
			dA[i] = -label[i]/output[i];
		}
	}
	public: CrossEntropyNetwork(int input_size):NeuralNetwork(input_size) {}
};

int main() {
	srand(time(NULL));
}
