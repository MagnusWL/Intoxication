package group04.datacontainers;

import group04.common.EntityType;
import group04.common.WeaponType;

public class WeaponContainer  implements DataContainer {
     
     
    private String weaponCarrier;
    private WeaponType weaponType;
    private float attackCooldown;
    private float timeSinceAttack;
    private boolean swinging;

    public String getWeaponCarrier() {
        return weaponCarrier;
    }

    public void setWeaponCarrier(String weaponCarrier) {
        this.weaponCarrier = weaponCarrier;
    }


    public WeaponType getWeaponType() {
        return weaponType;
    }

    public void setWeaponType(WeaponType weaponType) {
        this.weaponType = weaponType;
    }

    public float getAttackCooldown() {
        return attackCooldown;
    }

    public void setAttackCooldown(float attackCooldown) {
        this.attackCooldown = attackCooldown;
    }

    public float getTimeSinceAttack() {
        return timeSinceAttack;
    }

    public void setTimeSinceAttack(float timeSinceAttack) {
        this.timeSinceAttack = timeSinceAttack;
    }

    public boolean isSwinging() {
        return swinging;
    }

    public void setSwinging(boolean swinging) {
        this.swinging = swinging;
    }
}
