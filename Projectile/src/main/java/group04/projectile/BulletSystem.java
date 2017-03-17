/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package group04.projectile;

import java.util.ArrayList;
import org.openide.util.lookup.ServiceProvider;
import org.openide.util.lookup.ServiceProviders;
import group04.common.GameData;
import group04.common.World;
import group04.common.services.IServiceInitializer;
import group04.common.services.IServiceProcessor;
import group04.common.Entity;
import group04.common.EntityType;
import group04.common.WeaponType;
import group04.common.events.Event;
import group04.common.events.EventType;

/**
 *
 * @author burno
 */
@ServiceProviders(value = {
    @ServiceProvider(service = IServiceProcessor.class),
    @ServiceProvider(service = IServiceInitializer.class)})

public class BulletSystem implements IServiceProcessor, IServiceInitializer {

    private ArrayList<Entity> bullets = new ArrayList<>();

    private Entity createBullet(Entity weapon, GameData gameData, World world, float angle) {
        Entity bullet = new Entity();
        bullet.setEntityType(EntityType.PROJECTILE);
        bullet.setAngle(angle);
        bullet.setShotFrom(world.getEntity(weapon.getWeaponCarrier()).getEntityType());
        bullet.setVelocity((float) (350 * Math.cos(angle)));
        bullet.setVerticalVelocity((float) (350 * Math.sin(angle)));
        bullet.setSprite("bullet");
        bullet.setX(weapon.getX() + 35 + ((float) Math.cos(angle) * 50));
        bullet.setY(weapon.getY() + 35 + ((float) Math.sin(angle) * 50));
        bullet.setShapeX(new float[]{0, 5, 5, 0});
        bullet.setShapeY(new float[]{5, 5, 0, 0});
        bullet.setExplosive(false);

        bullets.add(bullet);
        return bullet;
    }

    private Entity createRocket(Entity entity, GameData gameData, World world, float angle) {
        Entity rocket = new Entity();
        rocket.setEntityType(EntityType.PROJECTILE);
        rocket.setAngle(angle);
        rocket.setVelocity((float) (350 * Math.cos(angle)));
        rocket.setVerticalVelocity((float) (350 * Math.sin(angle)));
        rocket.setSprite("rocket");
        rocket.setX(entity.getX() + 35 + ((float) Math.cos(angle) * 50));
        rocket.setY(entity.getY() + 35 + ((float) Math.sin(angle) * 50));
        rocket.setShapeX(new float[]{0, 5, 5, 0});
        rocket.setShapeY(new float[]{5, 5, 0, 0});
        rocket.setExplosive(true);
        rocket.setExplosionRadius(40);

        bullets.add(rocket);
        return rocket;
    }

    @Override
    public void process(GameData gameData, World world) {
        for (Event e : gameData.getAllEvents()) {
            if (e.getType() == EventType.PLAYER_SHOOT) {
                Entity player = world.getEntity(e.getEntityID());
                Entity playerWeapon = world.getEntity(player.getWeaponOwned());
                float angle = (float) Math.atan2(gameData.getMouseY() - (player.getY() + 15 - gameData.getCameraY()), gameData.getMouseX() - (player.getX() + 15 - gameData.getCameraX()));

                if (world.getEntity(player.getWeaponOwned()).getWeaponType() == WeaponType.ROCKET) {
                    world.addEntity(createRocket(playerWeapon, gameData, world, angle));
                }
                if (world.getEntity(player.getWeaponOwned()).getWeaponType() == WeaponType.GUN) {
                    world.addEntity(createBullet(playerWeapon, gameData, world, angle));
                }
                gameData.removeEvent(e);
            }

            if (e.getType() == EventType.ENEMY_SHOOT) {
                Entity enemyWeapon = world.getEntity(e.getEntityID());
                float distancePlayer = Float.MAX_VALUE;
                float distanceBase = Float.MAX_VALUE;

                for (Entity player : world.getEntities(EntityType.PLAYER)) {
                    distancePlayer = Math.abs(player.getX() - enemyWeapon.getX());
                }
                for (Entity base : world.getEntities(EntityType.BASE)) {
                    distanceBase = Math.abs(base.getX() - enemyWeapon.getX());
                }

                if (enemyWeapon.getX() + 30 > gameData.getCameraX() && enemyWeapon.getX() + 30 < gameData.getCameraX() + gameData.getDisplayWidth()) {

                    if (distancePlayer > distanceBase) {
                        shootDecision(enemyWeapon, EntityType.BASE, world, gameData);
                    } else {
                        shootDecision(enemyWeapon, EntityType.PLAYER, world, gameData);
                    }
                }
                gameData.removeEvent(e);
            }
        }
    }

    @Override
    public void start(GameData gameData, World world) {

    }

    @Override
    public void stop(GameData gameData, World world) {
        for (Entity e : bullets) {
            world.removeEntity(e);
        }
    }

    private void shootDecision(Entity enemy, EntityType entity, World world, GameData gameData) {

        for (Entity target : world.getEntities(entity)) {
            if (entity == EntityType.PLAYER) {
                float angle = (float) Math.atan2((target.getY() + 15) - (enemy.getY() + 15), (target.getX() + 15) - (enemy.getX() + 15));
                world.addEntity(createBullet(enemy, gameData, world, angle));
            } else {
                float angle = (float) Math.atan2(target.getY() + 50 - (enemy.getY() + 15), (target.getX() + 50) - (enemy.getX() + 15));
                world.addEntity(createBullet(enemy, gameData, world, angle));
            }
        }
    }
}
