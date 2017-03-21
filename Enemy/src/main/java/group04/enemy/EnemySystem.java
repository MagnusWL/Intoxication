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
import group04.datacontainers.AnimationContainer;
import group04.datacontainers.CollisionContainer;
import group04.datacontainers.ControllerContainer;
import group04.datacontainers.HealthContainer;
import group04.datacontainers.ImageContainer;
import group04.datacontainers.MovementContainer;
import group04.datacontainers.PlayerContainer;
import group04.datacontainers.WaveSpawnerContainer;

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
            WaveSpawnerContainer waveSpawnerContainer = ((WaveSpawnerContainer) entity.getContainer(WaveSpawnerContainer.class));

            waveSpawnerContainer.setSpawnTimer(waveSpawnerContainer.getSpawnTimer() + 1);

            if (waveSpawnerContainer.getSpawnTimer() > waveSpawnerContainer.getSpawnTimerMax()) {
                int timePerMob = waveSpawnerContainer.getSpawnDuration() / waveSpawnerContainer.getMobsSpawnedMax();

                if (waveSpawnerContainer.getSpawnTimer() - waveSpawnerContainer.getSpawnTimerMax() > timePerMob * waveSpawnerContainer.getMobsSpawned()) {
                    waveSpawnerContainer.setMobsSpawned(waveSpawnerContainer.getMobsSpawned() + 1);
                    createEnemy(gameData, world, 3100, 350);
                }

                if (waveSpawnerContainer.getSpawnTimer() > waveSpawnerContainer.getSpawnTimerMax() + waveSpawnerContainer.getSpawnDuration()) {
                    waveSpawnerContainer.setSpawnTimer(0);
                    waveSpawnerContainer.setMobsSpawned(0);
                }
            }
        }

        for (Entity entity : world.getEntities(EntityType.ENEMY)) {
            ControllerContainer controllerContainer = ((ControllerContainer) entity.getContainer(ControllerContainer.class));
            float movementSpeed = controllerContainer.getMovementSpeed();
            float jumpSpeed = controllerContainer.getMovementSpeed();

            MovementContainer movementContainer = ((MovementContainer) entity.getContainer(MovementContainer.class));
            AnimationContainer animationContainer = ((AnimationContainer) entity.getContainer(AnimationContainer.class));
            CollisionContainer collisionContainer = ((CollisionContainer) entity.getContainer(CollisionContainer.class));
            HealthContainer healthContainer = ((HealthContainer) entity.getContainer(HealthContainer.class));

            float distancePlayer = Float.MAX_VALUE;
            float distanceBase = Float.MAX_VALUE;
            for (Entity player : world.getEntities(EntityType.PLAYER)) {
                distancePlayer = Math.abs(player.getX() - entity.getX());
            }
            for (Entity base : world.getEntities(EntityType.BASE)) {
                distanceBase = Math.abs(base.getX() - entity.getX());
            }

            if (distancePlayer > distanceBase) {
                movementDecision(entity, movementContainer, controllerContainer, EntityType.BASE, world);
            } else {
                movementDecision(entity, movementContainer, controllerContainer, EntityType.PLAYER, world);
            }

            if (rand.nextFloat() > 0.99f) {
                if (collisionContainer.isGrounded()) {
                    movementContainer.setVerticalVelocity(jumpSpeed);
                }
            }

            for (Event e : gameData.getAllEvents()) {
                if (e.getType() == EventType.ENTITY_HIT && e.getEntityID().equals(entity.getID())) {

                    healthContainer.setLife(healthContainer.getLife() - 1);

                    //ENEMY DIES
                    if (healthContainer.getLife() <= 0) {
                        world.removeEntity(world.getEntity(healthContainer.getWeaponOwned()));
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
        createWaveSpawner(gameData, world);
    }

    private void createWaveSpawner(GameData gameData, World world) {
        Entity waveSpawner = new Entity();
        waveSpawner.setEntityType(EntityType.WAVE_SPAWNER);

        WaveSpawnerContainer waveSpawnerContainer = new WaveSpawnerContainer();
        waveSpawnerContainer.setSpawnTimerMax(600);
        waveSpawnerContainer.setMobsSpawnedMax(5);
        waveSpawnerContainer.setSpawnDuration(300);

        waveSpawner.addContainer(waveSpawnerContainer);
        world.addEntity(waveSpawner);
    }

    private Entity createEnemy(GameData gameData, World world, int x, int y) {
        Entity enemyCharacter = new Entity();

        ControllerContainer controllerContainer = new ControllerContainer();
        controllerContainer.setJumpSpeed(300);
        controllerContainer.setMovementSpeed(85);

        MovementContainer movementContainer = new MovementContainer();
        movementContainer.setHasGravity(true);

        HealthContainer healthContainer = new HealthContainer();
        healthContainer.setMaxLife(5);
        healthContainer.setLife(healthContainer.getMaxLife());

        ImageContainer imageContainer = new ImageContainer();
        imageContainer.setSprite("Enemy_Beer");

        CollisionContainer collisionContainer = new CollisionContainer();
        collisionContainer.setShapeX(new float[]{250, 250, 0, 0});
        collisionContainer.setShapeY(new float[]{0, 250, 250, 0});

        AnimationContainer animationContainer = new AnimationContainer();
        animationContainer.setAnimateable(true);
        animationContainer.setCurrentAnimation("Enemy_Beer_Run");

        enemyCharacter.addContainer(animationContainer);
        enemyCharacter.addContainer(controllerContainer);
        enemyCharacter.addContainer(movementContainer);
        enemyCharacter.addContainer(healthContainer);
        enemyCharacter.addContainer(imageContainer);
        enemyCharacter.addContainer(collisionContainer);

        enemyCharacter.setEntityType(EntityType.ENEMY);
        enemyCharacter.setX(x);
        enemyCharacter.setY(y);

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

    private void movementDecision(Entity enemy, MovementContainer movementContainer, ControllerContainer controllerContainer, EntityType target, World world) {
        for (Entity entity : world.getEntities(target)) {
            if (entity.getX() - 100 > enemy.getX()) {
                movementContainer.setVelocity(controllerContainer.getMovementSpeed());
            } else if (entity.getX() + 100 < enemy.getX()) {
                movementContainer.setVelocity(-controllerContainer.getMovementSpeed());
            } else {
                movementContainer.setVelocity(0);
            }
        }
    }
}
