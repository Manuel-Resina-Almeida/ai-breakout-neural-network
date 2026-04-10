package breakout;

import java.util.List;

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

		this.values = values;
		this.curSeed = curSeed;
		this.inputDim = inputDim;
		this.hiddenDim = hiddenDim;
		this.outputDim = outputDim;
		hiddenBiases = new double[hiddenDim];
		outputBiases = new double[outputDim];
		hiddenWeights = new double[inputDim][hiddenDim];
		outputWeights = new double[hiddenDim][outputDim];
		initializeParameters(inputDim, hiddenDim, outputDim, values);

	}

	public void changeW(double[] v) {
		initializeParameters(inputDim, hiddenDim, outputDim, v);
	}

	public NN(double i) {
		this.fitness = i;
	}

	private void initializeParameters(int inputDim2, int hiddenDim2, int outputDim2, double[] values) {

		int it = 0;
		for (int i = 0; i < inputDim; i++) {
			for (int j = 0; j < hiddenDim; j++) {

				hiddenWeights[i][j] = values[it];
				it++;
			}
		}

		for (int i = 0; i < hiddenDim; i++) {

			hiddenBiases[i] = values[it];
			it++;
		}

		for (int i = 0; i < hiddenDim; i++) {
			for (int j = 0; j < outputDim; j++) {

				outputWeights[i][j] = values[it];
				it++;
			}
		}

		for (int i = 0; i < outputDim; i++) {
			outputBiases[i] = values[it];
			it++;
		}

	}

	public double[] forward(int[] inputValues) {

		double[] hiddenLayer = new double[hiddenDim];
		for (int i = 0; i < hiddenDim; i++) {
			hiddenLayer[i] = 0.0;
			for (int j = 0; j < inputDim; j++) {

				hiddenLayer[i] += inputValues[j] * hiddenWeights[j][i];

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

		if (result[1] > result[0]) {

			return 2;
		}

		return 1;

	}

	public double getFitness() {
		return fitness;
	}

	public void setFitness(double i) {
		this.fitness = i;
	}

	public double[] getWeights() {
		// TODO Auto-generated method stub
		return values;
	}

	public static String toStringX(NN x) {
		return "fitness" + x.fitness;
	}

	public static String toStringW(NN x) {
		double[] w = x.getWeights();
		String r = "";
		for (int i = 0; i < w.length; i++) {
			r += w[i] + ", ";
		}

		return r;
	}

	public static NN getBest(List<NN> newPop) {
		NN n = new NN(0);
		for (int i = 0; i < newPop.size(); i++) {
			if (newPop.get(i).getFitness() > n.getFitness()) {
				n = newPop.get(i);
			}
		}
		return n;
	}

	public int getCurSeed() {
		return curSeed;
	}

}
