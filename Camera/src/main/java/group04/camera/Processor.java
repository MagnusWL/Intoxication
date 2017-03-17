package group04.camera;

import group04.common.Entity;
import group04.common.EntityType;
import group04.common.GameData;
import group04.common.World;
import group04.common.services.IServiceProcessor;
import org.openide.util.lookup.ServiceProvider;

@ServiceProvider(service = IServiceProcessor.class)

public class Processor implements IServiceProcessor {

    @Override
    public void process(GameData gameData, World world) {
        for (Entity player : world.getEntities(EntityType.PLAYER)) {
            gameData.setCameraX((int) (player.getX() + 180 - gameData.getDisplayWidth() / 2.0));
            gameData.setCameraY((int) (player.getY() - gameData.getDisplayHeight() / 2.0));
            checkBlankSpace(gameData);
            break;
        }
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
