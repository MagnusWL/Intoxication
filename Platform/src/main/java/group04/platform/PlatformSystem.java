package group04.platform;

import group04.common.GameData;
import group04.common.World;
import group04.common.services.IServiceInitializer;
import org.openide.util.lookup.ServiceProvider;
import org.openide.util.lookup.ServiceProviders;

@ServiceProviders(value = {
    @ServiceProvider(service = IServiceInitializer.class)
})

public class PlatformSystem implements IServiceInitializer {

    @Override
    public void start(GameData gameData, World world) {
    }

    @Override
    public void stop(GameData gameData, World world) {
    }

}
