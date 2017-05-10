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
        proj = Lookup.getDefault().lookup(IProjectileService.class);
    }
    
    PlayerEntity playerEntity;
    EnemyEntity enemyEntity;
    double totalFitness = 0;
    float lastX, lastY;
    int playerLife;
    float xDif, yDif;
    IProjectileService proj;
    
    public void calculateFitness(GameData gameData, World world) {
        playerEntity = null;
        enemyEntity = null;

        for (Entity player : world.getEntities(PlayerEntity.class)) {
            playerEntity = (PlayerEntity) player;
            player.setX((float) (100));
            player.setY((float)136.10168f);
            player.setVelocity(0);
            player.setVerticalVelocity(0);
            player.setLife(1000);
        }

        for (Entity enemy : world.getEntities(EnemyEntity.class)) {
            enemyEntity = (EnemyEntity) enemy;
        }

        totalFitness = 0;
        
        for (int i = 0; i < 20; i++) {
            proj.aiEnemyshoot(gameData, world, enemyEntity, playerEntity, genes[0], genes[1], genes[2]);


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
                xDif = Math.abs(lastX - playerEntity.getX());
                fitness = 1 - (xDif / 300.0f);
                fitness = Math.max(0.01, fitness);
            }
            totalFitness += fitness;
            playerEntity.setX(playerEntity.getX() + 40);
        }
        fitness = totalFitness / 20.0f;
    }

    public boolean mutate() {
        if (Math.random() < mutationRate) {
            return true;
        }

        return false;
    }

    double[] newGenes = new double[3];
    public DNA crossover(DNA partner) {
        for (int i = 0; i < newGenes.length; i++) {
                newGenes[i] = genes[i] + (partner.genes[i] - genes[i]) * Math.random();
        }

        DNA child = new DNA(new double[]{newGenes[0], newGenes[1], newGenes[2]});

        return child;
    }

    public double getFitness() {
        return Math.max(0.01, fitness);
    }
}
