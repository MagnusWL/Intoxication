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

    

    private void checkAnimation(PlayerEntity player, String newAnimation) {
        if (!newAnimation.equals(player.getCurrentAnimation())) {
            player.setCurrentFrame(0);
        }
    }

    @Override
    public void process(GameData gameData, World world) {
        for (Entity entity : world.getEntities(PlayerEntity.class)) {
            PlayerEntity playerEntity =  (PlayerEntity) entity;
            
            float movementSpeed = playerEntity.getMovementSpeed();
            float jumpSpeed = playerEntity.getJumpSpeed();

            if (gameData.getKeys().isDown(GameKeys.A)) {
                //left
                playerEntity.setVelocity(-movementSpeed);
                checkAnimation(playerEntity, "player_run");
                playerEntity.setCurrentAnimation("player_run");
            }
            if (gameData.getKeys().isDown(GameKeys.D)) {
                //right
                System.out.println("PRESSED RIGHT");
                playerEntity.setVelocity(movementSpeed);
                checkAnimation(playerEntity, "player_run");
                playerEntity.setCurrentAnimation("player_run");
            }

            if (gameData.getKeys().isDown(GameKeys.SPACE)) {
                if (playerEntity.isGrounded()) {
                    {
                        playerEntity.setVerticalVelocity(jumpSpeed);
                    }
                }
            }

            if (!gameData.getKeys().isDown(GameKeys.A) && !gameData.getKeys().isDown(GameKeys.D)) {
                playerEntity.setVelocity(0);
                checkAnimation(playerEntity, "player_idle");
                playerEntity.setCurrentAnimation("player_idle");
            }

            if (!playerEntity.isGrounded()) {
                checkAnimation(playerEntity, "player_jump");
                playerEntity.setCurrentAnimation("player_jump");
            }

            for (Event e : gameData.getAllEvents()) {
                if (e.getType() == EventType.ENTITY_HIT && e.getEntityID().equals(entity.getID())) {
                    playerEntity.setLife(playerEntity.getLife() - 1);
                    if (playerEntity.getLife() <= 0) {
                        world.removeEntity(playerEntity.getWeaponOwned());
                        world.removeEntity(entity);
                    }
                    gameData.removeEvent(e);
                }
            }
        }
    }

    @Override
    public void start(GameData gameData, World world) {
        Entity player = createPlayer(gameData, world);
        world.addEntity(player);
    }

    private Entity createPlayer(GameData gameData, World world) {
        PlayerEntity playerCharacter = new PlayerEntity();
        
        
        playerCharacter.setJumpSpeed(400);
        playerCharacter.setMovementSpeed(150);
        playerCharacter.setHasGravity(true);
        playerCharacter.setMaxLife(10000);
        playerCharacter.setLife(playerCharacter.getMaxLife());
        playerCharacter.setDrawable("player");

        
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
        
    }
}
