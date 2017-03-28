package group04.currencycommon;

import group04.common.Entity;
import group04.common.GameData;
import group04.common.World;

/**
 *
 * @author Mathias
 */
public interface ICurrencyService {
    
    public Entity dropCurrency(World world, Entity currency);
    public void pickUpCurrency(Entity player);
    
}
