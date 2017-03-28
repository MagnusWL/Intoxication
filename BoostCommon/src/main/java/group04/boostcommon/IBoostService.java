package group04.boostcommon;

import group04.common.Entity;
import group04.common.GameData;
import group04.common.World;

/**
 *
 * @author Mathias
 */
public interface IBoostService {
    
    public Entity dropBoost(Entity boost);
    public void pickUpBoost(Entity player);
    
}
