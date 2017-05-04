/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package group04.core.AI;

import group04.common.GameData;
import group04.common.World;
import group04.common.services.IServiceInitializer;
import group04.movementcommon.IMovementService;
import java.util.ArrayList;
import java.util.Random;
import org.openide.util.Lookup;

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

        for (int i = 0; i < size; i++) {

            for (int j = 0; j < genes.length; j++) {
                genes[j] = -breadth / 2 + breadth * rand.nextDouble();
            }

            population.add(new DNA(genes));
        }
    }

    public void calculateFitness(GameData gameData, World world) {
        System.out.println(matingPool.size());
        double avg = 0;
        double maxFitness = 0;
        double[] bestGenes = new double[8];
        for (int i = 0; i < population.size(); i++) {
            population.get(i).calculateFitness(gameData, world);
            if(population.get(i).getFitness() > maxFitness)
            {
                maxFitness = population.get(i).getFitness();
                bestGenes = population.get(i).getGenes();
            }
            avg += population.get(i).getFitness();
        }
        
        System.out.println("Avg Fitness: " + avg/population.size() );
        System.out.println("Best Fitness: " + maxFitness + ":" + bestGenes[0] + ":" + bestGenes[1] + ":" + bestGenes[2] + ":" + bestGenes[3] + ":" + bestGenes[4] + ":" + bestGenes[5] + ":" + bestGenes[6] + ":" + bestGenes[7]);
    }

    public void createMatingPool() {
        matingPool.clear();
        
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
                    mutatedGenes[j] = -breadth / 2 + breadth * rand.nextDouble();
                }

                DNA mutatedChild = new DNA(mutatedGenes);

                population.add(mutatedChild);
            } else {
                population.add(child);
            }
        }
        generations++;
    }

    public static void start(GameData gameData, World world) {
        GeneticAlgorithm gn = new GeneticAlgorithm();

        for (int i = 0; i < 1000; i++) {
            gn.calculateFitness(gameData, world);
            gn.createMatingPool();
            gn.reproduction();
        }
    }

    public static void main(String[] args) {

    }
}
