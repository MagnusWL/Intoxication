/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package group04.projectilecommon;

import group04.common.Entity;
import group04.common.EntityType;

/**
 *
 * @author burno
 */
public class ProjectileEntity extends Entity {

    private boolean explosive;
    private int explosionRadius;
    private EntityType shotFrom;
    private float angle;

    public float getAngle() {
        return angle;
    }

    public void setAngle(float angle) {
        this.angle = angle;
    }

    public EntityType getShotFrom() {
        return shotFrom;
    }

    public void setShotFrom(EntityType shotFrom) {
        this.shotFrom = shotFrom;
    }

    public boolean isExplosive() {
        return explosive;
    }

    public void setExplosive(boolean explosive) {
        this.explosive = explosive;
    }

    public int getExplosionRadius() {
        return explosionRadius;
    }

    public void setExplosionRadius(int explosionRadius) {
        this.explosionRadius = explosionRadius;
    }

}
