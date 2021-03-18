package me.Brick.TurtleCamera.AutoEncoder;

public class Information {
	public String inputString; // Stores the sentence we want to encode
	public float [] code; // Stores the encoded input sentence
	public String outputString; // Stores the sentence that was decoded
	public float cost; // The cost of the current image displayed
	public int numCorrect; // Stores the number of characters in the sentence guessed correctly

	public Information() {
		inputString = null;
		code = new float[Auto_Encoder.BOTTLENECK_LAYER_NODES];
		outputString = null;
		cost = 0;
		numCorrect = 0;
	}
}
