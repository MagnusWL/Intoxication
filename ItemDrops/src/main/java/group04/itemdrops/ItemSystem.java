package group04.itemdrops;

import group04.itemdropscommon.ItemType;
import group04.common.Entity;
import group04.common.GameData;
import group04.common.World;
import group04.common.services.IServiceInitializer;
import group04.itemdropscommon.IDropService;
import group04.itemdropscommon.ItemEntity;
import group04.playercommon.PlayerEntity;
import java.util.Random;
import org.openide.util.lookup.ServiceProvider;
import org.openide.util.lookup.ServiceProviders;

/**
 *
 * @author Mathias
 */
@ServiceProviders(value = {
    @ServiceProvider(service = IServiceInitializer.class),
    @ServiceProvider(service = IDropService.class)})

public class ItemSystem implements IServiceInitializer, IDropService {

    private Entity createItem(World world, GameData gameData, float x, float y, String animation, ItemType type) {
        ItemEntity item = new ItemEntity();
        item.setX(x);
        item.setY(y);
        item.setHasGravity(true);
        item.setAnimateable(true);
        item.setCurrentAnimation(animation);
        item.setType(type);

        int spriteWidth = gameData.getSpriteInfo().get(item.getCurrentAnimation())[0];
        int spriteHeight = gameData.getSpriteInfo().get(item.getCurrentAnimation())[1];
        item.setShapeX(new float[]{-(spriteWidth / 2) * gameData.getHitBoxScale(), -(spriteWidth / 2) * gameData.getHitBoxScale(),
            spriteWidth / 2 * gameData.getHitBoxScale(), spriteWidth / 2 * gameData.getHitBoxScale()});
        item.setShapeY(new float[]{-(spriteHeight / 2) * gameData.getHitBoxScale(), spriteHeight / 2 * gameData.getHitBoxScale(),
            spriteHeight / 2 * gameData.getHitBoxScale(), -(spriteHeight / 2 * gameData.getHitBoxScale())});

        return item;
    }

    @Override
    public void dropItem(GameData gameData, World world, float x, float y) {

        Random rand = new Random();
        float dropChance = (float) Math.random();
        
        if (dropChance < 0.25) {
            world.addEntity(createItem(world, gameData, x, y, "currency_gold_animation", ItemType.CURRENCY));
        } 
        
        if (dropChance > 0.75) {
            world.addEntity(createItem(world, gameData, x, y, "pill", ItemType.PILL));
        }

    }

    @Override
    public void pickUpItem(World world, Entity player, Entity loot) {
        ItemEntity itemEntity = (ItemEntity) loot;
        PlayerEntity playerEntity = (PlayerEntity) player;

        if (ItemType.PILL == itemEntity.getType()) {
            playerEntity.setLife(playerEntity.getLife() + 10);
            playerEntity.setLsdTimer(playerEntity.getLsdTimer() + playerEntity.getLsdAmount());
        }
        
        if (ItemType.CURRENCY == itemEntity.getType()) {
            playerEntity.setMoney(playerEntity.getMoney() + 10);
        }

        world.removeEntity(world.getEntity(itemEntity.getID()));
        
    }

    @Override
    public void start(GameData gameData, World world) {

    }

    @Override
    public void stop(GameData gameData, World world) {

    }

}
