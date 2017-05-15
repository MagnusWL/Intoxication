package group04.collisioncommon;

import group04.common.Entity;
import group04.common.GameData;
import group04.common.World;

public interface ICollisionService {
    public boolean isColliding(World world, GameData gameData, Entity entity, float moveX, float moveY);
    public boolean isEntitiesColliding(Entity entity1, Entity entity2);
}
