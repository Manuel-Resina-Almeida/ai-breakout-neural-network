package breakout;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GeneticAlgorithmBreakout {

	private int PopSize;
	private int GenCount = 0;
	private int threshold = 500;

	private List<NN> population;

	private int hiddenDim;
	private int inputDim = utils.Commons.BREAKOUT_STATE_SIZE;
	private int outputDim = utils.Commons.BREAKOUT_NUM_ACTIONS;

	private int num_w;
	private double mut_rate = 0.05;
	private double sel_rate = 0.2;
	private Random rand;

	private double alpha = -1;
	private double beta = 1;

	private boolean t;
	private int r;
	private int j;

	private int swap_rate = 1;

	private int curSeed;

	private NN best;

	public GeneticAlgorithmBreakout(int hiddenDim, int PopSize, int seed) {
		curSeed = seed;
		rand = new Random(curSeed);
		this.best = new NN(0);
		this.PopSize = PopSize;
		this.hiddenDim = hiddenDim;
		this.num_w = (this.inputDim * hiddenDim) + hiddenDim + (hiddenDim * this.outputDim) + this.outputDim;
		population = new ArrayList<>();

		this.population = genFirstPop();
		

		r = 0;
		j = r + 20;
		t = false;

		for (GenCount = 0; GenCount < threshold; GenCount++) {
//			if (GenCount % 200 == 0) {
//				curSeed = rand.nextInt();
//			}
			System.out.println(GenCount);

			for (int i = 0; i < PopSize; i++) {
				BreakoutBoard b = new BreakoutBoard(population.get(i), false, curSeed);
				b.setSeed(curSeed);
				b.runSimulation();
				population.get(i).setFitness(b.getFitness());
				if (best.getFitness() < b.getFitness()) {
					best = population.get(i);
					t = true;
				}

			}

			System.out.println(best.getFitness());

			this.population = filter();

			 keepTrack();

			this.population = evolve();

		}
		Breakout gg = new Breakout(best, best.getCurSeed());

	}

	private List<NN> evolve() {
		Random r = new Random();
		population.sort((a, b) -> (int) (b.getFitness() - a.getFitness()));
		List<NN> newPop = new ArrayList<>();
		for (int i = 0; i < 5; i++) {
			newPop.add(population.get(i));
		}

		for (int i = 0; i < PopSize; i++) {
			NN x = new NN(inputDim, hiddenDim, outputDim,
					population.get(r.nextInt((int) (PopSize * sel_rate) - 1)).getWeights(), curSeed);
			NN y = new NN(inputDim, hiddenDim, outputDim,
					population.get(r.nextInt((int) (PopSize * sel_rate) - 1)).getWeights(), curSeed);
			NN child = reproduce(x, y);
			if (r.nextDouble() <= mut_rate) {
				child = mutate(child);
			}
			newPop.add(child);
		}

		return newPop;

	}

	private List<NN> filter() {
		List<NN> newPop = new ArrayList<>();
		population.sort((a, b) -> (int) (b.getFitness() - a.getFitness()));
		for (int i = 0; i < PopSize; i++) {
			newPop.add(population.get(i));
		}
		return newPop;
	}

	public List<NN> genFirstPop() {
		List<NN> firstPop = new ArrayList<>();
		for (int i = 0; i < PopSize; i++) {
			double[] weights = generateWeights();

			NN ind = new NN(this.inputDim, this.hiddenDim, this.outputDim, weights, curSeed);

			firstPop.add(ind);

		}

		return firstPop;

	}

	private NN reproduce(NN x, NN y) {
		Random r = new Random();
		double[] weights1 = x.getWeights();
		double[] weights2 = y.getWeights();
		double[] child1 = new double[num_w];

		int cross = r.nextInt((int) (weights1.length) - 1);

		for (int i = 0; i < cross; i++) {
			child1[i] = weights1[i];
		}

		for (int i = cross; i < num_w; i++) {
			child1[i] = weights2[i];
		}

		return new NN(inputDim, hiddenDim, outputDim, child1, curSeed);

	}

	private NN mutate(NN child) {
		Random r = new Random();
		int swap = swap_rate;
		double[] w = child.getWeights();
		for (int i = 0; i < swap; i++) {
			int pos = r.nextInt(num_w - 1);
			w[pos] = w[pos] + (alpha + r.nextDouble() * (beta - alpha));
		}
		return new NN(inputDim, hiddenDim, outputDim, w, curSeed);
	}

	private void keepTrack() {
		if (t) {
			t = false;
			r = 0;
			j = r + 20;
			// swap_rate = 1;
			mut_rate *=0.99;
			swap_rate-=1;
		} else {
			if (r == j) {
				// swap_rate += 1;
				mut_rate *= 1.01;
				swap_rate+=1;
				j = r + 20;
				System.out.println(mut_rate);
			}
			r++;
		}
		
		mut_rate = Math.max(0.01, Math.min(mut_rate, 0.1));
	    swap_rate = Math.max(1, Math.min(swap_rate, 10));
		System.out.println(r + "   " + "   " + j);

	}

	public double[] generateWeights() {
		Random r = new Random();
		double[] weights = new double[num_w];

		for (int i = 0; i < num_w; i++) {
			weights[i] = (r.nextDouble() * 2 - 1);

		}
		return weights;

	}

}
