package group04.enemycommon;

import group04.common.Entity;
import group04.common.GameData;
import group04.common.World;
import java.util.ArrayList;
import group04.common.events.Event;

public interface IEnemyService {
    
    public void controller(GameData gameData, World world, Entity player, Entity base, ArrayList<EnemyEntity> enemy);
    
    public void enemyHit(GameData gameData, World world, EnemyEntity enemyHit);
    
    public void createEnemy(GameData gameData, World world, int x, int y, EnemyType enemyType, int currentLevel);
}
