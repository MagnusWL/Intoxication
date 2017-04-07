/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package group04.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import group04.basecommon.BaseEntity;
import group04.boostcommon.BoostEntity;
import group04.common.Entity;
import group04.common.GameData;
import group04.common.World;
import group04.core.managers.Assets;
import group04.enemycommon.EnemyEntity;
import group04.inventorycommon.InventoryEntity;
import group04.platformcommon.PlatformEntity;
import group04.playercommon.PlayerEntity;
import group04.projectilecommon.ProjectileEntity;
import group04.spawnercommon.WaveSpawnerEntity;
import group04.upgradecommon.UpgradeEntity;
import group04.weaponcommon.WeaponEntity;
import java.io.File;
import java.util.ArrayList;
import org.openide.util.Exceptions;
import java.util.Map.Entry;
import java.util.Random;
import javafx.application.Platform;

public class Renderer {

    private float counter = 0;
    private float r = 1;
    private float g = 1;
    private float b = 1;
    private Color startColor;
    private BitmapFont text;
    private SpriteBatch batch;
    private ShapeRenderer sr;

//    private Map<String, ArrayList<Sprite>> animations = new HashMap<>();
//    private Map<String, Sprite> images = new HashMap<>();
//
//    private Map<String, ArrayList<Sprite>> animationsFlip = new HashMap<>();
//    private Map<String, Sprite> imagesFlip = new HashMap<>();
    private boolean loaded = false;

    private Assets assetManager;

    public Renderer(GameData gameData) {
        text = new BitmapFont();
        batch = new SpriteBatch();
        sr = new ShapeRenderer();
        assetManager = new Assets(gameData);

        assetManager.load();
        while (!assetManager.getAssetManager().update()) {
//            System.out.println(assetManager.getAssetManager().getProgress() * 100);
        }

//        loadPNGAnimation("player_run_animation2.png", 75, 80, 5);
        //Animation speed += 1/animationspeed
        loadPNGAnimation("player_idle_animation.png", 105, 132, 5);
        loadPNGAnimation("player_jump_animation.png", 110, 120, 5);
        loadPNGAnimation("enemybeer_run_animation.png", 142, 122, 5);
        loadPNGAnimation("enemybeer_attack_animation.png", 128, 134, -3);
        loadPNGAnimation("enemynarko_run_animation.png", 85, 107, 5);
        loadPNGAnimation("enemynarko_attack_animation.png", 103, 109, 5);
        loadPNGAnimation("currency_gold_animation.png", 44, 45, 5);
        loadPNGAnimation("player_run_animation.png", 105, 132, 5);
        loadPNGAnimation("player_weapon_melee_champaign_attack_animation.png", 110, 166, 5);
        loadPNGAnimation("player_weapon_melee_champaign_run_animation.png", 108, 100, 5);
        loadPNGAnimation("player_weapon_ranged_champaign_attack_animation.png", 105, 132, 5);
        loadPNGAnimation("player_weapon_ranged_throwbottle_attack_animation.png", 111, 66, 5);
//        loadPNGAnimation("player_idle_animation.png", 44, 45, 5);

        // loadPNGImages();
        String fileName;
        Sprite sprite;
        for (Entry e : assetManager.getAllSprites().entrySet()) {
            fileName = (String) e.getKey();
            sprite = (Sprite) e.getValue();
            gameData.getSpriteInfo().put(fileName.substring(0, fileName.length() - 4), new int[]{(int) sprite.getWidth(), (int) sprite.getHeight()});
        }

        Animation animation;
        for (Entry e : assetManager.getAllAnimations().entrySet()) {
            fileName = (String) e.getKey();
            animation = (Animation) e.getValue();
            gameData.getSpriteInfo().put(fileName.substring(0, fileName.length() - 4), new int[]{(int) animation.getWidth(), (int) animation.getHeight()});
        }

//        assetManager.getAssetManager().get
//        gameData.getSpriteInfo().put(fileName.substring(0, fileName.length() - 4), new int[]{textureAsset.getWidth(), textureAsset.getHeight()});
    }

    public void loadPNGAnimation(String animationName, int spriteSizeX, int spriteSizeY, float animationSpeed) {
        assetManager.makeAnimation(animationName, assetManager.getAssetManager().get(assetManager.getFilePaths().get(animationName),
                Texture.class), spriteSizeX, spriteSizeY, animationSpeed);
    }

//    public void loadPNGImages() {
//        Array<Texture> imageTextures = new Array<Texture>();
//        for (Texture imageName : assetManager.getAssetManager().getAll(Texture.class, imageTextures)) {
//            Sprite thisSprite = new Sprite(imageName);
//            assetManager.getAssetManager().load(thisSprite, Sprite.class);
//            Sprite flip = new Sprite(imageName);
//            flip.flip(true, false);
//            assetManager.getAssetManager().load(thisSprite.toString() + "_flipped", Sprite.class);
//        }
//    }
    public void render(GameData gameData, World world) {
        sr.begin(ShapeType.Filled);
        sr.setAutoShapeType(true);
        clearBackground(gameData);
        sr.end();

        //Total back (Background)
        batch.begin();
        drawBackground(gameData, world);
        
        batch.end();
        drawSprites(gameData, world);
        drawAnimations(gameData, world);

        //Next layer: Still background
        sr.begin(ShapeType.Filled);
        drawHealthBars(gameData, world);
        sr.end();

        //Middle layer: Where entities is:
        batch.begin();
        drawForeground(gameData);
        drawScore(gameData, world);
        drawWaveCount(gameData, world);
        drawFPS(gameData);
        drawInventory(gameData, world);

        batch.end();

        //Layer beetween foreground and middleground: The frontside of the enemyspawner:
        //Foreground layer: The first one
        //Foreground layer: The last one
    }

//    public void makeAnimation(String animationName, Texture spriteSheet, int spriteSizeX, int spriteSizeY) {
//        ArrayList<Sprite> keyFrames = new ArrayList<>();
//        ArrayList<Sprite> flipKeyFrames = new ArrayList<>();
//        int numberOfSprites = (int) (spriteSheet.getWidth() / spriteSizeX);
//        for (int i = 0; i < numberOfSprites; i++) {
//            TextureRegion sprite = new TextureRegion(spriteSheet);
//            sprite.setRegion(i * spriteSizeX, 0, spriteSizeX, spriteSizeY);
//            keyFrames.add(new Sprite(sprite));
//
//            Sprite flip = new Sprite(sprite);
//            flip.flip(true, false);
//            flipKeyFrames.add(flip);
//        }
//        animations.put(animationName, keyFrames);
//        animationsFlip.put(animationName, flipKeyFrames);
//    }
    private void drawAnimations(GameData gameData, World world) {

        for (Entity entity : world.getAllEntities()) {
            if (entity.isAnimateable() && entity.getCurrentAnimation() != null) {

                if (!entity.isHit()) {
                    if (entity.getClass() != PlayerEntity.class) {
                        if (entity.getVelocity() <= 0) {
                            playAnimation(gameData, world, assetManager.getAnimationsFlip(entity.getCurrentAnimation() + "_flipped.png"), entity, assetManager.getAnimationSpeed(entity.getCurrentAnimation() + ".png"));
                        } else {
                            playAnimation(gameData, world, assetManager.getAnimations(entity.getCurrentAnimation() + ".png"), entity, assetManager.getAnimationSpeed(entity.getCurrentAnimation() + ".png"));
                        }
                    } else if (gameData.getMouseX() < entity.getX() - gameData.getCameraX()) {

                        if (entity.getVelocity() > 0) {
                            playAnimation(gameData, world, assetManager.getAnimationsFlip(entity.getCurrentAnimation() + "_flipped.png"), entity, -assetManager.getAnimationSpeed(entity.getCurrentAnimation() + ".png"));
                        } else {
                            playAnimation(gameData, world, assetManager.getAnimationsFlip(entity.getCurrentAnimation() + "_flipped.png"), entity, assetManager.getAnimationSpeed(entity.getCurrentAnimation() + ".png"));
                        }

                    } else if (entity.getVelocity() > 0) {
                        playAnimation(gameData, world, assetManager.getAnimations(entity.getCurrentAnimation() + ".png"), entity, assetManager.getAnimationSpeed(entity.getCurrentAnimation() + ".png"));
                    } else {
                        playAnimation(gameData, world, assetManager.getAnimations(entity.getCurrentAnimation() + ".png"), entity, -assetManager.getAnimationSpeed(entity.getCurrentAnimation() + ".png"));
                    }
                } else if (entity.isHit()) {
                    if (entity.getClass() != PlayerEntity.class) {
                        if (entity.getVelocity() <= 0) {
                            playAnimation(gameData, world, assetManager.getRedAnimationFlip(entity.getCurrentAnimation() + "_flipped.png"), entity, assetManager.getAnimationSpeed(entity.getCurrentAnimation() + ".png"));
                        } else {
                            playAnimation(gameData, world, assetManager.getRedAnimation(entity.getCurrentAnimation() + ".png"), entity, assetManager.getAnimationSpeed(entity.getCurrentAnimation() + ".png"));
                        }
                    } else if (gameData.getMouseX() < entity.getX() - gameData.getCameraX()) {

                        if (entity.getVelocity() > 0) {
                            playAnimation(gameData, world, assetManager.getRedAnimationFlip(entity.getCurrentAnimation() + "_flipped.png"), entity, -assetManager.getAnimationSpeed(entity.getCurrentAnimation() + ".png"));
                        } else {
                            playAnimation(gameData, world, assetManager.getRedAnimationFlip(entity.getCurrentAnimation() + "_flipped.png"), entity, assetManager.getAnimationSpeed(entity.getCurrentAnimation() + ".png"));
                        }

                    } else if (entity.getVelocity() > 0) {
                        playAnimation(gameData, world, assetManager.getRedAnimation(entity.getCurrentAnimation() + ".png"), entity, assetManager.getAnimationSpeed(entity.getCurrentAnimation() + ".png"));
                    } else {
                        playAnimation(gameData, world, assetManager.getRedAnimation(entity.getCurrentAnimation() + ".png"), entity, -assetManager.getAnimationSpeed(entity.getCurrentAnimation() + ".png"));
                    }
                    entity.setHitCounter();
                    if (entity.getHitCounter() == 0) {
                        entity.setHit(false);
                    }
                }
            }
        }
    }

    private void chooseAnimation(GameData gameData, World world, Entity entity, double animationSpeed, boolean flip, boolean red, boolean reverse) {
        if (reverse) {
            animationSpeed = -animationSpeed;
        }
        if (red) {
            if (flip) {
                playAnimation(gameData, world, assetManager.getRedAnimationFlip(entity.getCurrentAnimation() + "_flipped.png"), entity, assetManager.getAnimationSpeed(entity.getCurrentAnimation() + ".png"));
            } else {
                playAnimation(gameData, world, assetManager.getRedAnimation(entity.getCurrentAnimation() + ".png"), entity, assetManager.getAnimationSpeed(entity.getCurrentAnimation() + ".png"));
            }
        } else if (flip) {
            playAnimation(gameData, world, assetManager.getAnimationsFlip(entity.getCurrentAnimation() + "_flipped.png"), entity, assetManager.getAnimationSpeed(entity.getCurrentAnimation() + ".png"));
        } else {
            playAnimation(gameData, world, assetManager.getAnimations(entity.getCurrentAnimation() + ".png"), entity, assetManager.getAnimationSpeed(entity.getCurrentAnimation() + ".png"));
        }
    }

    private void playAnimation(GameData gameData, World world, ArrayList<Sprite> animation, Entity entity, double animationSpeed) {
        boolean draw = true;
        if (entity.getClass() == WeaponEntity.class) {
            if (world.getEntity(((WeaponEntity) entity).getWeaponCarrier()).getClass() == EnemyEntity.class) {
                draw = false;
            }
        }

        if (draw) {
            drawSprite(gameData, world, entity, animation.get((int) entity.getCurrentFrame()));

            if (animationSpeed > 0) {
                if (entity.getCurrentFrame() < (animation.size()) - 1 + (1 / animationSpeed)) {
                    entity.setCurrentFrame(entity.getCurrentFrame() + (1 / animationSpeed));
                } else {
                    entity.setCurrentFrame(0);
                    if (entity.getCurrentAnimation().equals(entity.getAttackAnimation())) {
                        entity.setCurrentAnimation(entity.getRunAnimation());
                        entity.setCurrentFrame(0);
                    }
                }
            } else if (entity.getCurrentFrame() > (1 / animationSpeed)) {
                entity.setCurrentFrame(entity.getCurrentFrame() + (1 / animationSpeed));
            } else {
                entity.setCurrentFrame(animation.size() - 1);
                if (entity.getCurrentAnimation().equals(entity.getAttackAnimation())) {
                    entity.setCurrentAnimation(entity.getRunAnimation());
                    entity.setCurrentFrame(0);
                }
            }
        }
    }

    private void drawSprites(GameData gameData, World world) {
        for (Entity entity : world.getEntities(BaseEntity.class)) {
            if (entity.getDrawable() != null) {
                drawSprite(gameData, world, entity, assetManager.getSprites(entity.getDrawable() + ".png"));
            }
        }

        for (Entity entity : world.getEntities(PlatformEntity.class)) {
            if (entity.getDrawable() != null) {
                System.out.println(entity.getDrawable());
                drawSprite(gameData, world, entity, assetManager.getSprites(entity.getDrawable() + ".png"));
            }
        }

        for (Entity entity : world.getEntities(EnemyEntity.class)) {
            if (entity.getDrawable() != null) {
                drawSprite(gameData, world, entity, assetManager.getSprites(entity.getDrawable() + ".png"));
            }
        }

        for (Entity entity : world.getEntities(ProjectileEntity.class)) {
            if (entity.getDrawable() != null) {
                drawSprite(gameData, world, entity, assetManager.getSprites(entity.getDrawable() + ".png"));
            }
        }

        for (Entity entity : world.getEntities(BoostEntity.class)) {
            if (entity.getDrawable() != null) {
                drawSprite(gameData, world, entity, assetManager.getSprites(entity.getDrawable() + ".png"));
            }
        }

        for (Entity entity : world.getEntities(UpgradeEntity.class)) {
            UpgradeEntity e = (UpgradeEntity) entity;
            if (entity.getDrawable() != null && e.isOpen()) {
                //Draw menu
                //Different sprites or just change text in code?
            }
        }
    }

    private void drawScore(GameData gameData, World world) {
        for (Entity entity : world.getEntities(PlayerEntity.class)) {
            PlayerEntity player = (PlayerEntity) entity;
            text.draw(batch, "Drug money: " + Integer.toString(player.getMoney()), 40, gameData.getDisplayHeight() - 30);
        }
    }

    private void drawFPS(GameData gameData) {
        int fps = Gdx.graphics.getFramesPerSecond();
        text.draw(batch, "FPS: " + Integer.toString(fps), 40, gameData.getDisplayHeight() - 70);
    }

    private void drawWaveCount(GameData gameData, World world) {
        for (Entity entity : world.getEntities(WaveSpawnerEntity.class)) {
            WaveSpawnerEntity wave = (WaveSpawnerEntity) entity;
            text.draw(batch, "Next wave: " + Integer.toString(Math.max(0, (wave.getSpawnTimerMax() - wave.getSpawnTimer()) / 60)) + " seconds", 40, gameData.getDisplayHeight() - 50);
        }
    }

    private void drawHealthBars(GameData gameData, World world) {
        int healthOffset = 0;
        int healthWidth;

        for (Entity entity : world.getAllEntities()) {

            int max = Integer.MIN_VALUE;
            int maxY = Integer.MIN_VALUE;
            int min = Integer.MAX_VALUE;

            if (entity.getMaxLife() != 0) {
                for (int i = 0; i < entity.getShapeX().length; i++) {
                    max = (int) Math.max(max, entity.getShapeX()[i]);
                    min = (int) Math.min(min, entity.getShapeX()[i]);
                    maxY = (int) Math.max(maxY, entity.getShapeY()[i]);
                }

                healthWidth = max - min;
                healthOffset = maxY + 5;

                sr.setColor(1f, 0f, 0, 1f);
                sr.rect(entity.getX() - gameData.getCameraX() - healthWidth / 2.0f, entity.getY() - gameData.getCameraY() + healthOffset, healthWidth, 5);
                sr.setColor(0.0f, 1f, 0, 1f);
                sr.rect(entity.getX() - gameData.getCameraX() - healthWidth / 2.0f, entity.getY() - gameData.getCameraY() + healthOffset, ((float) entity.getLife() / (float) entity.getMaxLife()) * healthWidth, 5);
            }
        }
    }

    private void drawSprite(GameData gameData, World world, Entity entity, Sprite sprite) {
        batch.begin();

        if (entity.getClass() == ProjectileEntity.class) {
            ProjectileEntity projectile = (ProjectileEntity) entity;
            if (projectile.getAngle() != 0) {
                sprite.setRotation((float) Math.toDegrees(projectile.getAngle()));
            }
        }

        sprite.setX((float) (entity.getX() - sprite.getWidth() / 2.0 - gameData.getCameraX()));
        sprite.setY((float) (entity.getY() - sprite.getHeight() / 2.0 - gameData.getCameraY()));
        sprite.draw(batch);

        batch.end();
    }

    float back1m = 1f;
    float back2m = 1f;
    float back3m = 1f;
    float back4m = 1.2f;
    float back5m = 1.4f;

    private void clearBackground(GameData gameData) {
        sr.setColor(new Color(0f, 138f / 255f, 1f, 1f));
        sr.rect(0, 0, gameData.getDisplayWidth(), gameData.getDisplayWidth());
    }

    private void drawBackground(GameData gameData, World world) {
        drawBackground(gameData, assetManager.getSprites("eye_withoutpupil.png"), back1m);
        drawPupil(gameData, world, assetManager.getSprites("pupil.png"), back1m);
        drawBackground(gameData, assetManager.getSprites("background_layer1.png"), back1m);
        drawBackground(gameData, assetManager.getSprites("background_layer2.png"), back2m);
        drawBackground(gameData, assetManager.getSprites("middleground.png"), back3m);
        drawBackground(gameData, assetManager.getSprites("level_01_back.png"), back3m);
        drawBackground(gameData, assetManager.getSprites("level_03_back.png"), back3m);
    }

    public void drawPupil(GameData gameData, World world, Sprite pupil, float mov) {
        float eyeX = 1818;
        float playerX = 0;
        float playerY = 80;
        for (Entity player : world.getEntities(PlayerEntity.class)) {
            playerX = player.getX();
            playerY = player.getY();
        }

        float d = (float) ((playerX - eyeX) / (eyeX));

        if (d < 0) {
            d = -d * d * 2;
        } else {
            d = d * d * 2;
        }

        int xTranslate = (int) (200 * d);
        int yTranslate = (int) (50 * Math.abs(d) + (playerY - 80) * 0.2);
        pupil.setX((float) (-pupil.getWidth() / 2.0 + eyeX - gameData.getCameraX() * mov + xTranslate * 3.5));
        pupil.setY(yTranslate);
        pupil.setScale((float) ((1 - Math.abs(d))), 1);
        pupil.setRotation(-d * 20);
        colorPupil(world, pupil);
        pupil.draw(batch);
    }

    public void setRGB() {

        counter += 0.01;

        r = (float) Math.abs(Math.sin(counter));
        g = (float) Math.abs(Math.sin(counter * 2.573758));
        b = (float) Math.abs(Math.sin(counter * 3.357285));

    }

    private void colorPupil(World world, Sprite pupil) {
        if (startColor == null) {
            startColor = pupil.getColor();
        }

        setRGB();

        for (Entity e : world.getEntities(PlayerEntity.class)) {
            PlayerEntity entity = (PlayerEntity) e;

            if (entity.getLsdTimer() > 0) {
                Color color = new Color(r, g, b, 1);
                pupil.setColor(color);
                entity.subtractLsdTimer();
            }

            if (entity.getLsdTimer() == 0) {
                pupil.setColor(startColor);
                batch.setShader(null);
            }
        }
    }

    private void drawForeground(GameData gameData) {
        drawBackground(gameData, assetManager.getSprites("level_02.png"), back3m);
        drawBackground(gameData, assetManager.getSprites("level_01_front.png"), back3m);
        drawBackground(gameData, assetManager.getSprites("level_03_front.png"), back3m);

        //Player        
        drawBackgroundNoRepeat(gameData, assetManager.getSprites("foreground_layer1.png"), back4m);
        drawBackground(gameData, assetManager.getSprites("foreground_layer2.png"), back5m);
        drawHalo(assetManager.getSprites("halo.png"));
    }

    //Tilføj at halo skal skifte farve afhængigt af playerens liv.
    public void drawHalo(Sprite sprite) {
        sprite.setColor(new Color(1, 0, 0, 0.95f));
        sprite.draw(batch);
    }

    private void drawBackgroundNoRepeat(GameData gameData, Sprite sprite, float mov) {
        sprite.setX(-gameData.getCameraX() * mov);
        sprite.draw(batch);
    }

    private void drawBackground(GameData gameData, Sprite sprite, float mov) {
        int repeats = 2 + (int) (((gameData.getTileSize() * gameData.getMapWidth()) / sprite.getWidth()) * mov);
        for (int i = -1; i < repeats; i++) {
            sprite.setX(i * sprite.getWidth() - gameData.getCameraX() * mov);
            sprite.draw(batch);
        }
    }

    private void drawInventory(GameData gameData, World world) {

        assetManager.getSprites("inventoryspace1.png").setX((gameData.getDisplayWidth() - 124));
        assetManager.getSprites("inventoryspace1.png").setY((gameData.getDisplayHeight() - 70));
        assetManager.getSprites("inventoryspace1.png").draw(batch);

        for (Entity e : world.getEntities(WeaponEntity.class)) {

            //BUGGER HVIS DER IKKE ER DRAWABLE MEN BARE ANIMATION, SORRY LARS - MAGNUS
            /*      assetManager.getSprites(e.getDrawable() + ".png").setX((assetManager.getSprites("inventoryspace1.png").getX() + 76 / 2));
            assetManager.getSprites(e.getDrawable() + ".png").setY((assetManager.getSprites("inventoryspace1.png").getY() + 50 / 2));
            assetManager.getSprites(e.getDrawable() + ".png").draw(batch);*/
            //drawSprite(gameData, world, e, assetManager.getSprites(e.getDrawable() + ".png"));
        }

    }

    

}
