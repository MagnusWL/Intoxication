package group04.boostcommon;

import group04.common.Entity;
import group04.common.GameData;
import group04.common.World;

/**
 *
 * @author Mathias
 */
public interface IBoostService {
    
    public Entity dropBoost(World world, Entity boost);
    public void pickUpBoost(GameData gameData, World world, Entity player, Entity boost);
    
}
