package group04.collision;

import group04.collisioncommon.ICollisionService;
import group04.common.Entity;
import group04.common.GameData;
import group04.common.World;
import group04.common.services.IServiceProcessor;
import group04.mapcommon.MapEntity;

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

        float[] shapex = entity.getShapeX();
        float[] shapey = entity.getShapeY();
        MapEntity mapEntity = null;
        for (Entity map : world.getEntities(MapEntity.class)) {
            mapEntity = (MapEntity) map;
        }

        if (mapEntity != null && shapex != null && shapey != null) {
            for (int i = 0; i < shapex.length; i++) {
                int x = (int) ((entity.getX() + shapex[i] + moveX) / gameData.getTileSize());
                int y = (int) ((entity.getY() + shapey[i] + moveY) / gameData.getTileSize());

                if (shapey[i] < 0) {
                    if (x >= 0 && y >= 0 && x < gameData.getMapWidth() && y < gameData.getMapHeight()) {
                        if (mapEntity.getMap()[x][y] == 1 && entity.getVerticalVelocity() < 0) {
                            return true;
                        }

                    } else {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    @Override
    public boolean isEntitiesColliding(World world, GameData gameData, Entity entity1, Entity entity2) {
        Polygon poly1 = new Polygon();
        Polygon poly2 = new Polygon();

        for (int i = 0; i < entity1.getShapeX().length; i++) {
            poly1.addPoint((int) (entity1.getShapeX()[i] + entity1.getX()), (int) (entity1.getShapeY()[i] + entity1.getY()));
        }
        for (int i = 0; i < entity2.getShapeX().length; i++) {
            poly2.addPoint((int) (entity2.getShapeX()[i] + entity2.getX()), (int) (entity2.getShapeY()[i] + entity2.getY()));
        }

        Area a = new Area(poly1);
        a.intersect(new Area(poly2));

        return !a.isEmpty();
    }

    @Override
    public void process(GameData gameData, World world) {
        for (Entity entity : world.getAllEntities()) {

            if (entity != null) {
                if (isColliding(world, gameData, entity, 0, -2)) {
                    entity.setGrounded(true);
                } else {
                    entity.setGrounded(false);
                }
            }

        }
    }
}
