package group04.movement;

import group04.common.Entity;
import group04.common.EntityType;
import group04.common.GameData;
import group04.common.World;
import group04.common.events.Event;
import group04.common.events.EventType;
import group04.common.services.ICollisionService;
import group04.common.services.IMovementService;
import group04.common.services.IServiceProcessor;
import group04.projectilecommon.ProjectileEntity;
import group04.weaponcommon.WeaponEntity;
import org.openide.util.Lookup;
import org.openide.util.lookup.ServiceProvider;

@ServiceProvider(service = IMovementService.class)

public class Processor implements IMovementService {

    private float steps = 10.0f;

    @Override
    public void process(GameData gameData, World world) {
        for (ICollisionService e : Lookup.getDefault().lookupAll(ICollisionService.class)) {

            for (Entity player : world.getEntities(EntityType.PLAYER)) {

                for (Entity loot : world.getEntities(EntityType.CURRENCY)) {

                    if (e.isEntitiesColliding(world, gameData, player, loot)) {
                        gameData.addEvent(new Event(EventType.PICKUP_CURRENCY, loot.getID()));
                    }
                }

                for (Entity boost : world.getEntities(EntityType.BOOST)) {

                    if (e.isEntitiesColliding(world, gameData, player, boost)) {
                        gameData.addEvent(new Event(EventType.PICKUP_BOOST, boost.getID()));
                    }
                }

            }

            for (Entity entity : world.getEntities(EntityType.PLAYER, EntityType.ENEMY, EntityType.PROJECTILE, EntityType.CURRENCY)) {

//                MovementContainer movementContainer = ((MovementContainer) entity.getContainer(MovementContainer.class));

                steps = (int) (Math.ceil(Math.abs(entity.getVelocity())) + Math.ceil(Math.abs(entity.getVerticalVelocity())));
                if (steps > 5) {
                    steps = 5;
                }
                for (int i = 0; i < steps; i++) {
                    //X
                    if (!e.isColliding(world, gameData, entity, entity.getVelocity() * gameData.getDelta() * (1.0f / steps), 0)) {
                        entity.setX(entity.getX() + entity.getVelocity() * gameData.getDelta() * (1.0f / steps));
                    } else {
                        entity.setVelocity(0);
                        if (entity.getEntityType() == EntityType.PROJECTILE) {
                            world.removeEntity(entity);
                        }
                    }

                    //Y
                    if (!e.isColliding(world, gameData, entity, 0, entity.getVerticalVelocity() * gameData.getDelta() * (1.0f / steps))) {
                        entity.setY(entity.getY() + entity.getVerticalVelocity() * gameData.getDelta() * (1.0f / steps));
                    } else {
                        entity.setVerticalVelocity(0);

                        if (entity.getEntityType() == EntityType.PROJECTILE) {
                            world.removeEntity(entity);
                        }
                    }
                }

                if (entity.getEntityType() == EntityType.PROJECTILE) {
//                    ProjectileContainer projectileContainer = ((ProjectileContainer) entity.getContainer(ProjectileContainer.class));
                    ProjectileEntity bullet = (ProjectileEntity) entity;
                    if (!bullet.isExplosive()) {
                        for (Entity entityHit : world.getEntities(EntityType.PLAYER, EntityType.ENEMY, EntityType.BASE)) {
                            if (entityHit.getEntityType() != bullet.getShotFrom()
                                    && !(entityHit.getEntityType() == EntityType.BASE && bullet.getShotFrom() == EntityType.PLAYER)) {
                                if (e.isEntitiesColliding(world, gameData, entity, entityHit)) {
                                    gameData.addEvent(new Event(EventType.ENTITY_HIT, entityHit.getID()));
                                    world.removeEntity(entity);
                                    break;
                                }
                            }
                        }
                    } else {
                        for (Entity entityHit : world.getEntities(EntityType.PLAYER, EntityType.ENEMY, EntityType.BASE)) {
                            if (entityHit.getEntityType() != bullet.getShotFrom()
                                    && !(entityHit.getEntityType() == EntityType.BASE && bullet.getShotFrom() == EntityType.PLAYER)) {
                                if (e.isEntitiesColliding(world, gameData, entity, entityHit)) {
                                    gameData.addEvent(new Event(EventType.ROCKET_HIT, entityHit.getID()));
                                    world.removeEntity(entity);
                                    break;
                                }
                            }
                        }
                    }
                }

                if (entity.getEntityType() == EntityType.EXPLOSION) {
                    for (Entity entityHit : world.getEntities(EntityType.PLAYER, EntityType.ENEMY, EntityType.BASE)) {
                        if (e.isEntitiesColliding(world, gameData, entity, entityHit)) {
                            gameData.addEvent(new Event(EventType.ENTITY_HIT, entityHit.getID()));
                            world.removeEntity(entity);
                            break;
                        }
                    }
                }

                if (entity.getEntityType() == EntityType.WEAPON) {
                    WeaponEntity weapon = (WeaponEntity) entity;
                    if(weapon.getWeaponType() == WeaponType.MELEE)
                    for (Entity entityHit : world.getEntities(EntityType.ENEMY, EntityType.PLAYER, EntityType.BASE)) {
                        if (e.isEntitiesColliding(world, gameData, entity, entityHit)) {
                            gameData.addEvent(new Event(EventType.ENTITY_HIT, entityHit.getID()));
                            break;
                        }
                    }
                }
            }

            break;
        }
    }

    @Override
    public void movementWhenGrounded(GameData gameData, World world, Entity entity) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void movementWhenNotGrounded(GameData gameData, World world, Entity entity) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void movementWhenColliding(GameData gameData, World world, Entity entity, Entity target) {
        
    }
}
