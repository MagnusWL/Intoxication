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
    @ServiceProvider(service = IServiceProcessor.class),
    @ServiceProvider(service = IServiceInitializer.class)})

public class EnemySystem implements IServiceProcessor, IServiceInitializer {

    private List<Entity> enemies = new ArrayList<>();
    private final Random rand = new Random();

    @Override
    public void process(GameData gameData, World world) {
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
                    if (entity.getLife() <= 0) {
                        world.removeWeapon(entity.getID());
                        world.removeEntity(entity);
                    }

                    gameData.removeEvent(e);
                }
            }
        }
    }

    @Override
    public void start(GameData gameData, World world
    ) {
        for (int i = 0; i < 2; i++) {
            Entity enemy = createEnemy(gameData, world);
            world.addEntity(enemy);
        }
    }

    private Entity createEnemy(GameData gameData, World world) {
        Entity enemyCharacter = new Entity();

        enemyCharacter.setEntityType(EntityType.ENEMY);
        enemyCharacter.setX((int) (gameData.getDisplayWidth()/2.0 + gameData.getDisplayWidth()/2.0 * Math.random()));
        enemyCharacter.setY((int) (gameData.getDisplayHeight() * 0.15));
        enemyCharacter.setHasGravity(true);
        enemyCharacter.setMaxLife(10);
        enemyCharacter.setLife(enemyCharacter.getMaxLife());
        enemyCharacter.setJumpSpeed(300);
        enemyCharacter.setMovementSpeed(85);
        enemyCharacter.setSprite("Enemy");
        enemyCharacter.setShapeX(new float[]{5, 25, 25, 5});
        enemyCharacter.setShapeY(new float[]{25, 25, 2, 2});
        gameData.addEvent(new Event(EventType.PICKUP_WEAPON, enemyCharacter.getID()));

        enemies.add(enemyCharacter);

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
