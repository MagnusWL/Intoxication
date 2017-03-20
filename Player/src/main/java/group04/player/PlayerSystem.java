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
import group04.datacontainers.AnimationContainer;
import group04.datacontainers.CollisionContainer;
import group04.datacontainers.ControllerContainer;
import group04.datacontainers.DataContainer;
import group04.datacontainers.HealthContainer;
import group04.datacontainers.ImageContainer;
import group04.datacontainers.MovementContainer;
import group04.datacontainers.PlayerContainer;
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
            float jumpSpeed = controllerContainer.getMovementSpeed();

            MovementContainer movementContainer = ((MovementContainer) entity.getContainer(MovementContainer.class));
            AnimationContainer animationContainer = ((AnimationContainer) entity.getContainer(AnimationContainer.class));
            CollisionContainer collisionContainer = ((CollisionContainer) entity.getContainer(CollisionContainer.class));
            HealthContainer healthContainer = ((HealthContainer) entity.getContainer(HealthContainer.class));

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
                    healthContainer.setLife(healthContainer.getLife() - 1);
                    if (healthContainer.getLife() <= 0) {
                        world.removeEntity(world.getEntity(healthContainer.getWeaponOwned()));
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

        PlayerContainer playerContainer = new PlayerContainer();

        ControllerContainer controllerContainer = new ControllerContainer();
        controllerContainer.setJumpSpeed(400);
        controllerContainer.setMovementSpeed(150);

        MovementContainer movementContainer = new MovementContainer();
        movementContainer.setHasGravity(true);

        HealthContainer healthContainer = new HealthContainer();
        healthContainer.setMaxLife(10000);
        healthContainer.setLife(healthContainer.getMaxLife());

        ImageContainer imageContainer = new ImageContainer();
        imageContainer.setSprite("Player");

        CollisionContainer collisionContainer = new CollisionContainer();
        collisionContainer.setShapeX(new float[]{17, 34, 52, 66});
        collisionContainer.setShapeY(new float[]{0, 73, 73, 0});

        playerCharacter.addContainer(playerContainer);
        playerCharacter.addContainer(controllerContainer);
        playerCharacter.addContainer(movementContainer);
        playerCharacter.addContainer(healthContainer);
        playerCharacter.addContainer(imageContainer);
        playerCharacter.addContainer(collisionContainer);

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
