package group04.base;

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
import group04.datacontainers.CollisionContainer;
import group04.datacontainers.UnitContainer;
import group04.datacontainers.ImageContainer;

@ServiceProviders(value = {
    @ServiceProvider(service = IServiceProcessor.class),
    @ServiceProvider(service = IServiceInitializer.class)})

public class BaseSystem implements IServiceProcessor, IServiceInitializer {
    
    private Entity base;

    // collision
    // health
    // image
    @Override
    public void process(GameData gameData, World world) {
        for (Entity entity : world.getEntities(EntityType.BASE)) {
            CollisionContainer collisionContainer = ((CollisionContainer) entity.getContainer(CollisionContainer.class));
            UnitContainer unitContainer = ((UnitContainer) entity.getContainer(UnitContainer.class));
            ImageContainer imageContrainer = ((ImageContainer) entity.getContainer(ImageContainer.class));
            
            for (Event e : gameData.getAllEvents()) {
                if (e.getType() == EventType.ENTITY_HIT && e.getEntityID().equals(entity.getID())) {
                    
                    unitContainer.setLife(unitContainer.getLife() - 1);
                    if (unitContainer.getLife() <= 0) {
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
        Entity base = new Entity();
        UnitContainer unitContainer = new UnitContainer();
        ImageContainer imageContainer = new ImageContainer();
        CollisionContainer collisionContainer = new CollisionContainer();
        
        base.addContainer(imageContainer);
        base.addContainer(unitContainer);
        base.addContainer(collisionContainer);
        
        collisionContainer.setShapeX(new float[]{20,20,200,200});
        collisionContainer.setShapeY(new float[]{35,180,180,35});
        
        imageContainer.setDrawOffsetX(-85);
        imageContainer.setDrawOffsetY(-40);
        imageContainer.setSprite("brain_jar");
        
        unitContainer.setMaxLife(50);
        unitContainer.setLife(unitContainer.getMaxLife());
       
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
