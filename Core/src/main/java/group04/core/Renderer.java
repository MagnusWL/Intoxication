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
    private boolean loaded = false;

    public Renderer(GameData gameData) {
        text = new BitmapFont();
        batch = new SpriteBatch();
        sr = new ShapeRenderer();
        loadPNGImages("Enemy", "Player", "gun", "bullet", "base", "sky", "grass", "back1", "back2", "back3", "back4", "sword", "rocket",
                "brain_jar", "Enemy_Beer", "Enemy_joint", "Enemy_LSD", "Enemy_narko", "Enemy_rave", "pupil",
                "Middleground", "lightSource", "level_01_back", "level_01_front", "level_02", "level_03_back", "level_03_front",
                "Eye_withoutpupil", "foreground_layer1", "foreground_layer2", "Background_layer1", "Background_layer2");

        /*        loadPNGAnimation("player_run", 75, 80);
        loadPNGAnimation("player_idle", 75, 80);
        loadPNGAnimation("player_jump", 75, 80);*/
        loadPNGAnimation("currency_gold", 44, 45);
    }

    public void loadPNGAnimation(String animationName, int spriteSizeX, int spriteSizeY) {
        makeAnimation(animationName, new Texture(Gdx.files.internal(animationName + ".png")), spriteSizeX, spriteSizeY);
    }

    public void loadPNGImages(String... imageNames) {
        for (String imageName : imageNames) {
            images.put(imageName, new Sprite(new Texture(Gdx.files.internal(imageName + ".png"))));
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
        batch.end();

        //Layer beetween foreground and middleground: The frontside of the enemyspawner:
        //Foreground layer: The first one
        //Foreground layer: The last one
    }

    public void makeAnimation(String animationName, Texture spriteSheet, int spriteSizeX, int spriteSizeY) {
        ArrayList<Sprite> keyFrames = new ArrayList<>();
        int numberOfSprites = (int) (spriteSheet.getWidth() / spriteSizeX);
        for (int i = 0; i < numberOfSprites; i++) {
            TextureRegion sprite = new TextureRegion(spriteSheet);
            sprite.setRegion(i * spriteSizeX, 0, spriteSizeX, spriteSizeY);
            keyFrames.add(new Sprite(sprite));
        }
        animations.put(animationName, keyFrames);
    }

    private void drawAnimations(GameData gameData, World world) {
        for (Entity entity : world.getAllEntities()) {
            if (entity.isAnimateable()) {
                playAnimation(gameData, world, animations.get(entity.getCurrentAnimation()), true, entity, 10);
            }
        }
    }

    private void playAnimation(GameData gameData, World world, ArrayList<Sprite> animation, boolean flip, Entity entity, double animationSpeed) {
        drawSprite(gameData, world, entity, animation.get((int) entity.getCurrentFrame()), flip);
        if (entity.getCurrentFrame() < (animation.size()) - 1) {
            entity.setCurrentFrame(entity.getCurrentFrame() + (1 / animationSpeed));
        } else {
            entity.setCurrentFrame(0);
        }

    }

    private void drawSprites(GameData gameData, World world) {
        for (Entity entity : world.getEntities(EntityType.BASE)) {
            drawSprite(gameData, world, entity, images.get(entity.getSprite()), false);
        }

        for (Entity entity : world.getEntities(EntityType.ENEMY)) {
            drawSprite(gameData, world, entity, images.get(entity.getSprite()), true);
        }

        for (Entity entity : world.getEntities(EntityType.PLAYER)) {
            drawSprite(gameData, world, entity, images.get(entity.getSprite()), true);
        }

        for (Entity entity : world.getEntities(EntityType.WEAPON)) {
            drawSprite(gameData, world, entity, images.get(entity.getSprite()), true);
        }

        for (Entity entity : world.getEntities(EntityType.PROJECTILE)) {
            drawSprite(gameData, world, entity, images.get(entity.getSprite()), true);
        }
    }

    private void drawScore(GameData gameData, World world) {
        for (Entity player : world.getEntities(EntityType.PLAYER)) {
            text.draw(batch, "Drug money: " + Integer.toString(player.getCurrency()), 40, gameData.getDisplayHeight() - 30);
        }
    }

    private void drawWaveCount(GameData gameData, World world) {
        for (Entity wave : world.getEntities(EntityType.WAVE_SPAWNER)) {
            text.draw(batch, "Next wave: " + Integer.toString((wave.getSpawnTimerMax() - wave.getSpawnTimer()) / 60) + " seconds", 40, gameData.getDisplayHeight() - 50);
        }
    }

    private void drawHealthBars(GameData gameData, World world) {
        int healthOffset;
        int healthWidth;

        for (Entity entity : world.getAllEntities()) {
            if (entity.getMaxLife() != 0) {
                healthOffset = (int) images.get(entity.getSprite()).getHeight() + 5;
                healthWidth = (int) images.get(entity.getSprite()).getWidth();
                sr.setColor(1f, 0f, 0, 1f);
                sr.rect(entity.getX() - gameData.getCameraX(), entity.getY() - gameData.getCameraY() + healthOffset, healthWidth, 5);
                sr.setColor(0.0f, 1f, 0, 1f);
                sr.rect(entity.getX() - gameData.getCameraX(), entity.getY() - gameData.getCameraY() + healthOffset, ((float) entity.getLife() / (float) entity.getMaxLife()) * healthWidth, 5);
            }
        }
    }

    private void drawSprite(GameData gameData, World world, Entity entity, Sprite sprite, boolean flip) {
        if (entity.getAngle() != 0) {
            sprite.setRotation((float) Math.toDegrees(entity.getAngle()));
        } else if (flip) {
            if (entity.getEntityType() == EntityType.PLAYER
                    && ((gameData.getMouseX() < (entity.getX() - gameData.getCameraX()) && !sprite.isFlipX())
                    || (gameData.getMouseX() > (entity.getX() - gameData.getCameraX()) && sprite.isFlipX()))) {
                sprite.flip(true, false);
            }
            if (entity.getEntityType() != EntityType.PLAYER && ((entity.getVelocity() < 0 && !sprite.isFlipX()) || (entity.getVelocity() > 0 && sprite.isFlipX()))) {
                sprite.flip(true, false);
            }
        }

        sprite.setX(entity.getDrawOffsetX() + entity.getX() - gameData.getCameraX());
        sprite.setY(entity.getDrawOffsetY() + entity.getY() - gameData.getCameraY());
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
    }

    private void drawBackground(GameData gameData, Sprite sprite, float mov) {
        int repeats = 2 + (int) (((gameData.getTileSize() * gameData.getMapWidth()) / sprite.getWidth()) * mov);
        for (int i = -1; i < repeats; i++) {
            sprite.setX(i * sprite.getWidth() - gameData.getCameraX() * mov);
            sprite.draw(batch);
        }
    }
}
