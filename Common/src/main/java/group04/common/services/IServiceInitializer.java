package group04.common.services;

import group04.common.GameData;
import group04.common.World;

public interface IServiceInitializer {
    public void start(GameData gameData, World world);
    public void stop(GameData gameData, World world);
}
