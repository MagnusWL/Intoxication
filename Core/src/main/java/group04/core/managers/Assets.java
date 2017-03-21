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
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
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
    private static List<AssetDescriptor<Texture>> textureAssets = new ArrayList<AssetDescriptor<Texture>>();
    private Map<String, ArrayList<Sprite>> animations = new HashMap<>();
    private Map<String, ArrayList<Sprite>> animationsFlip = new HashMap<>();

    private AssetsJarFileResolver jfhr;
    private String directory = new File("../../../").getAbsolutePath();

    public Assets() {
        FileHandle components = Gdx.files.internal(directory + "/Data/componentList.txt");

        try {
            System.out.println(directory + components.path());
            FileReader file = new FileReader(components.path());
            BufferedReader reader = new BufferedReader(file);
            String line = reader.readLine();
            while (line != null) {
                System.out.println(line);
                addComponentResources(line);
                line = reader.readLine();
            }
        } catch (Exception e) {
        }
        jfhr = new AssetsJarFileResolver();
        manager = new AssetManager();
    }

    public static void load() {
        for (AssetDescriptor<Sound> soundFile : soundAssets) {
            manager.load(soundFile);
        }
        for (AssetDescriptor<Texture> textureFile : textureAssets) {
            manager.load(textureFile);
        }
    }

    public void addSoundAsset(String soundAssetName) {
        soundAssets.add(new AssetDescriptor<>(soundAssetName, Sound.class));
    }

    public void addImageAsset(String textureAssetName) {
        textureAssets.add(new AssetDescriptor<>(textureAssetName, Texture.class));
        if (textureAssetName.contentEquals("animation")) {
            manager.setReferenceCount(textureAssetName, 1);
        } else {
            manager.setReferenceCount(textureAssetName, 0);
        }

    }

    public static void dispose() {
        manager.dispose();
    }

    public AssetManager getAssetManager() {
        return manager;
    }

    public void addComponentResources(String componentName) {
        FileHandle fh = Gdx.files.absolute(directory + "\\" + componentName);
        String jarUrl = fh.path() + "/target/" + componentName + "-1.0-SNAPSHOT.jar!../src/main/resources/";
        // FileHandle jarFile =  jfhr.resolve(jarUrl); 
        FileHandle folder = Gdx.files.absolute(fh.path() + "/src/main/resources/");
        listFilesForFolder(folder.file());
    }

    public void listFilesForFolder(final File folder) {
        for (final File fileEntry : folder.listFiles()) {
            String fileType = FilenameUtils.getExtension(fileEntry.getName());
            if (fileEntry.isDirectory()) {
                listFilesForFolder(fileEntry);
            } else if (fileType.equals("png")) {
                addImageAsset(fileEntry.getName());
            } else if (fileType.equals("wav")) {
                addSoundAsset(fileEntry.getName());
            }
        }
    }
    public Map<String, ArrayList<Sprite>> getAnimations() {
        return animations;
    }

    public void setAnimations(String key, ArrayList<Sprite> animation){
        this.animations.put(key, animation);
    }

    public Map<String, ArrayList<Sprite>> getAnimationsFlip() {
        return animationsFlip;
    }

    public void setAnimationsFlip(String key, ArrayList<Sprite> animation){
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
        manager.load(animationName, keyFrames.getClass());
        manager.load(animationName + "_flipped", flipKeyFrames.getClass());
    }
     
}
