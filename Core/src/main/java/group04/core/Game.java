package group04.core;

import group04.core.managers.InputController;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.OrthographicCamera;
import group04.basecommon.BaseEntity;
import group04.cameracommon.ICameraService;
import group04.collisioncommon.ICollisionService;
import group04.common.Entity;
import org.openide.util.Lookup;
import group04.common.GameData;
import group04.common.GameKeys;
import group04.weaponcommon.WeaponType;
import group04.common.World;
import group04.common.events.Event;
import group04.common.events.EventType;
import group04.projectilecommon.IProjectileService;
import group04.common.services.IServiceInitializer;
import group04.common.services.IServiceProcessor;
import group04.core.AI.GeneticAlgorithm;
import group04.core.managers.Assets;
import group04.enemycommon.EnemyEntity;
import group04.enemycommon.IEnemyService;
import group04.mapcommon.IMapService;
import group04.movementcommon.IMovementService;
import group04.playercommon.PlayerEntity;
import group04.spawnercommon.ISpawnerService;
import group04.spawnercommon.WaveSpawnerEntity;
import group04.weaponcommon.IWeaponService;
import group04.weaponcommon.WeaponEntity;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Game implements ApplicationListener {

    private World world;
    private GameData gameData;
    private OrthographicCamera cam;
    Renderer render;
    MenuHandler menu;
    private FPSLogger fps = new FPSLogger();
    private AudioController audio;
    private boolean generatingAI = false;

    public Game() {

    }

    Assets assetManager;

    public void loadWorld() {
        FileInputStream fin = null;
        ObjectInputStream ois = null;
        try {
            fin = new FileInputStream(new File("../../../Common/src/main/resources/save.object").getAbsolutePath());
            ois = new ObjectInputStream(fin);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(World.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(World.class.getName()).log(Level.SEVERE, null, ex);
        }

        if (ois != null) {
            try {
                world.setMap((Map<String, Entity>) ois.readObject());

            } catch (IOException ex) {
                Logger.getLogger(World.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(World.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    public void saveWorld() {
        FileOutputStream fout;
        try {
            fout = new FileOutputStream(new File("../../../Common/src/main/resources/save.object").getAbsolutePath());
            ObjectOutputStream oos = new ObjectOutputStream(fout);
            oos.writeObject(world.getMap());
        } catch (FileNotFoundException ex) {
            Logger.getLogger(World.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(World.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void create() {

        world = new World();
        gameData = new GameData();
        gameData.setDisplayWidth(Gdx.graphics.getWidth());
        gameData.setDisplayHeight(Gdx.graphics.getHeight());
        gameData.setTileSize(16);
        cam = new OrthographicCamera(gameData.getDisplayWidth(), gameData.getDisplayHeight());
        cam.translate(gameData.getDisplayWidth() / 2, gameData.getDisplayHeight() / 2);
        cam.update();

        assetManager = new Assets(gameData);

        assetManager.load();

        while (!assetManager.getAssetManager().update()) {
        }

        render = new Renderer(gameData, assetManager);
        audio = new AudioController(gameData, assetManager);
        menu = new MenuHandler();

        for (IServiceInitializer i : Lookup.getDefault().lookupAll(IServiceInitializer.class)) {
            i.start(gameData, world);
        }

        Gdx.input.setInputProcessor(
                new InputController(gameData)
        );
    }

    @Override
    public void render() {
        if (generatingAI) {
            updateAI();
        } else {
            gameData.setMouseX(Gdx.input.getX());
            gameData.setMouseY(gameData.getDisplayHeight() - Gdx.input.getY());
            if (menu.getGameState() == 1) {
                update();
                render.render(gameData, world);

            } else if (menu.getGameState() == 0) {
                menu.renderMenu(gameData);
            } else if (menu.getGameState() == 2) {
                menu.renderOptions(gameData);
            } else if (menu.getGameState() == 3) {
                menu.renderExit(gameData);
            }
            gameData.getKeys().update();
        }
    }

    private void updateAI() {
        gameData.setDelta(1.0f / 20.0f);
        Entity enemy = new EnemyEntity();
        enemy.setX((float) (100));
        enemy.setY((float) 136.10168f);
        world.addEntity(enemy);
        GeneticAlgorithm.start(gameData, world);
        System.exit(0);
    }

    private void update() {
        gameData.setDelta(Gdx.graphics.getDeltaTime());

        if (gameData.getKeys().isDown(GameKeys.O)) {
            saveWorld();
        }

        if (gameData.getKeys().isDown(GameKeys.L)) {
            loadWorld();
        }

        for (ICameraService e : Lookup.getDefault().lookupAll(ICameraService.class)) {
            for (Entity player : world.getEntities(PlayerEntity.class)) {
                e.followEntity(gameData, world, player);
            }
        }

        for (IServiceProcessor e : Lookup.getDefault().lookupAll(IServiceProcessor.class)) {
            e.process(gameData, world);
        }

        for (IMovementService e : Lookup.getDefault().lookupAll(IMovementService.class)) {
            e.process(gameData, world);
        }

        for (ISpawnerService i : Lookup.getDefault().lookupAll(ISpawnerService.class)) {
            for (Entity e : world.getEntities(WaveSpawnerEntity.class)) {
                i.spawner(gameData, world, (WaveSpawnerEntity) e);
            }
        }

        for (IWeaponService ips : Lookup.getDefault().lookupAll(IWeaponService.class)) {
            ips.pickUpWeapon(gameData, world);
            for (Entity e : world.getEntities(PlayerEntity.class)) {
                ips.switchWeapon(gameData, world, (PlayerEntity) e);
            }
        }

        platformProcess();
        playerProcess();
        enemyProcess();
    }

    @Override
    public void resize(int i, int i1) {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void dispose() {
    }

    private void playerProcess() {
        for (Entity p : world.getEntities(PlayerEntity.class)) {

            
            PlayerEntity player = (PlayerEntity) p;

            for (IWeaponService ips : Lookup.getDefault().lookupAll(IWeaponService.class)) {
                ips.playerAttack(gameData, world, p);
            }

            for (IProjectileService ips : Lookup.getDefault().lookupAll(IProjectileService.class)) {
                //ips.process(gameData, world);
                for (Event e : gameData.getAllEvents()) {
                    if (e.getType() == EventType.PLAYER_SHOOT_GUN) {
                        WeaponEntity weapon = (WeaponEntity) player.getWeaponOwned();
                        weapon.setCurrentAnimation(weapon.getAttackAnimation());
                        if (weapon.getWeaponType() == WeaponType.GUN) {
                            weapon.setCurrentFrame(0);
                            ips.playershootgun(gameData, world, p, weapon);
                        }
                        gameData.removeEvent(e);
                    } else if (e.getType() == EventType.PLAYER_SHOOT_ROCKET) {
                        WeaponEntity weapon = (WeaponEntity) player.getWeaponOwned();
                        weapon.setCurrentAnimation(weapon.getAttackAnimation());
                        if (weapon.getWeaponType() == WeaponType.ROCKET) {
                            ips.playershootrocket(gameData, world, p, weapon);

                        }
                        gameData.removeEvent(e);
                    } else if (e.getType() == EventType.PLAYER_SWING) {
                        WeaponEntity weapon = (WeaponEntity) player.getWeaponOwned();
                        weapon.setCurrentAnimation(weapon.getAttackAnimation());
                        if (weapon.getWeaponType() == WeaponType.MELEE) {
                            ips.playermeleeattack(gameData, world, p, weapon);
                            weapon.setCurrentFrame(0);
                        }
                        // audio.PlayAudio(weapon.getAttackAudio() + ".mp3",0.4f);
                        gameData.removeEvent(e);
                    }
                }
            }
        }
    }

    private void enemyProcess() {
        for (IEnemyService i : Lookup.getDefault().lookupAll(IEnemyService.class)) {
            Entity player = null;
            Entity base = null;
            ArrayList<EnemyEntity> enemies = new ArrayList<>();
            for (Entity p : world.getEntities(PlayerEntity.class)) {
                player = p;
            }
            for (Entity b : world.getEntities(BaseEntity.class)) {
                base = b;
            }
            for (Entity e : world.getEntities(EnemyEntity.class)) {
                enemies.add((EnemyEntity) e);
            }

            for (Event ev : gameData.getAllEvents()) {
                if (ev.getType() == EventType.ENEMY_SWING) {
                    Entity weapon = world.getEntity(ev.getEntityID());
                    gameData.removeEvent(ev);
                    if (weapon != null) {
                        for (ICollisionService serv : Lookup.getDefault().lookupAll(ICollisionService.class)) {
                            if (serv.isEntitiesColliding(player, weapon)) {
                                new Event(EventType.ENTITY_HIT, player.getID());
                            }
                        }
                    }
                }
            }

            for (IWeaponService ips : Lookup.getDefault().lookupAll(IWeaponService.class)) {
                for (Entity enemy : world.getEntities(EnemyEntity.class)) {
                    ips.enemyAttack(gameData, world, enemy, player, base);
                }
            }

            try {
                i.controller(gameData, world, player, base, enemies);
            } catch (NullPointerException e) {
                System.out.println("Base or player is null");
            }

            for (Event ev : gameData.getAllEvents()) {
                if (ev.getType() == EventType.ENTITY_HIT) {
                    Entity enemyHit = world.getEntity(ev.getEntityID());
                    if (enemyHit.getClass() == EnemyEntity.class) {
                        i.enemyHit(gameData, world, (EnemyEntity) enemyHit);
                        gameData.removeEvent(ev);
                    }
                }
            }

            for (IProjectileService ips : Lookup.getDefault().lookupAll(IProjectileService.class)) {
                for (Entity enemy : world.getEntities(EnemyEntity.class)) {
                    for (Event e : gameData.getAllEvents()) {
                        if (e.getType() == EventType.ENEMY_SHOOT && e.getEntityID().equals(enemy.getID())) {
                            EnemyEntity enemyEntity = (EnemyEntity) enemy;
                            ips.enemyshoot(gameData, world, enemy, base, player, enemyEntity.getK1(), enemyEntity.getK2());
                            gameData.removeEvent(e);
                        }
                    }
                }
            }

        }
    }

    private void platformProcess() {
        for (Event e : gameData.getAllEvents()) {
            if (e.getType() == EventType.PLATFORM_SPAWN) {
                for (IMapService i : Lookup.getDefault().lookupAll(IMapService.class
                )) {
                    for (Entity ent : world.getEntities(BaseEntity.class
                    )) {
                        BaseEntity base = (BaseEntity) ent;
                        if (base.getPlatformLevel() == 1) {
                            i.process(gameData, "../../../Common/src/main/resources/mapplat1.object");
                        }
                        if (base.getPlatformLevel() == 2) {
                            i.process(gameData, "../../../Common/src/main/resources/mapplat2.object");

                        }
                        if (base.getPlatformLevel() == 3) {
                            i.process(gameData, "../../../Common/src/main/resources/mapplat3.object");

                        }
                        if (base.getPlatformLevel() == 4) {
                            i.process(gameData, "../../../Common/src/main/resources/mapplat4.object");

                        }
                    }
                }
            }
        }
    }
}
