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
import group04.playercommon.PlayerEntity;
import org.openide.util.lookup.ServiceProvider;
import org.openide.util.lookup.ServiceProviders;

@ServiceProviders(value = {
    @ServiceProvider(service = IServiceProcessor.class),
    @ServiceProvider(service = IServiceInitializer.class)})

public class PlayerSystem implements IServiceProcessor, IServiceInitializer {

    private Entity player;

    private void checkAnimation(AnimationContainer container, String newAnimation) {
        if (!newAnimation.equals(container.getCurrentAnimation())) {
            container.setCurrentFrame(0);
        }
    }

    @Override
    public void process(GameData gameData, World world) {
        for (Entity entity : world.getEntities(EntityType.PLAYER)) {

            ControllerContainer controllerContainer = ((ControllerContainer) entity.getContainer(ControllerContainer.class));
            float movementSpeed = controllerContainer.getMovementSpeed();
            float jumpSpeed = controllerContainer.getJumpSpeed();
            MovementContainer movementContainer = ((MovementContainer) entity.getContainer(MovementContainer.class));
            AnimationContainer animationContainer = ((AnimationContainer) entity.getContainer(AnimationContainer.class));
            CollisionContainer collisionContainer = ((CollisionContainer) entity.getContainer(CollisionContainer.class));
            UnitContainer unitContainer = ((UnitContainer) entity.getContainer(UnitContainer.class));

            if (gameData.getKeys().isDown(GameKeys.A)) {
                //left

                movementContainer.setVelocity(-movementSpeed);
                checkAnimation(animationContainer, "player_run");
                animationContainer.setCurrentAnimation("player_run");
            }
            if (gameData.getKeys().isDown(GameKeys.D)) {
                //right
                movementContainer.setVelocity(movementSpeed);
                checkAnimation(animationContainer, "player_run");
                animationContainer.setCurrentAnimation("player_run");
            }

            if (gameData.getKeys().isDown(GameKeys.SPACE)) {
                if (collisionContainer.isGrounded()) {
                    {
                        movementContainer.setVerticalVelocity(jumpSpeed);
                    }
                }
            }

            if (!gameData.getKeys().isDown(GameKeys.A) && !gameData.getKeys().isDown(GameKeys.D)) {
                movementContainer.setVelocity(0);
                checkAnimation(animationContainer, "player_idle");
                animationContainer.setCurrentAnimation("player_idle");
            }

            if (!collisionContainer.isGrounded()) {
                checkAnimation(animationContainer, "player_jump");
                animationContainer.setCurrentAnimation("player_jump");
            }

            for (Event e : gameData.getAllEvents()) {
                if (e.getType() == EventType.ENTITY_HIT && e.getEntityID().equals(entity.getID())) {
                    unitContainer.setLife(unitContainer.getLife() - 1);
                    if (unitContainer.getLife() <= 0) {
                        world.removeEntity(world.getEntity(unitContainer.getWeaponOwned()));
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
        PlayerEntity playerCharacter = new PlayerEntity();
        
        
        playerCharacter.setJumpSpeed(400);
        playerCharacter.setMovementSpeed(150);
        playerCharacter.setHasGravity(true);
        playerCharacter.setMaxLife(10000);
        playerCharacter.setLife(playerCharacter.getMaxLife());
        playerCharacter.setDrawable("Player");

        
        playerCharacter.setShapeX(new float[]{17, 34, 52, 66});
        playerCharacter.setShapeY(new float[]{0, 73, 73, 0});

        playerCharacter.setEntityType(EntityType.PLAYER);
        playerCharacter.setX((int) (gameData.getDisplayWidth() * 0.5));
        playerCharacter.setY((int) (gameData.getDisplayHeight() * 0.15));

        gameData.addEvent(new Event(EventType.PICKUP_WEAPON, playerCharacter.getID()));

        return playerCharacter;
    }

    @Override
    public void stop(GameData gameData, World world) {
        world.removeEntity(player);
    }
}
