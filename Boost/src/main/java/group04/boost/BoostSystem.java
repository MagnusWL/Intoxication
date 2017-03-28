package group04.boost;

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
    public Entity dropBoost(Entity boost) {
        boost.setEntityType(BOOST);
        
        boost.setHasGravity(true);
        boost.setDrawable("pill");
        boosts.add(boost);

        
        boost.setShapeX(new float[]{
            1,
            1,
            39,
            39});

        boost.setShapeY(new float[]{
            1,
            39,
            39,
            1});

        return boost;
    }

    @Override
    public void pickUpBoost(Entity player) {
        PlayerEntity entity = (PlayerEntity) player;
        entity.setLife(entity.getLife() + 10);
    }
}
