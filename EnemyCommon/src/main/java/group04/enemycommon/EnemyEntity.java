package group04.enemycommon;

import group04.common.Entity;
import java.util.Objects;

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

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final EnemyEntity other = (EnemyEntity) obj;
        if (Float.floatToIntBits(this.movementSpeed) != Float.floatToIntBits(other.movementSpeed)) {
            return false;
        }
        if (Float.floatToIntBits(this.jumpSpeed) != Float.floatToIntBits(other.jumpSpeed)) {
            return false;
        }
        if (Double.doubleToLongBits(this.k1) != Double.doubleToLongBits(other.k1)) {
            return false;
        }
        if (Double.doubleToLongBits(this.k2) != Double.doubleToLongBits(other.k2)) {
            return false;
        }
        if (Double.doubleToLongBits(this.k3) != Double.doubleToLongBits(other.k3)) {
            return false;
        }
        if (Double.doubleToLongBits(this.k4) != Double.doubleToLongBits(other.k4)) {
            return false;
        }
        if (Double.doubleToLongBits(this.k1e) != Double.doubleToLongBits(other.k1e)) {
            return false;
        }
        if (Double.doubleToLongBits(this.k3e) != Double.doubleToLongBits(other.k3e)) {
            return false;
        }
        if (Double.doubleToLongBits(this.k4e) != Double.doubleToLongBits(other.k4e)) {
            return false;
        }
        if (this.boss != other.boss) {
            return false;
        }
        if (this.spawnMinions != other.spawnMinions) {
            return false;
        }
        if (!Objects.equals(this.weaponOwned, other.weaponOwned)) {
            return false;
        }
        if (this.enemyType != other.enemyType) {
            return false;
        }
        if (!Objects.equals(this.focusTarget, other.focusTarget)) {
            return false;
        }
        return true;
    }
    
    
}
