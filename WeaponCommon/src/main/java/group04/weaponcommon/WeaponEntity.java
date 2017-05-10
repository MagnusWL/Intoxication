package group04.weaponcommon;

import group04.common.Entity;

public class WeaponEntity extends Entity{

    private String weaponCarrier;
    private int damage;
    private WeaponType weaponType;
    private float attackCooldown;
    private float timeSinceAttack;
    private boolean swinging;
    
    //Audio:
    
    private String attackAudio;
    private String pickUpAudio;
    private String outOfProjectileAudio;

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

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
    
    //Audio methods:

    public String getAttackAudio() {
        return attackAudio;
    }

    public void setAttackAudio(String attackAudio) {
        this.attackAudio = attackAudio;
    }

    public String getPickUpAudio() {
        return pickUpAudio;
    }

    public void setPickUpAudio(String pickUpAudio) {
        this.pickUpAudio = pickUpAudio;
    }

    public String getOutOfProjectileAudio() {
        return outOfProjectileAudio;
    }

    public void setOutOfProjectileAudio(String outOfProjectileAudio) {
        this.outOfProjectileAudio = outOfProjectileAudio;
    }
    
}
