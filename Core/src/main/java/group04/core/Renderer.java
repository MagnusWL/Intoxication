/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package group04.core;

import com.badlogic.gdx.Gdx;
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
import group04.common.Entity;
import group04.common.EntityType;
import group04.common.GameData;
import group04.common.World;
import group04.datacontainers.AnimationContainer;
import group04.datacontainers.CollisionContainer;
import group04.datacontainers.DataContainer;
import group04.datacontainers.HealthContainer;
import group04.datacontainers.ImageContainer;
import group04.datacontainers.MovementContainer;
import group04.datacontainers.PlayerContainer;
import group04.datacontainers.WaveSpawnerContainer;
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

    private Map<String, ArrayList<Sprite>> animations = new HashMap<>();
    private Map<String, Sprite> images = new HashMap<>();

    private Map<String, ArrayList<Sprite>> animationsFlip = new HashMap<>();
    private Map<String, Sprite> imagesFlip = new HashMap<>();

    private boolean loaded = false;

    public Renderer(GameData gameData) {
        text = new BitmapFont();
        batch = new SpriteBatch();
        sr = new ShapeRenderer();

        loadPNGImages("Enemy", "Player", "pill", "gun", "bullet", "base", "sky", "grass", "back1", "back2", "back3", "back4", "sword", "rocket",
                "brain_jar", "Enemy_Beer", "Enemy_joint", "Enemy_LSD", "Enemy_narko", "Enemy_rave", "pupil",
                "Middleground", "lightSource", "level_01_back", "level_01_front", "level_02", "level_03_back", "level_03_front",
                "Eye_withoutpupil", "foreground_layer1", "foreground_layer2", "Background_layer1", "Background_layer2", "Halo");

        loadPNGAnimation("player_run", 75, 80);
        loadPNGAnimation("player_idle", 75, 80);
        loadPNGAnimation("player_jump", 75, 80);
        loadPNGAnimation("Enemy_Beer_Run", 142, 122);
        loadPNGAnimation("currency_gold", 44, 45);
    }

    public void loadPNGAnimation(String animationName, int spriteSizeX, int spriteSizeY) {
        makeAnimation(animationName, new Texture(Gdx.files.internal(animationName + ".png")), spriteSizeX, spriteSizeY);
    }

    public void loadPNGImages(String... imageNames) {
        for (String imageName : imageNames) {
            images.put(imageName, new Sprite(new Texture(Gdx.files.internal(imageName + ".png"))));
            Sprite flip = new Sprite(images.get(imageName).getTexture());
            flip.flip(true, false);
            imagesFlip.put(imageName, flip);
        }
    }

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

    public void makeAnimation(String animationName, Texture spriteSheet, int spriteSizeX, int spriteSizeY) {
        ArrayList<Sprite> keyFrames = new ArrayList<>();
        ArrayList<Sprite> flipKeyFrames = new ArrayList<>();
        int numberOfSprites = (int) (spriteSheet.getWidth() / spriteSizeX);
        for (int i = 0; i < numberOfSprites; i++) {
            TextureRegion sprite = new TextureRegion(spriteSheet);
            sprite.setRegion(i * spriteSizeX, 0, spriteSizeX, spriteSizeY);
            keyFrames.add(new Sprite(sprite));

            Sprite flip = new Sprite(sprite);
            flip.flip(true, false);
            flipKeyFrames.add(flip);
        }
        animations.put(animationName, keyFrames);
        animationsFlip.put(animationName, flipKeyFrames);
    }

    private void drawAnimations(GameData gameData, World world) {
        for (Entity entity : world.getAllEntities()) {
            AnimationContainer animationContainer = (AnimationContainer) entity.getContainer(AnimationContainer.class);
            MovementContainer movementContainer = (MovementContainer) entity.getContainer(MovementContainer.class);
            if (animationContainer != null) {
                //FLIP
                if (movementContainer != null && movementContainer.getVelocity() < 0) {
                    playAnimation(gameData, world, animationsFlip.get(animationContainer.getCurrentAnimation()), entity, 5, animationContainer);
                } else {
                    playAnimation(gameData, world, animations.get(animationContainer.getCurrentAnimation()), entity, 5, animationContainer);
                }
            }
        }
    }

    private void playAnimation(GameData gameData, World world, ArrayList<Sprite> animation, Entity entity, double animationSpeed, AnimationContainer animationContainer) {
        ImageContainer imageContainer = (ImageContainer) entity.getContainer(ImageContainer.class);
        drawSprite(gameData, world, entity, animation.get((int) animationContainer.getCurrentFrame()), imageContainer);

        if (animationContainer.getCurrentFrame() < (animation.size()) - 1) {
            animationContainer.setCurrentFrame(animationContainer.getCurrentFrame() + (1 / animationSpeed));
        } else {
            animationContainer.setCurrentFrame(0);
        }

    }

    private void drawSprites(GameData gameData, World world) {
        for (Entity entity : world.getEntities(EntityType.BASE)) {
            ImageContainer imageContainer = (ImageContainer) entity.getContainer(ImageContainer.class);
            drawSprite(gameData, world, entity, images.get(imageContainer.getSprite()), imageContainer);
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

        for (Entity entity : world.getEntities(EntityType.WEAPON)) {
            ImageContainer imageContainer = (ImageContainer) entity.getContainer(ImageContainer.class);
            drawSprite(gameData, world, entity, images.get(imageContainer.getSprite()), imageContainer);
        }

        for (Entity entity : world.getEntities(EntityType.PROJECTILE)) {
            ImageContainer imageContainer = (ImageContainer) entity.getContainer(ImageContainer.class);
            drawSprite(gameData, world, entity, images.get(imageContainer.getSprite()), imageContainer);
        }

        for (Entity entity : world.getEntities(EntityType.BOOST)) {
            ImageContainer imageContainer = (ImageContainer) entity.getContainer(ImageContainer.class);
            drawSprite(gameData, world, entity, images.get(imageContainer.getSprite()), imageContainer);
        }
    }

    private void drawScore(GameData gameData, World world) {
        for (Entity player : world.getEntities(EntityType.PLAYER)) {
            PlayerContainer playerContainer = (PlayerContainer) player.getContainer(PlayerContainer.class);
            text.draw(batch, "Drug money: " + Integer.toString(playerContainer.getMoney()), 40, gameData.getDisplayHeight() - 30);
        }
    }

    private void drawFPS(GameData gameData) {
        int fps = Gdx.graphics.getFramesPerSecond();
        text.draw(batch, "FPS: " + Integer.toString(fps), 40, gameData.getDisplayHeight() - 70);
    }

    private void drawWaveCount(GameData gameData, World world) {
        for (Entity wave : world.getEntities(EntityType.WAVE_SPAWNER)) {
            WaveSpawnerContainer waveSpawnerContainer = (WaveSpawnerContainer) wave.getContainer(WaveSpawnerContainer.class);
            text.draw(batch, "Next wave: " + Integer.toString(Math.max(0, (waveSpawnerContainer.getSpawnTimerMax() - waveSpawnerContainer.getSpawnTimer()) / 60)) + " seconds", 40, gameData.getDisplayHeight() - 50);
        }
    }

    private void drawHealthBars(GameData gameData, World world) {
        int healthOffset = 0;
        int healthWidth;

        for (Entity entity : world.getAllEntities()) {

            int max = 0;
            int maxY = 0;
            int min = Integer.MAX_VALUE;

            HealthContainer healthContainer = (HealthContainer) entity.getContainer(HealthContainer.class);
            CollisionContainer collisionContainer = (CollisionContainer) entity.getContainer(CollisionContainer.class);

            if (healthContainer != null) {
                ImageContainer imageContainer = (ImageContainer) entity.getContainer(ImageContainer.class);

                if (imageContainer != null) {
                    healthOffset = (int) images.get(imageContainer.getSprite()).getHeight() + 5;

                    for (int i = 0; i < collisionContainer.getShapeX().length; i++) {
                        max = (int) Math.max(max, collisionContainer.getShapeX()[i]);
                        min = (int) Math.min(min, collisionContainer.getShapeX()[i]);
                        maxY = (int) Math.max(maxY, collisionContainer.getShapeY()[i]);
                    }

                    healthWidth = max - min;
                    healthOffset = maxY + 5;

                    sr.setColor(1f, 0f, 0, 1f);
                    sr.rect(entity.getX() - gameData.getCameraX(), entity.getY() - gameData.getCameraY() + healthOffset, healthWidth, 5);
                    sr.setColor(0.0f, 1f, 0, 1f);
                    sr.rect(entity.getX() - gameData.getCameraX(), entity.getY() - gameData.getCameraY() + healthOffset, ((float) healthContainer.getLife() / (float) healthContainer.getMaxLife()) * healthWidth, 5);
                }
            }
        }
    }

    private void drawSprite(GameData gameData, World world, Entity entity, Sprite sprite, ImageContainer imageContainer) {
        if (imageContainer != null && imageContainer.getAngle() != 0) {
            sprite.setRotation((float) Math.toDegrees(imageContainer.getAngle()));
        }

        if (imageContainer != null) {
            sprite.setX(imageContainer.getDrawOffsetX() + entity.getX() - gameData.getCameraX());
            sprite.setY(imageContainer.getDrawOffsetY() + entity.getY() - gameData.getCameraY());
        } else {
            sprite.setX(entity.getX() - gameData.getCameraX());
            sprite.setY(entity.getY() - gameData.getCameraY());
        }
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
        drawBackground(gameData, images.get("Eye_withoutpupil"), back1m);
        drawPupil(gameData, world, images.get("pupil"), back1m);
        drawBackground(gameData, images.get("Background_layer1"), back1m);
        //pupil
//        drawBackground(gameData, images.get("pupil"), back3m);
        drawBackground(gameData, images.get("Background_layer2"), back2m);

        /*        Sprite sp = images.get("lightSource");
        sp.setX(i * sprite.getWidth() - gameData.getCameraX() * mov);
        sp.draw(batch);*/
//        drawBackground(gameData, images.get("lightSource"), back5m);
        drawBackground(gameData, images.get("Middleground"), back3m);
        drawBackground(gameData, images.get("level_01_back"), back3m);
        drawBackground(gameData, images.get("level_03_back"), back3m);
//        drawBackground(gameData, images.get("Eye_withoutpupil"), back3m);101

    }

    public void drawPupil(GameData gameData, World world, Sprite pupil, float mov) {
        float eyeX = 1818;
        float playerX = 0;
        float playerY = 80;
        for (Entity player : world.getEntities(EntityType.PLAYER)) {
            playerX = (float) (player.getX() + images.get("Player").getWidth() / 2.0);
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
        drawBackground(gameData, images.get("level_02"), back3m);
        drawBackground(gameData, images.get("level_01_front"), back3m);
        drawBackground(gameData, images.get("level_03_front"), back3m);

        //Player        
        drawBackground(gameData, images.get("foreground_layer1"), back4m);
        drawBackground(gameData, images.get("foreground_layer2"), back5m);
        drawHalo(gameData);
    }

    public void drawHalo(GameData gameData) {
        images.get("Halo").draw(batch);
    }

    private void drawBackground(GameData gameData, Sprite sprite, float mov) {
        int repeats = 2 + (int) (((gameData.getTileSize() * gameData.getMapWidth()) / sprite.getWidth()) * mov);
        for (int i = -1; i < repeats; i++) {
            sprite.setX(i * sprite.getWidth() - gameData.getCameraX() * mov);
            sprite.draw(batch);
        }
    }
}
