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
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.io.FilenameUtils;

/**
 *
 * @author Josan gamle stodder
 */
public final class Assets {

    private static AssetManager manager;
    private static List<AssetDescriptor<Sound>> soundAssets = new ArrayList<AssetDescriptor<Sound>>();
    private static List<AssetDescriptor<Texture>> textureAssets = new ArrayList<AssetDescriptor<Texture>>();

    String directory = System.getProperty("user.dir");
    private AssetsJarFileResolver jfhr;

    public Assets() {
        FileHandle components = Gdx.files.internal("data/componentList.txt");
        
        try {
            FileReader file = new FileReader(components.path());
            BufferedReader reader = new BufferedReader(file);
            String line = reader.readLine();
            while(line != null)
            {
                addComponentResources(line);
                line = reader.readLine();
            }
        } catch (Exception e) {
        }
        jfhr = new AssetsJarFileResolver();
        manager = new AssetManager(jfhr);
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
    }

    public static void dispose() {
        manager.dispose();
    }

    public AssetManager getAssetManager() {
        return manager;
    }

    public void addComponentResources(String componentName) {
        FileHandle fh = Gdx.files.absolute(directory + "/" + componentName);
        String jarUrl = fh.path() + "/target/Core-1.0-SNAPSHOT.jar!/assets/";
        final FileHandle folder = jfhr.resolve(jarUrl);
        listFilesForFolder(folder.file());
    }

    public void listFilesForFolder(final File folder) {
        for (final File fileEntry : folder.listFiles()) {
            String fileType = FilenameUtils.getExtension(fileEntry.getName());
            if (fileEntry.isDirectory()) {
                listFilesForFolder(fileEntry);
            } else if (fileType.equals("png")) {
                addImageAsset(fileEntry.getAbsolutePath());
            } else if (fileType.equals("wav")) {
                addSoundAsset(fileEntry.getAbsolutePath());
            }
        }
    }
}
