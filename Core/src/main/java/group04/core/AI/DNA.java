package group04.core.AI;

import group04.common.Entity;
import group04.enemycommon.EnemyEntity;
import group04.playercommon.PlayerEntity;
import group04.projectilecommon.ProjectileEntity;
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
        
        Entity enemy = new EnemyEntity();
        enemy.setX(0);
        enemy.setY(0);
        
        Entity player = new PlayerEntity();
        player.setX(rand.nextFloat() * 1000);
        player.setY(rand.nextFloat() * 100);
        player.setVelocity(- 1 + rand.nextFloat() * 2);
        player.setVerticalVelocity(- 1 + rand.nextFloat() * 2);

        Entity projectile = new ProjectileEntity();
        
        fitness = 0.5;
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
