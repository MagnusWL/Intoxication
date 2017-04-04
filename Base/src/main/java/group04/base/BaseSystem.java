package group04.base;

import group04.basecommon.BaseEntity;
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

public class BaseSystem implements IServiceProcessor, IServiceInitializer {

    private Entity base;

    @Override
    public void process(GameData gameData, World world) {
        for (Entity entity : world.getEntities(BaseEntity.class)) {
            BaseEntity base = (BaseEntity) entity;
            for (Event e : gameData.getAllEvents()) {
                if (e.getType() == EventType.ENTITY_HIT && e.getEntityID().equals(entity.getID())) {
                    base.setLife(base.getLife() - 1);
                    if (base.getLife() <= 0) {
                        world.removeEntity(entity);
                    }

                    gameData.removeEvent(e);
                }
            }
        }
    }

    @Override
    public void start(GameData gameData, World world) {
        base = createBase(gameData, world);
        world.addEntity(base);
    }

    private Entity createBase(GameData gameData, World world) {
        BaseEntity base = new BaseEntity();
        base.setDrawable("brain_jar");

        int spriteWidth = gameData.getSpriteInfo().get(base.getDrawable())[0];
        int spriteHeight = gameData.getSpriteInfo().get(base.getDrawable())[1];
        base.setShapeX(new float[]{-(spriteWidth / 2) * gameData.getHitBoxScale(), -(spriteWidth / 2) * gameData.getHitBoxScale(),
            spriteWidth / 2 * gameData.getHitBoxScale(), spriteWidth / 2 * gameData.getHitBoxScale()});
        base.setShapeY(new float[]{-(spriteHeight / 2) * gameData.getHitBoxScale(), spriteHeight / 2 * gameData.getHitBoxScale(),
            spriteHeight / 2 * gameData.getHitBoxScale(), -(spriteHeight / 2 * gameData.getHitBoxScale())});

        base.setMaxLife(50);
        base.setLife(base.getMaxLife());
        base.setEntityType(EntityType.BASE);
        base.setX((int) (gameData.getDisplayWidth() * 0.2));
        base.setY((int) (gameData.getDisplayHeight() * 0.13));
        return base;
    }

    @Override
    public void stop(GameData gameData, World world) {
        world.removeEntity(base);
    }
}
