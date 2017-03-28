package group04.cameracommon;

import group04.common.Entity;
import group04.common.GameData;
import group04.common.World;

public interface ICameraService {
        public void followEntity(GameData gameData, World world, Entity entity);
}
