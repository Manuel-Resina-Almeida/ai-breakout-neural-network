package pacman;

import java.util.List;
import java.util.Random;

import utils.GameController;

public class NN implements GameController {

	private int inputDim;
	private int hiddenDim;
	private int outputDim;
	private double[][] hiddenWeights;
	private double[] hiddenBiases;
	private double[][] outputWeights;
	private double[] outputBiases;
	public double[] result;

	private double[] values;
	private double fitness;

	private int curSeed;

	public NN(int inputDim, int hiddenDim, int outputDim, double[] values, int curSeed) {
		// To do
		this.values = values;
		this.setCurSeed(curSeed);
		this.inputDim = inputDim;
		this.hiddenDim = hiddenDim;
		this.outputDim = outputDim;
		hiddenBiases = new double[hiddenDim];
		outputBiases = new double[outputDim];
		hiddenWeights = new double[inputDim][hiddenDim];
		outputWeights = new double[hiddenDim][outputDim];
		initializeParameters(inputDim, hiddenDim, outputDim, values);

	}

	public NN(double fit) {
		this.fitness = fit;
	}

	private void initializeParameters(int inputDim2, int hiddenDim2, int outputDim2, double[] values) {
		// System.out.println(values.length);
		int it = 0;
		for (int i = 0; i < inputDim; i++) {
			for (int j = 0; j < hiddenDim; j++) {
				// System.out.println(it);
				hiddenWeights[i][j] = values[it];
				it++;
			}
		}
		// System.out.println(it);
		for (int i = 0; i < hiddenDim; i++) {
			// System.out.println(it);
			hiddenBiases[i] = values[it];
			it++;
		}
		// System.out.println(it);
		for (int i = 0; i < hiddenDim; i++) {
			for (int j = 0; j < outputDim; j++) {
				// System.out.println(it);
				outputWeights[i][j] = values[it];
				it++;
			}
		}
		// System.out.println(it);

		for (int i = 0; i < outputDim; i++) {
			outputBiases[i] = values[it];
			it++;
		}

	}

	public double[] forward(int[] inputValues) {
//			toStringI(inputValues);
		double[] hiddenLayer = new double[hiddenDim];
		for (int i = 0; i < hiddenDim; i++) {
			hiddenLayer[i] = 0.0;
			for (int j = 0; j < inputDim; j++) {
				// System.out.println("1 " + inputValues[j] + " " + hiddenWeights[j][i]);
				hiddenLayer[i] += inputValues[j] * hiddenWeights[j][i];
				// System.out.println("2 " + " " + hiddenLayer[i]);

			}
			hiddenLayer[i] = sigmoid(hiddenLayer[i] + hiddenBiases[i]);
		}
		double[] outputLayer = new double[outputDim];
		for (int i = 0; i < outputDim; i++) {
			outputLayer[i] = 0.0;
			for (int j = 0; j < hiddenDim; j++) {
				outputLayer[i] += hiddenLayer[j] * outputWeights[j][i];
			}
			outputLayer[i] = sigmoid(outputLayer[i] + outputBiases[i]);
		}
		return outputLayer;

	}

	public double sigmoid(double x) {
		return 1 / (1 + Math.exp(-x));
	}

	@Override
	public int nextMove(int[] currentState) {

		double[] result = forward(currentState);
		this.result = result;

		int finalOutputNode = 0;
		double maxProbability = -1.0;

		for (int i = 0; i < result.length; i++) {
			if (result[i] > maxProbability) {
				maxProbability = result[i];
				finalOutputNode = i;
			}
		}

		switch (finalOutputNode) {
		case 0:
			return 3;
		case 1:
			return 4;
		case 2:
			return 1;
		case 3:
			return 2;
		}
		return 0;
	}

	public double getFitness() {
		return fitness;
	}

	public void setFitness(double fit) {

		this.fitness = fit;
	}

	public double[] getWeights() {
		// TODO Auto-generated method stub
		return values;
	}

	public void setCurSeed(int curSeed) {
		this.curSeed = curSeed;
	}

}
