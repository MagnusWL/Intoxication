package group04.enemycommon;

import group04.common.Entity;

public class EnemyEntity extends Entity {

    private float movementSpeed;
    private float jumpSpeed;
    private Entity weaponOwned;
    private EnemyType enemyType;
    private Entity focusTarget;

    //Boss variables:
    private boolean boss;
    private boolean spawnMinions;

    public Entity getFocusTarget() {
        return focusTarget;
    }

    public void setFocusTarget(Entity focusTarget) {
        this.focusTarget = focusTarget;
    }

    public boolean isBoss() {
        return boss;
    }

    public void setBoss(boolean boss) {
        this.boss = boss;
    }

    public boolean isSpawnMinions() {
        return spawnMinions;
    }

    public void setSpawnMinions(boolean spawnMinions) {
        this.spawnMinions = spawnMinions;
    }

    public EnemyType getEnemyType() {
        return enemyType;
    }

    public void setEnemyType(EnemyType enemyType) {
        this.enemyType = enemyType;
    }

    public float getMovementSpeed() {
        return movementSpeed;
    }

    public void setMovementSpeed(float movementSpeed) {
        this.movementSpeed = movementSpeed;
    }

    public float getJumpSpeed() {
        return jumpSpeed;
    }

    public void setJumpSpeed(float jumpSpeed) {
        this.jumpSpeed = jumpSpeed;
    }

    public Entity getWeaponOwned() {
        return weaponOwned;
    }

    public void setWeaponOwned(Entity weaponOwned) {
        this.weaponOwned = weaponOwned;
    }
}
