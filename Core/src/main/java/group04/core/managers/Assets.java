/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package group04.core.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FileTextureData;
import group04.common.GameData;
import group04.core.Animation;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.io.FilenameUtils;

/**
 *
 * @author Josan gamle stodder
 */
public final class Assets {

    private static AssetManager manager;
    private static List<AssetDescriptor<Sound>> soundAssets = new ArrayList<AssetDescriptor<Sound>>();
    private static List<Texture> textureAssets = new ArrayList<Texture>();
    private Map<String, Animation> animations = new HashMap<>();
    private Map<String, Sprite> sprites = new HashMap<>();
    private GameData gameData;

    private static Map<String, String> filePaths = new HashMap<>();

    private String directory = new File("../../../").getAbsolutePath();

    public Assets(GameData gameData) {
        addComponentResources();
        this.gameData = gameData;

        manager = new AssetManager();
    }

    public Map<String, String> getFilePaths() {
        return filePaths;
    }

    public Map<String, Sprite> getAllSprites() {
        return sprites;
    }

    public static void load() {
        for (AssetDescriptor<Sound> soundFile : soundAssets) {
            manager.load(soundFile);
        }
        for (Texture texture : textureAssets) {

            String path = ((FileTextureData) texture.getTextureData()).getFileHandle().path();
            manager.load(path, Texture.class);
        }
        textureAssets.clear();
        soundAssets.clear();
    }

    public void addSoundAsset(String soundAssetName) {
        soundAssets.add(new AssetDescriptor<>(soundAssetName, Sound.class));
    }

    public void addImageAsset(Texture textureAsset) {
        String path = ((FileTextureData) textureAsset.getTextureData()).getFileHandle().path();
        String[] filePathSplit = path.split("/");
        String fileName = filePathSplit[filePathSplit.length - 1];
        textureAssets.add(textureAsset);
        filePaths.put(fileName, path);
        sprites.put(fileName, new Sprite(textureAsset));
    }

    public static void dispose() {
        manager.dispose();
    }

    public AssetManager getAssetManager() {
        return manager;
    }

    public void addComponentResources() {
        FileHandle componentsFH = Gdx.files.absolute(directory);
        File components = componentsFH.file();
        for (final File fileEntry : components.listFiles()) {
            try {
                FileHandle folder = Gdx.files.absolute(fileEntry.getPath() + "/src/main/resources/");
                listFilesForFolder(folder.file());
            } catch (Exception e) {
            }
        }

    }

    public void listFilesForFolder(final File folder) {
        for (final File fileEntry : folder.listFiles()) {
            String fileType = FilenameUtils.getExtension(fileEntry.getName());
            if (fileEntry.isDirectory()) {
                listFilesForFolder(fileEntry);
            } else if (fileType.equals("png")) {
                addImageAsset(new Texture(folder.getAbsolutePath() + "/" + fileEntry.getName()));

            } else if (fileType.equals("wav")) {
                addSoundAsset(folder.getAbsolutePath() + "/" + fileEntry.getName());
            }
        }
    }

    public Animation getAnimation(String key) {
        return animations.get(key);
    }
    
    public float getAnimationSpeed(String key) {
        return animations.get(key).getAnimationSpeed();
    }

    public ArrayList<Sprite> getAnimations(String key) {
        return animations.get(key).getSprites();
    }

    public void setAnimations(String key, ArrayList<Sprite> animation) {
        this.animations.get(key).setSprites(animation);
    }
    
    public ArrayList<Sprite> addAnimation(ArrayList<Sprite> keyFrames, boolean flipped, boolean red, String animationName, Texture spriteSheet, int spriteSizeX, int spriteSizeY, float animationSpeed) {
 
        int numberOfSprites = (int) (spriteSheet.getWidth() / spriteSizeX);
        for (int i = 0; i < numberOfSprites; i++) {
            TextureRegion sprite = new TextureRegion(spriteSheet);
            sprite.setRegion(i * spriteSizeX, 0, spriteSizeX, spriteSizeY);
            Sprite s = new Sprite(sprite);
            if(red)
            {
                s.setColor(new Color(1, 0, 0, 0.95f));
            }
            if(flipped)
            {
            s.flip(true, false);
            keyFrames.add(s);
            }
            else
            {         
            keyFrames.add(s);
            }        
    }
        
        return keyFrames;
    }

    public void makeAnimation(String animationName, Texture spriteSheet, int spriteSizeX, int spriteSizeY, float animationSpeed) {
       
        ArrayList<Sprite> keyFrames = addAnimation(new ArrayList<Sprite>(), false, false, animationName,spriteSheet,spriteSizeX,spriteSizeY,animationSpeed);
        ArrayList<Sprite> flipKeyFrames = addAnimation(new ArrayList<Sprite>(), true, false, animationName,spriteSheet,spriteSizeX,spriteSizeY,animationSpeed);
        ArrayList<Sprite> redKeyFrames = addAnimation(new ArrayList<Sprite>(), false, true, animationName,spriteSheet,spriteSizeX,spriteSizeY,animationSpeed);
        ArrayList<Sprite> redFlipKeyFrames = addAnimation(new ArrayList<Sprite>(), true, true, animationName,spriteSheet,spriteSizeX,spriteSizeY,animationSpeed);
        

        animations.put(animationName, new Animation(keyFrames, spriteSizeX, spriteSizeY, animationSpeed));
        animations.put(animationName.substring(0, animationName.length() - 4) + "_flipped.png", new Animation(flipKeyFrames, spriteSizeX, spriteSizeY, animationSpeed));
        animations.put(animationName.substring(0, animationName.length() - 4) + "_red.png", new Animation(redKeyFrames, spriteSizeX, spriteSizeY, animationSpeed));
        animations.put(animationName.substring(0, animationName.length() - 4) + "_red_flipped.png", new Animation(redFlipKeyFrames, spriteSizeX, spriteSizeY, animationSpeed));      
    }

    public Sprite getSprites(String key) {
        return sprites.get(key);
    }

    public Map<String, Animation> getAllAnimations() {
        return animations;
    }

}
