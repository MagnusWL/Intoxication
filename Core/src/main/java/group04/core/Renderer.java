/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package group04.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFontCache;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.utils.Array;
import group04.basecommon.BaseEntity;
import group04.boostcommon.BoostEntity;
import group04.common.Entity;
import group04.common.EntityType;
import group04.common.GameData;
import group04.common.World;
import group04.core.managers.Assets;
import group04.playercommon.PlayerEntity;
import group04.projectilecommon.ProjectileEntity;
import group04.spawnercommon.WaveSpawnerEntity;
import group04.weaponcommon.WeaponEntity;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Magnus
 */
public class Renderer {

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

        assetManager = new Assets();

        assetManager.load();
        while (!assetManager.getAssetManager().update()) {
            System.out.println(assetManager.getAssetManager().getProgress() * 100);
        }
        loadPNGAnimation("player_run_animation.png", 75, 80);
        System.out.println("Renderer");
        loadPNGAnimation("player_idle_animation.png", 75, 80);
        loadPNGAnimation("player_jump_animation.png", 75, 80);
        loadPNGAnimation("enemybeer_run_animation.png", 142, 122);
        loadPNGAnimation("currency_gold_animation.png", 44, 45);
        // loadPNGImages();
        System.out.println(assetManager.getAssetManager().getLoadedAssets());

    }

    public void loadPNGAnimation(String animationName, int spriteSizeX, int spriteSizeY) {
        assetManager.makeAnimation(animationName, assetManager.getAssetManager().get(assetManager.getFilePaths().get(animationName), Texture.class), spriteSizeX, spriteSizeY);
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
        drawSprites(gameData, world);
        drawAnimations(gameData, world);
        batch.end();

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
            if (entity.getVelocity() < 0) {
                System.out.println(entity.getCurrentAnimation() + "_flipped.png");
                playAnimation(gameData, world, assetManager.getAnimationsFlip(entity.getCurrentAnimation() + "_flipped.png"), entity, 5);
            } else {
                playAnimation(gameData, world, assetManager.getAnimations(entity.getCurrentAnimation() + ".png"), entity, 5);
            }
        }
    }

    private void playAnimation(GameData gameData, World world, ArrayList<Sprite> animation, Entity entity, double animationSpeed) {
        drawSprite(gameData, world, entity, animation.get((int) entity.getCurrentFrame()));

        if (entity.getCurrentFrame() < (animation.size()) - 1) {
            entity.setCurrentFrame(entity.getCurrentFrame() + (1 / animationSpeed));
        } else {
            entity.setCurrentFrame(0);
        }

    }

    private void drawSprites(GameData gameData, World world) {
        for (Entity entity : world.getEntities(BaseEntity.class)) {
            drawSprite(gameData, world, entity, assetManager.getSprites(entity.getDrawable() + ".png"));
        }

//        for (Entity entity : world.getEntities(EntityType.ENEMY)) {
//            drawSprite(gameData, world, entity, images.get(entity.getSprite()), true);
//        }
        /*for (Entity entity : world.getEntities(EntityType.PLAYER)) {
            ImageContainer imageContainer = (ImageContainer) entity.getContainer(ImageContainer.class);
            if (gameData.getMouseX() < (entity.getX() - gameData.getCameraX())) {
                drawSprite(gameData, world, entity, imagesFlip.get(imageContainer.getSprite()), imageContainer);
            } else {
                drawSprite(gameData, world, entity, images.get(imageContainer.getSprite()), imageContainer);
            }
        }*/
        for (Entity entity : world.getEntities(WeaponEntity.class)) {
            drawSprite(gameData, world, entity, assetManager.getSprites(entity.getDrawable() + ".png"));
        }

        for (Entity entity : world.getEntities(ProjectileEntity.class)) {
            drawSprite(gameData, world, entity, assetManager.getSprites(entity.getDrawable() + ".png"));
        }

        for (Entity entity : world.getEntities(BoostEntity.class)) {
            drawSprite(gameData, world, entity, assetManager.getSprites(entity.getDrawable() + ".png"));

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

            int max = 0;
            int maxY = 0;
            int min = Integer.MAX_VALUE;

            if (entity.getMaxLife() != 0) {
                healthOffset = (int) assetManager.getSprites(entity.getDrawable() + ".png").getHeight() + 5;

                for (int i = 0; i < entity.getShapeX().length; i++) {
                    max = (int) Math.max(max, entity.getShapeX()[i]);
                    min = (int) Math.min(min, entity.getShapeX()[i]);
                    maxY = (int) Math.max(maxY, entity.getShapeY()[i]);
                }

                healthWidth = max - min;
                healthOffset = maxY + 5;

                sr.setColor(1f, 0f, 0, 1f);
                sr.rect(entity.getX() - gameData.getCameraX(), entity.getY() - gameData.getCameraY() + healthOffset, healthWidth, 5);
                sr.setColor(0.0f, 1f, 0, 1f);
                sr.rect(entity.getX() - gameData.getCameraX(), entity.getY() - gameData.getCameraY() + healthOffset, ((float) entity.getLife() / (float) entity.getMaxLife()) * healthWidth, 5);
            }
        }

    }

    private void drawSprite(GameData gameData, World world, Entity entity, Sprite sprite) {
        if (entity.getClass() == ProjectileEntity.class) {
            ProjectileEntity projectile = (ProjectileEntity) entity;
            if (projectile.getAngle() != 0) {
                sprite.setRotation((float) Math.toDegrees(projectile.getAngle()));
            }
        }

        sprite.setX(entity.getX() - gameData.getCameraX());
        sprite.setY(entity.getY() - gameData.getCameraY());
        sprite.draw(batch);
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
        //pupil
//        drawBackground(gameData, images.get("pupil"), back3m);
        drawBackground(gameData, assetManager.getSprites("background_layer2.png"), back2m);

        /*        Sprite sp = images.get("lightSource");
        sp.setX(i * sprite.getWidth() - gameData.getCameraX() * mov);
        sp.draw(batch);*/
//        drawBackground(gameData, images.get("lightSource"), back5m);
        drawBackground(gameData, assetManager.getSprites("middleground.png"), back3m);
        drawBackground(gameData, assetManager.getSprites("level_01_back.png"), back3m);
        drawBackground(gameData, assetManager.getSprites("level_03_back.png"), back3m);
//        drawBackground(gameData, images.get("Eye_withoutpupil"), back3m);101

    }

    public void drawPupil(GameData gameData, World world, Sprite pupil, float mov) {
        float eyeX = 1818;
        float playerX = 0;
        float playerY = 80;
        for (Entity player : world.getEntities(PlayerEntity.class)) {
            playerX = (float) (player.getX() + assetManager.getSprites("player.png").getWidth() / 2.0);
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
        pupil.draw(batch);
    }

    private void drawForeground(GameData gameData) {
        drawBackground(gameData, assetManager.getSprites("level_02.png"), back3m);
        drawBackground(gameData, assetManager.getSprites("level_01_front.png"), back3m);
        drawBackground(gameData, assetManager.getSprites("level_03_front.png"), back3m);

        //Player        
        drawBackground(gameData, assetManager.getSprites("foreground_layer1.png"), back4m);
        drawBackground(gameData, assetManager.getSprites("foreground_layer2.png"), back5m);
        drawHalo(gameData);
    }

    public void drawHalo(GameData gameData) {
        assetManager.getSprites("halo.png").draw(batch);
    }

    private void drawBackground(GameData gameData, Sprite sprite, float mov) {
        int repeats = 2 + (int) (((gameData.getTileSize() * gameData.getMapWidth()) / sprite.getWidth()) * mov);
        for (int i = -1; i < repeats; i++) {
            sprite.setX(i * sprite.getWidth() - gameData.getCameraX() * mov);
            sprite.draw(batch);
        }
    }
}
