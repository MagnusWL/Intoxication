package group04.core.AI;

import group04.common.GameData;
import group04.common.World;
import java.util.ArrayList;
import java.util.Random;

public class GeneticAlgorithm {

    private ArrayList<DNA> population;
    private ArrayList<DNA> matingPool;
    private static boolean finished;
    private double breadth;
    private int generations;
    private int size;
    private Random rand;

    public GeneticAlgorithm(GameData gameData, World world) {
        rand = new Random();
        population = new ArrayList<>();
        matingPool = new ArrayList<>();
        breadth = 50;
        size = 100;
        generations = 0;
        finished = false;
        setup();
        startAlgorithm(gameData, world);
    }
    
    public void startAlgorithm(GameData gameData, World world)
    {
        while(maxFitness < 1) {
            calculateFitness(gameData, world);
            createMatingPool();
            reproduction();
        }
    }

    public void setup() {
        for (int i = 0; i < size; i++) {
            double[] genes = new double[2];
            for (int j = 0; j < genes.length; j++) {
                genes[j] = -breadth / 2 + breadth * rand.nextDouble();
            }
            population.add(new DNA(genes));
        }
    }
    double maxFitness;
    public void calculateFitness(GameData gameData, World world) {
        double avg = 0;
        maxFitness = 0;
        double[] bestGenes = new double[2];
        for (int i = 0; i < population.size(); i++) {
                population.get(i).calculateFitness(gameData, world);
            if(population.get(i).getFitness() > maxFitness)
            {
                maxFitness = population.get(i).getFitness();
                bestGenes = population.get(i).getGenes();
            }
            avg += population.get(i).getFitness();
        }
        
        System.out.println("Avg Fitness: \n" + avg/population.size() );
        System.out.println("Best Fitness: \n" + maxFitness);
    }

    public void createMatingPool() {
        matingPool.clear();
        double totalProbability = 0;
        for (int i = 0; i < size; i++) {
            totalProbability += getProbability(population.get(i).getFitness());            
        }

        for (int i = 0; i < size; i++) {
            double actualProbability = getProbability(population.get(i).getFitness())/totalProbability;
            
            int n = (int)(Math.round(actualProbability * (double)size));

            for (int j = 0; j < n; j++) {   
                matingPool.add(population.get(i));
            }
        }
    }
    
    public double getProbability(double fitness)
    {
        return Math.pow(fitness, 8) * 100;
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

                double[] mutatedGenes = new double[2];

                for (int j = 0; j < mutatedGenes.length; j++) {
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
        GeneticAlgorithm gn = new GeneticAlgorithm(gameData, world);
    }

    public static void main(String[] args) {

    }
}
