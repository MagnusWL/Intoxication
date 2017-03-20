package group04.enemy;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.openide.util.lookup.ServiceProvider;
import org.openide.util.lookup.ServiceProviders;
import group04.common.Entity;
import group04.common.EntityType;
import group04.common.GameData;
import group04.common.World;
import group04.common.events.Event;
import group04.common.events.EventType;
import group04.common.services.IServiceInitializer;
import group04.common.services.IServiceProcessor;

@ServiceProviders(value = {
    @ServiceProvider(service = IServiceInitializer.class),
    @ServiceProvider(service = IServiceProcessor.class)})

public class EnemySystem implements IServiceProcessor, IServiceInitializer {

    private List<Entity> enemies = new ArrayList<>();
    private final Random rand = new Random();

    private void dropItem(Entity drop, Entity enemy, World world, GameData gameData, EventType type) {
        world.addEntity(drop);
        gameData.addEvent(new Event(type, drop.getID()));
        drop.setX(enemy.getX());
        drop.setY(enemy.getY());
    }

    @Override
    public void process(GameData gameData, World world) {
        for (Entity entity : world.getEntities(EntityType.WAVE_SPAWNER)) {

            entity.setSpawnTimer(entity.getSpawnTimer() + 1);

            if (entity.getSpawnTimer() > entity.getSpawnTimerMax()) {
                int timePerMob = entity.getSpawnDuration() / entity.getMobsSpawnedMax();

                if (entity.getSpawnTimer() - entity.getSpawnTimerMax() > timePerMob * entity.getMobsSpawned()) {
                    entity.setMobsSpawned(entity.getMobsSpawned() + 1);
                    createEnemy(gameData, world, (int) (gameData.getTileSize() * gameData.getMapWidth() * 0.95), (int) (gameData.getDisplayHeight() * 0.15));
                }

                if (entity.getSpawnTimer() > entity.getSpawnTimerMax() + entity.getSpawnDuration()) {
                    entity.setSpawnTimer(0);
                    entity.setMobsSpawned(0);
                }
            }
        }

        for (Entity entity : world.getEntities(EntityType.ENEMY)) {
            float distancePlayer = Float.MAX_VALUE;
            float distanceBase = Float.MAX_VALUE;
            for (Entity player : world.getEntities(EntityType.PLAYER)) {
                distancePlayer = Math.abs(player.getX() - entity.getX());
            }
            for (Entity base : world.getEntities(EntityType.BASE)) {
                distanceBase = Math.abs(base.getX() - entity.getX());
            }

            if (distancePlayer > distanceBase) {
                movementDecision(entity, EntityType.BASE, world);
            } else {
                movementDecision(entity, EntityType.PLAYER, world);
            }

            if (rand.nextFloat() > 0.99f) {
                if (entity.isGrounded()) {
                    entity.setVerticalVelocity(entity.getJumpSpeed());
                }
            }

            for (Event e : gameData.getAllEvents()) {
                if (e.getType() == EventType.ENTITY_HIT && e.getEntityID().equals(entity.getID())) {

                    entity.setLife(entity.getLife() - 1);
                    
                    
                    //ENEMY DIES
                    if (entity.getLife() <= 0) {
                        world.removeEntity(world.getEntity(entity.getWeaponOwned()));
                        world.removeEntity(entity);

                        Entity currency = new Entity();
                        Entity boost = new Entity();
                        
                        dropItem(currency, entity, world, gameData, EventType.DROP_CURRENCY);
                        dropItem(boost, entity, world, gameData, EventType.DROP_BOOST);
//                        
                    }
                    gameData.removeEvent(e);
                }
            }
        }
    }

    @Override
    public void start(GameData gameData, World world) {
        Entity waveSpawner = new Entity();
        waveSpawner.setEntityType(EntityType.WAVE_SPAWNER);
        waveSpawner.setSpawnTimerMax(600);
        waveSpawner.setMobsSpawnedMax(5);
        waveSpawner.setSpawnDuration(300);
        world.addEntity(waveSpawner);
    }

    private Entity createEnemy(GameData gameData, World world, int x, int y) {
        Entity enemyCharacter = new Entity();

        enemyCharacter.setEntityType(EntityType.ENEMY);
        enemyCharacter.setX(x);
        enemyCharacter.setY(y);
        enemyCharacter.setHasGravity(true);
        enemyCharacter.setMaxLife(2);
        enemyCharacter.setLife(enemyCharacter.getMaxLife());
        enemyCharacter.setJumpSpeed(300);
        enemyCharacter.setMovementSpeed(85);
        enemyCharacter.setAnimateable(true);
        enemyCharacter.setCurrentAnimation("Enemy_Beer_Run");
        enemyCharacter.setSprite("Enemy_Beer");
        enemyCharacter.setShapeX(new float[]{35, 6, 36, 68});
        enemyCharacter.setShapeY(new float[]{0, 43, 63, 21});
        gameData.addEvent(new Event(EventType.PICKUP_WEAPON, enemyCharacter.getID()));

        enemies.add(enemyCharacter);
        world.addEntity(enemyCharacter);
        return enemyCharacter;
    }

    @Override
    public void stop(GameData gameData, World world) {
        for (Entity e : enemies) {
            world.removeEntity(e);
        }
    }

    private void movementDecision(Entity enemy, EntityType target, World world) {
        for (Entity entity : world.getEntities(target)) {
            if (entity.getX() - 100 > enemy.getX()) {
                enemy.setVelocity(enemy.getMovementSpeed());
            } else if (entity.getX() + 100 < enemy.getX()) {
                enemy.setVelocity(-enemy.getMovementSpeed());
            } else {
                enemy.setVelocity(0);
            }
        }
    }
}
