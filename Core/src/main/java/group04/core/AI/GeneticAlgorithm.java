/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package group04.core.AI;

import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author Mathias
 */
public class GeneticAlgorithm {

    private ArrayList<DNA> population;
    private ArrayList<DNA> matingPool;
    private static boolean finished;
    private double breadth;
    private int generations;
    private int size;
    private Random rand;

    public GeneticAlgorithm() {
        rand = new Random();
        population = new ArrayList<>();
        matingPool = new ArrayList<>();
        breadth = 50;
        size = 100;
        generations = 0;
        finished = false;
        setup();
    }

    public void setup() {

        double[] genes = new double[8];

        for (int i = 0; i < population.size(); i++) {

            for (int j = 0; j < 8; j++) {
                genes[i] = -breadth / 2 + breadth * rand.nextDouble();
            }

            population.add(new DNA(genes));
        }
    }

    public void calculateFitness() {
        for (int i = 0; i < population.size(); i++) {
            population.get(i).fitness();
        }
    }

    public void createMatingPool() {
        for (int i = 0; i < size; i++) {

            //n is the probability between 0 and 100
            int n = (int) (population.get(i).getFitness() * population.size());

            //add to mating pool
            for (int j = 0; j < n; j++) {
                matingPool.add(population.get(i));
            }

        }
    }

    public void reproduction() {

        population.clear();

        for (int i = 0; i < size; i++) {

            int a = rand.nextInt(matingPool.size());
            int b = rand.nextInt(matingPool.size());

            DNA parentA = matingPool.get(a);
            DNA parentB = matingPool.get(b);

            DNA child = parentA.crossover(parentB);

            if (child.mutate()) {

                double[] mutatedGenes = new double[8];

                for (int j = 0; j < 8; j++) {
                    mutatedGenes[i] = -breadth / 2 + breadth * rand.nextDouble();
                }

                DNA mutatedChild = new DNA(mutatedGenes);

                population.add(mutatedChild);
            } else {
                population.add(child);
            }
        }
        generations++;
    }

    public static void main(String[] args) {
        GeneticAlgorithm gn = new GeneticAlgorithm();

        while (!finished) {
            gn.calculateFitness();
            gn.createMatingPool();
            gn.reproduction();
        }
    }

}
