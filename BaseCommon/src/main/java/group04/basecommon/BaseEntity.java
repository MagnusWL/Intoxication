package group04.basecommon;

import group04.common.Entity;

public class BaseEntity extends Entity {

    private int hpLevel, turretLevel, platformLevel;
    private Entity turretOwned;

    public Entity getTurretOwned() {
        return turretOwned;
    }

    public void setTurretOwned(Entity turretOwned) {
        this.turretOwned = turretOwned;
    }
    
    public int getHpLevel() {
        return hpLevel;
    }

    public void setHpLevel(int hpLevel) {
        this.hpLevel = hpLevel;
    }

    public int getTurretLevel() {
        return turretLevel;
    }

    public void setTurretLevel(int turretLevel) {
        this.turretLevel = turretLevel;
    }

    public int getPlatformLevel() {
        return platformLevel;
    }

    public void setPlatformLevel(int platformLevel) {
        this.platformLevel = platformLevel;
    }
    
    

}
