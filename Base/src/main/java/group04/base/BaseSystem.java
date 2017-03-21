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
import group04.datacontainers.HealthContainer;
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
            HealthContainer healthContainer = ((HealthContainer) entity.getContainer(HealthContainer.class));
            ImageContainer imageContrainer = ((ImageContainer) entity.getContainer(ImageContainer.class));
            
            for (Event e : gameData.getAllEvents()) {
                if (e.getType() == EventType.ENTITY_HIT && e.getEntityID().equals(entity.getID())) {
                    
                    healthContainer.setLife(healthContainer.getLife() - 1);
                    if (healthContainer.getLife() <= 0) {
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
        HealthContainer healthContainer = new HealthContainer();
        ImageContainer imageContainer = new ImageContainer();
        CollisionContainer collisionContainer = new CollisionContainer();
        
        base.addContainer(imageContainer);
        base.addContainer(healthContainer);
        base.addContainer(collisionContainer);
        
        collisionContainer.setShapeX(new float[]{20,20,200,200});
        collisionContainer.setShapeY(new float[]{35,180,180,35});
        
        imageContainer.setDrawOffsetX(-85);
        imageContainer.setDrawOffsetY(-40);
        imageContainer.setSprite("brain_jar");
        
        healthContainer.setLife(healthContainer.getMaxLife());
        healthContainer.setMaxLife(50);
       
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
