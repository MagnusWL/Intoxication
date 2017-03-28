package group04.camera;

import group04.cameracommon.ICameraService;
import group04.common.Entity;
import group04.common.EntityType;
import group04.common.GameData;
import group04.common.World;
import org.openide.util.lookup.ServiceProvider;

@ServiceProvider(service = ICameraService.class)

public class Processor implements ICameraService {

    @Override
    public void followEntity(GameData gameData, World world, Entity entity) {
        gameData.setCameraX((int) (entity.getX() + 180 - gameData.getDisplayWidth() / 2.0));
        gameData.setCameraY((int) (entity.getY() - gameData.getDisplayHeight() / 2.0));
        checkBlankSpace(gameData);
    }

    public void checkBlankSpace(GameData gameData) {

        if (gameData.getCameraX() < 0) {
            gameData.setCameraX(0);
        } else if (gameData.getCameraX() > (gameData.getMapWidth() * gameData.getTileSize()) - gameData.getDisplayWidth()) {
            gameData.setCameraX((gameData.getMapWidth() * gameData.getTileSize()) - gameData.getDisplayWidth());
        }

        if (gameData.getCameraY() > 0) {
            gameData.setCameraY(0);
        } else if (gameData.getCameraY() < (gameData.getMapHeight() * gameData.getTileSize()) - gameData.getDisplayHeight()) {
            gameData.setCameraY((gameData.getMapHeight() * gameData.getTileSize()) - gameData.getDisplayHeight());
        }
    }
}
