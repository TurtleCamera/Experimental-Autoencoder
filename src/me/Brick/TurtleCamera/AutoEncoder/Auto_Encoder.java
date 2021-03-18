package me.Brick.TurtleCamera.AutoEncoder;

import java.util.HashMap;

public class Auto_Encoder {
	
	// Some constants
	public static final int NUM_ENCODED_CHARACTERS = 44; // Number of characters we are using
	public static final int MIN_SENTENCE_LENGTH = 20; // Minimum sentence length
	public static final int MAX_SENTENCE_LENGTH = 24; // Maxinum number of chars in a sentence
	public static final int INPUT_LAYER_NODES = MAX_SENTENCE_LENGTH; // Number of nodes in the input layer
	public static final int LARGE_HIDDEN_LAYER_NODES = 12; // Number of nodes in the larger hidden layers
	public static final int SMALL_HIDDEN_LAYER_NODES = 7; // Number of nodes in the smaller hidden layers
	public static final int BOTTLENECK_LAYER_NODES = 4; // Number of nodes for the bottleneck layer
	public static final int OUTPUT_LAYER_NODES = INPUT_LAYER_NODES; // Number of nodes in the output layer
	
	public static final int AVERAGE_STOCHASTIC_DERIVATIVES = 20; // Used to average the weights and biases in the stochastic weight matrices and bias vectors
	public static final int MOD_BY_TWENTY = 20; // Used to check which image we are in within an image group
	
	public static final int GRADIENT_COMPONENT_UNITS = 1; // Multiplied with ∂C/∂b and ∂C/∂w to accelerate learning if these values are small (this is an experimental value, but it should be 1)

	public static int count; // How much training data we've gone through
	public static Node inputLayer[]; // Nodes for the input layer
	public static Node hiddenEncodingLayer1[]; // Nodes for the hidden encoding layer 1
	public static Node hiddenEncodingLayer2[]; // Nodes for the hidden encoding layer 2
	public static Node bottleneckLayer[]; // Nodes for the bottleneck layer
	public static Node hiddenDecodingLayer1[]; // Nodes for the hidden dEncoding layer 1
	public static Node hiddenDecodingLayer2[]; // Nodes for the hidden dEncoding layer 2
	public static Node outputLayer[]; // Nodes for the output layer
	
	public static float desiredOutput[]; // An array (vector) containing all of the desired activations for a given image
	
	public static float weightMatrix1[][]; // Weight matrix between input layer and hidden encoding layer 1
	public static float weightMatrix2[][]; // Weight matrix between hidden encoding layer 1 and hidden encoding layer 2
	public static float weightMatrix3[][]; // Weight matrix between hidden encoding layer 2 and bottleneck layer
	public static float weightMatrix4[][]; // Weight matrix between bottleneck layer and hidden dEncoding layer 1
	public static float weightMatrix5[][]; // Weight matrix between hidden dEncoding layer 1 and hidden dEncoding layer 2
	public static float weightMatrix6[][]; // Weight matrix between hidden dEncoding layer 2 and output layer
	
	public static float [] biasVector1; // Bias vector for hidden encoding layer 1
	public static float [] biasVector2; // Bias vector for hidden encoding layer 2
	public static float [] biasVector3; // Bias vector for bottleneck layer	
	public static float [] biasVector4; // Bias vector for hidden dEncoding layer 1
	public static float [] biasVector5; // Bias vector for hidden dEncoding layer 2
	public static float [] biasVector6; // Bias vector for output dEncoding layer
	
	public static float stochasticPartialDerivativeWeightMatrix1[][]; // Average of the 20 partial derivatives in the weight matrix between input layer and hidden encoding layer 1
	public static float stochasticPartialDerivativeWeightMatrix2[][]; // Average of the 20 partial derivatives in the weight matrix between hidden encoding layer 1 and hidden encoding layer 2
	public static float stochasticPartialDerivativeWeightMatrix3[][]; // Average of the 20 partial derivatives in the weight matrix between hidden encoding layer 2 and bottleneck layer 
	public static float stochasticPartialDerivativeWeightMatrix4[][]; // Average of the 20 partial derivatives in the weight matrix between input dEncoding layer and hidden dEncoding layer 1
	public static float stochasticPartialDerivativeWeightMatrix5[][]; // Average of the 20 partial derivatives in the weight matrix between hidden dEncoding layer 1 and hidden dEncoding layer 2
	public static float stochasticPartialDerivativeWeightMatrix6[][]; // Average of the 20 partial derivatives in the weight matrix between hidden dEncoding layer 2 and output layer 
	
	public static float [] stochasticPartialDerivativeBiasVector1; // Average of the 20 partial derivatives in the bias vector for hidden encoding layer 1
	public static float [] stochasticPartialDerivativeBiasVector2; // Average of the 20 partial derivatives in the bias vector for hidden encoding layer 2
	public static float [] stochasticPartialDerivativeBiasVector3; // Average of the 20 partial derivatives in the bias vector for bottleneck layer
	public static float [] stochasticPartialDerivativeBiasVector4; // Average of the 20 partial derivatives in the bias vector for hidden dEncoding layer 1
	public static float [] stochasticPartialDerivativeBiasVector5; // Average of the 20 partial derivatives in the bias vector for hidden dEncoding layer 2
	public static float [] stochasticPartialDerivativeBiasVector6; // Average of the 20 partial derivatives in the bias vector for output layer
	
	public static HashMap<Character, Integer> charEncoder; // Used to get the index of the character for one hot encoding
	public static HashMap<Integer, Character> charDecoder; // Used to get the character from the index for one hot encoding
	public static Information info; // Stores all of the information to be called by the toggle command in game
	
	public static boolean toggleWeightRestriction; // Tells whether we restrict the weights between [-1, 1]
	
	public static void readSentence(String sentence) {
		// Record this sentence
		info.inputString = sentence;
		
		// All of the calculations regarding the neural network start from this method
		// Check if we are on the first sentence of a group
		if(checkFirstSetence()) {
			// Clear for the next group of sentences
			clearStochasticWeightsAndBiases();
		}
		
		// One hot encode the input vector (and desired output vector)
		extractInputSentence(info.inputString);

		// Feed forward
		feedForward();
		
		// Calculate the cost and average cost of the current image using the cost function
		calculateCost();
					
		// Reconstruct the output string
		reconstruct();
					
		// Finally calculate partial derivatives (and backpropagate when we sum the average gradient vector of the past 20 sentences)
		backPropagate();
	}
	
	public static void calculateCost() {
		// Use the cross entropy formula to calculate the cost (ignore 0s)
		info.cost = 0;
		
		for(int i = 0; i < OUTPUT_LAYER_NODES; i ++) {
			if(outputLayer[i].activation > 0) {
				info.cost += -1 * desiredOutput[i] * Math.log(outputLayer[i].activation);
			}
		}
	}
	
    public static void feedForward() {
    	// Increment count
    	count ++;
    	
    	// Feed all input from the input layer all the way to the output layer
    	matrixMultiplication(weightMatrix1, inputLayer, biasVector1, hiddenEncodingLayer1);
    	matrixMultiplication(weightMatrix2, hiddenEncodingLayer1, biasVector2, hiddenEncodingLayer2);
    	matrixMultiplication(weightMatrix3, hiddenEncodingLayer2, biasVector3, bottleneckLayer);
    	matrixMultiplication(weightMatrix4, bottleneckLayer, biasVector4, hiddenDecodingLayer1);
    	matrixMultiplication(weightMatrix5, hiddenDecodingLayer1, biasVector5, hiddenDecodingLayer2);
    	matrixMultiplication(weightMatrix6, hiddenDecodingLayer2, biasVector6, outputLayer);

		// Store the code
    	storeCode();
    }
    
    public static void storeCode() {
		// Copy all of the float values into the info object
		for(int i = 0; i < bottleneckLayer.length; i ++) {
			info.code[i] = bottleneckLayer[i].activation;
		}
    }
    
    public static void matrixMultiplication(float weightMatrix[][], Node layerVector[], float biasVector[], Node resultingVector[]) {
    	// First clear the resulting vector (don't clear the input layer because it's constantly being updated already
    	clearResultingVector(resultingVector);
    	
    	// In terms of linear algebra, to calculate the resultingVector (i.e. the layer we are feeding forward to), we simply use the Ax + b = r formula
    	// where A is the weight matrix, x is the layer vector containing the activations of the nodes we want to feed forward, b is the bias vector, and
    	// r is the resulting vector we are feeding into
    	for(int r = 0; r < weightMatrix.length; r ++) {
    		// Columns of weightMatrix, rows of layerVector, rows of biasVector, and rows of resultingVector are assumed to be equal
    		for(int c = 0; c < weightMatrix[0].length; c ++) {
    			// Multiply row of weightMatric to column of layerVector and sum the result
    			resultingVector[r].unsquishedActivation += weightMatrix[r][c] * layerVector[c].activation;
    		}
    		
    		// Then add the bias
    		resultingVector[r].unsquishedActivation += biasVector[r];
    		
    		// Finally, squish the resulting activation value by using the sigmoid function
    		resultingVector[r].activation = sigmoid(resultingVector[r].unsquishedActivation);
    	}
    }
    
    public static void clearResultingVector(Node [] resultingVector) {
    	for(int i = 0; i < resultingVector.length; i ++) {
    		// Don't need to change normal activation because it will be calculated anyway
    		resultingVector[i].unsquishedActivation = 0;
    	}
    }
    
	// The sigmoid function used to translate the numbers to a value in the range of 0 to 1
	public static float sigmoid(float x) {
		return (float) (1 / (1 + Math.pow((float) Math.E, -1 * x)));
	}
	
	// The derivative of the sigmoid function
	public static float derivativeSigmoid(float x) {
		return (float) (Math.pow((float) Math.E, -1 * x) / Math.pow(1 + Math.pow((float) Math.E, -1 * x), 2));
	}
	
	public static void backPropagate() {
		// For each layer, backpropagate the weight matrix and bias vector and then backpropagate the activations 
		deriveWeightMatrix(stochasticPartialDerivativeWeightMatrix6, hiddenDecodingLayer2, outputLayer, desiredOutput);
		deriveBiasVector(stochasticPartialDerivativeBiasVector6, outputLayer, desiredOutput);
		deriveHiddenLayerActivations(weightMatrix6, hiddenDecodingLayer2, outputLayer, desiredOutput);
		
		deriveWeightMatrix(stochasticPartialDerivativeWeightMatrix5, hiddenDecodingLayer1, hiddenDecodingLayer2);
		deriveBiasVector(stochasticPartialDerivativeBiasVector5, hiddenDecodingLayer2);
		deriveHiddenLayerActivations(weightMatrix5, hiddenDecodingLayer1, hiddenDecodingLayer2);
		
		deriveWeightMatrix(stochasticPartialDerivativeWeightMatrix4, bottleneckLayer, hiddenDecodingLayer1);
		deriveBiasVector(stochasticPartialDerivativeBiasVector4, hiddenDecodingLayer1);
		deriveHiddenLayerActivations(weightMatrix4, bottleneckLayer, hiddenDecodingLayer1);
		
		deriveWeightMatrix(stochasticPartialDerivativeWeightMatrix3, hiddenEncodingLayer2, bottleneckLayer);
		deriveBiasVector(stochasticPartialDerivativeBiasVector3, bottleneckLayer);
		deriveHiddenLayerActivations(weightMatrix3, hiddenEncodingLayer2, bottleneckLayer);
		
		deriveWeightMatrix(stochasticPartialDerivativeWeightMatrix2, hiddenEncodingLayer1, hiddenEncodingLayer2);
		deriveBiasVector(stochasticPartialDerivativeBiasVector2, hiddenEncodingLayer2);
		deriveHiddenLayerActivations(weightMatrix2, hiddenEncodingLayer1, hiddenEncodingLayer2);
		
		// We can skip the activations for the input layer since we won't use them
		deriveWeightMatrix(stochasticPartialDerivativeWeightMatrix1, inputLayer, hiddenEncodingLayer1);
		deriveBiasVector(stochasticPartialDerivativeBiasVector1, hiddenEncodingLayer1);
		
		// If we traversed 20 sentences, adjust the weights and biases
		if(checkTwentySentences()) {
			// Now we tell the neural network to learn
			adjustWeightsAndBiases();
		}
	}
	
	public static void deriveWeightMatrix(float [][] stochasticWeightMatrix, Node [] previousLayer, Node [] forwardLayer, float [] desiredOutputLayer) {
		// Calculate ∂C/∂w(L) using a formula specific for the last layer
		// This double for loop will traverse the columns first before moving to the next row in the weight matrix (the indices might look weird because I'm following my notes)
		for(int j = 0; j < forwardLayer.length; j ++) {
			for(int i = 0; i < previousLayer.length; i ++) {
				// Formula: ∂C/∂w(L) = ∂C/∂a(L) * ∂a(L)/∂z(L) * ∂z(L)/∂w(L) (MUST USE THE NEGATIVE GRADIENT VECTOR)
				stochasticWeightMatrix[j][i] = -1 * 2 * (forwardLayer[j].activation - desiredOutputLayer[j]) * derivativeSigmoid(forwardLayer[j].unsquishedActivation) * previousLayer[i].activation;
			}
		}
	}
	
	public static void deriveWeightMatrix(float [][] stochasticWeightMatrix, Node [] previousLayer, Node [] forwardLayer) {
		// Calculate ∂C/∂w(L-1) for all weights in the weight matrix using a formula specific for weight matrices before the last weight matrix
		// This double for loop will traverse the columns first before moving to the next row in the weight matrix (the indices might look weird because I'm following my notes)
		for(int i = 0; i < forwardLayer.length; i ++) {
			for(int h = 0; h < previousLayer.length; h ++) {
				// Formula: ∂C/∂w(L-1) = Σ(∂C/∂a(L) * ∂a(L)/∂z(L) * ∂z(L)/∂a(L-1)) * ∂a(L-1)/∂z(L-1) * ∂z(L-1)/∂w(L-1) (MUST USE THE NEGATIVE GRADIENT VECTOR)
				// Remember that hiddenDecodingLayer2[j].partialDerivative stores ∂C/∂a(L) * ∂a(L)/∂z(L) * ∂z(L)/∂a(L-1) in the summation part of the formula
				stochasticWeightMatrix[i][h] = -1 * forwardLayer[i].partialDerivative * derivativeSigmoid(forwardLayer[i].unsquishedActivation) * previousLayer[h].activation;
			}
		}
	}
	
	public static void deriveBiasVector(float [] stochasticBiasVector, Node [] forwardLayer, float [] desiredOutputLayer) {
		// Calculate ∂C/∂b(L) using a formula specific for the last layer
		// This double for loop will traverse the columns first before moving to the next row in the weight matrix (the indices might look weird because I'm following my notes)
		for(int j = 0; j < forwardLayer.length; j ++) {
			// Formula: ∂C/∂w(L) = ∂C/∂a(L) * ∂a(L)/∂z(L) * ∂z(L)/∂b(L) (MUST USE THE NEGATIVE GRADIENT VECTOR)
			stochasticBiasVector[j] = -1 * 2 * (forwardLayer[j].activation - desiredOutputLayer[j]) * derivativeSigmoid(forwardLayer[j].unsquishedActivation) * 1;
		}	
	}
	
	public static void deriveBiasVector(float [] stochasticBiasVector, Node [] forwardLayer) {
		// Calculate ∂C/∂b(L-1)
		// This double for loop will traverse the columns first before moving to the next row in the weight matrix (the indices might look weird because I'm following my notes)
		for(int i = 0; i < forwardLayer.length; i ++) {
			// Formula: ∂C/∂b(L-1) = Σ(∂C/∂a(L) * ∂a(L)/∂z(L) * ∂z(L)/∂a(L-1)) * ∂a(L-1)/∂z(L-1) * ∂z(L-1)/∂b(L-1) (MUST USE THE NEGATIVE GRADIENT VECTOR)
			// Remember that hiddenDecodingLayer2[j].partialDerivative stores ∂C/∂a(L) * ∂a(L)/∂z(L) * ∂z(L)/∂a(L-1) in the summation part of the formula
			stochasticBiasVector[i] = -1 * forwardLayer[i].partialDerivative * derivativeSigmoid(forwardLayer[i].unsquishedActivation) * 1;
		}
	}
	
	public static void deriveHiddenLayerActivations(float [][] weightMatrix, Node [] previousLayer, Node [] forwardLayer, float [] desiredOutputLayer) {
		// Reset the partial derivatives first
		clearPartialDerivativesToActivations(previousLayer);
		
		// Calculate ∂C/∂a(L-1) for all activation nodes using a formula specifically for involving the last layer (this is just to make calculations easier for later weights, specifically the Σ(∂C/∂a(L) * ∂a(L)/∂z(L) * ∂z(L)/∂a(L-1)) part in deriveWeightMatrix2())
		// This double for loop will traverse the rows first before moving to the next column in the weight matrix (sum derivatives of each node)
		for(int i = 0; i < previousLayer.length; i ++) {
			for(int j = 0; j < forwardLayer.length; j ++) {
				// Formula: ∂C/∂a(L-1) = ∂C/∂a(L) * ∂a(L)/∂z(L) * ∂z(L)/∂a(L-1) (DO NOT USE NEGATIVE GRADIENT AS THIS IS PASSED TO DEEPER WEIGHTS AND BIASES)
				previousLayer[i].partialDerivative += 2 * (forwardLayer[j].activation - desiredOutputLayer[j]) * derivativeSigmoid(forwardLayer[j].unsquishedActivation) * weightMatrix[j][i];
			}
		}
	}
	
	public static void deriveHiddenLayerActivations(float [][] weightMatrix, Node [] previousLayer, Node [] forwardLayer) {
		// Reset the partial derivatives first
		clearPartialDerivativesToActivations(previousLayer);
		
		// Note, we wee treat the bottleneck layer as a hidden layer for this method
		// Calculate ∂C/∂a(L-2) for all activation nodes in hidden layer 1 (this is just to make calculations easier for later weights, specifically the Σ(Σ(∂C/∂a(L) * ∂a(L)/∂z(L) * ∂z(L)/∂a(L-1)) * ∂a(L-1)/∂z(L-1) * ∂z(L-1)/∂a(L-2)) part in deriveWeightMatrix1())
		// This double for loop will traverse the rows first before moving to the next column in the weight matrix (sum derivatives of each node)
		for(int h = 0; h < previousLayer.length; h ++) {
			for(int i = 0; i < forwardLayer.length; i ++) {
				// Formula: ∂C/∂a(L-2) = Σ(∂C/∂a(L) * ∂a(L)/∂z(L) * ∂z(L)/∂a(L-1)) * ∂a(L-1)/∂z(L-1) * ∂z(L-1)/∂a(L-2)
				previousLayer[h].partialDerivative += forwardLayer[i].partialDerivative * derivativeSigmoid(forwardLayer[i].unsquishedActivation) * weightMatrix[i][h];
			}
		}
	}
	
	public static void clearPartialDerivativesToActivations(Node [] layer) {
		// Set all of the partial derivatives with respect to the activations to 0 for resetting
		for(int i = 0; i < layer.length; i ++) {
			layer[i].partialDerivative = 0;
		}
	}
	
	public static boolean checkTwentySentences() {
		// Checks if we traversed 20 sentences
		if(count % MOD_BY_TWENTY == 0 && count >= AVERAGE_STOCHASTIC_DERIVATIVES) {
			return true;
		}
		
		return false;
	}	
	
	public static boolean checkFirstSetence() {
		// Checks if we hit the first image in an image group
		if(count % MOD_BY_TWENTY == 1 && count >= AVERAGE_STOCHASTIC_DERIVATIVES) {
			return true;
		}
		
		return false;
	}
	
	public static void adjustWeightsAndBiases() {
		// Use partial derivatives to change the weights and biases (For this project, I'll adjust the weights and biases by moving them by GRADIENT_COMPONENT_UNITS unit(s), which is simply changing the weight and bias by the value of the partial derivative)
		// Edit the weight matrices
		adjustWeightMatrix(weightMatrix1, stochasticPartialDerivativeWeightMatrix1);
		adjustWeightMatrix(weightMatrix2, stochasticPartialDerivativeWeightMatrix2);
		adjustWeightMatrix(weightMatrix3, stochasticPartialDerivativeWeightMatrix3);
		adjustWeightMatrix(weightMatrix4, stochasticPartialDerivativeWeightMatrix4);
		adjustWeightMatrix(weightMatrix5, stochasticPartialDerivativeWeightMatrix5);
		adjustWeightMatrix(weightMatrix6, stochasticPartialDerivativeWeightMatrix6);
		
		// Edit the bias vectors
		adjustBiasVector(biasVector1, stochasticPartialDerivativeBiasVector1);
		adjustBiasVector(biasVector2, stochasticPartialDerivativeBiasVector2);
		adjustBiasVector(biasVector3, stochasticPartialDerivativeBiasVector3);
		adjustBiasVector(biasVector4, stochasticPartialDerivativeBiasVector4);
		adjustBiasVector(biasVector5, stochasticPartialDerivativeBiasVector5);
		adjustBiasVector(biasVector6, stochasticPartialDerivativeBiasVector6);
	}
	
	public static void adjustWeightMatrix(float [][] weightMatrix, float [][] stochasticWeightMatrix) {
		// Edit each weight by GRADIENT_COMPONENT_UNITS amount
		for(int i = 0; i < weightMatrix.length &&  i < stochasticWeightMatrix.length; i ++) {
			for(int j = 0; j < weightMatrix[i].length && j < stochasticWeightMatrix[i].length; j ++) {
				weightMatrix[i][j] += (stochasticWeightMatrix[i][j] / AVERAGE_STOCHASTIC_DERIVATIVES) * GRADIENT_COMPONENT_UNITS;
				
				// Keep the value within -1 and 1 if we toggled the weight restriction
				weightMatrix[i][j] = boundValue(weightMatrix[i][j]);
			}
		}
	}
	
	public static void adjustBiasVector(float [] biasVector, float [] stochasticBiasVector) {
		// Edit weight bias by GRADIENT_COMPONENT_UNITS amount
		for(int i = 0; i < biasVector.length && i < stochasticBiasVector.length; i ++) {
			biasVector[i] += (stochasticBiasVector[i] / AVERAGE_STOCHASTIC_DERIVATIVES) * GRADIENT_COMPONENT_UNITS;
				
			// Keep the value within -1 and 1
			biasVector[i] = boundValue(biasVector[i]);
		}
	}
	
	public static float boundValue(float value) {
		// Check if this feature is enabled
		if(toggleWeightRestriction) {
			// Don't let the value go above 1
			if(value > 1) {
				return 1;
			}
			else if(value < -1) {
				return -1;
			}
			else {
				return value;
			}
		}
		else {
			// If disabled, then just return the value
			return value;
		}
	}
	
	public static void clearStochasticWeightsAndBiases() {
		// Clears the stochastic weight matrices and biases for the next 20 sentences
		// Start with weight matrices first
		clearStochasticWeights(stochasticPartialDerivativeWeightMatrix1);
		clearStochasticWeights(stochasticPartialDerivativeWeightMatrix2);
		clearStochasticWeights(stochasticPartialDerivativeWeightMatrix3);
		clearStochasticWeights(stochasticPartialDerivativeWeightMatrix4);
		clearStochasticWeights(stochasticPartialDerivativeWeightMatrix5);
		clearStochasticWeights(stochasticPartialDerivativeWeightMatrix6);
		
		// Then clear the bias vectors
		clearStochasticBiases(stochasticPartialDerivativeBiasVector1);
		clearStochasticBiases(stochasticPartialDerivativeBiasVector2);
		clearStochasticBiases(stochasticPartialDerivativeBiasVector3);
		clearStochasticBiases(stochasticPartialDerivativeBiasVector4);
		clearStochasticBiases(stochasticPartialDerivativeBiasVector5);
		clearStochasticBiases(stochasticPartialDerivativeBiasVector6);
	}
	
	public static void clearStochasticWeights(float [][] stochasticWeightMatrix) {
		// Self explanatory
		for(int i = 0; i < stochasticWeightMatrix.length; i ++) {
			for(int j = 0; j < stochasticWeightMatrix[i].length; j ++) {
				stochasticWeightMatrix[i][j] = 0;
			}
		}
	}
	
	public static void clearStochasticBiases(float [] stochasticBiasVector) {
		// Self explanatory
		for(int i = 0; i < stochasticBiasVector.length; i ++) {
			stochasticBiasVector[i] = 0;
		}
	}
	
	public static void reconstruct() {
		// Reset everything everything in the info object beforehand
		info.outputString = "";
		info.numCorrect = 0;
		
		for(int i = 0; i < outputLayer.length; i ++) {
			// Retrieve the character from this index
			char decodedChar = charDecoder.get((int) Math.round(outputLayer[i].activation * (NUM_ENCODED_CHARACTERS - 1)));
			
			// Check if we guessed correctly
			if(info.inputString.charAt(i) == decodedChar) {
				info.numCorrect ++;
			}
			
			// Continue building the output string for Brick to use
			info.outputString += decodedChar;
		}
	}
	
	public static void clearPartialDerivativeActivations() {
		// Clears the partial derivatives of the cost function with respect to the activations of these hidden layers since we are not using them in testing
		for(int i = 0; i < hiddenDecodingLayer1.length; i ++) {
			hiddenDecodingLayer1[i].partialDerivative = 0;
		}
		
		for(int i = 0; i < hiddenDecodingLayer2.length; i ++) {
			hiddenDecodingLayer2[i].partialDerivative = 0;
		}
	}
	
	public static void encodeChars() {
		// Initialize encoder and decoder
		charEncoder = new HashMap<Character, Integer>();
		charDecoder = new HashMap<Integer, Character>();
		
		// Assign IDs to each char we are using (use magic numbers here)
		charEncoder.put(' ', 0);
		charEncoder.put('a', 1);
		charEncoder.put('b', 2);
		charEncoder.put('c', 3);
		charEncoder.put('d', 4);
		charEncoder.put('e', 5);
		charEncoder.put('f', 6);
		charEncoder.put('g', 7);
		charEncoder.put('h', 8);
		charEncoder.put('i', 9);
		charEncoder.put('j', 10);
		charEncoder.put('k', 11);
		charEncoder.put('l', 12);
		charEncoder.put('m', 13);
		charEncoder.put('n', 14);
		charEncoder.put('o', 15);
		charEncoder.put('p', 16);
		charEncoder.put('q', 17);
		charEncoder.put('r', 18);
		charEncoder.put('s', 19);
		charEncoder.put('t', 20);
		charEncoder.put('u', 21);
		charEncoder.put('v', 22);
		charEncoder.put('w', 23);
		charEncoder.put('x', 24);
		charEncoder.put('y', 25);
		charEncoder.put('z', 26);
		charEncoder.put('0', 27);
		charEncoder.put('1', 28);
		charEncoder.put('2', 29);
		charEncoder.put('3', 30);
		charEncoder.put('4', 31);
		charEncoder.put('5', 32);
		charEncoder.put('6', 33);
		charEncoder.put('7', 34);
		charEncoder.put('8', 35);
		charEncoder.put('9', 36);
		charEncoder.put(',', 37);
		charEncoder.put('.', 38);
		charEncoder.put('?', 39);
		charEncoder.put('!', 40);
		charEncoder.put('\'', 41);
		charEncoder.put('(', 42);
		charEncoder.put(')', (NUM_ENCODED_CHARACTERS - 1));
		

		// Now do the same thing but the other way around
		charDecoder.put(0, ' ');
		charDecoder.put(1, 'a');
		charDecoder.put(2, 'b');
		charDecoder.put(3, 'c');
		charDecoder.put(4, 'd');
		charDecoder.put(5, 'e');
		charDecoder.put(6, 'f');
		charDecoder.put(7, 'g');
		charDecoder.put(8, 'h');
		charDecoder.put(9, 'i');
		charDecoder.put(10, 'j');
		charDecoder.put(11, 'k');
		charDecoder.put(12, 'l');
		charDecoder.put(13, 'm');
		charDecoder.put(14, 'n');
		charDecoder.put(15, 'o');
		charDecoder.put(16, 'p');
		charDecoder.put(17, 'q');
		charDecoder.put(18, 'r');
		charDecoder.put(19, 's');
		charDecoder.put(20, 't');
		charDecoder.put(21, 'u');
		charDecoder.put(22, 'v');
		charDecoder.put(23, 'w');
		charDecoder.put(24, 'x');
		charDecoder.put(25, 'y');
		charDecoder.put(26, 'z');
		charDecoder.put(27, '0');
		charDecoder.put(28, '1');
		charDecoder.put(29, '2');
		charDecoder.put(30, '3');
		charDecoder.put(31, '4');
		charDecoder.put(32, '5');
		charDecoder.put(33, '6');
		charDecoder.put(34, '7');
		charDecoder.put(35, '8');
		charDecoder.put(36, '9');
		charDecoder.put(37, ',');
		charDecoder.put(38, '.');
		charDecoder.put(39, '?');
		charDecoder.put(40, '!');
		charDecoder.put(41, '\'');
		charDecoder.put(42, '(');
		charDecoder.put((NUM_ENCODED_CHARACTERS - 1), ')');
	}
	
	public static void extractInputSentence(String encodeString) {
		// Clear the input layer first
		clearInputLayer();
		clearDesiredOutputLayer();
		
		// This method will encode the input layer and do the same for the desired output layer
		for(int i = 0; i < MAX_SENTENCE_LENGTH; i ++) {
			// No need to use unsquished activation this since one hot encoding uses binary representation
			inputLayer[i].activation = (float) charEncoder.get(encodeString.charAt(i)) / (NUM_ENCODED_CHARACTERS - 1);
			desiredOutput[i] = (float) charEncoder.get(encodeString.charAt(i)) / (NUM_ENCODED_CHARACTERS - 1);
		}
	}
	
	public static void clearInputLayer() {
		for(int i = 0; i < inputLayer.length; i ++) {
			inputLayer[i].activation = 0;
		}
	}
	
	public static void clearDesiredOutputLayer() {
		for(int i = 0; i < desiredOutput.length; i ++) {
			desiredOutput[i] = 0;
		}
	}
	
	public static void initialize(boolean isFound) {
		// Assign IDs to the chars we are using
		encodeChars();
		
		// Initialize all of the node vectors, weight matrices, and bias vectors
		inputLayer = new Node[INPUT_LAYER_NODES];
		hiddenEncodingLayer1 = new Node[LARGE_HIDDEN_LAYER_NODES];
		hiddenEncodingLayer2 = new Node[SMALL_HIDDEN_LAYER_NODES];
		bottleneckLayer = new Node[BOTTLENECK_LAYER_NODES];
		hiddenDecodingLayer1 = new Node[SMALL_HIDDEN_LAYER_NODES];
		hiddenDecodingLayer2 = new Node[LARGE_HIDDEN_LAYER_NODES];
		outputLayer = new Node[OUTPUT_LAYER_NODES];
		desiredOutput = new float[OUTPUT_LAYER_NODES];
		
		weightMatrix1 = new float[LARGE_HIDDEN_LAYER_NODES][INPUT_LAYER_NODES];
		weightMatrix2 = new float[SMALL_HIDDEN_LAYER_NODES][LARGE_HIDDEN_LAYER_NODES];
		weightMatrix3 = new float[BOTTLENECK_LAYER_NODES][SMALL_HIDDEN_LAYER_NODES];
		weightMatrix4 = new float[SMALL_HIDDEN_LAYER_NODES][BOTTLENECK_LAYER_NODES];
		weightMatrix5 = new float[LARGE_HIDDEN_LAYER_NODES][SMALL_HIDDEN_LAYER_NODES];
		weightMatrix6 = new float[OUTPUT_LAYER_NODES][LARGE_HIDDEN_LAYER_NODES];
		
		stochasticPartialDerivativeWeightMatrix1 = new float[LARGE_HIDDEN_LAYER_NODES][INPUT_LAYER_NODES];
		stochasticPartialDerivativeWeightMatrix2 = new float[SMALL_HIDDEN_LAYER_NODES][LARGE_HIDDEN_LAYER_NODES];
		stochasticPartialDerivativeWeightMatrix3 = new float[BOTTLENECK_LAYER_NODES][SMALL_HIDDEN_LAYER_NODES];
		stochasticPartialDerivativeWeightMatrix4 = new float[SMALL_HIDDEN_LAYER_NODES][BOTTLENECK_LAYER_NODES];
		stochasticPartialDerivativeWeightMatrix5 = new float[LARGE_HIDDEN_LAYER_NODES][SMALL_HIDDEN_LAYER_NODES];
		stochasticPartialDerivativeWeightMatrix6 = new float[OUTPUT_LAYER_NODES][LARGE_HIDDEN_LAYER_NODES];
		
		biasVector1 = new float[LARGE_HIDDEN_LAYER_NODES];
		biasVector2 = new float[SMALL_HIDDEN_LAYER_NODES];
		biasVector3 = new float[BOTTLENECK_LAYER_NODES];
		biasVector4 = new float[SMALL_HIDDEN_LAYER_NODES];
		biasVector5 = new float[LARGE_HIDDEN_LAYER_NODES];
		biasVector6 = new float[OUTPUT_LAYER_NODES];
		
		stochasticPartialDerivativeBiasVector1 = new float[LARGE_HIDDEN_LAYER_NODES];
		stochasticPartialDerivativeBiasVector2 = new float[SMALL_HIDDEN_LAYER_NODES];
		stochasticPartialDerivativeBiasVector3 = new float[BOTTLENECK_LAYER_NODES];
		stochasticPartialDerivativeBiasVector4 = new float[SMALL_HIDDEN_LAYER_NODES];
		stochasticPartialDerivativeBiasVector5 = new float[LARGE_HIDDEN_LAYER_NODES];
		stochasticPartialDerivativeBiasVector6 = new float[OUTPUT_LAYER_NODES];
		
		// Initialize nodes in each layer
		initializeNodeLayer(inputLayer);
		initializeNodeLayer(hiddenEncodingLayer1);
		initializeNodeLayer(hiddenEncodingLayer2);
		initializeNodeLayer(bottleneckLayer);
		initializeNodeLayer(hiddenDecodingLayer1);
		initializeNodeLayer(hiddenDecodingLayer2);
		initializeNodeLayer(outputLayer);
		
		// Create the Information object for Brick
		info = new Information();
		
		// Randomly initialize everything (and set count to 0) if we've never run this plugin before
		if(!isFound) {
			// Set count to 0
			count = 0;
			
			// Randomize each weight matrix
			randomizeWeightMatrix(weightMatrix1);
			randomizeWeightMatrix(weightMatrix2);
			randomizeWeightMatrix(weightMatrix3);
			randomizeWeightMatrix(weightMatrix4);
			randomizeWeightMatrix(weightMatrix5);
			randomizeWeightMatrix(weightMatrix6);
			
			// Randomize each bias vector
			randomizeBiasVector(biasVector1);
			randomizeBiasVector(biasVector2);
			randomizeBiasVector(biasVector3);
			randomizeBiasVector(biasVector4);
			randomizeBiasVector(biasVector5);
			randomizeBiasVector(biasVector6);
			
			// Set the weight restriction to be off by default
			toggleWeightRestriction = false;
		}
	}
	
	public static void initializeNodeLayer(Node [] layer) {
		// Initialize the nodes in every array slot
		for(int i = 0; i < layer.length; i ++) {
			layer[i] = new Node();
		}
	}
	
	public static void randomizeWeightMatrix(float [][] weightMatrix) {
		// Create and randomize weight matrices (rows correspond to the node number at the end of the weight connection while columns correspond to the node number at the beginning of the weight connection
		for(int i = 0; i < weightMatrix.length; i ++) {
			for(int j = 0; j < weightMatrix[i].length; j ++) {
				weightMatrix[i][j] = (float) ((Math.random() * 2) - 1);
			}
		}
	}
	
	public static void randomizeBiasVector(float [] biasVector) {
		// Create and randomize the bias vectors with biases in the range -5 to 5
		for(int i = 0; i < biasVector.length; i ++) {
			biasVector[i] = (float) (Math.random() * 10 - 5);
		}
		
	}
}
