package group04.movement;

import group04.basecommon.BaseEntity;
import group04.collisioncommon.ICollisionService;
import group04.common.Entity;
import group04.common.GameData;
import group04.common.World;
import group04.common.events.Event;
import group04.common.events.EventType;
import group04.common.services.IServiceProcessor;
import group04.enemycommon.EnemyEntity;
import group04.itemdropscommon.IDropService;
import group04.itemdropscommon.ItemEntity;
import group04.movementcommon.IMovementService;
import group04.playercommon.PlayerEntity;
import group04.projectilecommon.ProjectileEntity;
import group04.weaponcommon.WeaponEntity;
import group04.weaponcommon.WeaponType;
import org.openide.util.Lookup;
import org.openide.util.lookup.ServiceProvider;

@ServiceProvider(service = IServiceProcessor.class)

public class MovementSystem implements IServiceProcessor {

    private float steps = 10.0f;

    public void pickUpItemEvent(World world, ICollisionService e) {
        for (Entity player : world.getEntities(PlayerEntity.class)) {

            for (Entity loot : world.getEntities(ItemEntity.class)) {
                if (e.isEntitiesColliding(player, loot)) {
                    PlayerEntity playerEntity = (PlayerEntity) player;

                    for (IDropService i : Lookup.getDefault().lookupAll(IDropService.class)) {
                        i.pickUpItem(world, playerEntity, loot);
                    }
                }
            }
        }
    }

    public void movement(World world, GameData gameData, ICollisionService e) {
        for (Entity entity : world.getAllEntities()) {
            if (entity.isHasGravity()) {
                //Checks for collision in steps for X & Y
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
                        if (entity.getClass() == ProjectileEntity.class) {
                            world.removeEntity(entity);
                        }
                    }

                    //Y
                    if (!e.isColliding(world, gameData, entity, 0, entity.getVerticalVelocity() * gameData.getDelta() * (1.0f / steps))) {
                        entity.setY(entity.getY() + entity.getVerticalVelocity() * gameData.getDelta() * (1.0f / steps));
                    } else {
                        entity.setVerticalVelocity(0);

                        if (entity.getClass() == ProjectileEntity.class) {
                            world.removeEntity(entity);
                        }
                    }
                }
            }
        }
    }

    public void collision(World world, GameData gameData, ICollisionService e) {
        for (Entity entity : world.getAllEntities()) {
            if (entity.isHasGravity()) {
                if (entity.getClass() == ProjectileEntity.class) {
                    ProjectileEntity bullet = (ProjectileEntity) entity;
                    if (!bullet.isExplosive()) {
                        for (Entity entityHit : world.getEntities(PlayerEntity.class, EnemyEntity.class, BaseEntity.class)) {
                            if (entityHit.getClass() != bullet.getShotFrom().getClass()
                                    && !(entityHit.getClass() == BaseEntity.class && bullet.getShotFrom().getClass() == PlayerEntity.class)) {
                                if (e.isEntitiesColliding(entity, entityHit)) {
                                    gameData.addEvent(new Event(EventType.ENTITY_HIT, entityHit.getID()));
                                    world.removeEntity(entity);
                                    break;
                                }
                            }
                        }
                    } else {
                        for (Entity entityHit : world.getEntities(PlayerEntity.class, EnemyEntity.class, BaseEntity.class)) {
                            if (entityHit.getClass() != bullet.getShotFrom().getClass()
                                    && !(entityHit.getClass() == BaseEntity.class && bullet.getShotFrom().getClass() == PlayerEntity.class)) {
                                if (e.isEntitiesColliding(entity, entityHit)) {
                                    gameData.addEvent(new Event(EventType.ROCKET_HIT, entityHit.getID()));
                                    world.removeEntity(entity);
                                    break;
                                }
                            }
                        }
                    }
                }

                if (entity.getClass() == WeaponEntity.class) {
                    WeaponEntity weapon = (WeaponEntity) entity;
                    if (weapon.getWeaponType() == WeaponType.MELEE) {
                        for (Entity entityHit : world.getEntities(EnemyEntity.class, PlayerEntity.class, BaseEntity.class)) {
                            if (e.isEntitiesColliding(entity, entityHit)) {
                                gameData.addEvent(new Event(EventType.ENTITY_HIT, entityHit.getID()));
                                break;
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public void process(GameData gameData, World world) {
        for (ICollisionService e : Lookup.getDefault().lookupAll(ICollisionService.class)) {
            pickUpItemEvent(world, e);
            movement(world, gameData, e);
            collision(world, gameData, e);
        }
    }
}
