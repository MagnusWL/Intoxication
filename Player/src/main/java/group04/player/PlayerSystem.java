package group04.player;

import group04.common.Entity;
import group04.common.EntityType;
import group04.common.GameData;
import group04.common.GameKeys;
import group04.common.World;
import group04.common.events.Event;
import group04.common.events.EventType;
import group04.common.services.IServiceInitializer;
import group04.common.services.IServiceProcessor;
import org.openide.util.lookup.ServiceProvider;
import org.openide.util.lookup.ServiceProviders;

@ServiceProviders(value = {
    @ServiceProvider(service = IServiceProcessor.class),
    @ServiceProvider(service = IServiceInitializer.class)})

public class PlayerSystem implements IServiceProcessor, IServiceInitializer {

    private Entity player;

    private void checkAnimation(Entity entity, String newAnimation) {
        if (!newAnimation.equals(entity.getCurrentAnimation())) {
            entity.setCurrentFrame(0);
        }
    }

    @Override
    public void process(GameData gameData, World world) {
        for (Entity entity : world.getEntities(EntityType.PLAYER)) {

            
            System.out.println(gameData.getKeys().isDown(GameKeys.MOUSE0));
            if (gameData.getKeys().isDown(GameKeys.A)) {
                //left
                entity.setVelocity(-entity.getMovementSpeed());
                checkAnimation(entity, "player_run");
                entity.setCurrentAnimation("player_run");
            }
            if (gameData.getKeys().isDown(GameKeys.D)) {
                //right
                entity.setVelocity(entity.getMovementSpeed());
                checkAnimation(entity, "player_run");
                entity.setCurrentAnimation("player_run");
            }

            if (gameData.getKeys().isDown(GameKeys.SPACE)) {
                if (entity.isGrounded()) {
                    {
                        entity.setVerticalVelocity(entity.getJumpSpeed());
                    }
                }
            }

            if (!gameData.getKeys().isDown(GameKeys.A) && !gameData.getKeys().isDown(GameKeys.D)) {
                entity.setVelocity(0);
                checkAnimation(entity, "player_idle");
                entity.setCurrentAnimation("player_idle");
            }

            if (!entity.isGrounded()) {
                checkAnimation(entity, "player_jump");
                entity.setCurrentAnimation("player_jump");
            }
            
            for (Event e : gameData.getAllEvents()) {
                if (e.getType() == EventType.ENTITY_HIT && e.getEntityID().equals(entity.getID())) {
                    entity.setLife(entity.getLife() - 1);
                    if (entity.getLife() <= 0) {
                        world.removeEntity(world.getEntity(entity.getWeaponOwned()));
                        world.removeEntity(entity);
                    }
                    gameData.removeEvent(e);
                }
            }
        }
    }

    @Override
    public void start(GameData gameData, World world) {
        player = createPlayer(gameData, world);
        world.addEntity(player);
    }

    private Entity createPlayer(GameData gameData, World world) {
        Entity playerCharacter = new Entity();

        playerCharacter.setCurrentAnimation("player_idle");
        playerCharacter.setAnimateable(true);
        playerCharacter.setEntityType(EntityType.PLAYER);
        playerCharacter.setX((int) (gameData.getDisplayWidth() * 0.5));
        playerCharacter.setY((int) (gameData.getDisplayHeight() * 0.15));
        playerCharacter.setHasGravity(true);
        playerCharacter.setMaxLife(100);
        playerCharacter.setLife(playerCharacter.getMaxLife());
        playerCharacter.setJumpSpeed(400);
        playerCharacter.setMovementSpeed(150);
        playerCharacter.setSprite("Player");
        playerCharacter.setShapeX(new float[]{17, 34, 52, 66});
        playerCharacter.setShapeY(new float[]{0, 73, 73, 0});
        gameData.addEvent(new Event(EventType.PICKUP_WEAPON, playerCharacter.getID()));

        return playerCharacter;
    }

    @Override
    public void stop(GameData gameData, World world) {
        world.removeEntity(player);
    }
}
