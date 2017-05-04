package group04.core.AI;

import group04.common.Entity;
import group04.common.GameData;
import group04.common.World;
import group04.common.services.IServiceInitializer;
import group04.common.services.IServiceProcessor;
import group04.enemycommon.EnemyEntity;
import group04.movementcommon.IMovementService;
import group04.playercommon.PlayerEntity;
import group04.projectilecommon.IProjectileService;
import group04.projectilecommon.ProjectileEntity;
import java.util.Random;
import org.openide.util.Lookup;

/**
 *
 * @author Mathias
 */
public class DNA {

    private double mutationRate = 0.05;
    private double fitness;
    private Random rand = new Random();
    private double[] genes = new double[8];

    public double[] getGenes() {
        return genes;
    }

    public void setGenes(double[] genes) {
        this.genes = genes;
    }

    public DNA(double[] genes) {
        this.genes = genes;
    }
    PlayerEntity playerEntity;
    EnemyEntity enemyEntity;
    double totalFitness = 0;
    float lastX, lastY;
    int playerLife;
    float xDif, yDif;
    
    public void calculateFitness(GameData gameData, World world) {
        playerEntity = null;
        enemyEntity = null;

        for (Entity player : world.getEntities(PlayerEntity.class)) {
            playerEntity = (PlayerEntity) player;
            player.setX((float) (101));
            player.setY((int) (gameData.getDisplayHeight() * 0.3));
            player.setVelocity(0);
            player.setVerticalVelocity(0);
            player.setLife(1000);
        }

        for (Entity enemy : world.getEntities(EnemyEntity.class)) {
            enemyEntity = (EnemyEntity) enemy;
        }

        totalFitness = 0;

        for (int i = 0; i < 20; i++) {
            for (IProjectileService projService : Lookup.getDefault().lookupAll(IProjectileService.class)) {
                projService.aiEnemyshoot(gameData, world, enemyEntity, playerEntity, genes[0], genes[1], genes[2], genes[3], genes[4], genes[5], genes[6], genes[7]);
            }

            lastX = 0;
            lastY = 0;
            playerLife = playerEntity.getLife();

            while (world.getEntities(ProjectileEntity.class).size() == 1) {
                for (Entity projectile : world.getEntities(ProjectileEntity.class)) {
                    lastX = projectile.getX();
                    lastY = projectile.getY();
                }

                for (IMovementService e : Lookup.getDefault().lookupAll(IMovementService.class)) {
                    e.process(gameData, world);
                }

                for (IServiceProcessor processors : Lookup.getDefault().lookupAll(IServiceProcessor.class)) {
                    processors.process(gameData, world);
                }
            }

            if (playerLife != playerEntity.getLife()) {
                fitness = 1;
            } else {
                xDif = lastX - playerEntity.getX();
                yDif = lastY - playerEntity.getY();
                fitness = 1 - (Math.sqrt((xDif * xDif) + (yDif * yDif)) / 1000.0f);
                fitness = Math.max(0, fitness);
            }
            totalFitness += fitness;
            playerEntity.setX(playerEntity.getX() + 20);
        }
        fitness = totalFitness / 20.0f;
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
