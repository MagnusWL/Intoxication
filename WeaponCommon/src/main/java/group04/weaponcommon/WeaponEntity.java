/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package group04.weaponcommon;

import group04.common.Entity;
import group04.common.WeaponType;

/**
 *
 * @author Magnus
 */
public class WeaponEntity extends Entity {

    private String weaponCarrier;
    private int damage;
    private WeaponType weaponType;
    private float attackCooldown;
    private float timeSinceAttack;
    private boolean swinging;

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
}
