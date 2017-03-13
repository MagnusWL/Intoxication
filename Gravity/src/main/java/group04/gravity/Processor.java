package group04.gravity;

import org.openide.util.lookup.ServiceProvider;
import group04.common.Entity;
import group04.common.GameData;
import group04.common.World;

import group04.common.EntityType;
import static group04.common.EntityType.PLAYER;
import group04.common.services.IServiceInitializer;
import group04.common.services.IServiceProcessor;

@ServiceProvider(service = IServiceProcessor.class)

public class Processor implements IServiceProcessor {

    @Override
    public void process(GameData gameData, World world) {

        for (Entity n : world.getAllEntities()) {
            if (n.getHasGravity() == true) {
                n.setVerticalVelocity(n.getVerticalVelocity() + gameData.getDelta() * gameData.getGravityConstant());
            }

        }
    }
}
