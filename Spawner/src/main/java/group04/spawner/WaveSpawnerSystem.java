/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package group04.spawner;

import group04.common.Entity;
import group04.common.EntityType;
import group04.common.GameData;
import group04.common.World;
import group04.common.services.IServiceInitializer;
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

    private void createWaveSpawner(GameData gameData, World world) {
        WaveSpawnerEntity waveSpawner = new WaveSpawnerEntity();
        waveSpawner.setEntityType(EntityType.WAVE_SPAWNER);

        waveSpawner.setSpawnTimerMax(600);
        waveSpawner.setMobsSpawnedMax(5);
        waveSpawner.setSpawnDuration(500);

        world.addEntity(waveSpawner);
    }

    @Override
    public void spawner(GameData gameData, World world, WaveSpawnerEntity waveSpawner) {

        waveSpawner.setSpawnTimer((int) (waveSpawner.getSpawnTimer() + 60 * gameData.getDelta()));

        if (waveSpawner.getSpawnTimer() > waveSpawner.getSpawnTimerMax()) {
            int timePerMob = waveSpawner.getSpawnDuration() / waveSpawner.getMobsSpawnedMax();

            if (waveSpawner.getSpawnTimer() - waveSpawner.getSpawnTimerMax() > timePerMob * waveSpawner.getMobsSpawned()) {
                waveSpawner.setMobsSpawned(waveSpawner.getMobsSpawned() + 1);
                for (IEnemyService e : Lookup.getDefault().lookupAll(IEnemyService.class)) {
                    e.createEnemy(gameData, world, (int) (gameData.getTileSize() * gameData.getMapWidth() * 0.95), (int) (gameData.getDisplayHeight() * 0.15));
                }
            }

            if (waveSpawner.getSpawnTimer() > waveSpawner.getSpawnTimerMax() + waveSpawner.getSpawnDuration()) {
                waveSpawner.setSpawnTimer(0);
                waveSpawner.setMobsSpawned(0);
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
