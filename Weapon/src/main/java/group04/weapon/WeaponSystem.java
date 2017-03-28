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
import group04.common.services.IWeaponService;
import group04.playercommon.PlayerEntity;
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
        if (type.toString().equals("SWORD")) {
            createMelee(gameData, world, e, type);
        }
        if (type.toString().equals("ROCKET")) {
            createRocket(gameData, world, e, type);
        }
    }

    public void createGun(GameData gameData, World world, Entity e, WeaponType type) {
        PlayerEntity player = (PlayerEntity) e;
        WeaponEntity weapon = new WeaponEntity();
        weapon.setEntityType(EntityType.WEAPON);
        weapon.setDrawable("gun");
        weapon.setAttackCooldown(5);
        weapon.setTimeSinceAttack(0);
        weapon.setWeaponCarrier(e.getID());
        weapon.setWeaponType(type);
        weapon.setX(e.getX());
        weapon.setY(e.getY());
        world.addEntity(weapon);
        player.setWeaponOwned(weapon.getID());
    }

    private void createMelee(GameData gameData, World world, Entity e, WeaponType type) {
        PlayerEntity player = (PlayerEntity) e;
        WeaponEntity weapon = new WeaponEntity();
        weapon.setEntityType(EntityType.WEAPON);
        weapon.setDrawable("sword");
        weapon.setAttackCooldown(3);
        weapon.setTimeSinceAttack(0);
        weapon.setDamage(2);
        weapon.setWeaponCarrier(e.getID());
        weapon.setWeaponType(type);
        weapon.setShapeX(new float[]{2, 32, 8, 2});
        weapon.setShapeY(new float[]{4, 36, 36, 4});
        world.addEntity(weapon);
        player.setWeaponOwned(weapon.getID());
    }

    private void createRocket(GameData gameData, World world, Entity e, WeaponType type) {
        PlayerEntity player = (PlayerEntity) e;
        WeaponEntity weapon = new WeaponEntity();
        weapon.setDrawable("gun");
        weapon.setAttackCooldown(8);
        weapon.setTimeSinceAttack(0);
        weapon.setDamage(2);
        weapon.setWeaponCarrier(e.getID());
        weapon.setWeaponType(type);
        weapon.setEntityType(EntityType.WEAPON);
        world.addEntity(weapon);
        player.setWeaponOwned(weapon.getID());
    }

    @Override
    public void start(GameData gameData, World world) {

    }

    @Override
    public void stop(GameData gameData, World world) {
        for (Entity weapon : world.getEntities(EntityType.WEAPON)) {
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
        WeaponEntity weapon = (WeaponEntity) world.getEntity(player.getWeaponOwned());

        Entity carrier = world.getEntity(weapon.getWeaponCarrier());

        float angle1 = (float) Math.atan2(gameData.getMouseY() - (carrier.getY() + 15 - gameData.getCameraY()) + 45, gameData.getMouseX() - (carrier.getX() + 15 - gameData.getCameraX()) - 45);
        float angle2 = (float) Math.atan2(gameData.getMouseY() - (carrier.getY() + 15 - gameData.getCameraY()) - 45, gameData.getMouseX() - (carrier.getX() + 15 - gameData.getCameraX()) - 45);
        //swinging(angle1, angle2, weapon, weaponContainer, (ImageContainer) weapon.getContainer(ImageContainer.class));

        if (player.getVelocity() < 0) {
            weapon.setX(carrier.getX() - 20);
            weapon.setY(carrier.getY() + 30);
        } else {
            weapon.setX(carrier.getX() + 60);
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
        PlayerEntity player = (PlayerEntity) playerEntity;
        WeaponEntity weapon = (WeaponEntity) world.getEntity(player.getWeaponOwned());
        Entity carrier = world.getEntity(weapon.getWeaponCarrier());

        float angle1 = (float) Math.atan2(gameData.getMouseY() - (carrier.getY() + 15 - gameData.getCameraY()) + 45, gameData.getMouseX() - (carrier.getX() + 15 - gameData.getCameraX()) - 45);
        float angle2 = (float) Math.atan2(gameData.getMouseY() - (carrier.getY() + 15 - gameData.getCameraY()) - 45, gameData.getMouseX() - (carrier.getX() + 15 - gameData.getCameraX()) - 45);
//        swinging(angle1, angle2, weapon, weapon, (ImageContainer) weapon.getContainer(ImageContainer.class));
        if (player.getVelocity() < 0) {
            weapon.setX(carrier.getX() - 20);
            weapon.setY(carrier.getY() + 30);
        }
        if (player.getVelocity() > 0) {
            weapon.setX(carrier.getX() + 60);
            weapon.setY(carrier.getY() + 30);
        }

        weapon.setTimeSinceAttack(weapon.getTimeSinceAttack() + 10 * gameData.getDelta());

        if (weapon.getWeaponType().toString().equals("GUN") && carrier.getEntityType() == enemy.getEntityType() && weapon.getTimeSinceAttack() > weapon.getAttackCooldown()) {
            gameData.addEvent(new Event(EventType.ENEMY_SHOOT, weapon.getWeaponCarrier()));
            weapon.setTimeSinceAttack(0);
        }
    }

    @Override
    public void pickUpWeapon(GameData gameData, World world) {
        // Mangler at blive lavet komponentbaseret
        // Muligvis en Eventtype der tillader at man sender et våben med
        for (Event e : gameData.getAllEvents()) {
            if (e.getType() == EventType.PICKUP_WEAPON) {
                createWeapon(gameData, world, world.getEntity(e.getEntityID()), WeaponType.GUN);
                gameData.removeEvent(e);
            }
        }
    }

}
