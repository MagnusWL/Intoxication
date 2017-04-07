/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package group04.weapon;

import org.openide.util.lookup.ServiceProvider;
import org.openide.util.lookup.ServiceProviders;
import group04.common.Entity;
import group04.common.EntityType;
import group04.common.GameData;
import group04.common.GameKeys;
import group04.common.WeaponType;
import group04.common.World;
import group04.common.events.Event;
import group04.common.events.EventType;
import group04.common.services.IServiceInitializer;
import group04.enemycommon.EnemyEntity;
import group04.playercommon.PlayerEntity;
import group04.weaponcommon.IWeaponService;
import group04.weaponcommon.WeaponEntity;

@ServiceProviders(value = {
    @ServiceProvider(service = IWeaponService.class),
    @ServiceProvider(service = IServiceInitializer.class)})

public class WeaponSystem implements IWeaponService, IServiceInitializer {

    // ROCKET shoot - skal kigges på!
    /* for (Event e : gameData.getAllEvents()) {
            if (e.getType() == EventType.ROCKET_HIT) {
                for (Entity entity : world.getEntities(EntityType.PLAYER)) {
                    Entity entityHit = world.getEntity(e.getEntityID());
                    Entity player = entity;
                    Entity explosion = new Entity();
                    explosion.setExplosionRadius(world.getEntity(player.getWeaponOwned()).getExplosionRadius());
                    explosion.setDamage(world.getEntity(player.getWeaponOwned()).getDamage());
                    explosion.setX(entityHit.getX());
                    explosion.setY(entityHit.getY() + 50);
                    explosion.setShapeX(new float[]{0, 0, world.getEntity(player.getWeaponOwned()).getExplosionRadius() * 2, world.getEntity(player.getWeaponOwned()).getExplosionRadius() * 2});
                    explosion.setShapeY(new float[]{0, world.getEntity(player.getWeaponOwned()).getExplosionRadius() * 2, world.getEntity(player.getWeaponOwned()).getExplosionRadius() * 2, 0});
                    explosion.setSprite("explosion");
                    world.addEntity(explosion);
                }
            }
        }*/
//        for (Event e : gameData.getEvents(EventType.PLAYER_SWING)) {
//            Entity player = world.getEntity(e.getEntityID());
//            Entity weapon = null;
//            for (Entity ent : world.getEntities(EntityType.WEAPON)) {
//                weapon = ent;
//            }
//            //swing
//            System.out.println("HAJ");
//            weapon.setSwinging(true);
//            Entity carrier = world.getEntity(weapon.getWeaponCarrier());
//            float angle = (float) Math.atan2(gameData.getMouseY() - (carrier.getY() + 15 - gameData.getCameraY()), gameData.getMouseX() - (player.getX() + 15 - gameData.getCameraX()));
//            weapon.setAngle(angle);
//        }
    public void createWeapon(GameData gameData, World world, Entity e, WeaponType type) {
        //Find out which weapon it is
        if (type == WeaponType.GUN) {
            createGun(gameData, world, e, type);
        }
        if (type == WeaponType.MELEE) {
            createMelee(gameData, world, e, type);
        }
        if (type == WeaponType.ROCKET) {
            createRocket(gameData, world, e, type);
        }
    }

    public void createGun(GameData gameData, World world, Entity e, WeaponType type) {
        WeaponEntity weapon = new WeaponEntity();
        weapon.setEntityType(EntityType.WEAPON);

        /*        loadPNGAnimation("player_weapon_melee_champaign_attack_animation.png", 110, 166, 5);
        loadPNGAnimation("player_weapon_melee_champaign_run_animation.png", 108, 100, 5);
        loadPNGAnimation("player_weapon_ranged_champaign_attack_animation.png", 105, 132, 5);
        loadPNGAnimation("player_weapon_ranged_throwBottle_attack_animation.png", 111, 66, 5);
         */
        weapon.setAnimateable(true);
        weapon.setCurrentAnimation("player_weapon_ranged_throwBottle_attack_animation");
        weapon.setCurrentFrame(10);
        weapon.setAttackCooldown(5);
        weapon.setTimeSinceAttack(0);
        weapon.setWeaponCarrier(e.getID());
        weapon.setyCenter(20);
        weapon.setWeaponType(type);
        weapon.setX(e.getX());
        weapon.setY(e.getY());
        world.addEntity(weapon);
        if (e.getClass() == PlayerEntity.class) {
            PlayerEntity player = (PlayerEntity) e;
            player.setWeaponOwned(weapon);
        }
        if (e.getClass() == EnemyEntity.class) {
            EnemyEntity enemy = (EnemyEntity) e;
            enemy.setWeaponOwned(weapon);
        }
    }

    private void createMelee(GameData gameData, World world, Entity e, WeaponType type) {
        WeaponEntity weapon = new WeaponEntity();
        weapon.setEntityType(EntityType.WEAPON);
        weapon.setAnimateable(true);
        weapon.setCurrentAnimation("player_weapon_melee_champaign_attack_animation");
        weapon.setAttackCooldown(3);
        weapon.setTimeSinceAttack(0);
        weapon.setCurrentFrame(6);
        weapon.setxCenter(20);
        weapon.setyCenter(81);
        weapon.setDamage(15);
        weapon.setWeaponCarrier(e.getID());
        weapon.setWeaponType(type);
        weapon.setShapeX(new float[]{-30, -30, 30, 30});
        weapon.setShapeY(new float[]{30, -30, -30, 30});
        world.addEntity(weapon);
        if (e.getClass() == PlayerEntity.class) {
            PlayerEntity player = (PlayerEntity) e;
            player.setWeaponOwned(weapon);
        }
        if (e.getClass() == EnemyEntity.class) {
            EnemyEntity enemy = (EnemyEntity) e;
            enemy.setWeaponOwned(weapon);
        }
    }

    private void createRocket(GameData gameData, World world, Entity e, WeaponType type) {
        WeaponEntity weapon = new WeaponEntity();
        weapon.setAnimateable(true);
        weapon.setCurrentAnimation("player_weapon_ranged_throwbottle_attack_animation");
        weapon.setAttackCooldown(8);
        weapon.setTimeSinceAttack(0);
        weapon.setDamage(2);
        weapon.setWeaponCarrier(e.getID());
        weapon.setWeaponType(type);
        weapon.setEntityType(EntityType.WEAPON);
        world.addEntity(weapon);
        if (e.getClass() == PlayerEntity.class) {
            PlayerEntity player = (PlayerEntity) e;
            player.setWeaponOwned(weapon);
        }
        if (e.getClass() == EnemyEntity.class) {
            EnemyEntity enemy = (EnemyEntity) e;
            enemy.setWeaponOwned(weapon);
        }
    }

    @Override
    public void start(GameData gameData, World world) {

    }

    @Override
    public void stop(GameData gameData, World world) {
        for (Entity weapon : world.getEntities(WeaponEntity.class)) {
            world.removeEntity(weapon);
        }
    }

    private void swinging(float a1, float a2, Entity weapon) {
        /*      LOOK AT DIS SHIT  
        if (imageContainer.getAngle() > a1 && imageContainer.getAngle() <= a2) {
            imageContainer.setAngle(imageContainer.getAngle() + 0.5f);
            System.out.println("Angle: " + imageContainer.getAngle());
        } else {
            weaponContainer.setSwinging(false);
        }*/
    }

    @Override
    public void playerAttack(GameData gameData, World world, Entity entity) {
        PlayerEntity player = (PlayerEntity) entity;
        WeaponEntity weapon = (WeaponEntity) player.getWeaponOwned();

        Entity carrier = world.getEntity(weapon.getWeaponCarrier());

//        float angle1 = (float) Math.atan2(gameData.getMouseY() - (carrier.getY() - gameData.getCameraY()), gameData.getMouseX() - (carrier.getX() - gameData.getCameraX()));
//        float angle2 = (float) Math.atan2(gameData.getMouseY() - (carrier.getY() - gameData.getCameraY()), gameData.getMouseX() - (carrier.getX() - gameData.getCameraX()));
        //swinging(angle1, angle2, weapon, weaponContainer, (ImageContainer) weapon.getContainer(ImageContainer.class));
        if (gameData.getMouseX() < player.getX() - gameData.getCameraX()) {
            //FLIP
            weapon.setX(carrier.getX() - 75);
            weapon.setY(carrier.getY() + 30);
        } else {
            weapon.setX(carrier.getX() + 75);
            weapon.setY(carrier.getY() + 30);

        }

        weapon.setTimeSinceAttack(weapon.getTimeSinceAttack() + 10 * gameData.getDelta());

        if (weapon.getWeaponType() == WeaponType.GUN && carrier.getEntityType() == player.getEntityType() && gameData.getKeys().isDown(GameKeys.MOUSE0) && weapon.getTimeSinceAttack() > weapon.getAttackCooldown()) {
            gameData.addEvent(new Event(EventType.PLAYER_SHOOT_GUN, player.getID()));
            weapon.setTimeSinceAttack(0);
        } else if (weapon.getWeaponType() == WeaponType.MELEE && carrier.getEntityType() == player.getEntityType() && gameData.getKeys().isDown(GameKeys.MOUSE0) && weapon.getTimeSinceAttack() > weapon.getAttackCooldown()) {
            gameData.addEvent(new Event(EventType.PLAYER_SWING, player.getID()));
            weapon.setTimeSinceAttack(0);
        } else if (weapon.getWeaponType() == WeaponType.ROCKET && carrier.getEntityType() == player.getEntityType() && gameData.getKeys().isDown(GameKeys.MOUSE0) && weapon.getTimeSinceAttack() > weapon.getAttackCooldown()) {
            gameData.addEvent(new Event(EventType.PLAYER_SHOOT_ROCKET, player.getID()));
            weapon.setTimeSinceAttack(0);
        }
    }

    @Override
    public void enemyAttack(GameData gameData, World world, Entity enemy, Entity playerEntity, Entity base) {
        EnemyEntity enemyEntity = (EnemyEntity) enemy;
        WeaponEntity weapon = (WeaponEntity) enemyEntity.getWeaponOwned();

        if (enemyEntity.getVelocity() < 0) {
            weapon.setX(enemyEntity.getX() - 20);
            weapon.setY(enemyEntity.getY() + 30);
        } else {
            weapon.setX(enemyEntity.getX() + 60);
            weapon.setY(enemyEntity.getY() + 30);
        }

        weapon.setTimeSinceAttack(weapon.getTimeSinceAttack() + 10 * gameData.getDelta());

        if (weapon.getWeaponType().toString().equals("GUN") && enemyEntity.getEntityType() == enemy.getEntityType() && weapon.getTimeSinceAttack() > weapon.getAttackCooldown()) {
            gameData.addEvent(new Event(EventType.ENEMY_SHOOT, weapon.getWeaponCarrier()));
            weapon.setTimeSinceAttack(0);
        }

        if (!enemyEntity.getCurrentAnimation().equals(enemyEntity.getAttackAnimation()) && weapon.getWeaponType() == WeaponType.MELEE
                && enemyEntity.getEntityType() == enemy.getEntityType() && weapon.getTimeSinceAttack() > weapon.getAttackCooldown()
                && Math.abs(enemyEntity.getX() - playerEntity.getX()) < 100) {
            enemyEntity.setCurrentAnimation(enemyEntity.getAttackAnimation());
//            enemyEntity.setCurrentFrame(10);
            gameData.addEvent(new Event(EventType.ENEMY_SWING, weapon.getWeaponCarrier()));
            weapon.setTimeSinceAttack(0);
        }
    }

    @Override
    public void pickUpWeapon(GameData gameData, World world) {
        // Mangler at blive lavet komponentbaseret
        // Muligvis en Eventtype der tillader at man sender et våben med
        for (Event e : gameData.getAllEvents()) {
            if (e.getType() == EventType.PICKUP_WEAPON) {
                if (world.getEntity(e.getEntityID()).getClass() == EnemyEntity.class) {
                    createWeapon(gameData, world, world.getEntity(e.getEntityID()), WeaponType.MELEE);
                } else {
                    createWeapon(gameData, world, world.getEntity(e.getEntityID()), WeaponType.MELEE);
                }

                gameData.removeEvent(e);
            }
        }
    }

}
