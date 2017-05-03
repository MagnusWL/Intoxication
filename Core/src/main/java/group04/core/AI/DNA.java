package group04.core.AI;

import java.util.Random;

/**
 *
 * @author Mathias
 */
public class DNA {

    private double mutationRate = 0.05;
    private double fitness;
    private Random rand = new Random();
    private double[] genes = new double[8];

    public DNA(double[] genes) {
        this.genes = genes;
    }

    public void fitness() {

    }

    public boolean mutate() {
        if (Math.random() < mutationRate) {
            return true;
        }

        return false;
    }

    public DNA crossover(DNA partner) {

        double[] newGenes = new double[8];

        for (int i = 0; i < newGenes.length; i++) {
            if (rand.nextBoolean()) {
                newGenes[i] = partner.genes[i];
            } else {
                newGenes[i] = genes[i];
            }

        }

        DNA child = new DNA(newGenes);

        return child;
    }

    public double getFitness() {
        return fitness;
    }

}
