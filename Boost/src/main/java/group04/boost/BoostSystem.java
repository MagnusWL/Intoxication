package group04.boost;

import group04.boostcommon.BoostEntity;
import group04.boostcommon.IBoostService;
import group04.common.BoostType;
import group04.common.Entity;
import group04.common.EntityType;
import static group04.common.EntityType.BOOST;
import group04.common.GameData;
import group04.common.World;
import group04.common.events.Event;
import group04.common.events.EventType;
import group04.common.services.IServiceInitializer;
import group04.common.services.IServiceProcessor;
import group04.playercommon.PlayerEntity;
import org.openide.util.lookup.ServiceProvider;
import org.openide.util.lookup.ServiceProviders;
import java.util.ArrayList;
import java.util.List;

@ServiceProviders(value = {
    @ServiceProvider(service = IServiceInitializer.class),
    @ServiceProvider(service = IBoostService.class)})

public class BoostSystem implements IServiceInitializer, IBoostService {

    private List<Entity> boosts;

    @Override
    public void start(GameData gameData, World world) {
        boosts = new ArrayList<>();
    }

    @Override
    public void stop(GameData gameData, World world) {
        for (Entity b : boosts) {
            world.removeEntity(b);
        }
    }

    @Override
    public void dropBoost(GameData gameData, World world, float x, float y) {
        BoostEntity boost = new BoostEntity();
        boost.setHasGravity(true);
        boost.setX(x);
        boost.setY(y);
        boost.setEntityType(BOOST);
        boost.setDrawable("pill");
        boosts.add(boost);

        int spriteWidth = gameData.getSpriteInfo().get(boost.getDrawable())[0];
        int spriteHeight = gameData.getSpriteInfo().get(boost.getDrawable())[1];
        boost.setShapeX(new float[]{-(spriteWidth / 2) * gameData.getHitBoxScale(), -(spriteWidth / 2) * gameData.getHitBoxScale(),
            spriteWidth / 2 * gameData.getHitBoxScale(), spriteWidth / 2 * gameData.getHitBoxScale()});
        boost.setShapeY(new float[]{-(spriteHeight / 2) * gameData.getHitBoxScale(), spriteHeight / 2 * gameData.getHitBoxScale(),
            spriteHeight / 2 * gameData.getHitBoxScale(), -(spriteHeight / 2 * gameData.getHitBoxScale())});

        world.addEntity(boost);
    }

    @Override
    public void pickUpBoost(World world, Entity player, Entity boost) {
        PlayerEntity entity = (PlayerEntity) player;
        entity.setLife(entity.getLife() + 10);
        world.removeEntity(world.getEntity(boost.getID()));
    }
}
