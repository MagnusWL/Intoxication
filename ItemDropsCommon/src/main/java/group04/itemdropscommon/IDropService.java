package group04.itemdropscommon;

import group04.common.Entity;
import group04.common.GameData;
import group04.common.World;

public interface IDropService {
    
    public void dropItem(GameData gameData, World world, float x, float y);
    public void pickUpItem(World world, Entity player, Entity loot);
    
}
