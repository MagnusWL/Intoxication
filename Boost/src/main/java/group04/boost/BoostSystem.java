package group04.boost;

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
import group04.datacontainers.CollisionContainer;
import group04.datacontainers.HealthContainer;
import group04.datacontainers.ImageContainer;
import group04.datacontainers.MovementContainer;
import group04.datacontainers.PlayerContainer;
import org.openide.util.lookup.ServiceProvider;
import org.openide.util.lookup.ServiceProviders;
import java.util.ArrayList;
import java.util.List;

@ServiceProviders(value = {
    @ServiceProvider(service = IServiceInitializer.class),
    @ServiceProvider(service = IServiceProcessor.class)})

public class BoostSystem implements IServiceInitializer, IServiceProcessor {

    private List<Entity> boosts;

    private Entity createBoost(World world, Event e) {
        Entity boost = world.getEntity(e.getEntityID());
        boost.setEntityType(BOOST);
        MovementContainer movementContainer = new MovementContainer();
        movementContainer.setHasGravity(true);
        ImageContainer imageContainer = new ImageContainer();
        imageContainer.setSprite("Pill");
        boosts.add(boost);

        CollisionContainer collisionContainer = new CollisionContainer();
        collisionContainer.setShapeX(new float[]{
            1,
            1,
            39,
            39});

        collisionContainer.setShapeY(new float[]{
            1,
            39,
            39,
            1});

        return boost;
    }

    private void pickupBoost(GameData gameData, Event e, World world) {
        world.removeEntity(world.getEntity(e.getEntityID()));

        HealthContainer healthContainer = new HealthContainer();
        
        for (Entity player : world.getEntities(EntityType.PLAYER)) {
            healthContainer.setLife(healthContainer.getLife() + 10);
            //player.setLife(player.getLife() + 10);
        }
    }

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
    public void process(GameData gameData, World world) {

        for (Event e : gameData.getEvents()) {
            if (e.getType() == EventType.DROP_BOOST) {
                createBoost(world, e);
                gameData.removeEvent(e);
            }

            if (e.getType() == EventType.PICKUP_BOOST) {
                pickupBoost(gameData, e, world);
                gameData.removeEvent(e);
            }

        }
    }

}
