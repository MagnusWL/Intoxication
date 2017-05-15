package group04.projectilecommon;

import group04.common.Entity;
import group04.common.GameData;
import group04.common.World;
import group04.common.events.Event;

public interface IProjectileService {
    public void playershootgun(GameData gameData, World world, Entity player, Entity playerWeapon);
    public void playershootrocket(GameData gameData, World world, Entity player, Entity playerWeapon);
    public void enemyshoot(GameData gameData, World world, Entity enemy, Entity base, Entity player, double k1, double k2);
    public void aiEnemyshoot(GameData gameData, World world, Entity enemy, Entity player, double k1, double k2);
    public void playermeleeattack(GameData gameData, World world, Entity player, Entity playerWeapon);    
}
