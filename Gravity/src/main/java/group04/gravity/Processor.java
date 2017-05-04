package group04.gravity;

import org.openide.util.lookup.ServiceProvider;
import group04.common.Entity;
import group04.common.GameData;
import group04.common.World;

import group04.common.services.IServiceProcessor;

@ServiceProvider(service = IServiceProcessor.class)

public class Processor implements IServiceProcessor {

    @Override
    public void process(GameData gameData, World world) {

        for (Entity entity : world.getAllEntities()) {
             
            if (entity != null && entity.isHasGravity() == true) {
                entity.setVerticalVelocity(entity.getVerticalVelocity() + gameData.getDelta() * gameData.getGravityConstant());
            }

        }
    }
}
