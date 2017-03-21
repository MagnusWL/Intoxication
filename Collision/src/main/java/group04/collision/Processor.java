package group04.collision;

import group04.common.Entity;
import group04.common.EntityType;
import group04.common.GameData;
import group04.common.World;
import group04.common.services.ICollisionService;
import group04.common.services.IServiceProcessor;
import group04.datacontainers.CollisionContainer;
import group04.datacontainers.MapContainer;
import java.awt.Polygon;
import java.awt.geom.Area;
import org.openide.util.lookup.ServiceProvider;
import org.openide.util.lookup.ServiceProviders;

@ServiceProviders(value = {
    @ServiceProvider(service = ICollisionService.class),
    @ServiceProvider(service = IServiceProcessor.class)
})

public class Processor implements ICollisionService, IServiceProcessor {

    @Override
    public boolean isColliding(World world, GameData gameData, Entity entity, float moveX, float moveY) {
        CollisionContainer collisionContainer = (CollisionContainer) entity.getContainer(CollisionContainer.class);
        float[] shapex = collisionContainer.getShapeX();
        float[] shapey = collisionContainer.getShapeY();
        MapContainer mapContainer = null;
        for (Entity map : world.getEntities(EntityType.MAP)) {
            mapContainer = (MapContainer) map.getContainer(MapContainer.class);
        }
        
        if (mapContainer != null && shapex != null && shapey != null) {
            for (int i = 0; i < shapex.length; i++) {
                int x = (int) ((entity.getX() + shapex[i] + moveX) / gameData.getTileSize());
                int y = (int) ((entity.getY() + shapey[i] + moveY) / gameData.getTileSize());
                if (x >= 0 && y >= 0 && x < gameData.getMapWidth() && y < gameData.getMapHeight()) {
                    if (mapContainer.getMap()[x][y] == 1) {
                        return true;
                    }

                } else {
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public boolean isEntitiesColliding(World world, GameData gameData, Entity entity1, Entity entity2) {
        Polygon poly1 = new Polygon();
        Polygon poly2 = new Polygon();
        CollisionContainer collisionContainer1 = (CollisionContainer) entity1.getContainer(CollisionContainer.class);
        CollisionContainer collisionContainer2 = (CollisionContainer) entity2.getContainer(CollisionContainer.class);
        
        for (int i = 0; i < collisionContainer1.getShapeX().length; i++) {
            poly1.addPoint((int) (collisionContainer1.getShapeX()[i] + entity1.getX()), (int) (collisionContainer1.getShapeY()[i] + entity1.getY()));
        }
        for (int i = 0; i < collisionContainer2.getShapeX().length; i++) {
            poly2.addPoint((int) (collisionContainer2.getShapeX()[i] + entity2.getX()), (int) (collisionContainer2.getShapeY()[i] + entity2.getY()));
        }

        Area a = new Area(poly1);
        a.intersect(new Area(poly2));

        return !a.isEmpty();
    }

    @Override
    public void process(GameData gameData, World world) {
        for (Entity entity : world.getAllEntities()) {
            CollisionContainer collisionContainer = ((CollisionContainer) entity.getContainer(CollisionContainer.class));
            if (collisionContainer != null) {
                if (isColliding(world, gameData, entity, 0, -2)) {
                    collisionContainer.setGrounded(true);
                } else {
                    collisionContainer.setGrounded(false);
                }
            }

        }
    }
}
