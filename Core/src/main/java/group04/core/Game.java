/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package group04.core;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.utils.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.openide.util.Lookup;
import group04.common.Entity;
import group04.common.EntityType;
import group04.common.GameData;
import group04.common.World;
import group04.common.services.IServiceInitializer;
import group04.common.services.IServiceProcessor;

/**
 *
 * @author Magnus
 */
public class Game implements ApplicationListener {

    private World world;
    private GameData gameData;
    private OrthographicCamera cam;
    private Map<String, Animation> animations = new HashMap<>();
    private Map<String, Sprite> images = new HashMap<>();
    SpriteBatch batch;
    ShapeRenderer sr;

    public Game() {

    }

    @Override
    public void create() {
        world = new World();
        gameData = new GameData();
        gameData.setDisplayWidth(Gdx.graphics.getWidth());
        gameData.setDisplayHeight(Gdx.graphics.getHeight());
        gameData.setTileSize(16);
        gameData.setMapWidth(gameData.getDisplayWidth() / gameData.getTileSize() * 2);
        gameData.setMapHeight(gameData.getDisplayHeight() / gameData.getTileSize());

        cam = new OrthographicCamera(gameData.getDisplayWidth(), gameData.getDisplayHeight());
        cam.translate(gameData.getDisplayWidth() / 2, gameData.getDisplayHeight() / 2);
        cam.update();

        batch = new SpriteBatch();
        sr = new ShapeRenderer();

        addPlayer();
        addEnemy("Enemy");
        addBase();
        addWeapons();
        addEnvironment();

        for (IServiceInitializer i : Lookup.getDefault().lookupAll(IServiceInitializer.class)) {
            i.start(gameData, world);
        }

        Gdx.input.setInputProcessor(
                new InputController(gameData)
        );
    }

    @Override
    public void render() {
        update();
        
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        drawBackground(gameData, world);
        drawSprites(gameData, world);
        drawHealthBars(gameData, world);
        drawForeground(gameData, world);
    }

    private void update() {
        gameData.getKeys().update();
        gameData.setDelta(Gdx.graphics.getDeltaTime());
        gameData.setMouseX(Gdx.input.getX());
        gameData.setMouseY(gameData.getDisplayHeight() - Gdx.input.getY());

        for (IServiceProcessor e : Lookup.getDefault().lookupAll(IServiceProcessor.class)) {
            e.process(gameData, world);
        }

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

    private void drawHealthBars(GameData gameData, World world) {
        sr.setAutoShapeType(true);
        sr.begin(ShapeRenderer.ShapeType.Filled);
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
        sr.end();
    }

    public void makeAnimation(String animationName, Texture spriteSheet, int spriteSizeX, int spriteSizeY, int frameDuration) {
        Array<TextureRegion> keyFrames = new Array<TextureRegion>();
        int numberOfSprites = (int) (spriteSheet.getWidth() / spriteSizeX);
        for (int i = 0; i < numberOfSprites; i++) {
            TextureRegion sprite = new TextureRegion(spriteSheet);
            sprite.setRegion(i * spriteSizeX, 0, spriteSizeX, spriteSizeY);
            keyFrames.add(sprite);
        }

        animations.put(animationName, new Animation<TextureRegion>(frameDuration, keyFrames));
    }

    private void drawSprites(GameData gameData, World world) {
        batch.begin();
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
            drawSprite(gameData, world, entity, images.get(entity.getSprite()), false);
        }

        batch.end();
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

    private void drawBackground(GameData gameData, World world) {
        sr.begin(ShapeRenderer.ShapeType.Filled);
        sr.setColor(new Color(0, 138f / 255f, 1f, 1));
        sr.rect(0, 0, gameData.getDisplayWidth(), gameData.getDisplayWidth());
        sr.end();
        batch.begin();
        drawBackground(gameData, world, images.get("back4"), back4m);
        drawBackground(gameData, world, images.get("back3"), back3m);
        drawBackground(gameData, world, images.get("back2"), back2m);
        batch.end();
    }

    private void drawForeground(GameData gameData, World world) {
        batch.begin();
        drawBackground(gameData, world, images.get("back1"), back1m);
        batch.end();
    }

    private float playerX = 0;

    private void drawBackground(GameData gameData, World world, Sprite sprite, float mov) {
        for (Entity player : world.getEntities(EntityType.PLAYER)) {
            playerX = player.getX();
        }

        int repeats = 2 + (int) (((gameData.getTileSize() * gameData.getMapWidth()) / sprite.getWidth()) * mov);
        for (int i = -1; i < repeats; i++) {
            sprite.setX(i * sprite.getWidth() - gameData.getCameraX() * mov);
            sprite.draw(batch);
        }

    }

    private void drawMap(GameData gameData, World world) {
        batch.begin();
        for (Entity map : world.getEntities(EntityType.MAP)) {
            for (int i = 0; i < gameData.getMapWidth(); i++) {
                for (int j = 0; j < gameData.getMapHeight(); j++) {
                    if (map.getMap()[i][j] == 0) {
                        images.get("sky").setX(gameData.getTileSize() * i - gameData.getCameraX());
                        images.get("sky").setY(gameData.getTileSize() * j - gameData.getCameraY());
                        images.get("sky").draw(batch);
                    } else if (map.getMap()[i][j] == 1) {
                        images.get("grass").setX(gameData.getTileSize() * i - gameData.getCameraX());
                        images.get("grass").setY(gameData.getTileSize() * j - gameData.getCameraY());
                        images.get("grass").draw(batch);
                    }
                }
            }
        }
        batch.end();
    }

    // Animation and Image methods:
    public void addEnemy(String enemyType) {
        //Images:
        Texture tex = new Texture(Gdx.files.internal(enemyType + ".png"));
        images.put(enemyType, new Sprite(tex));
    }

    public void addPlayer() {
        //Images:
        Texture tex = new Texture(Gdx.files.internal("Player.png"));
        images.put("Player", new Sprite(tex));

        //Animations:
        makeAnimation("player_run", new Texture(Gdx.files.internal("player_run.png")), 75, 80, 2);
    }

    public void addWeapons() {
        //Images:
        //Guns:
        Texture tex = new Texture(Gdx.files.internal("gun.png"));
        images.put("gun", new Sprite(tex));

        //Projectiles: 
        tex = new Texture(Gdx.files.internal("bullet.png"));
        images.put("bullet", new Sprite(tex));
    }

    public void addBase() {
        //Images:
        Texture tex = new Texture(Gdx.files.internal("base.png"));
        images.put("base", new Sprite(tex));
    }

    public void addEnvironment() {
        //Images: 
        Texture tex = new Texture(Gdx.files.internal("sky.png"));
        images.put("sky", new Sprite(tex));
        tex = new Texture(Gdx.files.internal("grass.png"));
        images.put("grass", new Sprite(tex));
        tex = new Texture(Gdx.files.internal("back1.png"));
        images.put("back1", new Sprite(tex));
        tex = new Texture(Gdx.files.internal("back2.png"));
        images.put("back2", new Sprite(tex));
        tex = new Texture(Gdx.files.internal("back3.png"));
        images.put("back3", new Sprite(tex));
        tex = new Texture(Gdx.files.internal("back4.png"));
        images.put("back4", new Sprite(tex));
    }
}
