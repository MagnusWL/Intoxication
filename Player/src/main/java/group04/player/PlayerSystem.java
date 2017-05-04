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
import group04.currencycommon.ICurrencyService;
import group04.playercommon.PlayerEntity;
import org.openide.util.Lookup;
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

    public void playerMovement(Entity entity, GameData gameData, World world) {

        PlayerEntity playerEntity = (PlayerEntity) entity;
        float movementSpeed = playerEntity.getMovementSpeed();
        float jumpSpeed = playerEntity.getJumpSpeed();

        if (gameData.getKeys().isDown(GameKeys.A)) {
            //left
            playerEntity.setVelocity(-movementSpeed);
            checkAnimation(playerEntity, playerEntity.getRunAnimation());
            playerEntity.setCurrentAnimation(playerEntity.getRunAnimation());
        }
        if (gameData.getKeys().isDown(GameKeys.D)) {
            //right
            playerEntity.setVelocity(movementSpeed);
            checkAnimation(playerEntity, playerEntity.getRunAnimation());
            playerEntity.setCurrentAnimation(playerEntity.getRunAnimation());
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
            checkAnimation(playerEntity, playerEntity.getIdleAnimation());
            playerEntity.setCurrentAnimation(playerEntity.getIdleAnimation());
        }
        if (!playerEntity.isGrounded()) {
            checkAnimation(playerEntity, playerEntity.getJumpAnimation());
            playerEntity.setCurrentAnimation(playerEntity.getJumpAnimation());
        }

        for (Event e : gameData.getAllEvents()) {
            if (e.getType() == EventType.ENTITY_HIT && e.getEntityID().equals(entity.getID())) {
                playerEntity.setLife(playerEntity.getLife() - 1);
                if (playerEntity.getLife() <= 0) {
                    if(playerEntity.getWeaponOwned() != null)
                        world.removeEntity(playerEntity.getWeaponOwned());
                    world.removeEntity(entity);
                }
                gameData.removeEvent(e);
            }
        }
    }

    @Override
    public void process(GameData gameData, World world) {
        for (Entity entity : world.getEntities(PlayerEntity.class)) {
            playerMovement(entity, gameData, world);
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
        playerCharacter.setMaxLife(50);
        playerCharacter.setLife(playerCharacter.getMaxLife());
        playerCharacter.setAnimateable(true);
        playerCharacter.setCurrentAnimation("player_run_animation");
        playerCharacter.setRunAnimation("player_run_animation");
        playerCharacter.setJumpAnimation("player_jump_animation");
        playerCharacter.setIdleAnimation("player_idle_animation");
        int spriteWidth = gameData.getSpriteInfo().get(playerCharacter.getCurrentAnimation())[0];
        int spriteHeight = gameData.getSpriteInfo().get(playerCharacter.getCurrentAnimation())[1];
        playerCharacter.setShapeX(new float[]{-(spriteWidth / 2) * gameData.getHitBoxScale(), -(spriteWidth / 2) * gameData.getHitBoxScale(),
            spriteWidth / 2 * gameData.getHitBoxScale(), spriteWidth / 2 * gameData.getHitBoxScale()});
        playerCharacter.setShapeY(new float[]{-(spriteHeight / 2) * gameData.getHitBoxScale(), spriteHeight / 2 * gameData.getHitBoxScale(),
            spriteHeight / 2 * gameData.getHitBoxScale(), -(spriteHeight / 2 * gameData.getHitBoxScale())});

        playerCharacter.setEntityType(EntityType.PLAYER);
        playerCharacter.setX((int) (gameData.getDisplayWidth() * 0.5));
        playerCharacter.setY((int) (gameData.getDisplayHeight() * 0.3));

        gameData.addEvent(new Event(EventType.PICKUP_GUN, playerCharacter.getID()));

        return playerCharacter;
    }

    @Override
    public void stop(GameData gameData, World world) {

    }
}
