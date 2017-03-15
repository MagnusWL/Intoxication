/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package group04.weapon;

import java.util.Map.Entry;
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
                createWeapon(gameData, world, world.getEntity(e.getEntityID()), world.getEntity(e.getEntityID()).getWeaponType());
                gameData.removeEvent(e);
            }
        }

        for (Event e : gameData.getEvents(EventType.PLAYER_SWING)) {
            Entity player = world.getEntity(e.getEntityID());
            float angle = (float) Math.atan2(gameData.getMouseY() - (player.getY() + 15 - gameData.getCameraY()), gameData.getMouseX() - (player.getX() + 15 - gameData.getCameraX()));
            Entity weapon;
            for(Entity ent : world.getEntities(EntityType.WEAPON)) {
                weapon = ent;
            }
            
            
            
        }

        for (Entity weapon : world.getEntities(EntityType.WEAPON)) {
            Entity carrier = world.getEntity(weapon.getWeaponCarrier());
            weapon.setX(
                    carrier.getX());
            weapon.setY(carrier.getY());
            weapon.setVelocity(carrier.getVelocity());
            weapon.setTimeSinceAttack(weapon.getTimeSinceAttack() + 10 * gameData.getDelta());

            if (carrier.getEntityType() == EntityType.PLAYER && gameData.getKeys().isDown(GameKeys.MOUSE0) && weapon.getTimeSinceAttack() > weapon.getAttackCooldown()) {
                gameData.addEvent(new Event(EventType.PLAYER_SHOOT, weapon.getID()));
                weapon.setTimeSinceAttack(0);
            } else if (carrier.getEntityType() == EntityType.PLAYER && gameData.getKeys().isDown(GameKeys.MOUSE0) && weapon.getTimeSinceAttack() > weapon.getAttackCooldown()) {
                gameData.addEvent(new Event(EventType.PLAYER_SWING, weapon.getID()));
                weapon.setTimeSinceAttack(0);
            }

            if (carrier.getEntityType() == EntityType.ENEMY && weapon.getTimeSinceAttack() > weapon.getAttackCooldown()) {
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
        if (true) {
            createMelee(gameData, world, e, type);
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
        Entity weapon = new Entity();
        weapon.setEntityType(EntityType.WEAPON);
        weapon.setSprite("sword");
        weapon.setAttackCooldown(3);
        weapon.setTimeSinceAttack(0);
        weapon.setDamage(2);
        weapon.setWeaponCarrier(e.getID());
        weapon.setWeaponType(type);
        weapon.setShapeX(null);
        weapon.setShapeY(null);
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

}
