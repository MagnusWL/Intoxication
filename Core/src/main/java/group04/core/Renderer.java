/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package group04.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
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
        loadPNGImages("Enemy", "Player", "gun", "bullet", "base", "sky", "grass", "back1", "back2", "back3", "back4", "sword");
        loadPNGAnimation("player_run", 75, 80);
        loadPNGAnimation("player_idle", 75, 80);
        loadPNGAnimation("player_jump", 75, 80);
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

        batch.begin();
        drawBackground(gameData);
        drawSprites(gameData, world);
        drawAnimations(gameData, world);
        batch.end();

        sr.begin(ShapeType.Filled);
        drawHealthBars(gameData, world);
        sr.end();

        batch.begin();
        drawForeground(gameData);
        drawScore(gameData, world);
        batch.end();
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

        for (Entity entity : world.getEntities(EntityType.WEAPON)) {
            drawSprite(gameData, world, entity, images.get(entity.getSprite()), true);
        }

        for (Entity entity : world.getEntities(EntityType.PROJECTILE)) {
            drawSprite(gameData, world, entity, images.get(entity.getSprite()), false);
        }
    }

    private void drawScore(GameData gameData, World world) {
        for (Entity player : world.getEntities(EntityType.PLAYER)) {
            text.draw(batch, "Drug money: " + Integer.toString(player.getCurrency()), 40, gameData.getDisplayHeight() - 30);
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
        if (flip) {
            if ((entity.getVelocity() < 0 && !sprite.isFlipX()) || (entity.getVelocity() > 0 && sprite.isFlipX())) {
                sprite.flip(true, false);
            }
        }

        sprite.setX(entity.getX() - gameData.getCameraX());
        sprite.setY(entity.getY() - gameData.getCameraY());
        sprite.draw(batch);
    }

    float back1m = 2f;
    float back2m = 1f;
    float back3m = 0.5f;
    float back4m = 0.25f;

    private void clearBackground(GameData gameData) {
        sr.setColor(new Color(0f, 138f / 255f, 1f, 1f));
        sr.rect(0, 0, gameData.getDisplayWidth(), gameData.getDisplayWidth());
    }

    private void drawBackground(GameData gameData) {
        drawBackground(gameData, images.get("back4"), back4m);
        drawBackground(gameData, images.get("back3"), back3m);
        drawBackground(gameData, images.get("back2"), back2m);
    }

    private void drawForeground(GameData gameData) {
        drawBackground(gameData, images.get("back1"), back1m);
    }

    private void drawBackground(GameData gameData, Sprite sprite, float mov) {
        int repeats = 2 + (int) (((gameData.getTileSize() * gameData.getMapWidth()) / sprite.getWidth()) * mov);
        for (int i = -1; i < repeats; i++) {
            sprite.setX(i * sprite.getWidth() - gameData.getCameraX() * mov);
            sprite.draw(batch);
        }
    }
}
