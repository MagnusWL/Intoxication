/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package group04.projectile;

import group04.basecommon.BaseEntity;
import java.util.ArrayList;
import org.openide.util.lookup.ServiceProvider;
import org.openide.util.lookup.ServiceProviders;
import group04.common.GameData;
import group04.common.World;
import group04.common.services.IServiceInitializer;
import group04.common.Entity;
import group04.common.EntityType;
import group04.common.services.IProjectileService;
import group04.playercommon.PlayerEntity;
import group04.projectilecommon.ProjectileEntity;

/**
 *
 * @author burno
 */
@ServiceProviders(value = {
    /* @ServiceProvider(service = IServiceProcessor.class ) , */
    @ServiceProvider(service = IServiceInitializer.class),
    @ServiceProvider(service = IProjectileService.class)})

public class BulletSystem implements IServiceInitializer, IProjectileService {

    private ArrayList<Entity> bullets = new ArrayList<>();

    private Entity createBullet(Entity entity, GameData gameData, World world, float angle) {
        ProjectileEntity bullet = new ProjectileEntity();
        bullet.setEntityType(EntityType.PROJECTILE);

        bullet.setDrawable("bullet");
        bullet.setAngle(angle);

        bullet.setVelocity((float) (350 * Math.cos(angle)));
        bullet.setVerticalVelocity((float) (350 * Math.sin(angle)));

        bullet.setShapeX(new float[]{0, 5, 5, 0});
        bullet.setShapeY(new float[]{5, 5, 0, 0});

        bullet.setShotFrom(entity.getEntityType());
        bullet.setExplosive(false);

        bullet.setX(entity.getX() + 35 + ((float) Math.cos(angle) * 50));
        bullet.setY(entity.getY() + 35 + ((float) Math.sin(angle) * 50));

        bullets.add(bullet);
        return bullet;
    }

    private Entity createRocket(Entity entity, GameData gameData, World world, float angle) {
        ProjectileEntity rocket = new ProjectileEntity();
        rocket.setEntityType(EntityType.PROJECTILE);

        rocket.setDrawable("rocket");
        rocket.setAngle(angle);

        rocket.setVelocity((float) (350 * Math.cos(angle)));
        rocket.setVerticalVelocity((float) (350 * Math.sin(angle)));

        rocket.setShapeX(new float[]{0, 5, 5, 0});
        rocket.setShapeY(new float[]{5, 5, 0, 0});

        rocket.setShotFrom(entity.getEntityType());
        rocket.setExplosive(true);
        rocket.setExplosionRadius(40);

        rocket.setX(entity.getX() + 35 + ((float) Math.cos(angle) * 50));
        rocket.setY(entity.getY() + 35 + ((float) Math.sin(angle) * 50));

        bullets.add(rocket);
        return rocket;
    }

    /**
     * @Override public void process(GameData gameData, World world, Entity
     * player, Entity base) { for (Event e : gameData.getAllEvents()) { if
     * (e.getType() == EventType.PLAYER_SHOOT) { player =
     * world.getEntity(e.getEntityID()); Entity playerWeapon =
     * world.getEntity(((HealthContainer)
     * player.getContainer(HealthContainer.class)).getWeaponOwned());
     * WeaponContainer weaponContainer = ((WeaponContainer)
     * playerWeapon.getContainer(WeaponContainer.class));
     *
     * float angle = (float) Math.atan2(gameData.getMouseY() - (player.getY() +
     * 15 - gameData.getCameraY()), gameData.getMouseX() - (player.getX() + 15 -
     * gameData.getCameraX()));
     *
     * if (weaponContainer.getWeaponType() == WeaponType.ROCKET) {
     * world.addEntity(createRocket(playerWeapon, gameData, world, angle)); } if
     * (weaponContainer.getWeaponType() == WeaponType.GUN) {
     * world.addEntity(createBullet(playerWeapon, gameData, world, angle)); }
     * gameData.removeEvent(e); }
     *
     * if (e.getType() == EventType.ENEMY_SHOOT) { Entity enemyWeapon =
     * world.getEntity(e.getEntityID()); float distancePlayer = Float.MAX_VALUE;
     * float distanceBase = Float.MAX_VALUE;
     *
     * distancePlayer = Math.abs(player.getX() - enemyWeapon.getX());
     * distanceBase = Math.abs(base.getX() - enemyWeapon.getX());
     *
     * if (enemyWeapon.getX() + 30 > gameData.getCameraX() && enemyWeapon.getX()
     * + 30 < gameData.getCameraX() + gameData.getDisplayWidth()) {
     *
     * if (distancePlayer > distanceBase) { shootDecision(enemyWeapon,
     * EntityType.BASE, world, gameData); } else { shootDecision(enemyWeapon,
     * EntityType.PLAYER, world, gameData); } } gameData.removeEvent(e); } } }
     */
    @Override
    public void start(GameData gameData, World world) {

    }

    @Override
    public void stop(GameData gameData, World world) {
        for (Entity e : bullets) {
            world.removeEntity(e);
        }
    }

    private void shootDecision(Entity enemy, Entity target, World world, GameData gameData) {

        float angle = (float) Math.atan2((target.getY() + 15) - (enemy.getY() + 15), (target.getX() + 15) - (enemy.getX() + 15));
        world.addEntity(createBullet(enemy, gameData, world, angle));

    }

    @Override
    public void playershootgun(GameData gameData, World world, Entity player, Entity playerWeapon) {
        float angle = (float) Math.atan2(gameData.getMouseY() - (player.getY() + 15 - gameData.getCameraY()), gameData.getMouseX() - (player.getX() + 15 - gameData.getCameraX()));
        world.addEntity(createBullet(player, gameData, world, angle));
    }

    @Override
    public void playershootrocket(GameData gameData, World world, Entity player, Entity playerWeapon) {
        float angle = (float) Math.atan2(gameData.getMouseY() - (player.getY() + 15 - gameData.getCameraY()), gameData.getMouseX() - (player.getX() + 15 - gameData.getCameraX()));
        world.addEntity(createRocket(playerWeapon, gameData, world, angle));
    }

    @Override
    public void enemyshoot(GameData gameData, World world, Entity enemy, Entity base, Entity player) {
        float distancePlayer = Float.MAX_VALUE;
        float distanceBase = Float.MAX_VALUE;

        distancePlayer = Math.abs(player.getX() - enemy.getX());
        distanceBase = Math.abs(base.getX() - enemy.getX());

        if (enemy.getX() + 30 > gameData.getCameraX() && enemy.getX() + 30 < gameData.getCameraX() + gameData.getDisplayWidth()) {

            if (distancePlayer > distanceBase) {
                
                for(Entity baseEntity : world.getEntities(BaseEntity.class)) {
                    shootDecision(enemy, baseEntity, world, gameData);
                }
                
            } else {
                for(Entity playerEntity : world.getEntities(PlayerEntity.class)) {
                    shootDecision(enemy, playerEntity, world, gameData);
                }
                
            }
        }
    }

}
