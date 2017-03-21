package group04.movement;

import group04.common.Entity;
import group04.common.EntityType;
import group04.common.GameData;
import group04.common.WeaponType;
import group04.common.World;
import group04.common.events.Event;
import group04.common.events.EventType;
import group04.common.services.ICollisionService;
import group04.common.services.IServiceProcessor;
import group04.datacontainers.MovementContainer;
import group04.datacontainers.ProjectileContainer;
import group04.datacontainers.WeaponContainer;
import org.openide.util.Lookup;
import org.openide.util.lookup.ServiceProvider;

@ServiceProvider(service = IServiceProcessor.class)

public class Processor implements IServiceProcessor {

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

                MovementContainer movementContainer = ((MovementContainer) entity.getContainer(MovementContainer.class));

                steps = (int) (Math.ceil(Math.abs(movementContainer.getVelocity())) + Math.ceil(Math.abs(movementContainer.getVerticalVelocity())));
                if (steps > 20) {
                    steps = 20;
                }
                for (int i = 0; i < steps; i++) {
                    //X
                    if (!e.isColliding(world, gameData, entity, movementContainer.getVelocity() * gameData.getDelta() * (1.0f / steps), 0)) {
                        entity.setX(entity.getX() + movementContainer.getVelocity() * gameData.getDelta() * (1.0f / steps));
                    } else {
                        movementContainer.setVelocity(0);
                        if (entity.getEntityType() == EntityType.PROJECTILE) {
                            world.removeEntity(entity);
                        }
                    }

                    //Y
                    if (!e.isColliding(world, gameData, entity, 0, movementContainer.getVerticalVelocity() * gameData.getDelta() * (1.0f / steps))) {
                        entity.setY(entity.getY() + movementContainer.getVerticalVelocity() * gameData.getDelta() * (1.0f / steps));
                    } else {
                        movementContainer.setVerticalVelocity(0);

                        if (entity.getEntityType() == EntityType.PROJECTILE) {
                            world.removeEntity(entity);
                        }
                    }
                }

                if (entity.getEntityType() == EntityType.PROJECTILE) {
                    ProjectileContainer projectileContainer = ((ProjectileContainer) entity.getContainer(ProjectileContainer.class));
                    if (!projectileContainer.isExplosive()) {
                        for (Entity entityHit : world.getEntities(EntityType.PLAYER, EntityType.ENEMY, EntityType.BASE)) {
                            if (entityHit.getEntityType() != projectileContainer.getShotFrom()
                                    && !(entityHit.getEntityType() == EntityType.BASE && entity.getEntityType() == EntityType.PLAYER)) {
                                if (e.isEntitiesColliding(world, gameData, entity, entityHit)) {
                                    gameData.addEvent(new Event(EventType.ENTITY_HIT, entityHit.getID()));
                                    world.removeEntity(entity);
                                    break;
                                }
                            }
                        }
                    } else {
                        for (Entity entityHit : world.getEntities(EntityType.PLAYER, EntityType.ENEMY, EntityType.BASE)) {
                            if (entityHit.getEntityType() != projectileContainer.getShotFrom()
                                    && !(entityHit.getEntityType() == EntityType.BASE && entity.getEntityType() == EntityType.PLAYER)) {
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
                    WeaponContainer weaponContainer = ((WeaponContainer) entity.getContainer(WeaponContainer.class));
                    if(weaponContainer.getWeaponType() == WeaponType.MELEE)
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
}
