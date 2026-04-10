# Breakout AI: Neural Network & Genetic Algorithm

## Project Overview
This project was developed for the Artificial Intelligence course at ISCTE-IUL (2023/24). The primary objective was to explore machine learning techniques by evolving a feed-forward neural network to autonomously play a simplified version of the classic arcade game Breakout[cite: 2]. The network's performance was optimized using a custom-built genetic algorithm[cite: 2].

## Key Technical Features
* **Feed-Forward Neural Network:** Designed to receive the game state information as input (including the board position, the position and direction of the ball, and the location of the block) and produce the player's actions (move left or right) as output[cite: 2].
* **Genetic Algorithm:** Implements an evolutionary process to optimize the neural network's weights[cite: 2]. The algorithm utilizes mechanisms such as selection, mutation, and crossover across multiple generations to progressively improve the controller's performance[cite: 2].
* **Fitness Evaluation:** Utilizes a defined fitness function to evaluate and score the network's performance in the game environment[cite: 2].
* **GameController Interface:** The AI integrates seamlessly with the game engine by implementing the `GameController` interface, calculating the next best move based on the current state array[cite: 2].
* **Headless Training:** Supports running the game loop in headless mode (`runSimulation`) without the graphical interface, allowing for significantly faster evaluation of the generated AI populations during the training phase[cite: 2].

## Repository Structure
* **/src:** Contains the Java source code, including the neural network architecture, genetic algorithm logic, and the integration with the `BreakoutBoard`[cite: 2].
* **/images:** Contains the graphical assets required to run the game simulation with the visual GUI enabled.

## How to Run
The project allows evaluating a specific neural network controller visually or training a population headlessly. 

To visualize the behavior of a specific trained network using the graphical interface, initialize the game passing the network and a seed[cite: 2]:
```java
new Breakout(network, seed);
```
To run a fast, headless evaluation for training purposes[cite: 2]:
```java
BreakoutBoard b = new BreakoutBoard(network, false, seed);
b.setSeed(i);
b.runSimulation();
double fitness = b.getFitness();
```
