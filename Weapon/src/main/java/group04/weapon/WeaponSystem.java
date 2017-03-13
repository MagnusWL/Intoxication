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
                createGun(gameData, world, world.getEntity(e.getEntityID()));
                gameData.removeEvent(e);
            }
        }

        for (String key : world.getWeapons().keySet()) {

            Entity carrier = world.getEntity(key);
            Entity gun = world.getWeapons().get(key);
            gun.setX(
                    carrier.getX());
            gun.setY(carrier.getY());
            gun.setVelocity(carrier.getVelocity());
            gun.setTimeSinceAttack(gun.getTimeSinceAttack() + 10 * gameData.getDelta());
            
            if (carrier.getEntityType() == EntityType.PLAYER && gameData.getKeys().isDown(GameKeys.MOUSE0) && gun.getTimeSinceAttack() > gun.getAttackCooldown()) {
                gameData.addEvent(new Event(EventType.PLAYER_SHOOT, gun.getID()));
                gun.setTimeSinceAttack(0);
            }
            
            if (carrier.getEntityType() == EntityType.ENEMY && gun.getTimeSinceAttack() > gun.getAttackCooldown()) {
                gameData.addEvent(new Event(EventType.ENEMY_SHOOT, gun.getID()));
                gun.setTimeSinceAttack(0);
            }
        }
    }

    public void createGun(GameData gameData, World world, Entity e) {
        Entity weapon = new Entity();
        weapon.setEntityType(EntityType.WEAPON);
        weapon.setSprite("gun");
        weapon.setAttackCooldown(5);
        weapon.setTimeSinceAttack(0);
        world.getWeapons().put(e.getID(), weapon);
        world.addEntity(weapon);
    }

    @Override
    public void start(GameData gameData, World world) {

    }

    @Override
    public void stop(GameData gameData, World world) {
        for (Entity e : world.getWeapons().values()) {
            world.removeEntity(e);
        }
    }
}
