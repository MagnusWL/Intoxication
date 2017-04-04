package group04.currencycommon;

import group04.common.Entity;
import group04.common.GameData;
import group04.common.World;

/**
 *
 * @author Mathias
 */
public interface ICurrencyService {
    
    public void dropCurrency(GameData gameData, World world, float x, float y);
    public void pickUpCurrency(World world, Entity player, Entity currency);
    
}
