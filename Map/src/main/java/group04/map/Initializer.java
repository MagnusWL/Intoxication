package group04.map;

import org.openide.util.lookup.ServiceProvider;
import group04.common.Entity;
import group04.common.EntityType;
import group04.common.GameData;
import group04.common.World;
import group04.common.services.IServiceInitializer;

@ServiceProvider(service = IServiceInitializer.class)

public class Initializer implements IServiceInitializer {

    private Entity map;

    public Initializer() {
    }

    @Override
    public void start(GameData gameData, World world) {
        map = generateMap(gameData);
        world.addEntity(map);
    }

    @Override
    public void stop(GameData gameData, World world) {

    }

    private Entity generateMap(GameData gameData) {
        int[][] newMapInt = new int[gameData.getMapWidth()][gameData.getMapHeight()];

        for (int i = 0; i < gameData.getMapWidth(); i++) {
            for (int j = 0; j < gameData.getMapHeight(); j++) {
                if (j > (int) (gameData.getMapHeight() * 0.13)) {
                    newMapInt[i][j] = 0;
                } else {
                    newMapInt[i][j] = 1;
                }
            }
        }

        Entity newMap = new Entity();
        newMap.setEntityType(EntityType.MAP);
        newMap.setMap(newMapInt);
        return newMap;
    }
}
