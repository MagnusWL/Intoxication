package group04.common.services;

import group04.common.Entity;
import group04.common.GameData;
import group04.common.World;
import group04.common.events.Event;

/**
 *
 * @author Mathias
 */
public interface ICurrencyService {
    
    public Entity dropCurrency(World world, Entity currency);
    public void pickUpCurrency(GameData gameData, World world, Entity player, Entity currency);
}
