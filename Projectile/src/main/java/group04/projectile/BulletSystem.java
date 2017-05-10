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
import group04.common.services.IServiceProcessor;
import group04.playercommon.PlayerEntity;
import group04.projectilecommon.IProjectileService;
import group04.projectilecommon.ProjectileEntity;

/**
 *
 * @author burno
 */
@ServiceProviders(value = {
    @ServiceProvider(service = IServiceProcessor.class),
    @ServiceProvider(service = IServiceInitializer.class),
    @ServiceProvider(service = IProjectileService.class)})

public class BulletSystem implements IServiceInitializer, IProjectileService, IServiceProcessor {

    private Entity createBullet(Entity entity, GameData gameData, World world, float angle) {
        ProjectileEntity bullet = new ProjectileEntity();
        bullet.setEntityType(EntityType.PROJECTILE);

        bullet.setDrawable("beerbottle");
        bullet.setDestroyProjectileAudio("bottle_destroy");
        bullet.setAngle(angle);

        bullet.setVelocity((float) (850 * Math.cos(angle)));
        bullet.setVerticalVelocity((float) (850 * Math.sin(angle)));

        bullet.setHasGravity(true);
        int spriteWidth = gameData.getSpriteInfo().get(bullet.getDrawable())[0];
        int spriteHeight = gameData.getSpriteInfo().get(bullet.getDrawable())[1];
        bullet.setShapeX(new float[]{-(spriteWidth / 2) * gameData.getHitBoxScale(), -(spriteWidth / 2) * gameData.getHitBoxScale(),
            spriteWidth / 2 * gameData.getHitBoxScale(), spriteWidth / 2 * gameData.getHitBoxScale()});
        bullet.setShapeY(new float[]{-(spriteHeight / 2) * gameData.getHitBoxScale(), spriteHeight / 2 * gameData.getHitBoxScale(),
            spriteHeight / 2 * gameData.getHitBoxScale(), -(spriteHeight / 2 * gameData.getHitBoxScale())});

        bullet.setShotFrom(entity.getEntityType());
        bullet.setExplosive(false);

        bullet.setX(entity.getX());
        bullet.setY(entity.getY());

        return bullet;
    }

    private Entity createRocket(Entity entity, GameData gameData, World world, float angle) {
        ProjectileEntity rocket = new ProjectileEntity();
        rocket.setEntityType(EntityType.PROJECTILE);

        rocket.setDrawable("rocket");
        rocket.setAngle(angle);

        rocket.setVelocity((float) (350 * Math.cos(angle)));
        rocket.setVerticalVelocity((float) (350 * Math.sin(angle)));

        int spriteWidth = gameData.getSpriteInfo().get(rocket.getDrawable())[0];
        int spriteHeight = gameData.getSpriteInfo().get(rocket.getDrawable())[1];
        rocket.setShapeX(new float[]{-(spriteWidth / 2) * gameData.getHitBoxScale(), -(spriteWidth / 2) * gameData.getHitBoxScale(),
            spriteWidth / 2 * gameData.getHitBoxScale(), spriteWidth / 2 * gameData.getHitBoxScale()});
        rocket.setShapeY(new float[]{-(spriteHeight / 2) * gameData.getHitBoxScale(), spriteHeight / 2 * gameData.getHitBoxScale(),
            spriteHeight / 2 * gameData.getHitBoxScale(), -(spriteHeight / 2 * gameData.getHitBoxScale())});

        rocket.setShotFrom(entity.getEntityType());
        rocket.setExplosive(true);
        rocket.setExplosionRadius(40);

        rocket.setX(entity.getX());
        rocket.setY(entity.getY());

        return rocket;
    }

    private Entity createMeleeHit(Entity entity, GameData gameData, World world, float x, float y) {
        ProjectileEntity melee = new ProjectileEntity();
        melee.setEntityType(EntityType.PROJECTILE);
        melee.setHasGravity(true);
        melee.setShapeX(new float[]{-45, -45, 45, 45});
        melee.setShapeY(new float[]{45, -45, -45, 45});
        melee.setShotFrom(entity.getEntityType());
        melee.setDestructionTimer(30);
        melee.setX(x);
        melee.setY(y);
        return melee;
    }

    @Override
    public void start(GameData gameData, World world) {

    }

    @Override
    public void stop(GameData gameData, World world) {

    }

    private void shootDecision(Entity enemy, Entity target, World world, GameData gameData, double k1, double k2, double k3) {
//      GENETIC AI
        double xdif = Math.abs(target.getX() - enemy.getX());

        double overHead = k1 + k2 * xdif;

        double ydif = (target.getY() + overHead) - enemy.getY();
        float angle = (float) Math.atan2(ydif, target.getX() - enemy.getX());

        world.addEntity(createBullet(enemy, gameData, world, angle));
    }

    @Override
    public void playermeleeattack(GameData gameData, World world, Entity player, Entity playerWeapon) {
        float angle = (float) Math.atan2(gameData.getMouseY() - player.getY(), gameData.getMouseX() - (player.getX() - gameData.getCameraX()));
        float x = (float) (player.getX() + 80 * Math.cos(angle));
        float y = (float) (player.getY() + 80 * Math.sin(angle));
        world.addEntity(createMeleeHit(player, gameData, world, x, y));
    }

    @Override
    public void playershootgun(GameData gameData, World world, Entity player, Entity playerWeapon) {
        float angle = (float) Math.atan2(gameData.getMouseY() - (player.getY() - gameData.getCameraY()), gameData.getMouseX() - (player.getX() - gameData.getCameraX()));
        world.addEntity(createBullet(player, gameData, world, angle));
    }

    @Override
    public void playershootrocket(GameData gameData, World world, Entity player, Entity playerWeapon) {
        float angle = (float) Math.atan2(gameData.getMouseY() - (player.getY() - gameData.getCameraY()), gameData.getMouseX() - (player.getX() - gameData.getCameraX()));
        world.addEntity(createRocket(playerWeapon, gameData, world, angle));
    }

    @Override
    public void enemyshoot(GameData gameData, World world, Entity enemy, Entity base, Entity player, double k1, double k2, double k3) {
        float distancePlayer = Float.MAX_VALUE;
        float distanceBase = Float.MAX_VALUE;

        distancePlayer = Math.abs(player.getX() - enemy.getX());
        distanceBase = Math.abs(base.getX() - enemy.getX());

        if (enemy.getX() > gameData.getCameraX() && enemy.getX() < gameData.getCameraX() + gameData.getDisplayWidth()) {

            if (distancePlayer > distanceBase) {
                shootDecision(enemy, base, world, gameData, k1, k2, k3);
            } else {
                System.out.println("shootplayer");
                shootDecision(enemy, player, world, gameData, k1, k2, k3);

            }
        }
    }

    @Override
    public void process(GameData gameData, World world) {
        for (Entity e : world.getEntities(ProjectileEntity.class)) {
            ProjectileEntity projectile = (ProjectileEntity) e;
            projectile.setAngle((float) Math.atan2(projectile.getVerticalVelocity(), projectile.getVelocity()));
            if (projectile.getDestructionTimer() > 0) {
                projectile.setDestructionTimer(projectile.getDestructionTimer() - 1);
                if (projectile.getDestructionTimer() == 0) {
                    world.removeEntity(projectile);
                }
            }
        }
    }

    @Override
    public void aiEnemyshoot(GameData gameData, World world, Entity enemy, Entity player, double k1, double k2, double k3) {
        shootDecision(enemy, player, world, gameData, k1, k2, k3);
    }

}
