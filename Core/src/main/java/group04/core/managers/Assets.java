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
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FileTextureData;
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
    private Map<String, ArrayList<Sprite>> animations = new HashMap<>();
    private Map<String, ArrayList<Sprite>> animationsFlip = new HashMap<>();
    private Map<String, Sprite> sprites = new HashMap<>();

    private static Map<String, String> filePaths = new HashMap<>();

    private String directory = new File("../../../").getAbsolutePath();

    public Assets() {
        addComponentResources();

        manager = new AssetManager();
    }

    public Map<String, String> getFilePaths() {
        return filePaths;
    }

    public static void load() {
        for (AssetDescriptor<Sound> soundFile : soundAssets) {
            manager.load(soundFile);
            System.out.println("Loaded Soundfile");
        }
        for (Texture texture : textureAssets) {

            String path = ((FileTextureData) texture.getTextureData()).getFileHandle().path();
            manager.load(path, Texture.class);
            System.out.println("Loaded Texturefile");
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
                System.out.println("Files not found: " + fileEntry.getPath());
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

    public ArrayList<Sprite> getAnimations(String key) {
        return animations.get(key);
    }

    public void setAnimations(String key, ArrayList<Sprite> animation) {
        this.animations.put(key, animation);
    }

    public ArrayList<Sprite> getAnimationsFlip(String key) {
        return animationsFlip.get(key);
    }

    public void setAnimationsFlip(String key, ArrayList<Sprite> animation) {
        this.animations.put(key, animation);
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
        animationsFlip.put(animationName.substring(0, animationName.length() - 4) + "_flipped.png", flipKeyFrames);
        //System.out.println(animationName.substring(0, animationName.length() - 4) + "_flipped.png");
    }

    public Sprite getSprites(String key) {
        return sprites.get(key);
    }

}
