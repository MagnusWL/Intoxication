package group04.weaponcommon;

import group04.common.Entity;
import group04.common.GameData;
import group04.common.World;

public interface IWeaponService {
    
    public void playerAttack(GameData gameData, World world, Entity player);
    
    public void enemyAttack(GameData gameData, World world, Entity enemy, Entity player, Entity base);
    
    public void pickUpWeapon(GameData gameData, World world);
    
    public void switchWeapon(GameData gameData, World world, Entity player);
}
