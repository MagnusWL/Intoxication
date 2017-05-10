/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package group04.spawner;

import group04.common.Entity;
import group04.common.GameData;
import group04.common.World;
import group04.common.services.IServiceInitializer;
import group04.enemycommon.EnemyEntity;
import group04.enemycommon.EnemyType;
import group04.enemycommon.IEnemyService;
import group04.spawnercommon.ISpawnerService;
import group04.spawnercommon.WaveSpawnerEntity;
import org.openide.util.Lookup;
import org.openide.util.lookup.ServiceProvider;
import org.openide.util.lookup.ServiceProviders;

/**
 *
 * @author Michael-PC
 */
@ServiceProviders(value = {
    @ServiceProvider(service = ISpawnerService.class),
    @ServiceProvider(service = IServiceInitializer.class)
})

public class WaveSpawnerSystem implements ISpawnerService, IServiceInitializer {

    private double portalOneSpawn = 0.95;
    private double portalTwoSpawnX = 0.85;
    private double portalTwoSpawnY = 0.5;
    private double portalThreeSpawn = 0.72;
    
    private double bossSpawnHeight = 0.6;
    
    private void createWaveSpawner(GameData gameData, World world) {
        WaveSpawnerEntity waveSpawner = new WaveSpawnerEntity();
        waveSpawner.setSpawnTimerMax(600);
        waveSpawner.setMobsSpawnedMax(1);
        waveSpawner.setSpawnDuration(100);
        waveSpawner.setCurrentLevel(1);
        world.addEntity(waveSpawner);
    }

    @Override
    public void spawner(GameData gameData, World world, WaveSpawnerEntity waveSpawner) {
        waveSpawner.setSpawnTimer((int) (waveSpawner.getSpawnTimer() + 60 * gameData.getDelta()));
        if (waveSpawner.getSpawnTimer() > waveSpawner.getSpawnTimerMax()) {
            int timePerMob = waveSpawner.getSpawnDuration() / waveSpawner.getMobsSpawnedMax();

            if (waveSpawner.getSpawnTimer() - waveSpawner.getSpawnTimerMax() > timePerMob * waveSpawner.getMobsSpawned() && waveSpawner.getMobsSpawned() < waveSpawner.getMobsSpawnedMax()) {
                waveSpawner.setMobsSpawned(waveSpawner.getMobsSpawned() + 1);
                for (IEnemyService e : Lookup.getDefault().lookupAll(IEnemyService.class)) {
                    switch (waveSpawner.getCurrentLevel()) {
                        case 1: // level 1:
                            e.createEnemy(gameData, world, (int) (gameData.getTileSize() * gameData.getMapWidth() * portalOneSpawn-0.10), (int) (gameData.getDisplayHeight() * bossSpawnHeight), EnemyType.BOSS, waveSpawner.getCurrentLevel());
                            break;
                        case 2: // level 2:
                            waveSpawner.setMobsSpawnedMax(10);
                            waveSpawner.setSpawnDuration(200);
                            e.createEnemy(gameData, world, (int) (gameData.getTileSize() * gameData.getMapWidth() * portalThreeSpawn), (int) (gameData.getDisplayHeight() * 0.25), EnemyType.BEER, waveSpawner.getCurrentLevel());
                            break;
                        case 3: //level 3:
                            waveSpawner.setMobsSpawnedMax(10);
                            waveSpawner.setSpawnDuration(1000);
                            e.createEnemy(gameData, world, (int) (gameData.getTileSize() * gameData.getMapWidth() * portalThreeSpawn), (int) (gameData.getDisplayHeight() * 0.25), EnemyType.NARKO, waveSpawner.getCurrentLevel());
                            e.createEnemy(gameData, world, (int) (gameData.getTileSize() * gameData.getMapWidth() * portalOneSpawn), (int) (gameData.getDisplayHeight() * 0.25), EnemyType.BEER, waveSpawner.getCurrentLevel());
                            break; 
                        //Insert more levels here
                    }
                }
            }

            boolean enemiesLeft = false;
            for (Entity enemy : world.getEntities(EnemyEntity.class)) {
                enemiesLeft = true;
                break;
            }

            if (waveSpawner.getSpawnTimer() > waveSpawner.getSpawnTimerMax() + waveSpawner.getSpawnDuration() && !enemiesLeft) {
                waveSpawner.setSpawnTimer(0);
                waveSpawner.setMobsSpawned(0);
                waveSpawner.setCurrentLevel(waveSpawner.getCurrentLevel() + 1);
            }
        }
    }

    @Override
    public void start(GameData gameData, World world) {
        createWaveSpawner(gameData, world);
    }

    @Override
    public void stop(GameData gameData, World world) {
    }
}
