package group04.boostcommon;

import group04.common.Entity;
import group04.common.GameData;
import group04.common.World;

/**
 *
 * @author Mathias
 */
public interface IBoostService {
    
    public void dropBoost(World world, float x, float y);
    public void pickUpBoost(World world, Entity player, Entity boost);
    
}
