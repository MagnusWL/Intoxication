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
import group04.common.services.IServiceProcessor;

@ServiceProviders(value = {
    @ServiceProvider(service = IServiceProcessor.class),
    @ServiceProvider(service = IServiceInitializer.class)})

public class WeaponSystem implements IServiceProcessor, IServiceInitializer {

    @Override
    public void process(GameData gameData, World world) {
        for (Event e : gameData.getAllEvents()) {
            if (e.getType() == EventType.PICKUP_WEAPON) {
                if (world.getEntity(e.getEntityID()).getEntityType() == EntityType.ENEMY) {
                    createGun(gameData, world, world.getEntity(e.getEntityID()), WeaponType.GUN);
                }
                if (world.getEntity(e.getEntityID()).getEntityType() == EntityType.PLAYER) {
                    System.out.println("Player gets weapon");
                    createWeapon(gameData, world, world.getEntity(e.getEntityID()), WeaponType.ROCKET);
                }
                gameData.removeEvent(e);
            }
        }

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

        for (Entity weapon : world.getEntities(EntityType.WEAPON)) {
            Entity carrier = world.getEntity(weapon.getWeaponCarrier());
            float angle1 = (float) Math.atan2(gameData.getMouseY() - (carrier.getY() + 15 - gameData.getCameraY()) + 45, gameData.getMouseX() - (carrier.getX() + 15 - gameData.getCameraX()) - 45);
            float angle2 = (float) Math.atan2(gameData.getMouseY() - (carrier.getY() + 15 - gameData.getCameraY()) - 45, gameData.getMouseX() - (carrier.getX() + 15 - gameData.getCameraX()) - 45);
            swinging(angle1, angle2, weapon);
            if (carrier.getVelocity() < 0) {
                weapon.setX(carrier.getX() - 20);
                weapon.setY(carrier.getY() + 30);
            }
            if (carrier.getVelocity() > 0) {
                weapon.setX(carrier.getX() + 60);
                weapon.setY(carrier.getY() + 30);
            }

            weapon.setVelocity(carrier.getVelocity());
            weapon.setTimeSinceAttack(weapon.getTimeSinceAttack() + 10 * gameData.getDelta());

            if (weapon.getWeaponType() == WeaponType.GUN && carrier.getEntityType() == EntityType.PLAYER && gameData.getKeys().isDown(GameKeys.MOUSE0) && weapon.getTimeSinceAttack() > weapon.getAttackCooldown()) {
                gameData.addEvent(new Event(EventType.PLAYER_SHOOT, world.getEntity(weapon.getWeaponCarrier()).getID()));
                weapon.setTimeSinceAttack(0);
            } else if (weapon.getWeaponType() == WeaponType.MELEE && carrier.getEntityType() == EntityType.PLAYER && gameData.getKeys().isDown(GameKeys.MOUSE0) && weapon.getTimeSinceAttack() > weapon.getAttackCooldown()) {
                System.out.println("waddup");
                gameData.addEvent(new Event(EventType.PLAYER_SWING, world.getEntity(weapon.getWeaponCarrier()).getID()));
                weapon.setTimeSinceAttack(0);
            } else if (weapon.getWeaponType() == WeaponType.ROCKET && carrier.getEntityType() == EntityType.PLAYER && gameData.getKeys().isDown(GameKeys.MOUSE0) && weapon.getTimeSinceAttack() > weapon.getAttackCooldown()) {
                gameData.addEvent(new Event(EventType.PLAYER_SHOOT, world.getEntity(weapon.getWeaponCarrier()).getID()));
                weapon.setTimeSinceAttack(0);
            }

            if (weapon.getWeaponType() == WeaponType.GUN && carrier.getEntityType() == EntityType.ENEMY && weapon.getTimeSinceAttack() > weapon.getAttackCooldown()) {
                gameData.addEvent(new Event(EventType.ENEMY_SHOOT, weapon.getID()));
                weapon.setTimeSinceAttack(0);
            }

        }

    }

    public void createWeapon(GameData gameData, World world, Entity e, WeaponType type) {
        //Find out which weapon it is
        if (false) {
            createGun(gameData, world, e, type);
        }
        if (false) {
            createMelee(gameData, world, e, type);
        }
        if (true) {
            createRocket(gameData, world, e, type);
        }
    }

    public void createGun(GameData gameData, World world, Entity e, WeaponType type) {
        Entity weapon = new Entity();
        weapon.setEntityType(EntityType.WEAPON);
        weapon.setSprite("gun");
        weapon.setAttackCooldown(5);
        weapon.setTimeSinceAttack(0);
        weapon.setDamage(1);
        weapon.setWeaponCarrier(e.getID());
        weapon.setWeaponType(type);
        world.addEntity(weapon);
        world.getEntity(e.getID()).setWeaponOwned(weapon.getID());
    }

    private void createMelee(GameData gameData, World world, Entity e, WeaponType type) {
        System.out.println("sværd");
        Entity weapon = new Entity();
        weapon.setEntityType(EntityType.WEAPON);
        weapon.setSprite("sword");
        weapon.setAttackCooldown(3);
        weapon.setTimeSinceAttack(0);
        weapon.setDamage(2);
        weapon.setWeaponCarrier(e.getID());
        weapon.setWeaponType(type);
        weapon.setShapeX(new float[]{2, 32, 8, 2});
        weapon.setShapeY(new float[]{4, 36, 36, 4});
        world.addEntity(weapon);
        world.getEntity(e.getID()).setWeaponOwned(weapon.getID());
    }

    private void createRocket(GameData gameData, World world, Entity e, WeaponType type) {
        Entity weapon = new Entity();
        weapon.setEntityType(EntityType.WEAPON);
        weapon.setSprite("gun");
        weapon.setAttackCooldown(8);
        weapon.setTimeSinceAttack(0);
        weapon.setDamage(2);
        weapon.setWeaponCarrier(e.getID());
        weapon.setWeaponType(type);
        world.addEntity(weapon);

        world.getEntity(e.getID()).setWeaponOwned(weapon.getID());

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
        if (weapon.getAngle() > a1 && weapon.getAngle() <= a2) {
            weapon.setAngle(weapon.getAngle() + 0.5f);
            System.out.println("Angle: " + weapon.getAngle());
        } else {
            weapon.setSwinging(false);
        }
    }

}
