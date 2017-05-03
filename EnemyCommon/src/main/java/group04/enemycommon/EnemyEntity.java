package group04.enemycommon;

import group04.common.Entity;

public class EnemyEntity extends Entity {

    private float movementSpeed;
    private float jumpSpeed;
    private Entity weaponOwned;
    private EnemyType enemyType;
    private Entity focusTarget;
    private double k1;
    private double k2;
    private double k3;
    private double k4;
    private double k1e;
    private double k2e;
    private double k3e;
    private double k4e;

    public double getK1e() {
        return k1e;
    }

    public void setK1e(double k1e) {
        this.k1e = k1e;
    }

    public double getK2e() {
        return k2e;
    }

    public void setK2e(double k2e) {
        this.k2e = k2e;
    }

    public double getK3e() {
        return k3e;
    }

    public void setK3e(double k3e) {
        this.k3e = k3e;
    }

    public double getK4e() {
        return k4e;
    }

    public void setK4e(double k4e) {
        this.k4e = k4e;
    }

    public double getK1() {
        return k1;
    }

    public void setK1(double k1) {
        this.k1 = k1;
    }

    public double getK2() {
        return k2;
    }

    public void setK2(double k2) {
        this.k2 = k2;
    }

    public double getK3() {
        return k3;
    }

    public void setK3(double k3) {
        this.k3 = k3;
    }

    public double getK4() {
        return k4;
    }

    public void setK4(double k4) {
        this.k4 = k4;
    }

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
