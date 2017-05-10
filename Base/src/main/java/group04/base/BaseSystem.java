package group04.base;

import group04.basecommon.BaseEntity;
import org.openide.util.lookup.ServiceProvider;
import org.openide.util.lookup.ServiceProviders;
import group04.common.Entity;
import group04.common.GameData;
import group04.common.GameKeys;
import group04.common.World;
import group04.common.events.Event;
import group04.common.events.EventType;
import group04.common.services.IServiceInitializer;
import group04.common.services.IServiceProcessor;
import group04.platformcommon.PlatformEntity;
import group04.playercommon.PlayerEntity;
import group04.upgradecommon.UpgradeEntity;
import group04.weaponcommon.WeaponEntity;

@ServiceProviders(value = {
    @ServiceProvider(service = IServiceProcessor.class),
    @ServiceProvider(service = IServiceInitializer.class)})

public class BaseSystem implements IServiceProcessor, IServiceInitializer {

    private Entity base;

    @Override
    public void process(GameData gameData, World world) {
        PlayerEntity player = null;
        for (Entity entity : world.getEntities(PlayerEntity.class)) {
            player = (PlayerEntity) entity;
        }
        for (Entity entity : world.getEntities(BaseEntity.class)) {
            BaseEntity base = (BaseEntity) entity;
            for (Event e : gameData.getAllEvents()) {
                if (e.getType() == EventType.ENTITY_HIT && e.getEntityID().equals(entity.getID())) {
                    base.setLife(base.getLife() - 1);
                    if (base.getLife() <= 0) {
                        world.removeEntity(entity);
                    }

                    gameData.removeEvent(e);
                }
            }

            UpgradeEntity menu = null;

            for (Entity e : world.getEntities(UpgradeEntity.class)) {
                menu = (UpgradeEntity) e;
            }

            if (gameData.getKeys().isPressed(GameKeys.U)) {
                //Open upgrade screen
                System.out.println("Opening menu");
                menu.setOpen(!menu.isOpen());
            }

            if (gameData.getKeys().isPressed(GameKeys.I) && menu.isOpen()) {
                //HP Upgrade
                if (player.getMoney() > 100) {
                    base.setHpLevel(base.getHpLevel() + 1);
                    base.setMaxLife(base.getMaxLife() + 50);
                    player.setMoney(player.getMoney() - 100);
                }
            }

            if (gameData.getKeys().isPressed(GameKeys.J) && menu.isOpen()) {
                if (player.getMoney() > 200) {
                    WeaponEntity turret = (WeaponEntity) base.getTurretOwned();
                    //Turret Upgrade
                    base.setTurretLevel(base.getTurretLevel() + 1);
                    if (turret == null) {
                        turret = new WeaponEntity();
                        turret.setAttackCooldown(10);
                        turret.setTimeSinceAttack(0);
                        //turret.setDrawable("turret");
                        turret.setDamage(1);
                        turret.setWeaponCarrier(base.getID());
                        turret.setX(base.getX());
                        turret.setY(base.getY());
                        world.addEntity(turret);
                        base.setTurretOwned(turret);
                    }
                    if (turret.getAttackCooldown() > 2 && base.getTurretLevel() % 2 == 0) {
                        turret.setAttackCooldown(turret.getAttackCooldown() - 1);
                    } else if (turret.getDamage() < 5 && base.getTurretLevel() % 2 == 1) {
                        turret.setDamage(turret.getDamage() + 1);
                    }
                    player.setMoney(player.getMoney() - 200);
                }
            }
            if (gameData.getKeys().isPressed(GameKeys.K) && menu.isOpen()) {
                //Platform Upgrade
                if (true) {
                    System.out.println("Adding platform");
                    //if (player.getMoney() > 50 && base.getPlatformLevel() < 5) {
                    base.setPlatformLevel(base.getPlatformLevel() + 1);
                    player.setMoney(player.getMoney() - 50);
                    //Create platforms
                    PlatformEntity platform = new PlatformEntity();
                    platform.setDrawable("base_platform");
//                    platform.setX(690);
//                    platform.setY(370);
                    platform.setX(900);
                    platform.setY(150);
                    world.addEntity(platform);
                }
            }

        }
    }

    @Override
    public void start(GameData gameData, World world) {
        base = createBase(gameData, world);
        world.addEntity(base);
        world.addEntity(new UpgradeEntity());
    }

    private Entity createBase(GameData gameData, World world) {
        BaseEntity base = new BaseEntity();
        base.setDrawable("brain_jar");

        int spriteWidth = gameData.getSpriteInfo().get(base.getDrawable())[0];
        int spriteHeight = gameData.getSpriteInfo().get(base.getDrawable())[1];
        base.setShapeX(new float[]{-(spriteWidth / 2) * gameData.getHitBoxScale(), -(spriteWidth / 2) * gameData.getHitBoxScale(),
            spriteWidth / 2 * gameData.getHitBoxScale(), spriteWidth / 2 * gameData.getHitBoxScale()});
        base.setShapeY(new float[]{-(spriteHeight / 2) * gameData.getHitBoxScale(), spriteHeight / 2 * gameData.getHitBoxScale(),
            spriteHeight / 2 * gameData.getHitBoxScale(), -(spriteHeight / 2 * gameData.getHitBoxScale())});

        base.setMaxLife(50);
        base.setLife(base.getMaxLife());
        base.setX((int) (gameData.getDisplayWidth() * 0.4));
        base.setY((int) (gameData.getDisplayHeight() * 0.5));
        return base;
    }

    @Override
    public void stop(GameData gameData, World world) {
        world.removeEntity(base);
    }
}
