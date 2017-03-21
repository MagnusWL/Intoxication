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
import group04.common.services.IEnemyService;
import group04.common.services.IServiceInitializer;
import group04.datacontainers.AnimationContainer;
import group04.datacontainers.CollisionContainer;
import group04.datacontainers.ControllerContainer;
import group04.datacontainers.HealthContainer;
import group04.datacontainers.ImageContainer;
import group04.datacontainers.MovementContainer;
import group04.datacontainers.WaveSpawnerContainer;

@ServiceProviders(value = {
    @ServiceProvider(service = IEnemyService.class),
    @ServiceProvider(service = IServiceInitializer.class)})

public class EnemySystem implements IEnemyService, IServiceInitializer {

    private List<Entity> enemies = new ArrayList<>();
    private final Random rand = new Random();

    private void dropItem(Entity drop, Entity enemy, World world, GameData gameData, EventType type) {
        world.addEntity(drop);
        gameData.addEvent(new Event(type, drop.getID()));
        drop.setX(enemy.getX());
        drop.setY(enemy.getY());
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
        waveSpawnerContainer.setSpawnDuration(500);

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
        collisionContainer.setShapeX(new float[]{120, 120, 20, 20});
        collisionContainer.setShapeY(new float[]{0, 100, 100, 0});

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

    private void movementDecision(Entity enemy, MovementContainer movementContainer, ControllerContainer controllerContainer, Entity target, World world) {
        if (target.getX() - 100 > enemy.getX()) {
            movementContainer.setVelocity(controllerContainer.getMovementSpeed());
        } else if (target.getX() + 100 < enemy.getX()) {
            movementContainer.setVelocity(-controllerContainer.getMovementSpeed());
        } else {
            movementContainer.setVelocity(0);
        }

    }

    @Override
    public void spawner(GameData gameData, World world, Entity waveSpawner) {
        WaveSpawnerContainer waveSpawnerContainer = ((WaveSpawnerContainer) waveSpawner.getContainer(WaveSpawnerContainer.class));

        waveSpawnerContainer.setSpawnTimer(waveSpawnerContainer.getSpawnTimer() + 1);

        if (waveSpawnerContainer.getSpawnTimer() > waveSpawnerContainer.getSpawnTimerMax()) {
            int timePerMob = waveSpawnerContainer.getSpawnDuration() / waveSpawnerContainer.getMobsSpawnedMax();

            if (waveSpawnerContainer.getSpawnTimer() - waveSpawnerContainer.getSpawnTimerMax() > timePerMob * waveSpawnerContainer.getMobsSpawned()) {
                waveSpawnerContainer.setMobsSpawned(waveSpawnerContainer.getMobsSpawned() + 1);
                createEnemy(gameData, world, (int) (gameData.getTileSize() * gameData.getMapWidth() * 0.95), (int) (gameData.getDisplayHeight() * 0.15));
            }

            if (waveSpawnerContainer.getSpawnTimer() > waveSpawnerContainer.getSpawnTimerMax() + waveSpawnerContainer.getSpawnDuration()) {
                waveSpawnerContainer.setSpawnTimer(0);
                waveSpawnerContainer.setMobsSpawned(0);
            }

        }
    }

    @Override
    public void controller(GameData gameData, World world, Entity player, Entity base, ArrayList<Entity> enemyList) {
        for (Entity enemy : enemyList) {
            ControllerContainer controllerContainer = ((ControllerContainer) enemy.getContainer(ControllerContainer.class));
            float movementSpeed = controllerContainer.getMovementSpeed();
            float jumpSpeed = controllerContainer.getMovementSpeed();

            MovementContainer movementContainer = ((MovementContainer) enemy.getContainer(MovementContainer.class));
            AnimationContainer animationContainer = ((AnimationContainer) enemy.getContainer(AnimationContainer.class));
            CollisionContainer collisionContainer = ((CollisionContainer) enemy.getContainer(CollisionContainer.class));
            HealthContainer healthContainer = ((HealthContainer) enemy.getContainer(HealthContainer.class));

            float distancePlayer = Float.MAX_VALUE;
            float distanceBase = Float.MAX_VALUE;
            distancePlayer = Math.abs(player.getX() - enemy.getX());
            distanceBase = Math.abs(base.getX() - enemy.getX());

            if (distancePlayer > distanceBase) {
                movementDecision(enemy, movementContainer, controllerContainer, base, world);
            } else {
                movementDecision(enemy, movementContainer, controllerContainer, player, world);
            }

            if (rand.nextFloat() > 0.99f) {
                if (collisionContainer.isGrounded()) {
                    movementContainer.setVerticalVelocity(jumpSpeed);
                }
            }
        }
    }

    @Override
    public void enemyHit(GameData gameData, World world, Entity enemyHit) {
        HealthContainer healthContainer = ((HealthContainer) enemyHit.getContainer(HealthContainer.class));

        healthContainer.setLife(healthContainer.getLife() - 1);

        //ENEMY DIES
        if (healthContainer.getLife() <= 0) {
            world.removeEntity(world.getEntity(healthContainer.getWeaponOwned()));
            world.removeEntity(enemyHit);

            Entity currency = new Entity();
            Entity boost = new Entity();

            dropItem(currency, enemyHit, world, gameData, EventType.DROP_CURRENCY);
            dropItem(boost, enemyHit, world, gameData, EventType.DROP_BOOST);                       
        }
    }
}
