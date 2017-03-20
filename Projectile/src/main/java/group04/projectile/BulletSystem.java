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
import group04.datacontainers.CollisionContainer;
import group04.datacontainers.HealthContainer;
import group04.datacontainers.ImageContainer;
import group04.datacontainers.MovementContainer;
import group04.datacontainers.ProjectileContainer;
import group04.datacontainers.WeaponContainer;

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

        ImageContainer imageContainer = new ImageContainer();
        imageContainer.setSprite("bullet");
        imageContainer.setAngle(angle);

        MovementContainer movementContainer = new MovementContainer();
        movementContainer.setVelocity((float) (350 * Math.cos(angle)));
        movementContainer.setVerticalVelocity((float) (350 * Math.sin(angle)));

        CollisionContainer collisionContainer = new CollisionContainer();
        collisionContainer.setShapeX(new float[]{0, 5, 5, 0});
        collisionContainer.setShapeY(new float[]{5, 5, 0, 0});

        ProjectileContainer projectileContainer = new ProjectileContainer();
        projectileContainer.setShotFrom(world.getEntity(((WeaponContainer) weapon.getContainer(WeaponContainer.class)).getWeaponCarrier()).getEntityType());
        projectileContainer.setExplosive(false);

        bullet.setX(weapon.getX() + 35 + ((float) Math.cos(angle) * 50));
        bullet.setY(weapon.getY() + 35 + ((float) Math.sin(angle) * 50));

        bullet.addContainer(projectileContainer);
        bullet.addContainer(movementContainer);
        bullet.addContainer(collisionContainer);
        bullet.addContainer(imageContainer);

        bullets.add(bullet);
        return bullet;
    }

    private Entity createRocket(Entity entity, GameData gameData, World world, float angle) {
        Entity rocket = new Entity();
        rocket.setEntityType(EntityType.PROJECTILE);

        ImageContainer imageContainer = new ImageContainer();
        imageContainer.setSprite("rocket");
        imageContainer.setAngle(angle);

        MovementContainer movementContainer = new MovementContainer();
        movementContainer.setVelocity((float) (350 * Math.cos(angle)));
        movementContainer.setVerticalVelocity((float) (350 * Math.sin(angle)));

        CollisionContainer collisionContainer = new CollisionContainer();
        collisionContainer.setShapeX(new float[]{0, 5, 5, 0});
        collisionContainer.setShapeY(new float[]{5, 5, 0, 0});

        ProjectileContainer projectileContainer = new ProjectileContainer();
        projectileContainer.setShotFrom(world.getEntity(((WeaponContainer) entity.getContainer(WeaponContainer.class)).getWeaponCarrier()).getEntityType());
        projectileContainer.setExplosive(true);
        projectileContainer.setExplosionRadius(40);

        rocket.setX(entity.getX() + 35 + ((float) Math.cos(angle) * 50));
        rocket.setY(entity.getY() + 35 + ((float) Math.sin(angle) * 50));

        rocket.addContainer(projectileContainer);
        rocket.addContainer(movementContainer);
        rocket.addContainer(collisionContainer);
        rocket.addContainer(imageContainer);

        bullets.add(rocket);
        return rocket;
    }

    @Override
    public void process(GameData gameData, World world) {
        for (Event e : gameData.getAllEvents()) {
            if (e.getType() == EventType.PLAYER_SHOOT) {
                Entity player = world.getEntity(e.getEntityID());
                Entity playerWeapon = world.getEntity(((HealthContainer)player.getContainer(HealthContainer.class)).getWeaponOwned());
                WeaponContainer weaponContainer = ((WeaponContainer)playerWeapon.getContainer(WeaponContainer.class));

                float angle = (float) Math.atan2(gameData.getMouseY() - (player.getY() + 15 - gameData.getCameraY()), gameData.getMouseX() - (player.getX() + 15 - gameData.getCameraX()));

                if (weaponContainer.getWeaponType() == WeaponType.ROCKET) {
                    world.addEntity(createRocket(playerWeapon, gameData, world, angle));
                }
                if (weaponContainer.getWeaponType() == WeaponType.GUN) {
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
