package group04.datacontainers;

import group04.common.EntityType;

public class ProjectileContainer  implements DataContainer {
    private boolean explosive;
    private int explosionRadius;
    private EntityType shotFrom;

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
